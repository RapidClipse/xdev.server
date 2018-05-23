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

package com.xdev.ui.navigation;


import com.vaadin.navigator.NavigationStateManager;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;


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
 *
 * @author XDEV Software (JW)
 */
public class XdevNavigator extends Navigator
{
	/**
	 * Creates a navigator that is tracking the active view using URI fragments of
	 * the {@link Page} containing the given UI and replacing the contents of a
	 * {@link ComponentContainer} with the active view.
	 * <p>
	 * All components of the container are removed each time before adding the
	 * active {@link View}. Views must implement {@link Component} when using this
	 * constructor.
	 * <p>
	 * Navigation is automatically initiated after {@code UI.init()}Â if a navigator
	 * was created. If at a later point changes are made to the navigator,
	 * {@code navigator.navigateTo(navigator.getState())} may need to be explicitly
	 * called to ensure the current view matches the navigation state.
	 *
	 * @param ui
	 *            The UI to which this Navigator is attached.
	 * @param container
	 *            The ComponentContainer whose contents should be replaced with the
	 *            active view on view change
	 */
	public XdevNavigator(final UI ui, final ComponentContainer container)
	{
		super(ui,container);
	}


	/**
	 * Creates a navigator.
	 * <p>
	 * When a custom navigation state manager is not needed, use one of the other
	 * constructors which use a URI fragment based state manager.
	 * <p>
	 * Navigation is automatically initiated after {@code UI.init()}Â if a navigator
	 * was created. If at a later point changes are made to the navigator,
	 * {@code navigator.navigateTo(navigator.getState())} may need to be explicitly
	 * called to ensure the current view matches the navigation state.
	 *
	 * @param ui
	 *            The UI to which this Navigator is attached.
	 * @param stateManager
	 *            The NavigationStateManager keeping track of the active view and
	 *            enabling bookmarking and direct navigation
	 * @param display
	 *            The ViewDisplay used to display the views handled by this
	 *            navigator
	 */
	public XdevNavigator(final UI ui, final NavigationStateManager stateManager,
			final ViewDisplay display)
	{
		super(ui,stateManager,display);
	}


	/**
	 * Creates a navigator that is tracking the active view using URI fragments of
	 * the {@link Page} containing the given UI and replacing the contents of a
	 * {@link SingleComponentContainer} with the active view.
	 * <p>
	 * Views must implement {@link Component} when using this constructor.
	 * <p>
	 * Navigation is automatically initiated after {@code UI.init()}Â if a navigator
	 * was created. If at a later point changes are made to the navigator,
	 * {@code navigator.navigateTo(navigator.getState())} may need to be explicitly
	 * called to ensure the current view matches the navigation state.
	 *
	 * @param ui
	 *            The UI to which this Navigator is attached.
	 * @param container
	 *            The SingleComponentContainer whose contents should be replaced
	 *            with the active view on view change
	 */
	public XdevNavigator(final UI ui, final SingleComponentContainer container)
	{
		super(ui,container);
	}


	/**
	 * Creates a navigator that is tracking the active view using URI fragments of
	 * the {@link Page} containing the given UI.
	 * <p>
	 * Navigation is automatically initiated after {@code UI.init()}Â if a navigator
	 * was created. If at a later point changes are made to the navigator,
	 * {@code navigator.navigateTo(navigator.getState())} may need to be explicitly
	 * called to ensure the current view matches the navigation state.
	 *
	 * @param ui
	 *            The UI to which this Navigator is attached.
	 * @param display
	 *            The ViewDisplay used to display the views.
	 */
	public XdevNavigator(final UI ui, final ViewDisplay display)
	{
		super(ui,display);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void navigateTo(final String navigationState)
	{
		/*
		 * UI#doInit initialized the navigator with its default state. If no view is set
		 * the state is null and an IllegalArgumentException is thrown.
		 */
		if(navigationState != null)
		{
			super.navigateTo(navigationState);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void init(final UI ui, NavigationStateManager stateManager, final ViewDisplay display)
	{
		if(stateManager != null)
		{
			stateManager = new XdevNavigationStateManager(stateManager);
		}
		super.init(ui,stateManager,display);
	}



	private static class XdevNavigationStateManager implements NavigationStateManager
	{
		private final NavigationStateManager delegate;


		public XdevNavigationStateManager(final NavigationStateManager delegate)
		{
			this.delegate = delegate;
		}


		@Override
		public String getState()
		{
			/*
			 * Workaround for Vaadin Bug (NPE), see XWS-1021
			 */
			try
			{
				return this.delegate.getState();
			}
			catch(final NullPointerException e)
			{
				return "";
			}
		}


		@Override
		public void setState(final String state)
		{
			this.delegate.setState(state);
		}


		@Override
		public void setNavigator(final Navigator navigator)
		{
			this.delegate.setNavigator(navigator);
		}
	}
}
