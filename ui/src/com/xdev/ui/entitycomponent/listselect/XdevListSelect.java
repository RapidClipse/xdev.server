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

package com.xdev.ui.entitycomponent.listselect;


import java.util.Collection;

import com.vaadin.data.util.BeanItem;
import com.xdev.ui.entitycomponent.IDToBeanCollectionConverter;
import com.xdev.ui.entitycomponent.XdevBeanContainer;
import com.xdev.ui.util.KeyValueType;
import com.xdev.util.Caption;
import com.xdev.util.CaptionResolver;
import com.xdev.util.CaptionUtils;


/**
 * This is a simple list select without, for instance, support for new items,
 * lazyloading, and other advanced features.
 *
 * @author XDEV Software
 *		
 */
public class XdevListSelect<T> extends AbstractBeanListSelect<T>
{
	private boolean itemCaptionFromAnnotation = true;


	/**
	 *
	 */
	public XdevListSelect()
	{
		super();
	}


	/**
	 * @param caption
	 */
	public XdevListSelect(final String caption)
	{
		super(caption);
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


	/**
	 * {@inheritDoc}
	 */
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
					final String caption = CaptionUtils.resolveCaption(bean);
					if(caption != null)
					{
						return caption;
					}
				}
			}
		}
		
		return super.getItemCaption(itemId);
	}
}
