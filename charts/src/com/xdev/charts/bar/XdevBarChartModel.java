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

package com.xdev.charts.bar;


import java.util.LinkedHashMap;

import com.xdev.charts.Column;
import com.xdev.charts.ColumnType;
import com.xdev.charts.DataRoleType;
import com.xdev.charts.DataTable;
import com.xdev.charts.XdevChartModel;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class XdevBarChartModel implements XdevChartModel
{
	
	private DataTable													dataTable	= null;
	private final LinkedHashMap<Object, LinkedHashMap<String, Object>>	data		= new LinkedHashMap<>();
	private final LinkedHashMap<String, Object>							categories	= new LinkedHashMap<>();


	public XdevBarChartModel()
	{
		this.getDataTable().getColumns()
				.add(Column.create("ycaption","ycaption",ColumnType.STRING));
	}


	@Override
	public LinkedHashMap<Object, LinkedHashMap<String, Object>> getData()
	{
		return this.data;
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


	public void addCategory(final String caption, final ColumnType type)
	{
		this.categories.put(caption,null);
		this.getDataTable().getColumns().add(Column.create(caption.toLowerCase(),caption,type));
		this.getDataTable().getColumns()
				.add(Column.DataRoleColumn(ColumnType.STRING,DataRoleType.ANNOTATION));
	}


	@SuppressWarnings("unchecked")
	public void addItem(final String group, final String category, final Object value,
			final String barCaption)
	{
		if(!this.data.containsKey(group))
		{
			final Object[] insertValues = new Object[2];
			insertValues[0] = value;
			insertValues[1] = barCaption;
			
			final LinkedHashMap<String, Object> v = (LinkedHashMap<String, Object>)this.categories
					.clone();
			v.put(category,insertValues);
			this.data.put(group,v);
		}
		else
		{
			final Object[] insertValues = new Object[2];
			insertValues[0] = value;
			insertValues[1] = barCaption;
			
			final LinkedHashMap<String, Object> v = this.data.get(group);
			v.put(category,insertValues);
			this.data.put(group,v);
		}
	}
}
