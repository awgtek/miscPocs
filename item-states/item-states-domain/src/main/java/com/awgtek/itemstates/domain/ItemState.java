package com.awgtek.itemstates.domain;

public class ItemState {

	private String name;
	private String description;
	
	public ItemState() {
	}
	public ItemState(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
