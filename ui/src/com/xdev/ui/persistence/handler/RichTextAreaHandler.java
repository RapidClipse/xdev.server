
package com.xdev.ui.persistence.handler;


import java.util.Map;

import com.vaadin.ui.RichTextArea;
import com.xdev.ui.persistence.GuiPersistenceEntry;


public class RichTextAreaHandler extends AbstractComponentHandler<RichTextArea>
{
	protected static final String KEY_VALUE = "value";


	@Override
	public Class<RichTextArea> handledType()
	{
		return RichTextArea.class;
	}


	@Override
	protected void addEntryValues(final Map<String, Object> entryValues,
			final RichTextArea component)
	{
		super.addEntryValues(entryValues,component);
		
		entryValues.put(KEY_VALUE,component.getValue());
	}


	@Override
	public void restore(final RichTextArea component, final GuiPersistenceEntry entry)
	{
		super.restore(component,entry);
		
		component.setValue(entry.value(KEY_VALUE).toString());
		
	}
}
