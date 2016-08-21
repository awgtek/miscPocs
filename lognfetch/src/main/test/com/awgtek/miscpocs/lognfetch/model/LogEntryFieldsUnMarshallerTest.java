package com.awgtek.miscpocs.lognfetch.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;

public class LogEntryFieldsUnMarshallerTest {

	JAXBContext jc;
	
	@Before
	public void setUp() throws Exception {
		jc = JAXBContext.newInstance(LogEntryFields.class);
	}

	@Test
	public void testToString() throws Exception {
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		String payload = "<Fields xmlns='lognfetch'>" 
				+ "<Field name='attribute1'>attr 1</Field>"
				+ "<Field name='category'>CAT1</Field>"
				+ "</Fields>";
		LogEntryFields fields = (LogEntryFields) unmarshaller.unmarshal(new StringReader(payload));
		assertEquals("attr 1", fields.getFields().get(0).getContent());
		assertEquals("CAT1", fields.getFields().get(1).getContent());
	}
	
	@Test
	public void testToObject() throws Exception {
		String fieldsMarshalled = getRandomField();
		System.out.println(fieldsMarshalled);
		//assertTrue(fieldsMarshalled.contains("attr 1"));
	}

	private String getRandomField() throws JAXBException, PropertyException {
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		LogEntryFields fields = new LogEntryFields();
		fields.setFields(new ArrayList<>());
		List<LogEntryField> fieldList = new ArrayList<LogEntryField>();
		fieldList.add(new LogEntryField("attribute1", "attr 1 - " + new Random().nextInt(1000)));
		fieldList.add(new LogEntryField("attribute2", "attr 2" + new Random().nextInt(1000)));
		fieldList.add(new LogEntryField("attribute3", "attr 3" + new Random().nextInt(1000)));
		fieldList.add(new LogEntryField("category", "CAT" + (new Random().nextInt(3) + 1)));
		fieldList.add(new LogEntryField("num1", "33" + new Random().nextInt(1000)));
		fieldList.add(new LogEntryField("num2", "55" + new Random().nextInt(1000)));
		int i = new Random().nextInt(fieldList.size());
		fields.getFields().add(fieldList.get(i));
		StringWriter sw = new StringWriter();
		marshaller.marshal(fields, sw);
		String fieldsMarshalled = sw.toString();
		return fieldsMarshalled;
	}

}
