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


import com.vaadin.ui.Accordion;
import com.vaadin.ui.Component;


/**
 * An accordion is a component similar to a {@link XdevTabSheet}, but with a
 * vertical orientation and the selected component presented between tabs.
 *
 * Closable tabs are not supported by the accordion.
 *
 * The {@link Accordion} can be styled with the .v-accordion, .v-accordion-item,
 * .v-accordion-item-first and .v-accordion-item-caption styles.
 *
 * @author XDEV Software
 *		
 */
public class XdevAccordion extends Accordion implements XdevComponent
{
	private final Extensions extensions = new Extensions();
	
	
	/**
	 * Creates an empty accordion.
	 */
	public XdevAccordion()
	{
		super();
	}
	
	
	/**
	 * Constructs a new accordion containing the given components.
	 *
	 * @param components
	 *            The components to add to the accordion. Each component will be
	 *            added to a separate tab.
	 */
	public XdevAccordion(final Component... components)
	{
		super(components);
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
