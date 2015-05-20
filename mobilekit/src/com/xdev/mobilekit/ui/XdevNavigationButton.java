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


import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.Component;
import com.xdev.ui.XdevButton;


/**
 * The NavigationButton is a Button implementation optimized to be used inside a
 * {@link XdevNavigationManager} or generally in touch devices.
 * <p>
 * Clicking button will automatically navigate to the target view, if defined in
 * this button (via constructor or {@link #setTargetView(Component)} ). On the
 * client side, the {@link XdevNavigationManager} will start animation
 * immediately when clicked, while the client is waiting for a response from the
 * server.
 * <p>
 * If the button does not have a target view, {@link XdevNavigationButton} will
 * still cause a immediate client-side animation, and in this case you MUST make
 * sure to navigate to a view in the {@link XdevNavigationButtonClickListener},
 * otherwise the user will be stuck on an empty view.
 * <p>
 * Note that navigation will only work when the button is used inside a
 * {@link XdevNavigationManager}, otherwise it will work as a regular
 * {@link XdevButton}.
 * 
 * @author XDEV Software
 *
 */
public class XdevNavigationButton extends NavigationButton
{
	/**
	 *
	 */
	public XdevNavigationButton()
	{
		super();
	}


	/**
	 * @param targetView
	 */
	public XdevNavigationButton(final Component targetView)
	{
		super(targetView);
	}


	/**
	 * @param caption
	 * @param targetView
	 */
	public XdevNavigationButton(final String caption, final Component targetView)
	{
		super(caption,targetView);
	}


	/**
	 * @param caption
	 */
	public XdevNavigationButton(final String caption)
	{
		super(caption);
	}
}
