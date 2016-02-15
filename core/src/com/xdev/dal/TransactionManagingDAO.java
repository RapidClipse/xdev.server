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

package com.xdev.dal;


import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.googlecode.genericdao.dao.jpa.GenericDAO;
import com.googlecode.genericdao.search.ExampleOptions;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.SearchResult;
import com.googlecode.genericdao.search.jpa.JPAAnnotationMetadataUtil;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import com.xdev.communication.EntityManagerUtils;


/**
 *
 * @deprecated replaced by {@link JPADAO}, will be removed in a future release
 */
@Deprecated
public abstract class TransactionManagingDAO<T, IT extends Serializable>
		implements GenericDAO<T, IT>
{
	/*
	 * DAO type must be at least GenericDAOImpl to achieve typed behavior and
	 * JPA support, see type hierarchy.
	 */
	private JPADAO<T, IT> persistenceManager;


	public GenericDAO<T, IT> getPersistenceManager()
	{
		return this.persistenceManager;
	}


	public void setPersistenceManager(final JPADAO<T, IT> persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}


	public TransactionManagingDAO(final Class<T> persistentClass)
	{
		this.persistenceManager = new JPADAO<>(persistentClass);
		this.persistenceManager
				.setSearchProcessor(new JPASearchProcessor(new JPAAnnotationMetadataUtil()));
	}


	protected EntityManager em()
	{
		return EntityManagerUtils.getEntityManager();
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
		beginTransaction();
		this.persistenceManager.persist(entities);
		commit();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T save(final T entity)
	{
		beginTransaction();
		final T ret = this.persistenceManager.save(entity);
		commit();
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] save(@SuppressWarnings("unchecked") final T... entities)
	{
		beginTransaction();
		final T[] ret = this.persistenceManager.save(entities);
		commit();
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(final T entity)
	{
		beginTransaction();
		final boolean ret = this.persistenceManager.remove(entity);
		commit();
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(@SuppressWarnings("unchecked") final T... entities)
	{
		beginTransaction();
		this.persistenceManager.remove(entities);
		commit();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeById(final IT id)
	{
		beginTransaction();
		final boolean ret = this.persistenceManager.removeById(id);
		commit();
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeByIds(@SuppressWarnings("unchecked") final IT... ids)
	{
		beginTransaction();
		this.persistenceManager.removeByIds(ids);
		commit();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T merge(final T entity)
	{
		beginTransaction();
		final T ret = this.persistenceManager.merge(entity);
		commit();
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] merge(@SuppressWarnings("unchecked") final T... entities)
	{
		beginTransaction();
		final T[] ret = this.persistenceManager.merge(entities);
		commit();
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T find(final IT id)
	{
		beginTransaction();
		final T ret = this.persistenceManager.find(id);
		commit();
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] find(@SuppressWarnings("unchecked") final IT... ids)
	{
		beginTransaction();
		final T[] ret = this.persistenceManager.find(ids);
		commit();
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> findAll()
	{
		beginTransaction();
		final List<T> ret = this.persistenceManager.findAll();
		commit();
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getReference(final IT id)
	{
		beginTransaction();
		final T ret = this.persistenceManager.getReference(id);
		commit();
		return ret;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] getReferences(@SuppressWarnings("unchecked") final IT... ids)
	{
		beginTransaction();
		final T[] ret = this.persistenceManager.getReferences(ids);
		commit();
		return ret;
	}


	@Override
	public <RT> List<RT> search(final ISearch search)
	{
		beginTransaction();
		final List<RT> searchResult = this.persistenceManager.search(search);
		commit();

		return searchResult;
	}


	@Override
	public <RT> RT searchUnique(final ISearch search)
	{
		beginTransaction();
		final RT searchResult = this.persistenceManager.searchUnique(search);
		commit();

		return searchResult;
	}


	@Override
	public int count(final ISearch search)
	{
		beginTransaction();
		final int count = this.persistenceManager.count(search);
		commit();
		return count;
	}


	@Override
	public <RT> SearchResult<RT> searchAndCount(final ISearch search)
	{
		beginTransaction();
		final SearchResult<RT> searchCountResult = this.persistenceManager.searchAndCount(search);
		commit();

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
		beginTransaction();
		this.persistenceManager.refresh(entities);
		commit();
	}


	@Override
	public void flush()
	{
		beginTransaction();
		this.persistenceManager.flush();
		commit();
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
}
