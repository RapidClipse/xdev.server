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


/**
 * Generic authenticator type. The concrete usage depends on how the type
 * parameters are chosen for a specific requirement.
 * <p>
 * Type parameter C (credentials) can for example be
 * {@link CredentialsUsernamePassword}. It could also be a specific type of a
 * user or subject in order to authenticate already existing user instances.
 * <p>
 * Type parameter R (result) can be a Boolean for a simple information, if the
 * authentication succeeded or failed. It can also be a concrete user instance
 * in case a successful authentication has to create the user instance itself.
 *
 * @author XDEV Software (TM)
 *		
 * @param <C>
 *            the type of the credentials instance to be authenticated.
 * @param <R>
 *            the type of the result/response instance to be returned upon an
 *            authentication attempt.
 */
// @FunctionalInterface
public interface Authenticator<C, R> extends AuthenticatorLoginInfo
{

	/**
	 * Tries to authenticate the passed credentials and returns an apropriate
	 * result. The exact meaning of the result is implementation-dependant.
	 *
	 * @param credentials
	 *            the credentials to be authenticated
	 * @return the result of the authentication
	 * @throws AuthenticationFailedException
	 */
	public R authenticate(C credentials) throws AuthenticationFailedException;
	
	
	///////////////////////////////////////////////////////////////////////////
	// default methods //
	/////////////////////

	/**
	 * Chains the passed {@link Authenticator} instance to this one to be
	 * evaluated in case the evaluation of this one returns a negative result.
	 * <p>
	 * Note that multiple authenticators can be chained in sequence
	 *
	 * @param other
	 *            the authenticator instance to be chained.
	 * @return an authenticator instance that firstly attempts to evaluate this
	 *         authenticator and then the passed one if necessary.
	 */
	public default Authenticator<C, R> then(final Authenticator<C, R> other)
	{
		return new ChainingAuthenticator<>(this,other);
	}


	///////////////////////////////////////////////////////////////////////////
	// static methods //
	////////////////////

	/**
	 * Trivial helper method to compensate for the Java 8 lambda extension
	 * loophole that a lambda expression cannot be recognized without a target
	 * (variable declaration or method call).<br>
	 * Example:<br>
	 * {@code Authenticator
	 * <C,R> chained = ((c,r) -> debugAuth(c)).then((c,r) -> authenticate(c));}
	 * <br>
	 * With this trivial helper method, this is possible: {@code Authenticator
	 * <C,R>
	 *  chained = lambda((c,r) -> debugAuth(c)).then((c,r) -> authenticate(c));}
	 * <br>
	 *
	 * <p>
	 * Trivially always returns the passed instance ({@code return lambda;}.
	 * <p>
	 * Note that trivial static methods like this get eliminated (inlined) by
	 * the JIT compiler at runtime, so this convenience method does NOT come
	 * with a performance penalty.
	 *
	 * @param lambda
	 *            the lambda instance to make recognizable to the compiler.
	 * @return lambda
	 */
	public static <C, R> Authenticator<C, R> lambda(final Authenticator<C, R> lambda)
	{
		return lambda;
	}


	/**
	 * Returns a new {@link Authenticator} instance that wraps a sequence of
	 * passed authenticators. For further details, see
	 * {@link SequenceAuthenticator}.
	 *
	 * @param authenticators
	 *            the {@link Authenticator} instances sequence to be wrapped.
	 * @return a wrapping {@link SequenceAuthenticator} instance.
	 */
	@SafeVarargs
	public static <C, R> Authenticator<C, R> sequence(
			final Authenticator<? super C, R>... authenticators)
	{
		return SequenceAuthenticator.New(authenticators);
	}

}
