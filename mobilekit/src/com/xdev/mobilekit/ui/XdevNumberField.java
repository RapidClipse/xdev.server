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

package com.xdev.mobilekit.ui;


import com.vaadin.addon.touchkit.ui.NumberField;
import com.vaadin.data.Property;
import com.vaadin.ui.TextField;


/**
 * A {@link TextField} that is intended for numerical input.
 * <p>
 * Modern browsers and devices provide a better interface for entering numbers
 * when this field is used. This is especially important for touch devices that
 * can provide an on-screen keyboard optimized for numerical input.
 * </p>
 *
 * @author XDEV Software
 *
 */
public class XdevNumberField extends NumberField
{
	/**
	 *
	 */
	public XdevNumberField()
	{
		super();
	}


	/**
	 * @param dataSource
	 */
	public XdevNumberField(final Property<?> dataSource)
	{
		super(dataSource);
	}


	/**
	 * @param caption
	 * @param dataSource
	 */
	public XdevNumberField(final String caption, final Property<?> dataSource)
	{
		super(caption,dataSource);
	}


	/**
	 * @param caption
	 * @param value
	 */
	public XdevNumberField(final String caption, final String value)
	{
		super(caption,value);
	}


	/**
	 * @param caption
	 */
	public XdevNumberField(final String caption)
	{
		super(caption);
	}
}
