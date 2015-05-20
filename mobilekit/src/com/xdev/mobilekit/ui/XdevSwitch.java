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


import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;


/**
 * A {@link CheckBox} that is rendered as a switch button, which might provide a
 * better user experience on touch devices.
 *
 * @see CheckBox
 *
 * @author XDEV Software
 *
 */
public class XdevSwitch extends Switch
{
	/**
	 *
	 */
	public XdevSwitch()
	{
		super();
	}
	
	
	/**
	 * @param caption
	 * @param initialState
	 */
	public XdevSwitch(final String caption, final boolean initialState)
	{
		super(caption,initialState);
	}
	
	
	/**
	 * @param caption
	 * @param dataSource
	 */
	public XdevSwitch(final String caption, final Property<Boolean> dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * @param caption
	 */
	public XdevSwitch(final String caption)
	{
		super(caption);
	}
}
