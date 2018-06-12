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

package com.xdev.charts.geo;


import java.io.Serializable;

import com.xdev.charts.BackgroundStyle;
import com.xdev.charts.ColorAxis;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class XdevGeoChartConfig implements Serializable
{
	private String			region				= "world";
	private String			displayMode			= "auto";
	private String			defaultColor		= "#267114";
	private String			datalessRegionColor	= "#F5F5F5";

	private BackgroundStyle	backgroundColor		= new BackgroundStyle();
	private ColorAxis		colorAxis;
	
	
	public String getRegion()
	{
		return this.region;
	}
	
	
	/**
	 * The area to display on the geochart. (Surrounding areas will be displayed as
	 * well.) Can be one of the following: <br>
	 * <ul>
	 * <li>'world' - A geochart of the entire world.</li>
	 * <li>A continent or a sub-continent, specified by its 3-digit code, e.g.,
	 * '011' for Western Africa.</li>
	 * <li>A country, specified by its ISO 3166-1 alpha-2 code, e.g., 'AU' for
	 * Australia.</li>
	 * </ul>
	 *
	 * @param region
	 */
	public void setRegion(final String region)
	{
		this.region = region;
	}
	
	
	public String getDisplayMode()
	{
		return this.displayMode;
	}


	/**
	 * Which type of geochart this is. The DataTable format must match the value
	 * specified. The following values are supported: <br>
	 * <ul>
	 * <li>'auto' - Choose based on the format of the DataTable.</li>
	 * <li>'regions' - Color the regions on the geochart.</li>
	 * <li>'markers' - Place markers on the regions.</li>
	 * <li>'text' - Label the regions with text from the DataTable.</li>
	 * </ul>
	 * <br>
	 * Default: auto <br>
	 *
	 * @param displayMode
	 */
	public void setDisplayMode(final String displayMode)
	{
		this.displayMode = displayMode;
	}


	public BackgroundStyle getBackgroundColor()
	{
		return this.backgroundColor;
	}


	public void setBackgroundColor(final BackgroundStyle backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}
	
	
	/**
	 * @return the defaultColor
	 */
	public String getDefaultColor()
	{
		return this.defaultColor;
	}
	
	
	/**
	 * The color to use for data points in a geochart when the location (e.g., 'US'
	 * ) is present but the value is either null or unspecified. This is distinct
	 * from datalessRegionColor, which is the color used when data is missing. <br>
	 *
	 * @param defaultColor
	 *            the defaultColor to set
	 */
	public void setDefaultColor(final String defaultColor)
	{
		this.defaultColor = defaultColor;
	}
	
	
	/**
	 * @return the datalessRegionColor
	 */
	public String getDatalessRegionColor()
	{
		return this.datalessRegionColor;
	}


	/**
	 * Color to assign to regions with no associated data. <br>
	 * Default: '#F5F5F5' <br>
	 *
	 * @param datalessRegionColor
	 *            the datalessRegionColor to set
	 */
	public void setDatalessRegionColor(final String datalessRegionColor)
	{
		this.datalessRegionColor = datalessRegionColor;
	}
	
	
	/**
	 * @return the colorAxis
	 */
	public ColorAxis getColorAxis()
	{
		return this.colorAxis;
	}
	
	
	/**
	 * An object that specifies a mapping between color column values and colors or
	 * a gradient scale. <br>
	 *
	 * @param colorAxis
	 *            the colorAxis to set
	 */
	public void setColorAxis(final ColorAxis colorAxis)
	{
		this.colorAxis = colorAxis;
	}
	
}
