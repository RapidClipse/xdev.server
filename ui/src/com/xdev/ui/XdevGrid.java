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


import com.vaadin.v7.data.Container.Indexed;
import com.vaadin.v7.ui.Grid;


/**
 * @author XDEV Software
 * @since 4.0
 */
@SuppressWarnings("deprecation")
public class XdevGrid extends Grid implements XdevComponent
{
	private final Extensions extensions = new Extensions();


	public XdevGrid()
	{
		super();
	}


	/**
	 * @param dataSource
	 */
	public XdevGrid(final Indexed dataSource)
	{
		super(dataSource);
	}


	/**
	 * @param caption
	 * @param dataSource
	 */
	public XdevGrid(final String caption, final Indexed dataSource)
	{
		super(caption,dataSource);
	}


	/**
	 * @param caption
	 */
	public XdevGrid(final String caption)
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
}