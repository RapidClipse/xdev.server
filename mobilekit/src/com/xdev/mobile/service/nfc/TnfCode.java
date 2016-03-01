/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xdev.mobile.service.nfc;


import java.util.Arrays;


/**
 * @author XDEV Software
 *		
 */
public enum TnfCode
{
	TNF_EMPTY(0, "Empty"),
	TNF_WELL_KNOWN(1, "Well Known"),
	TNF_MIME_MEDIA(2, "Mime Media"),
	TNF_ABSOLUTE_URI(3, "Absolute URI"),
	TNF_EXTERNAL_TYPE(4, "External"),
	TNF_UNKNOWN(5, "Unknown"),
	TNF_UNCHANGED(6, "Unchanged"),
	TNF_RESERVED(7, "Reserved");
	
	public static TnfCode byNumber(final int number)
	{
		return Arrays.stream(values()).filter(code -> code.getNumber() == number).findAny()
				.orElseThrow(() -> new IllegalArgumentException("No Type for Number: " + number));
	}

	private final int		number;
	private final String	description;


	private TnfCode(final int number, final String description)
	{
		this.number = number;
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
	 * @return the number
	 */
	public int getNumber()
	{
		return this.number;
	}

}
