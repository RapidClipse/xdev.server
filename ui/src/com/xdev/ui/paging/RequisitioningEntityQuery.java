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


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.vaadin.addons.lazyquerycontainer.CompositeItem;
import org.vaadin.addons.lazyquerycontainer.EntityQuery;
import org.vaadin.addons.lazyquerycontainer.EntityQueryDefinition;
import org.vaadin.addons.lazyquerycontainer.NestingBeanItem;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Not;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.xdev.communication.EntityManagerUtils;
import com.xdev.data.util.filter.CaptionStringFilter;
import com.xdev.data.util.filter.CompareBIDirect;
import com.xdev.util.DTOUtils;
import com.xdev.util.HibernateEntityIDResolver;


@SuppressWarnings("unchecked")
public class RequisitioningEntityQuery<E> implements XdevEntityQuery, Serializable
{
	/**
	 * Java serialization version UID.
	 */
	private static final long			serialVersionUID	= 1L;
	/** The logger. */
	private static final Logger			LOGGER				= Logger.getLogger(EntityQuery.class);
	/**
	 * Flag reflecting whether application manages transactions.
	 */
	private final boolean				applicationTransactionManagement;
	/**
	 * The JPA entity class.
	 */
	private final Class<E>				entityClass;
	/**
	 * QueryDefinition contains definition of the query properties and batch
	 * size.
	 */
	private final EntityQueryDefinition	queryDefinition;
	/**
	 * The size of the query.
	 */
	private int							querySize			= -1;
															
