package com.awgtek.itemstates.integration.dao;

import java.util.ArrayList;
import java.util.List;

import com.awgtek.itemstates.domain.ItemState;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class CassandraItemStatesDao implements ItemStatesDao {

	private Cluster cluster;
	private Session session;

	@Override
	public List<ItemState> getAllItemStates() {
		List<ItemState> itemStates = new ArrayList<>();
		connect();
		session.execute("USE itemshop");
		ResultSet results = session
				.execute("SELECT name, description FROM itemstates");
		for (Row row : results) {
			itemStates.add(new ItemState(row.getString("name"), row.getString("description")));
		}
		cluster.close();
		return itemStates;
	}

	@Override
	public void saveOrUpdate(ItemState itemState) {
		connect();
		session.execute("USE itemshop");
		session.execute("INSERT INTO itemstates (name, description) VALUES ( '"
				+ itemState.getName() + "','" + itemState.getDescription() + "') IF NOT EXISTS");
		
		cluster.close();
	}

	private void connect() {
		cluster = Cluster.builder().addContactPoint("192.168.1.102").build();
		session = cluster.connect();

		session.execute("CREATE KEYSPACE IF NOT EXISTS itemshop WITH replication "
				+ "= {'class':'SimpleStrategy', 'replication_factor':2};");
		session.execute("CREATE TABLE IF NOT EXISTS itemshop.itemstates (name text, "
				+ "description text, PRIMARY KEY (name))");
		//session.execute("CREATE INDEX IF NOT EXISTS nameIndex on itemshop.itemstates (name)");

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
