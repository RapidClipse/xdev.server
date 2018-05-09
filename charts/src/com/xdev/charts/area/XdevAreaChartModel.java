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

package com.xdev.charts.area;


import java.time.LocalDate;
import java.util.LinkedHashMap;

import com.xdev.charts.Column;
import com.xdev.charts.ColumnType;
import com.xdev.charts.DataTable;
import com.xdev.charts.XdevChartModel;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class XdevAreaChartModel implements XdevChartModel
{

	private DataTable													dataTable	= null;
	private final LinkedHashMap<Object, LinkedHashMap<String, Object>>	data		= new LinkedHashMap<>();
	private final LinkedHashMap<String, Object>							categories	= new LinkedHashMap<>();
	
	
	@Override
	public DataTable getDataTable()
	{
		if(this.dataTable == null)
		{
			this.dataTable = new DataTable();
		}
		return this.dataTable;
	}
	
	
	@Override
	public LinkedHashMap<Object, LinkedHashMap<String, Object>> getData()
	{
		return this.data;
	}
	
	
	public void addXCategory(final ColumnType type)
	{
		this.getDataTable().getColumns().add(Column.create("xcategory","xcategory",type));
	}
	
	
	public void addCategory(final String caption, final ColumnType type)
	{
		this.categories.put(caption,null);
		this.getDataTable().getColumns().add(Column.create(caption.toLowerCase(),caption,type));
	}
	
	
	@SuppressWarnings("unchecked")
	public void addItem(final String category, Object xValue, final Integer yValue)
	{
		if(xValue instanceof LocalDate)
		{
			final LocalDate date = (LocalDate)xValue;

			xValue = "Date(" + date.getYear() + ", " + date.getMonthValue() + ", "
					+ date.getDayOfMonth() + ")";
		}

		if(!this.data.containsKey(xValue))
		{
			final LinkedHashMap<String, Object> rowData = (LinkedHashMap<String, Object>)this.categories
					.clone();
			rowData.put(category,yValue);
			this.data.put(xValue,rowData);
		}
		else
		{
			final LinkedHashMap<String, Object> rowData = this.data.get(xValue);
			rowData.put(category,yValue);
			this.data.put(xValue,rowData);
		}
	}
}
