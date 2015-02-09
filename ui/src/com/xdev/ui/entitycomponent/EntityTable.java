
package com.xdev.ui.entitycomponent;


import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.xdev.server.util.KeyValueType;


public class EntityTable<T> extends AbstractEntityTable<T, BeanItemContainer<T>>
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


	@Override
	protected UIModelProvider.Implementation<T> getModelProvider()
	{
		return new UIModelProvider.Implementation<T>();
	}


	@Override
	public BeanItem<T> getSelectedItem()
	{
		if(!this.isMultiSelect())
		{
			return this.getContainerDataSource().getItem(this.getValue());
		}
		return null;
	}
}
