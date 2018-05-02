/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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
 *
 * For further information see
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.ui;


import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalSplitPanel;


/**
 * A vertical split panel contains two components and lays them vertically. The
 * first component is above the second component.
 *
 * <pre>
 *      +--------------------------+
 *      |                          |
 *      |  The first component     |
 *      |                          |
 *      +==========================+  &lt;-- splitter
 *      |                          |
 *      |  The second component    |
 *      |                          |
 *      +--------------------------+
 * </pre>
 *
 * @author XDEV Software
 *
 */
public class XdevVerticalSplitPanel extends VerticalSplitPanel implements XdevComponent
{
	private final Extensions extensions = new Extensions();


	/**
	 * Creates an empty vertical split panel
	 */
	public XdevVerticalSplitPanel()
	{
		super();
	}
	
	
	/**
	 * Creates a vertical split panel containing the given components
	 *
	 * @param firstComponent
	 *            The component to be placed above the splitter
	 * @param secondComponent
	 *            The component to be placed below of the splitter
	 */
	public XdevVerticalSplitPanel(final Component firstComponent, final Component secondComponent)
	{
		super(firstComponent,secondComponent);
	}
	
	// init defaults
	{
		setSplitPosition(getSplitPosition(),Unit.PIXELS);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E addExtension(final Class<? super E> type, final E extension)
	{
		return this.extensions.add(type,extension);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E getExtension(final Class<E> type)
	{
		return this.extensions.get(type);
	}
}
