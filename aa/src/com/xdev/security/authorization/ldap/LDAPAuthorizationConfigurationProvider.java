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

package com.xdev.security.authorization.ldap;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;

import com.xdev.security.authentication.CredentialsUsernamePassword;
import com.xdev.security.authentication.ldap.LDAPConfiguration;
import com.xdev.security.authentication.ldap.LDAPRealm;
import com.xdev.security.authorization.AuthorizationConfiguration;
import com.xdev.security.authorization.AuthorizationConfigurationProvider;
import com.xdev.security.authorization.AuthorizationException;


/**
 *
 * @author XDEV Software (CK)
 */
public class LDAPAuthorizationConfigurationProvider implements AuthorizationConfigurationProvider
{
	private final LDAPConfiguration				configuration;
	private final CredentialsUsernamePassword	credentials;
	
	
	public LDAPAuthorizationConfigurationProvider(final LDAPConfiguration configuration,
			final CredentialsUsernamePassword credentials)
	{
		this.configuration = configuration;
		this.credentials = credentials;
	}


	/**
	 * @return the configuration
	 */
	public LDAPConfiguration getConfiguration()
	{
		return this.configuration;
	}
	
	
	/**
	 * @return the credentials
	 */
	public CredentialsUsernamePassword getCredentials()
	{
		return this.credentials;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AuthorizationConfiguration provideConfiguration()
	{
		try (final LDAPRealm realm = new LDAPRealm(this.configuration,this.credentials))
		{
			SecurityUtils.setSecurityManager(new DefaultSecurityManager(realm));

			final Map<String, Set<String>> resourceResources = new HashMap<String, Set<String>>();
			final Map<String, Set<String>> roleRoles = new HashMap<String, Set<String>>();
			final HashMap<String, HashMap<String, Integer>> rolePermissions = new HashMap<String, HashMap<String, Integer>>();
			final Map<String, Set<String>> subjectRoles = new HashMap<String, Set<String>>();

			final String username = this.credentials.username();
			final Set<String> groupNames = realm.getGroupNamesForUser(username);
			subjectRoles.put(username,groupNames);

			for(final String group : groupNames)
			{
				final HashMap<String, Integer> permissions = new HashMap<String, Integer>();
				permissions.put(group,1);
				rolePermissions.put(group,permissions);
				roleRoles.put(group,new HashSet<String>());
				resourceResources.put(group,new HashSet<String>());
			}

			return AuthorizationConfiguration.New(resourceResources,roleRoles,rolePermissions,
					subjectRoles);
		}
		catch(final NamingException e)
		{
			throw new AuthorizationException(e);
		}
	}
}
