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
public class TextStyle
{

	private String	color	= "black";
	private String	fontName;
	private Integer	fontSize;
	private Boolean	bold	= false;
	private Boolean	italic	= false;
	
	
	public String getColor()
	{
		return this.color;
	}


	/**
	 * The color can be any HTML color string, for example: 'red' or '#00cc00' <br>
	 *
	 * @param color
	 */
	public void setColor(final String color)
	{
		this.color = color;
	}
	
	
	public String getFontName()
	{
		return this.fontName;
	}
	
	
	/**
	 * Sets the font name of the text style <br>
	 * 
	 * @param fontName
	 */
	public void setFontName(final String fontName)
	{
		this.fontName = fontName;
	}
	
	
	public Integer getFontSize()
	{
		return this.fontSize;
	}


	/**
	 * The font size, in pixels <br>
	 *
	 * @param fontName
	 */
	public void setFontSize(final Integer fontSize)
	{
		this.fontSize = fontSize;
	}
	
	
	public Boolean getBold()
	{
		return this.bold;
	}
	
	
	public void setBold(final Boolean bold)
	{
		this.bold = bold;
	}
	
	
	public Boolean getItalic()
	{
		return this.italic;
	}
	
	
	public void setItalic(final Boolean italic)
	{
		this.italic = italic;
	}
}
