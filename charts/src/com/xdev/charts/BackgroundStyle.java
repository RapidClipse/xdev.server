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

package com.xdev.charts;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class BackgroundStyle
{
	private String	stroke		= "#666";
	private Integer	strokeWidth	= 0;
	private String	fill		= "white";
	
	
	public String getStroke()
	{
		return this.stroke;
	}
	
	
	/**
	 * The color of the chart border, as an HTML color string. <br>
	 * Can be a simple HTML color string, for example: 'red' or '#00cc00'.
	 * 
	 * @param stroke
	 */
	public void setStroke(final String stroke)
	{
		this.stroke = stroke;
	}
	
	
	public Integer getStrokeWidth()
	{
		return this.strokeWidth;
	}
	
	
	/**
	 * The border width, in pixels.
	 * 
	 * @param strokeWidth
	 */
	public void setStrokeWidth(final Integer strokeWidth)
	{
		this.strokeWidth = strokeWidth;
	}
	
	
	public String getFill()
	{
		return this.fill;
	}
	
	
	/**
	 * The chart fill color, as an HTML color string. <br>
	 * Can be a simple HTML color string, for example: 'red' or '#00cc00'.
	 * 
	 * @param fill
	 */
	public void setFill(final String fill)
	{
		this.fill = fill;
	}
}
