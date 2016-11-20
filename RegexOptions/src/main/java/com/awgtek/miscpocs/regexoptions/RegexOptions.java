package com.awgtek.miscpocs.regexoptions;

import java.util.*;

public class RegexOptions {

	private static final String OPTIONS_DELIMITER = "\000";
    private static final String CONCAT_OPERATOR = "\010";


	public static RegexOptions INSTANCE = new RegexOptions();

	public List<String> extractOptions(String inputRegex) {
        if (!parenthesisAreBalanced(inputRegex)) {
            return Collections.EMPTY_LIST;
        }
		InToPost theTrans = new InToPost(inputRegex);
		String[] options = Postfix.postfixEvaluate(theTrans.doTrans()).split(OPTIONS_DELIMITER);
		return Arrays.asList(options);
	}

	private static class InToPost {
		private Stack theStack;
		private String input;
		private String output = "";

		public InToPost(String in) {
			input = in;
			int stackSize = input.length();
			theStack = new Stack(stackSize);
		}

		public void concatFill() {
			StringBuilder modifiedInput = new StringBuilder();
			for (int j = 0; j < input.length(); j++) {
				char c = input.charAt(j);
				modifiedInput.append(c);
				if (j < input.length() - 1) {
					char d = input.charAt(j + 1);
					if (c != '(' && d != ')' && c != '|' && d != '|') {
						modifiedInput.append(CONCAT_OPERATOR);
					}
				}
			}
			input = modifiedInput.toString();
		}

		public String doTrans() {
			concatFill();
			for (int j = 0; j < input.length(); j++) {
				char ch = input.charAt(j);
				switch (ch) {
				case '|':
					gotOper(ch, 1);
					break;
				case '\010':
					gotOper(ch, 2);
					break;
				case '(':
					theStack.push(ch);
					break;
				case ')':
					gotParen(ch);
					break;
				default:
					output = output + OPTIONS_DELIMITER + ch;
					break;
				}
			}
			while (!theStack.isEmpty()) {
				output = output + OPTIONS_DELIMITER + theStack.pop();
			}
			return output;
		}

		public void gotOper(char opThis, int prec1) {
			while (!theStack.isEmpty()) {
				char opTop = theStack.pop();
				if (opTop == '(') {
					theStack.push(opTop);
					break;
				} else {
					int prec2;
					if (opTop == '|')
						prec2 = 1;
					else
						prec2 = 2;
					if (prec2 < prec1) {
						theStack.push(opTop);
						break;
					} else
						output = output + OPTIONS_DELIMITER + opTop;
				}
			}
			theStack.push(opThis);
		}

		public void gotParen(char ch) {
			while (!theStack.isEmpty()) {
				char chx = theStack.pop();
				if (chx == '(')
					break;
				else
					output = output + OPTIONS_DELIMITER + chx;
			}
		}

		class Stack {
			private int maxSize;
			private char[] stackArray;
			private int top;

			public Stack(int max) {
				maxSize = max;
				stackArray = new char[maxSize];
				top = -1;
			}

			public void push(char j) {
				stackArray[++top] = j;
			}

			public char pop() {
				return stackArray[top--];
			}

			public char peek() {
				return stackArray[top];
			}

			public boolean isEmpty() {
				return (top == -1);
			}

			@Override
			public String toString() {
				return new String(stackArray);
			}
		}
	}

	private static class Postfix {

		public static String postfixEvaluate(String exp) {
			Stack<String> s = new Stack<String>();
			Scanner tokens = new Scanner(exp).useDelimiter(OPTIONS_DELIMITER);

			while (tokens.hasNext()) {
				if (!tokens.hasNext("\\|") && !tokens.hasNext(CONCAT_OPERATOR)) {
					s.push(tokens.next());
				} else {
					String num2 = s.pop();
					String num1 = s.pop();
					String op = tokens.next();

					if (op.equals("|")) {
						s.push(num1 + OPTIONS_DELIMITER + num2);
					} else if (op.equals(CONCAT_OPERATOR)) {
                        s.push(combine(num1.split(OPTIONS_DELIMITER), num2.split(OPTIONS_DELIMITER)));
					} else {
						throw new IllegalStateException(op + " is not an operator");
					}
				}
			}
			return s.pop();
		}

        private static String combine(String[] left, String[] right) {
            List<String> res = new ArrayList<>();
            for (int i = 0; i < left.length; i++) {
                for (int j = 0; j < right.length; j++) {
                    res.add(left[i] + right[j]);
                }
            }
            return String.join(OPTIONS_DELIMITER, res);
        }
    }

    private static boolean parenthesisAreBalanced(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                char l = stack.pop();
                if (l != '(') {
                    return false;
                }
            }
        }
        return stack.empty();
    }
}
