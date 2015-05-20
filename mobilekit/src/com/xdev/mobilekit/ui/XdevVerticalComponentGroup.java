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


import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Field;


/**
 * The VerticalComponentGroup is a layout to group controls vertically. Unlike
 * with default layouts, Components in a VerticalComponentGroup are visually
 * decorated from other parts of the UI.
 * <p>
 * Captions are rendered on the same row as the component. Relative widths are
 * relative to the {@link XdevVerticalComponentGroup} width except if the
 * component has a caption, in which case a relative width is relative to the
 * remaining available space.
 * <p>
 * Most commonly {@link Field}s in {@link XdevVerticalComponentGroup} should be
 * full width, so {@link XdevVerticalComponentGroup} automatically sets width to
 * 100% when {@link Field}s are added to it, unless they have an explicit width
 * defined.
 *
 * @author XDEV Software
 *
 */
public class XdevVerticalComponentGroup extends VerticalComponentGroup
{
	/**
	 *
	 */
	public XdevVerticalComponentGroup()
	{
		super();
	}


	/**
	 * @param caption
	 */
	public XdevVerticalComponentGroup(final String caption)
	{
		super(caption);
	}
}
