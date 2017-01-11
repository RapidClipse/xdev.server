/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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
