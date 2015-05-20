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


import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.Component;


/**
 * The NavigationManager is a non-visible component container that allows for
 * smooth navigation between components, or views. It support all components,
 * but back buttons are updated automatically only for
 * {@link XdevNavigationView}s.
 * <p>
 * When a component is navigated to, it replaces the currently visible
 * component, which in turn is pushed on to the stack of previous views. One can
 * navigate backwards by calling {@link #navigateBack()}, in which case the
 * currently visible view is forgotten (still cached in case the user decides to
 * navigate to it again, see {@link #getNextComponent()}) and the previous view
 * is restored from the stack and made visible.
 * <p>
 * When used with {@link XdevNavigationView}s, {@link XdevNavigationBar}s and
 * {@link XdevNavigationButton}s, navigation is smooth and quite automatic.
 * <p>
 * Bootstrap the navigation by giving the {@link XdevNavigationManager} an
 * initial view, either by using the constructor
 * {@link #XdevNavigationManager(Component)} or by calling
 * {@link #navigateTo(Component)}.
 *
 * @author XDEV Software
 *
 */
public class XdevNavigationManager extends NavigationManager
{
	/**
	 *
	 */
	public XdevNavigationManager()
	{
		super();
	}


	/**
	 * @param c
	 */
	public XdevNavigationManager(final Component c)
	{
		super(c);
	}
}
