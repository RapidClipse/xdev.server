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


import com.vaadin.addon.touchkit.ui.NavigationBar;


/**
 * The NavigationBar component sits at the top of a {@link XdevNavigationView}
 * and contains an XHTML caption in the middle as well as component slots (most
 * commonly for buttons) on the left and right. A back-button is automatically
 * shown if a <code>previousView</code> is available.
 * <p>
 * Commonly used in a {@link XdevNavigationView}.
 * </p>
 *
 * @see XdevNavigationView
 *
 * @author XDEV Software
 *
 */
public class XdevNavigationBar extends NavigationBar
{
	/**
	 *
	 */
	public XdevNavigationBar()
	{
		super();
	}
}
