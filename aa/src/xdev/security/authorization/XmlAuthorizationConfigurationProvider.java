/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.authorization;

import static net.jadoth.Jadoth.notNull;

import java.io.File;
import java.util.List;

import net.jadoth.collections.EqHashEnum;
import net.jadoth.collections.EqHashTable;
import net.jadoth.util.chars.Named;
import xdev.security.configuration.xml.XmlConfiguration;
import xdev.security.configuration.xml.XmlPermission;
import xdev.security.configuration.xml.XmlResource;
import xdev.security.configuration.xml.XmlRole;
import xdev.security.configuration.xml.XmlSubject;

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
		final EqHashTable<String, EqHashEnum<String>>           resourceResources = EqHashTable.New();
		final EqHashTable<String, EqHashEnum<String>>           roleRoles         = EqHashTable.New();
		final EqHashTable<String, EqHashTable<String, Integer>> rolePermissions   = EqHashTable.New();
		final EqHashTable<String, EqHashEnum<String>>           subjectRoles      = EqHashTable.New();

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

	private static EqHashEnum<String> unboxNames(final List<? extends Named> nameds)
	{
		if(nameds == null)
		{
			return null;
		}

		final EqHashEnum<String> names = EqHashEnum.NewCustom(nameds.size());

		for(final Named named : nameds)
		{
			names.put(named.name());
		}

		return names;
	}

	private static EqHashTable<String, Integer> unboxPermissions(final List<XmlPermission> permissions)
	{
		if(permissions == null)
		{
			return null;
		}

		final EqHashTable<String, Integer> unboxed = EqHashTable.New();

		for(final XmlPermission permission : permissions)
		{
			unboxed.add(permission.resource(), permission.factor() == null ?0 :permission.factor());
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
