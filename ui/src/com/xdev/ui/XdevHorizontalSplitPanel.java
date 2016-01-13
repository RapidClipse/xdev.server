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


import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;


/**
 * A horizontal split panel contains two components and lays them horizontally.
 * The first component is on the left side.
 *
 * <pre>
 *
 *      +---------------------++----------------------+
 *      |                     ||                      |
 *      | The first component || The second component |
 *      |                     ||                      |
 *      +---------------------++----------------------+
 * 
 *                            ^
 *                            |
 *                      the splitter
 *
 * </pre>
 *
 * @author XDEV Software
 *
 */
public class XdevHorizontalSplitPanel extends HorizontalSplitPanel
{
	/**
	 * Creates an empty horizontal split panel
	 */
	public XdevHorizontalSplitPanel()
	{
		super();
	}


	/**
	 * Creates a horizontal split panel containing the given components
	 *
	 * @param firstComponent
	 *            The component to be placed to the left of the splitter
	 * @param secondComponent
	 *            The component to be placed to the right of the splitter
	 */
	public XdevHorizontalSplitPanel(final Component firstComponent, final Component secondComponent)
	{
		super(firstComponent,secondComponent);
	}

	// init defaults
	{
		setSplitPosition(getSplitPosition(),Unit.PIXELS);
	}
}
