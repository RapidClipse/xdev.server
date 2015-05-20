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


import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;


/**
 * The Popover is a modal sub window suitable for mobile devices. It is often
 * used to quickly display more options or small form related to an action.
 * Popover does not support dragging or resizing by the end user.
 * <p>
 * An example of a typical use case would be the following: In a mobile mail
 * application when you hit an icon, you'll see a {@link XdevPopover} with
 * actions: "Reply", "Forward" and "Print". E.g. on the iPad the actions would
 * be shown next to the touched icon so they are close to the users finger (the
 * related button can be set with {@link #showRelativeTo(Component)}), while on
 * smaller screens (e.g. the iPhone) this kind of UI element fills the whole
 * width of the screen.
 * <p>
 * A Popover can also be made full screen with {@link #setSizeFull()}. In this
 * case, all borders will be hidden and the window will block all other content
 * of the main window. Using a full screen subwindow, instead of changing the
 * whole content of the main window may cause a slightly faster return to the
 * original view.
 * <p>
 * Finally, on a pad-sized device, if the window is not full screen nor related
 * to a component, it will be completely undecorated by default, allowing the
 * content to dictate the look.
 *
 * @author XDEV Software
 *
 */
public class XdevPopover extends Popover
{
	/**
	 *
	 */
	public XdevPopover()
	{
		super();
	}
	
	
	/**
	 * @param content
	 */
	public XdevPopover(final ComponentContainer content)
	{
		super(content);
	}
}
