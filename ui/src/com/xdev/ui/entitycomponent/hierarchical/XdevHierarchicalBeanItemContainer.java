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

package com.xdev.ui.entitycomponent.hierarchical;


import com.vaadin.v7.data.util.HierarchicalContainer;
import com.xdev.dal.DAOs;
import com.xdev.ui.hierarchical.DynamicHierarchicalContainer;
import com.xdev.ui.hierarchical.TreeDataProvider;
import com.xdev.util.JPAMetaDataUtils;


/**
 * @author XDEV Software
 * 
 * @since 1.1.0
 */
@SuppressWarnings("deprecation")
public class XdevHierarchicalBeanItemContainer extends HierarchicalContainer
		implements DynamicHierarchicalContainer
{
	protected final TreeDataProvider treeDataProvider;
	
	
	public XdevHierarchicalBeanItemContainer(final TreeDataProvider treeDataProvider)
	{
		this.treeDataProvider = treeDataProvider;
		
		treeDataProvider.roots().forEach(this::addItem);
	}
	
	
	@Override
	public void preloadAll()
	{
		// toArray to avoid ConcurrentModificationException
		for(final Object id : this.rootItemIds().toArray())
		{
			expand(id,true,false);
		}
	}
	
	
	@Override
	public boolean expand(final Object parent)
	{
		return expand(parent,false,true);
	}
	
	
	protected boolean expand(final Object parent, final boolean deep, final boolean reattach)
	{
		if(!areChildrenAllowed(parent) || hasChildren(parent))
		{
			return false;
		}
		
		if(reattach && JPAMetaDataUtils.isManaged(parent.getClass()))
		{
			DAOs.get(parent).reattach(parent);
		}
		
		final Iterable<? extends Object> children = this.treeDataProvider.children(parent,this);
		if(children != null)
		{
			children.forEach(child -> {
				addItem(child);
				setParent(child,parent);
				if(this.treeDataProvider.isLeaf(child,this))
				{
					setChildrenAllowed(child,false);
				}
			});
		}
		if(!hasChildren(parent) || this.treeDataProvider.isLeaf(parent,this))
		{
			setChildrenAllowed(parent,false);
		}
		else if(deep)
		{
			for(final Object child : getChildren(parent))
			{
				// expand=false, only needed for root
				expand(child,true,false);
			}
		}
		
		return true;
	}
}
