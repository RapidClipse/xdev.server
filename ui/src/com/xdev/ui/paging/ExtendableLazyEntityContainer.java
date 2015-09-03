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

package com.xdev.ui.paging;


import java.util.Collection;

import org.vaadin.addons.lazyquerycontainer.CompositeItem;

import com.vaadin.data.util.BeanItem;
import com.xdev.ui.entitycomponent.BeanContainer;


/**
 * EntityContainer enables loading JPA entities in non lazy manner in single
 * read operation using same query backend as LCQ.
 *
 * @param <T>
 *            Entity class.
 * @author Tommi Laukkanen
 */
// copied from EntityContainer to become extendable
public class ExtendableLazyEntityContainer<T> extends XdevEntityLazyQueryContainer
		implements BeanContainer<T>
{
	private static final long	serialVersionUID	= 1L;
	private final Class<T>		entityType;


	/**
	 * Constructor which configures query definition for accessing JPA entities.
	 *
	 * @param entityClass
	 *            The entity class.
	 * @param idPropertyId
	 *            The ID of the ID property or null if item index is used as ID.
	 * @param batchSize
	 *            The batch size.
	 * @param applicationManagedTransactions
	 *            True if application manages transactions instead of container.
	 * @param detachedEntities
	 *            True if entities are detached from PersistenceContext.
	 * @param compositeItems
	 *            True f items are wrapped to CompositeItems.
	 */
	public ExtendableLazyEntityContainer(final Class<T> entityClass, final int batchSize,
			final Object idPropertyId, final boolean applicationManagedTransactions,
			final boolean detachedEntities)
	{
		super(new IntrospectionEntityQueryDefinition<T>(applicationManagedTransactions,
				detachedEntities,false,entityClass,batchSize,idPropertyId),
				new RequisitioningEntityQueryFactory<T>());
		this.entityType = entityClass;
	}


	/**
	 * Constructor which configures query definition for accessing JPA entities.
	 *
	 * @param applicationManagedTransactions
	 *            True if application manages transactions instead of container.
	 * @param detachedEntities
	 *            True if entities are detached from PersistenceContext. items
	 *            until commit.
	 * @param compositeItems
	 *            True if native items should be wrapped to CompositeItems.
	 * @param entityClass
	 *            The entity class.
	 * @param batchSize
	 *            The batch size.
	 * @param defaultSortPropertyIds
	 *            Properties participating in the native sort.
	 * @param defaultSortPropertyAscendingStates
	 *            List of property sort directions for the native sort.
	 * @param idPropertyId
	 *            Property containing the property ID.
	 */
	public ExtendableLazyEntityContainer(final boolean applicationManagedTransactions,
			final boolean detachedEntities, final Class<T> entityClass, final int batchSize,
			final Object[] defaultSortPropertyIds,
			final boolean[] defaultSortPropertyAscendingStates, final Object idPropertyId)
	{
		super(new IntrospectionEntityQueryDefinition<T>(applicationManagedTransactions,
				detachedEntities,false,entityClass,batchSize,idPropertyId),
				new RequisitioningEntityQueryFactory<T>());

		getQueryView().getQueryDefinition().setDefaultSortState(defaultSortPropertyIds,
				defaultSortPropertyAscendingStates);

		this.entityType = entityClass;
	}


	/**
	 * Adds entity to the container as first item i.e. at index 0.
	 *
	 * @return the new constructed entity.
	 */
	@Override
	public T addBean()
	{
		final Object itemId = addItem();
		final T bean = getBeanItem(indexOfId(itemId)).getBean();
		this.getQueryView().commit();
		return bean;
	}


	@Override
	public int addBean(final T entity)
	{
		final int index = getQueryView().addItem(entity);
		this.getQueryView().commit();
		return index;
	}


	/**
	 * Removes given entity at given index and returns it.
	 *
	 * @param index
	 *            Index of the entity to be removed.
	 * @return The removed entity.
	 */
	@Override
	public T removeBean(final int index)
	{
		final T entityToRemove = getBeanItem(index).getBean();
		removeItem(getIdByIndex(index));
		
		this.getQueryView().commit();
		return entityToRemove;
	}


	/**
	 * Gets entity by ID.
	 *
	 * @param id
	 *            The ID of the entity.
	 * @return the entity.
	 */
	@Override
	public BeanItem<T> getBeanItem(final Object id)
	{
		return getBeanItem(indexOfId(id));
	}


	/**
	 * Gets entity at given index.
	 *
	 * @param index
	 *            The index of the entity.
	 * @return the entity.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public BeanItem<T> getBeanItem(final int index)
	{
		if(getQueryView().getQueryDefinition().isCompositeItems())
		{
			final CompositeItem compositeItem = (CompositeItem)getItem(getIdByIndex(index));
			final BeanItem<T> beanItem = (BeanItem<T>)compositeItem.getItem("bean");
			return beanItem;
		}
		else
		{
			return((BeanItem<T>)getItem(getIdByIndex(index)));
		}
	}


	@Override
	public void removeBean(final T entity)
	{
		for(final Object itemId : this.getItemIds())
		{
			this.getBeanItem(itemId).equals(entity);
			this.removeItem(itemId);
		}
		this.commit();
	}


	@Override
	public Class<T> getBeanType()
	{
		return this.entityType;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAll(final Collection<T> collection)
	{
		for(final T entity : collection)
		{
			this.getQueryView().addItem(entity);
		}
		this.commit();
		notifyItemSetChanged();
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.xdev.ui.entitycomponent.BeanContainer#removeAllBeans()
	 */
	@Override
	public void removeAllBeans()
	{
		for(final Object itemId : this.getItemIds())
		{
			this.getQueryView().removeItem(this.indexOfId(itemId));
		}
		this.commit();
		notifyItemSetChanged();
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.xdev.ui.entitycomponent.BeanContainer#removeAll(java.util.Collection)
	 */
	@Override
	public void removeAll(final Collection<T> collection)
	{
		/*
		 * FIXME performance - call commit only once and not with each
		 * invocation of #removeBean
		 */
		for(final T t : collection)
		{
			this.removeBean(t);
		}
		this.commit();
	}

}
