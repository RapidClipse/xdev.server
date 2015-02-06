/*
 * Copyright (C) 2015 by XDEV Software, All Rights Reserved.
 *
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
