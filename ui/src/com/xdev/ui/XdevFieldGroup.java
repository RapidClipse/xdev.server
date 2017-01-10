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

package com.xdev.ui;


import org.hibernate.StaleObjectStateException;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.xdev.dal.DAOs;
import com.xdev.ui.fieldgroup.BeanItemCreator;
import com.xdev.ui.fieldgroup.ObjectLockedException;


/**
 * FieldGroup provides an easy way of binding fields to data and handling
 * commits of these fields.
 * <p>
 * The typical use case is to create a layout outside the FieldGroup and then
 * use FieldGroup to bind the fields to a data source.
 * </p>
 * <p>
 * {@link XdevFieldGroup} is not a UI component so it cannot be added to a
 * layout. Using the buildAndBind methods {@link XdevFieldGroup} can create
 * fields for you using a FieldGroupFieldFactory but you still have to add them
 * to the correct position in your layout.
 * </p>
 *
 * @author XDEV Software
 *
 */
public class XdevFieldGroup<T> extends BeanFieldGroup<T>
{
	private final Class<T>		beanType;
	
	private BeanItemCreator<T>	beanItemCreator;
	
	
	public XdevFieldGroup()
	{
		this(null);
	}
	
	
	/**
	 * @param beanType
	 */
	public XdevFieldGroup(final Class<T> beanType)
	{
		super(beanType);
		
		this.beanType = beanType;
	}
	
	
	/**
	 * @param beanItemCreator
	 *            the beanItemCreator to set
	 */
	public void setBeanItemCreator(final BeanItemCreator<T> beanItemCreator)
	{
		this.beanItemCreator = beanItemCreator;
	}


	/**
	 * @return the beanItemCreator
	 */
	public BeanItemCreator<T> getBeanItemCreator()
	{
		return this.beanItemCreator;
	}
	
	
	public T save() throws com.xdev.ui.fieldgroup.CommitException, ObjectLockedException
	{
		try
		{
			commit();
		}
		catch(final CommitException e)
		{
			throw new com.xdev.ui.fieldgroup.CommitException(e);
		}
		
		final BeanItem<T> beanItem = getItemDataSource();
		final T bean = beanItem.getBean();
		try
		{
			final T persistentBean = DAOs.get(bean).save(bean);
			
			if(persistentBean != bean)
			{
				BeanItem<T> newItem = null;
				if(this.beanItemCreator != null)
				{
					newItem = this.beanItemCreator.createBeanItem(beanItem,persistentBean);
				}
				if(newItem == null)
				{
					newItem = new BeanItem<T>(persistentBean,this.beanType);
				}
				this.setItemDataSource(newItem);
			}
			
			return persistentBean;
		}
		catch(final StaleObjectStateException e)
		{
			throw new ObjectLockedException(e,this,bean);
		}
	}
}
