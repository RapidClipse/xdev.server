
package com.xdev.ui.persistence.handler;


import java.util.Map;

import com.vaadin.ui.AbstractTextField;
import com.xdev.ui.persistence.GuiPersistenceEntry;


public class AbstractTextFieldHandler extends AbstractComponentHandler<AbstractTextField>
{
	protected static final String KEY_VALUE = "value";


	@Override
	public Class<AbstractTextField> handledType()
	{
		return AbstractTextField.class;
	}


	@Override
	protected void addEntryValues(final Map<String, Object> entryValues,
			final AbstractTextField component)
	{
		super.addEntryValues(entryValues,component);
		
		entryValues.put(KEY_VALUE,component.getValue());
	}


	@Override
	public void restore(final AbstractTextField component, final GuiPersistenceEntry entry)
	{
		super.restore(component,entry);
		
		component.setValue(entry.value(KEY_VALUE).toString());
		
	}
}
