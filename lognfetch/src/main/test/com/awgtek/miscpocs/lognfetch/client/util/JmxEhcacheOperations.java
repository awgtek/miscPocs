package com.awgtek.miscpocs.lognfetch.client.util;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import net.sf.ehcache.management.CacheMBean;

public class JmxEhcacheOperations {

	public static void clearCache() throws Exception {
	      JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:8600/jmxrmi");
	      JMXConnector connect = JMXConnectorFactory.connect(url);
	      MBeanServerConnection mbsc = connect.getMBeanServerConnection();
	      ObjectName objectName = new ObjectName(
	    		  "net.sf.ehcache:type=Cache,CacheManager=__DEFAULT__,name=doGetCache");
	      CacheMBean  fooProxy = JMX.newMBeanProxy(
	    		  mbsc, objectName, CacheMBean.class);
		 fooProxy.removeAll();
	}

}
