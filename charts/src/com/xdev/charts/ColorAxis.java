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


import java.util.ArrayList;
import java.util.List;


/**
 * @author XDEV Software (SS)
 * @since 4.0
 *
 */
public class ColorAxis
{
	private Integer			minValue;
	private Integer			maxValue;
	private List<String>	colors	= new ArrayList<>();
	
	
	/**
	 * @return the minValue
	 */
	public Integer getMinValue()
	{
		return this.minValue;
	}
	
	
	/**
	 * If present, specifies a minimum value for chart color data. Color data values
	 * of this value and lower will be rendered as the first color in the
	 * colorAxis.colors range. <br>
	 *
	 * @param minValue
	 *            the minValue to set
	 */
	public void setMinValue(final Integer minValue)
	{
		this.minValue = minValue;
	}
	
	
	/**
	 * @return the maxValue
	 */
	public Integer getMaxValue()
	{
		return this.maxValue;
	}
	
	
	/**
	 * If present, specifies a maximum value for chart color data. Color data values
	 * of this value and higher will be rendered as the last color in the
	 * colorAxis.colors range. <br>
	 *
	 * @param maxValue
	 *            the maxValue to set
	 */
	public void setMaxValue(final Integer maxValue)
	{
		this.maxValue = maxValue;
	}
	
	
	/**
	 * @return the colors
	 */
	public List<String> getColors()
	{
		return this.colors;
	}
	
	
	/**
	 * Colors to assign to values in the visualization. An array of strings, where
	 * each element is an HTML color string, for example: 'red', #004411. You must
	 * have at least two values; the gradient will include all your values, plus
	 * calculated intermediary values, with the first color as the smallest value,
	 * and the last color as the highest. <br>
	 *
	 * @param colors
	 *            the colors to set
	 */
	public void setColors(final List<String> colors)
	{
		this.colors = colors;
	}
	
}
