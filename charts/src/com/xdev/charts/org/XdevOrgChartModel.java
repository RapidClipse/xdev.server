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

package com.xdev.charts.org;


import java.util.LinkedHashMap;
import java.util.List;

import com.xdev.charts.Column;
import com.xdev.charts.ColumnType;
import com.xdev.charts.DataTable;
import com.xdev.charts.Row;
import com.xdev.charts.XdevChartModel;
import com.xdev.charts.config.Series;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class XdevOrgChartModel implements XdevChartModel
{
	
	private DataTable													dataTable	= null;
	private final LinkedHashMap<Object, LinkedHashMap<String, Object>>	data		= new LinkedHashMap<>();


	public XdevOrgChartModel()
	{
		this.getDataTable().getColumns().add(Column.create("name","name",ColumnType.STRING));
		this.getDataTable().getColumns().add(Column.create("root","root",ColumnType.STRING));
		this.getDataTable().getColumns().add(Column.create("tooltip","tooltip",ColumnType.STRING));
	}


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
	public List<Series> getSeries()
	{
		return null;
	}


	@Override
	public LinkedHashMap<Object, LinkedHashMap<String, Object>> getData()
	{
		return this.data;
	}


	public void addItem(final String name, final String root, final String tooltip)
	{
		this.getDataTable().getRows().add(Row.create(name,root,tooltip));
	}

}
