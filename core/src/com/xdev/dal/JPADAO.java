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

package com.xdev.dal;


import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.criterion.Example;

import com.googlecode.genericdao.dao.jpa.GenericDAO;
import com.googlecode.genericdao.dao.jpa.JPABaseDAO;
import com.googlecode.genericdao.search.ExampleOptions;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.MetadataUtil;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;
import com.googlecode.genericdao.search.jpa.JPAAnnotationMetadataUtil;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import com.xdev.Application;
import com.xdev.persistence.PersistenceManager;
import com.xdev.persistence.PersistenceUtils;


/**
 * Implementation of a <b>D</b>ata <b>A</b>ccess <b>O</b>bject using JPA that
 * can be used for a single specified type domain object.
 *
 * <p>
 * <b>IMPORTANT:</b><br>
 * This class currently extends {@link JPABaseDAO}. This is subject to change in
 * a future release. The com.googlecode.genericdao dependency will be removed
 * from the framework because it makes use of the deprecated Hibernate Criteria
 * API. Appropriate replacements with the JPA Criteria API will be provided. All
 * inherited methods which will be removed are marked as deprecated.
 * </p>
 *
 * @author XDEV Software
 *
 * @param <T>
 *            The type of the domain object for which this instance is to be
 *            used.
 * @param <ID>
 *            The type of the id of the domain object for which this instance is
 *            to be used.
 */
