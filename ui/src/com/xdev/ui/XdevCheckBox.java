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

package com.xdev.ui;


import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.CheckBox;


/**
 * An implementation of a check box -- an item that can be selected or
 * deselected, and which displays its state to the user.
 *
 * @author XDEV Software
 *
 */
@SuppressWarnings("deprecation")
public class XdevCheckBox extends CheckBox implements XdevField
{
	private final Extensions	extensions		= new Extensions();
	private boolean				persistValue	= PERSIST_VALUE_DEFAULT;
	
	
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
