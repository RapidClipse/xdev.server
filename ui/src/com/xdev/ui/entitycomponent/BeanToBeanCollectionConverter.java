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


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.vaadin.v7.data.util.converter.Converter;
import com.xdev.util.JPAEntityIDResolver;


@SuppressWarnings("deprecation")
public class BeanToBeanCollectionConverter<T> implements Converter<Set<T>, Collection<? extends T>>
{
	private final XdevBeanContainer<T>	container;
	private Collection<T>				modelCollection			= new ArrayList<T>();
	private Collection<Object>			presentationCollection	= new HashSet<>();
	
	
	/**
	 *
	 */
	public BeanToBeanCollectionConverter(final XdevBeanContainer<T> container)
	{
		this.container = container;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<Collection<? extends T>> getModelType()
	{
		return (Class<Collection<? extends T>>)this.presentationCollection.getClass();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<T>> getPresentationType()
	{
		return (Class<Set<T>>)this.modelCollection.getClass();
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
	 * java.lang.Class, java.util.Locale)
	 */
	@Override
	public Collection<? extends T> convertToModel(final Set<T> itemIds,
			final Class<? extends Collection<? extends T>> targetType, final Locale locale)
			throws Converter.ConversionException
	{
		// TODO create suitable type provider
		if(targetType.isAssignableFrom(List.class))
		{
			this.modelCollection = new ArrayList<>();
		}
		else
		{
			this.modelCollection = new HashSet<>();
		}
		
		if(itemIds != null)
		{
			this.modelCollection.addAll(itemIds);
		}
		
		return this.modelCollection;
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
	 * .Object, java.lang.Class, java.util.Locale)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Set<T> convertToPresentation(final Collection<? extends T> values,
			final Class<? extends Set<T>> targetType, final Locale locale)
			throws Converter.ConversionException
	{
		this.presentationCollection = new HashSet<>();
		
		if(values != null)
		{
			for(final T bean : values)
			{
				this.presentationCollection.add(convertToPresentation(bean));
			}
		}
		
		return (Set<T>)this.presentationCollection;
	}
	
	
	protected T convertToPresentation(final T value)
	{
		if(value == null)
		{
			return null;
		}
		
		final JPAEntityIDResolver idResolver = JPAEntityIDResolver.getInstance();
		final Object id = idResolver.getEntityIDAttributeValue(value);
		final T containerValue = this.container.getItemIds().stream()
				.map(propertyId -> this.container.getItem(propertyId).getBean())
				.filter(bean -> bean.equals(value)
						|| (id != null && id.equals(idResolver.getEntityIDAttributeValue(bean))))
				.findFirst().orElse(null);
		return containerValue != null ? containerValue : value;
	}
}
