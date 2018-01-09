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


import com.vaadin.data.Property;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;


/**
 * Label component for showing non-editable short texts.
 *
 * The label content can be set to the modes specified by {@link ContentMode}
 *
 * <p>
 * The contents of the label may contain simple formatting:
 * <ul>
 * <li><b>&lt;b&gt;</b> Bold
 * <li><b>&lt;i&gt;</b> Italic
 * <li><b>&lt;u&gt;</b> Underlined
 * <li><b>&lt;br/&gt;</b> Linebreak
 * <li><b>&lt;ul&gt;&lt;li&gt;item 1&lt;/li&gt;&lt;li&gt;item
 * 2&lt;/li&gt;&lt;/ul&gt;</b> List of items
 * </ul>
 * The <b>b</b>,<b>i</b>,<b>u</b> and <b>li</b> tags can contain all the tags in
 * the list recursively.
 * </p>
 *
 * @author XDEV Software
 *		
 */
public class XdevLabel extends Label implements XdevComponent
{
	private final Extensions extensions = new Extensions();
	
	
	/**
	 * Creates an empty Label.
	 */
	public XdevLabel()
	{
		super();
	}


	/**
	 * Creates a new instance of Label with text-contents read from given
	 * datasource.
	 *
	 * @param contentSource
	 * @param contentMode
	 */
	public XdevLabel(final Property<?> contentSource, final ContentMode contentMode)
	{
		super(contentSource,contentMode);
	}


	/**
	 * Creates a new instance of Label with text-contents read from given
	 * datasource.
	 *
	 * @param contentSource
	 */
	public XdevLabel(final Property<?> contentSource)
	{
		super(contentSource);
	}


	/**
	 * Creates a new instance of Label with text-contents.
	 *
	 * @param content
	 * @param contentMode
	 */
	public XdevLabel(final String content, final ContentMode contentMode)
	{
		super(content,contentMode);
	}


	/**
	 * Creates a new instance of Label with text-contents.
	 *
	 * @param content
	 */
	public XdevLabel(final String content)
	{
		super(content);
	}
	
	
	// init defaults
	{
		setSizeUndefined();
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
