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

package com.xdev.ui.persistence;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import com.vaadin.ui.Component;
import com.xdev.ui.util.UIUtils;


public interface GuiPersistenceAnalyzer
{

	public default GuiPersister createPersister(final String name, final Component root)
	{
		final Map<String, Component> map = new HashMap<>();
		map.put(name,root);
		return createPersister(map);
	}


	public GuiPersister createPersister(Map<String, Component> roots);


	public static GuiPersistenceAnalyzer New()
	{
		return new Implementation();
	}



	public static class Implementation implements GuiPersistenceAnalyzer
	{
		@Override
		public GuiPersister createPersister(final Map<String, Component> roots)
		{
			final Map<String, Map<String, Component>> entries = new HashMap<>();
			roots.entrySet().forEach(entry -> {
				final String name = entry.getKey();
				final Component root = entry.getValue();
				final Map<String, Component> map = collectComponents(root).stream()
						.collect(Collectors.toMap(c -> getIdentifier(c),c -> c));
				entries.put(name,map);
			});

			return GuiPersister.New(entries);
		}


		private String getIdentifier(final Component component)
		{
			final String identifier = componentIdentifier(component);
			if(identifier == null || identifier.length() == 0)
			{
				throw new IllegalArgumentException("Empty identifier");
			}
			return identifier;
		}


		protected boolean persist(final Component component)
		{
			if(GuiPersistenceHandlerRegistry.getInstance().lookupHandler(component) == null)
			{
				return false;
			}
			final String identifier = componentIdentifier(component);
			return identifier != null && identifier.length() > 0;
		}


		protected String componentIdentifier(final Component component)
		{
			return component.getId();
		}


		protected Collection<Component> collectComponents(final Component root)
		{
			final Collection<Component> collection = new HashSet<>();
			UIUtils.lookupComponentTree(root,cpn -> {
				if(persist(cpn))
				{
					collection.add(cpn);
				}
				return null;
			});
			return collection;
		}
	}
}
