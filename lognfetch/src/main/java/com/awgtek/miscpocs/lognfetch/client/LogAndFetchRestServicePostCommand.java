package com.awgtek.miscpocs.lognfetch.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.awgtek.miscpocs.lognfetch.model.LogEntry;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class LogAndFetchRestServicePostCommand extends HystrixCommand<String> {
	Logger logger = LoggerFactory.getLogger(LogAndFetchRestServicePostCommand.class);	

	LogEntry logEntry;
	
	protected LogAndFetchRestServicePostCommand(LogEntry logEntry) {
		super(HystrixCommandGroupKey.Factory.asKey("postCommand"), 9999);
		this.logEntry = logEntry;
	}

	@Override
	protected String run() throws Exception {
		LogEntry localLogEntry = LogEntry.getThreadLocalInstance();
		localLogEntry.getHystrixDataSent().add(logEntry.toString());
		logger.debug("added current logentry to localLogEntry which is now {} with added entry {}", 
				localLogEntry, localLogEntry.getHystrixDataSent());
		Client client = Client.create();
		WebResource webResource = client
				.resource("http://localhost:8080/lognfetch/jaxrslog/lognfetch");
		ClientResponse clientResponse = webResource.type("application/json").post(ClientResponse.class, 
				logEntry);
		
		String output = clientResponse.getEntity(String.class);
		logger.debug("sent post. response {}", output);

		return output;
	}

}
