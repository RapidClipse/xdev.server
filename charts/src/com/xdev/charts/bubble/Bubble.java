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

package com.xdev.charts.bubble;


import com.xdev.charts.TextStyle;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class Bubble
{
	private Double		opacity	= 0.8;
	private String		stroke	= "#ccc";
	private TextStyle	textStyle;
	
	
	public Double getOpacity()
	{
		return this.opacity;
	}
	
	
	/**
	 * The opacity of the bubbles, where 0 is fully transparent and 1 is fully
	 * opaque. <br>
	 * 
	 * @param opacity
	 */
	public void setOpacity(final Double opacity)
	{
		this.opacity = opacity;
	}
	
	
	public String getStroke()
	{
		return this.stroke;
	}
	
	
	/**
	 * The color of the bubbles' stroke. <br>
	 * 
	 * @param stroke
	 */
	public void setStroke(final String stroke)
	{
		this.stroke = stroke;
	}
	
	
	public TextStyle getTextStyle()
	{
		return this.textStyle;
	}
	
	
	/**
	 * An object that specifies the bubble text style. <br>
	 * 
	 * @param textStyle
	 */
	public void setTextStyle(final TextStyle textStyle)
	{
		this.textStyle = textStyle;
	}

}
