
package com.xdev.ui.persistence.handler;


import java.util.Map;

import com.vaadin.ui.Slider;
import com.xdev.ui.persistence.GuiPersistenceEntry;


public class SliderHandler extends AbstractComponentHandler<Slider>
{
	protected static final String KEY_VALUE = "value";
	
	
	@Override
	public Class<Slider> handledType()
	{
		return Slider.class;
	}
	
	
	@Override
	protected void addEntryValues(final Map<String, Object> entryValues, final Slider component)
	{
		super.addEntryValues(entryValues,component);

		entryValues.put(KEY_VALUE,component.getValue());
	}
	
	
	@Override
	public void restore(final Slider component, final GuiPersistenceEntry entry)
	{
		super.restore(component,entry);

		component.setValue(Double.valueOf(entry.value(KEY_VALUE).toString()));

	}
}
