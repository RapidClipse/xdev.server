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


import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.MappedSuperclass;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;

import com.xdev.persistence.PersistenceUtils;


/**
 * @author XDEV Software
 * 
 * @noapi <strong>For internal use only. This class is subject to change in the
 *        future.</strong>
 */
public final class JPAMetaDataUtils
{
	private JPAMetaDataUtils()
	{
	}


	public static <C> ManagedType<C> getManagedType(final Class<C> entityClass)
	{
		final EntityManager entityManager = PersistenceUtils.getEntityManager(entityClass);
		if(entityManager == null)
		{
			throw new IllegalArgumentException("Not an entity class: " + entityClass.getName());
		}
		
		return entityManager.getMetamodel().managedType(entityClass);
	}
	
	
	public static Attribute<?, ?> resolveAttribute(Class<?> entityClass, final String propertyPath)
	{
		ManagedType<?> entityType = null;
		try
		{
			entityType = getManagedType(entityClass);
		}
		catch(final IllegalArgumentException e)
		{
			// not a managed type, XWS-870
			return null;
		}
		
		final String[] parts = propertyPath.split("\\.");
		for(int i = 0; i < parts.length - 1; i++)
		{
			final String name = parts[i];
			Attribute<?, ?> attribute = null;
			try
			{
				attribute = entityType.getAttribute(name);
			}
			catch(final IllegalArgumentException e)
			{
				// attribute not found, XWS-870
				return null;
			}
			entityClass = attribute.getJavaType();
			if(entityClass == null)
			{
				return null;
			}
			try
			{
				entityType = getManagedType(entityClass);
			}
			catch(final IllegalArgumentException e)
			{
				// not a managed type, XWS-870
				return null;
			}
		}
		
		try
		{
			return entityType.getAttribute(parts[parts.length - 1]);
		}
		catch(final IllegalArgumentException e)
		{
			// attribute not found, XWS-870
			return null;
		}
	}
	
	
	public static boolean isManaged(final Class<?> clazz)
	{
		return clazz.getAnnotation(Entity.class) != null
				|| clazz.getAnnotation(Embeddable.class) != null
				|| clazz.getAnnotation(MappedSuperclass.class) != null;
	}
}
