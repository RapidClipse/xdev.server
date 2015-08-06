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

import com.googlecode.genericdao.dao.jpa.GenericDAO;
import com.xdev.util.SoftCache;


public class DAOs
{
	// private static final Configuration config;
	// private static final EntityFieldAnnotation annotationReader;
	private static final SoftCache<Class<?>, GenericDAO<?, ?>> cache;
	
	
	static
	{
		// config = new Configuration();
		// Set<EntityType<?>> set =
		// EntityManagerHelper.getEntityManager().getMetamodel()
		// .getEntities();
		//
		// for(Iterator<EntityType<?>> i = set.iterator(); i.hasNext();)
		// {
		// Class<?> eClazz = i.next().getJavaType();
		// try
		// {
		// config.addClass(eClazz);
		// }
		// catch(MappingException e)
		// {
		// config.addAnnotatedClass(eClazz);
		// }
		// }
		// config.buildMappings();
		// annotationReader = new EntityFieldAnnotation();

		cache = new SoftCache<>();
	}


	public static <D extends GenericDAO<?, ?>> D get(final Class<D> daoType) throws RuntimeException
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
	public static <T, I extends Serializable> GenericDAO<T, I> get(final Object entity)
			throws RuntimeException
	{
		final DAO dao = entity.getClass().getAnnotation(DAO.class);
		if(dao == null)
		{
			throw new RuntimeException("Not an entity");
		}

		return (GenericDAO<T, I>)get(dao.daoClass());
	}
	
	
	public static <T, I extends Serializable> GenericDAO<T, I> getByEntityType(
			final Class<T> entity) throws RuntimeException
	{
		try
		{
			return get(entity.newInstance());
		}
		catch(InstantiationException | IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	// @SuppressWarnings("unchecked")
	// public static <T, I extends Serializable> GenericDAO<T, I>
	// getByEntityType(
	// final Class<?> entityType) throws RuntimeException
	// {
	// final DAO dao = entityType.getAnnotation(DAO.class);
	// if(dao == null)
	// {
	// throw new RuntimeException("Not an entity");
	// }
	//
	// return (GenericDAO<T, I>)get(dao.daoClass());
	// }

	// - just use get with dao type for now
	// annotation types can not be generic
	// @SuppressWarnings("unchecked")
	// public static <T, I extends Serializable> GenericDAO<T, I> get(Class<T>
	// entityClass,
	// Class<I> idClass) throws RuntimeException
	// {
	// Iterator<PersistentClass> classes = config.getClassMappings();
	// while(classes.hasNext())
	// {
	// PersistentClass clazz = classes.next();
	//
	// if(clazz.getMappedClass().equals(entityClass))
	// {
	// DAO da =
	// annotationReader.isAnnotatedType(clazz.getMappedClass(),DAO.class);
	// try
	// {
	// return (GenericDAO<T, I>)da.daoClass().newInstance();
	// }
	// catch(InstantiationException | IllegalAccessException e)
	// {
	// throw new RuntimeException(e);
	// }
	// }
	// }
	// throw new RuntimeException("No DAO found for type " +
	// entityClass.getName());
	// }
}
