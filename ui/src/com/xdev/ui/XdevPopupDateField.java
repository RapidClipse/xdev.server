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


import java.util.Date;

import com.vaadin.data.Property;
import com.vaadin.ui.PopupDateField;


/**
 * A date entry component, which displays the actual date selector as a popup.
 *
 * @author XDEV Software
 */
public class XdevPopupDateField extends PopupDateField
{
	/**
	 *
	 */
	public XdevPopupDateField()
	{
		super();
	}
	
	
	/**
	 * @param dataSource
	 * @throws IllegalArgumentException
	 */
	public XdevPopupDateField(@SuppressWarnings("rawtypes") final Property dataSource)
			throws IllegalArgumentException
	{
		super(dataSource);
	}
	
	
	/**
	 * @param caption
	 * @param value
	 */
	public XdevPopupDateField(final String caption, final Date value)
	{
		super(caption,value);
	}
	
	
	/**
	 * @param caption
	 * @param dataSource
	 */
	public XdevPopupDateField(final String caption,
			@SuppressWarnings("rawtypes") final Property dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * @param caption
	 */
	public XdevPopupDateField(final String caption)
	{
		super(caption);
	}
}
