
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