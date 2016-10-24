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

package com.xdev.mobile.service.contacts;


/**
 * Contains different kinds of information about a Contact object's name.
 *
 * @author XDEV Software
 *
 */
public class ContactName
{
	private String	formatted;
	private String	familyName;
	private String	givenName;
	private String	middle;
	private String	prefix;
	private String	suffix;


	public ContactName()
	{
	}


	public ContactName(final String formatted, final String familyName, final String givenName,
			final String middle, final String prefix, final String suffix)
	{
		this.formatted = formatted;
		this.familyName = familyName;
		this.givenName = givenName;
		this.middle = middle;
		this.prefix = prefix;
		this.suffix = suffix;
	}
	
	
	/**
	 * The complete name of the contact.
	 */
	public String getFormatted()
	{
		return this.formatted;
	}
	
	
	/**
	 * The complete name of the contact.
	 */
	public void setFormatted(final String formatted)
	{
		this.formatted = formatted;
	}
	
	
	/**
	 * The contact's family name.
	 */
	public String getFamilyName()
	{
		return this.familyName;
	}
	
	
	/**
	 * The contact's family name.
	 */
	public void setFamilyName(final String familyName)
	{
		this.familyName = familyName;
	}
	
	
	/**
	 * The contact's given name.
	 */
	public String getGivenName()
	{
		return this.givenName;
	}
	
	
	/**
	 * The contact's given name.
	 */
	public void setGivenName(final String givenName)
	{
		this.givenName = givenName;
	}
	
	
	/**
	 * The contact's middle name.
	 */
	public String getMiddle()
	{
		return this.middle;
	}
	
	
	/**
	 * The contact's middle name.
	 */
	public void setMiddle(final String middle)
	{
		this.middle = middle;
	}
	
	
	/**
	 * The contact's prefix (example Mr. or Dr.)
	 */
	public String getPrefix()
	{
		return this.prefix;
	}
	
	
	/**
	 * The contact's prefix (example Mr. or Dr.)
	 */
	public void setPrefix(final String prefix)
	{
		this.prefix = prefix;
	}
	
	
	/**
	 * The contact's suffix (example Esq.)
	 */
	public String getSuffix()
	{
		return this.suffix;
	}
	
	
	/**
	 * The contact's suffix (example Esq.)
	 */
	public void setSuffix(final String suffix)
	{
		this.suffix = suffix;
	}
	
	
	@Override
	public String toString()
	{
		if(this.formatted != null && this.formatted.length() > 0)
		{
			return this.formatted;
		}
		
		return super.toString();
	}
}
