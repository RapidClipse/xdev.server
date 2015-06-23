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
public final class XmlResource
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	@XmlAttribute
	String                          name    ;

	@XmlElement(name="child")
	ArrayList<XmlResourceReference> children;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	public XmlResource(final String name)
	{
		this(name, null);
	}

	public XmlResource(final String name, final ArrayList<XmlResource> children)
	{
		super();
		this.name = name;
		this.children = XmlResourceReference.box(children);
	}

	// JAXB dummy constructor
	XmlResource()
	{
		this(null, null);
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	public final List<XmlResourceReference> children()
	{
		return this.children;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	public final String name()
	{
		return this.name;
	}

}
