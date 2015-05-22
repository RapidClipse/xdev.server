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
public final class XmlSubject implements Named
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	@XmlAttribute
	String                      name    ;

	@XmlAttribute
	String                      password;

	@XmlElement(name="role")
	ArrayList<XmlRoleReference> roles;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	public XmlSubject(final String name, final String password, final ArrayList<XmlRole> roles)
	{
		super();
		this.name     = name    ;
		this.password = password;
		this.roles    = XmlRoleReference.box(roles);
	}

	public XmlSubject(final String name, final ArrayList<XmlRole> roles)
	{
		this(name, null, roles);
	}

	// JAXB dummy constructor
	XmlSubject()
	{
		this(null, null, null);
	}







	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	public final String password()
	{
		return this.password;
	}

	public final List<XmlRoleReference> roles()
	{
		return this.roles;
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
