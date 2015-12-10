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

import javax.persistence.Entity;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.xdev.util.EntityIDResolver;
import com.xdev.util.HibernateEntityIDResolver;
import com.xdev.util.HibernateMetaDataUtils;


/**
 * @author Julian Will
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
		preloadRelevantData(item,this.getContainerPropertyIds().toArray());
		return item;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BeanItem<BEANTYPE> addItem(final Object itemId)
	{
		final BeanItem<BEANTYPE> item = super.addItem(itemId);
		preloadRelevantData(itemId,this.getContainerPropertyIds().toArray());
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
		Object itemId = null;
		
		// TODO if is lazy property
		while(it.hasNext())
		{
			itemId = it.next();
			for(final Object propertyID : propertyIds)
			{
				final BEANTYPE entity = this.getItem(itemId).getBean();
				
				final Object propertyValue = HibernateMetaDataUtils.resolveAttributeValue(entity,
						propertyID.toString());
				if(propertyValue != null)
				{
					// force lazy loading
					if(propertyValue instanceof Collection<?>)
					{
						final Collection<?> collection = (Collection<?>)propertyValue;
						for(final Object object : collection)
						{
							if(object.getClass().getAnnotation(Entity.class) != null)
							{
								// TODO check - really required to force lazy
								// loading?
								this.idResolver.getEntityIDPropertyValue(object);
							}
						}
					}
					else if(propertyValue.getClass().getAnnotation(Entity.class) != null)
					{
						// TODO check - really required to force lazy loading?
						this.idResolver.getEntityIDPropertyValue(propertyValue);
					}
				}
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preloadRelevantData(final Object itemID, final Object... propertyIds)
	{
		for(final Object propertyID : propertyIds)
		{
			final BEANTYPE entity = this.getItem(itemID).getBean();
			
			final Object propertyValue = HibernateMetaDataUtils.resolveAttributeValue(entity,
					propertyID.toString());
			if(propertyValue != null)
			{
				// force lazy loading
				if(propertyValue instanceof Collection<?>)
				{
					final Collection<?> collection = (Collection<?>)propertyValue;
					for(final Object object : collection)
					{
						if(object.getClass().getAnnotation(Entity.class) != null)
						{
							// TODO check - really required to force lazy
							// loading?
							this.idResolver.getEntityIDPropertyValue(object);
						}
					}
				}
				else if(propertyValue.getClass().getAnnotation(Entity.class) != null)
				{
					// TODO check - really required to force lazy loading?
					this.idResolver.getEntityIDPropertyValue(propertyValue);
				}
			}
		}
	}
}
