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
