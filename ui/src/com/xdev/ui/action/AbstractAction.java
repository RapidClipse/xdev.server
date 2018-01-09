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

package com.xdev.ui.action;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.UI;
import com.xdev.ui.XdevUI;
import com.xdev.ui.event.ActionEvent;


/**
 * This class provides default implementations for the {@link Action} interface.
 * <p>
 * Standard behaviors like the get and set methods for {@link Action} object
 * properties (icon, caption, enabled, ...) are defined here. The developer need
 * only subclass this abstract class and define the
 * {@link #actionPerformed(ActionEvent)} method.
 *
 * @author XDEV Software
 *
 */
public abstract class AbstractAction implements Action
{
	/**
	 * Bridge to Vaadin's {@link ShortcutAction}.
	 */
	private class ActionShortcut extends ShortcutListener
	{
		final Shortcut shortcut;


		ActionShortcut(final Shortcut shortcut)
		{
			super("",shortcut.getKeyCode(),shortcut.getModifiers());
			this.shortcut = shortcut;
		}


		@Override
		public String getCaption()
		{
			return AbstractAction.this.getCaption();
		}


		@Override
		public void handleAction(final Object sender, final Object target)
		{
			if(AbstractAction.this.isEnabled())
			{
				AbstractAction.this
						.actionPerformed(new ActionEvent(new ShortcutSource(sender,target)));
			}
		}
	}

	private XdevUI					ui;
	private String					caption			= "";
	private Resource				icon			= null;
	private String					description		= null;
	private ActionShortcut			actionShortcut	= null;
	private boolean					enabled			= true;
	private PropertyChangeSupport	propertyChangeSupport;


	/**
	 * Creates an {@code Action}.
	 */
	public AbstractAction()
	{
	}


	/**
	 * Creates an {@code Action} with the specified caption.
	 */
	public AbstractAction(final String caption)
	{
		this.caption = caption;
	}


	/**
	 * Creates an {@code Action} with the specified icon.
	 */
	public AbstractAction(final Resource icon)
	{
		this.icon = icon;
	}


	/**
	 * Creates an {@code Action} with the specified caption and icon.
	 */
	public AbstractAction(final String caption, final Resource icon)
	{
		this.caption = caption;
		this.icon = icon;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPropertyChangeListener(final PropertyChangeListener listener)
	{
		if(this.propertyChangeSupport == null)
		{
			this.propertyChangeSupport = new PropertyChangeSupport(this);
		}

		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removePropertyChangeListener(final PropertyChangeListener listener)
	{
		if(this.propertyChangeSupport != null)
		{
			this.propertyChangeSupport.removePropertyChangeListener(listener);
		}
	}


	protected void firePropertyChange(final String name, final Object oldValue,
			final Object newValue)
	{
		if(this.propertyChangeSupport != null)
		{
			this.propertyChangeSupport.firePropertyChange(name,oldValue,newValue);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public XdevUI getUI()
	{
		if(this.ui == null)
		{
			final UI ui = UI.getCurrent();
			if(ui instanceof XdevUI)
			{
				this.ui = (XdevUI)ui;
			}
		}

		return this.ui;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCaption()
	{
		return this.caption;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCaption(final String caption)
	{
		if(!Objects.equals(caption,this.caption))
		{
			final Object oldValue = this.caption;

			this.caption = caption;

			firePropertyChange(CAPTION_PROPERTY,oldValue,caption);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Resource getIcon()
	{
		return this.icon;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIcon(final Resource icon)
	{
		if(!Objects.equals(icon,this.icon))
		{
			final Object oldValue = this.icon;

			this.icon = icon;

			firePropertyChange(ICON_PROPERTY,oldValue,icon);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription()
	{
		return this.description;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDescription(final String description)
	{
		if(!Objects.equals(description,this.description))
		{
			final Object oldValue = this.description;

			this.description = description;

			firePropertyChange(DESCRIPTION_PROPERTY,oldValue,description);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Shortcut getShortcut()
	{
		return this.actionShortcut != null ? this.actionShortcut.shortcut : null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setShortcut(final Shortcut shortcut)
	{
		final Shortcut oldValue = getShortcut();
		if(!Objects.equals(oldValue,shortcut))
		{
			final UI ui = UI.getCurrent();

			if(this.actionShortcut != null)
			{
				if(ui != null)
				{
					ui.removeAction(this.actionShortcut);
				}
			}

			if(shortcut != null)
			{
				this.actionShortcut = new ActionShortcut(shortcut);
				if(ui != null)
				{
					ui.addAction(this.actionShortcut);
				}
			}
			else
			{
				this.actionShortcut = null;
			}

			firePropertyChange(SHORTCUT_PROPERTY,oldValue,shortcut);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled()
	{
		return this.enabled;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEnabled(final boolean enabled)
	{
		if(this.enabled != enabled)
		{
			final Object oldValue = this.enabled;

			this.enabled = enabled;

			firePropertyChange(ENABLED_PROPERTY,oldValue,enabled);
		}
	}

	// @Override
	// public boolean equals(final Object obj)
	// {
	// if(obj == this)
	// {
	// return true;
	// }
	//
	// return obj instanceof AbstractAction &&
	// ((AbstractAction)obj).id.equals(this.id);
	// }
	//
	//
	// @Override
	// public int hashCode()
	// {
	// return this.id.hashCode();
	// }
}
