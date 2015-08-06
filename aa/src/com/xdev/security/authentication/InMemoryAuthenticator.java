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

package com.xdev.security.authentication;


import java.util.Map;

import com.xdev.security.KeyValue;
import com.xdev.security.Util;


/**
 * Trivial in-memory implementation of an {@link Authenticator}.
 * <p>
 * Note that this implementation stores passwords in plain text in memory and is
 * therefor only suitable for examples or very trivial, non-security-critical
 * use cases.
 *
 * @author XDEV Software (TM)
 */
public final class InMemoryAuthenticator
		implements Authenticator<CredentialsUsernamePassword, Boolean>
{
	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////
	
	/**
	 * Creates a new {@link InMemoryAuthenticator} instance using a copy of the
	 * passed username/password map. The entries are interpreted as the keys
	 * representing the key and the associated value representing the associated
	 * password.
	 *
	 * @param usernamePasswords
	 *            the username/password data.
	 * @return a new {@link InMemoryAuthenticator} instance using the passed
	 *         data as a user inventory.
	 */
	public static final InMemoryAuthenticator New(final Map<String, String> usernamePasswords)
	{
		// immure to ensure immutability for central, potentially concurrently
		// used instance
		return new InMemoryAuthenticator(usernamePasswords);
	}
	
	
	/**
	 * Creates a new {@link InMemoryAuthenticator} instance using a copy of the
	 * passed username/password array. The entries are interpreted as the keys
	 * representing the key and the associated value representing the associated
	 * password.
	 *
	 * @param usernamePasswords
	 *            the username/password data.
	 * @return a new {@link InMemoryAuthenticator} instance using the passed
	 *         data as a user inventory.
	 */
	@SafeVarargs
	public static final InMemoryAuthenticator New(
			final KeyValue<String, String>... usernamePasswords)
	{
		return new InMemoryAuthenticator(Util.asMap(usernamePasswords));
	}
	
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////
	
	/**
	 * The internally used authentication map.
	 */
	final Map<String, String> usernamePasswords;
	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	
	/**
	 * Internal implementation-detail constructor.
	 *
	 * @param usernamePasswords
	 */
	InMemoryAuthenticator(final Map<String, String> usernamePasswords)
	{
		super();
		this.usernamePasswords = usernamePasswords;
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////
	
	public final boolean authenticate(final String username, final String password)
			throws AuthenticationFailedException
	{
		return this.authenticate(CredentialsUsernamePassword.New(username,password.getBytes()));
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////
	
	/**
	 * Returns {@link Boolean#FALSE} if the attempt to authenticate the passed
	 * credentials fails, {@link Boolean#TRUE} otherwise.
	 *
	 * @param credentials
	 *            the credentials instance to be authenticated.
	 * @return a non-null {@link Boolean} instance indicating the result of the
	 *         authentication attempt.
	 * @throws AuthenticationFailedException
	 *             never gets thrown in this simple implementation.
	 */
	@Override
	public final Boolean authenticate(final CredentialsUsernamePassword credentials)
			throws AuthenticationFailedException
	{
		// lookup stored password
		final String storedPassword = this.usernamePasswords.get(credentials.username());
		
		/*
		 * if no password found for the given username or passwords don't match,
		 * the authentication failed. Note that authentication logic does
		 * intentionally NOT give any clue about why username and password are
		 * not valid.
		 */
		return storedPassword != null && storedPassword.equals(credentials.password());
	}
	
	
	
	/**
	 * An {@link InMemoryAuthenticator}-specific provider type.
	 *
	 * @author XDEV Software (TM)
	 */
	public interface Provider extends AuthenticatorProvider<CredentialsUsernamePassword, Boolean>
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		public InMemoryAuthenticator provideAuthenticator();
	}
	
}
