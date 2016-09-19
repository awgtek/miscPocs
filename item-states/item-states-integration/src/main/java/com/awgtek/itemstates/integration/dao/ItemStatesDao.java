package com.awgtek.itemstates.integration.dao;

import java.util.List;

import com.awgtek.itemstates.domain.ItemState;

public interface ItemStatesDao {

	List<ItemState> getAllItemStates();
	
	public void saveOrUpdate(ItemState itemState);
    
    public void delete(int itemStateId);
     
    public ItemState get(int itemStateId);
}
