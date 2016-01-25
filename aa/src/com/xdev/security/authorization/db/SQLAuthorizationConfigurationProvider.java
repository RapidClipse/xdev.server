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


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import com.xdev.communication.EntityManagerUtils;
import com.xdev.security.authorization.AuthorizationConfiguration;
import com.xdev.security.authorization.AuthorizationConfigurationProvider;


/**
 *
 * @author XDEV Software (CK)
 */
public class SQLAuthorizationConfigurationProvider implements AuthorizationConfigurationProvider
{
	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////
	@SuppressWarnings("unchecked")
	public static final AuthorizationConfiguration build(final String usersAndGroupsSelect,
			final String rolesAndPermissionsSelect)
	{
		final Map<String, Set<String>> resourceResources = new HashMap<String, Set<String>>();
		final Map<String, Set<String>> roleRoles = new HashMap<String, Set<String>>();
		final HashMap<String, HashMap<String, Integer>> rolePermissions = new HashMap<String, HashMap<String, Integer>>();
		final Map<String, Set<String>> subjectRoles = new HashMap<String, Set<String>>();
		
		final Query usersAndGroupsQuery = EntityManagerUtils.getEntityManager()
				.createNativeQuery(usersAndGroupsSelect);
		final List<Object[]> usersAndGroupsList = usersAndGroupsQuery.getResultList();
		
		for(final Object[] row : usersAndGroupsList)
		{
			final String user = String.valueOf(row[0]);
			if(!subjectRoles.containsKey(user))
			{
				subjectRoles.put(user,unboxGroups(user,usersAndGroupsList));
			}
		}
		
		final Query rolesAndPermissionsQuery = EntityManagerUtils.getEntityManager()
				.createNativeQuery(rolesAndPermissionsSelect);
		final List<Object[]> rolesAndPermissionsList = rolesAndPermissionsQuery.getResultList();
		
		for(final Object[] row : rolesAndPermissionsList)
		{
			final String role = String.valueOf(row[0]);
			if(!rolePermissions.containsKey(role))
			{
				rolePermissions.put(role,unboxPermissions(role,rolesAndPermissionsList));
			}
		}
		
		for(final String role : rolePermissions.keySet())
		{
			final Set<String> childSet = new HashSet<String>();
			childSet.add(role);
			
			roleRoles.put(role,childSet);
		}
		
		for(final HashMap<String, Integer> rolePermission : rolePermissions.values())
		{
			for(final String permission : rolePermission.keySet())
			{
				if(!resourceResources.containsKey(permission))
				{
					final Set<String> childSet = new HashSet<String>();
					childSet.add(permission);
					resourceResources.put(permission,childSet);
				}
			}
		}
		return AuthorizationConfiguration.New(resourceResources,roleRoles,rolePermissions,
				subjectRoles);
	}
	
	
	private static Set<String> unboxGroups(final String username, final List<Object[]> rows)
	{
		final Set<String> groups = new HashSet<String>();
		
		for(final Object[] row : rows)
		{
			if(username.equals(row[0]))
			{
				groups.add((String)row[1]);
			}
		}
		
		return groups;
	}
	
	
	private static HashMap<String, Integer> unboxPermissions(final String groupName,
			final List<Object[]> rows)
	{
		final HashMap<String, Integer> permissions = new HashMap<String, Integer>();
		for(final Object[] row : rows)
		{
			if(groupName.equals(row[0]))
			{
				permissions.put((String)row[1],1);
			}
		}
		return permissions;
	}
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	
	private final String	usersAndGroupsSelect;
	private final String	rolesAndPermissionsSelect;
							
							
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	
	public SQLAuthorizationConfigurationProvider(final String usersAndGroupsSelect,
			final String rolesAndPermissionsSelect)
	{
		this.usersAndGroupsSelect = usersAndGroupsSelect;
		this.rolesAndPermissionsSelect = rolesAndPermissionsSelect;
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
		return build(this.usersAndGroupsSelect,this.rolesAndPermissionsSelect);
	}
}
