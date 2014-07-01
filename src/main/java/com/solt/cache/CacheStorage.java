package com.solt.cache;


public interface CacheStorage<E extends Persistentable> extends Persistentable {

	public E get(String key);

	public E put(String key, E value);

	public E remove(String key);
	
	public void removeAndClose(String key);

	public boolean contains(String key);

	public int getMaximumSize();

	public void setMaximumSize(int maxSize);

	public int size();
}