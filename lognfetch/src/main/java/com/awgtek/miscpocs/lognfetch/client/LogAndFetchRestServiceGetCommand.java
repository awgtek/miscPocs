package com.awgtek.miscpocs.lognfetch.client;

import com.awgtek.miscpocs.lognfetch.model.LogEntry;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class LogAndFetchRestServiceGetCommand extends HystrixCommand<LogEntry> {

	private String logid;
	
	protected LogAndFetchRestServiceGetCommand(String logid) {
		super(HystrixCommandGroupKey.Factory.asKey("getCommand"), 9999);	
		this.logid = logid;
	}

	@Override
	protected LogEntry run() throws Exception {
		Client client = Client.create();
		WebResource webResource = client
				.resource("http://localhost:8080/lognfetch/jaxrslog/lognfetch/" + logid);
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

		return response.getEntity(LogEntry.class);

	}

}
