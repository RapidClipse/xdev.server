/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;

import com.xdev.security.Named;

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
