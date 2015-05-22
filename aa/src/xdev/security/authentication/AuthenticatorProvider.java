/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.authentication;


/**
 * Type that provides a usable {@link Authenticator} instance.
 *
 * @author XDEV Software (TM)
 *
 * @param <C> the type of the credentials instance to be authenticated.
 * @param <R> the type of the result/response instance to be returned upon an authentication attempt.
 */
public interface AuthenticatorProvider<C, R>
{
	/**
	 * Provides a usable {@link Authenticator} instance.
	 * Whether "providing" means creating a new instance or returning an existing one, maybe even always the same,
	 * is completely implementation-specific.
	 *
	 * @return a usable {@link Authenticator} instance
	 */
	public Authenticator<C, R> provideAuthenticator();
}
