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
	private final XdevBeanContainer<T>	container;
	private final EntityIDResolver		idResolver;
										
										
	/**
	 *
	 */
	public IDToBeanConverter(final XdevBeanContainer<T> container)
	{
		this.container = container;
		this.idResolver = new HibernateEntityIDResolver();
	}
	
	
	@Override
	public T convertToModel(final Object itemID, final Class<? extends T> targetType,
			final Locale locale) throws Converter.ConversionException
	{
		if(itemID == null)
		{
			return null;
		}
		
		// multi selection
		if(!(itemID instanceof Set))
		{
			final BeanItem<T> item = this.container.getItem(itemID);
			if(item != null)
			{
				return item.getBean();
			}
		}
		
		return null;
	}
	
	
	@Override
	public Object convertToPresentation(final T value, final Class<? extends Object> targetType,
			final Locale locale) throws Converter.ConversionException
	{
		if(value != null)
		{
			return this.idResolver.getEntityIDPropertyValue(value);
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getModelType()
	{
		return (Class<T>)this.container.getBeanType();
	}
	
	
	@Override
	public Class<Object> getPresentationType()
	{
		return Object.class;
	}
}
