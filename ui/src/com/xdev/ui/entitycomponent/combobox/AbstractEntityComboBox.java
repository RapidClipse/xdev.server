
package com.xdev.ui.entitycomponent.combobox;


import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComboBox;
import com.xdev.ui.entitycomponent.EntityComponent;
import com.xdev.ui.entitycomponent.EntityContainer;
import com.xdev.ui.entitycomponent.UIModelProvider;


public abstract class AbstractEntityComboBox<BEANTYPE> extends ComboBox implements
		EntityComponent<BEANTYPE>
{
	/**
	 *
	 */
	private static final long			serialVersionUID	= 897703398940222936L;
	private EntityContainer<BEANTYPE>	container;
	
	
	public AbstractEntityComboBox()
	{
		super();
	}
	
	
	public AbstractEntityComboBox(final String caption)
	{
		super(caption);
	}
	
	
	public AbstractEntityComboBox(final EntityContainer<BEANTYPE> dataSource)
	{
		super(null,dataSource);
	}
	
	
	public AbstractEntityComboBox(final String caption, final EntityContainer<BEANTYPE> dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityContainer<BEANTYPE> getContainerDataSource()
	{
		return this.container;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BeanItem<BEANTYPE> getItem(final Object itemId)
	{
		return this.container.getEntityItem(itemId);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BeanItem<BEANTYPE> getSelectedItem()
	{
		return this.container.getEntityItem(this.getValue());
	}
	
	
	protected abstract UIModelProvider<BEANTYPE> getModelProvider();
	
	
	@Override
	public void setEntityDataSource(final EntityContainer<BEANTYPE> newDataSource)
	{
		this.container = newDataSource;
		super.setContainerDataSource(newDataSource);
	}
	
	
	@Override
	public EntityContainer<BEANTYPE> getEntityDataSource()
	{
		return this.container;
	}
}
