
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
