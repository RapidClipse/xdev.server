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

package com.xdev.ui.entitycomponent.table;


import java.util.Collection;

import com.xdev.ui.entitycomponent.IDToBeanCollectionConverter;
import com.xdev.ui.entitycomponent.IDToBeanConverter;
import com.xdev.ui.paging.LazyLoadingUIModelProvider;
import com.xdev.ui.paging.XdevLazyEntityContainer;
import com.xdev.ui.util.KeyValueType;


public class XdevTable<T> extends AbstractBeanTable<T>
{
	
	/**
	 *
	 */
	private static final long serialVersionUID = -836170197198239894L;
	
	
	public XdevTable()
	{
		super();
	}
	
	
	// init defaults
	{
		setSelectable(true);
		setImmediate(true);
	}
	
	
	/**
	 * Creates a new empty table with caption.
	 *
	 * @param caption
	 */
	public XdevTable(final String caption)
	{
		super(caption);
	}
	
	
	public XdevTable(final int pageLength)
	{
		super();
		super.setPageLength(pageLength);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@SafeVarargs
	@SuppressWarnings({"rawtypes","unchecked"})
	@Override
	public final void setDataContainer(final Class<T> beanClass,
			final KeyValueType<?, ?>... nestedProperties)
	{
		final XdevLazyEntityContainer<T> container = this.getModelProvider().getModel(this,
				beanClass,nestedProperties);
				
		// there is no vaadin multiselectable interface or something similar
		// hence cant use strategies here.
		if(this.isMultiSelect())
		{
			this.setConverter(new IDToBeanCollectionConverter(container));
		}
		else
		{
			this.setConverter(new IDToBeanConverter<T>(container));
		}
		this.setDataContainer(container);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({"rawtypes","unchecked"})
	@SafeVarargs
	@Override
	public final void setDataContainer(final Class<T> beanClass, final Collection<T> data,
			final KeyValueType<?, ?>... nestedProperties)
	{
		final XdevLazyEntityContainer<T> container = this.getModelProvider().getModel(this,
				beanClass,nestedProperties);
		container.addAll(data);
		
		// there is no vaadin multiselectable interface or something similar
		// hence cant use strategies here.
		if(this.isMultiSelect())
		{
			this.setConverter(new IDToBeanCollectionConverter(container));
		}
		else
		{
			this.setConverter(new IDToBeanConverter<T>(container));
		}
		
		this.setDataContainer(container);
		
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.AbstractSelect#setMultiSelect(boolean)
	 */
	@SuppressWarnings({"rawtypes","unchecked"})
	@Override
	public void setMultiSelect(final boolean multiSelect)
	{
		super.setMultiSelect(multiSelect);
		this.setConverter(new IDToBeanCollectionConverter(this.getDataContainer()));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LazyLoadingUIModelProvider<T> getModelProvider()
	{
		return new LazyLoadingUIModelProvider<T>(this.getPageLength(),this.isReadOnly(),
				this.isSortEnabled());
	}
	
	
	@Override
	public void setPageLength(final int pageLength)
	{
		// FIXME property change to create new model!
		super.setPageLength(pageLength);
	}
}
