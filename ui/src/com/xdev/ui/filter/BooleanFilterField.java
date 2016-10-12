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

package com.xdev.ui.filter;


import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.themes.ValoTheme;


/**
 * @author XDEV Software
 * 		
 */
public class BooleanFilterField extends CheckBox implements FilterField<Boolean>
{
	protected final List<FilterFieldChangeListener>	listeners	= new ArrayList<>();
	protected Object								filterValue;
													
													
	public BooleanFilterField()
	{
		setImmediate(true);
		addValueChangeListener(event -> fireFilterFieldChanged(getConvertedValue()));
		addStyleName(ValoTheme.CHECKBOX_SMALL);
		addStyleName(XdevContainerFilterComponent.FILTER_EDITOR_CLASS);
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
		if(filterValue instanceof Boolean)
		{
			this.filterValue = filterValue;
			setValue((Boolean)filterValue);
		}
	}
}
