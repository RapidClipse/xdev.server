/*
 * Copyright (C) 2013-2019 by XDEV Software, All Rights Reserved.
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

package com.xdev.communication;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.xdev.util.EntityIDResolver;
import com.xdev.util.JPAEntityIDResolver;
import com.xdev.util.JPAMetaDataUtils;


/**
 * @author XDEV Software (JW)
 *
 */
public class URLParameterRegistry implements Serializable
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
		 * The key has to be a combination of property name and view so view properties
		 * can't overwrite each other.
		 */
		final URLKeyDescriptor parameterKey = new URLKeyDescriptor(viewName,propertyName);
		
		if(value != null && JPAMetaDataUtils.isManaged(value.getClass()))
		{
			// See XWS-545
			final Object id = getIdResolver().getEntityIDAttributeValue(value);
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
	
	
	public EntityIDResolver getIdResolver()
	{
		return JPAEntityIDResolver.getInstance();
	}


	/**
	 * @since 3.1
	 */
	public void removeAll(final String viewName)
	{
		this.internal.keySet().removeIf(d -> d.getViewName().equals(viewName));
	}
}
