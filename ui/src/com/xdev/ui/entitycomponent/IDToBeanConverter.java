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


import java.util.Locale;
import java.util.Set;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.Converter;
import com.xdev.util.EntityIDResolver;
import com.xdev.util.HibernateEntityIDResolver;


//TODO check if object as ID type is always suitable
public class IDToBeanConverter<T> implements Converter<Object, T>
{
	private final BeanContainer<T>	container;
	private final EntityIDResolver	idResolver;
	
	
	/**
	 *
	 */
	public IDToBeanConverter(final BeanContainer<T> container)
	{
		this.container = container;
		this.idResolver = new HibernateEntityIDResolver();
	}
	
	
	@Override
	public T convertToModel(final Object itemID, final Class<? extends T> targetType,
			final Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if(itemID == null)
		{
			return null;
		}
		
		// multi selection
		if(!(itemID instanceof Set))
		{
			final BeanItem<T> item = this.container.getBeanItem(itemID);
			if(item != null)
			{
				return item.getBean();
			}
		}
		
		return null;
	}
	
	
	@Override
	public Object convertToPresentation(final T value, final Class<? extends Object> targetType,
			final Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if(value != null)
		{
			return this.idResolver.getEntityIDPropertyValue(value);
		}
		return null;
	}
	
	
	@Override
	public Class<T> getModelType()
	{
		return this.container.getBeanType();
	}
	
	
	@Override
	public Class<Object> getPresentationType()
	{
		return Object.class;
	}
}
