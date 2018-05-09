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

package com.xdev.charts.gantt;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class Arrow
{
	private Integer	angle		= 45;
	private String	color		= "#000";
	private Integer	length		= 8;
	private Integer	radius		= 15;
	private Integer	spaceAfter	= 4;
	private Double	width		= 1.4;
	
	
	public Integer getAngle()
	{
		return this.angle;
	}
	
	
	/**
	 * The angle of the head of the arrow. <br>
	 * 
	 * @param angle
	 */
	public void setAngle(final Integer angle)
	{
		this.angle = angle;
	}
	
	
	public String getColor()
	{
		return this.color;
	}
	
	
	/**
	 * The color of the arrows. <br>
	 * 
	 * @param color
	 */
	public void setColor(final String color)
	{
		this.color = color;
	}
	
	
	public Integer getLength()
	{
		return this.length;
	}
	
	
	/**
	 * The length of the head of the arrow. <br>
	 * 
	 * @param length
	 */
	public void setLength(final Integer length)
	{
		this.length = length;
	}
	
	
	public Integer getRadius()
	{
		return this.radius;
	}
	
	
	/**
	 * The radius for defining the curve of the arrow between two tasks. <br>
	 * 
	 * @param radius
	 */
	public void setRadius(final Integer radius)
	{
		this.radius = radius;
	}
	
	
	public Integer getSpaceAfter()
	{
		return this.spaceAfter;
	}
	
	
	/**
	 * The amount of whitespace between the head of an arrow and the task to which
	 * it points. <br>
	 * 
	 * @param spaceAfter
	 */
	public void setSpaceAfter(final Integer spaceAfter)
	{
		this.spaceAfter = spaceAfter;
	}
	
	
	public Double getWidth()
	{
		return this.width;
	}
	
	
	/**
	 * The width of the arrows. <br>
	 * 
	 * @param width
	 */
	public void setWidth(final Double width)
	{
		this.width = width;
	}
}
