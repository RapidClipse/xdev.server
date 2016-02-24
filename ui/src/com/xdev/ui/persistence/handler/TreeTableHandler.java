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

import com.vaadin.ui.TreeTable;
import com.xdev.ui.persistence.GuiPersistenceEntry;


public class TreeTableHandler extends AbstractFieldHandler<TreeTable>
{
	protected static final String	KEY_SORT_CONTAINER_PROPERTY_ID	= "sortContainerPropertyId";
	protected static final String	KEY_VISIBLE_COLUMNS				= "visibleColumns";
	protected static final String	KEY_IS_COLLAPSED				= "isCollapsed";
	protected static final String	KEY_IS_ASCENDING				= "isAscending";
	protected static final String	KEY_COLUMN_WIDTH				= "columnWidth";
																	
	// Doesn't work because of an Issue with Vaadin TreeTable
	// https://dev.vaadin.com/ticket/11211
	//
	// protected static final String KEY_EXPANDED = "expanded";
	
	
	@Override
	public Class<TreeTable> handledType()
	{
		return TreeTable.class;
	}


	@Override
	protected void addEntryValues(final Map<String, Object> entryValues, final TreeTable component)
	{
		super.addEntryValues(entryValues,component);

		entryValues.put(KEY_SORT_CONTAINER_PROPERTY_ID,component.getSortContainerPropertyId());

		final boolean[] comVisCol = new boolean[component.getVisibleColumns().length];
		final int[] widthCol = new int[component.getVisibleColumns().length];
		final Object[] allColumns = component.getVisibleColumns();
		int i = 0;

		for(final Object o : allColumns)
		{
			widthCol[i] = component.getColumnWidth(allColumns[i]);
			if(component.isColumnCollapsed(o))
			{
				comVisCol[i] = true;
			}
			else
			{
				comVisCol[i] = false;
			}
			i++;
		}
		
		// Doesn't work because of an Issue with Vaadin TreeTable
		// https://dev.vaadin.com/ticket/11211
		//
		// final Collection<?> co = component.getItemIds();
		//
		// final Object[] oae = new Object[co.size()];
		// int ie = 0;
		//
		// for(final Object oe : co)
		// {
		// final Boolean b = component.isCollapsed(oe);
		//
		// if(b == true)
		// {
		// oae[ie] = true;
		// }
		// else
		// {
		// oae[ie] = false;
		// }
		// ie++;
		// }
		
		// entryValues.put(KEY_EXPANDED,changeObjectArray(oae));
		
		entryValues.put(KEY_VISIBLE_COLUMNS,allColumns);
		entryValues.put(KEY_IS_COLLAPSED,comVisCol);
		entryValues.put(KEY_COLUMN_WIDTH,widthCol);
		entryValues.put(KEY_IS_ASCENDING,component.isSortAscending());
	}


	@Override
	public void restore(final TreeTable component, final GuiPersistenceEntry entry)
	{
		super.restore(component,entry);

		component.setSortContainerPropertyId(entry.value(KEY_SORT_CONTAINER_PROPERTY_ID));

		final boolean[] comVisCol = (boolean[])entry.value(KEY_IS_COLLAPSED);
		final int[] widthCol = (int[])entry.value(KEY_COLUMN_WIDTH);
		final Object[] namesCol = (Object[])entry.value(KEY_VISIBLE_COLUMNS);
		
		if(component.isColumnCollapsingAllowed())
		{
			for(int i = 0; i < comVisCol.length; i++)
			{
				component.setColumnCollapsed(namesCol[i],comVisCol[i]);
				component.setColumnWidth(namesCol[i],widthCol[i]);
			}
		}
		else
		{
			for(int i = 0; i < comVisCol.length; i++)
			{
				component.setColumnWidth(namesCol[i],widthCol[i]);
			}
		}

		component.setSortAscending((boolean)entry.value(KEY_IS_ASCENDING));
		
		// Doesn't work because of an Issue with Vaadin TreeTable
		// https://dev.vaadin.com/ticket/11211
		//
		// final Object[] oae =
		// returnObjectArray(entry.value(KEY_EXPANDED).toString());
		// final Collection co = component.getItemIds();
		// int i = 0;
		//
		// for(final Object o : co)
		// {
		// component.setCollapsed(o,new Boolean(oae[i].toString()));
		// i++;
		// }
	}
}
