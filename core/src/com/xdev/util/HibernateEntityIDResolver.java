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

package com.xdev.util;


import java.util.Optional;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import com.xdev.persistence.PersistenceUtils;


/**
 *
 * @author XDEV Software
 *
 */
public class HibernateEntityIDResolver implements EntityIDResolver
{
	private static HibernateEntityIDResolver instance;


	public static HibernateEntityIDResolver getInstance()
	{
		if(instance == null)
		{
			instance = new HibernateEntityIDResolver();
		}

		return instance;
	}


	private HibernateEntityIDResolver()
	{
	}


	@Override
	public Property getEntityIDProperty(Class<?> entityClass)
	{
		String className = entityClass.getName();
		if(className.contains("_$$_"))
		{
			className = className.substring(0,className.indexOf("_$$_"));
			try
			{
				entityClass = getClass().getClassLoader().loadClass(className);
			}
			catch(final ClassNotFoundException e)
			{
				throw new RuntimeException(e);
			}
		}

		final Configuration config = HibernateMetaDataUtils
				.getConfiguration(PersistenceUtils.getEntityManager(entityClass));
		final PersistentClass persistentClass = config.getClassMapping(className);
		if(persistentClass == null)
		{
			throw new IllegalArgumentException("Not an entity class: " + entityClass.getName());
		}

		return persistentClass.getIdentifierProperty();
	}


	@Override
	public Object getEntityIDPropertyValue(final Object entity)
	{
		final Property idProperty = getEntityIDProperty(entity.getClass());
		return Optional.ofNullable(idProperty)
				.map(property -> property.getPropertyAccessor(entity.getClass()))
				.map(accessor -> accessor.getGetter(entity.getClass(),idProperty.getName()))
				.map(getter -> getter.get(entity)).orElse(null);
	}
}
