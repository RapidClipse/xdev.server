/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.data.Property;
import com.vaadin.ui.PasswordField;


/**
 * A field that is used to enter secret text information like passwords. The
 * entered text is not displayed on the screen.
 *
 * @author XDEV Software
 *
 */
public class XdevPasswordField extends PasswordField
{
	/**
	 * Constructs an empty PasswordField.
	 */
	public XdevPasswordField()
	{
		super();
	}
	
	
	/**
	 * Constructs a PasswordField with given property data source.
	 * 
	 * @param dataSource
	 *            the property data source for the field
	 */
	public XdevPasswordField(final Property<?> dataSource)
	{
		super(dataSource);
	}
	
	
	/**
	 * Constructs a PasswordField with given caption and property data source.
	 * 
	 * @param caption
	 *            the caption for the field
	 * @param dataSource
	 *            the property data source for the field
	 */
	public XdevPasswordField(final String caption, final Property<?> dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * Constructs a PasswordField with given value and caption.
	 * 
	 * @param caption
	 *            the caption for the field
	 * @param value
	 *            the value for the field
	 */
	public XdevPasswordField(final String caption, final String value)
	{
		super(caption,value);
	}
	
	
	/**
	 * Constructs a PasswordField with given caption.
	 * 
	 * @param caption
	 *            the caption for the field
	 */
	public XdevPasswordField(final String caption)
	{
		super(caption);
	}
}
