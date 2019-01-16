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

package com.xdev.charts.config;


import java.io.Serializable;

import com.xdev.charts.TextStyle;


/**
 * @author XDEV Software
 *
 */
public class XdevPieSlice implements Serializable
{
	private String		color;
	private Double		offset		= 0.0;

	private TextStyle	textStyle	= new TextStyle();


	/**
	 * @return the color
	 */
	public String getColor()
	{
		return this.color;
	}


	/**
	 * The color to use for this slice. Specify a valid HTML color string. <br>
	 *
	 * @param color
	 *            the color to set
	 */
	public void setColor(final String color)
	{
		this.color = color;
	}


	/**
	 * @return the offset
	 */
	public Double getOffset()
	{
		return this.offset;
	}


	/**
	 * How far to separate the slice from the rest of the pie, from 0.0 (not at all)
	 * to 1.0 (the pie's radius). <br>
	 *
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(final Double offset)
	{
		this.offset = offset;
	}
	
	
	/**
	 * @return the pieSliceTextStyle
	 */
	public TextStyle getTextStyle()
	{
		return this.textStyle;
	}
	
	
	/**
	 * An object that specifies the slice text style. <br>
	 *
	 * @param pieSliceTextStyle
	 *            the pieSliceTextStyle to set
	 */
	public void setTextStyle(final TextStyle textStyle)
	{
		this.textStyle = textStyle;
	}
	
}
