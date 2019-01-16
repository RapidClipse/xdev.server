/*
 * Copyright (C) 2013-2019 by XDEV Software, All Rights Reserved.
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

package com.xdev.security.authentication.ldap;


import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.activedirectory.ActiveDirectoryRealm;
import org.apache.shiro.realm.ldap.JndiLdapContextFactory;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.ldap.LdapUtils;
import org.apache.shiro.subject.PrincipalCollection;

import com.xdev.security.authentication.CredentialsUsernamePassword;


/**
 * Realm for use to authenticate and authorize a user against ActiveDirectory
 *
 * @author XDEV Software
 */
public class LDAPRealm extends ActiveDirectoryRealm implements AutoCloseable
{
	private final LdapContext ldapContext;


	@SuppressWarnings("unchecked")
	public LDAPRealm(final LDAPConfiguration configuration,
			final CredentialsUsernamePassword credentials) throws NamingException
	{
		final String url = configuration.getProviderUrl();
		final String suffix = configuration.getSuffix();
		final String username = credentials.username();
		final String password = new String(credentials.password());
		final String userNameWithSuffix = username + suffix;

		setUrl(url);
		setSystemUsername(username);
		setSystemPassword(password);
		setPrincipalSuffix(suffix);

		final String searchbase = configuration.getSearchbase();
		if(searchbase != null)
		{
			setSearchBase(searchbase);
		}

		final JndiLdapContextFactory contextFactory = new JndiLdapContextFactory();
		contextFactory.setUrl(url);
		contextFactory.getEnvironment().put(Context.SECURITY_PRINCIPAL,userNameWithSuffix);
		contextFactory.getEnvironment().put(Context.SECURITY_CREDENTIALS,password);
		
		final String securityAuthentication = configuration.getSecurityAuthentication();
		if(securityAuthentication != null)
		{
			contextFactory.getEnvironment().put(Context.SECURITY_AUTHENTICATION,
					securityAuthentication);
		}
		final String securityProtocol = configuration.getSecurityProtocol();
		if(securityProtocol != null)
		{
			contextFactory.getEnvironment().put(Context.SECURITY_PROTOCOL,securityProtocol);
		}

		setLdapContextFactory(contextFactory);

		this.ldapContext = contextFactory.getSystemLdapContext();
	}


	/**
	 * Builds an {@link org.apache.shiro.authz.AuthorizationInfo} object by
	 * querying the active directory LDAP context for the groups that a user is
	 * a member of. The groups are then translated to role names by using the
	 * configured {@link #groupRolesMap}.
	 * <p/>
	 * This implementation expects the <tt>principal</tt> argument to be a
	 * String username.
	 * <p/>
	 * Subclasses can override this method to determine authorization data
	 * (roles, permissions, etc) in a more complex way. Note that this default
	 * implementation does not support permissions, only groups (roles).
	 *
	 * @param principals
	 *            the principal of the Subject whose account is being retrieved.
	 * @param ldapContextFactory
	 *            the factory used to create LDAP connections.
	 * @return the AuthorizationInfo for the given Subject principal.
	 * @throws NamingException
	 *             if an error occurs when searching the LDAP server.
	 */
	@Override
	protected AuthorizationInfo queryForAuthorizationInfo(final PrincipalCollection principals,
			final LdapContextFactory ldapContextFactory) throws NamingException
	{
		final String username = (String)getAvailablePrincipal(principals);
		return buildAuthorizationInfo(getGroupNamesForUser(username));
	}


	/**
	 * Returns a Collection of all group names the user belongs to. Also nestet
	 * group names are returned.
	 *
	 * @param username
	 * @return Set with the names of the groups the user belongs to
	 * @throws NamingException
	 */
	public Set<String> getGroupNamesForUser(final String username) throws NamingException
	{
		Set<String> groupNames;
		groupNames = new LinkedHashSet<String>();

		final SearchControls searchCtls = new SearchControls();
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		String userPrincipalName = username;
		if(this.principalSuffix != null)
		{
			userPrincipalName += this.principalSuffix;
		}

		// SHIRO-115 - prevent potential code injection:
		final String searchFilter = "(&(objectClass=person)(userPrincipalName={0}))";
		final Object[] searchArguments = new Object[]{userPrincipalName};

		final NamingEnumeration<?> answer = this.ldapContext.search(this.searchBase,searchFilter,
				searchArguments,searchCtls);

		while(answer.hasMoreElements())
		{
			final SearchResult sr = (SearchResult)answer.next();

			final Attributes attrs = sr.getAttributes();

			if(attrs != null)
			{
				final NamingEnumeration<?> ae = attrs.getAll();
				while(ae.hasMore())
				{
					final Attribute attr = (Attribute)ae.next();

					if(attr.getID().equals("memberOf"))
					{
						final Collection<String> items = LdapUtils.getAllAttributeValues(attr);
						for(final String s : items)
						{
							groupNames.add(s.split(",")[0].substring(3));
							getMembersOf(s.split(",")[0].substring(3),groupNames,getSearchBase(s));
						}
					}
				}
			}
		}
		return groupNames;
	}


	/**
	 * searches recursively for groups where the given group is a member of and
	 * saves it to the given set.
	 *
	 * @param groupName
	 *            {@link String} the name of the group
	 * @param groupNames
	 *            {@link String} Set to save the found groups
	 * @param ldapContext
	 *            {@link LdapContext}
	 * @param searchBase
	 *            {@link String} searchBase where the given group is found
	 * @throws NamingException
	 */
	private void getMembersOf(final String groupName, final Set<String> groupNames,
			final String searchBase) throws NamingException
	{
		final String searchFilter = "(&(objectClass=group)(CN={0}))";
		final Object[] searchArguments = new Object[]{groupName};

		final SearchControls searchCtls = new SearchControls();
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		final NamingEnumeration<?> answer = this.ldapContext.search(searchBase,searchFilter,
				searchArguments,searchCtls);

		while(answer.hasMoreElements())
		{
			final SearchResult sr = (SearchResult)answer.next();

			final Attributes attrs = sr.getAttributes();

			if(attrs != null)
			{
				final NamingEnumeration<?> ae = attrs.getAll();
				while(ae.hasMore())
				{
					final Attribute attr = (Attribute)ae.next();

					if(attr.getID().equals("memberOf"))
					{
						final Collection<String> items = LdapUtils.getAllAttributeValues(attr);
						for(final String s : items)
						{
							groupNames.add(s.split(",")[0].substring(3));
							getMembersOf(s.split(",")[0].substring(3),groupNames,getSearchBase(s));
						}
					}
				}
			}
		}
	}


	/**
	 * extracts the searchBase from the distinguished name
	 *
	 * @param distinguished
	 *            {@link String}
	 * @return searchBase to use for ldap search
	 */
	private String getSearchBase(final String distinguished)
	{
		String base = "";
		final String[] s = distinguished.split(",");

		for(int i = 1; i < s.length - 2; i++)
		{
			base += s[i];
			if(i != (s.length - 3))
			{
				base += ",";
			}
		}

		return base;
	}


	public LdapContext getLdapContext()
	{
		return this.ldapContext;
	}


	@Override
	public void close()
	{
		LdapUtils.closeContext(this.ldapContext);
	}
}
