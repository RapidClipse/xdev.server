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
import com.xdev.ui.entitycomponent.BeanComponent;
import com.xdev.ui.entitycomponent.BeanContainer;
import com.xdev.ui.entitycomponent.UIModelProvider;


public abstract class AbstractBeanTable<BEANTYPE> extends Table implements
		BeanComponent<BEANTYPE>
{
	/**
	 *
	 */
	private static final long			serialVersionUID	= 897703398940222936L;
	
	private BeanContainer<BEANTYPE>	container;
	
	
	public AbstractBeanTable()
	{
		super();
	}
	
	
	public AbstractBeanTable(final String caption)
	{
		super(caption);
	}
	
	
	public AbstractBeanTable(final BeanContainer<BEANTYPE> dataSource)
	{
		super(null,dataSource);
		this.container = dataSource;
	}
	
	
	public AbstractBeanTable(final String caption, final BeanContainer<BEANTYPE> dataSource)
	{
		super(caption,dataSource);
		this.container = dataSource;
	}
	
	
	@Override
	public BeanContainer<BEANTYPE> getContainerDataSource()
	{
		return this.container;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public BeanItem<BEANTYPE> getItem(final Object itemId)
	{
		return this.container.getBeanItem(itemId);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BeanItem<BEANTYPE> getSelectedItem()
	{
		if(!this.isMultiSelect())
		{
			return this.container.getBeanItem(this.getValue());
		}
		return null;
	}
	
	
	protected abstract UIModelProvider<BEANTYPE> getModelProvider();
	
	
	@Override
	public void setDataContainer(final BeanContainer<BEANTYPE> newDataSource)
	{
		this.container = newDataSource;
		super.setContainerDataSource(newDataSource);
	}
	
	
	@Override
	public BeanContainer<BEANTYPE> getDataContainer()
	{
		return this.container;
	}
}
