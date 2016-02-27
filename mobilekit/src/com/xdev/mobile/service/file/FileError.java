/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
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
 * 
 * For further information see 
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.mobile.service.file;


/**
 * @author XDEV Software
 *
 */
public enum FileError
{

	// TODO

	// File error codes
	// Found in DOMException
	// FileError.NOT_FOUND_ERR = 1;
	// FileError.SECURITY_ERR = 2;
	// FileError.ABORT_ERR = 3;
	//
	// // Added by File API specification
	// FileError.NOT_READABLE_ERR = 4;
	// FileError.ENCODING_ERR = 5;
	// FileError.NO_MODIFICATION_ALLOWED_ERR = 6;
	// FileError.INVALID_STATE_ERR = 7;
	// FileError.SYNTAX_ERR = 8;
	// FileError.INVALID_MODIFICATION_ERR = 9;
	// FileError.QUOTA_EXCEEDED_ERR = 10;
	// FileError.TYPE_MISMATCH_ERR = 11;
	// FileError.PATH_EXISTS_ERR = 12;

	NOT_FOUND_ERR("NOT_FOUND_ERR"),
	SECURITY_ERR("SECURITY_ERR"),
	ABORT_ERR("ABORT_ERR"),
	NOT_READABLE_ERR("NOT_READABLE_ERR"),
	ENCODING_ERR("ENCODING_ERR"),
	NO_MODIFICATION_ALLOWED_ERR("NO_MODIFICATION_ALLOWED_ERR"),
	INVALID_STATE_ERR("INVALID_STATE_ERR"),
	SYNTAX_ERR("SYNTAX_ERR"),
	INVALID_MODIFICATION_ERR("INVALID_MODIFICATION_ERR"),
	QUOTA_EXCEEDED_ERR("QUOTA_EXCEEDED_ERR"),
	TYPE_MISMATCH_ERR("TYPE_MISMATCH_ERR"),
	PATH_EXISTS_ERR("PATH_EXISTS_ERR");

	private String	value;


	FileError(final String value)
	{
		this.value = value;

	}


	public static FileError fromValue(final String value)
	{
		if(value == null || "".equals(value))
		{
			throw new IllegalArgumentException("Value cannot be null or empty!");

		}
		else if(FileError.NOT_FOUND_ERR.getValue().equals(value))
		{
			return FileError.NOT_FOUND_ERR;
		}
		else if(FileError.SECURITY_ERR.value.equals(value))
		{
			return FileError.SECURITY_ERR;
		}
		else if(FileError.ABORT_ERR.getValue().equals(value))
		{
			return FileError.ABORT_ERR;
		}
		else if(FileError.NOT_READABLE_ERR.getValue().equals(value))
		{
			return FileError.NOT_READABLE_ERR;
		}
		else if(FileError.ENCODING_ERR.getValue().equals(value))
		{
			return FileError.ENCODING_ERR;
		}
		else if(FileError.NO_MODIFICATION_ALLOWED_ERR.getValue().equals(value))
		{
			return FileError.NO_MODIFICATION_ALLOWED_ERR;
		}
		else if(FileError.INVALID_STATE_ERR.getValue().equals(value))
		{
			return FileError.INVALID_STATE_ERR;
		}
		else if(FileError.SYNTAX_ERR.getValue().equals(value))
		{
			return FileError.SYNTAX_ERR;
		}
		else if(FileError.INVALID_MODIFICATION_ERR.getValue().equals(value))
		{
			return FileError.INVALID_MODIFICATION_ERR;
		}
		else if(FileError.QUOTA_EXCEEDED_ERR.getValue().equals(value))
		{
			return FileError.QUOTA_EXCEEDED_ERR;
		}
		else if(FileError.TYPE_MISMATCH_ERR.getValue().equals(value))
		{
			return FileError.TYPE_MISMATCH_ERR;
		}
		else if(FileError.PATH_EXISTS_ERR.getValue().equals(value))
		{
			return FileError.PATH_EXISTS_ERR;
		}
		else
		{
			throw new IllegalArgumentException("Cannot create enum from " + value + " value!");
		}
	}


	/**
	 * @return the value
	 */
	public String getValue()
	{
		return this.value;
	}


	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(final String value)
	{
		this.value = value;
	}


	@Override
	public String toString()
	{
		return super.toString();
	}
}
