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

package com.xdev.charts.map;


import com.vaadin.annotations.JavaScript;
import com.xdev.charts.AbstractXdevChart;
import com.xdev.charts.XdevChart;
import com.xdev.charts.XdevChartModel;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
@JavaScript({"map-chart.js","map-chart-connector.js"})
public class XdevMapChart extends AbstractXdevChart implements XdevChart
{
	
	public XdevMapChart()
	{
		super();

		this.getState().setConfig(new XdevMapChartConfig());
		this.getState().setMapsApiKey("");
	}
	
	
	public XdevMapChart(final String apiKey)
	{
		super();
		
		this.getState().setConfig(new XdevMapChartConfig());
		this.getState().setMapsApiKey(apiKey);
	}


	@Override
	protected MapChartComponentState getState()
	{
		return (MapChartComponentState)super.getState();
	}


	/**
	 * Sets the configuration for the XdevMapChart. <br>
	 *
	 * @param config
	 */
	public void setConfig(final XdevMapChartConfig config)
	{
		this.getState().setConfig(config);
	}


	/**
	 * Sets the API key for the XdevMapChart. <br>
	 * l
	 *
	 * @param apiKey
	 */
	public void setApiKey(final String apiKey)
	{
		this.getState().setMapsApiKey(apiKey);
	}


	@Override
	public void setModel(final XdevChartModel model)
	{
		this.getState().setDataTable(model.getDataTable());
	}

}
