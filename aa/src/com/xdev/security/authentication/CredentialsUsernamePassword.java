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

package com.xdev.security.authentication;


import static com.xdev.security.Utils.notNull;

import java.nio.charset.StandardCharsets;


/**
 * Trivial username / password string value tuple.
 *
 * @author XDEV Software (TM)
 */
public interface CredentialsUsernamePassword
{
	/**
	 * @return the username.
	 */
	public String username();


	/**
	 * @return the password.
	 */
	public byte[] password();


	/**
	 * Wraps the passed username and password strings as a new
	 * {@link CredentialsUsernamePassword} instance.
	 *
	 * @param username
	 *            the username to be wrapped.
	 * @param password
	 *            the password to be wrapped.
	 * @return a new {@link CredentialsUsernamePassword} instance containing the
	 *         passed credential values.
	 */
	public static CredentialsUsernamePassword.Implementation New(final String username,
			final byte[] password)
	{
		return new CredentialsUsernamePassword.Implementation(notNull(username),notNull(password));
	}


	/**
	 * Wraps the passed username and password strings as a new
	 * {@link CredentialsUsernamePassword} instance.
	 *
	 * @param username
	 *            the username to be wrapped.
	 * @param password
	 *            the password to be wrapped.
	 * @return a new {@link CredentialsUsernamePassword} instance containing the
	 *         passed credential values.
	 */
	public static CredentialsUsernamePassword.ImplementationString New(final String username,
			final String password)
	{
		return new CredentialsUsernamePassword.ImplementationString(notNull(username),
				notNull(password));
	}



	public final class ImplementationString implements CredentialsUsernamePassword
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		final String	username;
		final String	password;


		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		ImplementationString(final String username, final String password)
		{
			super();
			this.username = username;
			this.password = password;
		}


		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		@Override
		public String username()
		{
			return this.username;
		}


		@Override
		public byte[] password()
		{
			return this.password.getBytes(StandardCharsets.UTF_8);
		}


		@Override
		public String toString()
		{
			// intentionally don't give away the actual password.
			return this.username + " // (PWD length " + this.password.length() + ")";
		}

	}



	public final class Implementation implements CredentialsUsernamePassword
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		final String	username;
		final byte[]	password;


		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		Implementation(final String username, final byte[] password)
		{
			super();
			this.username = username;
			this.password = password;
		}


		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		@Override
		public String username()
		{
			return this.username;
		}


		@Override
		public byte[] password()
		{
			return this.password;
		}


		@Override
		public String toString()
		{
			// intentionally don't give away the actual password.
			return this.username + " // (PWD length " + this.password.length + ")";
		}

	}

}
