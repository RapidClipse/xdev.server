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

package com.xdev.charts.map;


import java.util.LinkedHashMap;

import com.xdev.charts.Column;
import com.xdev.charts.ColumnType;
import com.xdev.charts.DataTable;
import com.xdev.charts.Row;
import com.xdev.charts.XdevChartModel;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class XdevMapChartModel implements XdevChartModel
{
	
	private DataTable													dataTable	= null;
	private final LinkedHashMap<Object, LinkedHashMap<String, Object>>	data		= new LinkedHashMap<>();
	
	
	
	/**
	 * Two alternative data formats are supported: <br>
	 * 1. Latitude: The first two columns should be numbers designating the latitude
	 * and longitude, respectively. An optional third column holds a string that
	 * describes the location specified in the first two columns. <br>
	 * 2. Address: The first column should be a string that contains an address.
	 * This address should be as complete as you can make it. An optional second
	 * column holds a string that describes the location in the first column. String
	 * addresses load more slowly, especially when you have more than 10 addresses.
	 * <br>
	 *
	 */
	public enum DataMapFormat
	{
		Latitude, Address
	}
	
	
	public XdevMapChartModel(final DataMapFormat dataMapFormat)
	{
		if(dataMapFormat.equals(DataMapFormat.Latitude))
		{
			this.getDataTable().getColumns()
					.add(Column.create("latitude","latitude",ColumnType.NUMBER));
			this.getDataTable().getColumns()
					.add(Column.create("longitude","longitude",ColumnType.NUMBER));
			this.getDataTable().getColumns()
					.add(Column.create("caption","caption",ColumnType.STRING));
		}
		else
		{
			this.getDataTable().getColumns()
					.add(Column.create("address","Address",ColumnType.STRING));
			this.getDataTable().getColumns()
					.add(Column.create("caption","Caption",ColumnType.STRING));
		}

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
	public LinkedHashMap<Object, LinkedHashMap<String, Object>> getData()
	{
		return this.data;
	}
	
	
	/**
	 * Only works with the DataMapFormat.Latitude option. <br>
	 * <br>
	 * Specify locations by latitude and longitude, which loads faster than named
	 * locations.<br>
	 *
	 * @param latitude
	 * @param longitude
	 * @param caption
	 */
	public void addItem(final Double latitude, final Double longitude, final String caption)
	{
		this.getDataTable().getRows().add(Row.create(latitude,longitude,caption));
	}
	
	
	/**
	 * Only works with the DataMapFormat.Address option. <br>
	 * <br>
	 * The first parameter must be a string that contains an address. This address
	 * should be as complete as you can make it. The second parameter holds a string
	 * that describes the location. String addresses load more slowly, especially
	 * when you have more than 10 addresses. <br>
	 *
	 * @param address
	 * @param caption
	 */
	public void addItem(final String address, final String caption)
	{
		this.getDataTable().getRows().add(Row.create(address,caption));
	}
}
