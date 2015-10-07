/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
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
