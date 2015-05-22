/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.configuration.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import net.jadoth.util.chars.Named;


/**
 * JAXB mapping type.
 *
 * @author XDEV Software (TM)
 */
public final class XmlRole implements Named
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

	@Override
	public final String name()
	{
		return this.name;
	}

}
