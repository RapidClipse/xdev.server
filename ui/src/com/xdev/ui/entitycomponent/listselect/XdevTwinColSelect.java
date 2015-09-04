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

package com.xdev.ui.entitycomponent.listselect;


import java.util.Collection;

import com.xdev.ui.entitycomponent.XdevBeanContainer;
import com.xdev.ui.util.KeyValueType;


/**
 * Multiselect component with two lists: left side for available items and right
 * side for selected items.
 *
 * @author XDEV Software
 * 		
 */
public class XdevTwinColSelect<T> extends AbstractBeanTwinColSelect<T>
{
	/**
	 *
	 */
	public XdevTwinColSelect()
	{
		super();
	}
	
	
	/**
	 * @param caption
	 */
	public XdevTwinColSelect(final String caption)
	{
		super(caption);
	}
	
	
	// init defaults
	{
		setImmediate(true);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@SafeVarargs
	@Override
	public final void setContainerDataSource(final Class<T> beanClass, final boolean autoQueryData,
			final KeyValueType<?, ?>... nestedProperties)
	{
		this.setAutoQueryData(autoQueryData);
		final XdevBeanContainer<T> container = this.getModelProvider().getModel(this,beanClass,
				nestedProperties);
				
		// FIXME converter only working if container already set to component?
		this.setContainerDataSource(container);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@SafeVarargs
	@Override
	public final void setContainerDataSource(final Class<T> beanClass,
			final KeyValueType<?, ?>... nestedProperties)
	{
		this.setContainerDataSource(beanClass,true,nestedProperties);
	}
	
	
	@SafeVarargs
	@Override
	public final void setContainerDataSource(final Class<T> beanClass, final Collection<T> data,
			final KeyValueType<?, ?>... nestedProperties)
	{
		this.setAutoQueryData(false);
		final XdevBeanContainer<T> container = this.getModelProvider().getModel(this,beanClass,
				nestedProperties);
		container.addAll(data);
		
		this.setContainerDataSource(container);
	}
	
	// /*
	// * (non-Javadoc)
	// *
	// * @see com.vaadin.ui.AbstractField#setConverter(java.lang.Class)
	// */
	// @SuppressWarnings("unchecked")
	// @Override
	// public void setConverter(final Class<?> datamodelType)
	// {
	// // set converter only if utils find a suitable one.
	// final Converter<Object, ?> c = (Converter<Object,
	// ?>)ConverterUtil.getConverter(getType(),
	// datamodelType,getSession());
	// if(c != null)
	// {
	// setConverter(c);
	// }
	// }
	
}
