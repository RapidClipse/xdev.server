/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */
 
package com.xdev.ui;


import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.ColorPicker;


/**
 * A class that defines default (button-like) implementation for a color picker
 * component.
 *
 * @author XDEV Software
 *
 */
public class XdevColorPicker extends ColorPicker
{

	/**
	 * Instantiates a new color picker.
	 */
	public XdevColorPicker()
	{
		super();
	}


	/**
	 * Instantiates a new color picker.
	 *
	 * @param popupCaption
	 *            caption of the color select popup
	 */
	public XdevColorPicker(final String popupCaption)
	{
		super(popupCaption);
	}


	/**
	 * Instantiates a new color picker.
	 *
	 * @param popupCaption
	 *            caption of the color select popup
	 * @param initialColor
	 *            the initial color
	 */
	public XdevColorPicker(final String popupCaption, final Color initialColor)
	{
		super(popupCaption,initialColor);
	}
}
