/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;


/**
 * JAXB mapping type.
 *
 * @author XDEV Software (TM)
 */
public final class XmlPermission
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	@XmlAttribute
	String  resource;

	@XmlAttribute
	Integer factor  ;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	public XmlPermission(final XmlResource resource, final Integer factor)
	{
		super();
		this.resource = resource == null ?null :resource.name;
		this.factor   = factor  ;
	}

	// JAXB dummy constructor
	XmlPermission()
	{
		this(null, null);
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	public final String resource()
	{
		return this.resource;
	}

	public final Integer factor()
	{
		return this.factor;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public final String toString()
	{
		return this.resource+(this.factor == 0 ?"" :"("+this.factor+")");
	}

}
