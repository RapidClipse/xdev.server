/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.ui.OptionGroup;


/**
 * Configures select to be used as an option group.
 *
 * @author XDEV Software
 *
 */
public class XdevOptionGroup extends OptionGroup
{
	/**
	 *
	 */
	public XdevOptionGroup()
	{
		super();
	}


	/**
	 * @param caption
	 * @param options
	 */
	public XdevOptionGroup(final String caption, final Collection<?> options)
	{
		super(caption,options);
	}


	/**
	 * @param caption
	 * @param dataSource
	 */
	public XdevOptionGroup(final String caption, final Container dataSource)
	{
		super(caption,dataSource);
	}


	/**
	 * @param caption
	 */
	public XdevOptionGroup(final String caption)
	{
		super(caption);
	}

	// init defaults
	{
		setImmediate(true);
	}
}
