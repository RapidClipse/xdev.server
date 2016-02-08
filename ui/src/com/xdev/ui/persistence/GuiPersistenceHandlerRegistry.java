
package com.xdev.ui.persistence;


import java.util.HashMap;
import java.util.Map;

import com.vaadin.ui.Component;
import com.xdev.ui.persistence.handler.AbstractSplitPanelHandler;
import com.xdev.ui.persistence.handler.TabSheetHandler;


public final class GuiPersistenceHandlerRegistry
{
	private static GuiPersistenceHandlerRegistry INSTANCE;


	public static GuiPersistenceHandlerRegistry getInstance()
	{
		if(INSTANCE == null)
		{
			INSTANCE = new GuiPersistenceHandlerRegistry();
		}
		return INSTANCE;
	}

	private final Map<Class<? extends Component>, GuiPersistenceHandler<? extends Component>> handlers;


	private GuiPersistenceHandlerRegistry()
	{
		this.handlers = new HashMap<>();
		addDefaultHandlers();
	}


	private void addDefaultHandlers()
	{
		registerHandler(new AbstractSplitPanelHandler());
		registerHandler(new TabSheetHandler());
	}


	public <C extends Component> void registerHandler(final GuiPersistenceHandler<C> handler)
	{
		this.registerHandler(handler.handledType(),handler);
	}


	public <C extends Component> void registerHandler(final Class<C> type,
			final GuiPersistenceHandler<? super C> handler)
	{
		this.handlers.put(type,handler);
	}


	@SuppressWarnings("unchecked")
	public <C extends Component> GuiPersistenceHandler<? super C> lookupHandler(final C component)
	{
		return (GuiPersistenceHandler<? super C>)lookupHandler(component.getClass());
	}


	@SuppressWarnings("unchecked") // cast necessary due to
	// type-heterogeneous collection content
	public <C extends Component> GuiPersistenceHandler<? super C> lookupHandler(
			final Class<C> componentType)
	{
		// check for a handler directly fitting the passed type
		final GuiPersistenceHandler<? extends Component> handler = this.handlers.get(componentType);
		if(handler != null)
		{
			return (GuiPersistenceHandler<? super C>)handler;
		}

		final Class<?> superclass = componentType.getSuperclass();
		if(superclass != null && Component.class.isAssignableFrom(superclass))
		{
			return (GuiPersistenceHandler<? super C>)lookupHandler(
					(Class<? extends Component>)superclass);
		}

		/*
		 * potentially null handler returned intentionally to give calling
		 * context the decision to either throw a specific exception or get a
		 * handler from somewhere else. This provider's answer is just
		 * "no handler found" (null), that is not an exception/problem.
		 */
		return (GuiPersistenceHandler<? super C>)handler;
	}
}
