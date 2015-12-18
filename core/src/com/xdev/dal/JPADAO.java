/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;
import com.googlecode.genericdao.search.jpa.JPAAnnotationMetadataUtil;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import com.xdev.communication.EntityManagerUtils;


@SuppressWarnings("unchecked")
public class JPADAO<T, ID extends Serializable> extends JPABaseDAO implements GenericDAO<T, ID>
{
	private final Class<T> persistentClass;


	public JPADAO(final Class<T> persistentClass)
	{
		this.persistentClass = persistentClass;
		this.setSearchProcessor(new JPASearchProcessor(new JPAAnnotationMetadataUtil()));
	}


	@Override
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


	public CriteriaQuery<T> buildCriteriaQuery(final Class<T> exampleType)
	{
		final CriteriaBuilder cb = em().getCriteriaBuilder();
		return cb.createQuery(exampleType);
	}


	public Criteria buildHibernateCriteriaQuery(final Class<T> entityType)
	{
		final Criteria crit = em().unwrap(Session.class).createCriteria(entityType);
		if(EntityManagerUtils.isCacheEnabled())
		{
			crit.setCacheable(true);
		}
		return crit;
	}


	public Criteria buildHibernateCriteriaQuery(final Class<T> entityType, final String alias)
	{
		final Criteria crit = em().unwrap(Session.class).createCriteria(entityType,alias);
		if(EntityManagerUtils.isCacheEnabled())
		{
			crit.setCacheable(true);
		}
		return crit;
	}


	public List<T> findByExample(final Class<T> entityType, final Object example)
	{
		final Criteria crit = em().unwrap(Session.class).createCriteria(entityType);

		if(EntityManagerUtils.isCacheEnabled())
		{
			crit.setCacheable(true);
		}

		return crit.add(Example.create(example)).list();
	}


	public List<T> findByExample(final Class<T> entityType, final String alias,
			final Object example)
	{
		final Criteria crit = em().unwrap(Session.class).createCriteria(entityType,alias);

		if(EntityManagerUtils.isCacheEnabled())
		{
			crit.setCacheable(true);
		}

		return crit.add(Example.create(example)).list();
	}


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


	@Override
	public T[] find(final ID... ids)
	{
		return _find(this.persistentClass,ids);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.googlecode.genericdao.dao.jpa.JPABaseDAO#_all(java.lang.Class)
	 */
	@SuppressWarnings("hiding")
	@Override
	protected <T> List<T> _all(final Class<T> type)
	{
		final Query query = em().createQuery(
				"select _it_ from " + getMetadataUtil().get(type).getEntityName() + " _it_");
		if(EntityManagerUtils.isCacheEnabled())
		{
			query.setHint("org.hibernate.cacheable",true);
		}
		return query.getResultList();
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.googlecode.genericdao.dao.jpa.JPABaseDAO#_count(java.lang.Class)
	 */
	@Override
	protected int _count(final Class<?> type)
	{
		final Query query = em().createQuery(
				"select count(_it_) from " + getMetadataUtil().get(type).getEntityName() + " _it_");
		return ((Number)query.getSingleResult()).intValue();
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.googlecode.genericdao.dao.jpa.JPABaseDAO#_exists(java.lang.Class,
	 * java.io.Serializable)
	 */
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
		if(EntityManagerUtils.isCacheEnabled())
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


	/*
	 * (non-Javadoc)
	 *
	 * @see com.googlecode.genericdao.dao.jpa.JPABaseDAO#_find(java.lang.Class,
	 * java.io.Serializable[])
	 */
	@SuppressWarnings("hiding")
	@Override
	protected <T> T[] _find(final Class<T> type, final Serializable... ids)
	{
		final Object[] retList = (Object[])Array.newInstance(type,ids.length);
		for(final Object entity : pullByIds("select _it_",type,ids))
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

		return (T[])retList;
	}


	private List<?> pullByIds(final String select, final Class<?> type, final Serializable[] ids)
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
			return new ArrayList<Object>(0);
		}

		final Query query = em().createQuery(sb.toString());
		if(EntityManagerUtils.isCacheEnabled())
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


	@Override
	public T getReference(final ID id)
	{
		return _getReference(this.persistentClass,id);
	}


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


	@Override
	public T[] merge(final T... entities)
	{
		return _merge(this.persistentClass,entities);
	}


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


	@Override
	public T[] save(final T... entities)
	{
		return _persistOrMerge(this.persistentClass,entities);
	}


	@Override
	public <RT> List<RT> search(final ISearch search)
	{
		if(search == null)
		{
			return (List<RT>)findAll();
		}
		return _search(this.persistentClass,search);
	}


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


	@Override
	public <RT> RT searchUnique(final ISearch search)
	{
		return (RT)_searchUnique(this.persistentClass,search);
	}


	@Override
	public Filter getFilterFromExample(final T example)
	{
		return _getFilterFromExample(example);
	}


	@Override
	public Filter getFilterFromExample(final T example, final ExampleOptions options)
	{
		return _getFilterFromExample(example,options);
	}


	public T reattach(final T object)
	{
		final Session session = em().unwrap(Session.class);
		session.refresh(object,new LockOptions(LockMode.NONE));
		return object;
	}
}
