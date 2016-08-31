/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * For further information see
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.ui.entitycomponent.listselect;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.TwinColSelect;
import com.xdev.ui.entitycomponent.BeanComponent;
import com.xdev.ui.entitycomponent.IDToBeanCollectionConverter;
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
	public XdevBeanContainer<BEANTYPE> getBeanContainerDataSource()
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
		
		updateConverter();
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.AbstractSelect#setMultiSelect(boolean)
	 */
	@Override
	public void setMultiSelect(final boolean multiSelect)
	{
		super.setMultiSelect(multiSelect);
		
		updateConverter();
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

		updateConverter();
	}


	/**
	 * @since 3.0
	 */
	@SuppressWarnings({"rawtypes","unchecked"})
	protected void updateConverter()
	{
		XdevBeanContainer<?> beanContainerDataSource;
		if(isAutoQueryData() && isMultiSelect()
				&& (beanContainerDataSource = this.getBeanContainerDataSource()) != null)
		{
			this.setConverter(new IDToBeanCollectionConverter(beanContainerDataSource));
		}
		else if(this.getConverter() instanceof IDToBeanCollectionConverter)
		{
			this.setConverter((Converter)null);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public BeanItem<BEANTYPE> getBeanItem(final Object itemId)
	{
		return this.getBeanContainerDataSource().getItem(itemId);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public BeanItem<BEANTYPE> getSelectedItem()
	{
		return this.getBeanContainerDataSource().getItem(this.getValue());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BeanItem<BEANTYPE>> getSelectedItems()
	{
		if(this.isMultiSelect())
		{
			final XdevBeanContainer<BEANTYPE> container = this.getBeanContainerDataSource();
			return ((Collection<?>)this.getValue()).stream().map(id -> container.getItem(id))
					.collect(Collectors.toList());
		}
		else
		{
			final List<BeanItem<BEANTYPE>> list = new ArrayList<>(1);
			list.add(getSelectedItem());
			return list;
		}
	}


	protected UIModelProvider<BEANTYPE> getModelProvider()
	{
		if(this.isAutoQueryData())
		{
			/*
			 * TWINCOlSELECT does not precalculate viewport size and does not
			 * set the row property!!!
			 */
			return new LazyLoadingUIModelProvider<BEANTYPE>(this.getRows(),false,false);
		}
		else
		{
			return new UIModelProvider.Implementation<BEANTYPE>();
		}
	}
}
