
package com.xdev.server.util;


public interface KeyValueType<K, V>
{
	/**
	 * Gets the key from the pair.
	 *
	 * @return the key
	 */
	K getKey();
	
	
	/**
	 * Gets the value from the pair.
	 *
	 * @return the value
	 */
	V getValue();
	
	
	/**
	 * Gets the type from the pair.
	 *
	 * @return the type
	 */
	Class<V> getType();
}
