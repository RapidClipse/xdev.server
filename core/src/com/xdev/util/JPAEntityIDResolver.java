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

import javax.persistence.EntityManager;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.SingularAttribute;

import com.xdev.persistence.PersistenceUtils;


/**
 *
 * @author XDEV Software
 *
 */
public class JPAEntityIDResolver implements EntityIDResolver
{
	private static JPAEntityIDResolver instance;
	
	
	public static JPAEntityIDResolver getInstance()
	{
		if(instance == null)
		{
			instance = new JPAEntityIDResolver();
		}
		
		return instance;
	}
	
	
	private JPAEntityIDResolver()
	{
	}
	
	
	@Override
	public String getEntityIDAttributeName(final Class<?> entityType)
	{
		final SingularAttribute<? extends Object, ?> idAttribute = getEntityIDAttribute(entityType);
		return idAttribute != null ? idAttribute.getName() : null;
	}
	
	
	@Override
	public Object getEntityIDAttributeValue(final Object entity)
	{
		final SingularAttribute<? extends Object, ?> idAttribute = getEntityIDAttribute(
				entity.getClass());
		return Optional.ofNullable(idAttribute).map(a -> a.getJavaMember())
				.map(m -> ReflectionUtils.getMemberValue(entity,m)).orElse(null);
	}
	
	
	@SuppressWarnings("unchecked")
	protected <C> SingularAttribute<C, ?> getEntityIDAttribute(final Class<C> entityClass)
	{
		final EntityManager entityManager = PersistenceUtils.getEntityManager(entityClass);
		if(entityManager == null)
		{
			throw new IllegalArgumentException("Not an entity class: " + entityClass.getName());
		}
		
		final ManagedType<C> managedType = entityManager.getMetamodel().managedType(entityClass);
		if(managedType == null)
		{
			throw new IllegalArgumentException("Not an entity class: " + entityClass.getName());
		}
		
		return managedType.getAttributes().stream().filter(SingularAttribute.class::isInstance)
				.map(SingularAttribute.class::cast).filter(SingularAttribute::isId).findFirst()
				.orElse(null);
	}
}
