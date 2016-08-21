package com.awgtek.miscpocs.lognfetch.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LogEntry implements Serializable {

	private static final long serialVersionUID = -1270162051913620150L;
	
	//private static ThreadLocal<LogEntry> logEntryTL = new ThreadLocal<>();
	private static InheritableThreadLocal<LogEntry> logEntryTL = new InheritableThreadLocal<>();
	public enum LogCategory {
		CAT1, CAT2, CAT3
	}

	private String attribute1;
	private String attribute2;
	private String attribute3;
	private int num1;
	private BigDecimal num2;
	private LogCategory category;
	private List<String> hystrixDataSent = new ArrayList<>();
	
	public static LogEntry getThreadLocalInstance() {
		LogEntry logEntry = logEntryTL.get();
		if (logEntry == null) {
			logEntry = new LogEntry();
			logEntryTL.set(logEntry);
		}
		return logEntry;
	}
	
	public void setParameter(String fieldName, String value) {
		for (Field field : LogEntry.class.getDeclaredFields()) {
			if (field.getName().equals(fieldName)) {
				try {
					field.setAccessible(true);
					if (field.getName().equals(fieldName)) {
						if (field.getType().equals(int.class)) {
							field.setInt(this, Integer.valueOf(value));
						} else if (field.getType().equals(BigDecimal.class)){
							field.set(this, new BigDecimal(value));
						} else if (field.getType().equals(LogCategory.class)) {
							field.set(this,  LogCategory.valueOf(value));
						} else {
							field.set(this, value);
						}
					}
					
				} catch (IllegalAccessException iae) {
					iae.printStackTrace();
				}
			}
		}
	}
	
	public String getAttribute1() {
		return attribute1;
	}
	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
	public String getAttribute2() {
		return attribute2;
	}
	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}
	public String getAttribute3() {
		return attribute3;
	}
	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
	public int getNum1() {
		return num1;
	}
	public void setNum1(int num1) {
		this.num1 = num1;
	}
	public BigDecimal getNum2() {
		return num2;
	}
	public void setNum2(BigDecimal num2) {
		this.num2 = num2;
	}
	public LogCategory getCategory() {
		return category;
	}
	public void setCategory(LogCategory category) {
		this.category = category;
	}
	public List<String> getHystrixDataSent() {
		return hystrixDataSent;
	}

	public void setHystrixDataSent(List<String> hystrixDataSent) {
		this.hystrixDataSent = hystrixDataSent;
	}

	@Override
	public String toString() {
		return "LogEntry [attribute1=" + attribute1 + ", attribute2=" + attribute2 + ", attribute3=" + attribute3
				+ ", num1=" + num1 + ", num2=" + num2 + ", category=" + category + "]";
	}
	
	
}
