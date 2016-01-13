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


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;


/**
 * @author XDEV Software
 *
 */
public class XdevView extends CustomComponent implements View
{
	/**
	 *
	 */
	public XdevView()
	{
		super();

		setSizeFull();
	}
	
	
	public void setContent(final Component c)
	{
		setCompositionRoot(c);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enter(final ViewChangeEvent event)
	{
	}
}
