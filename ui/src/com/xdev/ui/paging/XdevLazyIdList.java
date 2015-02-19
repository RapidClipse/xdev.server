
package com.xdev.ui.paging;


import java.io.Serializable;
import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.addons.lazyquerycontainer.QueryView;

import com.vaadin.data.Item;


public class XdevLazyIdList<T> extends AbstractList<T> implements Serializable
{
	/**
	 * Java serialization version UID.
	 */
	private static final long			serialVersionUID	= 1L;
	/**
	 * The composite LazyQueryView.
	 */
	private final QueryView				lazyQueryView;
	/**
	 * The ID of the item ID property.
	 */
	private final Object				idPropertyId;
	/**
	 * Map containing index to item ID mapping for IDs already loaded through
	 * this list.
	 */
	private final Map<Object, Integer>	idIndexMap			= new HashMap<Object, Integer>();


	/**
	 * Constructor which sets composite LazyQueryView and ID of the item ID
	 * property.
	 *
	 * @param lazyQueryView
	 *            the LazyQueryView where IDs can be read from.
	 * @param idPropertyId
	 *            Property containing the property ID.
	 */
	public XdevLazyIdList(final QueryView lazyQueryView, final Object idPropertyId)
	{
		this.lazyQueryView = lazyQueryView;
		this.idPropertyId = idPropertyId;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size()
	{
		return this.lazyQueryView.size();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized T[] toArray()
	{
		throw new UnsupportedOperationException();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E[] toArray(final E[] a)
	{
		throw new UnsupportedOperationException();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get(final int index)
	{
		if(index < 0 || index >= this.lazyQueryView.size())
		{
			throw new IndexOutOfBoundsException();
		}
		@SuppressWarnings("unchecked")
		final T itemId = (T)this.lazyQueryView.getItem(index).getItemProperty(this.idPropertyId)
				.getValue();
		// Do not put added item ids to id index map and make sure that
		// existing item indexes start from 0 i.e. ignore added items as they
		// are compensated for in indexOf method.
		final int addedItemSize = this.lazyQueryView.getAddedItems().size();
		if(index >= addedItemSize)
		{
			this.idIndexMap.put(itemId,index - addedItemSize);
		}
		return itemId;
	}


	/**
	 * {@inheritDoc}
	 */
	public Integer set(final int index, final Integer element)
	{
		throw new UnsupportedOperationException();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOf(final Object o)
	{
		if(o == null)
		{
			return -1;
		}
		// Brute force added items first. There should only be a few.
		final List<Item> addedItems = this.lazyQueryView.getAddedItems();
		for(int i = 0; i < addedItems.size(); i++)
		{
			if(o.equals(addedItems.get(i).getItemProperty(this.idPropertyId).getValue()))
			{
				return i;
			}
		}
		// Check from mapping cache.
		if(this.idIndexMap.containsKey(o))
		{
			return addedItems.size() + this.idIndexMap.get(o);
		}
		// Switching to brute forcing.
		for(int i = addedItems.size(); i < this.lazyQueryView.size(); i++)
		{
			if(o.equals(this.lazyQueryView.getItem(i).getItemProperty(this.idPropertyId).getValue()))
			{
				return i;
			}
		}
		// Not found.
		return -1;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(final Object o)
	{
		return indexOf(o) != -1;
	}
}
