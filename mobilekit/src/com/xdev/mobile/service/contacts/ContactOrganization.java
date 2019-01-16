/*
 * Copyright (C) 2013-2019 by XDEV Software, All Rights Reserved.
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
 * The ContactOrganization object stores a contact's organization properties. A
 * {@link Contact} object stores one or more ContactOrganization objects in a
 * list.
 *
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
	
	
	/**
	 * Unique identifier
	 */
	public String getId()
	{
		return this.id;
	}
	
	
	/**
	 * Unique identifier
	 */
	public ContactOrganization setId(final String id)
	{
		this.id = id;
		return this;
	}


	/**
	 * Set to <code>true</code> if this ContactOrganization contains the user's
	 * preferred value.
	 */
	public boolean isPref()
	{
		return this.pref;
	}


	/**
	 * Set to <code>true</code> if this ContactOrganization contains the user's
	 * preferred value.
	 */
	public ContactOrganization setPref(final boolean pref)
	{
		this.pref = pref;
		return this;
	}


	/**
	 * A string that indicates what type of field this is, &quot;home&quot; for
	 * example.
	 */
	public String getType()
	{
		return this.type;
	}


	/**
	 * A string that indicates what type of field this is, &quot;home&quot; for
	 * example.
	 */
	public ContactOrganization setType(final String type)
	{
		this.type = type;
		return this;
	}


	/**
	 * The name of the organization.
	 */
	public String getName()
	{
		return this.name;
	}


	/**
	 * The name of the organization.
	 */
	public ContactOrganization setName(final String name)
	{
		this.name = name;
		return this;
	}


	/**
	 * The department the contact works for.
	 */
	public String getDepartment()
	{
		return this.department;
	}


	/**
	 * The department the contact works for.
	 */
	public ContactOrganization setDepartment(final String department)
	{
		this.department = department;
		return this;
	}


	/**
	 * The contact's title at the organization.
	 */
	public String getTitle()
	{
		return this.title;
	}


	/**
	 * The contact's title at the organization.
	 */
	public ContactOrganization setTitle(final String title)
	{
		this.title = title;
		return this;
	}
	
	
	@Override
	public String toString()
	{
		return this.type + " = " + this.name;
	}
}
