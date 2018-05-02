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

package com.xdev.ui.persistence.handler;


import java.util.Map;

import com.vaadin.v7.shared.ui.colorpicker.Color;
import com.vaadin.v7.ui.AbstractColorPicker;
import com.xdev.ui.persistence.GuiPersistenceEntry;


@SuppressWarnings("deprecation")
public class AbstractColorPickerHandler extends AbstractComponentHandler<AbstractColorPicker>
{
	protected static final String	KEY_RED		= "red";
	protected static final String	KEY_GREEN	= "green";
	protected static final String	KEY_BLUE	= "blue";


	@Override
	public Class<AbstractColorPicker> handledType()
	{
		return AbstractColorPicker.class;
	}


	@Override
	protected void addEntryValues(final Map<String, Object> entryValues,
			final AbstractColorPicker component)
	{
		super.addEntryValues(entryValues,component);
		
		entryValues.put(KEY_RED,component.getColor().getRed());
		entryValues.put(KEY_GREEN,component.getColor().getGreen());
		entryValues.put(KEY_BLUE,component.getColor().getBlue());
	}


	@Override
	public void restore(final AbstractColorPicker component, final GuiPersistenceEntry entry)
	{
		super.restore(component,entry);

		component.setColor(new Color((int)entry.value(KEY_RED),(int)entry.value(KEY_GREEN),
				(int)entry.value(KEY_BLUE)));
	}
}
