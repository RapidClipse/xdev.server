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
 * 
 * For further information see 
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.ui.util;


import java.util.Iterator;

import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;


/**
 * @author XDEV Software
 *		
 */
public final class UIUtils
{
	private UIUtils()
	{
	}
	
	
	public static <T> T getNextParent(final Component c, final Class<T> type)
	{
		Component parent = c;
		while(parent != null)
		{
			if(type.isInstance(parent))
			{
				return type.cast(parent);
			}
			
			parent = parent.getParent();
		}
		
		return null;
	}
	
	
	/**
	 * Shortcut for {@link lookupComponentTree(parent,visitor,null)}
	 *
	 * @param <T>
	 *            The return type
	 * @param parent
	 *            The root of the component tree to visit
	 * @param visitor
	 *            the {@link ComponentTreeVisitor}
	 * @return @see {@link ComponentTreeVisitor}
	 */
	
	public static <T> T lookupComponentTree(final Component parent,
			final ComponentTreeVisitor<T, Component> visitor)
	{
		return lookupComponentTree(parent,visitor,null);
	}
	
	
	/**
	 * Walks through the <code>parent</code>'s component tree hierarchy.
	 * <p>
	 * For every child in the tree hierarchy, which fits the <code>type</code>
	 * parameter (or type is <code>null</code>), the <code>visitor</code>'s
	 * {@link ComponentTreeVisitor#visit(Component)} is invoked.
	 * <p>
	 * If the <code>visit</code> method returns a value != <code>null</code>,
	 * the visitation ends and that value is returned.
	 *
	 *
	 * @param <T>
	 *            The return type
	 * @param <C>
	 *            The component type to visit
	 * @param parent
	 *            The root of the component tree to visit
	 * @param visitor
	 *            the {@link ComponentTreeVisitor}
	 * @param type
	 *            The component type class to visit
	 * @return @see {@link ComponentTreeVisitor}
	 */
	
	@SuppressWarnings("unchecked")
	public static <T, C extends Component> T lookupComponentTree(final Component parent,
			final ComponentTreeVisitor<T, C> visitor, final Class<C> type)
	{
		T value = null;
		
		if(type == null || type.isInstance(parent))
		{
			value = visitor.visit((C)parent);
		}
		
		if(value == null && parent instanceof HasComponents)
		{
			final HasComponents hasComponents = (HasComponents)parent;
			final Iterator<Component> iterator = hasComponents.iterator();
			while(iterator.hasNext() && value == null)
			{
				final Component cpn = iterator.next();
				if(cpn instanceof HasComponents)
				{
					value = lookupComponentTree(cpn,visitor,type);
				}
				else if(type == null || type.isInstance(cpn))
				{
					value = visitor.visit((C)cpn);
				}
			}
		}
		
		return value;
	}
}
