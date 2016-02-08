
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
