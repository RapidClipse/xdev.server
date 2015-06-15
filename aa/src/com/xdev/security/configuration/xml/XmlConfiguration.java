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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.xdev.security.authorization.AuthorizationException;


/**
 * JAXB mapping type.
 *
 * @author XDEV Software (TM)
 */
@XmlRootElement(name="security")
public final class XmlConfiguration
{
	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////

	public static final XmlConfiguration readFromFile(final File xmlFile) throws AuthorizationException
	{
		try
		{
			final JAXBContext      jaxbContext      = JAXBContext.newInstance(XmlConfiguration.class);
			final Unmarshaller     jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final XmlConfiguration xmlConfig        = (XmlConfiguration)jaxbUnmarshaller.unmarshal(xmlFile);
			return xmlConfig;
		}
		catch(final Exception e)
		{
			// nothing to do here, just wrap in context-specific exception type.
			throw new AuthorizationException(e);
		}
	}




	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	@XmlElement(name="resource")
	ArrayList<XmlResource> resources;

	@XmlElement(name="role")
	ArrayList<XmlRole>     roles    ;

	@XmlElement(name="subject")
	ArrayList<XmlSubject>  subjects ;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	public XmlConfiguration(
		final ArrayList<XmlResource> resources,
		final ArrayList<XmlRole>     roles    ,
		final ArrayList<XmlSubject>  subjects
	)
	{
		super();
		this.resources = resources;
		this.roles     = roles    ;
		this.subjects  = subjects ;
	}

	// JAXB dummy constructor
	XmlConfiguration()
	{
		this(null, null, null);
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	public final List<XmlResource> resources()
	{
		return this.resources;
	}

	public final List<XmlRole> roles()
	{
		return this.roles;
	}

	public final List<XmlSubject> subjects()
	{
		return this.subjects;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public final String toString()
	{
		return this.getClass().getSimpleName()+" ("
			+this.resources.size()+" resources, "
			+this.roles.size()+" roles, "
			+this.subjects.size()+" subjects)"
		;
	}
}
