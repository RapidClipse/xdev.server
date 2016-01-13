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
 * @author XDEV Software (JW)
 *
 * @see GenericDAO
 */
public abstract class TransactionManagingDAO<T, IT extends Serializable> implements
		GenericDAO<T, IT>
{
	/*
	 * DAO type must be at least GenericDAOImpl to achieve typed behavior and
	 * JPA support, see type hierarchy.
	 */
	private JPADAO<T, IT>	persistenceManager;
	
	
	public GenericDAO<T, IT> getPersistenceManager()
	{
		return persistenceManager;
	}
	
	
	public void setPersistenceManager(JPADAO<T, IT> persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}
	
	
	public TransactionManagingDAO(Class<T> persistentClass)
	{
		this.persistenceManager = new JPADAO<>(persistentClass);
		this.persistenceManager.setSearchProcessor(new JPASearchProcessor(
				new JPAAnnotationMetadataUtil()));
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
	
	
	public CriteriaQuery<T> buildCriteriaQuery(Class<T> type)
	{
		CriteriaBuilder cb = em().getCriteriaBuilder();
		return cb.createQuery(type);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void persist(@SuppressWarnings("unchecked") T... entities)
	{
		beginTransaction();
		this.persistenceManager.persist(entities);
		commit();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public T save(T entity)
	{
		beginTransaction();
		T ret = this.persistenceManager.save(entity);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public T[] save(@SuppressWarnings("unchecked") T... entities)
	{
		beginTransaction();
		T[] ret = this.persistenceManager.save(entities);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean remove(T entity)
	{
		beginTransaction();
		boolean ret = this.persistenceManager.remove(entity);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void remove(@SuppressWarnings("unchecked") T... entities)
	{
		beginTransaction();
		this.persistenceManager.remove(entities);
		commit();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean removeById(IT id)
	{
		beginTransaction();
		boolean ret = this.persistenceManager.removeById(id);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void removeByIds(@SuppressWarnings("unchecked") IT... ids)
	{
		beginTransaction();
		this.persistenceManager.removeByIds(ids);
		commit();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public T merge(T entity)
	{
		beginTransaction();
		T ret = this.persistenceManager.merge(entity);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public T[] merge(@SuppressWarnings("unchecked") T... entities)
	{
		beginTransaction();
		T[] ret = this.persistenceManager.merge(entities);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public T find(IT id)
	{
		beginTransaction();
		T ret = this.persistenceManager.find(id);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] find(@SuppressWarnings("unchecked") IT... ids)
	{
		beginTransaction();
		T[] ret = this.persistenceManager.find(ids);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public List<T> findAll()
	{
		beginTransaction();
		List<T> ret = this.persistenceManager.findAll();
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public T getReference(IT id)
	{
		beginTransaction();
		T ret = this.persistenceManager.getReference(id);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public T[] getReferences(@SuppressWarnings("unchecked") IT... ids)
	{
		beginTransaction();
		T[] ret = this.persistenceManager.getReferences(ids);
		commit();
		return ret;
	}
	
	
	@Override
	public <RT> List<RT> search(ISearch search)
	{
		beginTransaction();
		List<RT> searchResult = this.persistenceManager.search(search);
		commit();
		
		return searchResult;
	}
	
	
	@Override
	public <RT> RT searchUnique(ISearch search)
	{
		beginTransaction();
		RT searchResult = this.persistenceManager.searchUnique(search);
		commit();
		
		return searchResult;
	}
	
	
	@Override
	public int count(ISearch search)
	{
		beginTransaction();
		int count = this.persistenceManager.count(search);
		commit();
		return count;
	}
	
	
	@Override
	public <RT> SearchResult<RT> searchAndCount(ISearch search)
	{
		beginTransaction();
		SearchResult<RT> searchCountResult = this.persistenceManager.searchAndCount(search);
		commit();
		
		return searchCountResult;
	}
	
	
	@Override
	public boolean isAttached(T entity)
	{
		return this.persistenceManager.isAttached(entity);
	}
	
	
	@Override
	public void refresh(@SuppressWarnings("unchecked") T... entities)
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
	public Filter getFilterFromExample(T example)
	{
		return this.persistenceManager.getFilterFromExample(example);
	}
	
	
	@Override
	public Filter getFilterFromExample(T example, ExampleOptions options)
	{
		return this.persistenceManager.getFilterFromExample(example,options);
	}
}
