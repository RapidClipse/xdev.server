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

package com.xdev.ui.filter;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.themes.ValoTheme;


/**
 * @author XDEV Software
 * 		
 */
public class DateFilterField extends PopupDateField implements FilterField<Date>
{
	protected final List<FilterFieldChangeListener>	listeners	= new ArrayList<>();
	protected Object								filterValue;
													
													
	public DateFilterField()
	{
		setImmediate(true);
		addStyleName(ValoTheme.DATEFIELD_SMALL);
		addStyleName(XdevContainerFilterComponent.FILTER_EDITOR_CLASS);
		addValueChangeListener(event -> fireFilterFieldChanged(getConvertedValue()));
	}


	@Override
	public void addFilterFieldChangeListener(final FilterFieldChangeListener l)
	{
		this.listeners.add(l);
	}


	@Override
	public void removeFilterFieldChangeListener(final FilterFieldChangeListener l)
	{
		this.listeners.remove(l);
	}
	
	
	protected void fireFilterFieldChanged(final Object filterValue)
	{
		this.filterValue = filterValue;
		
		final FilterFieldChangeEvent event = new FilterFieldChangeEvent(this,filterValue);
		for(final FilterFieldChangeListener l : this.listeners)
		{
			l.filterFieldChanged(event);
		}
	}
	
	
	@Override
	public Object getFilterValue()
	{
		return this.filterValue;
	}


	@Override
	public void setFilterValue(final Object filterValue)
	{
		if(filterValue instanceof Date)
		{
			this.filterValue = filterValue;
			setValue((Date)filterValue);
		}
	}
}
