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


import com.vaadin.addon.touchkit.ui.SwipeView;
import com.vaadin.ui.Component;


/**
 * The SwipeView is a simple layout which has a scrollable content area. It is
 * meant to be used inside a {@link XdevNavigationManager} where it controls its
 * parent on horizontal swipe gestures aka horizontal scrolls. Swiping navigates
 * forward/backward in the NavigationManager.
 * <p>
 * To make usage as fluent as possible, it is suggested to set both next and
 * previous component in the NavigationManager. This can be done using a
 * {@link XdevNavigationManager.NavigationListener}.
 *
 * @author XDEV Software
 *
 */
public class XdevSwipeView extends SwipeView
{
	/**
	 *
	 */
	public XdevSwipeView()
	{
		super();
	}
	
	
	/**
	 * @param content
	 */
	public XdevSwipeView(final Component content)
	{
		super(content);
	}
	
	
	/**
	 * @param caption
	 */
	public XdevSwipeView(final String caption)
	{
		super(caption);
	}
}
