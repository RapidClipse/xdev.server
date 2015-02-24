
package com.xdev.ui;


import java.util.Date;

import com.vaadin.data.Property;
import com.vaadin.ui.PopupDateField;


/**
 * A date entry component, which displays the actual date selector as a popup.
 *
 * @author XDEV Software
 */
public class XdevPopupDateField extends PopupDateField
{
	/**
	 *
	 */
	public XdevPopupDateField()
	{
		super();
	}
	
	
	/**
	 * @param dataSource
	 * @throws IllegalArgumentException
	 */
	public XdevPopupDateField(@SuppressWarnings("rawtypes") final Property dataSource)
			throws IllegalArgumentException
	{
		super(dataSource);
	}
	
	
	/**
	 * @param caption
	 * @param value
	 */
	public XdevPopupDateField(final String caption, final Date value)
	{
		super(caption,value);
	}
	
	
	/**
	 * @param caption
	 * @param dataSource
	 */
	public XdevPopupDateField(final String caption,
			@SuppressWarnings("rawtypes") final Property dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * @param caption
	 */
	public XdevPopupDateField(final String caption)
	{
		super(caption);
	}
}
