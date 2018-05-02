/**
 * Copyright 2010 Tommi S.E. Laukkanen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vaadin.addons.lazyquerycontainer;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.ObjectProperty;


/**
 * Simple generics bean query implementation which dynamically injects missing
 * query definition properties to CompositeItems.
 *
 * @param <T>
 *            Bean class.
 * @author Tommi S.E. Laukkanen
 */
@SuppressWarnings("deprecation")
public abstract class AbstractBeanQuery<T> implements Query, Serializable
{
	/**
	 * QueryDefinition contains definition of the query properties.
	 */
	private QueryDefinition		queryDefinition;
	/**
	 * Query configuration contains implementation specific configuration.
	 */
	private Map<String, Object>	queryConfiguration;
	/**
	 * The properties participating in sort.
	 */
	private Object[]			sortPropertyIds;
	/**
	 * The ascending or descending state of sort properties.
	 */
	private boolean[]			sortStates;


	/**
	 * Default constructor for serialization.
	 */
	public AbstractBeanQuery()
	{
	}


	/**
	 * The constructor for defining the query.
	 *
	 * @param queryDefinition
	 *            QueryDefinition contains the query properties.
	 * @param queryConfiguration
	 *            Implementation specific configuration.
	 * @param sortPropertyIds
	 *            The properties participating in sort.
	 * @param sortStates
	 *            The ascending or descending state of sort properties.
	 */
	public AbstractBeanQuery(final QueryDefinition queryDefinition,
			final Map<String, Object> queryConfiguration, final Object[] sortPropertyIds,
			final boolean[] sortStates)
	{
		this.queryDefinition = queryDefinition;
		this.queryConfiguration = queryConfiguration;
		this.sortPropertyIds = sortPropertyIds;
		this.sortStates = sortStates;
	}


	/**
	 * @return the queryDefinition
	 */
	protected final QueryDefinition getQueryDefinition()
	{
		return this.queryDefinition;
	}


	/**
	 * @return the queryConfiguration
	 */
	protected final Map<String, Object> getQueryConfiguration()
	{
		return this.queryConfiguration;
	}


	/**
	 * @return the sortPropertyIds
	 */
	protected final Object[] getSortPropertyIds()
	{
		return this.sortPropertyIds;
	}


	/**
	 * @return the sortStates
	 */
	protected final boolean[] getSortStates()
	{
		return this.sortStates;
	}


	/**
	 * Constructs new item based on QueryDefinition.
	 *
	 * @return new item.
	 */
	@Override
	public final Item constructItem()
	{
		try
		{
			final T bean = constructBean();
			final BeanInfo info = Introspector.getBeanInfo(bean.getClass());
			for(final PropertyDescriptor pd : info.getPropertyDescriptors())
			{
				for(final Object propertyId : this.queryDefinition.getPropertyIds())
				{
					if(pd.getName().equals(propertyId))
					{
						pd.getWriteMethod().invoke(bean,
								this.queryDefinition.getPropertyDefaultValue(propertyId));
					}
				}
			}
			return toItem(bean);
		}
		catch(final Exception e)
		{
			throw new RuntimeException(
					"Error in bean construction or property population with default values.",e);
		}
	}


	/**
	 * Constructs new bean.
	 *
	 * @return a new bean.
	 */
	protected abstract T constructBean();


	/**
	 * Number of beans returned by query.
	 *
	 * @return number of beans.
	 */
	@Override
	public abstract int size();


	/**
	 * Load batch of items.
	 *
	 * @param startIndex
	 *            Starting index of the item list.
	 * @param count
	 *            Count of the items to be retrieved.
	 * @return List of items.
	 */
	@Override
	public final List<Item> loadItems(final int startIndex, final int count)
	{
		final List<Item> items = new ArrayList<Item>();
		for(final T bean : loadBeans(startIndex,count))
		{
			items.add(toItem(bean));
		}
		return items;
	}


	/**
	 * Loads a batch of beans.
	 *
	 * @param startIndex
	 *            index of first bean to return in a batch,
	 * @param count
	 *            maximum number of beans in this batch.
	 * @return List of beans identified by startIndex and count.
	 */
	protected abstract List<T> loadBeans(int startIndex, int count);


	/**
	 * Saves the modifications done by container to the query result. Query will be
	 * discarded after changes have been saved and new query loaded so that changed
	 * items are sorted appropriately.
	 *
	 * @param addedItems
	 *            Items to be inserted.
	 * @param modifiedItems
	 *            Items to be updated.
	 * @param removedItems
	 *            Items to be deleted.
	 */
	@Override
	public final void saveItems(final List<Item> addedItems, final List<Item> modifiedItems,
			final List<Item> removedItems)
	{
		saveBeans(fromItems(addedItems),fromItems(modifiedItems),fromItems(removedItems));
	}


	/**
	 * Saves the modifications done by container to the query result. Query will be
	 * discarded after changes have been saved and new query loaded so that changed
	 * items are sorted appropriately.
	 *
	 * @param addedBeans
	 *            Beans to be inserted.
	 * @param modifiedBeans
	 *            Beans to be updated.
	 * @param removedBeans
	 *            Beans to be deleted.
	 */
	protected abstract void saveBeans(List<T> addedBeans, List<T> modifiedBeans,
			List<T> removedBeans);


	/**
	 * Removes all items. Query will be discarded after delete all items has been
	 * called.
	 *
	 * @return true if the operation succeeded or false in case of a failure.
	 */
	@Override
	public final boolean deleteAllItems()
	{
		throw new UnsupportedOperationException();
	}


	/**
	 * Converts bean to Item. Implemented by encapsulating the Bean first to
	 * BeanItem and then to CompositeItem.
	 *
	 * @param bean
	 *            bean to be converted.
	 * @return item converted from bean.
	 */
	@SuppressWarnings({"unchecked","rawtypes"})
	private Item toItem(final T bean)
	{
		final NestingBeanItem<T> beanItem = new NestingBeanItem<T>(bean,
				this.queryDefinition.getMaxNestedPropertyDepth(),
				this.queryDefinition.getPropertyIds());

		if(this.queryDefinition.isCompositeItems())
		{
			final CompositeItem compositeItem = new CompositeItem();
			compositeItem.addItem("bean",beanItem);

			for(final Object propertyId : this.queryDefinition.getPropertyIds())
			{
				if(compositeItem.getItemProperty(propertyId) == null)
				{
					compositeItem.addItemProperty(propertyId,
							new ObjectProperty(
									this.queryDefinition.getPropertyDefaultValue(propertyId),
									this.queryDefinition.getPropertyType(propertyId),
									this.queryDefinition.isPropertyReadOnly(propertyId)));
				}
			}

			return compositeItem;
		}
		else
		{
			return beanItem;
		}
	}


	/**
	 * Converts item back to bean.
	 *
	 * @param item
	 *            Item to be converted to bean.
	 * @return Resulting bean.
	 */
	@SuppressWarnings("unchecked")
	private T fromItem(final Item item)
	{
		if(this.queryDefinition.isCompositeItems())
		{
			return ((BeanItem<T>)(((CompositeItem)item).getItem("bean"))).getBean();
		}
		else
		{
			return ((BeanItem<T>)item).getBean();
		}
	}


	/**
	 * Converts List of Items to List of Beans.
	 *
	 * @param items
	 *            Item List to be converted.
	 * @return List of beans converted from Items.
	 */
	private List<T> fromItems(final List<Item> items)
	{
		final ArrayList<T> beans = new ArrayList<T>();
		for(final Item item : items)
		{
			beans.add(fromItem(item));
		}
		return beans;
	}
}
