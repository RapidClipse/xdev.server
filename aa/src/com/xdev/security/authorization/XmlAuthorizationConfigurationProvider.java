/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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

package com.xdev.security.authorization;

import static com.xdev.security.Utils.notNull;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.xdev.security.Named;
import com.xdev.security.configuration.xml.XmlConfiguration;
import com.xdev.security.configuration.xml.XmlPermission;
import com.xdev.security.configuration.xml.XmlResource;
import com.xdev.security.configuration.xml.XmlRole;
import com.xdev.security.configuration.xml.XmlSubject;

public class XmlAuthorizationConfigurationProvider implements AuthorizationConfigurationProvider
{
	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////

	public static final AuthorizationConfiguration readConfiguration(final File xmlFile) throws AuthorizationException
	{
		final XmlConfiguration xmlConfig = XmlConfiguration.readFromFile(xmlFile);
		return build(xmlConfig);
	}


	public static final AuthorizationConfiguration build(final XmlConfiguration xmlConfig)
	{
		final HashMap<String, HashSet<String>>          resourceResources = new HashMap<>();
		final HashMap<String, HashSet<String>>          roleRoles         = new HashMap<>();
		final HashMap<String, HashMap<String, Integer>> rolePermissions   = new HashMap<>();
		final HashMap<String, HashSet<String>>          subjectRoles      = new HashMap<>();

		for(final XmlResource resource : xmlConfig.resources())
		{
			resourceResources.put(resource.name(), unboxNames(resource.children()));
		}

		for(final XmlRole role : xmlConfig.roles())
		{
			// must put role name even if associated collections are null to register the role itself
			roleRoles.put(role.name(), unboxNames(role.roles()));
			rolePermissions.put(role.name(), unboxPermissions(role.permissions()));
		}

		for(final XmlSubject subject : xmlConfig.subjects())
		{
			subjectRoles.put(subject.name(), unboxNames(subject.roles()));
		}

		// (25.06.2014 TM)TODO: validate referential integrity at string level here?

		return AuthorizationConfiguration.New(resourceResources, roleRoles, rolePermissions, subjectRoles);
	}

	private static HashSet<String> unboxNames(final List<? extends Named> nameds)
	{
		if(nameds == null)
		{
			return null;
		}

		final HashSet<String> names = new HashSet<>(nameds.size());

		for(final Named named : nameds)
		{
			names.add(named.name());
		}

		return names;
	}

	private static HashMap<String, Integer> unboxPermissions(final List<XmlPermission> permissions)
	{
		if(permissions == null)
		{
			return null;
		}

		final HashMap<String, Integer> unboxed = new HashMap<>();

		for(final XmlPermission permission : permissions)
		{
			unboxed.put(permission.resource(), permission.factor() == null ?0 :permission.factor());
		}

		return unboxed;
	}


	public static final XmlAuthorizationConfigurationProvider New(final File xmlFile)
	{
		return new XmlAuthorizationConfigurationProvider(
			notNull(xmlFile)
		);
	}


	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	private final File xmlFile;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * Implementation detail constructor that might change in the future.
	 */
	XmlAuthorizationConfigurationProvider(final File xmlFile)
	{
		super();
		this.xmlFile = xmlFile;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AuthorizationConfiguration provideConfiguration()
	{
		return readConfiguration(this.xmlFile);
	}

}
