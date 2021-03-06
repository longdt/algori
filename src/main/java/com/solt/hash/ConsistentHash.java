/**
 * 
 */
package com.solt.hash;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author thienlong
 * 
 */
public class ConsistentHash<T> {
	private final HashFunction hashFunnction;
	private final int numberOfReplicas;
	private final SortedMap<Integer, T> circle = new TreeMap<Integer, T>();

	/**
	 * 
	 */
	public ConsistentHash(HashFunction hashFunction, int numberOfReplicas,
			Collection<T> nodes) {
		this.hashFunnction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;

		for (T node : nodes) {
			add(node);
		}
	}

	/**
	 * @param node
	 */
	public void add(T node) {
		for (int i = 0; i < numberOfReplicas; ++i) {
			circle.put(hashFunnction.hash(node.toString() + i), node);
		}
	}

	public void remove(T node) {
		for (int i = 0; i < numberOfReplicas; ++i) {
			circle.remove(hashFunnction.hash(node.toString() + i));
		}
	}

	/**
	 * Get the node to store a given key or to retrieve value associate with
	 * this key.
	 * 
	 * @param key
	 * @return
	 */
	public T get(Object key) {
		if (circle.isEmpty()) {
			return null;
		}
		int hash = hashFunnction.hash(key);
		if (!circle.containsKey(hash)) {
			SortedMap<Integer, T> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash);
	}
}
