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
 * 
 * For further information see 
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.ui.paging;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.vaadin.addons.lazyquerycontainer.NaturalNumberIdsList;
import org.vaadin.addons.lazyquerycontainer.NestingBeanItem;
import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryFactory;
import org.vaadin.addons.lazyquerycontainer.QueryItemStatus;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.data.util.BeanItem;


//copied from LazyQueryView
//provides addItem(entity)
@SuppressWarnings("unchecked")
public class EntityLazyQueryView implements XdevEntityQueryView, ValueChangeListener, Serializable
{
	/**
	 * Java serialization UID.
	 */
	private static final long			serialVersionUID					= 1L;
																			
	/**
	 * Query count debug property ID.
	 */
	public static final String			DEBUG_PROPERTY_ID_QUERY_INDEX		= "DEBUG_PROPERTY_ID_QUERY_COUT";
	/**
	 * Batch index debug property ID.
	 */
	public static final String			DEBUG_PROPERTY_ID_BATCH_INDEX		= "DEBUG_PROPERTY_ID_BATCH_INDEX";
	/**
	 * Batch query time debug property ID.
	 */
	public static final String			DEBUG_PROPERTY_ID_BATCH_QUERY_TIME	= "DEBUG_PROPERTY_ID_BATCH_QUERY_TIME";
	/**
	 * Item status property ID.
	 */
	public static final String			PROPERTY_ID_ITEM_STATUS				= "PROPERTY_ID_ITEM_STATUS";
	/**
	 * Initial maximum cache size.
	 */
	private static final int			DEFAULT_MAX_CACHE_SIZE				= 1000;
																			
	/**
	 * Maximum items in cache before old ones are evicted.
	 */
	private int							maxCacheSize						= DEFAULT_MAX_CACHE_SIZE;
	/**
	 * Number of query executions.
	 */
	private int							queryCount							= 0;
	/**
	 * Number of batches read.
	 */
	private int							batchCount							= 0;
	/**
	 * QueryDefinition containing query properties and batch size.
	 */
	private QueryDefinition				queryDefinition;
	/**
	 * QueryFactory for constructing new queries when sort state changes.
	 */
	private QueryFactory				queryFactory;
	/**
	 * Currenct query used by view.
	 */
	private Query						query;
	/**
	 * Size of the query.
	 */
	private int							querySize							= -1;
	/**
	 * Property IDs participating in sort.
	 */
	private Object[]					sortPropertyIds;
	/**
	 * Sort state of the properties participating in sort. If true then
	 * ascending else descending.
	 */
	private boolean[]					ascendingStates;
	/**
	 * List of item IDs.
	 */
	private List<?>						itemIdList;
	/**
	 * List of item indexes in cache in order of access.
	 */
	private final LinkedList<Integer>	itemCacheAccessLog					= new LinkedList<Integer>();
	/**
	 * Map of items in cache.
	 */
	private final Map<Integer, Item>	itemCache							= new HashMap<Integer, Item>();
	/**
	 * Map from properties to items for items which are in cache.
	 */
	private Map<Property<?>, Item>		propertyItemMapCache				= new HashMap<>();
																			
	/**
	 * List of added items since last commit/rollback.
	 */
	private final List<Item>			addedItems							= new ArrayList<Item>();
	/**
	 * List of modified items since last commit/rollback.
	 */
	private final List<Item>			modifiedItems						= new ArrayList<Item>();
	/**
	 * List of deleted items since last commit/rollback.
	 */
	private final List<Item>			removedItems						= new ArrayList<Item>();
																			
