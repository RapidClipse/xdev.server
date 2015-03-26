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
 
package com.xdev.ui.entitycomponent;


import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Set;

import org.hibernate.mapping.Property;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.Converter;
import com.xdev.server.util.EntityIDResolver;
import com.xdev.server.util.HibernateEntityIDResolver;


//TODO check if object as ID type is always suitable
public class IDToEntityConverter<T> implements Converter<Object, T>
{
	private final EntityContainer<T>	container;
	private final EntityIDResolver		idResolver;
	
	
	/**
	 *
	 */
	public IDToEntityConverter(final EntityContainer<T> container)
	{
		this.container = container;
		this.idResolver = new HibernateEntityIDResolver();
	}
	
	
	@Override
	public T convertToModel(final Object itemID, final Class<? extends T> targetType,
			final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if(itemID == null)
		{
			return null;
		}
		
		// multi selection
		if(!(itemID instanceof Set))
		{
			final BeanItem<T> item = this.container.getEntityItem(itemID);
			if(item != null)
			{
				return item.getBean();
			}
		}
		
		return null;
	}
	
	
	@Override
	public Object convertToPresentation(final T value, final Class<? extends Object> targetType,
			final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if(value != null)
		{
			final Property idProperty = this.idResolver.getEntityIDProperty(value.getClass());
			try
			{
				/*
				 * TODO rather make entity manager accesible within
				 * entitycontainer and use it for this purpose
				 */
				final Field idField = value.getClass().getDeclaredField(idProperty.getName());
				idField.setAccessible(true);
				return idField.get(value);
			}
			catch(NoSuchFieldException | SecurityException | IllegalArgumentException
					| IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
		}
		return null;
	}
	
	
	@Override
	public Class<T> getModelType()
	{
		return this.container.getEntityType();
	}
	
	
	@Override
	public Class<Object> getPresentationType()
	{
		return Object.class;
	}
}
