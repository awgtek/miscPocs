package com.awgtek.itemstates.service;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.awgtek.itemstates.domain.ItemState;
import com.awgtek.itemstates.integration.dao.ItemStatesDao;

@Named
public class ItemStatesService {
	private static final Logger logger = LoggerFactory.getLogger(ItemStatesService.class);
	private ItemStatesDao dao;
	
	public ItemStatesService(ItemStatesDao dao) {
		this.dao = dao;
	}

	public String getDesc() {

		logger.debug("getDesc() is executed!");

		return "titel from itemstates service";

	}

	public String getTitle(String name) {

		logger.debug("getTitle() is executed! $name : {}", name);

		if(StringUtils.isEmpty(name)){
			return "Hello World";
		}else{
			return "Hello " + name;
		}
		
	}

    public List<ItemState> list() {
    	return dao.getAllItemStates();
    }
    
	public void saveOrUpdate(ItemState itemState) {
		dao.saveOrUpdate(itemState);
	}

	public void delete(int contactId) {
		// TODO Auto-generated method stub
		
	}

	public ItemState get(int contactId) {
		// TODO Auto-generated method stub
		return null;
	}
}
