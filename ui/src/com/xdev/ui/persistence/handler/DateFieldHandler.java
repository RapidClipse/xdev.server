
package com.xdev.ui.persistence.handler;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.vaadin.ui.DateField;
import com.xdev.ui.persistence.GuiPersistenceEntry;


public class DateFieldHandler extends AbstractComponentHandler<DateField>
{
	protected static final String KEY_VALUE = "value";


	@Override
	public Class<DateField> handledType()
	{
		return DateField.class;
	}


	@Override
	protected void addEntryValues(final Map<String, Object> entryValues, final DateField component)
	{
		super.addEntryValues(entryValues,component);

		final Date comValue = component.getValue();
		if(comValue != null)
		{
			final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			final String strDate = sdfDate.format(comValue);
			
			entryValues.put(KEY_VALUE,strDate);
		}
	}


	@Override
	public void restore(final DateField component, final GuiPersistenceEntry entry)
	{
		super.restore(component,entry);
		
		if(entry.value(KEY_VALUE) != null)
		{
			final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date comD = new Date();
			try
			{
				comD = sdfDate.parse(entry.value(KEY_VALUE).toString());
			}
			catch(final ParseException e)
			{
				e.printStackTrace();
			}
			
			component.setValue(comD);
		}
	}
}
