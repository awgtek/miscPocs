package com.awgtek.miscpocs.lognfetch.client;

import java.lang.management.ManagementFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.contrib.servopublisher.HystrixServoMetricsPublisher;
import com.netflix.hystrix.strategy.HystrixPlugins;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;

@WebListener("configures log4j property file")
public class AppInitialization implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String log4jPropertyFilePath = System.getProperty("log4JPropertyFile");
		if (StringUtils.isEmpty(log4jPropertyFilePath)) {
			System.out.println("WARNing: log4j property not set. set system property log4JPropertyFile: " +
					"e.g.:  -Dlog4JPropertyFile=\"C:/Work/workspaces/miscPocs/lognfetch/configs/log4j.properties\"");
		} else {
			PropertyConfigurator.configure(log4jPropertyFilePath);
		}
		//initialize EHCache
		CacheManager cacheManager = CacheManager.newInstance();
		ManagementService.registerMBeans(cacheManager, ManagementFactory.getPlatformMBeanServer(), true, true, true, true);
		
		// Registry plugin with hystrix
		HystrixPlugins.getInstance().registerMetricsPublisher(HystrixServoMetricsPublisher.getInstance());
		
		try {
			CreateAndRegisterMBeanInMBeanServer.main(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
