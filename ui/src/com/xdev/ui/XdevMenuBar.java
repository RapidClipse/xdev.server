/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xdev.ui;


import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;
import com.xdev.ui.action.Action;
import com.xdev.ui.action.ActionComponent;
import com.xdev.ui.event.ActionEvent;


/**
 * <p>
 * A class representing a horizontal menu bar. The menu can contain MenuItem
 * objects, which in turn can contain more MenuItems. These sub-level MenuItems
 * are represented as vertical menu.
 * </p>
 *
 * @author XDEV Software
 *
 */
public class XdevMenuBar extends MenuBar
{
	/**
	 * Constructs an empty, horizontal menu
	 */
	public XdevMenuBar()
	{
		super();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public XdevMenuItem addItem(final String caption, final Command command)
	{
		return addItem(caption,null,command);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public XdevMenuItem addItem(final String caption, final Resource icon, final Command command)
	{
		if(caption == null)
		{
			throw new IllegalArgumentException("caption cannot be null");
		}
		final XdevMenuItem newItem = new XdevMenuItem(caption,icon,command);
		getItems().add(newItem);
		markAsDirty();
		
		return newItem;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public XdevMenuItem addItemBefore(final String caption, final Resource icon,
			final Command command, final MenuItem itemToAddBefore)
	{
		if(caption == null)
		{
			throw new IllegalArgumentException("caption cannot be null");
		}
		
		final XdevMenuItem newItem = new XdevMenuItem(caption,icon,command);
		final List<MenuItem> menuItems = getItems();
		if(menuItems.contains(itemToAddBefore))
		{
			final int index = menuItems.indexOf(itemToAddBefore);
			menuItems.add(index,newItem);
			
		}
		else
		{
			menuItems.add(newItem);
		}
		
		markAsDirty();
		
		return newItem;
	}
	
	
	public XdevMenuItem addItem(final Action action)
	{
		final XdevMenuItem newItem = new XdevMenuItem(action);
		getItems().add(newItem);
		markAsDirty();
		
		return newItem;
	}
	
	
	public XdevMenuItem addItemBefore(final Action action, final MenuItem itemToAddBefore)
	{
		final XdevMenuItem newItem = new XdevMenuItem(action);
		final List<MenuItem> menuItems = getItems();
		if(menuItems.contains(itemToAddBefore))
		{
			final int index = menuItems.indexOf(itemToAddBefore);
			menuItems.add(index,newItem);
		}
		else
		{
			menuItems.add(newItem);
		}
		
		markAsDirty();
		
		return newItem;
	}
	
	
	
	/**
	 * A composite class for menu items and sub-menus.
	 * <p>
	 * This is an {@link ActionComponent} which can be connected with an
	 * {@link Action}.
	 *
	 * @see #setAction(Action)
	 * @see Action
	 *		
	 * @author XDEV Software
	 *
	 */
	public class XdevMenuItem extends MenuItem implements ActionComponent
	{
		private Action					action;
		private PropertyChangeListener	actionPropertyChangeListener;
		private Command					actionCommand;
		
		
		public XdevMenuItem(final Action action)
		{
			super("",null,null);
			
			setAction(action);
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
					setCommand(null);
					this.actionCommand = null;
				}
				
				this.action = action;
				
				if(action != null)
				{
					Utils.setComponentPropertiesFromAction(this,action);
					this.actionPropertyChangeListener = new ActionPropertyChangeListener(this,
							action);
					action.addPropertyChangeListener(this.actionPropertyChangeListener);
					this.actionCommand = selectedItem -> action
							.actionPerformed(new ActionEvent(XdevMenuItem.this));
					setCommand(this.actionCommand);
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
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setCaption(final String caption)
		{
			setText(caption);
		}
		
		
		public XdevMenuItem(final String caption, final Resource icon, final Command command)
		{
			super(caption,icon,command);
		}
		
		
		public XdevMenuItem addItem(final Action action)
		{
			if(isSeparator())
			{
				throw new UnsupportedOperationException("Cannot add items to a separator");
			}
			if(isCheckable())
			{
				throw new IllegalStateException("A checkable item cannot have children");
			}
			
			final XdevMenuItem newItem = new XdevMenuItem(action);
			
			// The only place where the parent is set
			newItem.setParent(this);
			_children().add(newItem);
			
			markAsDirty();
			
			return newItem;
		}
		
		
		public XdevMenuItem addItemBefore(final Action action, final MenuItem itemToAddBefore)
				throws IllegalStateException
		{
			if(isCheckable())
			{
				throw new IllegalStateException("A checkable item cannot have children");
			}
			
			XdevMenuItem newItem = null;
			
			final List<MenuItem> itsChildren = _children();
			if(hasChildren() && itsChildren.contains(itemToAddBefore))
			{
				final int index = itsChildren.indexOf(itemToAddBefore);
				newItem = new XdevMenuItem(action);
				newItem.setParent(this);
				itsChildren.add(index,newItem);
			}
			else
			{
				newItem = addItem(action);
			}
			
			markAsDirty();
			
			return newItem;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public XdevMenuItem addItem(final String caption, final Command command)
		{
			return addItem(caption,null,command);
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public XdevMenuItem addItem(final String caption, final Resource icon,
				final Command command) throws IllegalStateException
		{
			if(isSeparator())
			{
				throw new UnsupportedOperationException("Cannot add items to a separator");
			}
			if(isCheckable())
			{
				throw new IllegalStateException("A checkable item cannot have children");
			}
			if(caption == null)
			{
				throw new IllegalArgumentException("Caption cannot be null");
			}
			
			final XdevMenuItem newItem = new XdevMenuItem(caption,icon,command);
			newItem.setParent(this);
			_children().add(newItem);
			
			markAsDirty();
			
			return newItem;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public XdevMenuItem addItemBefore(final String caption, final Resource icon,
				final Command command, final MenuItem itemToAddBefore) throws IllegalStateException
		{
			if(isCheckable())
			{
				throw new IllegalStateException("A checkable item cannot have children");
			}
			XdevMenuItem newItem = null;
			
			final List<MenuItem> children = _children();
			if(hasChildren() && children.contains(itemToAddBefore))
			{
				final int index = children.indexOf(itemToAddBefore);
				newItem = new XdevMenuItem(caption,icon,command);
				newItem.setParent(this);
				children.add(index,newItem);
			}
			else
			{
				newItem = addItem(caption,icon,command);
			}
			
			markAsDirty();
			
			return newItem;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public XdevMenuItem addSeparator()
		{
			return (XdevMenuItem)super.addSeparator();
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public XdevMenuItem addSeparatorBefore(final MenuItem itemToAddBefore)
		{
			return (XdevMenuItem)super.addSeparatorBefore(itemToAddBefore);
		}
		
		
		private List<MenuItem> _children()
		{
			final List<MenuItem> children = getChildren();
			if(children != null)
			{
				return children;
				
			}
			try
			{
				final Field field = MenuItem.class.getDeclaredField("itsChildren");
				field.setAccessible(true);
				@SuppressWarnings("unchecked")
				List<MenuItem> list = (List<MenuItem>)field.get(this);
				if(list == null)
				{
					list = new ArrayList<>();
					field.set(this,list);
				}
				return list;
			}
			catch(final Exception e)
			{
				// shouldn't happen
				e.printStackTrace();
			}
			
			return new ArrayList<>();
		}
	}
}
