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


import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;


/**
 * An implementation of a check box -- an item that can be selected or
 * deselected, and which displays its state to the user.
 *
 * @author XDEV Software
 *
 */
public class XdevCheckBox extends CheckBox
{
	
	/**
	 * Creates a new checkbox.
	 */
	public XdevCheckBox()
	{
		super();
	}
	
	
	/**
	 * Creates a new checkbox with a caption and a set initial state.
	 * 
	 * @param caption
	 *            the caption of the checkbox
	 * @param initialState
	 *            the initial state of the checkbox
	 */
	public XdevCheckBox(final String caption, final boolean initialState)
	{
		super(caption,initialState);
	}
	
	
	/**
	 * Creates a new checkbox that is connected to a boolean property.
	 * 
	 * @param state
	 *            the Initial state of the switch-button.
	 * @param dataSource
	 */
	public XdevCheckBox(final String caption, final Property<?> dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * Creates a new checkbox with a set caption.
	 * 
	 * @param caption
	 *            the Checkbox caption.
	 */
	public XdevCheckBox(final String caption)
	{
		super(caption);
	}
}
