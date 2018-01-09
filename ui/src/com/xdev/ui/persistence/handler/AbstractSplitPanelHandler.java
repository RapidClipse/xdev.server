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

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractSplitPanel;
import com.xdev.ui.persistence.GuiPersistenceEntry;


public class AbstractSplitPanelHandler extends AbstractComponentHandler<AbstractSplitPanel>
{
	protected static final String	KEY_MIN_SPLIT_POSITION		= "minSplitPosition";
	protected static final String	KEY_MIN_SPLIT_POSITION_UNIT	= "minSplitPositionUnit";
	protected static final String	KEY_MAX_SPLIT_POSITION		= "maxSplitPosition";
	protected static final String	KEY_MAX_SPLIT_POSITION_UNIT	= "maxSplitPositionUnit";
	protected static final String	KEY_SPLIT_POSITION			= "splitPosition";
	protected static final String	KEY_SPLIT_POSITION_UNIT		= "splitPositionUnit";
	protected static final String	KEY_SPLIT_POSITION_REVERSED	= "splitPositionReversed";
	protected static final String	KEY_IS_LOCKED				= "isLocked";


	@Override
	public Class<AbstractSplitPanel> handledType()
	{
		return AbstractSplitPanel.class;
	}


	@Override
	protected void addEntryValues(final Map<String, Object> entryValues,
			final AbstractSplitPanel component)
	{
		super.addEntryValues(entryValues,component);

		entryValues.put(KEY_MIN_SPLIT_POSITION,component.getMinSplitPosition());
		entryValues.put(KEY_MIN_SPLIT_POSITION_UNIT,component.getMinSplitPositionUnit());
		entryValues.put(KEY_MAX_SPLIT_POSITION,component.getMaxSplitPosition());
		entryValues.put(KEY_MAX_SPLIT_POSITION_UNIT,component.getMaxSplitPositionUnit());
		entryValues.put(KEY_SPLIT_POSITION,component.getSplitPosition());
		entryValues.put(KEY_SPLIT_POSITION_UNIT,component.getSplitPositionUnit());
		entryValues.put(KEY_SPLIT_POSITION_REVERSED,component.isSplitPositionReversed());
		entryValues.put(KEY_IS_LOCKED,component.isLocked());
	}


	@Override
	public void restore(final AbstractSplitPanel component, final GuiPersistenceEntry entry)
	{
		super.restore(component,entry);

		component.setMinSplitPosition(number(entry.value(KEY_MIN_SPLIT_POSITION)).floatValue(),
				Unit.getUnitFromSymbol(entry.value(KEY_MIN_SPLIT_POSITION_UNIT).toString()));
		component.setMaxSplitPosition(number(entry.value(KEY_MAX_SPLIT_POSITION)).floatValue(),
				Unit.getUnitFromSymbol(entry.value(KEY_MAX_SPLIT_POSITION_UNIT).toString()));
		component.setSplitPosition(number(entry.value(KEY_SPLIT_POSITION)).floatValue(),
				Unit.getUnitFromSymbol(entry.value(KEY_SPLIT_POSITION_UNIT).toString()),
				(Boolean)entry.value(KEY_SPLIT_POSITION_REVERSED));
		component.setLocked((Boolean)entry.value(KEY_IS_LOCKED));
	}
}
