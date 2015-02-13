
package com.xdev.ui.entitycomponent.combobox;


import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.ComboBox;
import com.xdev.server.util.KeyValueType;
import com.xdev.ui.entitycomponent.GenericEntityComponent;
import com.xdev.ui.entitycomponent.UIModelProvider;


public abstract class AbstractEntityComboBox<BEANTYPE, T extends Container> extends ComboBox
		implements GenericEntityComponent<BEANTYPE, T>
{
	/**
	 *
	 */
	private static final long	serialVersionUID	= 897703398940222936L;
	private T					container;


	public AbstractEntityComboBox()
	{
		super();
	}


	public AbstractEntityComboBox(final String caption)
	{
		super(caption);
	}


	public AbstractEntityComboBox(final T dataSource)
	{
		super(null,dataSource);
	}


	public AbstractEntityComboBox(final String caption, final T dataSource)
	{
		super(caption,dataSource);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getContainerDataSource()
	{
		return this.container;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGenericDataSource(final T newDataSource)
	{
		this.container = newDataSource;
		super.setContainerDataSource(newDataSource);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item getItem(final Object itemId)
	{
		return super.getItem(itemId);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item getSelectedItem()
	{
		return this.getContainerDataSource().getItem(this.getValue());
	}


	protected abstract UIModelProvider<BEANTYPE> getModelProvider();


	@Override
	public abstract <K, V> void setModel(Class<BEANTYPE> entityClass,
			@SuppressWarnings("unchecked") KeyValueType<K, V>... nestedProperties);
}
