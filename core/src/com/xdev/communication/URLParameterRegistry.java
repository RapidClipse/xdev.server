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

package com.xdev.communication;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.metamodel.EntityType;

import com.xdev.util.HibernateEntityIDResolver;


/**
 * @author XDEV Software
 *
 */
public class URLParameterRegistry
{
	private final Map<URLKeyDescriptor, URLParameterRegistryValue>	internal;
	private Set<EntityType<?>>										entities;
	private HibernateEntityIDResolver								idResolver;
	
	
	public Set<EntityType<?>> getEntities()
	{
		if(this.entities != null)
		{
			return this.entities;
		}
		else
		{
			this.entities = EntityManagerHelper.getEntityManager().getMetamodel().getEntities();
			return this.entities;
		}
	}
	
	
	public HibernateEntityIDResolver getIdResolver()
	{
		if(this.idResolver != null)
		{
			return this.idResolver;
		}
		else
		{
			this.idResolver = new HibernateEntityIDResolver();
			return this.idResolver;
		}
	}
	
	
	/**
	 *
	 */
	public URLParameterRegistry()
	{
		super();
		this.internal = new HashMap<>();
	}
	
	
	/*
	 * Key muss aus propertyname+view bestehen damit sich view properties
	 * gegenseitig nicht überschreiben können
	 */
	public URLParameterDescriptor put(final Object value, final String viewName,
			final String propertyName)
	{
		final URLKeyDescriptor parameterKey = new URLKeyDescriptor(viewName,propertyName);
		for(final EntityType<?> entityType : this.getEntities())
		{
			if(entityType.getJavaType().equals(value.getClass()))
			{
				try
				{
					final String pkFieldName = this.getIdResolver()
							.getEntityIDProperty(value.getClass()).getName();
					final Field f = value.getClass().getDeclaredField(pkFieldName);
					f.setAccessible(true);
					this.internal.put(parameterKey,new URLParameterRegistryValue(value.getClass(),
							null,(Serializable)f.get(value),parameterKey.getPropertyName()));
				}
				catch(IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException e)
				{
					throw new RuntimeException(e);
				}
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
	
}
