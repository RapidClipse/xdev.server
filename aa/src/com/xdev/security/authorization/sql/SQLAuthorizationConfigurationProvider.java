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

package com.xdev.security.authorization.sql;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
		final Map<String, Set<String>> resourceResources = new HashMap<>();
		final Map<String, Set<String>> roleRoles = new HashMap<>();
		final Map<String, Map<String, Integer>> rolePermissions = new HashMap<>();
		final Map<String, Set<String>> subjectRoles = new HashMap<>();

		final Query usersAndGroupsQuery = EntityManagerUtils.getEntityManager()
				.createNativeQuery(usersAndGroupsSelect);
		final List<Object[]> usersAndGroupsList = usersAndGroupsQuery.getResultList();

		usersAndGroupsList.stream().map(row -> String.valueOf(row[0]))
				.filter(user -> !subjectRoles.containsKey(user))
				.forEach(user -> subjectRoles.put(user,unboxGroups(user,usersAndGroupsList)));

		final Query rolesAndPermissionsQuery = EntityManagerUtils.getEntityManager()
				.createNativeQuery(rolesAndPermissionsSelect);
		final List<Object[]> rolesAndPermissionsList = rolesAndPermissionsQuery.getResultList();

		rolesAndPermissionsList.stream().map(row -> String.valueOf(row[0]))
				.filter(role -> !rolePermissions.containsKey(role)).forEach(role -> rolePermissions
						.put(role,unboxPermissions(role,rolesAndPermissionsList)));

		rolePermissions.forEach((role, permissions) -> {
			roleRoles.put(role,Collections.EMPTY_SET);
			permissions.keySet()
					.forEach(permission -> resourceResources.put(permission,Collections.EMPTY_SET));
		});

		subjectRoles.values()
				.forEach(roles -> roles.forEach(role -> roleRoles.put(role,Collections.EMPTY_SET)));

		return AuthorizationConfiguration.New(resourceResources,roleRoles,rolePermissions,
				subjectRoles);
	}


	private static Set<String> unboxGroups(final String username, final List<Object[]> rows)
	{
		return rows.stream().filter(row -> username.equals(row[0])).map(row -> (String)row[1])
				.collect(Collectors.toSet());
	}


	private static Map<String, Integer> unboxPermissions(final String groupName,
			final List<Object[]> rows)
	{
		return rows.stream().filter(row -> groupName.equals(row[0])).map(row -> (String)row[1])
				.collect(Collectors.toMap(permission -> permission,permission -> 1));
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
