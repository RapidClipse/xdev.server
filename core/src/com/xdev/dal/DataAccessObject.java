/*
 * Copyright (C) 2013-2019 by XDEV Software, All Rights Reserved.
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
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;


/**
 * @author XDEV Software
 *
 * @param <T>
 * @param <ID>
 *
 * @since 4.0
 */
public interface DataAccessObject<T, ID extends Serializable>
{
	public void beginTransaction();
	
	
	public void rollback();
	
	
	public void commit();
	
	
	public CriteriaQuery<T> buildCriteriaQuery(Class<T> exampleType);
	
	
	public T find(ID id);
	
	
	public T[] find(@SuppressWarnings("unchecked") ID... ids);
	
	
	public List<T> findAll();
	
	
	public void flush();
	
	
	public T reattach(T object);
	
	
	public T getReference(ID id);
	
	
	public T[] getReferences(@SuppressWarnings("unchecked") ID... ids);
	
	
	public boolean isAttached(T entity);
	
	
	public void refresh(@SuppressWarnings("unchecked") T... entities);
	
	
	public boolean remove(T entity);
	
	
	public void remove(@SuppressWarnings("unchecked") T... entities);
	
	
	public boolean removeById(ID id);
	
	
	public void removeByIds(@SuppressWarnings("unchecked") ID... ids);
	
	
	public T merge(T entity);
	
	
	public T[] merge(@SuppressWarnings("unchecked") T... entities);
	
	
	public void persist(T entity);
	
	
	public void persist(@SuppressWarnings("unchecked") T... entities);
	
	
	public T save(T entity);
	
	
	public T[] save(@SuppressWarnings("unchecked") T... entities);
	
	
	public boolean contains(Object entity);
	
	
	/**
	 * Find and load a list of entities.
	 *
	 * @param entity
	 *            a sample entity whose non-null properties may be used as search
	 *            hints
	 * @return the entities matching the search.
	 * @since 3.0
	 */
	public List<T> findByExample(T entity);
	
	
	/**
	 * Find and load a list of entities.
	 *
	 * @param entity
	 *            a sample entity whose non-null properties may be used as search
	 *            hints
	 * @param searchParameters
	 *            carries additional search information
	 * @return the entities matching the search.
	 * @since 3.0
	 */
	public List<T> findByExample(T entity, SearchParameters searchParameters);
	
	
	/**
	 * Count the number of instances.
	 *
	 * @param entity
	 *            a sample entity whose non-null properties may be used as search
	 *            hint
	 * @return the number of entities matching the search.
	 * @since 3.0
	 */
	public int countByExample(T entity);
	
	
	/**
	 * Count the number of instances.
	 *
	 * @param entity
	 *            a sample entity whose non-null properties may be used as search
	 *            hint
	 * @param searchParameters
	 *            carries additional search information
	 * @return the number of entities matching the search.
	 * @since 3.0
	 */
	public int countByExample(T entity, SearchParameters searchParameters);
}
