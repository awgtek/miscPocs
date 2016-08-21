package com.awgtek.miscpocs.lognfetch.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Fields", namespace = "lognfetch")
public class LogEntryFields implements Serializable {
	private final static long serialVersionUID = 1L;
	
	@XmlElement(name = "Field", namespace = "lognfetch")
	private List<LogEntryField> fields;

	public List<LogEntryField> getFields() {
		return fields;
	}

	public void setFields(List<LogEntryField> fields) {
		this.fields = fields;
	}
	
}
