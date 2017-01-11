/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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
public class XdevColorPicker extends ColorPicker implements XdevField
{
	private final Extensions	extensions		= new Extensions();
	private boolean				persistValue	= PERSIST_VALUE_DEFAULT;
												
												
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E addExtension(final Class<? super E> type, final E extension)
	{
		return this.extensions.add(type,extension);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E getExtension(final Class<E> type)
	{
		return this.extensions.get(type);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPersistValue()
	{
		return this.persistValue;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPersistValue(final boolean persistValue)
	{
		this.persistValue = persistValue;
	}
}
