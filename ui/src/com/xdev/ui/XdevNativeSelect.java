/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */

package com.xdev.ui;


import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.ui.NativeSelect;


/**
 * This is a simple drop-down select without, for instance, support for
 * multiselect, new items, lazyloading, and other advanced features. Sometimes
 * "native" select without all the bells-and-whistles of the ComboBox is a
 * better choice.
 *
 * @author XDEV Software
 *
 */
public class XdevNativeSelect extends NativeSelect
{
	/**
	 *
	 */
	public XdevNativeSelect()
	{
		super();
	}
	
	
	/**
	 * @param caption
	 * @param options
	 */
	public XdevNativeSelect(final String caption, final Collection<?> options)
	{
		super(caption,options);
	}
	
	
	/**
	 * @param caption
	 * @param dataSource
	 */
	public XdevNativeSelect(final String caption, final Container dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * @param caption
	 */
	public XdevNativeSelect(final String caption)
	{
		super(caption);
	}
}
