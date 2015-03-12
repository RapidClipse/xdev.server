/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */

package com.xdev.ui;


import org.hibernate.StaleObjectStateException;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.xdev.server.dal.DAOs;
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
	
	
	public void save() throws com.xdev.ui.fieldgroup.CommitException, ObjectLockedException
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
			DAOs.get(bean).merge(bean);
		}
		catch(final StaleObjectStateException e)
		{
			throw new ObjectLockedException(e,this,bean);
		}
	}
}
