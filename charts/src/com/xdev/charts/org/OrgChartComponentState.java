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

package com.xdev.charts.org;


import com.vaadin.shared.ui.JavaScriptComponentState;
import com.xdev.charts.DataTable;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class OrgChartComponentState extends JavaScriptComponentState
{

	private XdevOrgChartConfig	config;
	private DataTable			dataTable;
	
	
	public XdevOrgChartConfig getConfig()
	{
		return this.config;
	}
	
	
	public void setConfig(final XdevOrgChartConfig config)
	{
		this.config = config;
	}
	
	
	public DataTable getDataTable()
	{
		return this.dataTable;
	}
	
	
	public void setDataTable(final DataTable dataTable)
	{
		this.dataTable = dataTable;
	}
}
