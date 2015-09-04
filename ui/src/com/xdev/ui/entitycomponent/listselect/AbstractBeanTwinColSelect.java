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

package com.xdev.ui.entitycomponent.listselect;


import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.TwinColSelect;
import com.xdev.ui.entitycomponent.BeanComponent;
import com.xdev.ui.entitycomponent.UIModelProvider;
import com.xdev.ui.entitycomponent.XdevBeanContainer;
import com.xdev.ui.paging.LazyLoadingUIModelProvider;


public abstract class AbstractBeanTwinColSelect<BEANTYPE> extends TwinColSelect
		implements BeanComponent<BEANTYPE>
{
	/**
	 *
	 */
	private static final long	serialVersionUID	= 897703398940222936L;
	private boolean				autoQueryData		= true;
	
	
	public AbstractBeanTwinColSelect()
	{
		super();
	}
	
	
	public AbstractBeanTwinColSelect(final String caption)
	{
		super(caption);
	}
	
	
	public AbstractBeanTwinColSelect(final XdevBeanContainer<BEANTYPE> dataSource)
	{
		super(null,dataSource);
	}
	
	
	public AbstractBeanTwinColSelect(final String caption,
			final XdevBeanContainer<BEANTYPE> dataSource)
	{
		super(caption,dataSource);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public XdevBeanContainer<BEANTYPE> getContainerDataSource()
	{
		if(super.getContainerDataSource() instanceof XdevBeanContainer)
		{
			return (XdevBeanContainer<BEANTYPE>)super.getContainerDataSource();
		}
		// else
		// {
		// throw new RuntimeException(
		// "While using BeanComponents a fitting XdevBeanContainer must be
		// set");
		// }
		return null;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setContainerDataSource(final Container newDataSource)
	{
		if(newDataSource instanceof XdevBeanContainer)
		{
			super.setContainerDataSource(newDataSource);
			this.getModelProvider().setRelatedModelConverter(this,
					(XdevBeanContainer<BEANTYPE>)newDataSource);
		}
		else
		{
			super.setContainerDataSource(newDataSource);
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.xdev.ui.entitycomponent.BeanComponent#isAutoQueryData()
	 */
	@Override
	public boolean isAutoQueryData()
	{
		return this.autoQueryData;
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.xdev.ui.entitycomponent.BeanComponent#autoQueryData(boolean)
	 */
	@Override
	public void setAutoQueryData(final boolean autoQuery)
	{
		this.autoQueryData = autoQuery;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BeanItem<BEANTYPE> getItem(final Object itemId)
	{
		return this.getContainerDataSource().getItem(itemId);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BeanItem<BEANTYPE> getSelectedItem()
	{
		return this.getContainerDataSource().getItem(this.getValue());
	}
	
	
	protected UIModelProvider<BEANTYPE> getModelProvider()
	{
		if(this.isAutoQueryData())
		{
			return new LazyLoadingUIModelProvider<BEANTYPE>(this.getRows(),false,false);
		}
		else
		{
			return new UIModelProvider.Implementation<BEANTYPE>();
		}
	}
}
