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

package com.xdev.ui.entitycomponent;


import java.util.Collection;
import java.util.List;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.xdev.util.DTOUtils;
import com.xdev.util.HibernateEntityIDResolver;


/**
 * @author XDEV Software (JW,FH)
 *
 */
public class XdevBeanItemContainer<BEANTYPE> extends BeanItemContainer<BEANTYPE>
		implements XdevBeanContainer<BEANTYPE>
{
	private Object[] requiredProperties;
	
	
	/**
	 * @param type
	 * @throws IllegalArgumentException
	 */
	public XdevBeanItemContainer(final Class<? super BEANTYPE> type) throws IllegalArgumentException
	{
		super(type);
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.xdev.ui.entitycomponent.XdevBeanContainer#removeAll()
	 */
	@Override
	public void removeAll()
	{
		this.removeAllItems();
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.xdev.ui.entitycomponent.XdevBeanContainer#refresh()
	 */
	@Override
	public void refresh()
	{
		// no need to synchronize
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.xdev.ui.entitycomponent.XdevBeanContainer#setRequiredProperties(java.
	 * lang.Object[])
	 */
	@Override
	public void setRequiredProperties(final Object... propertyIDs)
	{
		this.requiredProperties = propertyIDs;
		
		for(final BEANTYPE bean : getAllItemIds())
		{
			preload(bean);
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.xdev.ui.entitycomponent.XdevBeanContainer#getRequiredProperties()
	 */
	@Override
	public Object[] getRequiredProperties()
	{
		return this.requiredProperties;
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.xdev.ui.entitycomponent.XdevBeanContainer#removeAll(java.util.
	 * Collection)
	 */
	@Override
	public void removeAll(final Collection<? extends BEANTYPE> beans)
	{
		for(final BEANTYPE bean : beans)
		{
			this.removeItem(bean);
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.data.util.AbstractInMemoryContainer#getAllItemIds()
	 */
	@Override
	public List<BEANTYPE> getAllItemIds()
	{
		return super.getAllItemIds();
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.data.util.AbstractBeanContainer#getUnfilteredItem(java.lang.
	 * Object)
	 */
	@Override
	public BeanItem<BEANTYPE> getUnfilteredItem(final Object itemId)
	{
		return super.getUnfilteredItem(itemId);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAll(final Collection<? extends BEANTYPE> collection)
	{
		super.addAll(collection);
		
		if(!collection.isEmpty())
		{
			for(final BEANTYPE bean : collection)
			{
				preload(bean);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BeanItem<BEANTYPE> addBean(final BEANTYPE bean)
	{
		final BeanItem<BEANTYPE> item = super.addBean(bean);
		// addBean id is bean itself
		preload(item.getBean());
		return item;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BeanItem<BEANTYPE> addItem(final Object itemId)
	{
		final BeanItem<BEANTYPE> item = super.addItem(itemId);
		preload(item.getBean());
		return item;
	}
	
	
	protected void preload(final BEANTYPE bean)
	{
		if(this.requiredProperties == null || this.requiredProperties.length == 0)
		{
			return;
		}
		
		final String[] properties = new String[this.requiredProperties.length];
		for(int i = 0; i < properties.length; i++)
		{
			properties[i] = this.requiredProperties[i].toString();
		}
		
		DTOUtils.preload(bean,HibernateEntityIDResolver.getInstance(),properties);
	}
}
