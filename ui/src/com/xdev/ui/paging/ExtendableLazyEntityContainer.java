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

package com.xdev.ui.paging;


import java.util.Collection;

import org.vaadin.addons.lazyquerycontainer.Query;

import com.vaadin.data.util.BeanItem;
import com.xdev.ui.entitycomponent.XdevBeanContainer;


/**
 * EntityContainer enables loading JPA entities in non lazy manner in single
 * read operation using same query backend as LCQ.
 *
 * @param <T>
 *            Entity class.
 * @author Tommi Laukkanen / XDEV Software (JW,FH)
 */
// copied from EntityContainer to become extendable
public class ExtendableLazyEntityContainer<T> extends XdevEntityLazyQueryContainer
		implements XdevBeanContainer<T>
{
	private static final long	serialVersionUID	= 1L;
	private final Class<T>		entityType;
	private Object[]			requiredProperties;
								
								
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


	@Override
	public Class<T> getBeanType()
	{
		return this.entityType;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAll(final Collection<? extends T> beans) throws UnsupportedOperationException
	{
		for(final T entity : beans)
		{
			this.getQueryView().addItem(entity);
		}
		this.commit();
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.xdev.ui.entitycomponent.BeanContainer#removeAllBeans()
	 */
	@Override
	public void removeAll() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
		// for(final Object itemId : this.getItemIds())
		// {
		// this.getQueryView().removeItem(this.indexOfId(itemId));
		// }
		// this.commit();
		// notifyItemSetChanged();
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.xdev.ui.entitycomponent.BeanContainer#removeAll(java.util.Collection)
	 */
	@Override
	public void removeAll(final Collection<? extends T> beans) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
		// /*
		// * FIXME performance - call commit only once and not with each
		// * invocation of #removeBean
		// */
		// for(final T t : beans)
		// {
		// this.removeItem(t);
		// }
		// this.commit();
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.xdev.ui.entitycomponent.XdevBeanContainer#addBean(java.lang.Object)
	 */
	@Override
	public BeanItem<T> addBean(final T bean) throws UnsupportedOperationException
	{
		final int index = getQueryView().addItem(bean);
		this.commit();
		return getItem(index);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.xdev.ui.entitycomponent.XdevBeanContainer#getItem(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BeanItem<T> getItem(final Object itemId)
	{
		return (BeanItem<T>)super.getItem(itemId);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.xdev.ui.entitycomponent.XdevBeanContainer#setRequiredProperties(java.
	 * lang.Object[])
	 */
	@Override
	public void setRequiredProperties(final Object... propertyIDs)
	{
		this.requiredProperties = propertyIDs;

		final Query query = getQueryView().getQuery();
		if(query instanceof XdevEntityQuery)
		{
			((XdevEntityQuery)query).setRequiredProperties(propertyIDs);
		}
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.xdev.ui.entitycomponent.XdevBeanContainer#getRequiredProperties()
	 */
	@Override
	public Object[] getRequiredProperties()
	{
		return this.requiredProperties;
	}
}
