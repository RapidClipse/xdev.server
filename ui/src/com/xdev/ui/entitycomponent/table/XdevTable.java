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
 * 
 * For further information see 
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.ui.entitycomponent.table;


import java.util.Collection;

import com.xdev.ui.XdevField;
import com.xdev.ui.entitycomponent.IDToBeanCollectionConverter;
import com.xdev.ui.entitycomponent.XdevBeanContainer;
import com.xdev.ui.util.KeyValueType;
import com.xdev.util.CaptionUtils;


/**
 * <p>
 * <code>Table</code> is used for representing data or components in a pageable
 * and selectable table.
 * </p>
 *
 * <p>
 * Scalability of the Table is largely dictated by the container. A table does
 * not have a limit for the number of items and is just as fast with hundreds of
 * thousands of items as with just a few. The current GWT implementation with
 * scrolling however limits the number of rows to around 500000, depending on
 * the browser and the pixel height of rows.
 * </p>
 *
 * <p>
 * Components in a Table will not have their caption nor icon rendered.
 * </p>
 *
 *
 * @author XDEV Software
 */
public class XdevTable<T> extends AbstractBeanTable<T> implements XdevField
{
	private final Extensions	extensions						= new Extensions();
	private boolean				persistValue					= PERSIST_VALUE_DEFAULT;

	/**
	 *
	 */
	private static final long	serialVersionUID				= -836170197198239894L;
																
	private boolean				autoUpdateRequiredProperties	= true;
																
																
	public XdevTable()
	{
		super();
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
	
	
	// init defaults
	{
		setSelectable(true);
		setImmediate(true);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E addExtension(final Class<? super E> type, final E extension)
	{
		return this.extensions.add(type,extension);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E getExtension(final Class<E> type)
	{
		return this.extensions.get(type);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPersistValue()
	{
		return this.persistValue;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPersistValue(final boolean persistValue)
	{
		this.persistValue = persistValue;
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@SafeVarargs
	@Override
	public final void setContainerDataSource(final Class<T> beanClass, final Collection<T> data,
			final KeyValueType<?, ?>... nestedProperties)
	{
		this.setAutoQueryData(false);
		final XdevBeanContainer<T> container = this.getModelProvider().getModel(this,beanClass,
				nestedProperties);
		container.setRequiredProperties(getVisibleColumns());
		container.addAll(data);
		
		try
		{
			this.autoUpdateRequiredProperties = false;

			this.setContainerDataSource(container);
		}
		finally
		{
			this.autoUpdateRequiredProperties = true;
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.Table#setVisibleColumns(java.lang.Object[])
	 */
	@Override
	public void setVisibleColumns(final Object... visibleColumns)
	{
		super.setVisibleColumns(visibleColumns);
		
		if(this.autoUpdateRequiredProperties)
		{
			final XdevBeanContainer<T> beanContainer = getContainerDataSource();
			if(beanContainer != null)
			{
				beanContainer.setRequiredProperties(visibleColumns);
			}
		}
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
		if(this.isAutoQueryData())
		{
			this.setConverter(new IDToBeanCollectionConverter(this.getContainerDataSource()));
		}
	}
	
	
	@Override
	public void setPageLength(final int pageLength)
	{
		// FIXME property change to create new model!
		super.setPageLength(pageLength);
	}
	
	
	@Override
	public String getColumnHeader(final Object propertyId)
	{
		String header = super.getColumnHeader(propertyId);
		if(header.equals(propertyId.toString()))
		{
			// Header not set explicitly, resolve @Caption
			final XdevBeanContainer<T> container = getContainerDataSource();
			if(container != null)
			{
				header = CaptionUtils.resolveEntityMemberCaption(container.getBeanType(),header);
			}
		}
		return header;
	}
}
