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


import java.io.Serializable;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class XdevMapChartConfig implements Serializable
{
	private String	mapType				= "hybrid";
	private boolean	enableScrollWheel	= false;
	private Integer	zoomLevel;
	private boolean	useMapTypeControl	= false;
	private boolean	showLine			= false;
	private String	lineColor			= "#800000";
	private Integer	lineWidth			= 10;


	public String getMapType()
	{
		return this.mapType;
	}


	/**
	 * The type of map to show. Possible values are 'normal', 'terrain',
	 * 'satellite', 'hybrid'. <br>
	 * <br>
	 *
	 * @see MapType
	 *
	 * @param mapType
	 *
	 */
	public void setMapType(final String mapType)
	{
		this.mapType = mapType;
	}


	/**
	 * @return the enableScrollWheel
	 */
	public boolean isEnableScrollWheel()
	{
		return this.enableScrollWheel;
	}


	/**
	 * If set to true, enables zooming in and out using the mouse scroll wheel. <br>
	 * Default: false<br>
	 *
	 * @param enableScrollWheel
	 *            the enableScrollWheel to set
	 */
	public void setEnableScrollWheel(final boolean enableScrollWheel)
	{
		this.enableScrollWheel = enableScrollWheel;
	}
	
	
	/**
	 * @return the zoomLevel
	 */
	public Integer getZoomLevel()
	{
		return this.zoomLevel;
	}
	
	
	/**
	 * An integer indicating the initial zoom level of the map, where 0 is
	 * completely zoomed out (whole world) and 19 is the maximum zoom level. <br>
	 *
	 * @param zoomLevel
	 *            the zoomLevel to set
	 */
	public void setZoomLevel(final Integer zoomLevel)
	{
		this.zoomLevel = zoomLevel;
	}


	/**
	 * @return the useMapTypeControl
	 */
	public boolean isUseMapTypeControl()
	{
		return this.useMapTypeControl;
	}


	/**
	 * Show a map type selector that enables the viewer to switch between [map,
	 * satellite, hybrid, terrain]. When useMapTypeControl is false (default) no
	 * selector is presented and the type is determined by the mapType option. <br>
	 *
	 * @param useMapTypeControl
	 *            the useMapTypeControl to set
	 */
	public void setUseMapTypeControl(final boolean useMapTypeControl)
	{
		this.useMapTypeControl = useMapTypeControl;
	}
	
	
	/**
	 * @return the showLine
	 */
	public boolean isShowLine()
	{
		return this.showLine;
	}
	
	
	/**
	 * If set to true, shows a Google Maps polyline through all the points. <br>
	 *
	 * @param showLine
	 *            the showLine to set
	 */
	public void setShowLine(final boolean showLine)
	{
		this.showLine = showLine;
	}


	/**
	 * @return the lineColor
	 */
	public String getLineColor()
	{
		return this.lineColor;
	}


	/**
	 * If showLine is true, defines the line color. For example: '#800000'. <br>
	 *
	 * @param lineColor
	 *            the lineColor to set
	 */
	public void setLineColor(final String lineColor)
	{
		this.lineColor = lineColor;
	}


	/**
	 * @return the lineWidth
	 */
	public Integer getLineWidth()
	{
		return this.lineWidth;
	}


	/**
	 * If showLine is true, defines the line width (in pixels). <br>
	 *
	 * @param lineWidth
	 *            the lineWidth to set
	 */
	public void setLineWidth(final Integer lineWidth)
	{
		this.lineWidth = lineWidth;
	}

}
