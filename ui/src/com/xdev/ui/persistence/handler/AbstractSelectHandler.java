
package com.xdev.ui.persistence.handler;


import java.util.Map;

import com.vaadin.ui.AbstractSelect;
import com.xdev.ui.persistence.GuiPersistenceEntry;


public class AbstractSelectHandler extends AbstractComponentHandler<AbstractSelect>
{
	protected static final String KEY_VALUE = "value";
	
	
	@Override
	public Class<AbstractSelect> handledType()
	{
		return AbstractSelect.class;
	}
	
	
	@Override
	protected void addEntryValues(final Map<String, Object> entryValues,
			final AbstractSelect component)
	{
		super.addEntryValues(entryValues,component);
		
		entryValues.put(KEY_VALUE,component.getValue());
	}
	
	
	@Override
	public void restore(final AbstractSelect component, final GuiPersistenceEntry entry)
	{
		super.restore(component,entry);
		
		component.setValue(entry.value(KEY_VALUE));
	}
}
