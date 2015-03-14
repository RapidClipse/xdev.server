
package com.xdev.ui.entitycomponent.combobox;


import java.util.Collection;

import com.xdev.ui.paging.LazyLoadingUIModelProvider;
import com.xdev.ui.paging.XdevLazyEntityContainer;
import com.xdev.ui.util.KeyValueType;


public class XdevComboBox<T> extends AbstractEntityComboBox<T>
{
	
	/**
	 *
	 */
	private static final long	serialVersionUID	= -836170197198239894L;
	
	
	public XdevComboBox()
	{
		super();
	}
	
	
	public XdevComboBox(final int pageLength)
	{
		super();
		super.setPageLength(pageLength);
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
		return new LazyLoadingUIModelProvider<T>(this.getPageLength(),this.isTextInputAllowed(),
				false);
	}
	
	
	@Override
	public void setPageLength(final int pageLength)
	{
		// FIXME property change to create new model!
		super.setPageLength(pageLength);
	}
}
