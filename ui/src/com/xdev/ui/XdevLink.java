/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
public class XdevLink extends Link implements XdevComponent
{
	public final static String	TOP			= "_top";
	public final static String	BLANK		= "_blank";
											
	private final Extensions	extensions	= new Extensions();
											
											
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


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E addExtension(final Class<? super E> type, final E extension)
	{
		return this.extensions.add(type,extension);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E getExtension(final Class<E> type)
	{
		return this.extensions.get(type);
	}
}
