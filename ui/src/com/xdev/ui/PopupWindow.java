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


import java.awt.Point;
import java.util.function.Consumer;

import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;


/**
 * Helper method to show a popup window.
 *
 * <pre>
 * ...
 * 
 * PopupWindow.For(myComponent).modal(true).onClose(this::popupClosed).show();
 * 
 * ...
 * 
 * 
 * private void popupClosed(CloseEvent event)
 * {
 * 
 * }
 * </pre>
 */
public interface PopupWindow
{
	public static PopupWindow For(final Component content)
	{
		return new Implementation(content);
	}
	
	
	/**
	 * @param modal
	 *            the modal to set
	 */
	public PopupWindow modal(final boolean modal);
	
	
	/**
	 * @param resizable
	 *            the resizable to set
	 */
	public PopupWindow resizable(final boolean resizable);
	
	
	/**
	 * @param closable
	 *            the closable to set
	 */
	public PopupWindow closable(final boolean closable);
	
	
	/**
	 * @param draggable
	 *            the draggable to set
	 */
	public PopupWindow draggable(final boolean draggable);
	
	
	/**
	 * @param maximized
	 *            the maximized to set
	 */
	public PopupWindow maximized(final boolean maximized);
	
	
	/**
	 * Sets the predefined location for the popup window, default is centered.
	 */
	public PopupWindow location(final int x, final int y);
	
	
	/**
	 * Sets the close handler
	 */
	public PopupWindow onClose(Consumer<Window.CloseEvent> closeHandler);
	
	
	/**
	 * Opens and returns the window
	 *
	 * @return the opened window
	 */
	public Window show();
	
	
	
	public static class Implementation implements PopupWindow
	{
		private final Component				content;
		private boolean						modal		= false;
		private boolean						resizable	= true;
		private boolean						closable	= true;
		private boolean						draggable	= true;
		private boolean						maximized	= false;
		private Point						location	= null;
		private Consumer<Window.CloseEvent>	closeHandler;
		
		
		public Implementation(final Component content)
		{
			this.content = content;
		}
		
		
		@Override
		public PopupWindow modal(final boolean modal)
		{
			this.modal = modal;
			return this;
		}
		
		
		@Override
		public PopupWindow resizable(final boolean resizable)
		{
			this.resizable = resizable;
			return this;
		}
		
		
		@Override
		public PopupWindow closable(final boolean closable)
		{
			this.closable = closable;
			return this;
		}
		
		
		@Override
		public PopupWindow draggable(final boolean draggable)
		{
			this.draggable = draggable;
			return this;
		}
		
		
		@Override
		public PopupWindow maximized(final boolean maximized)
		{
			this.maximized = maximized;
			return this;
		}
		
		
		@Override
		public PopupWindow location(final int x, final int y)
		{
			this.location = new Point(x,y);
			return this;
		}
		
		
		@Override
		public PopupWindow onClose(final Consumer<CloseEvent> closeHandler)
		{
			this.closeHandler = closeHandler;
			return this;
		}
		
		
		@Override
		public Window show()
		{
			final Window window = new Window();
			window.setWidth(this.content.getWidth() + 20,this.content.getWidthUnits());
			window.setHeight(this.content.getHeight() + 70,this.content.getWidthUnits());
			this.content.setSizeFull();
			window.setContent(this.content);
			window.setCaption(this.content.getCaption());
			window.setIcon(this.content.getIcon());
			window.setModal(this.modal);
			window.setResizable(this.resizable);
			window.setClosable(this.closable);
			window.setDraggable(this.draggable);
			if(this.maximized)
			{
				window.setWindowMode(WindowMode.MAXIMIZED);
			}
			else if(this.location != null)
			{
				window.setPositionX(this.location.x);
				window.setPositionY(this.location.y);
			}
			else
			{
				window.center();
			}
			if(this.closeHandler != null)
			{
				window.addCloseListener(event -> closeHandler.accept(event));
			}
			UI.getCurrent().addWindow(window);
			
			return window;
		}
	}
}
