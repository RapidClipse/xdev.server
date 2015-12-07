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

package com.xdev.mobile.service.contacts;


/**
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


	public String getFormatted()
	{
		return this.formatted;
	}


	public void setFormatted(final String formatted)
	{
		this.formatted = formatted;
	}


	public String getFamilyName()
	{
		return this.familyName;
	}


	public void setFamilyName(final String familyName)
	{
		this.familyName = familyName;
	}


	public String getGivenName()
	{
		return this.givenName;
	}


	public void setGivenName(final String givenName)
	{
		this.givenName = givenName;
	}


	public String getMiddle()
	{
		return this.middle;
	}


	public void setMiddle(final String middle)
	{
		this.middle = middle;
	}


	public String getPrefix()
	{
		return this.prefix;
	}


	public void setPrefix(final String prefix)
	{
		this.prefix = prefix;
	}


	public String getSuffix()
	{
		return this.suffix;
	}


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
