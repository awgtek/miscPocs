package com.awgtek.miscpocs.lognfetch.client;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.junit.Test;

import com.awgtek.miscpocs.lognfetch.model.LogEntryField;
import com.awgtek.miscpocs.lognfetch.model.LogEntryFields;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

public class TestThreads {

	public void sendPosts() throws Exception, ClientHandlerException, PropertyException, JAXBException {
		Client client = Client.create();
		WebResource webResource = client
				.resource("http://localhost:8080/lognfetch/logapi");
//		String input = "{\"attribute1\":\"testStr1\",\"category\":\"CAT1\",\"num1\":\"1\"}";
//		String payload = "<Fields xmlns='lognfetch'>" 
//				+ "<Field name='attribute1'>attr 1</Field>"
//				+ "<Field name='category'>CAT1</Field>"
//				+ "</Fields>";

		ClientResponse response = webResource.type("application/xml")
				.post(ClientResponse.class, getRandomField());

		String output = response.getEntity(String.class);
		System.out.println(output);
		
	}
	
	public static void main(String ...string) throws ClientHandlerException, PropertyException, JAXBException, Exception {
		new TestThreads().sendPosts();
	}
	

	private String getRandomField() throws JAXBException, PropertyException {
		JAXBContext jc = JAXBContext.newInstance(LogEntryFields.class);
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
