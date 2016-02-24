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
 * 
 * For further information see 
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.dal;


import java.io.Serializable;

import com.xdev.util.SoftCache;


/**
 *
 * @author XDEV Software
 *		
 */
public class DAOs
{
	private static final SoftCache<Class<?>, JPADAO<?, ?>> cache = new SoftCache<>();
	
	
	public static <D extends JPADAO<?, ?>> D get(final Class<D> daoType) throws RuntimeException
	{
		synchronized(cache)
		{
			@SuppressWarnings("unchecked")
			D dao = (D)cache.get(daoType);
			
			if(dao == null)
			{
				try
				{
					dao = daoType.newInstance();
					cache.put(daoType,dao);
				}
				catch(InstantiationException | IllegalAccessException e)
				{
					throw new RuntimeException(e);
				}
			}
			
			return dao;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T, I extends Serializable> JPADAO<T, I> get(final T entity)
			throws RuntimeException
	{
		return (JPADAO<T, I>)getByEntityType(entity.getClass());
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T, I extends Serializable> JPADAO<T, I> getByEntityType(final Class<T> entity)
			throws RuntimeException
	{
		final DAO dao = entity.getAnnotation(DAO.class);
		if(dao == null)
		{
			throw new RuntimeException("Not an entity");
		}
		
		return (JPADAO<T, I>)get(dao.daoClass());
	}
}
