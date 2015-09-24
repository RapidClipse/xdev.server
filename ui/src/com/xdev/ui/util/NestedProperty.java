/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xdev.ui.util;


public class NestedProperty<K, V> implements KeyValueType<K, V>
{
	private final K			property;
	private final V			value;
	private final Class<V>	type;
	
	
	public NestedProperty(final K property, final V value, final Class<V> type)
	{
		this.property = property;
		this.type = type;
		this.value = value;
	}
	
	
	public static <K, V> KeyValueType<K, V> of(final K key, final V value, final Class<V> type)
	{
		final KeyValueType<K, V> pair = new NestedProperty<>(key,value,type);
		return pair;
	}
	
	
	public static <K, V> KeyValueType<K, V> of(final K key, final Class<V> type)
	{
		final KeyValueType<K, V> pair = new NestedProperty<>(key,null,type);
		return pair;
	}


	public static <K> KeyValueType<K, Object> of(final K key)
	{
		final KeyValueType<K, Object> pair = new NestedProperty<>(key,null,Object.class);
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
