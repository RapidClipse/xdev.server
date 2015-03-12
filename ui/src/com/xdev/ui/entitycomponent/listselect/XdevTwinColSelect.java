/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */

package com.xdev.ui.entitycomponent.listselect;


import java.util.Collection;

import com.xdev.ui.paging.LazyLoadingUIModelProvider;
import com.xdev.ui.paging.XdevLazyEntityContainer;
import com.xdev.ui.util.KeyValueType;


/**
 * Multiselect component with two lists: left side for available items and right
 * side for selected items.
 *
 * @author XDEV Software
 *
 */
public class XdevTwinColSelect<T> extends AbstractTwinColSelect<T>
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


	/**
	 * {@inheritDoc}
	 */
	@SafeVarargs
	@Override
	public final void setModel(final Class<T> entityClass,
			final KeyValueType<?, ?>... nestedProperties)
	{
		this.setEntityDataSource(this.getModelProvider()
				.getModel(this,entityClass,nestedProperties));
	}


	@SafeVarargs
	@Override
	public final void setModel(final Class<T> entityClass, final Collection<T> data,
			final KeyValueType<?, ?>... nestedProperties)
	{
		final XdevLazyEntityContainer<T> container = this.getModelProvider().getModel(this,
				entityClass,nestedProperties);
		for(final T entity : data)
		{
			container.addEntity(entity);
		}

		this.setEntityDataSource(container);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LazyLoadingUIModelProvider<T> getModelProvider()
	{
		return new LazyLoadingUIModelProvider<T>(this.getRows(),false,false);
	}
}
