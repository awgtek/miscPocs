package com.awgtek.miscpocs.lognfetch.client;

import com.awgtek.miscpocs.lognfetch.model.LogEntry;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
public class Tester {

	public static void main(String[] args) {
//		ClientConfig clientConfig = new DefaultClientConfig();
//		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create();
		WebResource webResource = client
				.resource("http://localhost:8080/lognfetch/jaxrslog/lognfetch/test");
		ClientResponse response = webResource.accept("text/xml").get(ClientResponse.class);

		String output = response.getEntity(String.class);
		System.out.println(output);
		
		webResource = client
				.resource("http://localhost:8080/lognfetch/jaxrslog/lognfetch/222");
		response = webResource.accept("application/json").get(ClientResponse.class);

		LogEntry logEntry = response.getEntity(LogEntry.class);
		System.out.println(logEntry);
		
		String input = "{\"attribute1\":\"testStr1\",\"category\":\"CAT1\",\"num1\":\"1\"}";
		webResource = client
				.resource("http://localhost:8080/lognfetch/jaxrslog/lognfetch");
		response = webResource.type("application/json").post(ClientResponse.class, input);

		output = response.getEntity(String.class);
		System.out.println(output);
		
		
	}

}
