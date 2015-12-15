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

package com.xdev.ui.entitycomponent;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.xdev.util.EntityIDResolver;
import com.xdev.util.HibernateEntityIDResolver;
import com.xdev.util.HibernateMetaDataUtils;


/**
 * @author XDEV Software (JW,FH)
 * 		
 */
public class XdevBeanItemContainer<BEANTYPE> extends BeanItemContainer<BEANTYPE>
		implements XdevBeanContainer<BEANTYPE>, DTOBeanContainer
{

	private final EntityIDResolver idResolver;


	/**
	 * @param type
	 * @throws IllegalArgumentException
	 */
	public XdevBeanItemContainer(final Class<? super BEANTYPE> type) throws IllegalArgumentException
	{
		super(type);
		this.idResolver = new HibernateEntityIDResolver();
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
		this.preloadRelevantData();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public BeanItem<BEANTYPE> addBean(final BEANTYPE bean)
	{
		final BeanItem<BEANTYPE> item = super.addBean(bean);
		// addBean id is bean itself
		preloadRelevantItemData(item,this.getContainerPropertyIds().toArray());
		return item;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public BeanItem<BEANTYPE> addItem(final Object itemId)
	{
		final BeanItem<BEANTYPE> item = super.addItem(itemId);
		preloadRelevantItemData(itemId,this.getContainerPropertyIds().toArray());
		return item;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.xdev.ui.entitycomponent.DTOBeanContainer#preloadRelevantData()
	 */
	@Override
	public void preloadRelevantData()
	{
		this.preloadRelevantData(this.getContainerPropertyIds().toArray());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preloadRelevantData(final Object... propertyIds)
	{
		final Iterator<?> it = this.getItemIds().iterator();
		while(it.hasNext())
		{
			final Object itemId = it.next();
			preloadRelevantItemData(itemId,propertyIds);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preloadRelevantItemData(final Object itemID, final Object... propertyIds)
	{
		final BEANTYPE bean = this.getItem(itemID).getBean();
		preloadRelevantBeanData(bean,propertyIds);
	}


	protected void preloadRelevantBeanData(final BEANTYPE bean, final Object... propertyIds)
	{
		// TODO if is lazy property
		for(final Object propertyID : propertyIds)
		{
			final Object propertyValue = HibernateMetaDataUtils.resolveAttributeValue(bean,
					propertyID.toString());
			if(propertyValue != null)
			{
				// force lazy loading
				if(propertyValue instanceof Collection<?>)
				{
					final Collection<?> collection = (Collection<?>)propertyValue;
					for(final Object object : collection)
					{
						if(HibernateMetaDataUtils.isManaged(object.getClass()))
						{
							// TODO check - really required to force lazy
							// loading?
							this.idResolver.getEntityIDPropertyValue(object);
						}
					}
				}
				else if(HibernateMetaDataUtils.isManaged(propertyValue.getClass()))
				{
					// TODO check - really required to force lazy loading?
					this.idResolver.getEntityIDPropertyValue(propertyValue);
				}
			}
		}
	}
}
