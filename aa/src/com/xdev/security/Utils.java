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


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class Utils
{
	public static final <T> T notNull(final T object) throws NullPointerException
	{
		if(object == null)
		{
			throw new NullPointerException();
		}
		return object;
	}


	@SafeVarargs
	public static <K, V> Map<K, V> asMap(final KeyValue<K, V>... keyValues)
	{
		final HashMap<K, V> newMap = new HashMap<>();

		for(final KeyValue<K, V> keyValue : keyValues)
		{
			newMap.put(keyValue.key(),keyValue.value());
		}

		return newMap;
	}


	public static <K, V> Map<K, V> ensureNonNullMap(final Map<K, V> subject)
	{
		return subject != null ? subject : new HashMap<>();
	}


	@SafeVarargs
	public static <E> HashSet<E> HashSet(final E... elements)
	{
		final HashSet<E> newSet = new HashSet<>(elements.length);
		for(final E e : elements)
		{
			newSet.add(e);
		}
		return newSet;
	}

}
