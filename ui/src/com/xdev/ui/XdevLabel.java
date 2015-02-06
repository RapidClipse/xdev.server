/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.data.Property;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;


/**
 * Label component for showing non-editable short texts.
 *
 * The label content can be set to the modes specified by {@link ContentMode}
 *
 * <p>
 * The contents of the label may contain simple formatting:
 * <ul>
 * <li><b>&lt;b></b> Bold
 * <li><b>&lt;i></b> Italic
 * <li><b>&lt;u></b> Underlined
 * <li><b>&lt;br/></b> Linebreak
 * <li><b>&lt;ul>&lt;li>item 1&lt;/li>&lt;li>item 2&lt;/li>&lt;/ul></b> List of
 * items
 * </ul>
 * The <b>b</b>,<b>i</b>,<b>u</b> and <b>li</b> tags can contain all the tags in
 * the list recursively.
 * </p>
 *
 * @author XDEV Software
 *
 */
public class XdevLabel extends Label
{
	/**
	 * Creates an empty Label.
	 */
	public XdevLabel()
	{
		super();
	}


	/**
	 * Creates a new instance of Label with text-contents read from given
	 * datasource.
	 *
	 * @param contentSource
	 * @param contentMode
	 */
	public XdevLabel(final Property<?> contentSource, final ContentMode contentMode)
	{
		super(contentSource,contentMode);
	}


	/**
	 * Creates a new instance of Label with text-contents read from given
	 * datasource.
	 *
	 * @param contentSource
	 */
	public XdevLabel(final Property<?> contentSource)
	{
		super(contentSource);
	}


	/**
	 * Creates a new instance of Label with text-contents.
	 *
	 * @param content
	 * @param contentMode
	 */
	public XdevLabel(final String content, final ContentMode contentMode)
	{
		super(content,contentMode);
	}


	/**
	 * Creates a new instance of Label with text-contents.
	 *
	 * @param content
	 */
	public XdevLabel(final String content)
	{
		super(content);
	}

	// init defaults
	{
		setSizeUndefined();
	}
}
