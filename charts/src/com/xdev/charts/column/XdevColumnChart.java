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

package com.xdev.charts.column;


import com.vaadin.annotations.JavaScript;
import com.xdev.charts.AbstractXdevChart;
import com.xdev.charts.Row;
import com.xdev.charts.XdevChart;
import com.xdev.charts.XdevChartModel;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
@JavaScript({"column-chart.js","column-chart-connector.js"})
public class XdevColumnChart extends AbstractXdevChart implements XdevChart
{
	
	public XdevColumnChart()
	{
		super();
		
		this.getState().setConfig(new XdevColumnChartConfig());
	}


	@Override
	protected ColumnChartComponentState getState()
	{
		return (ColumnChartComponentState)super.getState();
	}


	public void setConfig(final XdevColumnChartConfig config)
	{
		this.getState().setConfig(config);
	}


	@Override
	public void setModel(final XdevChartModel model)
	{
		Row.createFromHashmap(model.getData()).forEach(row -> {
			model.getDataTable().getRows().add(row);
		});

		this.getState().setDataTable(model.getDataTable());
	}

}
