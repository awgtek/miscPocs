package com.awgtek.miscpocs.lognfetch.client;

import java.lang.management.ManagementFactory;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.QueryExp;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.awgtek.miscpocs.lognfetch.client.CreateAndRegisterMBeanInMBeanServer.Hello;

public class CreateAndRegisterMBeanInMBeanServerTest {

	public static void main(String[] args) throws Exception {
	      JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:8600/jmxrmi");
	      JMXConnector connect = JMXConnectorFactory.connect(url);
	      MBeanServerConnection mbsc = connect.getMBeanServerConnection();
		//String objectName = "com.javacodegeeks.snippets.enterprise:type=Hello";
		//String objectName = "com.javacodegeeks.snippets.enterprise:*";
		String objectName = "*:type=Hello";
		
	   // QueryExp exp = Query.eq(Query.attr("Message"), Query.value("Hello World"));
	    QueryExp exp = Query.eq(Query.attr("Message"), Query.value("Hello World"));

	   // Set<ObjectInstance> instances = mbsc.queryMBeans(new ObjectName(objectName), exp);
	    Set<ObjectInstance> instances = mbsc.queryMBeans(new ObjectName(objectName), null);
	    
	    ObjectInstance instance = (ObjectInstance) instances.toArray()[0];
	    
	    System.out.println("Class Name:t" + instance.getClassName());
	    System.out.println("Object Name:t" + instance.getObjectName());

	}

}
