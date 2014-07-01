package com.solt.cache;

import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;


public class AdaptiveCacheStorage<E extends Persistentable> implements
		CacheStorage<E> {
	private ReentrantReadWriteLock lock;
	private ReadLock rlock;
	private WriteLock wlock;
	private PersistenceOpener<E> opener;
	private LRUCache<String, E> onceAccess;
	private LinkedList<String> onceGhost;
	private LRUCache<String, E> twiceAccess;
	private LinkedList<String> twiceGhost;
	private int p = 0;

	private int maxSize;
	private int doubleSize;

	/**
	 * 
	 */
	public AdaptiveCacheStorage(int maxSize, PersistenceOpener<E> opener) {
		this.opener = opener;
		this.maxSize = maxSize;
		doubleSize = 2 * maxSize;
		onceAccess = new LRUCache<String, E>();
		twiceAccess = new LRUCache<String, E>();
		onceGhost = new LinkedList<String>();
		twiceGhost = new LinkedList<String>();
		lock = new ReentrantReadWriteLock();
		rlock = lock.readLock();
		wlock = lock.writeLock();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vn.vccorp.cache.CacheStorage#put(java.lang.String,
	 * vn.vccorp.db.Persistentable)
	 */
	@Override
	public E put(String key, E value) {
		wlock.lock();
		try {
			if (onceAccess.size() == maxSize) {
				Entry<String, E> eldest = onceAccess.getEldestEntry();
				onceAccess.remove(eldest.getKey());
				eldest.getValue().close();
			}
			return onceAccess.put(key, value);
		} finally {
			wlock.unlock();
		}
	}

	public E get(String key) {
		wlock.lock();
		try {
			E item = onceAccess.remove(key);
			if (item != null) {
				twiceAccess.put(key, item);
				return item;
			} else if ((item = (E) twiceAccess.get(key)) != null) {
				return item;
			} else if (onceGhost.contains(key)) {
				int alpha = onceGhost.size() >= twiceGhost.size() ? 1
						: (twiceGhost.size() / onceGhost.size());
				p = Math.min(p + alpha, maxSize);
				replace(key, p);
				onceGhost.remove(key);
				item = opener.open(key);
				twiceAccess.put(key, item);
				return item;
			} else if (twiceGhost.contains(key)) {
				int alpha = twiceGhost.size() >= onceGhost.size() ? 1
						: onceGhost.size() / twiceGhost.size();
				p = Math.max(p - alpha, 0);
				replace(key, p);
				twiceGhost.remove(key);
				item = opener.open(key);
				twiceAccess.put(key, item);
				return item;
			}
			int oneSize = onceAccess.size() + onceGhost.size();
			int totalSize = oneSize + twiceAccess.size() + twiceGhost.size();
			if (oneSize == maxSize) {
				if (onceAccess.size() < maxSize) {
					onceGhost.poll();
					replace(key, p);
				} else {
					Entry<String, E> eldest = onceAccess.getEldestEntry();
					onceAccess.remove(eldest.getKey());
					eldest.getValue().close();
				}
			} else if (oneSize < maxSize && totalSize >= maxSize) {
				if (totalSize == doubleSize) {
					twiceGhost.poll();
				}
				replace(key, p);
			}
			item = opener.open(key);
			onceAccess.put(key, item);
			return item;
		} finally {
			wlock.unlock();
		}
	}

	/**
	 * @param key
	 * @param p2
	 */
	private void replace(String key, int p) {
		if (!onceAccess.isEmpty()
				&& (onceAccess.size() > p || (twiceGhost.contains(key) && onceAccess
						.size() == p))) {
			moveToGhostCache(onceAccess, onceGhost);
		} else {
			moveToGhostCache(twiceAccess, twiceGhost);
		}
	}

	private void moveToGhostCache(LRUCache<String, E> cache,
			LinkedList<String> ghostCache) {
		Entry<String, E> eldest = cache.getEldestEntry();
		String eldestKey = eldest.getKey();
		cache.remove(eldestKey);
		eldest.getValue().close();
		ghostCache.remove(eldestKey);
		ghostCache.add(eldestKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vn.vccorp.cache.CacheStorage#contains(java.lang.String)
	 */
	@Override
	public boolean contains(String key) {
		rlock.lock();
		try {
			return (onceAccess.containsKey(key) || twiceAccess.containsKey(key));
		} finally {
			rlock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vn.vccorp.cache.CacheStorage#getMaximumSize()
	 */
	@Override
	public int getMaximumSize() {
		rlock.lock();
		try {
			return maxSize;
		} finally {
			rlock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vn.vccorp.cache.CacheStorage#remove(java.lang.String)
	 */
	@Override
	public E remove(String key) {
		wlock.lock();
		try {
			E item = onceAccess.remove(key);
			if (item == null) {
				item = twiceAccess.remove(key);
			}
			return item;
		} finally {
			wlock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vn.vccorp.cache.CacheStorage#setMaximumSize(int)
	 */
	@Override
	public void setMaximumSize(int maxSize) {
		wlock.lock();
		try {
			this.maxSize = maxSize;
		} finally {
			wlock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vn.vccorp.cache.CacheStorage#size()
	 */
	@Override
	public int size() {
		rlock.lock();
		try {
			return onceAccess.size() + twiceAccess.size();
		} finally {
			rlock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vn.vccorp.db.Persistentable#close()
	 */
	@Override
	public void close() {
		wlock.lock();
		try {
			for (Entry<String, E> entry : onceAccess.entrySet()) {
				entry.getValue().close();
			}
		} finally {
			wlock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vn.vccorp.db.Persistentable#sync()
	 */
	@Override
	public void sync() {
		wlock.lock();
		try {
			for (Entry<String, E> entry : onceAccess.entrySet()) {
				entry.getValue().sync();
			}
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