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

package com.xdev.ui.entitycomponent.combobox;


import java.util.Collection;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.xdev.data.util.filter.CaptionStringFilter;
import com.xdev.ui.ItemCaptionProvider;
import com.xdev.ui.ItemIconProvider;
import com.xdev.ui.XdevSelect;
import com.xdev.ui.entitycomponent.XdevBeanContainer;
import com.xdev.ui.util.KeyValueType;


public class XdevComboBox<T> extends AbstractBeanComboBox<T> implements XdevSelect<T>
{
	private final Extensions		extensions					= new Extensions();
	private boolean					persistValue				= PERSIST_VALUE_DEFAULT;
	private boolean					itemCaptionFromAnnotation	= true;
	private String					itemCaptionValue;
	private ItemCaptionProvider<T>	itemCaptionProvider;
	private ItemIconProvider<T>		itemIconProvider;


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
		try
		{
			String caption;
			if((caption = SelectUtils.getItemCaption(this,itemId)) != null)
			{
				return caption;
			}
		}
		catch(final NoClassDefFoundError ncdfe)
		{
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
		try
		{
			Resource icon;
			if((icon = SelectUtils.getItemIcon(this,itemId)) != null)
			{
				return icon;
			}
		}
		catch(final NoClassDefFoundError ncdfe)
		{
		}

		return super.getItemIcon(itemId);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.ComboBox#buildFilter(java.lang.String,
	 * com.vaadin.shared.ui.combobox.FilteringMode)
	 */
	@Override
	protected Filter buildFilter(final String filterString, final FilteringMode filteringMode)
	{
		if(isItemCaptionFromAnnotation())
		{
			return buildCaptionFilter(filterString,filteringMode,null);
		}
		else if(this.itemCaptionValue != null)
		{
			return buildCaptionFilter(filterString,filteringMode,this.itemCaptionValue);
		}

		return super.buildFilter(filterString,filteringMode);
	}


	protected Filter buildCaptionFilter(final String filterString,
			final FilteringMode filteringMode, final String itemCaptionValue)
	{
		Filter filter = null;

		if(null != filterString && !"".equals(filterString))
		{
			switch(filteringMode)
			{
				case OFF:
				break;

				case STARTSWITH:
					filter = new CaptionStringFilter(getItemCaptionPropertyId(),filterString,true,
							true,itemCaptionValue);
				break;

				case CONTAINS:
					filter = new CaptionStringFilter(getItemCaptionPropertyId(),filterString,true,
							false,itemCaptionValue);
				break;
			}
		}

		return filter;
	}
}
