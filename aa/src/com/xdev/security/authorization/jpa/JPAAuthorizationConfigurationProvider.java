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

package com.xdev.security.authorization.jpa;


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
public class JPAAuthorizationConfigurationProvider implements AuthorizationConfigurationProvider
{
	private final Class<? extends AuthorizationSubject>		subjectEntityType;
	private final Class<? extends AuthorizationRole>		roleEntityType;
	private final Class<? extends AuthorizationResource>	resourceEntityType;


	public JPAAuthorizationConfigurationProvider(
			final Class<? extends AuthorizationSubject> subjectEntityType,
			final Class<? extends AuthorizationRole> roleEntityType,
			final Class<? extends AuthorizationResource> resourceEntityType)
	{
		this.subjectEntityType = subjectEntityType;
		this.roleEntityType = roleEntityType;
		this.resourceEntityType = resourceEntityType;
	}


	/**
	 * @return the subjectEntityType
	 */
	public Class<? extends AuthorizationSubject> getSubjectEntityType()
	{
		return this.subjectEntityType;
	}
	
	
	/**
	 * @return the roleEntityType
	 */
	public Class<? extends AuthorizationRole> getRoleEntityType()
	{
		return this.roleEntityType;
	}
	
	
	/**
	 * @return the resourceEntityType
	 */
	public Class<? extends AuthorizationResource> getResourceEntityType()
	{
		return this.resourceEntityType;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public AuthorizationConfiguration provideConfiguration()
	{
		final Map<String, Set<String>> resourceResources = new HashMap<String, Set<String>>();
		final Map<String, Set<String>> roleRoles = new HashMap<String, Set<String>>();
		final Map<String, Map<String, Integer>> rolePermissions = new HashMap<String, Map<String, Integer>>();
		final Map<String, Set<String>> subjectRoles = new HashMap<String, Set<String>>();

		final List<? extends AuthorizationSubject> subjects = getSubjects();
		final List<? extends AuthorizationRole> roles = getRoles();
		final List<? extends AuthorizationResource> resources = getResources();

		for(final AuthorizationSubject subject : subjects)
		{
			subjectRoles.put(subject.subjectName(),unboxRoles(subject.roles()));
		}

		for(final AuthorizationRole role : roles)
		{
			rolePermissions.put(role.roleName(),unboxResources(role.resources()));
			roleRoles.put(role.roleName(),unboxRoles(role.roles()));
		}

		for(final AuthorizationResource resource : resources)
		{
			resourceResources.put(resource.resourceName(),new HashSet<String>());
		}

		return AuthorizationConfiguration.New(resourceResources,roleRoles,rolePermissions,
				subjectRoles);
	}


	protected List<? extends AuthorizationSubject> getSubjects()
	{
		return DAOs.getByEntityType(getSubjectEntityType()).findAll();
	}


	protected List<? extends AuthorizationRole> getRoles()
	{
		return DAOs.getByEntityType(getRoleEntityType()).findAll();
	}
	
	
	protected List<? extends AuthorizationResource> getResources()
	{
		return DAOs.getByEntityType(getResourceEntityType()).findAll();
	}


	protected Set<String> unboxRoles(final Collection<? extends AuthorizationRole> roles)
	{
		if(roles == null)
		{
			return null;
		}

		return roles.stream().map(AuthorizationRole::roleName).collect(Collectors.toSet());
	}


	protected Map<String, Integer> unboxResources(
			final Collection<? extends AuthorizationResource> resources)
	{
		if(resources == null)
		{
			return null;
		}

		return resources.stream()
				.collect(Collectors.toMap(AuthorizationResource::resourceName,r -> 1));
	}
}
