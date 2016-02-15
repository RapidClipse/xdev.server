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
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;


/**
 * A simple RichTextArea to edit HTML format text.
 *
 * Note, that using {@link TextField#setMaxLength(int)} method in
 * {@link RichTextArea} may produce unexpected results as formatting is counted
 * into length of field.
 *
 * @author XDEV Software
 * 		
 */
public class XdevRichTextArea extends RichTextArea implements XdevField
{
	private final Extensions	extensions		= new Extensions();
	private boolean				persistValue	= PERSIST_VALUE_DEFAULT;
												
												
	/**
	 * Constructs an empty <code>RichTextArea</code> with no caption.
	 */
	public XdevRichTextArea()
	{
		super();
	}
	
	
	/**
	 * Constructs a new <code>RichTextArea</code> that's bound to the specified
	 * <code>Property</code> and has no caption.
	 *
	 * @param dataSource
	 *            the data source for the editor value
	 */
	public XdevRichTextArea(final Property<?> dataSource)
	{
		super(dataSource);
	}
	
	
	/**
	 * Constructs a new <code>RichTextArea</code> that's bound to the specified
	 * <code>Property</code> and has the given caption.
	 *
	 * @param caption
	 *            the caption for the editor.
	 * @param dataSource
	 *            the data source for the editor value
	 */
	public XdevRichTextArea(final String caption, final Property<?> dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * Constructs a new <code>RichTextArea</code> with the given caption and
	 * initial text contents.
	 *
	 * @param caption
	 *            the caption for the editor.
	 * @param value
	 *            the initial text content of the editor.
	 */
	public XdevRichTextArea(final String caption, final String value)
	{
		super(caption,value);
	}
	
	
	/**
	 *
	 * Constructs an empty <code>RichTextArea</code> with the given caption.
	 *
	 * @param caption
	 *            the caption for the editor.
	 */
	public XdevRichTextArea(final String caption)
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
