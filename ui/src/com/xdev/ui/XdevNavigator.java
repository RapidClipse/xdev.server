/*
 * Copyright (C) 2007-2014 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xdev.ui;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.Navigator.EmptyView;
import com.vaadin.navigator.Navigator.SingleComponentContainerViewDisplay;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.ClientConnector.AttachEvent;
import com.vaadin.server.ClientConnector.AttachListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.SingleComponentContainer;


/**
 * A navigator utility that allows switching of views in a part of an
 * application.
 * <p>
 * The view switching can be based e.g. on URI fragments containing the view
 * name and parameters to the view. There are two types of parameters for views:
 * an optional parameter string that is included in the fragment (may be
 * bookmarkable).
 * <p>
 * The views are displayed in the attached container, see
 * {@link #setContainer(SingleComponentContainer)}.
 *
 * @author XDEV Software
 */
public class XdevNavigator
{
	private SingleComponentContainer		container;
	private Navigator						navigator;
	private final Map<String, ViewEntry>	views	= new HashMap<>();
	private List<ViewProvider>				additionalViewProviders;
	private String							initialNavigationState;
	private ViewEntry						errorView;
	
	
	/**
	 *
	 */
	public XdevNavigator()
	{
		this.views.put("",new XdevViewEntry("",EmptyView.class,""));
	}
	
	
	/**
	 * @return the container in which the views are displayed
	 */
	public SingleComponentContainer getContainer()
	{
		return this.container;
	}
	
	
	/**
	 * @param container
	 *            the container in which the views are displayed
	 */
	public void setContainer(final SingleComponentContainer container)
	{
		this.container = container;
		
		container.addAttachListener(new AttachListener()
		{
			@Override
			public void attach(final AttachEvent event)
			{
				container.removeAttachListener(this);
				XdevNavigator.this.attach();
			}
		});
	}
	
	
	private void attach()
	{
		this.navigator = createNavigator();
		this.navigator.addProvider(new XdevViewProvider());
		
		if(this.additionalViewProviders != null)
		{
			for(final ViewProvider viewProvider : this.additionalViewProviders)
			{
				this.navigator.addProvider(viewProvider);
			}
			this.additionalViewProviders = null;
		}
		
		if(this.errorView != null)
		{
			navigator_setErrorView(this.errorView);
			this.errorView = null;
		}
		
		if(this.initialNavigationState != null)
		{
			this.navigator.navigateTo(this.initialNavigationState);
			this.initialNavigationState = null;
		}
	}
	
	
	protected Navigator createNavigator()
	{
		return new Navigator(this.container.getUI(),new SingleComponentContainerViewDisplay(
				this.container));
	}
	
	
	/**
	 * Registers a view class for a view name.
	 * <p>
	 * Registering another view with a name that is already registered
	 * overwrites the old registration of the same type.
	 * <p>
	 * A new view instance is created every time a view is requested.
	 *
	 * @param path
	 *            String that identifies a view (not null nor empty string)
	 * @param viewType
	 *            {@link View} class to instantiate when a view is requested
	 *            (not null)
	 */
	public void addView(final String path, final Class<? extends View> viewType)
	{
		addView(path,viewType,path);
	}
	
	
	/**
	 * Registers a view class for a view name.
	 * <p>
	 * Registering another view with a name that is already registered
	 * overwrites the old registration of the same type.
	 * <p>
	 * A new view instance is created every time a view is requested.
	 *
	 * @param path
	 *            String that identifies a view (not null nor empty string)
	 * @param view
	 *            {@link View} class to instantiate when a view is requested
	 *            (not null)
	 * @param optional
	 *            title title of the view
	 */
	public void addView(final String path, final Class<? extends View> viewType, final String title)
	{
		this.views.put(path,new XdevViewEntry(path,viewType,title));
	}
	
	
	/**
	 * Registers a view class for a view name.
	 * <p>
	 * Registering another view with a name that is already registered
	 * overwrites the old registration of the same type.
	 * <p>
	 * A new view instance is created every time a view is requested.
	 *
	 * @param path
	 *            String that identifies a view (not null nor empty string)
	 * @param view
	 *            {@link View} the view to display (not null)
	 */
	public <V extends Component & View> void addView(final String path, final V view)
	{
		addView(path,view,path);
	}
	
	
	/**
	 * Registers a view for a view name.
	 * <p>
	 * Registering another view with a name that is already registered
	 * overwrites the old registration of the same type.
	 * <p>
	 * A new view instance is created every time a view is requested.
	 *
	 * @param path
	 *            String that identifies a view (not null nor empty string)
	 * @param view
	 *            {@link View} the view to display (not null)
	 * @param optional
	 *            title title of the view
	 */
	public <V extends Component & View> void addView(final String path, final V view,
			final String title)
	{
		this.views.put(path,new XdevViewEntry(path,view,title));
	}
	
	
	public ViewEntry removeView(final String path)
	{
		return this.views.remove(path);
	}
	
	
	/**
	 *
	 * @return all registered views
	 * @see #addView(String, Class)
	 * @see #addView(String, Class, String)
	 */
	public ViewEntry[] getViews()
	{
		return this.views.values().toArray(new XdevViewEntry[this.views.size()]);
	}
	
	
	/**
	 * Registers a view that is instantiated when no other view matches the
	 * navigation state.
	 *
	 * @param viewType
	 */
	public void setErrorView(final Class<? extends View> viewType)
	{
		setErrorView(new XdevViewEntry("",viewType,""));
	}
	
	
	/**
	 * Registers a view that is instantiated when no other view matches the
	 * navigation state.
	 *
	 * @param view
	 */
	public <V extends Component & View> void setErrorView(final V view)
	{
		setErrorView(new XdevViewEntry("",view,""));
	}
	
	
	private void setErrorView(final ViewEntry errorView)
	{
		if(this.navigator != null)
		{
			navigator_setErrorView(errorView);
		}
		else
		{
			this.errorView = errorView;
		}
	}
	
	
	private void navigator_setErrorView(final ViewEntry errorView)
	{
		this.navigator.setErrorProvider(new ViewProvider()
		{
			@Override
			public String getViewName(final String viewAndParameters)
			{
				return viewAndParameters;
			}
			
			
			@Override
			public View getView(final String viewName)
			{
				return errorView.getView();
			}
		});
	}
	
	
	public void removeErrorView()
	{
		if(this.navigator != null)
		{
			this.navigator.setErrorProvider(null);
		}
		else
		{
			this.errorView = null;
		}
	}
	
	
	public void addViewProvider(final ViewProvider viewProvider)
	{
		if(this.navigator != null)
		{
			this.navigator.addProvider(viewProvider);
		}
		else
		{
			if(this.additionalViewProviders == null)
			{
				this.additionalViewProviders = new ArrayList<>();
			}
			
			this.additionalViewProviders.add(viewProvider);
		}
	}
	
	
	public void removeViewProvider(final ViewProvider viewProvider)
	{
		if(this.navigator != null)
		{
			this.navigator.removeProvider(viewProvider);
		}
		else if(this.additionalViewProviders != null)
		{
			this.additionalViewProviders.remove(viewProvider);
			
			if(this.additionalViewProviders.isEmpty())
			{
				this.additionalViewProviders = null;
			}
		}
	}
	
	
	/**
	 * Navigates to a view and initialize the view with given parameters.
	 * <p>
	 * The view string consists of a view name optionally followed by a slash
	 * and a parameters part that is passed as-is to the view. ViewProviders are
	 * used to find and create the correct type of view.
	 * <p>
	 * If multiple providers return a matching view, the view with the longest
	 * name is selected. This way, e.g. hierarchies of subviews can be
	 * registered like "admin/", "admin/users", "admin/settings" and the longest
	 * match is used.
	 * <p>
	 * If the view being deactivated indicates it wants a confirmation for the
	 * navigation operation, the user is asked for the confirmation.
	 * <p>
	 * Registered {@link ViewChangeListener}s are called upon successful view
	 * change.
	 *
	 * @param navigationState
	 *            view name and parameters
	 *
	 * @throws IllegalArgumentException
	 *             if <code>navigationState</code> does not map to a known view
	 *             and no error view is registered
	 */
	public void navigateTo(final String navigationState)
	{
		if(this.navigator != null)
		{
			this.navigator.navigateTo(navigationState);
		}
		else
		{
			this.initialNavigationState = navigationState;
		}
	}
	
	
	
