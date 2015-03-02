
package com.xdev.ui.entitycomponent.combobox;


import java.util.Collection;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.xdev.ui.entitycomponent.UIModelProvider;
import com.xdev.ui.util.KeyValueType;


public class EntityComboBox<T> extends AbstractEntityComboBox<T, BeanItemContainer<T>>
{
	/**
	 *
	 */
	private static final long	serialVersionUID	= 8319108515219631399L;


	@Override
	public BeanItem<T> getItem(final Object id)
	{
		return super.getContainerDataSource().getItem(id);
	}


	@Override
	@SafeVarargs
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
		final BeanItemContainer<T> container = this.getModelProvider().getModel(this,entityClass,
				nestedProperties);
		for(final T entity : data)
		{
			container.addBean(entity);
		}

		this.setGenericDataSource(container);
	}


	@Override
	protected UIModelProvider.Implementation<T> getModelProvider()
	{
		return new UIModelProvider.Implementation<T>();
	}
}
