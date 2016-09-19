package com.awgtek.itemshop.itemstates.presentation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.awgtek.itemstates.integration.dao.CassandraItemStatesDao;
import com.awgtek.itemstates.integration.dao.ItemStatesDao;

@Configuration
@ComponentScan({"com.awgtek.itemshop.itemstates.presentation.service"})
public class AppRootConfig {

	@Bean
	public ItemStatesDao getItemStatesDAO() {
	//	return new MockItemStatesDao(getDataSource());
		return new CassandraItemStatesDao();
	}

}
