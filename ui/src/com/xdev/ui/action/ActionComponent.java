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

package com.xdev.ui.action;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;


/**
 * An ActionComponent is a {@link Component} which can be connected to an
 * {@link Action}.
 *
 * @see Action
 * @see Component
 * 		
 * @author XDEV Software
 * 		
 */
public interface ActionComponent
{
	/**
	 * Returns the action which this component is connected to.
	 *
	 * @return this component's action
	 */
	public Action getAction();


	/**
	 * Connects this component with an action. Properties like text, icon and
	 * the enabled state will be used from the action.
	 * <p>
	 * The main action of this component, like click of a button, will trigger
	 * the actions {@link Action#actionPerformed(com.xdev.ui.event.ActionEvent)}
	 * method.
	 *
	 * @param action
	 *            the connected action
	 */
	public void setAction(Action action);


	/**
	 * Sets the caption of the component.
	 *
	 * @param caption
	 *            the new caption for the component. If the caption is
	 *            {@code null}, no caption is shown and it does not normally
	 *            take any space
	 * 			
	 * @see Component#setCaption(String)
	 */
	public void setCaption(String caption);


	/**
	 * Sets the icon of the component.
	 *
	 * @param icon
	 *            the icon of the component. If null, no icon is shown and it
	 *            does not normally take any space
	 * 			
	 * @see Component#setIcon(Resource)
	 */
	public void setIcon(Resource icon);


	/**
	 * Sets the component's description.
	 *
	 * @param description
	 *            the new description string for the component
	 * 			
	 * @see AbstractComponent#setDescription(String)
	 */
	public void setDescription(String description);


	/**
	 * Enables or disables the component. The user can not interact disabled
	 * components, which are shown with a style that indicates the status,
	 * usually shaded in light gray color. Components are enabled by default.
	 *
	 * @param enabled
	 *            a boolean value specifying if the component should be enabled
	 *            or not
	 * 			
	 * @see Component#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled);



	public static class Util
	{
		public static void setComponentPropertiesFromAction(final ActionComponent component,
				final Action action)
		{
			component.setCaption(action.getCaption());
			component.setIcon(action.getIcon());
			component.setDescription(action.getDescription());
			component.setEnabled(action.isEnabled());
		}
	}



	public static class ActionPropertyChangeListener implements PropertyChangeListener
	{
		private final ActionComponent	component;
		private final Action			action;


		public ActionPropertyChangeListener(final ActionComponent component, final Action action)
		{
			this.component = component;
			this.action = action;
		}


		@Override
		public void propertyChange(final PropertyChangeEvent event)
		{
			switch(event.getPropertyName())
			{
				case Action.CAPTION_PROPERTY:
					component.setCaption(action.getCaption());
				break;

				case Action.ICON_PROPERTY:
					component.setIcon(action.getIcon());
				break;

				case Action.DESCRIPTION_PROPERTY:
					component.setDescription(action.getDescription());
				break;

				case Action.ENABLED_PROPERTY:
					component.setEnabled(action.isEnabled());
				break;
			}
		}
	}
}