	private Object[]					requiredProperties;
										
										
	/**
	 * Constructs LazyQueryView with given QueryDefinition and QueryFactory. The
	 * role of this constructor is to enable use of custom QueryDefinition
	 * implementations.
	 *
	 * @param queryDefinition
	 *            The QueryDefinition to be used.
	 * @param queryFactory
	 *            The QueryFactory to be used.
	 */
	public EntityLazyQueryView(final QueryDefinition queryDefinition,
			final QueryFactory queryFactory)
	{
		initialize(queryDefinition,queryFactory);
	}
	
	
	/**
	 * Initializes the LazyQueryView.
	 *
	 * @param queryDefinition
	 *            The QueryDefinition to be used.
	 * @param queryFactory
	 *            The QueryFactory to be used.
	 */
	private void initialize(final QueryDefinition queryDefinition, final QueryFactory queryFactory)
	{
		this.queryDefinition = queryDefinition;
		this.queryFactory = queryFactory;
		this.sortPropertyIds = new Object[0];
		this.ascendingStates = new boolean[0];
	}
	
	
	/**
	 * Gets the QueryDefinition.
	 *
	 * @return the QueryDefinition
	 */
	@Override
	public QueryDefinition getQueryDefinition()
	{
		return this.queryDefinition;
	}
	
	
	/**
	 * Sets new sort state and refreshes view.
	 *
	 * @param sortPropertyIds
	 *            The IDs of the properties participating in sort.
	 * @param ascendingStates
	 *            The sort state of the properties participating in sort. True
	 *            means ascending.
	 */
	@Override
	public void sort(final Object[] sortPropertyIds, final boolean[] ascendingStates)
	{
		this.sortPropertyIds = sortPropertyIds;
		this.ascendingStates = ascendingStates;
		refresh();
	}
	
	
	/**
	 * Refreshes the view by clearing cache, discarding buffered changes and
	 * current query instance. New query is created on demand.
	 */
	@Override
	public void refresh()
	{
		
		for(final Property<?> property : this.propertyItemMapCache.keySet())
		{
			if(property instanceof ValueChangeNotifier)
			{
				final ValueChangeNotifier notifier = (ValueChangeNotifier)property;
				notifier.removeValueChangeListener(this);
			}
		}
		
		this.query = null;
		this.batchCount = 0;
		this.itemIdList = null;
		this.itemCache.clear();
		this.itemCacheAccessLog.clear();
		this.propertyItemMapCache.clear();
		
		discard();
	}
	
	
	/**
	 * Returns the total size of query and added items since last commit.
	 *
	 * @return total number of items in the view.
	 */
	@Override
	public int size()
	{
		return getQuerySize() + this.addedItems.size();
	}
	
	
	/**
	 * Gets the batch size i.e. how many items is fetched at a time from
	 * storage.
	 *
	 * @return the batch size.
	 */
	public int getBatchSize()
	{
		return this.queryDefinition.getBatchSize();
	}
	
	
	/**
	 * @return the maxCacheSize
	 */
	@Override
	public int getMaxCacheSize()
	{
		return this.maxCacheSize;
	}
	
	
	/**
	 * @param maxCacheSize
	 *            the maxCacheSize to set
	 */
	@Override
	public void setMaxCacheSize(final int maxCacheSize)
	{
		this.maxCacheSize = maxCacheSize;
	}
	
	
	/**
	 * Gets item at given index from addedItems, cache and loads new batch on
	 * demand if required.
	 *
	 * @param index
	 *            The item index.
	 * @return the item at given index.
	 */
	@Override
	public Item getItem(final int index)
	{
		if(index < 0 || index >= size())
		{
			throw new IndexOutOfBoundsException(
					"Container size: " + size() + " and item index  requested: " + index);
		}
		final int addedItemCount = this.addedItems.size();
		if(index < addedItemCount)
		{
			// an item from the addedItems was requested
			return this.addedItems.get(index);
		}
		if(!this.itemCache.containsKey(index - addedItemCount))
		{
			// item is not in our cache, ask the query for more items
			queryItem(index - addedItemCount);
		}
		else
		{
			// item is already in our cache
			// refresh cache access log.
			this.itemCacheAccessLog.remove(new Integer(index));
			this.itemCacheAccessLog.addLast(new Integer(index));
		}
		
		return this.itemCache.get(index - addedItemCount);
	}
	
	
	/**
	 * Query item and the surrounding batch of items.
	 *
	 * @param index
	 *            The index of item requested to be queried.
	 */
	private void queryItem(final int index)
	{
		final int batchSize = getBatchSize();
		final int startIndex = index - index % batchSize;
		final int count = Math.min(batchSize,getQuerySize() - startIndex);
		
		final long queryStartTime = System.currentTimeMillis();
		// load more items
		final List<Item> items = getQuery().loadItems(startIndex,count);
		final long queryEndTime = System.currentTimeMillis();
		
		for(int i = 0; i < count; i++)
		{
			final int itemIndex = startIndex + i;
			
			final Item item;
			
			// if(i < items.size())
			// // {
			// // item = getQuery().constructItem();
			// // }
			// // else
			if(i >= items.size())
			{
				item = getQuery().constructItem();
			}
			else
			{
				item = items.get(i);
				
				this.itemCache.put(itemIndex,item);
				
				if(i >= items.size())
				{
					removeItem(itemIndex);
				}
				
				if(this.itemCacheAccessLog.contains(itemIndex))
				{
					this.itemCacheAccessLog.remove((Object)itemIndex);
				}
				this.itemCacheAccessLog.addLast(itemIndex);
			}
		}
		
		for(int i = 0; i < count; i++)
		{
			final int itemIndex = startIndex + i;
			
			if(i < this.itemCache.size())
			{
				final Item item = this.itemCache.get(itemIndex);
				if(item != null)
				{
					if(item.getItemProperty(DEBUG_PROPERTY_ID_BATCH_INDEX) != null)
					{
						item.getItemProperty(DEBUG_PROPERTY_ID_BATCH_INDEX).setReadOnly(false);
						item.getItemProperty(DEBUG_PROPERTY_ID_BATCH_INDEX)
								.setValue(this.batchCount);
						item.getItemProperty(DEBUG_PROPERTY_ID_BATCH_INDEX).setReadOnly(true);
					}
					if(item.getItemProperty(DEBUG_PROPERTY_ID_QUERY_INDEX) != null)
					{
						item.getItemProperty(DEBUG_PROPERTY_ID_QUERY_INDEX).setReadOnly(false);
						item.getItemProperty(DEBUG_PROPERTY_ID_QUERY_INDEX)
								.setValue(this.queryCount);
						item.getItemProperty(DEBUG_PROPERTY_ID_QUERY_INDEX).setReadOnly(true);
					}
					if(item.getItemProperty(DEBUG_PROPERTY_ID_BATCH_QUERY_TIME) != null)
					{
						item.getItemProperty(DEBUG_PROPERTY_ID_BATCH_QUERY_TIME).setReadOnly(false);
						item.getItemProperty(DEBUG_PROPERTY_ID_BATCH_QUERY_TIME)
								.setValue(queryEndTime - queryStartTime);
						item.getItemProperty(DEBUG_PROPERTY_ID_BATCH_QUERY_TIME).setReadOnly(true);
					}
					
					for(final Object propertyId : item.getItemPropertyIds())
					{
						final Property<?> property = item.getItemProperty(propertyId);
						if(property instanceof ValueChangeNotifier)
						{
							final ValueChangeNotifier notifier = (ValueChangeNotifier)property;
							notifier.addValueChangeListener(this);
							this.propertyItemMapCache.put(property,item);
						}
					}
				}
			}
		}
		
		// Increase batch count.
		this.batchCount++;
		
		// Evict items from cache if cache size exceeds max cache size
		int counter = 0;
		while(this.itemCache.size() > this.maxCacheSize)
		{
			final int firstIndex = this.itemCacheAccessLog.getFirst();
			final Item firstItem = this.itemCache.get(firstIndex);
			
			// Remove oldest item in cache access log if it is not modified or
			// removed.
			if(!this.modifiedItems.contains(firstItem) && !this.removedItems.contains(firstItem))
			{
				this.itemCacheAccessLog.removeFirst();
				this.itemCache.remove(firstIndex);
				
				for(final Object propertyId : firstItem.getItemPropertyIds())
				{
					final Property<?> property = firstItem.getItemProperty(propertyId);
					if(property instanceof ValueChangeNotifier)
					{
						final ValueChangeNotifier notifier = (ValueChangeNotifier)property;
						notifier.removeValueChangeListener(this);
						this.propertyItemMapCache.remove(property);
					}
				}
				
			}
			else
			{
				this.itemCacheAccessLog.removeFirst();
				this.itemCacheAccessLog.addLast(firstIndex);
			}
			
			// Break from loop if entire cache has been iterated (all items are
			// modified).
			counter++;
			if(counter > this.itemCache.size())
			{
				break;
			}
		}
	}
	
	
	/**
	 * Get the query size.
	 *
	 * @return the query size
	 */
	private int getQuerySize()
	{
		if(this.query == null)
		{
			getQuery();
		}
		if(this.querySize == -1)
		{
			// Fallback: if setting batch size fails - re request.
			this.querySize = this.query.size();
		}
		return this.querySize;
	}
	
	
	/**
	 * Gets current query or constructs one on demand.
	 *
	 * @return The current query.
	 */
	public Query getQuery()
	{
		if(this.query == null)
		{
			this.queryDefinition.setSortPropertyIds(this.sortPropertyIds);
			this.queryDefinition.setSortPropertyAscendingStates(this.ascendingStates);
			this.query = this.queryFactory.constructQuery(this.queryDefinition);
			if(this.query instanceof XdevEntityQuery)
			{
				((XdevEntityQuery)this.query).setRequiredProperties(this.requiredProperties);
			}
			this.querySize = this.query.size();
			if(this.queryDefinition.getMaxQuerySize() > -1
					&& this.queryDefinition.getMaxQuerySize() < this.querySize)
			{
				this.querySize = this.queryDefinition.getMaxQuerySize();
			}
			this.queryCount++;
		}
		return this.query;
	}
	
	
	@Override
	public void setRequiredProperties(final Object... propertyIDs)
	{
		this.requiredProperties = propertyIDs;
	}


