/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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


import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import com.xdev.dal.DAOs;
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


	public static void reattachIfManaged(final Object bean)
	{
		if(JPAMetaDataUtils.isManaged(bean.getClass()))
		{
			DAOs.get(bean).reattach(bean);
		}
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
							idResolver.getEntityIDAttributeValue(object);
						}
					}
				}
				else if(JPAMetaDataUtils.isManaged(propertyValue.getClass()))
				{
					idResolver.getEntityIDAttributeValue(propertyValue);
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
			
			propertyValue = ReflectionUtils.getMemberValue(managedObject,attribute.getJavaMember());
			if(propertyValue != null && JPAMetaDataUtils.isManaged(propertyValue.getClass()))
			{
				managedObject = propertyValue;
			}
		}
		
		return propertyValue;
	}
}
