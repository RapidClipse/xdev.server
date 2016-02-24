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


import java.util.Collection;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.xdev.ui.XdevField;
import com.xdev.ui.entitycomponent.IDToBeanCollectionConverter;
import com.xdev.ui.entitycomponent.XdevBeanContainer;
import com.xdev.ui.paging.XdevEntityLazyQueryContainer;
import com.xdev.ui.util.KeyValueType;
import com.xdev.util.Caption;
import com.xdev.util.CaptionResolver;
import com.xdev.util.CaptionUtils;


/**
 * Multiselect component with two lists: left side for available items and right
 * side for selected items.
 *
 * @author XDEV Software
 *
 */
public class XdevTwinColSelect<T> extends AbstractBeanTwinColSelect<T> implements XdevField
{
	private final Extensions	extensions					= new Extensions();
	private boolean				persistValue				= PERSIST_VALUE_DEFAULT;
	private boolean				itemCaptionFromAnnotation	= true;
	private String				itemCaptionValue			= null;
															
															
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@SafeVarargs
	@Override
	public final void setContainerDataSource(final Class<T> beanClass,
			final KeyValueType<?, ?>... nestedProperties)
	{
		this.setContainerDataSource(beanClass,true,nestedProperties);
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
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.AbstractSelect#setMultiSelect(boolean)
	 */
	@SuppressWarnings({"rawtypes","unchecked"})
	@Override
	public void setMultiSelect(final boolean multiSelect)
	{
		super.setMultiSelect(multiSelect);
		if(this.isAutoQueryData())
		{
			this.setConverter(new IDToBeanCollectionConverter(this.getContainerDataSource()));
		}
	}


	/**
	 * Sets if the item's caption should be derived from its {@link Caption}
	 * annotation.
	 *
	 * @see CaptionResolver
	 * 		
	 * @param itemCaptionFromAnnotation
	 *            the itemCaptionFromAnnotation to set
	 */
	public void setItemCaptionFromAnnotation(final boolean itemCaptionFromAnnotation)
	{
		this.itemCaptionFromAnnotation = itemCaptionFromAnnotation;
	}


	/**
	 * @return if the item's caption should be derived from its {@link Caption}
	 *         annotation
	 */
	public boolean isItemCaptionFromAnnotation()
	{
		return this.itemCaptionFromAnnotation;
	}


	/**
	 * Sets a user defined caption value for the items to display.
	 *
	 * @see Caption
	 * @see #setItemCaptionFromAnnotation(boolean)
	 * @param itemCaptionValue
	 *            the itemCaptionValue to set
	 */
	public void setItemCaptionValue(final String itemCaptionValue)
	{
		this.itemCaptionValue = itemCaptionValue;
	}


	/**
	 * Returns the user defined caption value for the items to display
	 *
	 * @return the itemCaptionValue
	 */
	public String getItemCaptionValue()
	{
		return this.itemCaptionValue;
	}


	@Override
	public String getItemCaption(final Object itemId)
	{
		if(itemId != null && this.getContainerDataSource() != null)
		{
			if(isItemCaptionFromAnnotation())
			{
				final BeanItem<T> item = getItem(itemId);
				if(item != null)
				{
					final T bean = item.getBean();
					if(bean != null && CaptionUtils.hasCaptionAnnotationValue(bean.getClass()))
					{
						return CaptionUtils.resolveCaption(bean,getLocale());
					}
				}
			}
			else if(this.itemCaptionValue != null)
			{
				final BeanItem<T> item = getItem(itemId);
				if(item != null)
				{
					final T bean = item.getBean();
					if(bean != null)
					{
						return CaptionUtils.resolveCaption(bean,this.itemCaptionValue,getLocale());
					}
				}
			}
		}

		return super.getItemCaption(itemId);
	}
}
