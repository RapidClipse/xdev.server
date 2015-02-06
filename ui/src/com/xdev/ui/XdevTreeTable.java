/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.data.Collapsible;
import com.vaadin.data.Container;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeTable;


/**
 * TreeTable extends the {@link Table} component so that it can also visualize a
 * hierarchy of its Items in a similar manner that {@link Tree} does. The tree
 * hierarchy is always displayed in the first actual column of the TreeTable.
 * <p>
 * The TreeTable supports the usual {@link Table} features like lazy loading, so
 * it should be no problem to display lots of items at once. Only required rows
 * and some cache rows are sent to the client.
 * <p>
 * TreeTable supports standard {@link Hierarchical} container interfaces, but
 * also a more fine tuned version - {@link Collapsible}. A container
 * implementing the {@link Collapsible} interface stores the collapsed/expanded
 * state internally and can this way scale better on the server side than with
 * standard Hierarchical implementations. Developer must however note that
 * {@link Collapsible} containers can not be shared among several users as they
 * share UI state in the container.
 *
 * @author XDEV Software
 *
 */
public class XdevTreeTable extends TreeTable
{
	/**
	 * Creates an empty TreeTable with a default container.
	 */
	public XdevTreeTable()
	{
		super();
	}
	
	
	/**
	 * Creates a TreeTable instance with given captions and data source.
	 * 
	 * @param caption
	 *            the caption for the component
	 * @param dataSource
	 *            the dataSource that is used to list items in the component
	 */
	public XdevTreeTable(final String caption, final Container dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * Creates an empty TreeTable with a default container.
	 * 
	 * @param caption
	 *            the caption for the TreeTable
	 */
	public XdevTreeTable(final String caption)
	{
		super(caption);
	}
}
