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

package com.xdev.charts;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class ChartArea
{
	private String	backgroundColor	= "white";
	private String	left;
	private String	top;
	private String	right;
	private String	width;
	private String	heigth;
	
	
	public String getBackgroundColor()
	{
		return this.backgroundColor;
	}
	
	
	/**
	 * Chart area background color. When a string is used, it can be either a hex
	 * string (e.g., '#fdc') or an English color name. <br>
	 * 
	 * @param backgroundColor
	 */
	public void setBackgroundColor(final String backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}
	
	
	public String getLeft()
	{
		return this.left;
	}
	
	
	/**
	 * How far to draw the chart from the left border. <br>
	 * 
	 * @param left
	 */
	public void setLeft(final String left)
	{
		this.left = left;
	}
	
	
	public String getRight()
	{
		return this.right;
	}
	
	
	/**
	 * How far to draw the chart from the right border. <br>
	 * 
	 * @param left
	 */
	public void setRight(final String right)
	{
		this.right = right;
	}
	
	
	public String getTop()
	{
		return this.top;
	}
	
	
	/**
	 * How far to draw the chart from the top border. <br>
	 * 
	 * @param top
	 */
	public void setTop(final String top)
	{
		this.top = top;
	}
	
	
	public String getWidth()
	{
		return this.width;
	}
	
	
	public void setWidth(final String width)
	{
		this.width = width;
	}
	
	
	public String getHeigth()
	{
		return this.heigth;
	}
	
	
	public void setHeigth(final String heigth)
	{
		this.heigth = heigth;
	}
	
}
