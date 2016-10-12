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
 *
 * For further information see
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.security.configuration.xml;

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
