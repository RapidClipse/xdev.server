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


import com.vaadin.addon.touchkit.ui.Toolbar;
import com.xdev.ui.XdevButton;


/**
 * The Toolbar is a native looking toolbar for showing buttons.
 * <p>
 * Typically {@link XdevButton}s with icons or a
 * {@link XdevHorizontalButtonGroup} containing Buttons are added to the
 * Toolbar. All components will be rendered as equally sized and centered
 * vertically in the toolbar.
 * <p>
 * A Toolbar is typically used as a part of a {@link XdevNavigationView}.
 *
 * @author XDEV Software
 *
 */
public class XdevToolbar extends Toolbar
{
	/**
	 *
	 */
	public XdevToolbar()
	{
		super();
	}
}
