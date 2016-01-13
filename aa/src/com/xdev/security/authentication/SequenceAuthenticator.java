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

package com.xdev.security.authentication;


/**
 * An implementation of {@link Authenticator} that wraps a sequence of passed authenticators in a new instance.
 * For the exact behaviour, see {@link #authenticate(Object)}.
 *
 * @author XDEV Software (TM)
 *
 * @param <C> the type of the credentials instance to be authenticated.
 * @param <R> the type of the result/response instance to be returned upon an authentication attempt.
 */
public final class SequenceAuthenticator<C, R> implements Authenticator<C, R>
{
	///////////////////////////////////////////////////////////////////////////
	// static methods  //
	///////////////////

	/**
	 * Constructor method that does validation and implementation detail encapsulation of the actual constructor.
	 * <p>
	 * The passed array of {@link Authenticator} instances may neither be <tt>null</tt> nor empty.
	 * The created instance uses a copy of the passed array internally to avoid any later side effects.
	 *
	 * @param authenticators the authenticators to be evaluated in order.
	 * @return a new authenticator instance wrapping a sequence of other {@link Authenticator} instances.
	 * @throws IllegalArgumentException if the passed array is empty.
	 * @see #authenticate(Object)
	 */
	@SafeVarargs
	public static final <C, R> SequenceAuthenticator<C, R> New(final Authenticator<? super C, R>... authenticators)
		throws IllegalArgumentException
	{
		// at least one authenticator must be present
		if(authenticators.length < 1){
			throw new IllegalArgumentException();
		}

		// defensive copy of a usually tiny array, usually only once per application, is negligible.
		return new SequenceAuthenticator<>(authenticators.clone());
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	/**
	 * The sequence of {@link Authenticator} instances used by the {@link SequenceAuthenticator} instance.
	 */
	private final Authenticator<? super C, R>[] authenticators;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * Constructor might change in the future (maybe to using a collection), so it gets hidden as the implementation
	 * detail it is.
	 *
	 * @param authenticators the authenticators to be evaluated in order.
	 */
	SequenceAuthenticator(final Authenticator<? super C, R>[] authenticators)
	{
		super();
		this.authenticators = authenticators; // null-check to be done by implementation-private calling context.
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	/**
	 * Returns a defensive copy of the internally used authenticator sequence.
	 *
	 * @return the wrapped authenticators.
	 */
	public final Authenticator<? super C, R>[] getAuthenticators()
	{
		// defensive copy of a usually tiny array, usually never called, is negligible.
		return this.authenticators.clone();
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * Calling this method internally delegates to the same method of the passed authenticators in the exact same
	 * order as they have been passed at construction time. If an internally called authenticator produces one of
	 * the following results, the delegated authentication attempt is deemed a failure and the next authenticator
	 * in the sequence is used:
	 * <ul>
	 * <li>returned value of <tt>null</tt>.</li>
	 * <li>returned value equal to {@link Boolean#FALSE}.</li>
	 * <li>an {@link AuthenticationFailedException} occured.</li>
	 * </ul>
	 * If the last authenticator is reached, it is called directly without any wrapping logic,
	 * returning any value and passing any exception it might throw.
	 * <p>
	 * This effectively creates a multi-attempt {@link Authenticator} that succeed on the first success of one of its
	 * subsidiary authenticators.
	 *
	 * @param credentials the credentials to be evaluated for the authentication.
	 * @return the first return value not deemed to indicate a failure or whatever the last authenticator returns.
	 * @throws AuthenticationFailedException if the last authenticator throws an {@link AuthenticationFailedException}.
	 */
	@Override
	public final R authenticate(final C credentials) throws AuthenticationFailedException
	{
		// iterate over all non-last authenticators
		final int lastIndex = this.authenticators.length - 1;
		for(int i = 0; i < lastIndex; i++)
		{
			try{
				// first attempt to authenticate the passed credentials
				final R firstResult = this.authenticators[i].authenticate(credentials);
				if(firstResult != null && !Boolean.FALSE.equals(firstResult)){
					return firstResult;
				}
				// otherwise (null or FALSE), fall through to second attempt
			}
			catch(final AuthenticationFailedException e){
				// for a business-logical failed authentication, fall through to the next attempt.
			}
			catch(final Throwable t) {
				// pass through any other throwable
				throw t;
			}
		}

		// rely on last authenticator for the final attempt to prevent context-specific logic in here
		return this.authenticators[lastIndex].authenticate(credentials);
	}

}
