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

package com.xdev.mobile.service.nfc;


import java.util.Arrays;


/**
 * @author XDEV Software
 * 
 */
public enum TypeNameFormat
{
	TNF_EMPTY(0, "Empty"),
	TNF_WELL_KNOWN(1, "Well Known"),
	TNF_MIME_MEDIA(2, "Mime Media"),
	TNF_ABSOLUTE_URI(3, "Absolute URI"),
	TNF_EXTERNAL_TYPE(4, "External"),
	TNF_UNKNOWN(5, "Unknown"),
	TNF_UNCHANGED(6, "Unchanged"),
	TNF_RESERVED(7, "Reserved");

	public static TypeNameFormat byNumber(final int code)
	{
		return Arrays.stream(values()).filter(tnf -> tnf.getCode() == code).findAny()
				.orElseThrow(() -> new IllegalArgumentException("No TNF for code: " + code));
	}
	
	private final int		code;
	private final String	description;
	
	
	private TypeNameFormat(final int code, final String description)
	{
		this.code = code;
		this.description = description;
		
	}
	
	
	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return this.description;
	}
	
	
	/**
	 * @return the code
	 */
	public int getCode()
	{
		return this.code;
	}
	
}
