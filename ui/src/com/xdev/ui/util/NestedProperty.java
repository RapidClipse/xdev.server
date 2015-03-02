
package com.xdev.ui.util;


public class NestedProperty<K, V> implements KeyValueType<K, V>
{
	private final K			property;
	private final V			value;
	private final Class<V>	type;
	
	
	public NestedProperty(K property, V value, Class<V> type)
	{
		this.property = property;
		this.type = type;
		this.value = value;
	}
	
	
	public static <K, V> KeyValueType<K, V> of(K key, V value, Class<V> type)
	{
		KeyValueType<K, V> pair = new NestedProperty<>(key,value,type);
		return pair;
	}
	
	
	public static <K, V> KeyValueType<K, V> of(K key, Class<V> type)
	{
		KeyValueType<K, V> pair = new NestedProperty<>(key,null,type);
		return pair;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public K getKey()
	{
		return this.property;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public V getValue()
	{
		return this.value;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<V> getType()
	{
		return this.type;
	}
}
