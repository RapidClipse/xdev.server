/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;


/**
 * A component container.
 * <p>
 * The panel displays scrollbars if the content gets too big to display.
 *
 * @author XDEV Software
 *
 */
public class XdevPanel extends Panel
{
	/**
	 * Creates a new empty panel.
	 */
	public XdevPanel()
	{
		super();
	}
	
	
	/**
	 * Creates a new empty panel which contains the given content.
	 * 
	 * @param content
	 *            the content for the panel.
	 */
	public XdevPanel(final Component content)
	{
		super(content);
	}
	
	
	/**
	 * Creates a new empty panel with the given caption and content.
	 * 
	 * @param caption
	 *            the caption of the panel (HTML).
	 * @param content
	 *            the content used in the panel.
	 */
	public XdevPanel(final String caption, final Component content)
	{
		super(caption,content);
	}
	
	
	/**
	 * Creates a new empty panel with caption.
	 * 
	 * @param caption
	 *            the caption used in the panel (HTML).
	 */
	public XdevPanel(final String caption)
	{
		super(caption);
	}
}
