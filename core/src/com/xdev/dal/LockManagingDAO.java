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

package com.xdev.dal;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.EntityType;

import com.googlecode.genericdao.dao.jpa.GenericDAO;
import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;
import com.googlecode.genericdao.search.ExampleOptions;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.SearchResult;
import com.googlecode.genericdao.search.jpa.JPAAnnotationMetadataUtil;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import com.xdev.communication.EntityManagerHelper;
import com.xdev.communication.JPADAOWrapper;


/**
 * CAUTION: Use only in correspondence with a transactional DAO implementation
 * if no transaction manager like for example JTA is active. The actions
 * executed with this DAO implementation do neither open nor close transactions.
 *
 * @author XDEV Software (JW)
 *
 * @see GenericDAO
 */
public abstract class LockManagingDAO<T, IT extends Serializable> extends GenericDAOImpl<T, IT>
{
	/*
	 * DAO type must be at least GenericDAOImpl to achieve typed behavior and
	 * JPA support, see type hierarchy.
	 */
	private JPADAOWrapper<T, IT>		persistenceManager;

	// lock meta data
	private long						lockTimeOut				= 0;
	private static final String			LOCK_TIMEOUT_PROPERTY	= "javax.persistence.lock.timeout";
	private final Map<String, Object>	lockProperties			= new HashMap<String, Object>();


	public void setLockTimeOut(final long lockTimeOut)
	{
		this.lockTimeOut = lockTimeOut;
	}


	public JPADAOWrapper<T, IT> getPersistenceManager()
	{
		return this.persistenceManager;
	}


	public void setPersistenceManager(final JPADAOWrapper<T, IT> persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}

	{
		this.persistenceManager.setEntityManager(EntityManagerHelper.getEntityManager());
		this.persistenceManager.setSearchProcessor(new JPASearchProcessor(
				new JPAAnnotationMetadataUtil()));
	}


	@Override
	protected EntityManager em()
	{
		return EntityManagerHelper.getEntityManager();
	}


	public CriteriaQuery<T> buildCriteriaQuery(final Class<T> type)
	{
		final CriteriaBuilder cb = em().getCriteriaBuilder();
		return cb.createQuery(type);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void persist(@SuppressWarnings("unchecked") final T... entities)
	{
		for(final T entity : entities)
		{
			// TODO make LockModeType configurable?
			em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		}

		this.persistenceManager.persist(entities);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T save(final T entity)
	{
		em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		final T ret = this.persistenceManager.save(entity);
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] save(@SuppressWarnings("unchecked") final T... entities)
	{
		for(final T entity : entities)
		{
			em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		}
		final T[] ret = this.persistenceManager.save(entities);
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(final T entity)
	{
		em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		final boolean ret = this.persistenceManager.remove(entity);
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(@SuppressWarnings("unchecked") final T... entities)
	{
		for(final T entity : entities)
		{
			em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		}
		this.persistenceManager.remove(entities);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeById(final IT id)
	{
		final T entity = this.find(id);
		em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);

		final boolean ret = this.persistenceManager.removeById(id);
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeByIds(@SuppressWarnings("unchecked") final IT... ids)
	{
		final T[] entities = this.find(ids);
		for(final T entity : entities)
		{
			em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		}

		this.persistenceManager.removeByIds(ids);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T merge(final T entity)
	{
		em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		final T ret = this.persistenceManager.merge(entity);
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] merge(@SuppressWarnings("unchecked") final T... entities)
	{
		for(final T entity : entities)
		{
			em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		}
		final T[] ret = this.persistenceManager.merge(entities);
		return ret;
	}


	// -------- TODO pessimistic read allowed ? - check / provide configuration
	// ----------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T find(final IT id)
	{
		final T ret = this.persistenceManager.find(id);
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] find(@SuppressWarnings("unchecked") final IT... ids)
	{
		final T[] ret = this.persistenceManager.find(ids);
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> findAll()
	{
		final List<T> ret = this.persistenceManager.findAll();
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getReference(final IT id)
	{
		final T ret = this.persistenceManager.getReference(id);
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] getReferences(@SuppressWarnings("unchecked") final IT... ids)
	{
		final T[] ret = this.persistenceManager.getReferences(ids);
		return ret;
	}


	@Override
	public <RT> List<RT> search(final ISearch search)
	{
		final List<RT> searchResult = this.persistenceManager.search(search);
		return searchResult;
	}


	@Override
	public <RT> RT searchUnique(final ISearch search)
	{
		final RT searchResult = this.persistenceManager.searchUnique(search);
		return searchResult;
	}


	@Override
	public int count(final ISearch search)
	{
		final int count = this.persistenceManager.count(search);
		return count;
	}


	@Override
	public <RT> SearchResult<RT> searchAndCount(final ISearch search)
	{
		final SearchResult<RT> searchCountResult = this.persistenceManager.searchAndCount(search);

		return searchCountResult;
	}


	@Override
	public boolean isAttached(final T entity)
	{
		return this.persistenceManager.isAttached(entity);
	}


	@Override
	public void refresh(@SuppressWarnings("unchecked") final T... entities)
	{
		this.persistenceManager.refresh(entities);
	}


	@Override
	public void flush()
	{
		for(final EntityType<?> entity : em().getMetamodel().getEntities())
		{
			// FIXME is this a lockable entity respresentation?
			em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		}
		this.persistenceManager.flush();
	}


	@Override
	public Filter getFilterFromExample(final T example)
	{
		return this.persistenceManager.getFilterFromExample(example);
	}


	@Override
	public Filter getFilterFromExample(final T example, final ExampleOptions options)
	{
		return this.persistenceManager.getFilterFromExample(example,options);
	}


	public long getLockTimeOut()
	{
		if(this.lockProperties.get(LOCK_TIMEOUT_PROPERTY) == null
				|| !this.lockProperties.get(LOCK_TIMEOUT_PROPERTY).equals(this.lockTimeOut))
		{
			this.lockProperties.put(LOCK_TIMEOUT_PROPERTY,this.lockTimeOut);
		}
		return this.lockTimeOut;
	}
}
