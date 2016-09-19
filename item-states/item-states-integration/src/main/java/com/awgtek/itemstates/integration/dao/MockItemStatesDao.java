package com.awgtek.itemstates.integration.dao;

import java.util.List;

import com.awgtek.itemstates.domain.ItemState;

public class MockItemStatesDao implements ItemStatesDao {

	private Object mockDataSource;
	
	public MockItemStatesDao(Object mockDataSource) {
		super();
		this.mockDataSource = mockDataSource;
	}

	@Override
	public List<ItemState> getAllItemStates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOrUpdate(ItemState itemState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int itemStateId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ItemState get(int itemStateId) {
		// TODO Auto-generated method stub
		return null;
	}

}
