/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.data.Property;
import com.vaadin.ui.TextArea;


/**
 * A text field that supports multi line editing.
 *
 * @author XDEV Software
 *
 */
public class XdevTextArea extends TextArea
{
	/**
	 * Constructs an empty TextArea.
	 */
	public XdevTextArea()
	{
		super();
	}
	
	
	/**
	 * Constructs a TextArea with given property data source.
	 * 
	 * @param dataSource
	 *            the data source for the field
	 */
	public XdevTextArea(final Property<?> dataSource)
	{
		super(dataSource);
	}
	
	
	/**
	 * Constructs a TextArea with given caption and property data source.
	 * 
	 * @param caption
	 *            the caption for the field
	 * @param dataSource
	 *            the data source for the field
	 */
	public XdevTextArea(final String caption, final Property<?> dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * Constructs a TextArea with given caption and value.
	 * 
	 * @param caption
	 *            the caption for the field
	 * @param value
	 *            the value for the field
	 */
	public XdevTextArea(final String caption, final String value)
	{
		super(caption,value);
	}
	
	
	/**
	 * Constructs an empty TextArea with given caption.
	 * 
	 * @param caption
	 *            the caption for the field.
	 */
	public XdevTextArea(final String caption)
	{
		super(caption);
	}
}
