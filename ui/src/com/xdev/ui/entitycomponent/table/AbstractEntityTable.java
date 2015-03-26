/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */
 
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
