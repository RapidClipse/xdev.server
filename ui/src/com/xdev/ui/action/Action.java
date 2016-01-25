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

package com.xdev.ui.action;


import java.beans.PropertyChangeListener;

import com.vaadin.server.Resource;
import com.xdev.ui.XdevButton;
import com.xdev.ui.XdevMenuBar.XdevMenuItem;
import com.xdev.ui.XdevNativeButton;
import com.xdev.ui.XdevUI;
import com.xdev.ui.event.ActionListener;


/**
 * The <code>Action</code> interface provides a useful extension to the
 * <code>ActionListener</code> interface in cases where the same functionality
 * may be accessed by several controls.
 * <p>
 * In addition to the <code>actionPerformed</code> method defined by the
 * {@link ActionListener} interface, this interface allows the application to
 * define, in a single place:
 * <ul>
 * <li>One or more text strings that describe the function. These strings can be
 * used, for example, to display the flyover text for a button or to set the
 * text in a menu item.
 * <li>One or more icons that depict the function. These icons can be used for
 * the images in a menu control, or for composite entries in a more
 * sophisticated user interface.
 * <li>The enabled/disabled state of the functionality. Instead of having to
 * separately disable the menu item and the toolbar button, the application can
 * disable the function that implements this interface. All components which are
 * registered as listeners for the state change then know to disable event
 * generation for that item and to modify the display accordingly.
 * </ul>
 * <p>
 * This interface can be added to an existing class or used to create an adapter
 * (typically, by subclassing {@link AbstractAction}). The <code>Action</code>
 * object can then be added to multiple <code>Action</code> -aware containers
 * and connected to <code>Action</code>-capable components. The GUI controls can
 * then be activated or deactivated all at once by invoking the
 * <code>Action</code> object's <code>setEnabled</code> method.
 * <p>
 * Actions are always bound to a {@link XdevUI} instance.
 * <p>
 * Components currently supporting Action:
 * <ul>
 * <li>{@link XdevButton}</li>
 * <li>{@link XdevNativeButton}</li>
 * <li>{@link XdevMenuItem}</li>
 * </ul>
 *
 * @see #getUI()
 * @see ActionComponent
 * @see ActionListener
 * @see AbstractAction
 * @see ContextSensitiveAction
 * 		
 * @author XDEV Software
 * 		
 */
public interface Action extends ActionListener
{
	/**
	 * Property name of the caption property.
	 *
	 * @see #setCaption(String)
	 */
	public final static String	CAPTION_PROPERTY		= "caption";
														
	/**
	 * Property name of the icon property.
	 *
	 * @see #setIcon(Resource)
	 */
	public final static String	ICON_PROPERTY			= "icon";
														
	/**
	 * Property name of the description property.
	 *
	 * @see #setDescription(String)
	 */
	public final static String	DESCRIPTION_PROPERTY	= "description";
														
	/**
	 * Property name of the enabled property.
	 *
	 * @see #setEnabled(boolean)
	 */
	public final static String	ENABLED_PROPERTY		= "enabled";
														
	/**
	 * Property name of the shortcut property.
	 *
	 * @see #setShortcut(Shortcut)
	 */
	public final static String	SHORTCUT_PROPERTY		= "shortcut";
														
														
	/**
	 * Adds a <code>PropertyChange</code> listener. Containers and attached
	 * components use these methods to register interest in this
	 * <code>Action</code> object. When its enabled state or other property
	 * changes, the registered listeners are informed of the change.
	 *
	 * @param listener
	 *            a <code>PropertyChangeListener</code> object
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener);


	/**
	 * Removes a <code>PropertyChange</code> listener.
	 *
	 * @param listener
	 *            a <code>PropertyChangeListener</code> object
	 * @see #addPropertyChangeListener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener);


	/**
	 * Returns the UI object which this action is associated with.
	 *
	 * @return
	 */
	public XdevUI getUI();


	/**
	 * Returns the caption of this action, used e.g. as text for a
	 * {@link XdevButton}.
	 *
	 * @return the caption of this action
	 */
	public String getCaption();


	/**
	 * Sets the caption of this action, used e.g. as text for a
	 * {@link XdevButton}.
	 * <p>
	 * If the value has changed, a <code>PropertyChangeEvent</code> is sent to
	 * listeners.
	 *
	 * @param caption
	 *            the new caption
	 * @see #CAPTION_PROPERTY
	 */
	public void setCaption(String caption);


	/**
	 * Returns the icon of this action.
	 *
	 * @return
	 */
	public Resource getIcon();


	/**
	 * Returns the icon of this action.
	 * <p>
	 * If the value has changed, a <code>PropertyChangeEvent</code> is sent to
	 * listeners.
	 *
	 * @param icon
	 *            the icon of this action
	 * @see #ICON_PROPERTY
	 */
	public void setIcon(Resource icon);


	/**
	 * Returns the caption of this action, used e.g. as tool tip text for a
	 * {@link XdevButton}.
	 *
	 * @return the caption of this action
	 */
	public String getDescription();


	/**
	 * Sets the description of this action, used e.g. as tool tip text for a
	 * {@link XdevButton}.
	 * <p>
	 * If the value has changed, a <code>PropertyChangeEvent</code> is sent to
	 * listeners.
	 *
	 * @param description
	 *            the new caption
	 * @see #DESCRIPTION_PROPERTY
	 */
	public void setDescription(String description);


	/**
	 * Returns the associated shortcut of this action.
	 *
	 * @return the shortcut of this action
	 */
	public Shortcut getShortcut();


	/**
	 * Sets the shortcut for this action. When the according keys are pressed in
	 * the browser window, this action's {@link #actionPerformed(ActionEvent)}
	 * method will be triggered, given that this action is enabled.
	 * <p>
	 * If the value has changed, a <code>PropertyChangeEvent</code> is sent to
	 * listeners.
	 * <p>
	 * The change of the shortcut will not reflect to already created
	 * components.
	 *
	 * @param shortcut
	 *            the new shortcut
	 * @see #SHORTCUT_PROPERTY
	 */
	public void setShortcut(Shortcut shortcut);


	/**
	 * Returns the enabled state of the <code>Action</code>. When enabled, any
	 * component associated with this object is active and able to fire this
	 * object's <code>actionPerformed</code> method.
	 *
	 * @return true if this <code>Action</code> is enabled
	 */
	public boolean isEnabled();


	/**
	 * Sets the enabled state of the <code>Action</code>. When enabled, any
	 * component associated with this object is active and able to fire this
	 * object's <code>actionPerformed</code> method.
	 * <p>
	 * If the value has changed, a <code>PropertyChangeEvent</code> is sent to
	 * listeners.
	 *
	 * @param enabled
	 *            true to enable this <code>Action</code>, false to disable it
	 * @see #ENABLED_PROPERTY
	 */
	public void setEnabled(boolean enabled);
}
