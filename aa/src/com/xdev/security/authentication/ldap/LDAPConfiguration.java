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

package com.xdev.security.authentication.ldap;


import java.util.Hashtable;

import com.xdev.security.authentication.CredentialsUsernamePassword;


/**
 *
 * @author XDEV Software
 */
public class LDAPConfiguration
{
	public static class LDAPConfigurationBuilder
	{
		private final String	providerUrl;

		// Optional
		private String			suffix	= "";
		private String			searchbase;
		private String			securityAuthentication;
		private String			securityProtocol;


		public LDAPConfigurationBuilder(final String providerUrl)
		{
			this.providerUrl = providerUrl;
		}


		public LDAPConfigurationBuilder suffix(final String suffix)
		{
			this.suffix = suffix;
			return this;
		}


		public LDAPConfigurationBuilder searchBase(final String searchBase)
		{
			this.searchbase = searchBase;
			return this;
		}


		public LDAPConfigurationBuilder securityAuthentication(final String securityAuthentication)
		{
			this.securityAuthentication = securityAuthentication;
			return this;
		}


		public LDAPConfigurationBuilder securityProtocol(final String securityProtocol)
		{
			this.securityProtocol = securityProtocol;
			return this;
		}


		/**
		 * @deprecated not used anymore, will be removed in a future release
		 */
		@Deprecated
		public LDAPConfigurationBuilder contextFactory(final String contextFactory)
		{
			return this;
		}


		public LDAPConfiguration build()
		{
			return new LDAPConfiguration(this);
		}
	}

	private final String	providerUrl;

	// Optional
	private final String	suffix;
	private final String	searchbase;
	private final String	securityAuthentication;
	private final String	securityProtocol;


	/**
	 *
	 */
	private LDAPConfiguration(final LDAPConfigurationBuilder builder)
	{
		this.providerUrl = builder.providerUrl;

		// optionals
		this.suffix = builder.suffix;
		this.searchbase = builder.searchbase;
		this.securityAuthentication = builder.securityAuthentication;
		this.securityProtocol = builder.securityProtocol;
	}


	public String getSuffix()
	{
		return this.suffix;
	}


	public String getProviderUrl()
	{
		return this.providerUrl;
	}


	public String getSearchbase()
	{
		return this.searchbase;
	}


	public String getSecurityAuthentication()
	{
		return this.securityAuthentication;
	}


	public String getSecurityProtocol()
	{
		return this.securityProtocol;
	}


	///////////////////////////////////////////////////////////////////////////
	// Deprected stuff //
	///////////////////

	/**
	 * @deprecated replaced with {@link CredentialsUsernamePassword}, will be
	 *             removed in a future release
	 */
	@Deprecated
	public void setPrincipal(final String principal)
	{
	}


	/**
	 * @deprecated replaced with {@link CredentialsUsernamePassword}, will be
	 *             removed in a future release
	 */
	@Deprecated
	public void setCredential(final String credential)
	{
	}


	/**
	 * @deprecated replaced with {@link CredentialsUsernamePassword}, will be
	 *             removed in a future release
	 */
	@Deprecated
	public String getPrincipal()
	{
		return null;
	}


	/**
	 * @deprecated replaced with {@link CredentialsUsernamePassword}, will be
	 *             removed in a future release
	 */
	@Deprecated
	public String getCredential()
	{
		return null;
	}


	/**
	 * @deprecated not used anymore, will be removed in a future release
	 */
	@Deprecated
	public String getContextFactory()
	{
		return null;
	}


	/**
	 * @deprecated not used anymore, will be removed in a future release
	 */
	@Deprecated
	public Hashtable<String, String> getLdapEnviromentConfiguration()
	{
		return null;
	}
}
