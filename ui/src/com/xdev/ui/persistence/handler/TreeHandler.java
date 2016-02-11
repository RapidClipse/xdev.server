
package com.xdev.ui.persistence.handler;


import java.util.Collection;
import java.util.Map;

import com.vaadin.ui.Tree;
import com.xdev.ui.persistence.GuiPersistenceEntry;


public class TreeHandler extends AbstractComponentHandler<Tree>
{
	protected static final String	KEY_VALUE		= "value";
	protected static final String	KEY_EXPANDED	= "expanded";


	@Override
	public Class<Tree> handledType()
	{
		return Tree.class;
	}
	
	
	@Override
	protected void addEntryValues(final Map<String, Object> entryValues, final Tree component)
	{
		super.addEntryValues(entryValues,component);
		
		final Collection<?> collItem = component.getItemIds();

		final Object[] oaExpanded = new Object[collItem.size()];
		int i = 0;

		for(final Object o : collItem)
		{
			final Boolean b = component.isExpanded(o);

			if(b == true)
			{
				oaExpanded[i] = true;
			}
			else
			{
				oaExpanded[i] = false;
			}
			i++;
		}

		entryValues.put(KEY_EXPANDED,changeObjectArray(oaExpanded));
		entryValues.put(KEY_VALUE,component.getValue());
	}
	
	
	@Override
	public void restore(final Tree component, final GuiPersistenceEntry entry)
	{
		super.restore(component,entry);

		component.setValue(entry.value(KEY_VALUE));
		
		final Object[] oaExpanded = returnObjectArray(entry.value(KEY_EXPANDED).toString());
		final Collection<?> collItem = component.getItemIds();
		int i = 0;
		
		for(final Object o : collItem)
		{
			if(new Boolean(oaExpanded[i].toString()) == true)
			{
				component.expandItem(o);
			}
			else
			{
				component.collapseItem(o);
			}
			i++;
		}
	}
}
