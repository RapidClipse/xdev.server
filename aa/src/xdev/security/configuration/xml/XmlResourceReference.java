/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.configuration.xml;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;

import net.jadoth.util.chars.Named;

// pragmatic workaround class for insufficient JAXB concept
/**
 * JAXB mapping type.
 *
 * @author XDEV Software (TM)
 */
public final class XmlResourceReference implements Named
{
	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////

	public static final ArrayList<XmlResourceReference> box(final ArrayList<XmlResource> roles)
	{
		if(roles == null)
		{
			return null;
		}

		final ArrayList<XmlResourceReference> names = new ArrayList<>(roles.size());

		for(final XmlResource role : roles)
		{
			names.add(new XmlResourceReference(role.name));
		}

		return names;
	}


	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	@XmlAttribute
	String name;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	public XmlResourceReference(final String name)
	{
		super();
		this.name = name;
	}

	// JAXB dummy constructor
	XmlResourceReference()
	{
		this(null);
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
