
package com.xdev.ui.persistence.handler;


import java.util.Map;

import com.xdev.ui.filter.FilterData;
import com.xdev.ui.filter.XdevContainerFilterComponent;
import com.xdev.ui.persistence.GuiPersistenceEntry;


public class XdevContainerFilterComponentHandler
		extends AbstractComponentHandler<XdevContainerFilterComponent>
{
	protected static final String	KEY_SEARCH_TEXT	= "searchText";
	protected static final String	KEY_FILTER_DATA	= "filterData";
													
													
	@Override
	public Class<XdevContainerFilterComponent> handledType()
	{
		return XdevContainerFilterComponent.class;
	}
	
	
	@Override
	protected void addEntryValues(final Map<String, Object> entryValues,
			final XdevContainerFilterComponent component)
	{
		super.addEntryValues(entryValues,component);

		entryValues.put(KEY_SEARCH_TEXT,component.getSearchText());
		entryValues.put(KEY_FILTER_DATA,component.getFilterData());
	}
	
	
	@Override
	public void restore(final XdevContainerFilterComponent component,
			final GuiPersistenceEntry entry)
	{
		super.restore(component,entry);

		component.setSearchText((String)entry.value(KEY_SEARCH_TEXT));
		component.setFilterData((FilterData[])entry.value(KEY_FILTER_DATA));
	}
}
