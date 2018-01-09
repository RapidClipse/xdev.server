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


import java.util.Collections;
import java.util.Map;


public final class GuiPersistentStates
{
	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////
	
	public static GuiPersistentStates New(final Map<String, GuiPersistentState> states)
	{
		final GuiPersistentStates instance = new GuiPersistentStates();
		instance.states = Collections.unmodifiableMap(states);
		return instance;
	}
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	
	Map<String, GuiPersistentState> states;
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	
	GuiPersistentStates()
	{
		super();
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	
	public synchronized Map<String, GuiPersistentState> states()
	{
		return this.states;
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	
	@Override
	public String toString()
	{
		final StringBuilder vs = new StringBuilder();
		for(final GuiPersistentState e : this.states.values())
		{
			vs.append(e).append("\n\n");
		}
		return vs.toString();
	}
	
}
