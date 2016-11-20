package com.awgtek.mvcrestclient;

import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class JerseyClient {
	public String serverData(String url, String msg) {
		try {

			Client client = Client.create();

			WebResource webResource = client.resource(url + msg);

			ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.getEntity(String.class);

			System.out.println("Output from Server .... \n");
			System.out.println(output);
			return output;

		} catch (Exception e) {

			e.printStackTrace();

		}
		return "failure";
	}
}
