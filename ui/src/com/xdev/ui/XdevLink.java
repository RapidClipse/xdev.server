/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.server.Resource;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Link;


/**
 * Link is used to create external or internal URL links.
 *
 * @author XDEV Software
 *
 */
public class XdevLink extends Link
{
	public final static String	TOP		= "_top";
	public final static String	BLANK	= "_blank";


	/**
	 * Creates a new link.
	 */
	public XdevLink()
	{
		super();
	}


	/**
	 * Creates a new instance of Link that opens a new window.
	 *
	 *
	 * @param caption
	 *            the Link text.
	 * @param targetName
	 *            the name of the target window where the link opens to. Empty
	 *            name of null implies that the target is opened to the window
	 *            containing the link.
	 * @param width
	 *            the Width of the target window.
	 * @param height
	 *            the Height of the target window.
	 * @param border
	 *            the Border style of the target window.
	 *
	 */
	public XdevLink(final String caption, final Resource resource, final String targetName,
			final int width, final int height, final BorderStyle border)
	{
		super(caption,resource,targetName,width,height,border);
	}


	/**
	 * Creates a new instance of Link.
	 *
	 * @param caption
	 * @param resource
	 */
	public XdevLink(final String caption, final Resource resource)
	{
		super(caption,resource);
	}


	/**
	 * {@inheritDoc}
	 *
	 * @beaninfo enum: TOP XdevLink.TOP BLANK XdevLink.BLANK
	 */
	@Override
	public void setTargetName(final String targetName)
	{
		super.setTargetName(targetName);
	}
}
