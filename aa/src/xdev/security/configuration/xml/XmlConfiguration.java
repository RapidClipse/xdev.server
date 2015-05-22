/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.configuration.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import xdev.security.authorization.AuthorizationException;


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
