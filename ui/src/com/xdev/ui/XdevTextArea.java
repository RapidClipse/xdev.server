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
import com.vaadin.ui.TextArea;


/**
 * A text field that supports multi line editing.
 *
 * @author XDEV Software
 *
 */
public class XdevTextArea extends TextArea
{
	/**
	 * Constructs an empty TextArea.
	 */
	public XdevTextArea()
	{
		super();
	}
	
	
	/**
	 * Constructs a TextArea with given property data source.
	 * 
	 * @param dataSource
	 *            the data source for the field
	 */
	public XdevTextArea(final Property<?> dataSource)
	{
		super(dataSource);
	}
	
	
	/**
	 * Constructs a TextArea with given caption and property data source.
	 * 
	 * @param caption
	 *            the caption for the field
	 * @param dataSource
	 *            the data source for the field
	 */
	public XdevTextArea(final String caption, final Property<?> dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * Constructs a TextArea with given caption and value.
	 * 
	 * @param caption
	 *            the caption for the field
	 * @param value
	 *            the value for the field
	 */
	public XdevTextArea(final String caption, final String value)
	{
		super(caption,value);
	}
	
	
	/**
	 * Constructs an empty TextArea with given caption.
	 * 
	 * @param caption
	 *            the caption for the field.
	 */
	public XdevTextArea(final String caption)
	{
		super(caption);
	}
}
