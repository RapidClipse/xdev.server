
package com.xdev.ui.persistence.handler;


import java.util.HashMap;
import java.util.Map;

import com.vaadin.ui.AbstractComponent;
import com.xdev.ui.persistence.GuiPersistenceEntry;
import com.xdev.ui.persistence.GuiPersistenceHandler;


public abstract class AbstractComponentHandler<C extends AbstractComponent>
		implements GuiPersistenceHandler<C>
{
	protected static final String KEY_VISIBLE = "visible";
	
	
	/**
	 * Trivial utility method to improve use site readability.
	 *
	 * @param uncastNumberInstance
	 *            the uncast {@link Number} instance to be cast.
	 * @return the instance cast as {@link Number}.
	 */
	protected static Number number(final Object uncastNumberInstance)
	{
		return (Number)uncastNumberInstance;
	}
	
	
	@Override
	public GuiPersistenceEntry persist(final C component)
	{
		final Map<String, Object> valueTable = new HashMap<>();
		this.addEntryValues(valueTable,component);
		return GuiPersistenceEntry.New(valueTable);
	}
	
	
	protected void addEntryValues(final Map<String, Object> entryValues, final C component)
	{
		entryValues.put(KEY_VISIBLE,component.isVisible());
	}
	
	
	@Override
	public void restore(final C component, final GuiPersistenceEntry entry)
	{
		component.setVisible((Boolean)entry.value(KEY_VISIBLE));
	}
	
	
	public static String changeObjectArray(final Object[] x)
	{
		String s = "";

		for(final Object o : x)
		{
			s = s + o + ";";
		}

		s = s.substring(0,s.length() - 1);

		return s;
	}
	
	
	public static Object[] returnObjectArray(final String s)
	{
		final String[] parts = s.split(";");

		return parts;
		
	}

}
