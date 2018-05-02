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

package com.xdev.reports.tableexport;


import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.ui.Table.Align;


@SuppressWarnings("deprecation")
public class Column
{
	private String						columnHeader;
	private Integer						columnWidth;
	private Converter<String, Object>	converter;
	private Align						columnAlignment;
	private Class<?>					valueType;
	private Object						propertyID;
	
	
	public Column(final String columnHeader, final Integer columnWidth,
			final Converter<String, Object> converter, final Align columnAlignment,
			final Class<?> valueType, final Object propertyID)
	{
		this.columnHeader = columnHeader;
		this.columnWidth = columnWidth;
		this.converter = converter;
		this.columnAlignment = columnAlignment;
		this.valueType = valueType;
		this.propertyID = propertyID;
	}
	
	
	public String getColumnHeader()
	{
		return this.columnHeader;
	}
	
	
	public void setColumnHeader(final String columnHeader)
	{
		this.columnHeader = columnHeader;
	}
	
	
	public Integer getColumnWidth()
	{
		return this.columnWidth;
	}
	
	
	public void setColumnWidth(final Integer columnWidth)
	{
		this.columnWidth = columnWidth;
	}
	
	
	public Converter<String, Object> getConverter()
	{
		return this.converter;
	}
	
	
	public void setConverter(final Converter<String, Object> converter)
	{
		this.converter = converter;
	}
	
	
	public Align getColumnAlignment()
	{
		return this.columnAlignment;
	}
	
	
	public void setColumnAlignment(final Align columnAlignment)
	{
		this.columnAlignment = columnAlignment;
	}
	
	
	public Class<?> getValueType()
	{
		return this.valueType;
	}
	
	
	public void setValueType(final Class<?> valueType)
	{
		this.valueType = valueType;
	}
	
	
	public Object getPropertyID()
	{
		return this.propertyID;
	}
	
	
	public void setPropertyID(final Object propertyID)
	{
		this.propertyID = propertyID;
	}
}
