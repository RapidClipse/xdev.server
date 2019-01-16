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

package com.xdev.ui.filter;


import java.util.EventListener;
import java.util.EventObject;

import com.vaadin.ui.Field;


/**
 * @author XDEV Software
 *
 */
public interface FilterField<T> extends Field<T>
{
	public static class FilterFieldChangeEvent extends EventObject
	{
		private final Object filterValue;


		public FilterFieldChangeEvent(final FilterField<?> filterField, final Object filterValue)
		{
			super(filterField);

			this.filterValue = filterValue;
		}


		public Object getFilterValue()
		{
			return this.filterValue;
		}
	}



	public static interface FilterFieldChangeListener extends EventListener
	{
		public void filterFieldChanged(FilterFieldChangeEvent event);
	}


	public void addFilterFieldChangeListener(FilterFieldChangeListener l);


	public void removeFilterFieldChangeListener(FilterFieldChangeListener l);


	public Object getFilterValue();


	public void setFilterValue(Object filterValue);
}
