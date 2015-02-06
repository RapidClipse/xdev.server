/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.ui.NativeSelect;


/**
 * This is a simple drop-down select without, for instance, support for multi
 * select, new items, lazy loading, and other advanced features. Sometimes
 * "native" select without all the bells-and-whistles of the ComboBox is a
 * better choice.
 *
 * @author XDEV Software
 *
 */
public class XdevNativeComboBox extends NativeSelect
{
	/**
	 *
	 */
	public XdevNativeComboBox()
	{
		super();
	}
	
	
	/**
	 * @param caption
	 * @param options
	 */
	public XdevNativeComboBox(final String caption, final Collection<?> options)
	{
		super(caption,options);
	}
	
	
	/**
	 * @param caption
	 * @param dataSource
	 */
	public XdevNativeComboBox(final String caption, final Container dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * @param caption
	 */
	public XdevNativeComboBox(final String caption)
	{
		super(caption);
	}
	
	// init defaults
	{
		setImmediate(true);
	}
}
