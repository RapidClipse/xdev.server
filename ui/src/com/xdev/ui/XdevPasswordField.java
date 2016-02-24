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
 * 
 * For further information see 
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.ui;


import com.vaadin.data.Property;
import com.vaadin.ui.PasswordField;


/**
 * A field that is used to enter secret text information like passwords. The
 * entered text is not displayed on the screen.
 *
 * @author XDEV Software
 * 		
 */
public class XdevPasswordField extends PasswordField implements XdevField
{
	private final Extensions	extensions		= new Extensions();
	private boolean				persistValue	= PERSIST_VALUE_DEFAULT;
												
												
	/**
	 * Constructs an empty PasswordField.
	 */
	public XdevPasswordField()
	{
		super();
	}
	
	
	/**
	 * Constructs a PasswordField with given property data source.
	 *
	 * @param dataSource
	 *            the property data source for the field
	 */
	public XdevPasswordField(final Property<?> dataSource)
	{
		super(dataSource);
	}
	
	
	/**
	 * Constructs a PasswordField with given caption and property data source.
	 *
	 * @param caption
	 *            the caption for the field
	 * @param dataSource
	 *            the property data source for the field
	 */
	public XdevPasswordField(final String caption, final Property<?> dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * Constructs a PasswordField with given value and caption.
	 *
	 * @param caption
	 *            the caption for the field
	 * @param value
	 *            the value for the field
	 */
	public XdevPasswordField(final String caption, final String value)
	{
		super(caption,value);
	}
	
	
	/**
	 * Constructs a PasswordField with given caption.
	 *
	 * @param caption
	 *            the caption for the field
	 */
	public XdevPasswordField(final String caption)
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
