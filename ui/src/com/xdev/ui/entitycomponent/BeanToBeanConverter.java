/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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

package com.xdev.ui.entitycomponent;


import java.util.Locale;

import com.vaadin.v7.data.util.BeanItem;
import com.vaadin.v7.data.util.converter.Converter;
import com.xdev.util.JPAEntityIDResolver;
import com.xdev.util.JPAMetaDataUtils;


@SuppressWarnings("deprecation")
public class BeanToBeanConverter<T> implements Converter<T, T>
{
	private final XdevBeanContainer<T> container;
	
	
	/**
	 *
	 */
	public BeanToBeanConverter(final XdevBeanContainer<T> container)
	{
		this.container = container;
	}
	
	
	@Override
	public T convertToModel(final T bean, final Class<? extends T> targetType, final Locale locale)
			throws Converter.ConversionException
	{
		return bean;
	}
	
	
	@Override
	public T convertToPresentation(final T value, final Class<? extends T> targetType,
			final Locale locale) throws Converter.ConversionException
	{
		if(value == null)
		{
			return null;
		}
		
		final BeanItem<T> item = this.container.getItem(value);
		if(item != null)
		{
			// same object
			return item.getBean();
		}

		if(!JPAMetaDataUtils.isManaged(value.getClass()))
		{
			return value;
		}
		
		// find by id
		final JPAEntityIDResolver idResolver = JPAEntityIDResolver.getInstance();
		final Object id = idResolver.getEntityIDAttributeValue(value);
		if(id == null)
		{
			return value;
		}
		final T containerValue = this.container.getItemIds().stream()
				.map(propertyId -> this.container.getItem(propertyId).getBean())
				.filter(bean -> bean.equals(value)
						|| (id != null && id.equals(idResolver.getEntityIDAttributeValue(bean))))
				.findFirst().orElse(null);
		return containerValue != null ? containerValue : value;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getModelType()
	{
		return (Class<T>)this.container.getBeanType();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getPresentationType()
	{
		return (Class<T>)this.container.getBeanType();
	}
}
