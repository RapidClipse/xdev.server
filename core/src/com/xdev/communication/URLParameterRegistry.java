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
 */

package com.xdev.communication;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.metamodel.EntityType;

import com.xdev.util.EntityIDResolver;
import com.xdev.util.HibernateEntityIDResolver;
import com.xdev.util.JPAMetaDataUtils;


/**
 * @author XDEV Software (JW)
 * 		
 */
public class URLParameterRegistry
{
	private final Map<URLKeyDescriptor, URLParameterRegistryValue> internal;
	// private Set<EntityType<?>> entities;
	
	
	/**
	 *
	 */
	public URLParameterRegistry()
	{
		super();
		this.internal = new HashMap<>();
	}


	public URLParameterDescriptor put(final Object value, final String viewName,
			final String propertyName)
	{
		/*
		 * The key has to be a combination of property name and view so view
		 * properties can't overwrite each other.
		 */
		final URLKeyDescriptor parameterKey = new URLKeyDescriptor(viewName,propertyName);

		if(value != null && JPAMetaDataUtils.isManaged(value.getClass()))
		{
			// See XWS-545
			final Object id = getIdResolver().getEntityIDPropertyValue(value);
			if(id instanceof Serializable)
			{
				this.internal.put(parameterKey,new URLParameterRegistryValue(value.getClass(),null,
						(Serializable)id,parameterKey.getPropertyName()));
				return new URLParameterDescriptor(value.getClass(),parameterKey.getPropertyName());
			}
		}

		this.internal.put(parameterKey,new URLParameterRegistryValue(value.getClass(),value,null,
				parameterKey.getPropertyName()));
		return new URLParameterDescriptor(value.getClass(),parameterKey.getPropertyName());
	}


	public URLParameterRegistryValue get(final URLKeyDescriptor key)
	{
		return this.internal.get(key);
	}


	public URLParameterRegistryValue get(final String viewName, final String parameterName)
	{
		return this.internal.get(new URLKeyDescriptor(viewName,parameterName));
	}


	public Collection<URLParameterRegistryValue> getValues(final String viewName)
	{
		final Collection<URLParameterRegistryValue> valuesForView = new ArrayList<>();

		for(final URLKeyDescriptor key : this.internal.keySet())
		{
			if(key.getViewName().equals(viewName))
			{
				valuesForView.add(this.internal.get(key));
			}
		}

		return valuesForView;
	}


	/**
	 * @deprecated not used anymore, will be removed in a future release
	 * @return <code>null</code>
	 */
	@Deprecated
	public Set<EntityType<?>> getEntities()
	{
		// if(this.entities == null)
		// {
		// final EntityManager entityManager =
		// EntityManagerUtils.getEntityManager();
		// if(entityManager != null)
		// {
		// this.entities = entityManager.getMetamodel().getEntities();
		// }
		// }
		//
		// return this.entities;
		return null;
	}


	public EntityIDResolver getIdResolver()
	{
		return HibernateEntityIDResolver.getInstance();
	}
}
