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
 */

package com.xdev.security.authorization.db;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.xdev.dal.DAOs;
import com.xdev.security.authorization.AuthorizationConfiguration;
import com.xdev.security.authorization.AuthorizationConfigurationProvider;


/**
 *
 * @author XDEV Software (CK)
 */
public class DBAuthorizationConfigurationProvider implements AuthorizationConfigurationProvider
{
	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////
	
	public static final AuthorizationConfiguration build(
			final Class<? extends AuthorizationSubject> subjectEntityType,
			final Class<? extends AuthorizationRole> roleEntityType,
			final Class<? extends AuthorizationResource> resourceEntityType)
	{
		final Map<String, Set<String>> resourceResources = new HashMap<String, Set<String>>();
		final Map<String, Set<String>> roleRoles = new HashMap<String, Set<String>>();
		final Map<String, Map<String, Integer>> rolePermissions = new HashMap<String, Map<String, Integer>>();
		final Map<String, Set<String>> subjectRoles = new HashMap<String, Set<String>>();
		
		final List<? extends AuthorizationSubject> subjects = DAOs
				.getByEntityType(subjectEntityType).findAll();
		final List<? extends AuthorizationRole> roles = DAOs.getByEntityType(roleEntityType)
				.findAll();
		final List<? extends AuthorizationResource> resources = DAOs
				.getByEntityType(resourceEntityType).findAll();
				
		for(final AuthorizationSubject subject : subjects)
		{
			subjectRoles.put(subject.name(),unboxRoles(subject.roles()));
		}
		
		for(final AuthorizationRole role : roles)
		{
			rolePermissions.put(role.name(),unboxResources(role.resources()));
			roleRoles.put(role.name(),unboxRoles(role.roles()));
		}
		
		for(final AuthorizationResource resource : resources)
		{
			resourceResources.put(resource.resourceName(),new HashSet<String>());
		}
		
		return AuthorizationConfiguration.New(resourceResources,roleRoles,rolePermissions,
				subjectRoles);
	}
	
	
	private static Set<String> unboxRoles(final Collection<? extends AuthorizationRole> roles)
	{
		if(roles == null)
		{
			return null;
		}
		
		return roles.stream().map(AuthorizationRole::name).collect(Collectors.toSet());
	}
	
	
	private static Map<String, Integer> unboxResources(
			final Collection<? extends AuthorizationResource> resources)
	{
		if(resources == null)
		{
			return null;
		}
		
		return resources.stream()
				.collect(Collectors.toMap(AuthorizationResource::resourceName,r -> 1));
	}
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	
	private final Class<? extends AuthorizationSubject>		subjectEntityType;
	private final Class<? extends AuthorizationRole>		roleEntityType;
	private final Class<? extends AuthorizationResource>	resourceEntityType;
															
															
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	
	public DBAuthorizationConfigurationProvider(
			final Class<? extends AuthorizationSubject> subjectEntityType,
			final Class<? extends AuthorizationRole> roleEntityType,
			final Class<? extends AuthorizationResource> resourceEntityType)
	{
		this.subjectEntityType = subjectEntityType;
		this.roleEntityType = roleEntityType;
		this.resourceEntityType = resourceEntityType;
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
		return build(this.subjectEntityType,this.roleEntityType,this.resourceEntityType);
	}
}
