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

package com.xdev.ui.persistence.handler;


import java.util.Map;

import com.vaadin.ui.Tree;
import com.xdev.ui.persistence.GuiPersistenceEntry;


public class TreeHandler extends AbstractFieldHandler<Tree>
{
	protected static final String KEY_EXPANDED = "expandedItems";


	@Override
	public Class<Tree> handledType()
	{
		return Tree.class;
	}


	@Override
	protected void addEntryValues(final Map<String, Object> entryValues, final Tree component)
	{
		super.addEntryValues(entryValues,component);

		final Object[] expandedIds = component.getItemIds().stream().filter(component::isExpanded)
				.toArray();
		if(expandedIds != null && expandedIds.length > 0)
		{
			entryValues.put(KEY_EXPANDED,getFieldValueToStore(expandedIds));
		}
	}


	@Override
	public void restore(final Tree component, final GuiPersistenceEntry entry)
	{
		super.restore(component,entry);

		final Object[] expandedIds = (Object[])getFieldValueToRestore(component,
				entry.value(KEY_EXPANDED));
		if(expandedIds != null && expandedIds.length > 0)
		{
			component.getItemIds().stream().filter(component::isExpanded)
					.forEach(component::collapseItem);
			for(final Object id : expandedIds)
			{
				component.expandItem(id);
			}
		}
	}
}
