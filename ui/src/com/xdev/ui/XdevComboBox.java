/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.ui.ComboBox;


/**
 * A filtering drop down single-select. Suitable for newItemsAllowed, but it's
 * turned of by default to avoid mistakes. Items are filtered based on user
 * input, and loaded dynamically ("lazy-loading") from the server. You can turn
 * on newItemsAllowed and change filtering mode (and also turn it off), but you
 * can not turn on multi-select mode.
 *
 * @author XDEV Software
 *
 */
public class XdevComboBox extends ComboBox
{
	/**
	 *
	 */
	public XdevComboBox()
	{
		super();
	}


	/**
	 * @param caption
	 * @param options
	 */
	public XdevComboBox(final String caption, final Collection<?> options)
	{
		super(caption,options);
	}


	/**
	 * @param caption
	 * @param dataSource
	 */
	public XdevComboBox(final String caption, final Container dataSource)
	{
		super(caption,dataSource);
	}


	/**
	 * @param caption
	 */
	public XdevComboBox(final String caption)
	{
		super(caption);
	}

	// init defaults
	{
		setTextInputAllowed(false);
	}
}