public class JPADAO<T, ID extends Serializable> extends JPABaseDAO implements GenericDAO<T, ID>
{
	private final Class<T> persistentClass;
	
	
	public JPADAO(final Class<T> persistentClass)
	{
		this.persistentClass = persistentClass;
		this.setSearchProcessor(new JPASearchProcessor(new JPAAnnotationMetadataUtil()));
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	public void setEntityManager(final EntityManager entityManager)
	{
		super.setEntityManager(entityManager);
	}
	
	
	@Override
	protected EntityManager em()
	{
		return PersistenceUtils.getEntityManager(this.persistentClass);
	}
	
	
	public void beginTransaction()
	{
		em().getTransaction().begin();
	}
	
	
	public void rollback()
	{
		em().getTransaction().rollback();
	}
	
	
	public void commit()
	{
		em().getTransaction().commit();
	}
	
	
	protected boolean isQueryCacheEnabled()
	{
		final PersistenceManager persistenceManager = Application.getPersistenceManager();
		final String persistenceUnit = persistenceManager.getPersistenceUnit(this.persistentClass);
		return persistenceManager.isQueryCacheEnabled(persistenceUnit);
	}
	
	
	public CriteriaQuery<T> buildCriteriaQuery(final Class<T> exampleType)
	{
		final CriteriaBuilder cb = em().getCriteriaBuilder();
		return cb.createQuery(exampleType);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	public Criteria buildHibernateCriteriaQuery(final Class<T> entityType)
	{
		final Criteria crit = getSession().createCriteria(entityType);
		crit.setCacheable(isQueryCacheEnabled());
		return crit;
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	public Criteria buildHibernateCriteriaQuery(final Class<T> entityType, final String alias)
	{
		final Criteria crit = getSession().createCriteria(entityType,alias);
		crit.setCacheable(isQueryCacheEnabled());
		return crit;
	}
	
	
	/**
	 * @deprecated replaced with
	 *             {@link #findByExample(Object, SearchParameters)}, will be
	 *             removed in a future release
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public List<T> findByExample(final Class<T> entityType, final Object example)
	{
		final Criteria crit = getSession().createCriteria(entityType);
		crit.setCacheable(isQueryCacheEnabled());
		return crit.add(Example.create(example)).list();
	}
	
	
	/**
	 * @deprecated replaced with
	 *             {@link #findByExample(Object, SearchParameters)}, will be
	 *             removed in a future release
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public List<T> findByExample(final Class<T> entityType, final String alias,
			final Object example)
	{
		final Criteria crit = getSession().createCriteria(entityType,alias);
		crit.setCacheable(isQueryCacheEnabled());
		return crit.add(Example.create(example)).list();
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	protected Session getSession()
	{
		return em().unwrap(Session.class);
	}
	
	
	/**
	 * @deprecated replaced with
	 *             {@link #countByExample(Object, SearchParameters)}, will be
	 *             removed in a future release
	 */
	@Deprecated
	@Override
	public int count(ISearch search)
	{
		if(search == null)
		{
			search = new Search();
		}
		return _count(this.persistentClass,search);
	}
	
	
	@Override
	public T find(final ID id)
	{
		return _find(this.persistentClass,id);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public T[] find(final ID... ids)
	{
		return _find(this.persistentClass,ids);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@SuppressWarnings("hiding")
	@Override
	protected <T> List<T> _all(final Class<T> type)
	{
		final TypedQuery<T> query = em().createQuery(
				"select _it_ from " + getMetadataUtil().get(type).getEntityName() + " _it_",type);
		if(isQueryCacheEnabled())
		{
			query.setHint("org.hibernate.cacheable",true);
		}
		return query.getResultList();
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected int _count(final Class<?> type)
	{
		final Query query = em().createQuery(
				"select count(_it_) from " + getMetadataUtil().get(type).getEntityName() + " _it_");
		return ((Number)query.getSingleResult()).intValue();
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected boolean _exists(final Class<?> type, final Serializable id)
	{
		if(type == null)
		{
			throw new NullPointerException("Type is null.");
		}
		if(!validId(id))
		{
			return false;
		}
		
		final Query query = em().createQuery("select _it_.id from "
				+ getMetadataUtil().get(type).getEntityName() + " _it_ where _it_.id = :id");
		if(isQueryCacheEnabled())
		{
			query.setHint("org.hibernate.cacheable",true);
		}
		query.setParameter("id",id);
		return query.getResultList().size() == 1;
	}
	
	
	private boolean validId(final Serializable id)
	{
		if(id == null)
		{
			return false;
		}
		if(id instanceof Number && ((Number)id).equals(0))
		{
			return false;
		}
		if(id instanceof String && "".equals(id))
		{
			return false;
		}
		return true;
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@SuppressWarnings({"unchecked","hiding"})
	@Override
	protected <T> T[] _find(final Class<T> type, final Serializable... ids)
	{
		final T[] retList = (T[])Array.newInstance(type,ids.length);
		for(final T entity : pullByIds("select _it_",type,ids))
		{
			final Serializable id = getMetadataUtil().getId(entity);
			
			for(int i = 0; i < ids.length; i++)
			{
				if(id.equals(ids[i]))
				{
					retList[i] = entity;
					// don't break. the same id could be in the list twice.
				}
			}
		}
		
		return retList;
	}
	
	
	@SuppressWarnings("hiding")
	private <T> List<T> pullByIds(final String select, final Class<T> type,
			final Serializable[] ids)
	{
		final List<Serializable> nonNulls = new LinkedList<Serializable>();
		
		final StringBuilder sb = new StringBuilder(select);
		sb.append(" from ");
		sb.append(getMetadataUtil().get(type).getEntityName());
		sb.append(" _it_ where ");
		for(final Serializable id : ids)
		{
			if(id != null)
			{
				if(nonNulls.size() == 0)
				{
					sb.append("_it_.id = ?1");
				}
				else
				{
					sb.append(" or _it_.id = ?").append(nonNulls.size() + 1);
				}
				nonNulls.add(id);
			}
		}
		if(nonNulls.size() == 0)
		{
			return new ArrayList<>(0);
		}
		
		final TypedQuery<T> query = em().createQuery(sb.toString(),type);
		if(isQueryCacheEnabled())
		{
			query.setHint("org.hibernate.cacheable",true);
		}
		int idx = 1;
		for(final Serializable id : nonNulls)
		{
			query.setParameter(idx++,id);
		}
		return query.getResultList();
	}
	
	
	@Override
	public List<T> findAll()
	{
		return _all(this.persistentClass);
	}
	
	
	@Override
	public void flush()
	{
		_flush();
	}
	
	
	public T reattach(final T object)
	{
		final Session session = em().unwrap(Session.class);
		if(!session.contains(object))
		{
			session.lock(object,LockMode.NONE);
			session.refresh(object,new LockOptions(LockMode.NONE));
		}
		return object;
	}
	
	
	@Override
	public T getReference(final ID id)
	{
		return _getReference(this.persistentClass,id);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public T[] getReferences(final ID... ids)
	{
		return _getReferences(this.persistentClass,ids);
	}
	
	
	@Override
	public boolean isAttached(final T entity)
	{
		return _contains(entity);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void refresh(final T... entities)
	{
		_refresh(entities);
	}
	
	
	@Override
	public boolean remove(final T entity)
	{
		return _removeEntity(entity);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void remove(final T... entities)
	{
		_removeEntities((Object[])entities);
	}
	
	
	@Override
	public boolean removeById(final ID id)
	{
		return _removeById(this.persistentClass,id);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void removeByIds(final ID... ids)
	{
		_removeByIds(this.persistentClass,ids);
	}
	
	
	@Override
	public T merge(final T entity)
	{
		return _merge(entity);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public T[] merge(final T... entities)
	{
		return _merge(this.persistentClass,entities);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void persist(final T... entities)
	{
		_persist(entities);
	}
	
	
	@Override
	public T save(final T entity)
	{
		return _persistOrMerge(entity);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public T[] save(final T... entities)
	{
		return _persistOrMerge(this.persistentClass,entities);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	@Override
	public <RT> List<RT> search(final ISearch search)
	{
		if(search == null)
		{
			return (List<RT>)findAll();
		}
		return _search(this.persistentClass,search);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	@Override
	public <RT> SearchResult<RT> searchAndCount(final ISearch search)
	{
		if(search == null)
		{
			final SearchResult<RT> result = new SearchResult<RT>();
			result.setResult((List<RT>)findAll());
			result.setTotalCount(result.getResult().size());
			return result;
		}
		return _searchAndCount(this.persistentClass,search);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	@Override
	public <RT> RT searchUnique(final ISearch search)
	{
		return (RT)_searchUnique(this.persistentClass,search);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	public Filter getFilterFromExample(final T example)
	{
		return _getFilterFromExample(example);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	public Filter getFilterFromExample(final T example, final ExampleOptions options)
	{
		return _getFilterFromExample(example,options);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	public void setSearchProcessor(final JPASearchProcessor searchProcessor)
	{
		super.setSearchProcessor(searchProcessor);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected JPASearchProcessor getSearchProcessor()
	{
		return super.getSearchProcessor();
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected MetadataUtil getMetadataUtil()
	{
		return super.getMetadataUtil();
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	@Override
	protected List _search(final ISearch search)
	{
		return super._search(search);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	@Override
	protected List _search(final Class<?> searchClass, final ISearch search)
	{
		return super._search(searchClass,search);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	@Override
	protected SearchResult _searchAndCount(final ISearch search)
	{
		return super._searchAndCount(search);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	@Override
	protected SearchResult _searchAndCount(final Class<?> searchClass, final ISearch search)
	{
		return super._searchAndCount(searchClass,search);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected Object _searchUnique(final ISearch search)
			throws NonUniqueResultException, NoResultException
	{
		return super._searchUnique(search);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected Object _searchUnique(final Class<?> searchClass, final ISearch search)
			throws NonUniqueResultException, NoResultException
	{
		return super._searchUnique(searchClass,search);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected Filter _getFilterFromExample(final Object example)
	{
		return super._getFilterFromExample(example);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected Filter _getFilterFromExample(final Object example, final ExampleOptions options)
	{
		return super._getFilterFromExample(example,options);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected int _count(final ISearch search)
	{
		return super._count(search);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected int _count(final Class<?> searchClass, final ISearch search)
	{
		return super._count(searchClass,search);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected void _persist(final Object... entities)
	{
		super._persist(entities);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected boolean _removeById(final Class<?> type, final Serializable id)
	{
		return super._removeById(type,id);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected void _removeByIds(final Class<?> type, final Serializable... ids)
	{
		super._removeByIds(type,ids);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected boolean _removeEntity(final Object entity)
	{
		return super._removeEntity(entity);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected void _removeEntities(final Object... entities)
	{
		super._removeEntities(entities);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@SuppressWarnings("hiding")
	@Deprecated
	@Override
	protected <T> T _find(final Class<T> type, final Serializable id)
	{
		return super._find(type,id);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@SuppressWarnings("hiding")
	@Deprecated
	@Override
	protected <T> T _getReference(final Class<T> type, final Serializable id)
	{
		return super._getReference(type,id);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@SuppressWarnings("hiding")
	@Deprecated
	@Override
	protected <T> T[] _getReferences(final Class<T> type, final Serializable... ids)
	{
		return super._getReferences(type,ids);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@SuppressWarnings("hiding")
	@Deprecated
	@Override
	protected <T> T _merge(final T entity)
	{
		return super._merge(entity);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@SuppressWarnings({"hiding","unchecked"})
	@Deprecated
	@Override
	protected <T> T[] _merge(final Class<T> arrayType, final T... entities)
	{
		return super._merge(arrayType,entities);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@SuppressWarnings("hiding")
	@Deprecated
	@Override
	protected <T> T _persistOrMerge(final T entity)
	{
		return super._persistOrMerge(entity);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@SuppressWarnings({"hiding","unchecked"})
	@Deprecated
	@Override
	protected <T> T[] _persistOrMerge(final Class<T> arrayType, final T... entities)
	{
		return super._persistOrMerge(arrayType,entities);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected boolean _contains(final Object o)
	{
		return super._contains(o);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected void _flush()
	{
		super._flush();
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected void _refresh(final Object... entities)
	{
		super._refresh(entities);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected boolean _exists(final Object entity)
	{
		return super._exists(entity);
	}
	
	
	/**
	 * @deprecated will be removed in a future release
	 */
	@Deprecated
	@Override
	protected boolean[] _exists(final Class<?> type, final Serializable... ids)
	{
		return super._exists(type,ids);
	}
	
	
	/**
	 * Find and load a list of entities.
	 *
	 * @param entity
	 *            a sample entity whose non-null properties may be used as
	 *            search hints
	 * @return the entities matching the search.
	 * @since 3.0
	 */
	public List<T> findByExample(final T entity)
	{
		return findByExample(entity,new SearchParameters());
	}
	
	
	/**
	 * Find and load a list of entities.
	 *
	 * @param entity
	 *            a sample entity whose non-null properties may be used as
	 *            search hints
	 * @param searchParameters
	 *            carries additional search information
	 * @return the entities matching the search.
	 * @since 3.0
	 */
	public List<T> findByExample(final T entity, final SearchParameters searchParameters)
	{
		return new FindByExampleHelper<T>(this.persistentClass,em(),searchParameters)
				.findByExample(entity);
	}
	
	
	/**
	 * Count the number of instances.
	 *
	 * @param entity
	 *            a sample entity whose non-null properties may be used as
	 *            search hint
	 * @return the number of entities matching the search.
	 * @since 3.0
	 */
	public int countByExample(final T entity)
	{
		return countByExample(entity,new SearchParameters());
	}
	
	
	/**
	 * Count the number of instances.
	 *
	 * @param entity
	 *            a sample entity whose non-null properties may be used as
	 *            search hint
	 * @param searchParameters
	 *            carries additional search information
	 * @return the number of entities matching the search.
	 * @since 3.0
	 */
	public int countByExample(final T entity, final SearchParameters searchParameters)
	{
		return new FindByExampleHelper<T>(this.persistentClass,em(),searchParameters)
				.countByExample(entity);
	}
}
