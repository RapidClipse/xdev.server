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


import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;


/**
 * A component container.
 * <p>
 * The panel displays scrollbars if the content gets too big to display.
 *
 * @author XDEV Software
 *		
 */
public class XdevPanel extends Panel implements XdevComponent
{
	private final Extensions extensions = new Extensions();
	
	
	/**
	 * Creates a new empty panel.
	 */
	public XdevPanel()
	{
		super();
	}


	/**
	 * Creates a new empty panel which contains the given content.
	 *
	 * @param content
	 *            the content for the panel.
	 */
	public XdevPanel(final Component content)
	{
		super(content);
	}


	/**
	 * Creates a new empty panel with the given caption and content.
	 *
	 * @param caption
	 *            the caption of the panel (HTML).
	 * @param content
	 *            the content used in the panel.
	 */
	public XdevPanel(final String caption, final Component content)
	{
		super(caption,content);
	}


	/**
	 * Creates a new empty panel with caption.
	 *
	 * @param caption
	 *            the caption used in the panel (HTML).
	 */
	public XdevPanel(final String caption)
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
