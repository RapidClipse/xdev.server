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


import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.xdev.ui.persistence.GuiPersistence;


/**
 * @author XDEV Software
 *
 */
public class XdevView extends CustomComponent implements View, XdevComponent
{
	private final Extensions extensions = new Extensions();
	
	
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
	
	
	/**
	 * Persists the state of all components in this view and returns the
	 * serialized data which can be stored in an arbitrary location.
	 * <p>
	 * This state can be restored with {@link #restoreState(String)}
	 *
	 * @return the serialized state data of all components
	 * @see GuiPersistence#save(Component, String)
	 */
	public String persistState()
	{
		return GuiPersistence.save(this,getClass().getName());
	}
	
	
	/**
	 * Restores a previously saved state.
	 *
	 * @param data
	 *            the serialized state data
	 * @see #persistState()
	 * @see GuiPersistence#load(Component, String, String)
	 */
	public void restoreState(final String data)
	{
		GuiPersistence.load(this,getClass().getName(),data);
	}
}
