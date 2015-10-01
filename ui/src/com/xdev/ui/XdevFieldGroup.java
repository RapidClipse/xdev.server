/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xdev.ui;


import org.hibernate.StaleObjectStateException;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.xdev.dal.DAOs;
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
	public XdevFieldGroup()
	{
		super(null);
	}


	/**
	 * @param beanType
	 */
	public XdevFieldGroup(final Class<T> beanType)
	{
		super(beanType);
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

		final T bean = getItemDataSource().getBean();
		try
		{
			final T persistentBean = DAOs.get(bean).save(bean);
			this.setItemDataSource(persistentBean);
			return persistentBean;
		}
		catch(final StaleObjectStateException e)
		{
			throw new ObjectLockedException(e,this,bean);
		}
	}
}
