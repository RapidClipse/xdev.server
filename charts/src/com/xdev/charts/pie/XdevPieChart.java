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

package com.xdev.charts.pie;


import com.vaadin.annotations.JavaScript;
import com.xdev.charts.AbstractXdevChart;
import com.xdev.charts.DataTable;
import com.xdev.charts.XdevChart;
import com.xdev.charts.XdevChartModel;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
@JavaScript({"pie-chart.js","pie-chart-connector.js"})
public class XdevPieChart extends AbstractXdevChart implements XdevChart
{
	public XdevPieChart()
	{
		super();

		this.getState().setConfig(new XdevPieChartConfig());
	}
	
	
	@Override
	protected PieChartComponentState getState()
	{
		return (PieChartComponentState)super.getState();
	}


	public void setConfig(final XdevPieChartConfig config)
	{
		this.getState().setConfig(config);
	}


	@Override
	public void setModel(final XdevChartModel model)
	{
		final XdevPieChartModel pieModel = (XdevPieChartModel)model;

		final DataTable table = new DataTable();
		table.setColumns(pieModel.getDataTable().getColumns());
		table.setRows(pieModel.getDataTable().getRows());
		
		this.getState().setDataTable(table);
		this.getState().setSlices(pieModel.getSlices());
	}
}
