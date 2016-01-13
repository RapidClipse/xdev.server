/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
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
 */

package com.xdev.ui;


import com.vaadin.ui.Slider;


/**
 * A component for selecting a numerical value within a range.
 *
 * @author XDEV Software
 *
 */
public class XdevSlider extends Slider
{
	/**
	 * Default slider constructor. Sets all values to defaults and the slide
	 * handle at minimum value.
	 * 
	 */
	public XdevSlider()
	{
		super();
	}
	
	
	/**
	 * Create a new slider with the given range and resolution.
	 * 
	 * @param min
	 *            The minimum value of the slider
	 * @param max
	 *            The maximum value of the slider
	 * @param resolution
	 *            The number of digits after the decimal point.
	 */
	public XdevSlider(final double min, final double max, final int resolution)
	{
		super(min,max,resolution);
	}
	
	
	/**
	 * Create a new slider with the given range that only allows integer values.
	 * 
	 * @param min
	 *            The minimum value of the slider
	 * @param max
	 *            The maximum value of the slider
	 */
	public XdevSlider(final int min, final int max)
	{
		super(min,max);
	}
	
	
	/**
	 * Create a new slider with the given caption and range that only allows
	 * integer values.
	 * 
	 * @param caption
	 *            The caption for the slider
	 * @param min
	 *            The minimum value of the slider
	 * @param max
	 *            The maximum value of the slider
	 */
	public XdevSlider(final String caption, final int min, final int max)
	{
		super(caption,min,max);
	}
	
	
	/**
	 * Create a new slider with the caption given as parameter.
	 * 
	 * The range of the slider is set to 0-100 and only integer values are
	 * allowed.
	 * 
	 * @param caption
	 *            The caption for this slider (e.g. "Volume").
	 */
	public XdevSlider(final String caption)
	{
		super(caption);
	}
}
