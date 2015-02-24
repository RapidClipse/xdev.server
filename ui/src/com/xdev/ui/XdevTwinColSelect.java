/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */

package com.xdev.ui;


import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.ui.TwinColSelect;


/**
 * Multiselect component with two lists: left side for available items and right
 * side for selected items.
 *
 * @author XDEV Software
 *
 */
public class XdevTwinColSelect extends TwinColSelect
{
	/**
	 *
	 */
	public XdevTwinColSelect()
	{
		super();
	}


	/**
	 * @param caption
	 * @param options
	 */
	public XdevTwinColSelect(final String caption, final Collection<?> options)
	{
		super(caption,options);
	}


	/**
	 * @param caption
	 * @param dataSource
	 */
	public XdevTwinColSelect(final String caption, final Container dataSource)
	{
		super(caption,dataSource);
	}


	/**
	 * @param caption
	 */
	public XdevTwinColSelect(final String caption)
	{
		super(caption);
	}
}
