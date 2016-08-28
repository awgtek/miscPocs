package com.awgtek.miscpocs.lognfetch.client;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.awgtek.miscpocs.lognfetch.model.LogEntry;
import com.awgtek.miscpocs.lognfetch.model.LogEntryField;
import com.awgtek.miscpocs.lognfetch.model.LogEntryFields;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

@WebServlet( urlPatterns = { "/logapi" })
public class ClientServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(ClientServlet.class);	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
		LogEntry logEntry = null;
		String logid = req.getParameter("logid");
		Ehcache cache = CacheManager.getInstance().getEhcache("doGetCache");
		Instant start = Instant.now();
		if (cache.isKeyInCache(logid)) { //inMemoryHits incremented
			Element element = cache.get(logid);
			if (element == null) { //CacheMisses incremented (somehow element is set to null instead of being removed upon invalidation?)
				logger.debug("cache key {} found but element retrieved was null", logid);
			} else {
				logEntry = (LogEntry) cache.get(logid).getObjectValue();
			}
		} 
		if (logEntry == null) {
			logEntry = new LogAndFetchRestServiceGetCommand(logid).execute();
			cache.put(new Element(logid, logEntry));
		}
		
		Instant end = Instant.now();
		logger.debug("doGet duration fetching {}: {}", logid, Duration.between(start, end).toString());
		resp.getWriter().println(logEntry.toString());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		LogEntry logEntry = LogEntry.getThreadLocalInstance();
		populateLogEntryWithRequestFieldData(request, logEntry);
		logger.debug("creating a command to send {}", logEntry);
		String output = new LogAndFetchRestServicePostCommand(logEntry).execute();
		response.getWriter().println("did post - " + output);
	}

	private void populateLogEntryWithRequestFieldData(HttpServletRequest request, LogEntry logEntry)
			throws IOException, ServletException {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(LogEntryFields.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			LogEntryFields fields = (LogEntryFields) unmarshaller.unmarshal(request.getReader());
//			Enumeration<String> paramNames = request.getParameterNames();
//			while (paramNames.hasMoreElements()) {
//				String paramName = paramNames.nextElement();
//				logEntry.setParameter(paramName, request.getParameter(paramName));
//			}
			for (LogEntryField field : fields.getFields()) {
				logEntry.setParameter(field.getName(), field.getContent());
			}
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
}
