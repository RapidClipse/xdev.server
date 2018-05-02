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

package com.xdev.ui.persistence;


import com.vaadin.ui.Component;


/**
 * Helper class to quickly save and restore GUI states.
 *
 * @see #save(Component, String)
 * @see #load(Component, String, String)
 * @see #persist(Component, String)
 * @see #restore(Component, String, GuiPersistentStates)
 * 
 * @author XDEV Software
 */
public final class GuiPersistence
{
	private GuiPersistence()
	{
	}


	/**
	 * Persists the state of <code>component</code> and its sub-components and
	 * returns the serialized data.
	 *
	 * @param component
	 *            the root component
	 * @param name
	 *            the state's name
	 * @return the serialized state data
	 * @see #load(Component, String, String)
	 */
	public static String save(final Component component, final String name)
	{
		return GuiPersistenceSerializer.DEFAULT.serialize(persist(component,name));
	}


	/**
	 * Persists the state of <code>component</code> and its sub-components.
	 *
	 * @param component
	 *            the root component
	 * @param name
	 *            the state's name
	 * @return the state data
	 * @see #restore(Component, String, GuiPersistentStates)
	 */
	public static GuiPersistentStates persist(final Component component, final String name)
	{
		return GuiPersistenceAnalyzer.New().createPersister(name,component).persistState();
	}


	/**
	 * Restores a previously saved state of the <code>component</code> and its
	 * sub-components.
	 *
	 * @param component
	 *            the root component
	 * @param name
	 *            the state's name
	 * @param data
	 *            the serialized data
	 * @see #save(Component, String)
	 */
	public static void load(final Component component, final String name, final String data)
	{
		restore(component,name,GuiPersistenceSerializer.DEFAULT.deserialize(data));
	}


	/**
	 * Restores a previously saved state of the <code>component</code> and its
	 * sub-components.
	 *
	 * @param component
	 *            the root component
	 * @param name
	 *            the state's name
	 * @param states
	 *            the persistence state
	 * @see #persist(Component, String)
	 */
	public static void restore(final Component component, final String name,
			final GuiPersistentStates states)
	{
		GuiPersistenceAnalyzer.New().createPersister(name,component).restoreState(states);
	}
}
