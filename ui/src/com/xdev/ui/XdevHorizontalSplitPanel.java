/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
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
