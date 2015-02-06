/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.ui.ListSelect;


/**
 * This is a simple list select without, for instance, support for new items,
 * lazyloading, and other advanced features.
 *
 * @author XDEV Software
 *
 */
public class XdevListBox extends ListSelect
{
	/**
	 *
	 */
	public XdevListBox()
	{
		super();
	}
	
	
	/**
	 * @param caption
	 * @param options
	 */
	public XdevListBox(final String caption, final Collection<?> options)
	{
		super(caption,options);
	}
	
	
	/**
	 * @param caption
	 * @param dataSource
	 */
	public XdevListBox(final String caption, final Container dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * @param caption
	 */
	public XdevListBox(final String caption)
	{
		super(caption);
	}
	
	// init defaults
	{
		setImmediate(true);
	}
}
