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
public class LegendOptions
{

	private String		alignment;
	private String		position	= "right";
	private Integer		maxLines	= 1;
	private TextStyle	textStyle;
	
	
	public String getAlignment()
	{
		return this.alignment;
	}
	
	
	/**
	 * Alignment of the legend. Can be one of the following: <br>
	 * <ul>
	 * <li>'start' - Aligned to the start of the area allocated for the legend.</li>
	 * <li>'center' - Centered in the area allocated for the legend.</li>
	 * <li>'end' - Aligned to the end of the area allocated for the legend.</li>
	 * </ul>
	 * <br>
	 * Start, center, and end are relative to the style -- vertical or horizontal --
	 * of the legend. For example, in a 'right' legend, 'start' and 'end' are at the
	 * top and bottom, respectively; for a 'top' legend, 'start' and 'end' would be
	 * at the left and right of the area, respectively. <br>
	 * <br>
	 * The default value depends on the legend's position. For 'bottom' legends, the
	 * default is 'center'; other legends default to 'start'.
	 * 
	 * @param alignment
	 */
	public void setAlignment(final String alignment)
	{
		this.alignment = alignment;
	}
	
	
	public String getPosition()
	{
		return this.position;
	}
	
	
	/**
	 * Position of the legend. Can be one of the following: <br>
	 * <ul>
	 * <li>'bottom' - Displays the legend below the chart.</li>
	 * <li>'labeled' - Draws lines connecting slices to their data values.</li>
	 * <li>'left' - Displays the legend left of the chart.</li>
	 * <li>'none' - Displays no legend.</li>
	 * <li>'right' - Displays the legend right of the chart.</li>
	 * <li>'top' - Displays the legend above the chart.</li>
	 * </ul>
	 * 
	 * @param position
	 */
	public void setPosition(final String position)
	{
		this.position = position;
	}
	
	
	public Integer getMaxLines()
	{
		return this.maxLines;
	}
	
	
	/**
	 * Maximum number of lines in the legend. Set this to a number greater than one
	 * to add lines to your legend. Note: The exact logic used to determine the
	 * actual number of lines rendered is still in flux. <br>
	 * <br>
	 * This option currently works only when legend.position is 'top'.
	 * 
	 * @param maxLines
	 */
	public void setMaxLines(final Integer maxLines)
	{
		this.maxLines = maxLines;
	}
	
	
	public TextStyle getTextStyle()
	{
		return this.textStyle;
	}
	
	
	/**
	 * An object that specifies the legend text style.
	 * 
	 * @param textStyle
	 */
	public void setTextStyle(final TextStyle textStyle)
	{
		this.textStyle = textStyle;
	}
}
