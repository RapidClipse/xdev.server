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
public class ContactField
{
	private String	id;
	private String	type;
	private String	value;
	private boolean	pref;


	public ContactField()
	{
	}


	public ContactField(final String id, final String type, final String value, final boolean pref)
	{
		this.id = id;
		this.type = type;
		this.value = value;
		this.pref = pref;
	}


	public String getId()
	{
		return this.id;
	}


	public void setId(final String id)
	{
		this.id = id;
	}


	public String getType()
	{
		return this.type;
	}


	public void setType(final String type)
	{
		this.type = type;
	}


	public String getValue()
	{
		return this.value;
	}


	public void setValue(final String value)
	{
		this.value = value;
	}


	public boolean isPref()
	{
		return this.pref;
	}


	public void setPref(final boolean pref)
	{
		this.pref = pref;
	}


	@Override
	public String toString()
	{
		return this.type + " = " + this.value;
	}
}
