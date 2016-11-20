package com.awgtek.miscpocs.regexoptions;

import spock.lang.Specification

class RegexOptionsSpec extends Specification {


    RegexOptions optionsParser;


    void setup() {
        optionsParser = new RegexOptions()
    }


    def "Test valid three item list"() {
        given:
        String input = '(^a bc$)|(xyz)|(123)'


        when:
        def res = optionsParser.extractOptions(input)


        then:
        res.sort() == ['^a bc$', 'xyz', '123'].sort()
    }


    def "Test valid list with subgroups"() {
        given:
        String input = '(^a bc$)|(x((y)|(z)))|(123)'


        when:
        def res = optionsParser.extractOptions(input)


        then:
        res.sort() == ['^a bc$', 'xy', 'xz', '123'].sort()
    }

    def "Test valid list with or subgroup"() {
        given:
        String input = 'x(y|z)'


        when:
        def res = optionsParser.extractOptions(input)


        then:
        res == ['xy', 'xz']
    }

    def "Test valid list with or two subgroup"() {
        given:
        String input = '(w|x)(y|z)'


        when:
        def res = optionsParser.extractOptions(input)


        then:
        res == ['wy', 'wz', 'xy', 'xz']
    }

    def "Test valid list with or two subgroup and one operand"() {
        given:
        String input = '(w|x)u(y|z)'


        when:
        def res = optionsParser.extractOptions(input)


        then:
        res == ['wuy', 'wuz', 'xuy', 'xuz']
    }

    def "Test valid list with or three subgroups"() {
        given:
        String input = '(u|v)(w|x)(y|z)'

        when:
        def res = optionsParser.extractOptions(input)

        then:
        res == ['uwy', 'uwz', 'uxy', 'uxz', 'vwy', 'vwz', 'vxy', 'vxz']
    }

    def "Test valid three item list grouped and ungrouped"() {
        given:
        String input = 'abc|xyz|(1234 aa)'


        when:
        def res = optionsParser.extractOptions(input)


        then:
        res.sort() == ['abc', 'xyz', '1234 aa'].sort()
    }


    def "Test valid one item list"() {
        given:
        String input = 'xyz'


        when:
        def res = optionsParser.extractOptions(input)


        then:
        res == ['xyz']
    }


    def "Test valid one item list with spaces"() {
        given:
        String input = 'aaa bbb'


        when:
        def res = optionsParser.extractOptions(input)


        then:
        res == ['aaa bbb']
    }


    def "Test invalid input - unbalanced parens"() {
        given:
        String input = '(aaa|bbb'


        when:
        def res = optionsParser.extractOptions(input)


        then:
        res == []
    }
}


