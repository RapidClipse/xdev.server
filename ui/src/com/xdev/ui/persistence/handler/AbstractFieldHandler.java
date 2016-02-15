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

package com.xdev.ui.persistence.handler;


import java.util.Map;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.xdev.ui.XdevField;
import com.xdev.ui.persistence.GuiPersistenceEntry;


@SuppressWarnings("rawtypes")
public abstract class AbstractFieldHandler<C extends AbstractField>
		extends AbstractComponentHandler<C>
{
	protected static final String KEY_VALUE = "value";


	protected static boolean persistFieldValue(final Component component)
	{
		if(component instanceof XdevField)
		{
			return ((XdevField)component).isPersistValue();
		}

		return true;
	}


	@Override
	protected void addEntryValues(final Map<String, Object> entryValues, final C component)
	{
		super.addEntryValues(entryValues,component);

		if(persistFieldValue(component))
		{
			entryValues.put(KEY_VALUE,component.getValue());
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public void restore(final C component, final GuiPersistenceEntry entry)
	{
		super.restore(component,entry);

		if(persistFieldValue(component))
		{
			component.setValue(entry.value(KEY_VALUE));
		}
	}
}
