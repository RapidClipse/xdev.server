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
import com.vaadin.ui.Button;


/**
 * A generic button component.
 *
 * @author XDEV Software
 */

public class XdevButton extends Button
{
	/**
	 * Creates a button with no set text or icon.
	 */
	public XdevButton()
	{
		super();
	}


	/**
	 * Creates a button with text.
	 *
	 * @param caption
	 *            the text of the button
	 */
	public XdevButton(final String caption)
	{
		super(caption);
	}
	
	
	/**
	 * Creates a new push button with the given icon.
	 * 
	 * @param icon
	 *            the icon
	 */
	public XdevButton(final Resource icon)
	{
		super(icon);
	}
	
	
	/**
	 * Creates a new push button with a click listener.
	 * 
	 * @param caption
	 *            the Button caption.
	 * @param listener
	 *            the Button click listener.
	 */
	public XdevButton(final String caption, final ClickListener listener)
	{
		super(caption,listener);
	}
	
	
	/**
	 * Creates a new push button with the given caption and icon.
	 * 
	 * @param caption
	 *            the caption
	 * @param icon
	 *            the icon
	 */
	public XdevButton(final String caption, final Resource icon)
	{
		super(caption,icon);
	}
}
