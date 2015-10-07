/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
