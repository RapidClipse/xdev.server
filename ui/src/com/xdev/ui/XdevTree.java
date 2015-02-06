/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
 */
 
package com.xdev.ui;


import com.vaadin.data.Container;
import com.vaadin.ui.Tree;


/**
 * Tree component. A Tree can be used to select an item (or multiple items) from
 * a hierarchical set of items.
 *
 * @author XDEV Software
 *
 */
public class XdevTree extends Tree
{
	/**
	 * Creates a new empty tree.
	 */
	public XdevTree()
	{
		super();
	}
	
	
	/**
	 * Creates a new tree with caption and connect it to a Container.
	 * 
	 * @param caption
	 * @param dataSource
	 */
	public XdevTree(final String caption, final Container dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * Creates a new empty tree with caption.
	 * 
	 * @param caption
	 */
	public XdevTree(final String caption)
	{
		super(caption);
	}
}
