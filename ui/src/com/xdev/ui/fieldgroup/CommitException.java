
package com.xdev.ui.fieldgroup;


import java.util.Map;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Field;
import com.xdev.ui.XdevFieldGroup;


public class CommitException extends RuntimeException
{
	private final com.vaadin.data.fieldgroup.FieldGroup.CommitException	wrapped;


	public CommitException(final com.vaadin.data.fieldgroup.FieldGroup.CommitException wrapped)
	{
		super("Commit failed");

		this.wrapped = wrapped;
	}


	/**
	 * Returns a map containing the fields which failed validation and the
	 * exceptions the corresponding validators threw.
	 *
	 * @return a map with all the invalid value exceptions. Can be empty but not
	 *         null
	 */
	public Map<Field<?>, InvalidValueException> getInvalidFields()
	{
		return this.wrapped.getInvalidFields();
	}


	/**
	 * Returns the field group where the exception occurred
	 *
	 * @return the field group
	 */
	public XdevFieldGroup<?> getFieldGroup()
	{
		return (XdevFieldGroup<?>)this.wrapped.getFieldGroup();
	}
}
