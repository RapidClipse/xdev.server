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


import java.beans.PropertyChangeListener;
import java.util.Objects;

import com.vaadin.ui.NativeButton;
import com.xdev.ui.action.Action;
import com.xdev.ui.action.ActionComponent;
import com.xdev.ui.event.ActionEvent;


/**
 * System button component.
 *
 * @author XDEV Software
 *		
 */
public class XdevNativeButton extends NativeButton implements XdevComponent, ActionComponent
{
	private final Extensions		extensions	= new Extensions();
	private Action					action;
	private PropertyChangeListener	actionPropertyChangeListener;
	private ClickListener			actionClickListener;
									
									
	/**
	 *
	 */
	public XdevNativeButton()
	{
		super();
	}


	/**
	 * @param caption
	 * @param listener
	 */
	public XdevNativeButton(final String caption, final ClickListener listener)
	{
		super(caption,listener);
	}


	/**
	 * @param caption
	 */
	public XdevNativeButton(final String caption)
	{
		super(caption);
	}


	public XdevNativeButton(final Action action)
	{
		setAction(action);
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
	 * {@inheritDoc}
	 */
	@Override
	public void setAction(final Action action)
	{
		if(!Objects.equals(action,this.action))
		{
			if(this.action != null)
			{
				this.action.removePropertyChangeListener(this.actionPropertyChangeListener);
				this.actionPropertyChangeListener = null;
				removeClickListener(this.actionClickListener);
				this.actionClickListener = null;
			}

			this.action = action;

			if(action != null)
			{
				Utils.setComponentPropertiesFromAction(this,action);
				this.actionPropertyChangeListener = new ActionPropertyChangeListener(this,action);
				action.addPropertyChangeListener(this.actionPropertyChangeListener);
				this.actionClickListener = event -> action
						.actionPerformed(new ActionEvent(XdevNativeButton.this));
				addClickListener(this.actionClickListener);
			}
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Action getAction()
	{
		return this.action;
	}
}
