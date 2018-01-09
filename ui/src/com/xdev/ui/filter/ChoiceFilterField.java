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

package com.xdev.ui.filter;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.vaadin.ui.themes.ValoTheme;
import com.xdev.ui.entitycomponent.XdevBeanItemContainer;
import com.xdev.ui.entitycomponent.combobox.XdevComboBox;
import com.xdev.ui.paging.XdevEntityLazyQueryContainer;


/**
 * @author XDEV Software
 * 		
 */
public class ChoiceFilterField<T> extends XdevComboBox<T> implements FilterField<Object>
{
	protected final List<FilterFieldChangeListener>	listeners	= new ArrayList<>();
	protected Object								filterValue;
													
													
	@SuppressWarnings({"unchecked","rawtypes"})
	public ChoiceFilterField(final FilterContext context, final Class<T> beanType)
	{
		super();
		
		setImmediate(true);
		addStyleName(ValoTheme.COMBOBOX_SMALL);
		addStyleName(XdevContainerFilterComponent.FILTER_EDITOR_CLASS);
		
		final Filterable container = context.getContainer();
		if(container instanceof XdevEntityLazyQueryContainer)
		{
			setContainerDataSource(beanType);
		}
		else if(container instanceof XdevBeanItemContainer)
		{
			final XdevBeanItemContainer<T> beanItemContainer = (XdevBeanItemContainer<T>)container;
			final List values = beanItemContainer.getAllItemIds().stream()
					.map(id -> beanItemContainer.getUnfilteredItem(id)
							.getItemProperty(context.getPropertyId()))
					.filter(Objects::nonNull).map(p -> p.getValue()).filter(Objects::nonNull)
					.distinct().collect(Collectors.toList());
			setContainerDataSource(beanType,values);
		}
		else
		{
			final List values = container.getItemIds().stream()
					.map(id -> container.getItem(id).getItemProperty(context.getPropertyId()))
					.filter(Objects::nonNull).map(p -> p.getValue()).filter(Objects::nonNull)
					.distinct().collect(Collectors.toList());
			setContainerDataSource(beanType,values);
		}
		
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
		if(filterValue != null)
		{
			this.filterValue = filterValue;
			setConvertedValue(filterValue);
		}
	}
}
