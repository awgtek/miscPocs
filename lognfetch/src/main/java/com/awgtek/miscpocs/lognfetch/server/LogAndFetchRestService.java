package com.awgtek.miscpocs.lognfetch.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.stream.Stream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.awgtek.miscpocs.lognfetch.model.LogEntry;

@Path("/lognfetch")
public class LogAndFetchRestService {
	Logger logger = LoggerFactory.getLogger(LogAndFetchRestService.class);	
	java.nio.file.Path FILE_PATH = Paths.get("C:/deleteCOB", "logentries.txt");
	
	@GET
	@Path("/{logid}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public LogEntry fetch(@PathParam("logid") String logid) {
		LogEntry logEntry = findLogEntryInLogFileForGivenLogId(logid);
		return logEntry;
	}

	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_XML)
	public Response fetch() {
		String output = "{'blah':'foo'}";
		return Response.status(200).entity(output).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response log(LogEntry logEntry) {
		long logid = 0L;
		logid = findLogIdOfLastLine();
		serializeLogEntryToLogFile(logEntry, logid);
		String response = "Logentry saved: " + logid;
		logger.debug("saving logentry {}", logEntry); 
		return Response.status(Status.CREATED).entity(response).build();
	}

	private void serializeLogEntryToLogFile(LogEntry logEntry, long logid) {
		String serializedLogEntry = null;
		ObjectMapper om = new ObjectMapper();
		try {
			serializedLogEntry = om.writeValueAsString(logEntry);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (BufferedWriter bufWriter = Files.newBufferedWriter(FILE_PATH, StandardCharsets.UTF_8, 
				StandardOpenOption.WRITE, 
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE)) {
			PrintWriter out = new PrintWriter(bufWriter, true);
	        out.println(logid + " " + serializedLogEntry);
	        out.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	private long findLogIdOfLastLine() {
		long logid = 0;
		if (Files.exists(FILE_PATH)) {
			try {
			    Stream<String> lines = Files.lines(FILE_PATH);
			    Optional<String> lastLine = lines.filter(l-> StringUtils.isNotBlank(l))
			    		.reduce((a, b) -> b); //get last element
			    if(lastLine.isPresent()){
			    	String lastItem = lastLine.get();
			    	String logIdInLine = lastItem.substring(0, lastItem.indexOf('{')).trim();
			        logid = Long.parseLong(logIdInLine) + 1;
			    }
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
		return logid;
	}
	
	private LogEntry findLogEntryInLogFileForGivenLogId(String logid) {
		LogEntry logEntry = null;
		if (Files.exists(FILE_PATH)) {
			try {
			    Stream<String> lines = Files.lines(FILE_PATH);
			    Optional<String> containsJava = lines.filter(l->l.startsWith(logid + " ")).findFirst();
			    if(containsJava.isPresent()){
			        String serializedLogEntry = containsJava.get();
			        serializedLogEntry = serializedLogEntry.substring((logid + " ").length());
			        logEntry = new ObjectMapper().readValue(new StringReader(serializedLogEntry), LogEntry.class);
			    }
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}
		return logEntry;
	}

}
