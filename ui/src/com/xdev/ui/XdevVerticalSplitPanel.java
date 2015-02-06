/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
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
 *      +==========================+  <-- splitter
 *      |                          |
 *      |  The second component    |
 *      |                          |
 *      +--------------------------+
 * </pre>
 *
 * @author XDEV Software
 *
 */
public class XdevVerticalSplitPanel extends VerticalSplitPanel
{
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
}
