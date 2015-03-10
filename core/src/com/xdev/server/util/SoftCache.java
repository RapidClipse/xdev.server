
package com.xdev.server.util;


import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * A synchronized cache which uses {@link SoftReference}s.
 *
 * @author XDEV Software Corp.
 */

@SuppressWarnings({"rawtypes","unchecked"})
public class SoftCache<K, V>
{
	public static enum Equality
	{
		REFERENCE
		{
			@Override
			Map createMap()
			{
				return new HashMap<>();
			}
		},

		IDENTITY
		{
			@Override
			Map createMap()
			{
				return new IdentityHashMap<>();
			}
		};

		abstract Map createMap();
	}

	private final Map<K, SoftValue<K, V>>	map;
	private final ReferenceQueue			queue;


	public SoftCache()
	{
		this(Equality.REFERENCE);
	}


	public SoftCache(final Equality equality)
	{
		this.map = Collections.synchronizedMap(equality.createMap());
		this.queue = new ReferenceQueue();
	}


	public SoftCache(final Map<K, V> data)
	{
		this(data,Equality.REFERENCE);
	}


	public SoftCache(final Map<K, V> data, final Equality equality)
	{
		this(equality);

		for(final Entry<K, V> entry : data.entrySet())
		{
			final K key = entry.getKey();
			final V value = entry.getValue();
			this.map.put(key,new SoftValue(value,key,this.queue));
		}
	}


	public void put(final K key, final V value)
	{
		sweep();
		this.map.put(key,new SoftValue(value,key,this.queue));
	}


	public V get(final K key)
	{
		final SoftValue<K, V> ref = this.map.get(key);
		if(ref == null)
		{
			return null;
		}

		final V value = ref.get();
		if(value == null)
		{
			this.map.remove(key);
		}
		return value;
	}


	public boolean containsKey(final K key)
	{
		return this.map.containsKey(key);
	}


	public void remove(final K key)
	{
		sweep();
		this.map.remove(key);
	}


	public void clear()
	{
		sweep();
		this.map.clear();
	}


	public List<V> clearAndGetRetainedValues()
	{
		final List<V> values = values();

		clear();

		return values;
	}


	public K[] keys(final K[] array)
	{
		return this.map.keySet().toArray(array);
	}


	public Iterator<K> keyIterator()
	{
		return this.map.keySet().iterator();
	}


	public List<V> values()
	{
		final List<V> list = new ArrayList();
		for(final Object key : this.map.keySet().toArray())
		{
			final V v = get((K)key);
			if(v != null)
			{
				list.add(v);
			}
		}
		return list;
	}


	public int size()
	{
		sweep();
		return this.map.size();
	}


	private void sweep()
	{
		SoftValue entry;
		while((entry = (SoftValue)this.queue.poll()) != null)
		{
			this.map.remove(entry.key);
		}
	}


	public Map<K, V> toMap()
	{
		final Map<K, V> hard = new HashMap<K, V>();
		for(final Map.Entry<K, SoftValue<K, V>> entry : this.map.entrySet())
		{
			final V value = entry.getValue().get();
			if(value != null)
			{
				hard.put(entry.getKey(),value);
			}
		}
		return hard;
	}



	private static class SoftValue<K, V> extends SoftReference<V>
	{
		final K	key;


		SoftValue(final V value, final K key, final ReferenceQueue q)
		{
			super(value,q);
			this.key = key;
		}
	}
}
