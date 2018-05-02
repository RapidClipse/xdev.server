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

package com.xdev.security.authentication.ui;


import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.xdev.security.authentication.AuthenticationFailedException;
import com.xdev.security.authentication.AuthenticatorProvider;
import com.xdev.security.authentication.CredentialsUsernamePassword;
import com.xdev.security.authorization.AuthorizationConfigurationProvider;
import com.xdev.security.authorization.AuthorizationManager;
import com.xdev.security.authorization.Subject;
import com.xdev.security.authorization.ui.Authorization;


/**
 * Utility class for authentication purposes.
 *
 * @author XDEV Software
 *
 */
public final class Authentication
{
	private Authentication()
	{
	}
	
	private final static String AUTHENTICATION_RESULT = "AUTHENTICATION_RESULT";
	
	
	/**
	 * A login with the given credentials is attempted. If successful the user is
	 * registered in the current session and then the redirect view will be shown.
	 *
	 * @return <code>true</code> if the login was successful
	 *
	 * @see #login(Subject, Object)
	 *
	 * @since 3.1
	 */
	public static boolean tryLogin(final CredentialsUsernamePassword credentials,
			final AuthenticatorProvider<CredentialsUsernamePassword, ?> authenticatorProvider)
	{
		return tryLogin(credentials,authenticatorProvider,null);
	}
	
	
	/**
	 * A login with the given credentials is attempted. If successful the user is
	 * registered in the current session and then the redirect view will be shown.
	 *
	 * @return <code>true</code> if the login was successful
	 *
	 * @see #login(Subject, Object)
	 *
	 * @since 3.1
	 */
	public static boolean tryLogin(final CredentialsUsernamePassword credentials,
			final AuthenticatorProvider<CredentialsUsernamePassword, ?> authenticatorProvider,
			final AuthorizationConfigurationProvider authorizationConfigurationProvider)
	{
		try
		{
			final Object authenticationResult = authenticatorProvider.provideAuthenticator()
					.authenticate(credentials);
			Subject subject;
			if(authorizationConfigurationProvider != null)
			{
				final AuthorizationManager authorizationManager = AuthorizationManager
						.New(authorizationConfigurationProvider);
				Authorization.setAuthorizationManager(authorizationManager);
				subject = authorizationManager.subject(credentials.username());
			}
			else
			{
				subject = new Subject.Implementation(credentials.username());
			}
			login(subject,authenticationResult);
			return true;
		}
		catch(final AuthenticationFailedException e)
		{
			return false;
		}
	}
	
	
	/**
	 * Registers the <code>user</code> with the <code>authenticationResult</code> in
	 * the current session and navigates to the redirect view.
	 *
	 * @param user
	 *            the current user
	 * @param authenticationResult
	 *            the authentication's result
	 *
	 * @see #setUser(Subject, Object)
	 * @see #navigateToRedirectView()
	 */
	public static void login(final Subject user, final Object authenticationResult)
	{
		setUser(user,authenticationResult);
		navigateToRedirectView();
	}
	
	
	/**
	 * Removes the current user from the current session and redirects to the login
	 * view.
	 *
	 * @see #setUser(Subject, Object)
	 * @see #navigateToLoginView()
	 */
	public static void logout()
	{
		setUser(null,null);
		navigateToLoginView();
	}
	
	
	/**
	 * Registers the <code>user</code> with the <code>authenticationResult</code> in
	 * the current session.
	 *
	 * @param user
	 *            the current user
	 * @param authenticationResult
	 *            the authentication's result
	 *
	 * @see #getUser()
	 */
	public static void setUser(final Subject user, final Object authenticationResult)
	{
		final VaadinSession session = UI.getCurrent().getSession();
		session.setAttribute(Subject.class,user);
		session.setAttribute(AUTHENTICATION_RESULT,authenticationResult);
	}
	
	
	/**
	 * Returns the current user, which was registered with
	 * {@link #setUser(Subject, Object)}.
	 *
	 * @return the current user or <code>null</code> if no user is currently
	 *         registered.
	 */
	public static Subject getUser()
	{
		return UI.getCurrent().getSession().getAttribute(Subject.class);
	}
	
	
	/**
	 * Returns the result of the last authentification of
	 * {@link #setUser(Subject, Object)}.
	 *
	 * @return the current authentication result or <code>null</code> if no user is
	 *         currently registered.
	 */
	public static Object getAuthenticationResult()
	{
		return UI.getCurrent().getSession().getAttribute(AUTHENTICATION_RESULT);
	}
	
	
	/**
	 * Returns <code>true</code> if a user is registered in the current session,
	 * <code>false</code> otherwise.
	 *
	 * @return the current user
	 *
	 * @see #setUser(Subject, Object)
	 * @see #login(Subject, Object)
	 * @see #logout()
	 */
	public static boolean isUserLoggedIn()
	{
		return getUser() != null;
	}
	
	
	/**
	 * Navigates to the application's {@link LoginView}.
	 *
	 * @see XdevAuthenticationNavigator#setLoginViewName(String)
	 * @see LoginView
	 * @see #logout()
	 */
	public static void navigateToLoginView()
	{
		final Navigator navigator = UI.getCurrent().getNavigator();
		if(navigator instanceof XdevAuthenticationNavigator)
		{
			((XdevAuthenticationNavigator)navigator).navigateToLoginView();
		}
		else
		{
			throw new IllegalStateException(
					"Navigating to login view requires XDEVAuthenticationNavigator");
		}
	}
	
	
	/**
	 * Navigates to the application's redirect view.
	 * <p>
	 * Used after a successful login.
	 *
	 * @see XdevAuthenticationNavigator#setRedirectViewName(String)
	 * @see #login(Subject, Object)
	 */
	public static void navigateToRedirectView()
	{
		final Navigator navigator = UI.getCurrent().getNavigator();
		if(navigator instanceof XdevAuthenticationNavigator)
		{
			((XdevAuthenticationNavigator)navigator).navigateToRedirectView();
		}
		else
		{
			throw new IllegalStateException(
					"Navigating to redirect view requires XDEVAuthenticationNavigator");
		}
	}
}
