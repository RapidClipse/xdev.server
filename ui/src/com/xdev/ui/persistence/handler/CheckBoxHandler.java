
package com.xdev.ui.persistence.handler;


import java.util.Map;

import com.vaadin.ui.CheckBox;
import com.xdev.ui.persistence.GuiPersistenceEntry;


public class CheckBoxHandler extends AbstractComponentHandler<CheckBox>
{
	protected static final String KEY_VALUE = "value";


	@Override
	public Class<CheckBox> handledType()
	{
		return CheckBox.class;
	}


	@Override
	protected void addEntryValues(final Map<String, Object> entryValues, final CheckBox component)
	{
		super.addEntryValues(entryValues,component);
		
		entryValues.put(KEY_VALUE,component.getValue());
	}


	@Override
	public void restore(final CheckBox component, final GuiPersistenceEntry entry)
	{
		super.restore(component,entry);
		
		component.setValue((boolean)(entry.value(KEY_VALUE)));
		
	}
}
