/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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


import java.time.Duration;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.xdev.communication.Cookies;


/**
 * Credentials memoizer. Mostly used for the 'remember me' or 'stay logged in'
 * feature of an application.
 * <p>
 * For example if the login form's 'stay logged in' checkbox is selected and the
 * authentication was successful, call {@link #remember(Object)}.
 * <p>
 * Important: Don't forget to call {@link #forget(Object)} when the logout is
 * done.
 * <p>
 * Before showing the login form to the user {@link #lookup()} can be called to
 * retrieve the last used login credentials.
 *
 * @author XDEV Software
 * @since 3.1
 */
public interface AuthenticationMemoizer<C>
{
	/**
	 * Remembers credentials. Best used after a successful authentication.
	 *
	 * @param credentials
	 *            the credentials to be remembered
	 */
	public void remember(C credentials);
	
	
	/**
	 * Forgets credentials. Best used after a successful logout.
	 *
	 * @param credentials
	 *            the credentials to forget
	 */
	public void forget(C credentials);
	
	
	/**
	 * Retrieves the last remembered credentials. Default is cookie based, see
	 * {@link CookieBasedAuthenticationMemoizer}.
	 *
	 * @return the last remembered credentials or <code>null</code>
	 */
	public C lookup();
	
	/**
	 * Default, cookie based, authentication memoizer.
	 */
	public final static AuthenticationMemoizer<CredentialsUsernamePassword> DEFAULT = new CookieBasedAuthenticationMemoizer();
	
	
	
	/**
	 * Cookie based implementation of {@link AuthenticationMemoizer}.
	 *
	 * @see Cookies
	 *
	 * @author XDEV Software
	 * @since 3.1
	 */
	public static class CookieBasedAuthenticationMemoizer
			implements AuthenticationMemoizer<CredentialsUsernamePassword>
	{
		protected final static String								COOKIE_NAME	= "XUID";
		protected final BiMap<String, CredentialsUsernamePassword>	rememberedCredentials;
		protected Duration											lifespan;
		
		
		public CookieBasedAuthenticationMemoizer()
		{
			this.rememberedCredentials = HashBiMap.create();
			this.lifespan = Duration.ofDays(14);
		}
		
		
		public Duration getLifespan()
		{
			return this.lifespan;
		}
		
		
		public void setLifespan(final Duration lifespan)
		{
			this.lifespan = lifespan;
		}
		
		
		@Override
		public void remember(final CredentialsUsernamePassword credentials)
		{
			remove(credentials);
			
			final String hash = UUID.randomUUID().toString();
			Cookies.getCurrent().setCookie(COOKIE_NAME,hash,this.lifespan);
			this.rememberedCredentials.put(hash,credentials);
		}
		
		
		@Override
		public void forget(final CredentialsUsernamePassword credentials)
		{
			remove(credentials);
			
			Cookies.getCurrent().deleteCookie(COOKIE_NAME);
		}
		
		
		protected void remove(final CredentialsUsernamePassword credentials)
		{
			final String hash = this.rememberedCredentials.inverse().get(credentials);
			if(hash != null)
			{
				this.rememberedCredentials.remove(hash);
			}
		}
		
		
		@Override
		public CredentialsUsernamePassword lookup()
		{
			final String hash = Cookies.getCurrent().getCookie(COOKIE_NAME);
			return this.rememberedCredentials.get(hash);
		}
	}
}
