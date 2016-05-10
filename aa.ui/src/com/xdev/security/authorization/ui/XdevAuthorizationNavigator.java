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

package com.xdev.security.authorization.ui;


import java.util.HashMap;
import java.util.Map;

import com.vaadin.navigator.NavigationStateManager;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;
import com.xdev.security.authentication.ui.Authentication;
import com.xdev.security.authentication.ui.XdevAuthenticationNavigator;
import com.xdev.security.authorization.Resource;
import com.xdev.security.authorization.Subject;


/**
 * @author XDEV Software
 * @since 1.3
 */
public class XdevAuthorizationNavigator extends XdevAuthenticationNavigator
{
	private String							permissionDeniedViewName	= null;
	private final Map<String, Resource[]>	viewResources				= new HashMap<>();
	
	
	/**
	 * Creates a navigator that is tracking the active view using URI fragments
	 * of the {@link Page} containing the given UI and replacing the contents of
	 * a {@link ComponentContainer} with the active view.
	 * <p>
	 * All components of the container are removed each time before adding the
	 * active {@link View}. Views must implement {@link Component} when using
	 * this constructor.
	 * <p>
	 * Navigation is automatically initiated after {@code UI.init()}Â if a
	 * navigator was created. If at a later point changes are made to the
	 * navigator, {@code navigator.navigateTo(navigator.getState())} may need to
	 * be explicitly called to ensure the current view matches the navigation
	 * state.
	 *
	 * @param ui
	 *            The UI to which this Navigator is attached.
	 * @param container
	 *            The ComponentContainer whose contents should be replaced with
	 *            the active view on view change
	 */
	public XdevAuthorizationNavigator(final UI ui, final ComponentContainer container)
	{
		super(ui,container);
	}
	
	
	/**
	 * Creates a navigator.
	 * <p>
	 * When a custom navigation state manager is not needed, use one of the
	 * other constructors which use a URI fragment based state manager.
	 * <p>
	 * Navigation is automatically initiated after {@code UI.init()}Â if a
	 * navigator was created. If at a later point changes are made to the
	 * navigator, {@code navigator.navigateTo(navigator.getState())} may need to
	 * be explicitly called to ensure the current view matches the navigation
	 * state.
	 *
	 * @param ui
	 *            The UI to which this Navigator is attached.
	 * @param stateManager
	 *            The NavigationStateManager keeping track of the active view
	 *            and enabling bookmarking and direct navigation
	 * @param display
	 *            The ViewDisplay used to display the views handled by this
	 *            navigator
	 */
	public XdevAuthorizationNavigator(final UI ui, final NavigationStateManager stateManager,
			final ViewDisplay display)
	{
		super(ui,stateManager,display);
	}
	
	
	/**
	 * Creates a navigator that is tracking the active view using URI fragments
	 * of the {@link Page} containing the given UI and replacing the contents of
	 * a {@link SingleComponentContainer} with the active view.
	 * <p>
	 * Views must implement {@link Component} when using this constructor.
	 * <p>
	 * Navigation is automatically initiated after {@code UI.init()}Â if a
	 * navigator was created. If at a later point changes are made to the
	 * navigator, {@code navigator.navigateTo(navigator.getState())} may need to
	 * be explicitly called to ensure the current view matches the navigation
	 * state.
	 *
	 * @param ui
	 *            The UI to which this Navigator is attached.
	 * @param container
	 *            The SingleComponentContainer whose contents should be replaced
	 *            with the active view on view change
	 */
	public XdevAuthorizationNavigator(final UI ui, final SingleComponentContainer container)
	{
		super(ui,container);
	}
	
	
	/**
	 * Creates a navigator that is tracking the active view using URI fragments
	 * of the {@link Page} containing the given UI.
	 * <p>
	 * Navigation is automatically initiated after {@code UI.init()}Â if a
	 * navigator was created. If at a later point changes are made to the
	 * navigator, {@code navigator.navigateTo(navigator.getState())} may need to
	 * be explicitly called to ensure the current view matches the navigation
	 * state.
	 *
	 * @param ui
	 *            The UI to which this Navigator is attached.
	 * @param display
	 *            The ViewDisplay used to display the views.
	 */
	public XdevAuthorizationNavigator(final UI ui, final ViewDisplay display)
	{
		super(ui,display);
	}
	
	
	/**
	 * @return the permissionDeniedViewName
	 */
	public String getPermissionDeniedViewName()
	{
		return this.permissionDeniedViewName;
	}
	
	
	/**
	 * @param permissionDeniedViewName
	 *            the permissionDeniedViewName to set
	 */
	public void setPermissionDeniedViewName(final String permissionDeniedViewName)
	{
		this.permissionDeniedViewName = permissionDeniedViewName;
	}
	
	
	public void setResources(final String viewName, final Resource... resources)
	{
		this.viewResources.put(viewName,resources);
	}
	
	
	public Resource[] getResources(final String viewName)
	{
		return this.viewResources.get(viewName);
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.xdev.ui.navigation.XdevNavigator#navigateTo(java.lang.String)
	 */
	@Override
	public void navigateTo(final String navigationState)
	{
		if(navigationState != null)
		{
			if(canNavigateTo(navigationState))
			{
				super.navigateTo(navigationState);
			}
			else if(this.permissionDeniedViewName != null)
			{
				navigateTo(this.permissionDeniedViewName);
			}
		}
	}
	
	
	protected boolean canNavigateTo(final String navigationState)
	{
		final Resource[] resources = getRequiredResources(navigationState);
		if(resources == null || resources.length == 0)
		{
			return true;
		}
		
		final Subject user = Authentication.getUser();
		if(user == null)
		{
			return false;
		}
		
		for(final Resource resource : resources)
		{
			if(!user.hasPermission(resource))
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	protected Resource[] getRequiredResources(final String navigationState)
	{
		for(final String viewName : this.viewResources.keySet())
		{
			if(navigationState.equals(viewName) || navigationState.startsWith(viewName + "/"))
			{
				return this.viewResources.get(viewName);
			}
		}
		
		return null;
	}
	
	
	/**
	 * Navigates to the permission denied view URL set with
	 * {@link #setPermissionDeniedViewName(String)}.
	 *
	 * @throws IllegalStateException
	 *             if no permission denied view is set
	 */
	public void navigateToPermissionDeniedView()
	{
		final String permissionDeniedViewName = getPermissionDeniedViewName();
		if(permissionDeniedViewName == null)
		{
			throw new IllegalStateException("No permission denied view set");
		}
		
		navigateTo(permissionDeniedViewName);
	}
}
