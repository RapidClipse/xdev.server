/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import java.util.Date;

import com.vaadin.data.Property;
import com.vaadin.ui.InlineDateField;


/**
 * A date entry component, which displays the actual date selector inline.
 *
 * @author XDEV Software
 *
 */
public class XdevInlineDateField extends InlineDateField
{
	/**
	 *
	 */
	public XdevInlineDateField()
	{
		super();
	}
	
	
	/**
	 * @param dataSource
	 * @throws IllegalArgumentException
	 */
	public XdevInlineDateField(final Property<?> dataSource) throws IllegalArgumentException
	{
		super(dataSource);
	}
	
	
	/**
	 * @param caption
	 * @param value
	 */
	public XdevInlineDateField(final String caption, final Date value)
	{
		super(caption,value);
	}
	
	
	/**
	 * @param caption
	 * @param dataSource
	 */
	public XdevInlineDateField(final String caption, final Property<?> dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * @param caption
	 */
	public XdevInlineDateField(final String caption)
	{
		super(caption);
	}
}
