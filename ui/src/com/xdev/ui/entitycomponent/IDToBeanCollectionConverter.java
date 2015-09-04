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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.hibernate.mapping.Property;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.Converter;
import com.xdev.util.EntityIDResolver;
import com.xdev.util.HibernateEntityIDResolver;


//TODO check if object as ID type is always suitable
public class IDToBeanCollectionConverter<T>
		implements Converter<Set<T>, Collection<? extends Object>>
{
	private final XdevBeanContainer<T>	container;
	private final EntityIDResolver		idResolver;
	private Collection<T>				beanCollection	= new ArrayList<T>();
	private Collection<Object>			idCollection	= new HashSet<>();
	
	
	/**
	 *
	 */
	public IDToBeanCollectionConverter(final XdevBeanContainer<T> container)
	{
		this.container = container;
		this.idResolver = new HibernateEntityIDResolver();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<Collection<? extends Object>> getModelType()
	{
		return (Class<Collection<? extends Object>>)this.idCollection.getClass();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<T>> getPresentationType()
	{
		return (Class<Set<T>>)this.beanCollection.getClass();
		
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
	 * java.lang.Class, java.util.Locale)
	 */
	@Override
	public Collection<? extends Object> convertToModel(final Set<T> itemIds,
			final Class<? extends Collection<? extends Object>> targetType, final Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		// TODO create suitable type provider
		if(targetType.isAssignableFrom(List.class))
		{
			this.beanCollection = new ArrayList<>();
		}
		else
		{
			this.beanCollection = new HashSet<>();
		}

		if(itemIds != null)
		{
			for(final Object itemId : itemIds)
			{
				final BeanItem<T> item = this.container.getItem(itemId);
				if(item != null)
				{
					this.beanCollection.add(item.getBean());
				}
			}
			return this.beanCollection;
		}

		return this.beanCollection;
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
	 * .Object, java.lang.Class, java.util.Locale)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Set<T> convertToPresentation(final Collection<? extends Object> values,
			final Class<? extends Set<T>> targetType, final Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		this.idCollection = new HashSet<>();
		if(values != null)
		{
			for(final Object bean : values)
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
					
					this.idCollection.add(idField.get(bean));
				}
				catch(NoSuchFieldException | SecurityException | IllegalArgumentException
						| IllegalAccessException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		return (Set<T>)this.idCollection;
	}
	
}
