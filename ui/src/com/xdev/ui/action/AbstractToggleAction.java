/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
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

package com.xdev.ui.action;


import com.vaadin.server.Resource;
import com.xdev.ui.XdevUI;


/**
 * @author XDEV Software
 * 		
 */
public abstract class AbstractToggleAction extends AbstractAction implements ToggleAction
{
	private boolean selected = false;


	/**
	 */
	public AbstractToggleAction(final XdevUI ui)
	{
		super(ui);
	}
	
	
	/**
	 * @param caption
	 */
	public AbstractToggleAction(final XdevUI ui, final String caption)
	{
		super(ui,caption);
	}


	/**
	 * @param icon
	 */
	public AbstractToggleAction(final XdevUI ui, final Resource icon)
	{
		super(ui,icon);
	}
	
	
	/**
	 * @param caption
	 * @param icon
	 */
	public AbstractToggleAction(final XdevUI ui, final String caption, final Resource icon)
	{
		super(ui,caption,icon);
	}
	
	
	@Override
	public boolean isSelected()
	{
		return this.selected;
	}
	
	
	@Override
	public void setSelected(final boolean selected)
	{
		if(this.selected != selected)
		{
			final Object oldValue = this.selected;
			
			this.selected = selected;
			
			firePropertyChange(SELECTED_PROPERTY,oldValue,selected);
		}
	}
}
