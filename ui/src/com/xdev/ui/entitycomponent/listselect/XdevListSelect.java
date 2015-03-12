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
 * This is a simple list select without, for instance, support for new items,
 * lazyloading, and other advanced features.
 *
 * @author XDEV Software
 *
 */
public class XdevListSelect<T> extends AbstractEntityListSelect<T>
{
	/**
	 *
	 */
	public XdevListSelect()
	{
		super();
	}


	/**
	 * @param caption
	 */
	public XdevListSelect(final String caption)
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
