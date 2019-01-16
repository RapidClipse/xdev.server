/*
 * Copyright (C) 2013-2019 by XDEV Software, All Rights Reserved.
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


import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutAction.ModifierKey;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
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
	 * @param caption
	 *            the caption to set
	 * @since 3.0.2
	 */
	public PopupWindow caption(String caption);


	/**
	 * @param icon
	 *            the icon to set
	 * @since 3.0.2
	 */
	public PopupWindow icon(Resource icon);


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
	 * Sets the width of the popup window. Negative number implies unspecified
	 * size (terminal is free to set the size).
	 *
	 * @since 1.2
	 */
	public PopupWindow width(final float width, final Unit unit);


	/**
	 * Sets the height of the popup window. Negative number implies unspecified
	 * size (terminal is free to set the size).
	 *
	 * @since 1.2
	 */
	public PopupWindow height(final float height, final Unit unit);


	/**
	 * Sets the size of the popup window in pixels.
	 *
	 * @since 1.2
	 */
	public default PopupWindow size(final float width, final float height)
	{
		return size(width,height,Unit.PIXELS);
	}


	/**
	 * Sets the size of the popup window. Negative number implies unspecified
	 * size (terminal is free to set the size).
	 *
	 * @since 1.2
	 */
	public PopupWindow size(final float width, final float height, final Unit unit);


	/**
	 * Adds a close shortcut that reacts to the given {@link KeyCode} and
	 * (optionally) {@link ModifierKey}s.
	 *
	 * @param keyCode
	 *            the keycode for invoking the shortcut.
	 * @param modifiers
	 *            the (optional) modifiers for invoking the shortcut. Can be set
	 *            to <code>null</code> to be explicit about not having
	 *            modifiers.
	 * @see KeyCode
	 * @see ModifierKey
	 * @since 3.0.2
	 */
	public PopupWindow closeShortcut(int keyCode, int... modifiers);


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
		private String						caption;
		private Resource					icon;
		private boolean						modal			= false;
		private boolean						resizable		= false;
		private boolean						closable		= true;
		private boolean						draggable		= true;
		private boolean						maximized		= false;
		private Point						location		= null;
		private Float						width;
		private Unit						widthUnit;
		private Float						height;
		private Unit						heightUnit;
		private final List<ShortcutAction>	closeShortcuts	= new ArrayList<>();
		private Consumer<Window.CloseEvent>	closeHandler;


		public Implementation(final Component content)
		{
			this.content = content;
		}


		@Override
		public PopupWindow caption(final String caption)
		{
			this.caption = caption;
			return this;
		}


		@Override
		public PopupWindow icon(final Resource icon)
		{
			this.icon = icon;
			return this;
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
		public PopupWindow width(final float width, final Unit unit)
		{
			this.width = width;
			this.widthUnit = unit;
			return this;
		}


		@Override
		public PopupWindow height(final float height, final Unit unit)
		{
			this.height = height;
			this.heightUnit = unit;
			return this;
		}


		@Override
		public PopupWindow size(final float width, final float height, final Unit unit)
		{
			this.width = width;
			this.height = height;
			this.widthUnit = unit;
			this.heightUnit = unit;
			return this;
		}


		@Override
		public PopupWindow closeShortcut(final int keyCode, final int... modifiers)
		{
			this.closeShortcuts.add(new ShortcutAction("",keyCode,modifiers));
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

			float width;
			Unit widthUnit;
			if(this.width != null && this.widthUnit != null)
			{
				width = this.width;
				widthUnit = this.widthUnit;
				content.setWidth(100,Unit.PERCENTAGE);
			}
			else if(this.content.getWidth() > 0f && this.content.getWidthUnits() == Unit.PIXELS)
			{
				width = this.resizable ? this.content.getWidth() : -1;
				widthUnit = Unit.PIXELS;
			}
			else
			{
				width = -1;
				widthUnit = Unit.PIXELS;
				content.setWidthUndefined();
			}

			float height;
			Unit heightUnit;
			if(this.height != null && this.heightUnit != null)
			{
				height = this.height;
				heightUnit = this.heightUnit;
				content.setHeight(100,Unit.PERCENTAGE);
			}
			else if(this.content.getHeight() > 0f && this.content.getHeightUnits() == Unit.PIXELS)
			{
				height = this.resizable ? this.content.getHeight() : -1;
				heightUnit = Unit.PIXELS;
			}
			else
			{
				height = -1;
				heightUnit = Unit.PIXELS;
				content.setHeightUndefined();
			}

			window.setWidth(width,widthUnit);
			window.setHeight(height,heightUnit);

			window.setContent(this.content);

			String caption = this.caption;
			if(caption == null)
			{
				caption = this.content.getCaption();
			}
			window.setCaption(caption);

			Resource icon = this.icon;
			if(icon == null)
			{
				icon = this.content.getIcon();
			}
			window.setIcon(icon);

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
			for(final ShortcutAction shortcut : this.closeShortcuts)
			{
				window.addCloseShortcut(shortcut.getKeyCode(),shortcut.getModifiers());
			}
			if(this.closeHandler != null)
			{
				window.addCloseListener(event -> closeHandler.accept(event));
			}
			UI.getCurrent().addWindow(window);

			if(this.resizable)
			{
				content.setSizeFull();
			}

			return window;
		}
	}
}
