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


import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.ui.NativeSelect;


/**
 * This is a simple drop-down select without, for instance, support for
 * multiselect, new items, lazyloading, and other advanced features. Sometimes
 * "native" select without all the bells-and-whistles of the ComboBox is a
 * better choice.
 *
 * @author XDEV Software
 *
 */
public class XdevNativeSelect extends NativeSelect
{
	/**
	 *
	 */
	public XdevNativeSelect()
	{
		super();
	}
	
	
	/**
	 * @param caption
	 * @param options
	 */
	public XdevNativeSelect(final String caption, final Collection<?> options)
	{
		super(caption,options);
	}
	
	
	/**
	 * @param caption
	 * @param dataSource
	 */
	public XdevNativeSelect(final String caption, final Container dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * @param caption
	 */
	public XdevNativeSelect(final String caption)
	{
		super(caption);
	}
}
