/**
 * 
 */
package com.solt.cache;

/**
 * @author thienlong
 * 
 */
public interface Persistentable {
	void sync();

	void close();
}
