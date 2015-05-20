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


import java.lang.reflect.Field;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Component;
import com.xdev.ui.XdevPanel;


/**
 * The NavigationView is a component container which integrates well with the
 * {@link XdevNavigationManager}. It consists of a {@link XdevNavigationBar}, a
 * content area, and optionally a {@link XdevToolbar}.
 * <p>
 * The content area is scrollable (i.e. no need to use a {@link XdevPanel} in
 * it). The {@link XdevNavigationView} is most commonly used with a
 * {@link XdevNavigationManager} which provides smooth forward/back animations.
 * <p>
 * In addition to the main content area (set with {@link #setContent(Component)}
 * ), a {@link XdevNavigationView} can contain a secondary component which, by
 * default, is positioned at the bottom of the layout. The secondary content is
 * set with {@link #setToolbar(Component)}, and is usually a {@link XdevToolbar}.
 *
 *
 * @author XDEV Software
 *
 */
public class XdevNavigationView extends NavigationView
{
	/**
	 *
	 */
	public XdevNavigationView()
	{
		super();
	}
	
	
	/**
	 * @param content
	 */
	public XdevNavigationView(final Component content)
	{
		super(content);
	}
	
	
	/**
	 * @param caption
	 * @param content
	 */
	public XdevNavigationView(final String caption, final Component content)
	{
		super(caption,content);
	}
	
	
	/**
	 * @param caption
	 */
	public XdevNavigationView(final String caption)
	{
		super(caption);
	}
	
	{
		// Inject XdevNavigationBar, request to open API is pending ...
		try
		{
			final Field field = NavigationView.class.getDeclaredField("navigationBar");
			field.setAccessible(true);
			field.set(this,new XdevNavigationBar());
			field.setAccessible(false);
		}
		catch(final Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	@Override
	public XdevNavigationBar getNavigationBar()
	{
		return (XdevNavigationBar)super.getNavigationBar();
	}
}
