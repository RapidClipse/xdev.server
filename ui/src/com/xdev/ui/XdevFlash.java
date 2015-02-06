/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.server.Resource;
import com.vaadin.ui.Flash;


/**
 * A component for displaying Adobe� Flash� content.
 *
 * @author XDEV Software
 *
 */
public class XdevFlash extends Flash
{
	/**
	 * Creates a new empty Flash component.
	 */
	public XdevFlash()
	{
		super();
	}
	
	
	/**
	 * Creates a new Flash component with the given caption and content.
	 * 
	 * @param caption
	 *            The caption for the component
	 * @param source
	 *            A Resource representing the Flash content that should be
	 *            displayed
	 */
	public XdevFlash(final String caption, final Resource source)
	{
		super(caption,source);
	}
	
	
	/**
	 * Creates a new empty Flash component with the given caption
	 * 
	 * @param caption
	 *            The caption for the component
	 */
	public XdevFlash(final String caption)
	{
		super(caption);
	}
	
	
	/**
	 * Sets the fullscreen allowed flag, short for
	 * 
	 * <pre>
	 * setParameter(&quot;allowFullScreen&quot;,String.valueOf(b));
	 * </pre>
	 * 
	 * @param b
	 */
	public void setFullscreenAllowed(final boolean b)
	{
		setParameter("allowFullScreen",String.valueOf(b));
	}
	
	
	/**
	 * Returns the value of the fullscreen allowed flag, short for
	 * 
	 * <pre>
	 * &quot;true&quot;.equals(getParameter(&quot;allowFullScreen&quot;))
	 * </pre>
	 *
	 * @return if fullscreen is allowed
	 */
	public boolean isFullscreenAllowed()
	{
		return "true".equals(getParameter("allowFullScreen"));
	}
}
