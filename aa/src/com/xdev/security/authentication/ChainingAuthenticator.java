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


import static com.xdev.security.Utils.notNull;


/**
 * Trivial {@link Authenticator} chaining implementation.
 *
 * See {@link Authenticator#then(Authenticator)}.
 *
 * @author XDEV Software (TM)
 *
 * @param <C>
 *            the type of the credentials instance to be authenticated.
 * @param <R>
 *            the type of the result/response instance to be returned upon an
 *            authentication attempt.
 */
public final class ChainingAuthenticator<C, R> implements Authenticator<C, R>
{
	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////

	/**
	 * Constructor method that does validation and implementation detail
	 * encapsulation of the actual constructor. Both passed
	 * {@link Authenticator} instance may not be null.
	 * <p>
	 * Not that while the first authenticator is always evaluated first, any
	 * {@link AuthenticationFailedException} it might throw will get swallowed
	 * intentionally to allow the second {@link Authenticator} to be evaluated
	 * as well.
	 *
	 * @param first
	 *            the first or "primary" {@link Authenticator} instance.
	 * @param second
	 *            the second or "fallback" {@link Authenticator} instance.
	 * @return a new {@link ChainingAuthenticator} instance wrapping the two
	 *         passed {@link Authenticator} instances.
	 */
	public static final <C, R> ChainingAuthenticator<C, R> New(final Authenticator<C, R> first,
			final Authenticator<C, R> second)
	{
		return new ChainingAuthenticator<>(notNull(first),notNull(second));
	}

	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	/**
	 * The first or "primary" {@link Authenticator} instance to be used in
	 * {@link #authenticate(Object)}.
	 */
	private final Authenticator<C, R> first;

	/**
	 * The second or "fallback" {@link Authenticator} instance to be used in
	 * {@link #authenticate(Object)}.
	 */
	private final Authenticator<C, R> second;


	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * Constructor might change in the future, so it gets hidden as the
	 * implementation detail it is.
	 *
	 * @param first
	 *            the first or "primary" {@link Authenticator} instance.
	 * @param second
	 *            the second or "fallback" {@link Authenticator} instance.
	 */
	ChainingAuthenticator(final Authenticator<C, R> first, final Authenticator<C, R> second)
	{
		super();
		this.first = first;
		this.second = second;
	}


	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * Tries to authenticate the credentials with the first wrapped
	 * authenticator. If and only if the first attempt fails, the second wrapped
	 * authenticator is used for a second authentication attempt.
	 * <p>
	 * The first attempt is assumed to have failed if any of the following
	 * conditions are met:
	 * <ol>
	 * <li>returned value of <tt>null</tt></li>
	 * <li>returned value equal to {@link Boolean#FALSE}</li>
	 * <li>an {@link AuthenticationFailedException} occured.</li>
	 * </ol>
	 *
	 * @param credentials
	 *            the credentials to be used in the authentication attempt.
	 * @return either the result of an successful first attempt or whatever the
	 *         second attempt returns.
	 *
	 * @throws AuthenticationFailedException
	 *             if the second authenticator throws one.
	 */
	@Override
	public final R authenticate(final C credentials) throws AuthenticationFailedException
	{
		/*
		 * There are three cases in which the second authentication attempt must
		 * be made: 1.) The first authenticator returns null Meaning no actual
		 * result instance could have been created. If the authenticator is
		 * properly used, is means the authencation has failed 2.) The first
		 * authenticator returns false This means the authenticator simply uses
		 * true/false and returns false for a failed attempt. It is kind of
		 * unclean to cover this case here in a generic piece of logic. However
		 * as the equals method is safe for such a case and the error
		 * probability is almost zero (i.e. it is hardly conceivable that an
		 * authenticator would return false for a successful authentication) it
		 * is still okay to cover this case in here. The alternative, not
		 * covering it, would mean that this chaining implementation could not
		 * be used for authenticators using a simple Boolean. 3.) A
		 * busines-logical AuthenticationFailedException occured in the first
		 * exception. Swallowing this exception is okay (more precisely:
		 * desired) as the second authenticator has the final say, anyway.
		 *
		 * If the assumptions made by this implementation prove to cause
		 * problems in exotic corner cases, it can always be replaced by a
		 * custom implemented implementation. The required code is rather tiny
		 * and trivial.
		 */
		try
		{
			// first attempt to authenticate the passed credentials
			final R firstResult = this.first.authenticate(credentials);
			if(firstResult != null && !Boolean.FALSE.equals(firstResult))
			{
				return firstResult;
			}
			// otherwise (null or FALSE), fall through to second attempt
		}
		catch(final AuthenticationFailedException e)
		{
			// for a business-logical failed authentication, fall through to the
			// second attempt.
		}
		catch(final Throwable t)
		{
			// pass through any other throwable
			throw t;
		}

		// second attempt (with "final say" characteristics). Returns whatever
		// the second authenticator returns
		return this.second.authenticate(credentials);
	}

}
