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

package com.xdev.ui;


import com.vaadin.server.Resource;
import com.xdev.ui.entitycomponent.combobox.XdevComboBox;
import com.xdev.ui.entitycomponent.listselect.XdevListSelect;
import com.xdev.ui.entitycomponent.listselect.XdevTwinColSelect;


/**
 * Simple provider interface to map icons to items.
 * <p>
 * This is used in select components like {@link XdevTree},
 * {@link XdevComboBox}, {@link XdevListSelect} and {@link XdevTwinColSelect}.
 *
 * @author XDEV Software
 * @since 3.0.2
 */
@FunctionalInterface
public interface ItemIconProvider<T>
{
	/**
	 * Provides an icon for a specific item.
	 *
	 * @param item
	 * @return the icon to display, or <code>null</code>
	 */
	public Resource getIcon(T item);
}
