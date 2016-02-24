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

import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;


/**
 * @author XDEV Software
 *
 */
public class TextFilterField extends TextField implements FilterField<String>
{
	protected final List<FilterFieldChangeListener>	listeners	= new ArrayList<>();
	protected Object								filterValue;
	protected Class<?>								converterType;


	public TextFilterField()
	{
		setImmediate(true);
		addTextChangeListener(this::textChanged);
		addStyleName(ValoTheme.TEXTFIELD_SMALL);
		addStyleName(XdevContainerFilterComponent.FILTER_EDITOR_CLASS);
	}


	public TextFilterField(final Class<?> converterType)
	{
		this();

		this.converterType = _converterType(converterType);

		setConverter(this.converterType);
		setNullRepresentation("");
		addValueChangeListener(event -> fireFilterFieldChanged(getConvertedValue()));
	}
	
	
	private Class<?> _converterType(final Class<?> t)
	{
		if(t.isPrimitive())
		{
			if(t == int.class)
			{
				return Integer.class;
			}
			if(t == double.class)
			{
				return Double.class;
			}
			if(t == boolean.class)
			{
				return Boolean.class;
			}
			if(t == long.class)
			{
				return Long.class;
			}
			if(t == float.class)
			{
				return Float.class;
			}
			if(t == short.class)
			{
				return Short.class;
			}
			if(t == char.class)
			{
				return Character.class;
			}
			if(t == byte.class)
			{
				return Byte.class;
			}
		}
		
		return t;
	}


	protected void textChanged(final TextChangeEvent event)
	{
		final String text = event.getText();
		if(this.converterType == null)
		{
			fireFilterFieldChanged(text);
		}
		else
		{
			try
			{
				final Object value = getConverter().convertToModel(text,String.class,getLocale());
				fireFilterFieldChanged(value);
			}
			catch(final ConversionException e)
			{
				fireFilterFieldChanged(null);
			}
		}
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
			if(this.converterType != null)
			{
				setConvertedValue(filterValue);
			}
			else
			{
				final String string = filterValue.toString();
				this.filterValue = string;
				setValue(string);
			}
		}
	}
}
