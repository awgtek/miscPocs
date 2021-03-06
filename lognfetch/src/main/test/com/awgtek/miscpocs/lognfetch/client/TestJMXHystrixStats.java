package com.awgtek.miscpocs.lognfetch.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.awgtek.miscpocs.lognfetch.client.util.JmxEhcacheOperations;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class TestJMXHystrixStats {

	@Test
	public void testJmxAttributesAreGettingSetMethod1() throws Exception {
		int iterations = 3;
		int cnt = 0;
		do {
			makeCalls();
			CommandJmxAttributes attributes = getJmxAttributes();
			assertNotNull(attributes.countSuccess);
			assertNotNull(attributes.isCircuitBreakerOpen);
			System.out.println(attributes);
			if (attributes.getLatencyExecuteMean().intValue() < attributes.getLatencyExecutePercentile99().intValue()) {
				System.out.println("Reached sufficient statistics generation point. Breaking at iteration " + cnt);
				break;
			}
		} while (cnt++ < iterations);
		if (cnt > iterations) {
			fail("Never generated sufficient rolling percentiles.");
		}
	}

	@Test
	public void testJmxAttributesAreGettingSetMethod2() throws Exception {
		int iterations = 100;
		int cnt = 0;
		do {
			makeCalls();
			if (percentile99IsGreaterThanMean()) {
				System.out.println("Reached sufficient statistics generation point. Breaking at iteration " + cnt);
				break;
			}
		} while (cnt++ < iterations);
		if (cnt > iterations) {
			fail("Never generated sufficient rolling percentiles.");
		}
	}

	private boolean percentile99IsGreaterThanMean() throws Exception {
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:8600/jmxrmi");
		JMXConnector connect = JMXConnectorFactory.connect(url);
		MBeanServerConnection mbsc = connect.getMBeanServerConnection();
		//String objectName = "com.netflix.servo:*, type=HystrixCommand";
		String objectName = "com.netflix.servo:type=HystrixCommand,*";
//		QueryExp queryExp = Query.and(
//				Query.gt(Query.attr("latencyExecute_percentile_99"), Query.attr("latencyExecute_mean")),
//				Query.eq(Query.attr("isCircuitBreakerOpen"), Query.value("false")));
//		QueryExp queryExp2 = Query.isInstanceOf(Query.value(MonitorMBean.class.getName()));
//		QueryExp queryExp3 = Query.eq(Query.attr("value"), Query.value("false"));
//		queryExp = Query.and(queryExp, Query.isInstanceOf(Query.value(HystrixCommand.class.getName())));
		//Set<ObjectName> results = mbsc.queryNames(new ObjectName(objectName), null);
		Set<ObjectInstance> results = mbsc.queryMBeans(new ObjectName(objectName), null); 
		Iterator<ObjectInstance> iterator = results.iterator();
		Set<String> commandNames = new HashSet<>();
		while (iterator.hasNext()) {
			commandNames.add(iterator.next().getObjectName().getKeyProperty("instance"));
		}
		String attributeObjectName = "com.netflix.servo:name=%s,type=HystrixCommand,instance=%s";
		for (String commandName : commandNames) {
			ObjectName o = new ObjectName(String.format(attributeObjectName,
					"latencyExecute_percentile_99", commandName));
			Number percentile99 = (Number) mbsc.getAttribute(o, "value");
			Number mean = (Number) mbsc.getAttribute(new ObjectName(String.format(attributeObjectName,
					"latencyExecute_mean", commandName)), "value");
			if (percentile99 == null || mean == null) {
				continue;
			}
			if (percentile99.intValue() > mean.intValue()) {
				System.out.println("Found match in " + commandName);
				return true;
			}
		}
		
		return false;
	}

	private CommandJmxAttributes getJmxAttributes() throws Exception {
		CommandJmxAttributes commandJmxAttributes = null;
		Map<String, Object> map = new HashMap<>();
		String commandName = "LogAndFetchRestServiceGetCommand";
		map.put("commandName", commandName);

		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:8600/jmxrmi");
		JMXConnector connect = JMXConnectorFactory.connect(url);
		MBeanServerConnection mbsc = connect.getMBeanServerConnection();
		// ObjectName o = new
		// ObjectName("com.netflix.servo:name=countSuccess,type=HystrixCommand,instance=LogAndFetchRestServicePostCommand");
		String[] attributeNames = { "countSuccess", "isCircuitBreakerOpen", "countFailure", "countFallbackSuccess",
				"latencyExecute_mean", "latencyExecute_percentile_99" };
		for (String attributeName : attributeNames) {
			ObjectName o = new ObjectName(String.format("com.netflix.servo:name=%s,type=HystrixCommand,instance=%s",
					attributeName, commandName));
			Object value = mbsc.getAttribute(o, "value");
			map.put(attributeName, value);
		}
		// commandJmxAttributes.countSuccess = value;
		// o = new ObjectName(
		// "com.netflix.servo:name=isCircuitBreakerOpen,type=HystrixCommand,instance=LogAndFetchRestServiceGetCommand");
		// value = mbsc.getAttribute(o, "value");
		// commandJmxAttributes.isCircuitBreakerOpen = value;
		connect.close();
		final ObjectMapper mapper = new ObjectMapper(); // jackson's
														// objectmapper
		commandJmxAttributes = mapper.convertValue(map, CommandJmxAttributes.class);
		return commandJmxAttributes;
	}

	private void makeCalls() throws Exception {
		Client client = Client.create();
		for (int i = 1; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				getLogEntry(client, i);
				JmxEhcacheOperations.clearCache();
			}
		}
	}

	private String getLogEntry(Client client, int logid) {
		WebResource webResource = client.resource("http://localhost:8080/lognfetch/logapi?logid=" + logid);
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		String logEntry = response.getEntity(String.class);
		response.close();
		return logEntry;
	}

	public static class CommandJmxAttributes {
		private String commandName;
		private String isCircuitBreakerOpen;
		private Number countSuccess;
		private Number countFailure;
		private Number countFallbackSuccess;
		private Number latencyExecuteMean;
		private Number latencyExecutePercentile99;

		public String getCommandName() {
			return commandName;
		}

		public void setCommandName(String commandName) {
			this.commandName = commandName;
		}

		public String getIsCircuitBreakerOpen() {
			return isCircuitBreakerOpen;
		}

		public void setIsCircuitBreakerOpen(String isCircuitBreakerOpen) {
			this.isCircuitBreakerOpen = isCircuitBreakerOpen;
		}

		public Number getCountSuccess() {
			return countSuccess;
		}

		public void setCountSuccess(Number countSuccess) {
			this.countSuccess = countSuccess;
		}

		public Number getCountFallbackSuccess() {
			return countFallbackSuccess;
		}

		public void setCountFallbackSuccess(Number countFallbackSuccess) {
			this.countFallbackSuccess = countFallbackSuccess;
		}

		@JsonProperty("latencyExecute_mean")
		public Number getLatencyExecuteMean() {
			return latencyExecuteMean;
		}

		public void setLatencyExecuteMean(Number latencyExecuteMean) {
			this.latencyExecuteMean = latencyExecuteMean;
		}

		@JsonProperty("latencyExecute_percentile_99")
		public Number getLatencyExecutePercentile99() {
			return latencyExecutePercentile99;
		}

		public void setLatencyExecutePercentile99(Number latencyExecutePercentile99) {
			this.latencyExecutePercentile99 = latencyExecutePercentile99;
		}

		public Number getCountFailure() {
			return countFailure;
		}

		public void setCountFailure(Number countFailure) {
			this.countFailure = countFailure;
		}

		@Override
		public String toString() {
			return "CommandJmxAttributes [commandName=" + commandName + ", isCircuitBreakerOpen=" + isCircuitBreakerOpen
					+ ", countSuccess=" + countSuccess + ", countFailure=" + countFailure + ", countFallbackSuccess="
					+ countFallbackSuccess + ", latencyExecuteMean=" + latencyExecuteMean
					+ ", latencyExecutePercentile99=" + latencyExecutePercentile99 + "]";
		}

	}

}
