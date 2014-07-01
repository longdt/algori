/**
 * 
 */
package com.solt.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author thienlong
 * 
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
	private Map.Entry<K, V> eldestEntry;

	/**
	 * 
	 */
	public LRUCache() {
		super(16, 0.75f, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
	 */
	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
		this.eldestEntry = eldest;
		return false;
	}

	/**
	 * @return the removedEldest
	 */
	public Map.Entry<K, V> getEldestEntry() {
		return eldestEntry;
	}
}
