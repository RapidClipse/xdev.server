/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
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
 */

package com.xdev.ui.entitycomponent.combobox;


import java.util.Collection;

import com.vaadin.data.util.BeanItem;
import com.xdev.ui.entitycomponent.XdevBeanContainer;
import com.xdev.ui.util.KeyValueType;
import com.xdev.util.Caption;
import com.xdev.util.CaptionResolver;
import com.xdev.util.CaptionUtils;


public class XdevComboBox<T> extends AbstractBeanComboBox<T>
{
	
	/**
	 *
	 */
	private static final long	serialVersionUID			= -836170197198239894L;
															
	private boolean				itemCaptionFromAnnotation	= true;
	private String				itemCaptionValue			= null;
															
															
	public XdevComboBox()
	{
		super();
	}
	
	
	public XdevComboBox(final int pageLength)
	{
		super();
		super.setPageLength(pageLength);
	}
	
	
	// init defaults
	{
		setImmediate(true);
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
	
	
	@Override
	public void setPageLength(final int pageLength)
	{
		// FIXME property change to create new model!
		super.setPageLength(pageLength);
	}
	
	
	@Override
	public String getItemCaption(final Object itemId)
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
		
		return super.getItemCaption(itemId);
	}
}
