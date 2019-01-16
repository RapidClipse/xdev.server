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


import java.io.Serializable;
import java.util.List;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class VAxis implements Serializable
{
	private String			title				= "";
	private List<Integer>	ticks;
	private TextStyle		titleTextStyle;
	private TextStyle		textStyle;
	private boolean			slantedText;
	private Integer			slantedTextAngle	= 30;
	private String			textPosition		= "out";


	public VAxis(final String title)
	{
		this.title = title;
	}


	public String getTitle()
	{
		return this.title;
	}


	/**
	 * Title property that specifies a title for the vertical axis. <br>
	 *
	 * @param title
	 */
	public void setTitle(final String title)
	{
		this.title = title;
	}


	public List<Integer> getTicks()
	{
		return this.ticks;
	}


	/**
	 *
	 * Replaces the automatically generated X-axis ticks with the specified array.
	 * Each element of the array should be either a valid tick value (such as a
	 * number, date, datetime, or timeofday), or an object.
	 *
	 * @param ticks
	 */
	public void setTicks(final List<Integer> ticks)
	{
		this.ticks = ticks;
	}


	public TextStyle getTextStyle()
	{
		return this.textStyle;
	}


	/**
	 * An object that specifies the vertical axis text style. <br>
	 *
	 * @param textStyle
	 */
	public void setTextStyle(final TextStyle textStyle)
	{
		this.textStyle = textStyle;
	}


	public boolean isSlantedText()
	{
		return this.slantedText;
	}


	/**
	 * If true, draw the vertical axis text at an angle, to help fit more text along
	 * the axis; if false, draw vertical axis text upright. <br>
	 *
	 * @param slantedText
	 */
	public void setSlantedText(final boolean slantedText)
	{
		this.slantedText = slantedText;
	}


	public Integer getSlantedTextAngle()
	{
		return this.slantedTextAngle;
	}


	/**
	 * The angle of the vertical axis text, if it's drawn slanted. Ignored if
	 * vertical.slantedText is false, or is in auto mode, and the chart decided to
	 * draw the text vertically. <br>
	 *
	 * @param slantedTextAngle
	 *            Integer 1—90
	 */
	public void setSlantedTextAngle(final Integer slantedTextAngle)
	{
		this.slantedTextAngle = slantedTextAngle;
	}


	public TextStyle getTitleTextStyle()
	{
		return this.titleTextStyle;
	}


	/**
	 * An object that specifies the vertical axis title text style. <br>
	 *
	 * @param titleTextStyle
	 */
	public void setTitleTextStyle(final TextStyle titleTextStyle)
	{
		this.titleTextStyle = titleTextStyle;
	}


	public String getTextPosition()
	{
		return this.textPosition;
	}


	/**
	 * Position of the vertical axis text, relative to the chart area. Supported
	 * values: 'out', 'in', 'none'. <br>
	 *
	 * @param textPosition
	 */
	public void setTextPosition(final String textPosition)
	{
		this.textPosition = textPosition;
	}
}
