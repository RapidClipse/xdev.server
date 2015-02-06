/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.data.Property;
import com.vaadin.ui.TextField;


/**
 * <p>
 * A text editor component that can be bound to any bindable Property. The text
 * editor supports both multiline and single line modes, default is one-line
 * mode.
 * </p>
 *
 * <p>
 * Since <code>TextField</code> extends <code>AbstractField</code> it implements
 * the {@link com.vaadin.data.Buffered} interface. A <code>TextField</code> is
 * in write-through mode by default, so
 * {@link com.vaadin.ui.AbstractField#setWriteThrough(boolean)} must be called
 * to enable buffering.
 * </p>
 *
 * @author XDEV Software
 *
 */
public class XdevTextField extends TextField
{
	/**
	 * Constructs an empty <code>TextField</code> with no caption.
	 */
	public XdevTextField()
	{
		super();
	}
	
	
	/**
	 * Constructs a new <code>TextField</code> that's bound to the specified
	 * <code>Property</code> and has no caption.
	 * 
	 * @param dataSource
	 *            the Property to be edited with this editor.
	 */
	public XdevTextField(final Property<?> dataSource)
	{
		super(dataSource);
	}
	
	
	/**
	 * Constructs a new <code>TextField</code> that's bound to the specified
	 * <code>Property</code> and has the given caption <code>String</code>.
	 * 
	 * @param caption
	 *            the caption <code>String</code> for the editor.
	 * @param dataSource
	 *            the Property to be edited with this editor.
	 */
	public XdevTextField(final String caption, final Property<?> dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * Constructs a new <code>TextField</code> with the given caption and
	 * initial text contents. The editor constructed this way will not be bound
	 * to a Property unless
	 * {@link com.vaadin.data.Property.Viewer#setPropertyDataSource(Property)}
	 * is called to bind it.
	 * 
	 * @param caption
	 *            the caption <code>String</code> for the editor.
	 * @param value
	 *            the initial text content of the editor.
	 */
	public XdevTextField(final String caption, final String value)
	{
		super(caption,value);
	}
	
	
	/**
	 * Constructs an empty <code>TextField</code> with given caption.
	 * 
	 * @param caption
	 *            the caption <code>String</code> for the editor.
	 */
	public XdevTextField(final String caption)
	{
		super(caption);
	}
}