	private Object[]					requiredProperties;
										
										
	/**
	 * Constructor for configuring the query.
	 *
	 * @param entityQueryDefinition
	 *            The entity query definition.
	 * @param entityManager
	 *            The entity manager.
	 */
	public RequisitioningEntityQuery(final EntityQueryDefinition entityQueryDefinition)
	{
		this.queryDefinition = entityQueryDefinition;
		this.entityClass = (Class<E>)entityQueryDefinition.getEntityClass();
		this.applicationTransactionManagement = entityQueryDefinition
				.isApplicationManagedTransactions();
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
	 * Constructs new item based on QueryDefinition.
	 *
	 * @return new item.
	 */
	@Override
	public final Item constructItem()
	{
		try
		{
			final Object entity = this.entityClass.newInstance();
			final BeanInfo info = Introspector.getBeanInfo(this.entityClass);
			for(final PropertyDescriptor pd : info.getPropertyDescriptors())
			{
				for(final Object propertyId : this.queryDefinition.getPropertyIds())
				{
					if(pd.getName().equals(propertyId))
					{
						pd.getWriteMethod().invoke(entity,
								this.queryDefinition.getPropertyDefaultValue(propertyId));
					}
				}
			}
			return toItem(entity);
		}
		catch(final Exception e)
		{
			throw new RuntimeException(
					"Error in bean construction or property population with default values.",e);
		}
	}
	
	
	/**
	 * Number of beans returned by query.
	 *
	 * @return number of beans.
	 */
	@Override
	public final int size()
	{
		if(this.querySize == -1)
		{
			if(this.queryDefinition.getBatchSize() == 0)
			{
				LOGGER.debug(this.entityClass.getName() + " size skipped due to 0 batch size.");
				return -1;
			}
			
			final EntityManager entityManager = em();
			final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			final Root<E> root = cq.from(this.entityClass);
			
			cq.select(cb.count(root));
			
			setWhereCriteria(cb,cq,root);
			
			// setOrderClause(cb, cq, root);
			
			final javax.persistence.Query query = entityManager.createQuery(cq);
			if(EntityManagerUtils.isQueryCacheEnabled(entityManager))
			{
				query.setHint("org.hibernate.cacheable",true);
			}
			
			this.querySize = ((Number)query.getSingleResult()).intValue();
			
			LOGGER.debug(this.entityClass.getName() + " container size: " + this.querySize);
		}
		return this.querySize;
	}
	
	
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
		final EntityManager entityManager = this.em();
		final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		final CriteriaQuery<E> cq = cb.createQuery(this.entityClass);
		final Root<E> root = cq.from(this.entityClass);
		
		cq.select(root);
		
		setWhereCriteria(cb,cq,root);
		
		setOrderClause(cb,cq,root);
		
		final javax.persistence.TypedQuery<E> query = entityManager.createQuery(cq);
		query.setFirstResult(startIndex);
		query.setMaxResults(count);
		if(EntityManagerUtils.isQueryCacheEnabled(entityManager))
		{
			query.setHint("org.hibernate.cacheable",true);
		}
		
		final List<?> entities = query.getResultList();
		final List<Item> items = new ArrayList<Item>();
		
		for(final Object entity : entities)
		{
			if(entity != null)
			{
				preload(entity);
				
				if(this.queryDefinition.isDetachedEntities())
				{
					entityManager.detach(entity);
				}
				
				items.add(toItem(entity));
			}
		}
		
		return items;
	}
	
	
	protected void preload(final Object entity)
	{
		if(this.requiredProperties == null || this.requiredProperties.length == 0)
		{
			return;
		}
		
		final String[] properties = new String[this.requiredProperties.length];
		for(int i = 0; i < properties.length; i++)
		{
			properties[i] = this.requiredProperties[i].toString();
		}
		
		DTOUtils.preload(entity,HibernateEntityIDResolver.getInstance(),properties);
	}
	
	
	/**
	 * Sets where criteria of JPA 2.0 Criteria API query according to Vaadin
	 * filters.
	 *
	 * @param cb
	 *            the CriteriaBuilder
	 * @param cq
	 *            the CriteriaQuery
	 * @param root
	 *            the root
	 * @param <SE>
	 *            the selected entity
	 */
	private <SE> void setWhereCriteria(final CriteriaBuilder cb, final CriteriaQuery<SE> cq,
			final Root<E> root)
	{
		final List<Container.Filter> filters = new ArrayList<Container.Filter>();
		filters.addAll(this.queryDefinition.getDefaultFilters());
		filters.addAll(this.queryDefinition.getFilters());
		
		// final Object[] sortPropertyIds;
		// final boolean[] sortPropertyAscendingStates;
		
		Container.Filter rootFilter;
		if(filters.size() > 0)
		{
			rootFilter = filters.remove(0);
		}
		else
		{
			rootFilter = null;
		}
		while(filters.size() > 0)
		{
			final Container.Filter filter = filters.remove(0);
			rootFilter = new And(rootFilter,filter);
		}
		
		if(rootFilter != null)
		{
			cq.where(setFilter(rootFilter,cb,cq,root));
		}
	}
	
	
	/**
	 * Sets order clause of JPA 2.0 Criteria API query according to Vaadin sort
	 * states.
	 *
	 * @param cb
	 *            the CriteriaBuilder
	 * @param cq
	 *            the CriteriaQuery
	 * @param root
	 *            the root
	 * @param <SE>
	 *            the selected entity
	 */
	private <SE> void setOrderClause(final CriteriaBuilder cb, final CriteriaQuery<SE> cq,
			final Root<E> root)
	{
		Object[] sortPropertyIds;
		boolean[] sortPropertyAscendingStates;
		
		if(this.queryDefinition.getSortPropertyIds().length == 0)
		{
			sortPropertyIds = this.queryDefinition.getDefaultSortPropertyIds();
			sortPropertyAscendingStates = this.queryDefinition
					.getDefaultSortPropertyAscendingStates();
		}
		else
		{
			sortPropertyIds = this.queryDefinition.getSortPropertyIds();
			sortPropertyAscendingStates = this.queryDefinition.getSortPropertyAscendingStates();
		}
		
		if(sortPropertyIds.length > 0)
		{
			final List<Order> orders = new ArrayList<Order>();
			for(int i = 0; i < sortPropertyIds.length; i++)
			{
				final Expression<?> property = getPropertyPath(root,sortPropertyIds[i]);
				if(sortPropertyAscendingStates[i])
				{
					orders.add(cb.asc(property));
				}
				else
				{
					orders.add(cb.desc(property));
				}
			}
			cq.orderBy(orders);
		}
	}
	
	
	/**
	 * Implements conversion of Vaadin filter to JPA 2.0 Criteria API based
	 * predicate. Supports the following operations:
	 *
	 * And, Between, Compare, Compare.Equal, Compare.Greater,
	 * Compare.GreaterOrEqual, Compare.Less, Compare.LessOrEqual, IsNull, Like,
	 * Not, Or, SimpleStringFilter
	 *
	 * @param filter
	 *            the Vaadin filter
	 * @param cb
	 *            the CriteriaBuilder
	 * @param cq
	 *            the CriteriaQuery
	 * @param root
	 *            the root
	 * @return the predicate
	 */
	@SuppressWarnings("rawtypes")
	private Predicate setFilter(final Container.Filter filter, final CriteriaBuilder cb,
			final CriteriaQuery<?> cq, final Root<?> root)
	{
		if(filter instanceof And)
		{
			final And and = (And)filter;
			final List<Container.Filter> filters = new ArrayList<Container.Filter>(
					and.getFilters());
					
			Predicate predicate = cb.and(setFilter(filters.remove(0),cb,cq,root),
					setFilter(filters.remove(0),cb,cq,root));
					
			while(filters.size() > 0)
			{
				predicate = cb.and(predicate,setFilter(filters.remove(0),cb,cq,root));
			}
			
			return predicate;
		}
		
		if(filter instanceof Or)
		{
			final Or or = (Or)filter;
			final List<Container.Filter> filters = new ArrayList<Container.Filter>(or.getFilters());
			
			Predicate predicate = cb.or(setFilter(filters.remove(0),cb,cq,root),
					setFilter(filters.remove(0),cb,cq,root));
					
			while(filters.size() > 0)
			{
				predicate = cb.or(predicate,setFilter(filters.remove(0),cb,cq,root));
			}
			
			return predicate;
		}
		
		if(filter instanceof Not)
		{
			final Not not = (Not)filter;
			return cb.not(setFilter(not.getFilter(),cb,cq,root));
		}
		
		if(filter instanceof Between)
		{
			final Between between = (Between)filter;
			final Expression property = getPropertyPath(root,between.getPropertyId());
			return cb.between(property,(Comparable)between.getStartValue(),
					(Comparable)between.getEndValue());
		}
		
		// workaround because vaadin compare / compare.equal is not extensible
		if(filter instanceof CompareBIDirect)
		{
			final CompareBIDirect compare = (CompareBIDirect)filter;
			final Path<Object> propertyPath = getPropertyPath(root,compare.getPropertyId());
			final Expression property = propertyPath;
			
			if(Collection.class.isAssignableFrom(property.getJavaType()))
			{
				/*
				 * passing concrete instance to compare collection values
				 */
				return cb.isMember(compare.getValue(),property);
			}
			else
			{
				return cb.equal(property,compare.getValue());
			}
		}
		
		if(filter instanceof com.vaadin.data.util.filter.Compare)
		{
			final com.vaadin.data.util.filter.Compare compare = (com.vaadin.data.util.filter.Compare)filter;
			final Path<Object> propertyPath = getPropertyPath(root,compare.getPropertyId());
			final Expression property = propertyPath;
			
			switch(compare.getOperation())
			{
				case EQUAL:
					
					if(Collection.class.isAssignableFrom(property.getJavaType()))
					{
						/*
						 * passing concrete instance to compare collection
						 * values
						 */
						return cb.isMember(compare.getValue(),property);
					}
					else
					{
						return cb.equal(property,compare.getValue());
					}
				case GREATER:
					return cb.greaterThan(property,(Comparable)compare.getValue());
				case GREATER_OR_EQUAL:
					return cb.greaterThanOrEqualTo(property,(Comparable)compare.getValue());
				case LESS:
					return cb.lessThan(property,(Comparable)compare.getValue());
				case LESS_OR_EQUAL:
					return cb.lessThanOrEqualTo(property,(Comparable)compare.getValue());
				default:
			}
		}
		
		if(filter instanceof com.xdev.data.util.filter.Compare)
		{
			final com.xdev.data.util.filter.Compare compare = (com.xdev.data.util.filter.Compare)filter;
			final Path<Object> propertyPath = getPropertyPath(root,compare.getPropertyId());
			final Expression property = propertyPath;
			
			switch(compare.getOperation())
			{
				case EQUAL:
					
					if(Collection.class.isAssignableFrom(property.getJavaType()))
					{
						/*
						 * passing concrete instance to compare collection
						 * values
						 */
						return cb.isMember(compare.getValue(),property);
					}
					else
					{
						return cb.equal(property,compare.getValue());
					}
				case GREATER:
					return cb.greaterThan(property,(Comparable)compare.getValue());
				case GREATER_OR_EQUAL:
					return cb.greaterThanOrEqualTo(property,(Comparable)compare.getValue());
				case LESS:
					return cb.lessThan(property,(Comparable)compare.getValue());
				case LESS_OR_EQUAL:
					return cb.lessThanOrEqualTo(property,(Comparable)compare.getValue());
				default:
			}
		}
		
		if(filter instanceof IsNull)
		{
			final IsNull isNull = (IsNull)filter;
			return cb.isNull(getPropertyPath(root,isNull.getPropertyId()));
		}
		
		if(filter instanceof Like)
		{
			final Like like = (Like)filter;
			if(like.isCaseSensitive())
			{
				return cb.like((Expression)getPropertyPath(root,like.getPropertyId()),
						like.getValue());
			}
			else
			{
				return cb.like(cb.lower((Expression)getPropertyPath(root,like.getPropertyId())),
						like.getValue().toLowerCase());
			}
		}
		
		if(filter instanceof SimpleStringFilter)
		{
			final SimpleStringFilter stringFilter = (SimpleStringFilter)filter;
			return createLike(cb,root,stringFilter.getPropertyId(),stringFilter.isIgnoreCase(),
					stringFilter.isOnlyMatchPrefix(),stringFilter.getFilterString());
		}
		
		if(filter instanceof CaptionStringFilter)
		{
			final CaptionStringFilter stringFilter = (CaptionStringFilter)filter;
			return createLike(cb,root,stringFilter.getPropertyId(),stringFilter.isIgnoreCase(),
					stringFilter.isOnlyMatchPrefix(),stringFilter.getFilterString());
		}
		
		throw new UnsupportedOperationException(
				"Vaadin filter: " + filter.getClass().getName() + " is not supported.");
	}
	
	
	private Predicate createLike(final CriteriaBuilder cb, final Root<?> root,
			final Object propertyId, final boolean ignoreCase, final boolean onlyMatchPrefix,
			final String filterString)
	{
		@SuppressWarnings("rawtypes")
		final Expression<String> property = (Expression)getPropertyPath(root,propertyId);
		if(ignoreCase)
		{
			final StringBuilder pattern = new StringBuilder();
			if(!onlyMatchPrefix)
			{
				pattern.append("%");
			}
			pattern.append(filterString.toUpperCase());
			pattern.append("%");
			return cb.like(cb.upper(property),pattern.toString());
		}
		else
		{
			final StringBuilder pattern = new StringBuilder();
			if(!onlyMatchPrefix)
			{
				pattern.append("%");
			}
			pattern.append(filterString);
			pattern.append("%");
			return cb.like(property,pattern.toString());
		}
	}
	
	
	/**
	 * Gets property path.
	 *
	 * @param root
	 *            the root where path starts form
	 * @param propertyId
	 *            the property ID
	 * @return the path to property
	 */
	private Path<Object> getPropertyPath(final Root<?> root, final Object propertyId)
	{
		final String[] propertyIdParts = ((String)propertyId).split("\\.");
		
		Path<Object> path = null;
		for(final String part : propertyIdParts)
		{
			if(path == null)
			{
				path = root.get(part);
			}
			else
			{
				path = path.get(part);
			}
		}
		return path;
	}
	
	
	/**
	 * Saves the modifications done by container to the query result. Query will
	 * be discarded after changes have been saved and new query loaded so that
	 * changed items are sorted appropriately.
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
		final EntityManager entityManager = this.em();
		if(this.applicationTransactionManagement)
		{
			entityManager.getTransaction().begin();
		}
		try
		{
			for(final Item item : addedItems)
			{
				if(!removedItems.contains(item))
				{
					entityManager.persist(fromItem(item));
				}
			}
			for(final Item item : modifiedItems)
			{
				if(!removedItems.contains(item))
				{
					Object entity = fromItem(item);
					if(this.queryDefinition.isDetachedEntities())
					{
						entity = entityManager.merge(entity);
					}
					entityManager.persist(entity);
				}
			}
			for(final Item item : removedItems)
			{
				if(!addedItems.contains(item))
				{
					Object entity = fromItem(item);
					if(this.queryDefinition.isDetachedEntities())
					{
						entity = entityManager.merge(entity);
					}
					entityManager.remove(entity);
				}
			}
			if(this.applicationTransactionManagement)
			{
				entityManager.getTransaction().commit();
			}
		}
		catch(final Exception e)
		{
			if(this.applicationTransactionManagement)
			{
				if(entityManager.getTransaction().isActive())
				{
					entityManager.getTransaction().rollback();
				}
			}
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * Removes all items. Query will be discarded after delete all items has
	 * been called.
	 *
	 * @return true if the operation succeeded or false in case of a failure.
	 */
	@Override
	public final boolean deleteAllItems()
	{
		final EntityManager entityManager = this.em();
		if(this.applicationTransactionManagement)
		{
			entityManager.getTransaction().begin();
		}
		try
		{
			final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			final CriteriaQuery<E> cq = cb.createQuery(this.entityClass);
			final Root<E> root = cq.from(this.entityClass);
			
			cq.select(root);
			
			setWhereCriteria(cb,cq,root);
			
			setOrderClause(cb,cq,root);
			
			final javax.persistence.TypedQuery<E> query = entityManager.createQuery(cq);
			if(EntityManagerUtils.isQueryCacheEnabled(entityManager))
			{
				query.setHint("org.hibernate.cacheable",true);
			}
			
			final List<?> entities = query.getResultList();
			for(final Object entity : entities)
			{
				entityManager.remove(entity);
			}
			
			if(this.applicationTransactionManagement)
			{
				entityManager.getTransaction().commit();
			}
		}
		catch(final Exception e)
		{
			if(this.applicationTransactionManagement)
			{
				if(entityManager.getTransaction().isActive())
				{
					entityManager.getTransaction().rollback();
				}
			}
			throw new RuntimeException(e);
		}
		return true;
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
		// final NestingBeanItem<?> beanItem = new
		// NestingBeanItem<Object>(entity,
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
		if(entity != null)
		{
			return new NestingBeanItem<T>(entity,this.queryDefinition.getMaxNestedPropertyDepth(),
					this.queryDefinition.getPropertyIds());
		}
		// }
		return null;
	}
	
	
	/**
	 * Converts item back to bean.
	 *
	 * @param item
	 *            Item to be converted to bean.
	 * @return Resulting bean.
	 */
	protected final Object fromItem(final Item item)
	{
		if(this.queryDefinition.isCompositeItems())
		{
			return ((BeanItem<?>)(((CompositeItem)item).getItem("bean"))).getBean();
		}
		else
		{
			return ((BeanItem<?>)item).getBean();
		}
	}
	
	
	/**
	 * @return the queryDefinition
	 */
	protected final EntityQueryDefinition getQueryDefinition()
	{
		return this.queryDefinition;
	}
	
	
	protected EntityManager em()
	{
		return EntityManagerUtils.getEntityManager();
	}
}