	private class XdevViewProvider implements ViewProvider
	{
		@Override
		public String getViewName(final String viewAndParameters)
		{
			for(final String path : XdevNavigator.this.views.keySet())
			{
				if(viewAndParameters.equals(path) || viewAndParameters.startsWith(path + "/"))
				{
					return path;
				}
			}
			
			return null;
		}
		
		
		@Override
		public View getView(final String viewName)
		{
			final ViewEntry entry = XdevNavigator.this.views.get(viewName);
			if(entry != null)
			{
				return entry.getView();
			}
			
			return null;
		}
	}
	
	
	
	public static interface ViewEntry
	{
		/**
		 * @return the path
		 */
		public String getPath();


		/**
		 * @return the view
		 */
		public View getView();


		/**
		 * @return the title
		 */
		public String getTitle();
	}
	
	
	
	private static class XdevViewEntry implements ViewEntry
	{
		private final String				path;
		private final Class<? extends View>	viewType;
		private View						view;
		private final String				title;
		
		
		private XdevViewEntry(final String path, final Class<? extends View> viewType,
				final String title)
		{
			this.path = path;
			this.viewType = viewType;
			this.title = title;
		}
		
		
		@SuppressWarnings("unchecked")
		private <V extends Component & View> XdevViewEntry(final String path, final V view,
				final String title)
		{
			this.path = path;
			this.viewType = (Class<View>)view.getClass();
			this.view = view;
			this.title = title;
		}
		
		
		@Override
		public String getPath()
		{
			return this.path;
		}
		
		
		@Override
		public View getView()
		{
			if(this.view == null)
			{
				try
				{
					this.view = this.viewType.newInstance();
				}
				catch(final Exception e)
				{
					// Checked exception is not possible, ViewProvider#getView()
					// throws nothing.
					throw new RuntimeException(e);
				}
			}
			
			return this.view;
		}
		
		
		@Override
		public String getTitle()
		{
			return this.title;
		}
	}
}
