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

package com.xdev.mobilekit.ui;


import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;


/**
 * HorizontalButtonGroup is a layout that groups buttons horizontally. Note that
 * only buttons are supported, but this is not enforced in any way. Using other
 * components results in undefined behavior.
 * <p>
 * Any relative sizes are relative to the size of the
 * {@link XdevHorizontalButtonGroup}.
 * <p>
 *
 * @author XDEV Software
 *
 */
public class XdevHorizontalButtonGroup extends HorizontalButtonGroup
{
	/**
	 *
	 */
	public XdevHorizontalButtonGroup()
	{
		super();
	}
	
	
	/**
	 * @param caption
	 */
	public XdevHorizontalButtonGroup(final String caption)
	{
		super(caption);
	}
}
