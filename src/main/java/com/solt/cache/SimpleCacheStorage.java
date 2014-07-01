package com.solt.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class SimpleCacheStorage<E extends Persistentable> implements
		CacheStorage<E> {
	private ReentrantReadWriteLock lock;
	private ReadLock rlock;
	private WriteLock wlock;
	private PersistenceOpener<E> opener;
	protected Map<String, E> cache;
	protected int maxSize;

	/**
	 * 
	 */
	public SimpleCacheStorage(PersistenceOpener<E> opener) {
		cache = new HashMap<String, E>();
		maxSize = Integer.MAX_VALUE;
		this.opener = opener;
		lock = new ReentrantReadWriteLock();
		rlock = lock.readLock();
		wlock = lock.writeLock();
	}

	public E remove(String key) {
		wlock.lock();
		try {
			return cache.remove(key);
		} finally {
			wlock.unlock();
		}
	}

	public boolean contains(String key) {
		rlock.lock();
		try {
			return cache.containsKey(key);
		} finally {
			rlock.unlock();
		}
	}

	public int getMaximumSize() {
		rlock.lock();
		try {
			return maxSize;
		} finally {
			rlock.unlock();
		}
	}

	@Override
	public void setMaximumSize(int maxSize) {
		wlock.lock();
		try {
			this.maxSize = maxSize;
		} finally {
			wlock.unlock();
		}
	}

	public int size() {
		rlock.lock();
		try {
			return cache.size();
		} finally {
			rlock.unlock();
		}
	}

	@Override
	public void close() {
		wlock.lock();
		try {
			for (E e : cache.values()) {
				e.close();
			}
		} finally {
			wlock.unlock();
		}
	}

	@Override
	public void sync() {
		wlock.lock();
		try {
			for (E e : cache.values()) {
				e.sync();
			}
		} finally {
			wlock.unlock();
		}
	}

	public E put(String key, E persistentable) {
		wlock.lock();
		try {
			return cache.put(key, persistentable);
		} finally {
			wlock.unlock();
		}
	}

	public E get(String key) {
		wlock.lock();
		try {
			E item = cache.get(key);
			if (item == null) {
				item = opener.open(key);
				cache.put(key, item);
			}
			return item;
		} finally {
			wlock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vn.vccorp.cache.CacheStorage#removeAndClose(java.lang.String)
	 */
	@Override
	public void removeAndClose(String key) {
		remove(key).close();
	}
}