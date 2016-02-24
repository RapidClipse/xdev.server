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


import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;


public final class GuiPersistenceEntry
{
	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////
	
	public static GuiPersistenceEntry New(final Map<String, Object> values)
	{
		final GuiPersistenceEntry instance = new GuiPersistenceEntry();
		instance.values = Collections.unmodifiableMap(values);
		return instance;
	}
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	
	Map<String, Object> values;
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	
	public GuiPersistenceEntry()
	{
		super();
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	
	public final synchronized Map<String, Object> values()
	{
		return this.values;
	}
	
	
	public Object value(final String key)
	{
		return this.values().get(key);
	}
	
	
	@Override
	public String toString()
	{
		return toString("");
	}
	
	
	String toString(final String prefix)
	{
		return this.values.entrySet().stream()
				.map(entry -> prefix + entry.getKey() + ":\t" + entry.getValue())
				.collect(Collectors.joining("\n"));
	}
	
}
