/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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
 *
 * For further information see
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.security;


import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;


public final class LockedMap<K, V> implements Map<K, V>
{
	// /////////////////////////////////////////////////////////////////////////
	// static methods //
	// /////////////////

	public static <K, V> LockedMap<K, V> New(final Map<K, V> subject, final Object lock)
	{
		return new LockedMap<>(subject,lock);
	}

	// /////////////////////////////////////////////////////////////////////////
	// instance fields //
	// //////////////////

	// (19.06.2015 TM)NOTE: securing of keySet etc. ignored intentionally for
	// now.
	private final Map<K, V>	subject;
	private final Object	lock;


	// /////////////////////////////////////////////////////////////////////////
	// constructors //
	// ///////////////

	LockedMap(final Map<K, V> subject, final Object lock)
	{
		super();
		this.subject = subject;
		this.lock = lock;
	}


	// /////////////////////////////////////////////////////////////////////////
	// override methods //
	// ///////////////////

	@Override
	public int size()
	{
		synchronized(this.lock)
		{
			return this.subject.size();
		}
	}


	@Override
	public boolean isEmpty()
	{
		synchronized(this.lock)
		{
			return this.subject.isEmpty();
		}
	}


	@Override
	public boolean containsKey(final Object key)
	{
		synchronized(this.lock)
		{
			return this.subject.containsKey(key);
		}
	}


	@Override
	public boolean containsValue(final Object value)
	{
		synchronized(this.lock)
		{
			return this.subject.containsValue(value);
		}
	}


	@Override
	public V get(final Object key)
	{
		synchronized(this.lock)
		{
			return this.subject.get(key);
		}
	}


	@Override
	public V put(final K key, final V value)
	{
		synchronized(this.lock)
		{
			return this.subject.put(key,value);
		}
	}


	@Override
	public V remove(final Object key)
	{
		synchronized(this.lock)
		{
			return this.subject.remove(key);
		}
	}


	@Override
	public void putAll(final Map<? extends K, ? extends V> m)
	{
		synchronized(this.lock)
		{
			this.subject.putAll(m);
		}
	}


	@Override
	public void clear()
	{
		synchronized(this.lock)
		{
			this.subject.clear();
		}
	}


	@Override
	public Set<K> keySet()
	{
		synchronized(this.lock)
		{
			return this.subject.keySet();
		}
	}


	@Override
	public Collection<V> values()
	{
		synchronized(this.lock)
		{
			return this.subject.values();
		}
	}


	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		synchronized(this.lock)
		{
			return this.subject.entrySet();
		}
	}


	@Override
	public boolean equals(final Object o)
	{
		synchronized(this.lock)
		{
			return this.subject.equals(o);
		}
	}


	@Override
	public int hashCode()
	{
		synchronized(this.lock)
		{
			return this.subject.hashCode();
		}
	}


	@Override
	public V getOrDefault(final Object key, final V defaultValue)
	{
		synchronized(this.lock)
		{
			return this.subject.getOrDefault(key,defaultValue);
		}
	}


	@Override
	public void forEach(final BiConsumer<? super K, ? super V> action)
	{
		synchronized(this.lock)
		{
			this.subject.forEach(action);
		}
	}


	@Override
	public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function)
	{
		synchronized(this.lock)
		{
			this.subject.replaceAll(function);
		}
	}


	@Override
	public V putIfAbsent(final K key, final V value)
	{
		synchronized(this.lock)
		{
			return this.subject.putIfAbsent(key,value);
		}
	}


	@Override
	public boolean remove(final Object key, final Object value)
	{
		synchronized(this.lock)
		{
			return this.subject.remove(key,value);
		}
	}


	@Override
	public boolean replace(final K key, final V oldValue, final V newValue)
	{
		synchronized(this.lock)
		{
			return this.subject.replace(key,oldValue,newValue);
		}
	}


	@Override
	public V replace(final K key, final V value)
	{
		synchronized(this.lock)
		{
			return this.subject.replace(key,value);
		}
	}


	@Override
	public V computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction)
	{
		synchronized(this.lock)
		{
			return this.subject.computeIfAbsent(key,mappingFunction);
		}
	}


	@Override
	public V computeIfPresent(final K key,
			final BiFunction<? super K, ? super V, ? extends V> remappingFunction)
	{
		synchronized(this.lock)
		{
			return this.subject.computeIfPresent(key,remappingFunction);
		}
	}


	@Override
	public V compute(final K key,
			final BiFunction<? super K, ? super V, ? extends V> remappingFunction)
	{
		synchronized(this.lock)
		{
			return this.subject.compute(key,remappingFunction);
		}
	}


	@Override
	public V merge(final K key, final V value,
			final BiFunction<? super V, ? super V, ? extends V> remappingFunction)
	{
		synchronized(this.lock)
		{
			return this.subject.merge(key,value,remappingFunction);
		}
	}

}
