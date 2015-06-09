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

package com.xdev.ui.entitycomponent.grid;


import java.lang.reflect.Method;
import java.util.Collection;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.AbstractField;
import com.xdev.dal.DAOs;
import com.xdev.ui.paging.LazyLoadingUIModelProvider;
import com.xdev.ui.paging.XdevLazyEntityContainer;
import com.xdev.ui.util.KeyValueType;


/**
 * @author XDEV Software
 *
 */
public class XdevGrid<T> extends AbstractBeanGrid<T>
{
	/**
	 *
	 */
	private static final long	serialVersionUID	= -836170197198239894L;
	
	
	public XdevGrid()
	{
		super();
	}
	
	// init defaults
	{
		// setSelectable(true);
		setImmediate(true);
	}
	
	
	/**
	 * Creates a new empty table with caption.
	 *
	 * @param caption
	 */
	public XdevGrid(final String caption)
	{
		super(caption);
	}
	
	
	// public XdevGrid(final int pageLength)
	// {
	// super();
	// super.setPageLength(pageLength);
	// }
	
	/**
	 * {@inheritDoc}
	 */
	@SafeVarargs
	@Override
	public final void setDataContainer(final Class<T> beanClass,
			final KeyValueType<?, ?>... nestedProperties)
	{
		final XdevLazyEntityContainer<T> container = this.getModelProvider().getModel(this,
				beanClass,nestedProperties);
		this.setDataContainer(container);
		
		this.getModelProvider().setBeanConverter(container,this);
	}
	
	
	@SafeVarargs
	@Override
	public final void setDataContainer(final Class<T> beanClass, final Collection<T> data,
			final KeyValueType<?, ?>... nestedProperties)
	{
		final XdevLazyEntityContainer<T> container = this.getModelProvider().getModel(this,
				beanClass,nestedProperties);
		for(final T entity : data)
		{
			container.addBean(entity);
		}
		
		this.setDataContainer(container);
		
		this.getModelProvider().setBeanConverter(container,this);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LazyLoadingUIModelProvider<T> getModelProvider()
	{
		System.out.println(getHeightByRows());
		return new LazyLoadingUIModelProvider<T>(new Double(getHeightByRows()).intValue(),
				this.isReadOnly(),true);
	}
	
	
	@Override
	public void addValueChangeListener(final ValueChangeListener listener)
	{
		addListener(AbstractField.ValueChangeEvent.class,listener,VALUE_CHANGE_METHOD);
		// ensure "automatic immediate handling" works
		markAsDirty();
	}
	
	/* Value change events */
	
	private static final Method	VALUE_CHANGE_METHOD;
	
	static
	{
		try
		{
			VALUE_CHANGE_METHOD = Property.ValueChangeListener.class.getDeclaredMethod(
					"valueChange",new Class[]{Property.ValueChangeEvent.class});
		}
		catch(final java.lang.NoSuchMethodException e)
		{
			// This should never happen
			throw new java.lang.RuntimeException("Internal error finding methods in AbstractField");
		}
	}
	
	
	public Class<?> getColumnModelType(final Column column) throws RuntimeException
	{
		Class<?> modelType = null;
		try
		{
			final Method m = column.getClass().getDeclaredMethod("getModelType");
			m.setAccessible(true);
			modelType = (Class<?>)m.invoke(column);
		}
		catch(final Exception e)
		{
			throw new RuntimeException("Error while accesing method");
		}
		
		return modelType;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveEditor() throws CommitException
	{
		super.saveEditor();
		// persist changes
		final BeanItem<T> item = getDataContainer().getBeanItem(this.getEditedItemId());
		DAOs.get(item.getBean()).merge(item.getBean());
	}
}
