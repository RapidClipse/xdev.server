
package com.xdev.server.dal;


import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.googlecode.genericdao.dao.jpa.GenericDAO;
import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;
import com.googlecode.genericdao.search.jpa.JPAAnnotationMetadataUtil;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import com.xdev.server.communication.VaadinSessionEntityManagerHelper;


/**
 * 
 * @author jwill
 *
 * @see GenericDAO
 */
public abstract class TransactionManagingDAO<T, IT extends Serializable> extends
		GenericDAOImpl<T, IT>
{
	{
		setEntityManager(VaadinSessionEntityManagerHelper.getEntityManager());
		setSearchProcessor(new JPASearchProcessor(new JPAAnnotationMetadataUtil()));
	}
	
	
	@Override
	protected EntityManager em()
	{
		return VaadinSessionEntityManagerHelper.getEntityManager();
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
	@Override
	public void persist(@SuppressWarnings("unchecked") T... entities)
	{
		beginTransaction();
		super.persist(entities);
		commit();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T save(T entity)
	{
		beginTransaction();
		T ret = super.save(entity);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] save(@SuppressWarnings("unchecked") T... entities)
	{
		beginTransaction();
		T[] ret = super.save(entities);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(T entity)
	{
		beginTransaction();
		boolean ret = super.remove(entity);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(@SuppressWarnings("unchecked") T... entities)
	{
		beginTransaction();
		super.remove(entities);
		commit();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeById(IT id)
	{
		beginTransaction();
		boolean ret = super.removeById(id);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeByIds(@SuppressWarnings("unchecked") IT... ids)
	{
		beginTransaction();
		super.removeByIds(ids);
		commit();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T merge(T entity)
	{
		beginTransaction();
		T ret = super.merge(entity);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] merge(@SuppressWarnings("unchecked") T... entities)
	{
		beginTransaction();
		T[] ret = super.merge(entities);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T find(IT id)
	{
		beginTransaction();
		T ret = super.find(id);
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
		T[] ret = super.find(ids);
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
		List<T> ret = super.findAll();
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getReference(IT id)
	{
		beginTransaction();
		T ret = super.getReference(id);
		commit();
		return ret;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] getReferences(@SuppressWarnings("unchecked") IT... ids)
	{
		beginTransaction();
		T[] ret = super.getReferences(ids);
		commit();
		return ret;
	}
}
