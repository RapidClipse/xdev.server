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
import com.vaadin.ui.TextField;


/**
 * <p>
 * A text editor component that can be bound to any bindable Property. The text
 * editor supports both multiline and single line modes, default is one-line
 * mode.
 * </p>
 *
 * <p>
 * Since <code>TextField</code> extends <code>AbstractField</code> it implements
 * the {@link com.vaadin.data.Buffered} interface. A <code>TextField</code> is
 * in write-through mode by default, so
 * {@link com.vaadin.ui.AbstractField#setWriteThrough(boolean)} must be called
 * to enable buffering.
 * </p>
 *
 * @author XDEV Software
 * 		
 */
public class XdevTextField extends TextField implements XdevComponent
{
	private final Extensions extensions = new Extensions();
	
	
	/**
	 * Constructs an empty <code>TextField</code> with no caption.
	 */
	public XdevTextField()
	{
		super();
	}
	
	
	/**
	 * Constructs a new <code>TextField</code> that's bound to the specified
	 * <code>Property</code> and has no caption.
	 *
	 * @param dataSource
	 *            the Property to be edited with this editor.
	 */
	public XdevTextField(final Property<?> dataSource)
	{
		super(dataSource);
	}
	
	
	/**
	 * Constructs a new <code>TextField</code> that's bound to the specified
	 * <code>Property</code> and has the given caption <code>String</code>.
	 *
	 * @param caption
	 *            the caption <code>String</code> for the editor.
	 * @param dataSource
	 *            the Property to be edited with this editor.
	 */
	public XdevTextField(final String caption, final Property<?> dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * Constructs a new <code>TextField</code> with the given caption and
	 * initial text contents. The editor constructed this way will not be bound
	 * to a Property unless
	 * {@link com.vaadin.data.Property.Viewer#setPropertyDataSource(Property)}
	 * is called to bind it.
	 *
	 * @param caption
	 *            the caption <code>String</code> for the editor.
	 * @param value
	 *            the initial text content of the editor.
	 */
	public XdevTextField(final String caption, final String value)
	{
		super(caption,value);
	}
	
	
	/**
	 * Constructs an empty <code>TextField</code> with given caption.
	 *
	 * @param caption
	 *            the caption <code>String</code> for the editor.
	 */
	public XdevTextField(final String caption)
	{
		super(caption);
	}
	
	
	// init defaults
	{
		setNullRepresentation("");
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
}