	@Override
	public Object[] getRequiredProperties()
	{
		return this.requiredProperties;
	}
	
	
	/**
	 * Constructs and adds item to added items and returns index. Change can be
	 * committed or discarded with respective methods.
	 *
	 * @return index of the new item.
	 */
	@Override
	public int addItem()
	{
		final Item item = getQuery().constructItem();
		if(item.getItemProperty(PROPERTY_ID_ITEM_STATUS) != null)
		{
			item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(false);
			item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setValue(QueryItemStatus.Added);
			item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(true);
		}
		this.addedItems.add(0,item);
		if(this.itemIdList instanceof NaturalNumberIdsList)
		{
			this.itemIdList = null;
		}
		return 0;
	}
	
	
	public <T> int addItem(final T entity)
	{
		final Item item = this.toItem(entity);
		if(item.getItemProperty(PROPERTY_ID_ITEM_STATUS) != null)
		{
			item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(false);
			item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setValue(QueryItemStatus.Added);
			item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(true);
		}
		this.addedItems.add(0,item);
		if(this.itemIdList instanceof NaturalNumberIdsList)
		{
			this.itemIdList = null;
		}
		return 0;
	}
	
	
	/**
	 * Converts bean to Item. Implemented by encapsulating the Bean first to
	 * BeanItem and then to CompositeItem.
	 *
	 * @param entity
	 *            bean to be converted.
	 * @return item converted from bean.
	 */
	protected final <T> BeanItem<T> toItem(final T entity)
	{
		// if(this.queryDefinition.isCompositeItems())
		// {
		// final NestingBeanItem<T> beanItem = new NestingBeanItem<T>(entity,
		// this.queryDefinition.getMaxNestedPropertyDepth(),
		// this.queryDefinition.getPropertyIds());
		//
		// final CompositeItem compositeItem = new CompositeItem();
		// compositeItem.addItem("bean",beanItem);
		//
		// for(final Object propertyId : this.queryDefinition.getPropertyIds())
		// {
		// if(compositeItem.getItemProperty(propertyId) == null)
		// {
		// compositeItem.addItemProperty(propertyId,
		// new ObjectProperty(
		// this.queryDefinition.getPropertyDefaultValue(propertyId),
		// this.queryDefinition.getPropertyType(propertyId),
		// this.queryDefinition.isPropertyReadOnly(propertyId)));
		// }
		// }
		//
		// return compositeItem;
		// }
		// else
		// {
		return new NestingBeanItem<T>(entity,this.queryDefinition.getMaxNestedPropertyDepth(),
				this.queryDefinition.getPropertyIds());
		// }
	}
	
	
	/**
	 * Event handler for value change events. Adds the item to modified list if
	 * value was actually changed. Change can be committed or discarded with
	 * respective methods.
	 *
	 * @param event
	 *            the ValueChangeEvent
	 */
	@Override
	public void valueChange(final ValueChangeEvent event)
	{
		final Property<?> property = event.getProperty();
		final Item item = this.propertyItemMapCache.get(property);
		if(property == item.getItemProperty(PROPERTY_ID_ITEM_STATUS))
		{
			return;
		}
		if(item.getItemProperty(PROPERTY_ID_ITEM_STATUS) != null && ((QueryItemStatus)item
				.getItemProperty(PROPERTY_ID_ITEM_STATUS).getValue()) != QueryItemStatus.Modified)
		{
			item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(false);
			item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setValue(QueryItemStatus.Modified);
			item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(true);
		}
		if(!this.addedItems.contains(item) && !this.modifiedItems.contains(item))
		{
			this.modifiedItems.add(item);
		}
	}
	
	
	/**
	 * Removes item at given index by adding it to the removed list. Change can
	 * be committed or discarded with respective methods.
	 *
	 * @param index
	 *            of the item to be removed.
	 */
	@Override
	public void removeItem(final int index)
	{
		final Item item = getItem(index);
		
		if(item.getItemProperty(PROPERTY_ID_ITEM_STATUS) != null)
		{
			item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(false);
			item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setValue(QueryItemStatus.Removed);
			item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(true);
		}
		
		for(final Object propertyId : item.getItemPropertyIds())
		{
			final Property<?> property = item.getItemProperty(propertyId);
			property.setReadOnly(true);
		}
		
		this.removedItems.add(item);
	}
	
	
	@Override
	public void removeAllItems() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
		// disabled evil default behavior
		// getQuery().deleteAllItems();
	}
	
	
	/**
	 * Checks whether view has been modified.
	 *
	 * @return True if view has been modified.
	 */
	@Override
	public boolean isModified()
	{
		return this.addedItems.size() != 0 || this.modifiedItems.size() != 0
				|| this.removedItems.size() != 0;
	}
	
	
	/**
	 * Commits changes in the view.
	 */
	@Override
	public void commit()
	{
		for(final Item item : this.addedItems)
		{
			if(item.getItemProperty(PROPERTY_ID_ITEM_STATUS) != null)
			{
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(false);
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setValue(QueryItemStatus.None);
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(true);
			}
		}
		for(final Item item : this.modifiedItems)
		{
			if(item.getItemProperty(PROPERTY_ID_ITEM_STATUS) != null)
			{
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(false);
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setValue(QueryItemStatus.None);
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(true);
			}
		}
		for(final Item item : this.removedItems)
		{
			if(item.getItemProperty(PROPERTY_ID_ITEM_STATUS) != null)
			{
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(false);
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setValue(QueryItemStatus.None);
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(true);
			}
		}
		
		// Reverse added items so that they are saved in order of addition.
		final List<Item> addedItemReversed = new ArrayList<Item>(this.addedItems);
		Collections.reverse(addedItemReversed);
		getQuery().saveItems(addedItemReversed,this.modifiedItems,this.removedItems);
		this.addedItems.clear();
		this.modifiedItems.clear();
		this.removedItems.clear();
	}
	
	
	/**
	 * Discards changes in the view.
	 */
	@Override
	public void discard()
	{
		for(final Item item : this.addedItems)
		{
			if(item.getItemProperty(PROPERTY_ID_ITEM_STATUS) != null)
			{
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(false);
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setValue(QueryItemStatus.None);
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(true);
			}
		}
		for(final Item item : this.modifiedItems)
		{
			if(item.getItemProperty(PROPERTY_ID_ITEM_STATUS) != null)
			{
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(false);
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setValue(QueryItemStatus.None);
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(true);
			}
		}
		for(final Item item : this.removedItems)
		{
			if(item.getItemProperty(PROPERTY_ID_ITEM_STATUS) != null)
			{
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(false);
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setValue(QueryItemStatus.None);
				item.getItemProperty(PROPERTY_ID_ITEM_STATUS).setReadOnly(true);
			}
		}
		this.addedItems.clear();
		this.modifiedItems.clear();
		this.removedItems.clear();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> getAddedItems()
	{
		return Collections.<Item> unmodifiableList(this.addedItems);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> getModifiedItems()
	{
		return Collections.<Item> unmodifiableList(this.modifiedItems);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> getRemovedItems()
	{
		return Collections.<Item> unmodifiableList(this.removedItems);
	}
	
	
	/**
	 * Used to set implementation property item cache map.
	 *
	 * @param propertyItemCacheMap
	 *            the propertyItemMapCache to set
	 */
	public void setPropertyItemCacheMap(final Map<Property<?>, Item> propertyItemCacheMap)
	{
		this.propertyItemMapCache = propertyItemCacheMap;
	}
	
	
	/**
	 * Gets list of item IDs present in this view.
	 *
	 * @return list of item IDs present in this view.
	 */
	@Override
	public List<?> getItemIdList()
	{
		if(this.itemIdList == null)
		{
			if(this.queryDefinition.getIdPropertyId() != null)
			{
				this.itemIdList = new XdevLazyIdList<Object>(this,
						this.queryDefinition.getIdPropertyId());
			}
			else
			{
				this.itemIdList = new NaturalNumberIdsList(size());
			}
		}
		
		return this.itemIdList;
	}
	
	
	@Override
	public void addFilter(final Container.Filter filter)
	{
		this.queryDefinition.addFilter(filter);
		refresh();
	}
	
	
	@Override
	public void removeFilter(final Container.Filter filter)
	{
		this.queryDefinition.removeFilter(filter);
		refresh();
	}
	
	
	@Override
	public void removeFilters()
	{
		this.queryDefinition.removeFilters();
		refresh();
	}
	
	
	@Override
	public Collection<Container.Filter> getFilters()
	{
		return this.queryDefinition.getFilters();
	}
	
}
