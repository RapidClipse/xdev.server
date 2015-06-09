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

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;


/**
 * Converter for containers which do hold actual beans to choose the beans
 * presentation representation.
 *
 * @author XDEV Software
 *
 * @see BeanItemContainer
 */
public class BeanDelegatorConverter implements Converter<Object, Object>
{
	private final Class<?>	modelType;
	private final Class<?>	presentationType;
	
	
	/**
	 *
	 */
	public BeanDelegatorConverter(final Class<?> modelType, final Class<?> presentationType)
	{
		this.modelType = modelType;
		this.presentationType = presentationType;
	}
	
	
	@Override
	public Object convertToModel(final Object value, final Class<?> targetType, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException, RuntimeException
	{
		/*
		 * gridconverter uses already beanitems as column editors which contain
		 * the actual bean so just passing the instance here.
		 */
		return value;
	}
	
	
	@Override
	public Object convertToPresentation(final Object value,
			final Class<? extends Object> targetType, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if(value == null)
		{
			return null;
		}
		
		/*
		 * FIXME either require toString or caption annotation and just
		 */
		return value.toString();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<Object> getModelType()
	{
		return (Class<Object>)this.modelType;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<Object> getPresentationType()
	{
		return (Class<Object>)this.presentationType;
	}
}
