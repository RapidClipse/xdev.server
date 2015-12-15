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

package com.xdev.util;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.MappedSuperclass;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import org.hibernate.mapping.Collection;
import org.hibernate.mapping.ManyToOne;
import org.hibernate.mapping.OneToMany;
import org.hibernate.mapping.OneToOne;
import org.hibernate.mapping.Value;

import com.xdev.communication.EntityManagerUtils;


public class HibernateMetaDataUtils
{
	public static String getReferencablePropertyName(final org.hibernate.mapping.Value value)
	{
		/*
		 * there is no super type for each relation type e.g. one-to-many
		 * many-to-many, one-to-one have are directly created are not inherited
		 * from a type like relation, they are independently inherited from
		 * value...
		 */
		
		final OneToMany oneToMany = getOneToMany(value);
		if(oneToMany != null)
		{
			return oneToMany.getReferencedEntityName();
		}
		else if(value instanceof OneToOne)
		{
			final OneToOne oneToOne = (OneToOne)value;
			return oneToOne.getReferencedEntityName();
		}
		
		final ManyToOne manyToOne = getManyToOne(value);
		if(manyToOne != null)
		{
			return manyToOne.getReferencedEntityName();
		}
		
		return null;
	}
	
	
	private static OneToMany getOneToMany(Value value)
	{
		// in case of wrapping because bidirectional
		if(value instanceof Collection)
		{
			value = ((Collection)value).getElement();
		}
		if(value instanceof OneToMany)
		{
			return (OneToMany)value;
		}
		return null;
	}
	
	
	private static ManyToOne getManyToOne(Value value)
	{
		// in case of wrapping because bidirectional
		if(value instanceof Collection)
		{
			value = ((Collection)value).getElement();
		}
		if(value instanceof ManyToOne)
		{
			return (ManyToOne)value;
		}
		return null;
	}
	
	
	public static Attribute<?, ?> resolveAttribute(Class<?> entityClass, final String propertyPath)
	{
		final EntityManager entityManager = EntityManagerUtils.getEntityManager();
		if(entityManager == null)
		{
			return null;
		}
		
		final Metamodel metamodel = entityManager.getMetamodel();
		ManagedType<?> entityType = metamodel.managedType(entityClass);
		if(entityType == null)
		{
			return null;
		}
		
		final String[] parts = propertyPath.split("\\.");
		for(int i = 0; i < parts.length - 1; i++)
		{
			final String name = parts[i];
			final Attribute<?, ?> attribute = entityType.getAttribute(name);
			if(attribute == null)
			{
				return null;
			}
			entityClass = attribute.getJavaType();
			if(entityClass == null)
			{
				return null;
			}
			entityType = metamodel.managedType(entityClass);
			if(entityType == null)
			{
				return null;
			}
		}
		
		return entityType.getAttribute(parts[parts.length - 1]);
	}
	
	
	public static Object resolveAttributeValue(Object managedObject, final String propertyPath)
	{
		final EntityManager entityManager = EntityManagerUtils.getEntityManager();
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
			managedType = metamodel.managedType(managedObject.getClass());
			if(managedType == null)
			{
				return null;
			}
			
			final String name = parts[i];
			final Attribute<?, ?> attribute = managedType.getAttribute(name);
			if(attribute == null)
			{
				return null;
			}
			managedClass = attribute.getJavaType();
			if(managedClass == null)
			{
				return null;
			}
			if(isManaged(managedClass))
			{
				managedType = metamodel.managedType(managedClass);
				if(managedType == null)
				{
					return null;
				}
			}
			
			if(attribute.getJavaMember() instanceof Field)
			{
				try
				{
					propertyValue = ((Field)attribute.getJavaMember()).get(managedObject);
					if(propertyValue != null && isManaged(propertyValue.getClass()))
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
					if(propertyValue != null && isManaged(propertyValue.getClass()))
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
	
	
	public static boolean isManaged(final Class<?> clazz)
	{
		return clazz.getAnnotation(Entity.class) != null
				|| clazz.getAnnotation(Embeddable.class) != null
				|| clazz.getAnnotation(MappedSuperclass.class) != null;
	}
}
