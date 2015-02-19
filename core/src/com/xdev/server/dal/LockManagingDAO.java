
package com.xdev.server.dal;


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
import com.xdev.server.communication.EntityManagerHelper;


/**
 * CAUTION: Use only in correspondence with a transactional DAO implementation if no
 * transaction manager like for example JTA is active. The actions executed with
 * this DAO implementation do neither open nor close transactions.
 * 
 * @author jwill
 *
 * @see GenericDAO
 */
public abstract class LockManagingDAO<T, IT extends Serializable> extends GenericDAOImpl<T, IT>
{
	/*
	 * DAO type must be at least GenericDAOImpl to achieve typed behavior and
	 * JPA support, see type hierarchy.
	 */
	private GenericDAOImpl<T, IT>	persistenceManager		= new GenericDAOImpl<>();
	
	// lock meta data
	private long					lockTimeOut				= 0;
	private static final String		LOCK_TIMEOUT_PROPERTY	= "javax.persistence.lock.timeout";
	private Map<String, Object>		lockProperties			= new HashMap<String, Object>();
	
	
	public void setLockTimeOut(long lockTimeOut)
	{
		this.lockTimeOut = lockTimeOut;
	}
	
	
	public GenericDAOImpl<T, IT> getPersistenceManager()
	{
		return persistenceManager;
	}
	
	
	public void setPersistenceManager(GenericDAOImpl<T, IT> persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}
	
	{
		persistenceManager.setEntityManager(EntityManagerHelper.getEntityManager());
		persistenceManager.setSearchProcessor(new JPASearchProcessor(
				new JPAAnnotationMetadataUtil()));
	}
	
	
	protected EntityManager em()
	{
		return EntityManagerHelper.getEntityManager();
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
		for(T entity : entities)
		{
			// TODO make LockModeType configurable?
			em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		}
		
		this.persistenceManager.persist(entities);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public T save(T entity)
	{
		em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		T ret = this.persistenceManager.save(entity);
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public T[] save(@SuppressWarnings("unchecked") T... entities)
	{
		for(T entity : entities)
		{
			em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		}
		T[] ret = this.persistenceManager.save(entities);
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean remove(T entity)
	{
		em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		boolean ret = this.persistenceManager.remove(entity);
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void remove(@SuppressWarnings("unchecked") T... entities)
	{
		for(T entity : entities)
		{
			em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		}
		this.persistenceManager.remove(entities);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean removeById(IT id)
	{
		T entity = this.find(id);
		em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		
		boolean ret = this.persistenceManager.removeById(id);
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void removeByIds(@SuppressWarnings("unchecked") IT... ids)
	{
		T[] entities = this.find(ids);
		for(T entity : entities)
		{
			em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		}
		
		this.persistenceManager.removeByIds(ids);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public T merge(T entity)
	{
		em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		T ret = this.persistenceManager.merge(entity);
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public T[] merge(@SuppressWarnings("unchecked") T... entities)
	{
		for(T entity : entities)
		{
			em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		}
		T[] ret = this.persistenceManager.merge(entities);
		return ret;
	}
	
	
	// -------- TODO pessimistic read allowed ? - check / provide configuration
	// ----------
	/**
	 * {@inheritDoc}
	 */
	public T find(IT id)
	{
		T ret = this.persistenceManager.find(id);
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] find(@SuppressWarnings("unchecked") IT... ids)
	{
		T[] ret = this.persistenceManager.find(ids);
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public List<T> findAll()
	{
		List<T> ret = this.persistenceManager.findAll();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public T getReference(IT id)
	{
		T ret = this.persistenceManager.getReference(id);
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public T[] getReferences(@SuppressWarnings("unchecked") IT... ids)
	{
		T[] ret = this.persistenceManager.getReferences(ids);
		return ret;
	}
	
	
	@Override
	public <RT> List<RT> search(ISearch search)
	{
		List<RT> searchResult = this.persistenceManager.search(search);
		return searchResult;
	}
	
	
	@Override
	public <RT> RT searchUnique(ISearch search)
	{
		RT searchResult = this.persistenceManager.searchUnique(search);
		return searchResult;
	}
	
	
	@Override
	public int count(ISearch search)
	{
		int count = this.persistenceManager.count(search);
		return count;
	}
	
	
	@Override
	public <RT> SearchResult<RT> searchAndCount(ISearch search)
	{
		SearchResult<RT> searchCountResult = this.persistenceManager.searchAndCount(search);
		
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
		this.persistenceManager.refresh(entities);
	}
	
	
	@Override
	public void flush()
	{
		for(EntityType<?> entity : em().getMetamodel().getEntities())
		{
			// FIXME is this a lockable entity respresentation?
			em().lock(entity,LockModeType.PESSIMISTIC_WRITE,this.lockProperties);
		}
		this.persistenceManager.flush();
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
	
	
	public long getLockTimeOut()
	{
		if(this.lockProperties.get(LOCK_TIMEOUT_PROPERTY) == null
				|| !this.lockProperties.get(LOCK_TIMEOUT_PROPERTY).equals(this.lockTimeOut))
		{
			this.lockProperties.put(LOCK_TIMEOUT_PROPERTY,this.lockTimeOut);
		}
		return lockTimeOut;
	}
}
