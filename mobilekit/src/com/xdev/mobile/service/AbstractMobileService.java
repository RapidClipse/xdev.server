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

package com.xdev.mobile.service;


import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.UI;
import com.xdev.mobile.config.MobileServiceConfiguration;

import elemental.json.JsonArray;


/**
 * @author XDEV Software
 * 		
 */
public abstract class AbstractMobileService extends AbstractJavaScriptExtension
{
	/**
	 * Returns a registered service.
	 *
	 * Services are registered in the mobile.xml configuration file. Example:
	 *
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <mobile-app>
	 * 	<services>
	 * 		<service>com.xdev.mobile.service.contacts.ContactsService</service>
	 * 	</services>
	 * </mobile-app>
	 * }
	 * </pre>
	 *
	 * @param type
	 * @return
	 */
	public static <T extends AbstractMobileService> T getMobileService(final Class<T> clazz)
	{
		final UI ui = UI.getCurrent();
		return ui.getExtensions().stream().filter(e -> e.getClass().equals(clazz)).map(clazz::cast)
				.findFirst().orElse(null);
	}
	
	private final MobileServiceConfiguration configuration;
	
	
	protected AbstractMobileService(final AbstractClientConnector target,
			final MobileServiceConfiguration configuration)
	{
		super(target);

		this.configuration = configuration;
	}


	public MobileServiceConfiguration getConfiguration()
	{
		return this.configuration;
	}
	
	
	protected String generateCallerID()
	{
		return Long.toString(System.nanoTime(),Character.MAX_RADIX);
	}
	
	
	protected String toLiteral(final String str)
	{
		final StringBuilder sb = new StringBuilder(str.length() + 2);
		
		sb.append('"');
		
		for(int i = 0, len = str.length(); i < len; i++)
		{
			final char ch = str.charAt(i);
			
			switch(ch)
			{
				case '\b':
					sb.append("\\b");
				break;
				case '\t':
					sb.append("\\t");
				break;
				case '\n':
					sb.append("\\n");
				break;
				case '\f':
					sb.append("\\f");
				break;
				case '\r':
					sb.append("\\r");
				break;
				case '\"':
					sb.append("\\\"");
				break;
				case '\'':
					sb.append("\\\'");
				break;
				case '\\':
					sb.append("\\\\");
				break;
				default:
					sb.append(ch);
				break;
			}
		}
		
		sb.append('"');
		
		return sb.toString();
	}
	
	
	protected void callError(final JsonArray arguments,
			final Map<String, ? extends ServiceCall<?, MobileServiceError>> callMap,
			final boolean remove)
	{
		final String id = arguments.getString(0);
		final ServiceCall<?, MobileServiceError> call = remove ? callMap.remove(id)
				: callMap.get(id);
		if(call != null)
		{
			call.error(new MobileServiceError(this,arguments.get(1).asString()));
		}
	}



	protected static interface ServiceCall<R, E extends MobileServiceError>
	{
		public static <R, E extends MobileServiceError> ServiceCall<R, E> New(
				final Consumer<R> successCallback, final Consumer<E> errorCallback)
		{
			return new Implementation<R, E>(successCallback,errorCallback);
		}


		public void success(final R returnValue);
		
		
		public void error(final E error);
		
		
		
		public static class Implementation<R, E extends MobileServiceError>
				implements ServiceCall<R, E>
		{
			private final Consumer<R>	successCallback;
			private final Consumer<E>	errorCallback;
			
			
			public Implementation(final Consumer<R> successCallback,
					final Consumer<E> errorCallback)
			{
				this.successCallback = successCallback;
				this.errorCallback = errorCallback;
			}
			
			
			@Override
			public void success(final R returnValue)
			{
				if(this.successCallback != null)
				{
					this.successCallback.accept(returnValue);
				}
			}
			
			
			@Override
			public void error(final E error)
			{
				if(this.errorCallback != null)
				{
					this.errorCallback.accept(error);
				}
			}
		}
	}
}
