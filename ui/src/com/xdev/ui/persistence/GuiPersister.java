
package com.xdev.ui.persistence;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.vaadin.ui.Component;


public interface GuiPersister
{
	public GuiPersistentStates persistState();
	
	
	public void restoreState(GuiPersistentStates states);
	
	
	public static GuiPersister New(final Map<String, Map<String, Component>> entries)
	{
		return new Implementation(entries);
	}
	
	
	
	public static class Implementation implements GuiPersister
	{
		
		protected final static Logger						LOG	= Logger
				.getLogger(GuiPersister.class.getName());
				
		private final Map<String, Map<String, Component>>	entries;
															
															
		public Implementation(final Map<String, Map<String, Component>> entries)
		{
			super();
			this.entries = entries;
		}
		
		
		@Override
		public GuiPersistentStates persistState()
		{
			final Map<String, GuiPersistentState> states = new HashMap<>();
			
			this.entries.forEach((name, componentMap) -> {
				final Map<String, GuiPersistenceEntry> entryMap = componentMap.entrySet().stream()
						.collect(Collectors.toMap(e -> e.getKey(),
								e -> persistComponent(e.getValue())));
				states.put(name,GuiPersistentState.New(name,entryMap));
			});
			
			return GuiPersistentStates.New(states);
		}
		
		
		protected GuiPersistenceEntry persistComponent(final Component component)
		{
			return GuiPersistenceHandlerRegistry.getInstance().lookupHandler(component)
					.persist(component);
		}
		
		
		@Override
		public void restoreState(final GuiPersistentStates states)
		{
			states.states().forEach((name, state) -> {
				final Map<String, Component> componentMap = this.entries.get(name);
				if(componentMap == null)
				{
					LOG.info("[GuiPersister.restoreState] Unused state: " + name);
				}
				else
				{
					state.entries().forEach((identifier, entry) -> {
						final Component component = componentMap.get(identifier);
						if(component == null)
						{
							LOG.info("[GuiPersister.restoreState] Component not found: " + name);
						}
						else
						{
							GuiPersistenceHandlerRegistry.getInstance().lookupHandler(component)
									.restore(component,entry);
						}
					});
				}
			});
		}
	}
}
