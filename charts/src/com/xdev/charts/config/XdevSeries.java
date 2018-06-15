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

package com.xdev.charts.config;


import java.io.Serializable;


/**
 * @author XDEV Software
 *
 */
public class XdevSeries implements Serializable
{
	private String	type			= "line";
	private String	color;
	private String	labelInLegend;
	private boolean	visibleInLegend	= true;
	private Integer	lineWidth		= 2;
	private Integer	pointSize		= 0;


	/**
	 * @return the type
	 */
	public String getType()
	{
		return this.type;
	}


	/**
	 * The type of marker for this series. Valid values are 'line', 'area', 'bars'
	 * and 'steppedArea'. Note that bars are actually vertical bars (columns). The
	 * default value is specified by the chart's seriesType option. <br>
	 *
	 * @param type
	 *            the type to set
	 */
	public void setType(final String type)
	{
		this.type = type;
	}
	
	
	/**
	 * @return the color
	 */
	public String getColor()
	{
		return this.color;
	}
	
	
	/**
	 * The color to use for this series. Specify a valid HTML color string. <br>
	 *
	 * @param color
	 *            the color to set
	 */
	public void setColor(final String color)
	{
		this.color = color;
	}
	
	
	/**
	 * @return the labelInLegend
	 */
	public String getLabelInLegend()
	{
		return this.labelInLegend;
	}
	
	
	/**
	 * The description of the series to appear in the chart legend. <br>
	 *
	 * @param labelInLegend
	 *            the labelInLegend to set
	 */
	public void setLabelInLegend(final String labelInLegend)
	{
		this.labelInLegend = labelInLegend;
	}
	
	
	/**
	 * @return the visibleInLegend
	 */
	public boolean isVisibleInLegend()
	{
		return this.visibleInLegend;
	}
	
	
	/**
	 * A boolean value, where true means that the series should have a legend entry,
	 * and false means that it should not. Default is true. <br>
	 *
	 * @param visibleInLegend
	 *            the visibleInLegend to set
	 */
	public void setVisibleInLegend(final boolean visibleInLegend)
	{
		this.visibleInLegend = visibleInLegend;
	}


	/**
	 * @return the lineWidth
	 */
	public Integer getLineWidth()
	{
		return this.lineWidth;
	}


	/**
	 * Data line width in pixels. Use zero to hide all lines and show only the
	 * points. <br>
	 *
	 * @param lineWidth
	 *            the lineWidth to set
	 */
	public void setLineWidth(final Integer lineWidth)
	{
		this.lineWidth = lineWidth;
	}
	
	
	/**
	 * @return the pointSize
	 */
	public Integer getPointSize()
	{
		return this.pointSize;
	}
	
	
	/**
	 * Diameter of displayed points in pixels. Use zero to hide all points. <br>
	 *
	 * @param pointSize
	 *            the pointSize to set
	 */
	public void setPointSize(final Integer pointSize)
	{
		this.pointSize = pointSize;
	}
	
}
