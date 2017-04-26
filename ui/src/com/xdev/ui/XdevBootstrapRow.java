/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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
import com.vaadin.ui.CssLayout;


/**
 * @author XDEV Software
 * @since 3.1
 */
public class XdevBootstrapRow extends CssLayout
{
	private final static String	NO_GUTTERS	= "no-gutters";

	private boolean				spacing		= true;


	public XdevBootstrapRow()
	{
		super();

		addStyleName("row");
	}


	@Override
	public void addComponent(final Component c)
	{
		if(!(c instanceof XdevBootstrapCol))
		{
			throw new IllegalArgumentException("Only " + XdevBootstrapCol.class.getSimpleName()
					+ "s are allowed as child components.");
		}

		super.addComponent(c);
	}


	public void setSpacing(final boolean spacing)
	{
		this.spacing = spacing;

		if(spacing)
		{
			removeStyleName(NO_GUTTERS);
		}
		else
		{
			addStyleName(NO_GUTTERS);
		}
	}


	public boolean isSpacing()
	{
		return this.spacing;
	}
}
