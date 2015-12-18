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


import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.MappedSuperclass;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;

import com.xdev.communication.EntityManagerUtils;


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


	public static boolean isManaged(final Class<?> clazz)
	{
		return clazz.getAnnotation(Entity.class) != null
				|| clazz.getAnnotation(Embeddable.class) != null
				|| clazz.getAnnotation(MappedSuperclass.class) != null;
	}
}
