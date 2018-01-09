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

package com.xdev.ui.util.wizard;


import com.vaadin.data.Container;
import com.vaadin.ui.AbstractSelect;
import com.xdev.lang.ExecutableCommandObject;


/**
 *
 * @deprecated will be removed in a future release
 */
@Deprecated
public interface ComponentFilterBuilder extends ExecutableCommandObject
{
	public void setMasterComponent(AbstractSelect masterComponent);


	public void setFilterableDetail(Container.Filterable filterable);


	public void setMasterProperty(Object masterProperty);


	public void setDetailProperty(Object detailProperty);
}
