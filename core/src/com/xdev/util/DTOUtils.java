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


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import com.xdev.persistence.PersistenceUtils;


/**
 * @author XDEV Software
 *
 * @noapi <strong>For internal use only. This class is subject to change in the
 *        future.</strong>
 */
public final class DTOUtils
{
	private DTOUtils()
	{
	}
	
	
	public static void preload(final Object bean, final EntityIDResolver idResolver,
			final String... requiredProperties)
	{
		for(final String property : requiredProperties)
		{
			final Object propertyValue = resolveAttributeValue(bean,property);
			if(propertyValue != null)
			{
				// force eager loading
				if(propertyValue instanceof Collection<?>)
				{
					final Collection<?> collection = (Collection<?>)propertyValue;
					for(final Object object : collection)
					{
						if(JPAMetaDataUtils.isManaged(object.getClass()))
						{
							idResolver.getEntityIDPropertyValue(object);
						}
					}
				}
				else if(JPAMetaDataUtils.isManaged(propertyValue.getClass()))
				{
					idResolver.getEntityIDPropertyValue(propertyValue);
				}
			}
		}
	}
	
	
	public static Object resolveAttributeValue(Object managedObject, final String propertyPath)
	{
		final EntityManager entityManager = PersistenceUtils
				.getEntityManager(managedObject.getClass());
		if(entityManager == null)
		{
			return null;
		}
		
		Object propertyValue = null;
		
		final Metamodel metamodel = entityManager.getMetamodel();
		ManagedType<?> managedType = null;
		
		final String[] parts = propertyPath.split("\\.");
		for(int i = 0; i < parts.length; i++)
		{
			Class<?> managedClass = managedObject.getClass();
			try
			{
				managedType = metamodel.managedType(managedClass);
			}
			catch(final IllegalArgumentException e)
			{
				// not a managed type, XWS-870
				return null;
			}
			
			final String name = parts[i];
			Attribute<?, ?> attribute = null;
			try
			{
				attribute = managedType.getAttribute(name);
			}
			catch(final IllegalArgumentException e)
			{
				// attribute not found, XWS-870
				return null;
			}
			managedClass = attribute.getJavaType();
			if(managedClass == null)
			{
				return null;
			}
			if(JPAMetaDataUtils.isManaged(managedClass))
			{
				try
				{
					managedType = metamodel.managedType(managedClass);
				}
				catch(final IllegalArgumentException e)
				{
					// not a managed type, XWS-870
					return null;
				}
			}
			
			if(attribute.getJavaMember() instanceof Field)
			{
				try
				{
					propertyValue = ((Field)attribute.getJavaMember()).get(managedObject);
					if(propertyValue != null
							&& JPAMetaDataUtils.isManaged(propertyValue.getClass()))
					{
						managedObject = propertyValue;
					}
				}
				catch(IllegalArgumentException | IllegalAccessException e)
				{
					throw new RuntimeException(e);
				}
			}
			else if(attribute.getJavaMember() instanceof Method)
			{
				// invoke getter
				try
				{
					propertyValue = ((Method)attribute.getJavaMember()).invoke(managedObject);
					if(propertyValue != null
							&& JPAMetaDataUtils.isManaged(propertyValue.getClass()))
					{
						managedObject = propertyValue;
					}
				}
				catch(IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		
		return propertyValue;
	}
}
