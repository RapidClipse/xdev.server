/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */

package com.xdev.ui;


import com.vaadin.data.Container;
import com.vaadin.ui.Table;


/**
 * <p>
 * <code>Table</code> is used for representing data or components in a pageable
 * and selectable table.
 * </p>
 *
 * <p>
 * Scalability of the Table is largely dictated by the container. A table does
 * not have a limit for the number of items and is just as fast with hundreds of
 * thousands of items as with just a few. The current GWT implementation with
 * scrolling however limits the number of rows to around 500000, depending on
 * the browser and the pixel height of rows.
 * </p>
 *
 * <p>
 * Components in a Table will not have their caption nor icon rendered.
 * </p>
 *
 * @author XDEV Software
 *
 */
public class XdevTable extends Table
{
	/**
	 * Creates a new empty table.
	 */
	public XdevTable()
	{
		super();
	}


	/**
	 * Creates a new table with caption and connect it to a Container.
	 *
	 * @param caption
	 * @param dataSource
	 */
	public XdevTable(final String caption, final Container dataSource)
	{
		super(caption,dataSource);
	}


	/**
	 * Creates a new empty table with caption.
	 *
	 * @param caption
	 */
	public XdevTable(final String caption)
	{
		super(caption);
	}

	// init defaults
	{
		setSelectable(true);
		setImmediate(true);
	}
}
