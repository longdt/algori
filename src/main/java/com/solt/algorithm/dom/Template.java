package com.solt.algorithm.dom;

import java.util.Collection;
import java.util.Map;

public class Template {
	private Map<String, Item> items;
	private boolean missingItem = false;
	
	public Template() {
		
	}
	
	public Template(Map<String, Item> items) {
		this.items = items;
	}
	
	public Item addItem(Item item) {
		String itemName = item.getItemName();
		if (item.isMissing()) {
			missingItem = true;
		}
		return items.put(itemName, item);
	}
	
	public boolean isMissingItem() {
		return missingItem;
	}
	
	public Collection<Item> getItems() {
		return items.values();
	}
}
