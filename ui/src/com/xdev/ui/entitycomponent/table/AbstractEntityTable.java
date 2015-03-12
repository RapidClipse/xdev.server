
package com.xdev.ui.entitycomponent.table;


import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Table;
import com.xdev.ui.entitycomponent.EntityComponent;
import com.xdev.ui.entitycomponent.EntityContainer;
import com.xdev.ui.entitycomponent.UIModelProvider;


public abstract class AbstractEntityTable<BEANTYPE> extends Table implements
		EntityComponent<BEANTYPE>
{
	/**
	 *
	 */
	private static final long			serialVersionUID	= 897703398940222936L;
	
	private EntityContainer<BEANTYPE>	container;
	
	
	public AbstractEntityTable()
	{
		super();
	}
	
	
	public AbstractEntityTable(final String caption)
	{
		super(caption);
	}
	
	
	public AbstractEntityTable(final EntityContainer<BEANTYPE> dataSource)
	{
		super(null,dataSource);
		this.container = dataSource;
	}
	
	
	public AbstractEntityTable(final String caption, final EntityContainer<BEANTYPE> dataSource)
	{
		super(caption,dataSource);
		this.container = dataSource;
	}
	
	
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
		if(!this.isMultiSelect())
		{
			return this.container.getEntityItem(this.getValue());
		}
		return null;
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
