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

package com.xdev.ui.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.xdev.ui.XdevUI;
import com.xdev.ui.event.ContextSensitiveHandlerChangeEvent;
import com.xdev.ui.event.ContextSensitiveHandlerChangeListener;
import com.xdev.ui.util.UIUtils;


/**
 * Registry for all {@link ContextSensitiveAction}s of a {@link UI}. The actions
 * are registered automatically inside this manager.
 *
 * @see #addContextSensitiveHandlerChangeListener(Class,
 *      ContextSensitiveHandlerChangeListener)
 * @see #getCurrent()
 *
 * @author XDEV Software
 *
 */
public final class XdevActionManager
{
	private static final Logger LOGGER = Logger.getLogger(XdevActionManager.class);


	/**
	 * Returns the current action manager.
	 *
	 * @return the current action manager
	 */
	public static XdevActionManager getCurrent()
	{
		final UI ui = UI.getCurrent();
		if(!(ui instanceof XdevUI))
		{
			LOGGER.warn("XdevActionManager requires XdevUI, found: " + ui.getClass().getName());
			return null;
		}
		
		final XdevUI xdevUI = (XdevUI)ui;
		
		XdevActionManager manager = xdevUI.getExtension(XdevActionManager.class);
		if(manager == null)
		{
			manager = new XdevActionManager(xdevUI);
			xdevUI.addExtension(XdevActionManager.class,manager);
		}

		return manager;
	}



	private static class ActionContext<T extends ContextSensitiveHandler>
	{
		T										handler	= null;
		final List<ContextSensitiveAction<T>>	actions	= new ArrayList<>();
	}

	private final Map<Class<? extends ContextSensitiveHandler>, ActionContext<?>>								handlerContextMap;
	private final Map<Class<? extends ContextSensitiveHandler>, List<ContextSensitiveHandlerChangeListener>>	changeListeners;


	private XdevActionManager(final XdevUI ui)
	{
		this.handlerContextMap = new HashMap<>();
		this.changeListeners = new HashMap<>();

		ui.addFocusChangeListener(event -> focusChanged(event.getComponent()));
		
		final Navigator navigator = ui.getNavigator();
		if(navigator != null)
		{
			navigator.addViewChangeListener(new ViewChangeListener()
			{
				@Override
				public boolean beforeViewChange(final ViewChangeEvent event)
				{
					return true;
				}
				
				
				@Override
				public void afterViewChange(final ViewChangeEvent event)
				{
					focusChanged(UI.getCurrent());
				}
			});
		}
	}


	/**
	 * Registeres a state change listener for a certain handler type.
	 *
	 * @see #contextSensitiveHandlerChanged(Class, Component)
	 *
	 * @param handlerType
	 *            the handler type to register the listener for
	 * @param listener
	 *            the listener to add
	 */
	public void addContextSensitiveHandlerChangeListener(
			final Class<? extends ContextSensitiveHandler> handlerType,
			final ContextSensitiveHandlerChangeListener listener)
	{
		synchronized(this.changeListeners)
		{
			List<ContextSensitiveHandlerChangeListener> list = this.changeListeners
					.get(handlerType);
			if(list == null)
			{
				list = new ArrayList<>();
				this.changeListeners.put(handlerType,list);
			}
			list.add(listener);
		}
	}


	/**
	 * Removes the listener of a certain handler type.
	 *
	 * @param handlerType
	 *            the handler type to remove the listener for
	 * @param listener
	 *            the listener to remove
	 */
	public void removeContextSensitiveHandlerChangeListener(
			final Class<? extends ContextSensitiveHandler> handlerType,
			final ContextSensitiveHandlerChangeListener listener)
	{
		synchronized(this.changeListeners)
		{
			final List<ContextSensitiveHandlerChangeListener> list = this.changeListeners
					.get(handlerType);
			if(list != null && list.remove(listener) && list.isEmpty())
			{
				this.changeListeners.remove(handlerType);
			}
		}
	}


	/**
	 * Notifies all {@link ContextSensitiveHandlerChangeListener}s that the state of
	 * a certain handler has changed.
	 *
	 * @param handlerType
	 *            the specific handler type
	 * @param handler
	 *            the handler instance
	 */
	public <H extends ContextSensitiveHandler> void fireContextSensitiveHandlerChanged(
			final Class<H> handlerType, final H handler)
	{
		synchronized(this.changeListeners)
		{
			final List<ContextSensitiveHandlerChangeListener> list = this.changeListeners
					.get(handlerType);
			if(list != null)
			{
				final ContextSensitiveHandlerChangeEvent event = new ContextSensitiveHandlerChangeEvent(
						handler);
				for(final ContextSensitiveHandlerChangeListener listener : list)
				{
					listener.contextSensitiveHandlerChanged(event);
				}
			}
		}
	}


	<T extends ContextSensitiveHandler> void registerContextSensitiveAction(
			final ContextSensitiveAction<T> action, final Class<T> handlerType)
	{
		@SuppressWarnings("unchecked")
		ActionContext<T> actionContext = (ActionContext<T>)this.handlerContextMap.get(handlerType);
		if(actionContext == null)
		{
			actionContext = new ActionContext<>();
			this.handlerContextMap.put(handlerType,actionContext);
		}
		actionContext.actions.add(action);
		if(actionContext.handler != null)
		{
			action.setHandler(actionContext.handler);
		}
		
		addContextSensitiveHandlerChangeListener(handlerType,action);
	}


	private void focusChanged(final Component root)
	{
		if(root instanceof ActionComponent && ((ActionComponent)root).getAction() != null)
		{
			// ActionComponents themselves don't fire focus event
			return;
		}

		for(final Class<? extends ContextSensitiveHandler> type : this.handlerContextMap.keySet())
		{
			focusChanged(root,type);
		}
	}


	private <T extends ContextSensitiveHandler> void focusChanged(final Component root,
			final Class<T> type)
	{
		final T newHandler = getHandler(type,root);

		@SuppressWarnings("unchecked")
		final ActionContext<T> actionContext = (ActionContext<T>)this.handlerContextMap.get(type);
		if(newHandler != actionContext.handler)
		{
			actionContext.handler = newHandler;
			for(final ContextSensitiveAction<T> action : actionContext.actions)
			{
				action.setHandler(newHandler);
			}
		}
	}


	private <T extends ContextSensitiveHandler> T getHandler(final Class<T> type,
			final Component root)
	{
		T handler = UIUtils.getNextParent(root,type);
		while(handler != null)
		{
			if(handler.isContextSensitiveHandlerEnabled(type))
			{
				return handler;
			}

			handler = UIUtils.getNextParent(((Component)handler).getParent(),type);
		}

		final T singleHandler = getSingleHandler(type,root.getUI());
		if(singleHandler != null)
		{
			return singleHandler;
		}

		return null;
	}


	private <T extends ContextSensitiveHandler> T getSingleHandler(final Class<T> type, final UI ui)
	{
		final List<T> handlers = new ArrayList<>();
		UIUtils.lookupComponentTree(ui,component -> {
			if(type.isInstance(component))
			{
				final T handler = type.cast(component);
				if(handler.isContextSensitiveHandlerEnabled(type))
				{
					handlers.add(handler);
				}
			}
			return null;
		});
		if(handlers.size() == 1)
		{
			return handlers.get(0);
		}
		return null;
	}
}
