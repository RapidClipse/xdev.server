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


import com.vaadin.ui.NativeButton;


/**
 * System button component.
 *
 * @author XDEV Software
 *
 */
public class XdevNativeButton extends NativeButton
{
	/**
	 *
	 */
	public XdevNativeButton()
	{
		super();
	}
	
	
	/**
	 * @param caption
	 * @param listener
	 */
	public XdevNativeButton(final String caption, final ClickListener listener)
	{
		super(caption,listener);
	}
	
	
	/**
	 * @param caption
	 */
	public XdevNativeButton(final String caption)
	{
		super(caption);
	}
}
