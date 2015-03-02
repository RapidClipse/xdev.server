
package com.xdev.ui.entitycomponent.table;


import java.util.Collection;

import com.vaadin.data.util.BeanItem;
import com.xdev.ui.paging.LazyLoadingUIModelProvider;
import com.xdev.ui.paging.XdevLazyEntityContainer;
import com.xdev.ui.util.KeyValueType;


public class XdevTable<T> extends AbstractEntityTable<T, XdevLazyEntityContainer<T>>
{

	/**
	 *
	 */
	private static final long	serialVersionUID	= -836170197198239894L;


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


	/*
	 * see XdevLazyEntityContainer code it contains only BeanItems but stores it
	 * as Items due to framework inheritance restrictions. See
	 * RequisitioningEntityQuery<T>#toItem
	 */
	@SuppressWarnings("unchecked")
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BeanItem<T> getItem(final Object id)
	{
		return (BeanItem<T>)getContainerDataSource().getItem(id);
	}


	/**
	 * {@inheritDoc}
	 */
	@SafeVarargs
	@Override
	public final <K, V> void setModel(final Class<T> entityClass,
			final KeyValueType<K, V>... nestedProperties)
	{
		this.setGenericDataSource(this.getModelProvider().getModel(this,entityClass,
				nestedProperties));
	}


	@SafeVarargs
	@Override
	public final <K, V> void setModel(final Class<T> entityClass, final Collection<T> data,
			final KeyValueType<K, V>... nestedProperties)
	{
		final XdevLazyEntityContainer<T> container = this.getModelProvider().getModel(this,
				entityClass,nestedProperties);
		for(final T entity : data)
		{
			container.addEntity(entity);
		}

		this.setGenericDataSource(container);
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


	/*
	 * see XdevLazyEntityContainer code it contains only BeanItems but stores it
	 * as Items due to framework inheritance restrictions. See
	 * RequisitioningEntityQuery<T>#toItem
	 */
	@SuppressWarnings("unchecked")
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BeanItem<T> getSelectedItem()
	{
		if(!this.isMultiSelect())
		{
			return (BeanItem<T>)this.getContainerDataSource().getItem(this.getValue());
		}
		return null;
	}


	@Override
	public void setPageLength(final int pageLength)
	{
		// FIXME property change to create new model!
		super.setPageLength(pageLength);
	}
}
