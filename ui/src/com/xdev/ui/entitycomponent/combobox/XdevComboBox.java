
package com.xdev.ui.entitycomponent.combobox;


import com.vaadin.data.util.BeanItem;
import com.xdev.server.util.KeyValueType;
import com.xdev.ui.paging.LazyLoadingUIModelProvider;
import com.xdev.ui.paging.XdevLazyEntityContainer;


public class XdevComboBox<T> extends AbstractEntityComboBox<T, XdevLazyEntityContainer<T>>
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


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LazyLoadingUIModelProvider<T> getModelProvider()
	{
		return new LazyLoadingUIModelProvider<T>(this.getPageLength(),this.isTextInputAllowed(),
				false);
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
		return (BeanItem<T>)this.getContainerDataSource().getItem(this.getValue());
	}


	@Override
	public void setPageLength(final int pageLength)
	{
		// FIXME property change to create new model!
		super.setPageLength(pageLength);
	}
}
