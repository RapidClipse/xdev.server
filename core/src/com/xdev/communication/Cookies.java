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

package com.xdev.communication;


import java.time.Duration;

import javax.servlet.http.Cookie;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.JavaScript;


/**
 * Utility class to write and read browser cookies.
 * <p>
 * Use {@link #getCurrent()} to get the session's instance.
 *
 * @author XDEV Software
 * @since 3.1
 */
public final class Cookies
{
	public static Cookies getCurrent()
	{
		return VaadinSession.getCurrent().getAttribute(Cookies.class);
	}
	
	private Cookie[] cookies;
	
	
	Cookies(final VaadinSession vs)
	{
		vs.addRequestHandler((session, request, response) -> {
			
			this.cookies = request.getCookies();
			
			return false;
		});
	}
	
	
	public void setCookie(final String key, final String value)
	{
		setCookie(key,value,"/",null);
	}
	
	
	public void setCookie(final String key, final String value, final Duration lifespan)
	{
		setCookie(key,value,"/",lifespan);
	}
	
	
	public void setCookie(final String key, final String value, final String path)
	{
		setCookie(key,value,path,null);
	}
	
	
	public void setCookie(final String key, final String value, final String path,
			final Duration lifespan)
	{
		final long millis = lifespan != null ? lifespan.toMillis() : 0L;
		final String js = String.format("var millis=%s;" + "var expires=\"\";\n"
				+ "if(millis>0){var date = new Date();date.setTime(date.getTime()+millis);"
				+ "expires=\";expires=\"+date.toGMTString();}\n"
				+ "document.cookie=\"%s=%s;path=%s\"+expires;",millis,key,value,path);
		JavaScript.getCurrent().execute(js);
	}
	
	
	public void deleteCookie(final String key)
	{
		setCookie(key,"");
	}
	
	
	public String getCookie(final String key)
	{
		final Cookie[] cookies = this.cookies;
		if(cookies != null)
		{
			for(final Cookie cookie : cookies)
			{
				if(cookie.getName().equals(key))
				{
					return cookie.getValue();
				}
			}
		}
		
		return null;
	}
}
