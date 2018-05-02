/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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


import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.util.BeanItem;
import com.xdev.ui.fieldgroup.BeanItemCreator;
import com.xdev.ui.fieldgroup.JPASaveHandler;
import com.xdev.ui.fieldgroup.ObjectLockedException;
import com.xdev.ui.fieldgroup.SaveHandler;
import com.xdev.util.JPAMetaDataUtils;


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
@SuppressWarnings("deprecation")
public class XdevFieldGroup<T> extends BeanFieldGroup<T>
{
	private final Class<T>		beanType;
	
	private BeanItemCreator<T>	beanItemCreator;
	private SaveHandler<T>		saveHandler;
	
	
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
	 * @return the beanType
	 * @since 3.2
	 */
	public Class<T> getBeanType()
	{
		return this.beanType;
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
	
	
	/**
	 * @param saveHandler
	 *            the saveHandler to set
	 * @since 3.2
	 */
	public void setSaveHandler(final SaveHandler<T> saveHandler)
	{
		this.saveHandler = saveHandler;
	}
	
	
	/**
	 * @return the saveHandler
	 * @since 3.2
	 */
	public SaveHandler<T> getSaveHandler()
	{
		return this.saveHandler;
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
		
		final SaveHandler<T> saveHandler = lookupSaveHandler(bean);
		if(saveHandler != null)
		{
			return saveHandler.save(this);
		}
		
		return bean;
	}
	
	
	protected SaveHandler<T> lookupSaveHandler(final T bean)
	{
		if(this.saveHandler != null)
		{
			return this.saveHandler;
		}
		
		if(JPAMetaDataUtils.isManaged(bean.getClass()))
		{
			return new JPASaveHandler<>();
		}
		
		return null;
	}
}
