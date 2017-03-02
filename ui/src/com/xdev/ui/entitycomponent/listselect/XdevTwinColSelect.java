/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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


import java.util.Collection;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.server.Resource;
import com.xdev.ui.ItemCaptionProvider;
import com.xdev.ui.ItemIconProvider;
import com.xdev.ui.XdevSelect;
import com.xdev.ui.entitycomponent.XdevBeanContainer;
import com.xdev.ui.paging.XdevEntityLazyQueryContainer;
import com.xdev.ui.util.KeyValueType;


/**
 * Multiselect component with two lists: left side for available items and right
 * side for selected items.
 *
 * @author XDEV Software
 *
 */
public class XdevTwinColSelect<T> extends AbstractBeanTwinColSelect<T> implements XdevSelect<T>
{
	private final Extensions		extensions					= new Extensions();
	private boolean					persistValue				= PERSIST_VALUE_DEFAULT;
	private boolean					itemCaptionFromAnnotation	= true;
	private String					itemCaptionValue;
	private ItemCaptionProvider<T>	itemCaptionProvider;
	private ItemIconProvider<T>		itemIconProvider;


	/**
	 *
	 */
	public XdevTwinColSelect()
	{
		super();
	}
	
	
	/**
	 * @param caption
	 */
	public XdevTwinColSelect(final String caption)
	{
		super(caption);
	}

	// init defaults
	{
		setImmediate(true);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E addExtension(final Class<? super E> type, final E extension)
	{
		return this.extensions.add(type,extension);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E getExtension(final Class<E> type)
	{
		return this.extensions.get(type);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPersistValue()
	{
		return this.persistValue;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPersistValue(final boolean persistValue)
	{
		this.persistValue = persistValue;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@SafeVarargs
	@Override
	public final void setContainerDataSource(final Class<T> beanClass, final boolean autoQueryData,
			final KeyValueType<?, ?>... nestedProperties)
	{
		this.setAutoQueryData(autoQueryData);
		final XdevBeanContainer<T> container = this.getModelProvider().getModel(this,beanClass,
				nestedProperties);

		// Workaround for TwinColSelect internal size behavior.
		if(container instanceof XdevEntityLazyQueryContainer)
		{
			final XdevEntityLazyQueryContainer lqc = (XdevEntityLazyQueryContainer)container;
			final List<Item> items = lqc.getQueryView().getQuery().loadItems(0,Integer.MAX_VALUE);
			/*
			 * manualy set batch size because twincolselect does not calucalte
			 * its own internal row count / size
			 */
			lqc.getQueryView().getQueryDefinition().setBatchSize(items.size());
			for(int i = 0; i < items.size(); i++)
			{
				// triggers internal add
				lqc.getQueryView().getItem(i);
			}
		}
		this.setContainerDataSource(container);
	}
	
	
	@SafeVarargs
	@Override
	public final void setContainerDataSource(final Class<T> beanClass, final Collection<T> data,
			final KeyValueType<?, ?>... nestedProperties)
	{
		this.setAutoQueryData(false);
		final XdevBeanContainer<T> container = this.getModelProvider().getModel(this,beanClass,
				nestedProperties);
		container.addAll(data);
		
		this.setContainerDataSource(container);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setItemCaptionFromAnnotation(final boolean itemCaptionFromAnnotation)
	{
		this.itemCaptionFromAnnotation = itemCaptionFromAnnotation;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isItemCaptionFromAnnotation()
	{
		return this.itemCaptionFromAnnotation;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setItemCaptionValue(final String itemCaptionValue)
	{
		this.itemCaptionValue = itemCaptionValue;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getItemCaptionValue()
	{
		return this.itemCaptionValue;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setItemCaptionProvider(final ItemCaptionProvider<T> itemCaptionProvider)
	{
		this.itemCaptionProvider = itemCaptionProvider;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemCaptionProvider<T> getItemCaptionProvider()
	{
		return this.itemCaptionProvider;
	}


	@Override
	public String getItemCaption(final Object itemId)
	{
		String caption;
		if((caption = SelectUtils.getItemCaption(this,itemId)) != null)
		{
			return caption;
		}

		return super.getItemCaption(itemId);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setItemIconProvider(final ItemIconProvider<T> itemIconProvider)
	{
		this.itemIconProvider = itemIconProvider;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemIconProvider<T> getItemIconProvider()
	{
		return this.itemIconProvider;
	}


	@Override
	public Resource getItemIcon(final Object itemId)
	{
		Resource icon;
		if((icon = SelectUtils.getItemIcon(this,itemId)) != null)
		{
			return icon;
		}

		return super.getItemIcon(itemId);
	}
}
