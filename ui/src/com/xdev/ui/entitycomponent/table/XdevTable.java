
package com.xdev.ui.entitycomponent.table;


import java.util.Collection;

import com.xdev.ui.entitycomponent.IDToEntitySetConverter;
import com.xdev.ui.paging.LazyLoadingUIModelProvider;
import com.xdev.ui.paging.XdevLazyEntityContainer;
import com.xdev.ui.util.KeyValueType;


public class XdevTable<T> extends AbstractEntityTable<T>
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


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({"rawtypes","unchecked"})
	@SafeVarargs
	@Override
	public final void setModel(final Class<T> entityClass,
			final KeyValueType<?, ?>... nestedProperties)
	{
		final XdevLazyEntityContainer<T> container = this.getModelProvider().getModel(this,
				entityClass,nestedProperties);
		this.setEntityDataSource(container);

		// vaadin api compiler warnings
		this.setConverter(new IDToEntitySetConverter(container));
	}


	@SuppressWarnings({"rawtypes","unchecked"})
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

		// vaadin api compiler warnings
		this.setConverter(new IDToEntitySetConverter(container));
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
