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

package com.xdev.ui.util.wizard;


import java.util.Collection;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.AbstractSelect;
import com.xdev.lang.ExecutableCommandObject;
import com.xdev.ui.entitycomponent.hierarchical.FillTree;
import com.xdev.ui.entitycomponent.hierarchical.FillTree.Strategy;
import com.xdev.ui.entitycomponent.hierarchical.Group;
import com.xdev.util.EntityReferenceResolver;
import com.xdev.util.HibernateEntityReferenceResolver;


/**
 * @deprecated @see {@link FillTree} for more information
 */
@Deprecated
public class XdevFillTree implements ExecutableCommandObject
{
	private final FillTree					fillTreeComposite;
	private final EntityReferenceResolver	referenceResolver;
											
											
	/**
	 *
	 */
	public XdevFillTree(final AbstractSelect tree)
	{
		super();
		this.fillTreeComposite = new FillTree.Implementation();
		this.fillTreeComposite.setHierarchicalReceiver(tree);
		this.referenceResolver = HibernateEntityReferenceResolver.getInstance();
	}
	
	/*
	 * --------------- UTILITY DELEGATORS -------------------
	 */
	
	
	public <T> Group addRootGroup(final Class<T> clazz) throws RuntimeException
	{
		return this.fillTreeComposite.addRootGroup(clazz);
	}
	
	
	public Group addGroup(final Class<?> clazz, final Class<?> parentClazz) throws RuntimeException
	{
		// TODO check bidirectionals
		final String propertyName = this.referenceResolver
				.getReferenceEntityPropertyName(parentClazz,clazz);
		try
		{
			return this.fillTreeComposite.addGroup(clazz,clazz.getDeclaredField(propertyName));
		}
		catch(NoSuchFieldException | SecurityException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	
	public <T> void setGroupData(final Class<T> groupClass, final Collection<T> data)
	{
		this.fillTreeComposite.setGroupData(groupClass,data);
	}
	
	
	@Override
	public void execute()
	{
		this.fillTreeComposite.fillTree(new HierarchicalContainer());
	}
	
	
	public void setHierarchicalReceiver(final AbstractSelect treeComponent)
	{
		this.fillTreeComposite.setHierarchicalReceiver(treeComponent);
	}
	
	
	public AbstractSelect getHierarchicalReceiver()
	{
		return this.fillTreeComposite.getHierarchicalReceiver();
	}
	
	
	public void setStrategy(final Strategy strategy)
	{
		this.fillTreeComposite.setStrategy(strategy);
	}
}
