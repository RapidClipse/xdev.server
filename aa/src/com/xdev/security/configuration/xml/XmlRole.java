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
 */

package com.xdev.security.configuration.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


/**
 * JAXB mapping type.
 *
 * @author XDEV Software (TM)
 */
public final class XmlRole
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	@XmlAttribute
	String                      name       ;

	@XmlElement(name="role")
	ArrayList<XmlRoleReference> roles      ;

	@XmlElement(name="permission")
	ArrayList<XmlPermission>    permissions;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	public XmlRole(final String name)
	{
		this(name, null, null);
	}

	public XmlRole(final String name, final ArrayList<XmlPermission> permissions)
	{
		this(name, null, permissions);
	}

	public XmlRole(final String name, final ArrayList<XmlRole> roles, final ArrayList<XmlPermission> permissions)
	{
		super();
		this.name        = name                       ;
		this.roles       = XmlRoleReference.box(roles);
		this.permissions = permissions                ;
	}

	// JAXB dummy constructor
	XmlRole()
	{
		this(null, null, null);
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	public final List<XmlRoleReference> roles()
	{
		return this.roles;
	}

	public final List<XmlPermission> permissions()
	{
		return this.permissions;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	public final String name()
	{
		return this.name;
	}

}
