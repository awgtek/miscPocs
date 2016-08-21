package com.awgtek.miscpocs.lognfetch.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "content"
})
@XmlRootElement(name = "Field", namespace = "lognfetch")
public class LogEntryField implements Serializable {
	private final static long serialVersionUID = 1L;
    @XmlValue
    protected String content;
    @XmlAttribute(name = "name", required = true)
    protected String name;
	public String getContent() {
		return content;
	}
	public String getName() {
		return name;
	}
	public LogEntryField() {
		
	}
	public LogEntryField(String name, String content) {
		super();
		this.content = content;
		this.name = name;
	}
    
    
}
