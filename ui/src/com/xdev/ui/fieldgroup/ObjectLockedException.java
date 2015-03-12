
package com.xdev.ui.fieldgroup;


import org.hibernate.StaleObjectStateException;

import com.xdev.ui.XdevFieldGroup;


public class ObjectLockedException extends RuntimeException
{
	private final XdevFieldGroup<?>	fieldGroup;
	private final Object			bean;


	public ObjectLockedException(final StaleObjectStateException cause,
			final XdevFieldGroup<?> fieldGroup, final Object bean)
	{
		super(cause);

		this.fieldGroup = fieldGroup;
		this.bean = bean;
	}


	@Override
	public synchronized StaleObjectStateException getCause()
	{
		return (StaleObjectStateException)super.getCause();
	}


	/**
	 * @return the fieldGroup
	 */
	public XdevFieldGroup<?> getFieldGroup()
	{
		return this.fieldGroup;
	}


	/**
	 * @return the bean
	 */
	public Object getBean()
	{
		return this.bean;
	}
}
