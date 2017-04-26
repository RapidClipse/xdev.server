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


import com.vaadin.annotations.StyleSheet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Layout.MarginHandler;
import com.vaadin.ui.Layout.SpacingHandler;


/**
 * @author XDEV Software
 * @since 3.1
 */
@StyleSheet({"bootstrap4a6.v3.min.css"})
public class XdevBootstrapLayout extends CssLayout implements SpacingHandler, MarginHandler
{
	final static String			SPACING_SIZE		= "3";

	private final static String	SPACING_CLASS		= "pt-".concat(SPACING_SIZE);
	private final static String	MARGIN_ALL_CLASS	= "p-".concat(SPACING_SIZE);
	private final static String	MARGIN_TOP_CLASS	= "pt-".concat(SPACING_SIZE);
	private final static String	MARGIN_LEFT_CLASS	= "pl-".concat(SPACING_SIZE);
	private final static String	MARGIN_BOTTOM_CLASS	= "pb-".concat(SPACING_SIZE);
	private final static String	MARGIN_RIGHT_CLASS	= "pr-".concat(SPACING_SIZE);
	
	private boolean				spacing				= true;
	private MarginInfo			marginInfo			= new MarginInfo(true);
	
	
	public XdevBootstrapLayout()
	{
		super();
		
		addStyleName("container-fluid");
		updateSpacing();
		updateMargin();
	}
	
	
	@Override
	public void addComponent(final Component c)
	{
		if(!(c instanceof XdevBootstrapRow))
		{
			throw new IllegalArgumentException("Only " + XdevBootstrapRow.class.getSimpleName()
					+ "s are allowed as child components.");
		}
		
		super.addComponent(c);
		
		updateSpacing();
	}
	
	
	@Override
	public void setSpacing(final boolean spacing)
	{
		this.spacing = spacing;

		updateSpacing();
	}
	
	
	@Override
	public boolean isSpacing()
	{
		return this.spacing;
	}
	
	
	protected void updateSpacing()
	{
		if(this.spacing)
		{
			for(int i = 0, c = getComponentCount(); i < c; i++)
			{
				if(i == 0)
				{
					getComponent(i).removeStyleName(SPACING_CLASS);
				}
				else
				{
					getComponent(i).addStyleName(SPACING_CLASS);
				}
			}
		}
		else
		{
			for(int i = 0, c = getComponentCount(); i < c; i++)
			{
				getComponent(i).removeStyleName(SPACING_CLASS);
			}
		}
	}
	
	
	@Override
	public MarginInfo getMargin()
	{
		return this.marginInfo;
	}
	
	
	@Override
	public void setMargin(final boolean enabled)
	{
		setMargin(new MarginInfo(enabled));
		
		updateMargin();
	}
	
	
	@Override
	public void setMargin(final MarginInfo marginInfo)
	{
		this.marginInfo = marginInfo;
	}
	
	
	protected void updateMargin()
	{
		removeStyleName(MARGIN_ALL_CLASS);
		removeStyleName(MARGIN_TOP_CLASS);
		removeStyleName(MARGIN_LEFT_CLASS);
		removeStyleName(MARGIN_BOTTOM_CLASS);
		removeStyleName(MARGIN_RIGHT_CLASS);
		
		if(this.marginInfo != null)
		{
			if(this.marginInfo.hasAll())
			{
				addStyleName(MARGIN_ALL_CLASS);
			}
			else
			{
				if(this.marginInfo.hasTop())
				{
					addStyleName(MARGIN_TOP_CLASS);
				}
				if(this.marginInfo.hasLeft())
				{
					addStyleName(MARGIN_LEFT_CLASS);
				}
				if(this.marginInfo.hasBottom())
				{
					addStyleName(MARGIN_BOTTOM_CLASS);
				}
				if(this.marginInfo.hasRight())
				{
					addStyleName(MARGIN_RIGHT_CLASS);
				}
			}
		}
	}
}
