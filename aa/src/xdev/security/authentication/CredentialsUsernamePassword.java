/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.authentication;

import static net.jadoth.Jadoth.notNull;


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
	public String password();



	/**
	 * Wraps the passed username and password strings as a new {@link CredentialsUsernamePassword} instance.
	 *
	 * @param  username the username to be wrapped.
	 * @param  password the password to be wrapped.
	 * @return a new {@link CredentialsUsernamePassword} instance containing the passed credential values.
	 */
	public static CredentialsUsernamePassword.Implementation New(final String username, final String password)
	{
		return new CredentialsUsernamePassword.Implementation(
			notNull(username),
			notNull(password)
		);
	}



	public final class Implementation implements CredentialsUsernamePassword
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		final String username;
		final String password;



		///////////////////////////////////////////////////////////////////////////
		// constructors //
		/////////////////

		Implementation(final String username, final String password)
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
		public String password()
		{
			return this.password;
		}

		@Override
		public String toString()
		{
			// intentionally don't give away the actual password.
			return this.username + " // (PWD length "+this.password.length()+")";
		}

	}

}
