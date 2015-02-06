/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import java.util.Date;

import com.vaadin.data.Property;
import com.vaadin.ui.DateField;


/**
 * <p>
 * A date editor component that can be bound to any {@link Property} that is
 * compatible with <code>java.util.Date</code>.
 * </p>
 * <p>
 * Since <code>DateField</code> extends <code>AbstractField</code> it implements
 * the {@link com.vaadin.data.Buffered}interface.
 * </p>
 * <p>
 * A <code>DateField</code> is in write-through mode by default, so
 * {@link com.vaadin.ui.AbstractField#setWriteThrough(boolean)}must be called to
 * enable buffering.
 *
 * @author XDEV Software
 *
 */
public class XdevDateField extends DateField
{
	/**
	 * Constructs an empty <code>DateField</code> with no caption.
	 */
	public XdevDateField()
	{
		super();
	}
	
	
	/**
	 * Constructs a new <code>DateField</code> that's bound to the specified
	 * <code>Property</code> and has no caption.
	 * 
	 * @param dataSource
	 *            the Property to be edited with this editor.
	 */
	public XdevDateField(final Property<?> dataSource) throws IllegalArgumentException
	{
		super(dataSource);
	}
	
	
	/**
	 * Constructs a new <code>DateField</code> with the given caption and
	 * initial text contents. The editor constructed this way will not be bound
	 * to a Property unless
	 * {@link com.vaadin.data.Property.Viewer#setPropertyDataSource(Property)}
	 * is called to bind it.
	 * 
	 * @param caption
	 *            the caption <code>String</code> for the editor.
	 * @param value
	 *            the Date value.
	 */
	public XdevDateField(final String caption, final Date value)
	{
		super(caption,value);
	}
	
	
	/**
	 * Constructs a new <code>DateField</code> that's bound to the specified
	 * <code>Property</code> and has the given caption <code>String</code>.
	 * 
	 * @param caption
	 *            the caption <code>String</code> for the editor.
	 * @param dataSource
	 *            the Property to be edited with this editor.
	 */
	public XdevDateField(final String caption, final Property<?> dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * Constructs an empty <code>DateField</code> with caption.
	 * 
	 * @param caption
	 *            the caption of the datefield.
	 */
	public XdevDateField(final String caption)
	{
		super(caption);
	}
}
