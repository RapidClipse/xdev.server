/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xdev.security;


/**
 * @author XDEV Software (TM)
 *
 */
public interface KeyValue<K, V>
{
	public K key();


	public V value();


	public static <K, V> KeyValue<K, V> New(final K key, final V value)
	{
		return new KeyValue.Implementation<>(key,value);
	}



	public final class Implementation<K, V> implements KeyValue<K, V>
	{
		final K	key;
		final V	value;


		public Implementation(final K key, final V value)
		{
			super();
			this.key = key;
			this.value = value;
		}


		@Override
		public K key()
		{
			return this.key;
		}


		@Override
		public V value()
		{
			return this.value;
		}


		/**
		 * @return a String of pattern <code>[<i>key</i> -> <i>value</i>]</code>
		 */
		@Override
		public String toString()
		{
			return '[' + String.valueOf(this.key) + " -> " + String.valueOf(this.value) + ']';
		}

	}

}
