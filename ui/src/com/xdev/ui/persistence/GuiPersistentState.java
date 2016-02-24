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

package com.xdev.ui.persistence;


import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Map;


public final class GuiPersistentState
{
	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////
	
	public static GuiPersistentState New(final String name,
			final Map<String, GuiPersistenceEntry> entries)
	{
		final GuiPersistentState instance = new GuiPersistentState();
		instance.name = requireNonNull(name);
		instance.entries = Collections.unmodifiableMap(requireNonNull(entries));
		return instance;
	}
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	
	String								name;
	Map<String, GuiPersistenceEntry>	entries;
										
										
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	
	GuiPersistentState()
	{
		super();
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	
	public final String name()
	{
		return this.name;
	}
	
	
	public final Map<String, GuiPersistenceEntry> entries()
	{
		return this.entries;
	}
	
	
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder().append("------").append('\n').append(this.name)
				.append('\n').append("------").append('\n');
		this.entries.entrySet().forEach(e -> sb.append(e.getKey()).append(" = ")
				.append(e.getValue()).append('\n').append("---").append('\n'));
		return sb.toString();
	}
}
