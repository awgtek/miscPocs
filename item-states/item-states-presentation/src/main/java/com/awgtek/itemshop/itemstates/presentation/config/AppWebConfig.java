package com.awgtek.itemshop.itemstates.presentation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.awgtek.itemstates.integration.dao.CassandraItemStatesDao;
import com.awgtek.itemstates.integration.dao.ItemStatesDao;
import com.awgtek.itemstates.integration.dao.MockItemStatesDao;
import com.awgtek.itemstates.service.ItemStatesService;

@EnableWebMvc
@Configuration
@ComponentScan({ "com.awgtek.itemshop.itemstates.presentation.web" })
public class AppWebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("home");
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Bean
	public Object getDataSource() {
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//		dataSource.setUrl("jdbc:mysql://localhost:3306/contactdb");
//		dataSource.setUsername("root");
//		dataSource.setPassword("P@ssw0rd");

		return new Object();
	}
	@Bean
	public ItemStatesService getItemStatesService(ItemStatesDao dao) {
	//	return new MockItemStatesDao(getDataSource());
		return new ItemStatesService(dao);
	}
}
