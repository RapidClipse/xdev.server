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

package com.xdev.security.authentication.ui;


import com.vaadin.navigator.NavigationStateManager;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;
import com.xdev.ui.navigation.XdevNavigator;


/**
 * A navigator utility that allows switching of views in a part of an
 * application.
 * <p>
 * The view switching can be based e.g. on URI fragments containing the view
 * name and parameters to the view. There are two types of parameters for views:
 * an optional parameter string that is included in the fragment (may be
 * bookmarkable).
 * <p>
 * Views can be explicitly registered or dynamically generated and listening to
 * view changes is possible.
 * <p>
 * Note that {@link XdevNavigator} is not a component itself but uses a
 * {@link ViewDisplay} to update contents based on the state.
 */
public class XdevAuthenticationNavigator extends XdevNavigator
{
	private String	loginViewName		= "";
	private String	redirectViewName	= null;
	
	
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
	public XdevAuthenticationNavigator(final UI ui, final ComponentContainer container)
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
	public XdevAuthenticationNavigator(final UI ui, final NavigationStateManager stateManager,
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
	public XdevAuthenticationNavigator(final UI ui, final SingleComponentContainer container)
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
	public XdevAuthenticationNavigator(final UI ui, final ViewDisplay display)
	{
		super(ui,display);
	}
	
	
	{
		this.initAuthenticationListener();
	}
	
	
	/**
	 * @return the loginViewName
	 */
	public String getLoginViewName()
	{
		return this.loginViewName;
	}
	
	
	/**
	 * @param loginViewName
	 *            the loginViewName to set
	 */
	public void setLoginViewName(final String loginViewName)
	{
		this.loginViewName = loginViewName;
	}
	
	
	/**
	 * @return the redirectViewName
	 */
	public String getRedirectViewName()
	{
		return this.redirectViewName;
	}
	
	
	/**
	 * @param redirectViewName
	 *            the redirectViewName to set
	 */
	public void setRedirectViewName(final String redirectViewName)
	{
		this.redirectViewName = redirectViewName;
	}
	
	
	protected void initAuthenticationListener()
	{
		this.addViewChangeListener(new ViewChangeListener()
		{
			@Override
			public boolean beforeViewChange(final ViewChangeEvent event)
			{
				if(event.getNewView() instanceof LoginView || Authentication.getUser() != null)
				{
					return true;
				}
				
				showUnauthorizedAccessMessage();
				
				navigateToLoginView();
				
				return false;
			}
			
			
			@Override
			public void afterViewChange(final ViewChangeEvent event)
			{
			}
		});
	}
	
	
	protected void showUnauthorizedAccessMessage()
	{
		Notification.show("Permission denied",Type.ERROR_MESSAGE);
	}


	/**
	 * Navigates to the login view URL set with
	 * {@link #setLoginViewName(String)}.
	 *
	 * @throws IllegalStateException
	 *             if no login view is set
	 */
	public void navigateToLoginView()
	{
		final String loginViewName = getLoginViewName();
		if(loginViewName == null)
		{
			throw new IllegalStateException("No login view set");
		}
		
		navigateTo(loginViewName);
	}
	
	
	/**
	 * Navigates to the redirect view URL set with
	 * {@link #setRedirectViewName(String)}.
	 *
	 * @throws IllegalStateException
	 *             if no redirect view is set
	 */
	public void navigateToRedirectView()
	{
		final String redirectViewName = getRedirectViewName();
		if(redirectViewName == null)
		{
			throw new IllegalStateException("No redirect view set");
		}
		
		navigateTo(redirectViewName);
	}
}
