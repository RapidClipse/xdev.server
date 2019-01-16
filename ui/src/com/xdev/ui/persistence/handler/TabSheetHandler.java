/*
 * Copyright (C) 2013-2019 by XDEV Software, All Rights Reserved.
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

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.xdev.ui.persistence.GuiPersistenceEntry;


public class TabSheetHandler extends AbstractComponentHandler<TabSheet>
{
	protected static final String SELECTED_TAB_INDEX = "selectedTabIndex";
	
	
	@Override
	public Class<TabSheet> handledType()
	{
		return TabSheet.class;
	}
	
	
	@Override
	protected void addEntryValues(final Map<String, Object> entryValues, final TabSheet component)
	{
		super.addEntryValues(entryValues,component);
		
		entryValues.put(SELECTED_TAB_INDEX,getSelectedIndex(component));
	}


	private int getSelectedIndex(final TabSheet tabSheet)
	{
		final Component selectedTab = tabSheet.getSelectedTab();
		if(selectedTab == null)
		{
			return -1;
		}
		return tabSheet.getTabPosition(tabSheet.getTab(selectedTab));
	}
	
	
	@Override
	public void restore(final TabSheet component, final GuiPersistenceEntry entry)
	{
		super.restore(component,entry);
		
		component.setSelectedTab(number(entry.value(SELECTED_TAB_INDEX)).intValue());
	}
}
