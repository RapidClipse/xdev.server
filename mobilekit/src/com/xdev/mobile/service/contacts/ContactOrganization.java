/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
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

package com.xdev.mobile.service.contacts;


/**
 * @author XDEV Software
 * 		
 */
public class ContactOrganization
{
	private String	id;
	private boolean	pref;
	private String	type;
	private String	name;
	private String	department;
	private String	title;


	public ContactOrganization()
	{
	}


	public ContactOrganization(final String id, final boolean pref, final String type,
			final String name, final String department, final String title)
	{
		this.id = id;
		this.pref = pref;
		this.type = type;
		this.name = name;
		this.department = department;
		this.title = title;
	}


	public String getId()
	{
		return this.id;
	}


	public void setId(final String id)
	{
		this.id = id;
	}


	public boolean isPref()
	{
		return this.pref;
	}


	public void setPref(final boolean pref)
	{
		this.pref = pref;
	}


	public String getType()
	{
		return this.type;
	}


	public void setType(final String type)
	{
		this.type = type;
	}


	public String getName()
	{
		return this.name;
	}


	public void setName(final String name)
	{
		this.name = name;
	}


	public String getDepartment()
	{
		return this.department;
	}


	public void setDepartment(final String department)
	{
		this.department = department;
	}


	public String getTitle()
	{
		return this.title;
	}


	public void setTitle(final String title)
	{
		this.title = title;
	}
	
	
	@Override
	public String toString()
	{
		return this.type + " = " + this.name;
	}
}
