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


import com.vaadin.shared.ui.JavaScriptComponentState;
import com.xdev.charts.DataTable;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class AreaChartComponentState extends JavaScriptComponentState
{

	private XdevAreaChartConfig	config;
	private DataTable			dataTable;
	private Integer				numberId;
	private String				captionValue;
	private Object				dataValue;
	
	
	public XdevAreaChartConfig getConfig()
	{
		return this.config;
	}
	
	
	public void setConfig(final XdevAreaChartConfig config)
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
	
	
	public Integer getNumberId()
	{
		return this.numberId;
	}
	
	
	public void setNumberId(final Integer numberId)
	{
		this.numberId = numberId;
	}
	
	
	public String getCaptionValue()
	{
		return this.captionValue;
	}
	
	
	public void setCaptionValue(final String captionValue)
	{
		this.captionValue = captionValue;
	}
	
	
	public Object getDataValue()
	{
		return this.dataValue;
	}
	
	
	public void setDataValue(final Object dataValue)
	{
		this.dataValue = dataValue;
	}
}
