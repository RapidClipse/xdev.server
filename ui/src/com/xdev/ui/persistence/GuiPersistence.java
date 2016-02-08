
package com.xdev.ui.persistence;


import com.vaadin.ui.Component;
import com.xdev.ui.XdevView;


public final class GuiPersistence
{
	private GuiPersistence()
	{
	}
	
	
	public static GuiPersistentStates persist(XdevView view)
	{
		return persist(view,view.getClass().getName());
	}
	
	
	public static GuiPersistentStates persist(Component component, String name)
	{
		return GuiPersistenceAnalyzer.New().createPersister(name,component).persistState();
	}
	
	
	public static void restore(GuiPersistentStates states, XdevView view)
	{
		restore(states,view,view.getClass().getName());
	}
	
	
	public static void restore(GuiPersistentStates states, Component component, String name)
	{
		GuiPersistenceAnalyzer.New().createPersister(name,component).restoreState(states);
	}
}
