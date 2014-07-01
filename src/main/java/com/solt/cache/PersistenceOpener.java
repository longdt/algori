/**
 * 
 */
package com.solt.cache;

/**
 * @author thienlong
 *
 */
public interface PersistenceOpener<E extends Persistentable> {
	public E open(String persistenceName);
}
