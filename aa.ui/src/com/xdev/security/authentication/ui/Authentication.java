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

package com.xdev.security.authentication.ui;


import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.xdev.security.authorization.Subject;


public final class Authentication
{
	private Authentication()
	{
	}
	
	private final static String AUTHENTICATION_RESULT = "AUTHENTICATION_RESULT";
	
	
	public static Subject getUser()
	{
		return UI.getCurrent().getSession().getAttribute(Subject.class);
	}
	
	
	public static void setUser(final Subject user, final Object authenticationResult)
	{
		final VaadinSession session = UI.getCurrent().getSession();
		session.setAttribute(Subject.class,user);
		session.setAttribute(AUTHENTICATION_RESULT,authenticationResult);
	}
	
	
	public static boolean isUserLoggedIn()
	{
		return Authentication.getUser() != null;
	}
	
	
	public static Object getAuthenticationResult()
	{
		return UI.getCurrent().getSession().getAttribute(AUTHENTICATION_RESULT);
	}
	
	
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
