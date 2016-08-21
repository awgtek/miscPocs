package com.awgtek.miscpocs.lognfetch.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class TestFetch {

	/**
	 * by running this, should see first execution is long compared to subsequent per logid due to caching
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = Client.create();
		for (int i = 1; i<5; i++) {
			for (int j=0; j<5; j++) {
				printLogEntry(client, i);
			}
		}
	}

	private static void printLogEntry(Client client, int logid) {
		WebResource webResource = client
				.resource("http://localhost:8080/lognfetch/logapi?logid=" + logid);
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		String logEntry = response.getEntity(String.class);
		System.out.println(logEntry);
		response.close();
	}

}
