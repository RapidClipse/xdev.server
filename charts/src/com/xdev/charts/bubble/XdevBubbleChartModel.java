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

package com.xdev.charts.bubble;


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
public class XdevBubbleChartModel implements XdevChartModel
{
	
	private DataTable													dataTable	= null;
	private final LinkedHashMap<Object, LinkedHashMap<String, Object>>	data		= new LinkedHashMap<>();


	public XdevBubbleChartModel(final String xColumnCaption, final String yColumnCaption,
			final String groupCaption, final String sizeCaption)
	{
		this.getDataTable().getColumns().add(Column.create("caption","Caption",ColumnType.STRING));
		this.getDataTable().getColumns()
				.add(Column.create(xColumnCaption.toLowerCase(),xColumnCaption,ColumnType.NUMBER));
		this.getDataTable().getColumns()
				.add(Column.create(yColumnCaption.toLowerCase(),yColumnCaption,ColumnType.NUMBER));
		this.getDataTable().getColumns()
				.add(Column.create(groupCaption.toLowerCase(),groupCaption,ColumnType.STRING));
		this.getDataTable().getColumns()
				.add(Column.create(sizeCaption.toLowerCase(),sizeCaption,ColumnType.NUMBER));
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


	public void addItem(final String caption, final Double x, final Double y, final String group,
			final Double size)
	{
		this.getDataTable().getRows().add(Row.create(caption,x,y,group,size));
	}
}
