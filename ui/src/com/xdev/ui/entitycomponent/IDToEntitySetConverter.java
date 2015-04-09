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
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import org.hibernate.mapping.Property;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.Converter;
import com.xdev.util.EntityIDResolver;
import com.xdev.util.HibernateEntityIDResolver;


//TODO check if object as ID type is always suitable
public class IDToEntitySetConverter<T> implements Converter<Set<? extends Object>, Set<T>>
{
	private final EntityContainer<T>	container;
	private final EntityIDResolver		idResolver;
	private Set<T>						beanSet	= new LinkedHashSet<>();
	private Set<Object>					idSet	= new LinkedHashSet<>();
	
	
	/**
	 *
	 */
	public IDToEntitySetConverter(final EntityContainer<T> container)
	{
		this.container = container;
		this.idResolver = new HibernateEntityIDResolver();
	}
	
	
	@Override
	public Set<T> convertToModel(final Set<? extends Object> itemIds,
			final Class<? extends Set<T>> targetType, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		this.beanSet = new LinkedHashSet<>();
		if(itemIds != null)
		{
			for(final Object itemId : itemIds)
			{
				final BeanItem<T> item = this.container.getEntityItem(itemId);
				if(item != null)
				{
					this.beanSet.add(item.getBean());
				}
			}
			return this.beanSet;
		}
		
		return this.beanSet;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<T>> getModelType()
	{
		return (Class<Set<T>>)this.beanSet.getClass();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<? extends Object>> getPresentationType()
	{
		return (Class<Set<? extends Object>>)this.idSet.getClass();
	}
	
	
	@Override
	public Set<? extends Object> convertToPresentation(final Set<T> values,
			final Class<? extends Set<? extends Object>> targetType, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		this.idSet = new LinkedHashSet<>();
		if(values != null)
		{
			for(final T bean : values)
			{
				try
				{
					/*
					 * TODO rather make entity manager accesible within
					 * entitycontainer and use it for this purpose
					 */
					final Property idProperty = this.idResolver
							.getEntityIDProperty(bean.getClass());
					final Field idField = bean.getClass().getDeclaredField(idProperty.getName());
					idField.setAccessible(true);
					
					this.idSet.add(idField.get(bean));
				}
				catch(NoSuchFieldException | SecurityException | IllegalArgumentException
						| IllegalAccessException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		return this.idSet;
	}
	
}
