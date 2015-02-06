/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.data.Property;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;


/**
 * A simple RichTextArea to edit HTML format text.
 *
 * Note, that using {@link TextField#setMaxLength(int)} method in
 * {@link RichTextArea} may produce unexpected results as formatting is counted
 * into length of field.
 *
 * @author XDEV Software
 *
 */
public class XdevRichTextArea extends RichTextArea
{
	/**
	 * Constructs an empty <code>RichTextArea</code> with no caption.
	 */
	public XdevRichTextArea()
	{
		super();
	}
	
	
	/**
	 * Constructs a new <code>RichTextArea</code> that's bound to the specified
	 * <code>Property</code> and has no caption.
	 * 
	 * @param dataSource
	 *            the data source for the editor value
	 */
	public XdevRichTextArea(final Property<?> dataSource)
	{
		super(dataSource);
	}
	
	
	/**
	 * Constructs a new <code>RichTextArea</code> that's bound to the specified
	 * <code>Property</code> and has the given caption.
	 * 
	 * @param caption
	 *            the caption for the editor.
	 * @param dataSource
	 *            the data source for the editor value
	 */
	public XdevRichTextArea(final String caption, final Property<?> dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * Constructs a new <code>RichTextArea</code> with the given caption and
	 * initial text contents.
	 * 
	 * @param caption
	 *            the caption for the editor.
	 * @param value
	 *            the initial text content of the editor.
	 */
	public XdevRichTextArea(final String caption, final String value)
	{
		super(caption,value);
	}
	
	
	/**
	 * 
	 * Constructs an empty <code>RichTextArea</code> with the given caption.
	 * 
	 * @param caption
	 *            the caption for the editor.
	 */
	public XdevRichTextArea(final String caption)
	{
		super(caption);
	}
}
