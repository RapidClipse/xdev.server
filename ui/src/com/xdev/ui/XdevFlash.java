/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
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
