package com.solt.algorithm.dom;

public class Item {
	private String itemName;
	private String prefix;
	private String suffix;

	public Item() {

	}

	public Item(String itemName, String prefix, String suffix) {
		this.itemName = itemName;
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public boolean isMissing() {
		boolean flag = (prefix == null || prefix.trim().isEmpty())
				|| (suffix == null || suffix.trim().isEmpty());
		return flag;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}
