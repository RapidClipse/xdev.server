/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xdev.security.authentication.ldap;


import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.xdev.security.authentication.AuthenticationFailedException;
import com.xdev.security.authentication.Authenticator;
import com.xdev.security.authentication.AuthenticatorProvider;
import com.xdev.security.authentication.CredentialsUsernamePassword;


/**
 * @author XDEV Software (JW)
 */

// TODO return type should be login meta data object
public final class LDAPAuthenticator
		implements Authenticator<CredentialsUsernamePassword, DirContext>
{

	// /////////////////////////////////////////////////////////////////////////
	// instance fields //
	// //////////////////

	final LDAPConfiguration	configuration;
	static DirContext		ldapContext;


	// /////////////////////////////////////////////////////////////////////////
	// constructors //
	// ///////////////

	public LDAPAuthenticator(final LDAPConfiguration configuration)
	{
		super();
		this.configuration = configuration;

	}


	// /////////////////////////////////////////////////////////////////////////
	// declared methods //
	// ///////////////////

	public final DirContext authenticate(final String username, final String password)
			throws AuthenticationFailedException
	{
		return this.authenticate(CredentialsUsernamePassword.New(username,password));
	}


	// /////////////////////////////////////////////////////////////////////////
	// override methods //
	// ///////////////////

	@Override
	public final DirContext authenticate(final CredentialsUsernamePassword credentials)
			throws AuthenticationFailedException
	{
		try
		{
			this.configuration.setPrincipal(credentials.username());
			this.configuration.setCredential(new String(credentials.password()));
			ldapContext = new InitialDirContext(
					this.configuration.getLdapEnviromentConfiguration());
			return ldapContext;
		}
		catch(final NamingException e)
		{
			throw new AuthenticationFailedException(e);
		}
	}



	/**
	 * An {@link LDAPAuthenticator}-specific provider type.
	 *
	 * @author XDEV Software (TM)
	 */
	public interface Provider extends AuthenticatorProvider<CredentialsUsernamePassword, DirContext>
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		public LDAPAuthenticator provideAuthenticator();
	}

}
