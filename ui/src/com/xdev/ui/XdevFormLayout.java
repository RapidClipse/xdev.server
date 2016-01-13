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


import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalLayout;


/**
 * FormLayout is a close relative of {@link VerticalLayout}, but in FormLayout
 * captions are rendered to the left of their respective components. Required
 * and validation indicators are shown between the captions and the fields.
 *
 * FormLayout by default has component spacing on. Also margin top and margin
 * bottom are by default on.
 *
 * @author XDEV Software
 *
 */
public class XdevFormLayout extends FormLayout
{
	/**
	 * Constructs an empty FormLayout.
	 */
	public XdevFormLayout()
	{
		super();
	}
	
	
	/**
	 * Constructs a FormLayout and adds the given components to it.
	 *
	 * @see AbstractOrderedLayout#addComponents(Component...)
	 *
	 * @param children
	 *            Components to add to the FormLayout
	 */
	public XdevFormLayout(final Component... children)
	{
		super(children);
	}

	// init defaults
	{
		setMargin(true);
		setSpacing(true);
	}
}
