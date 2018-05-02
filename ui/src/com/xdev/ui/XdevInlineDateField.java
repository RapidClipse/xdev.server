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


import java.util.Date;

import com.vaadin.v7.data.Property;
import com.vaadin.v7.ui.InlineDateField;


/**
 * A date entry component, which displays the actual date selector inline.
 *
 * @author XDEV Software
 * 
 */
@SuppressWarnings("deprecation")
public class XdevInlineDateField extends InlineDateField implements XdevField
{
	private final Extensions	extensions		= new Extensions();
	private boolean				persistValue	= PERSIST_VALUE_DEFAULT;
	
	
	/**
	 *
	 */
	public XdevInlineDateField()
	{
		super();
	}


	/**
	 * @param dataSource
	 * @throws IllegalArgumentException
	 */
	public XdevInlineDateField(final Property<?> dataSource) throws IllegalArgumentException
	{
		super(dataSource);
	}


	/**
	 * @param caption
	 * @param value
	 */
	public XdevInlineDateField(final String caption, final Date value)
	{
		super(caption,value);
	}


	/**
	 * @param caption
	 * @param dataSource
	 */
	public XdevInlineDateField(final String caption, final Property<?> dataSource)
	{
		super(caption,dataSource);
	}


	/**
	 * @param caption
	 */
	public XdevInlineDateField(final String caption)
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
