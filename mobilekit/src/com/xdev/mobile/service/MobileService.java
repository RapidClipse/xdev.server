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
 * 
 * For further information see 
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.mobile.service;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.UI;


/**
 * @author XDEV Software
 *
 */
public abstract class MobileService extends AbstractJavaScriptExtension
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
	public static <T extends MobileService> T getMobileService(final Class<T> clazz)
	{
		final UI ui = UI.getCurrent();
		return ui.getExtensions().stream().filter(e -> e.getClass().equals(clazz)).map(clazz::cast)
				.findFirst().orElse(null);
	}
	
	
	protected MobileService(final AbstractClientConnector target)
	{
		super(target);
	}
	
	
	protected String generateCallerID()
	{
		return Long.toString(System.nanoTime(),Character.MAX_RADIX);
	}
	
	
	
	protected static interface ServiceCall<R>
	{
		public static <R> ServiceCall<R> async(final Consumer<R> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			return new Implementation<R>(successCallback,errorCallback);
		}


		// public static <R> ServiceCall.Synchronized<R> sync()
		// {
		// return new Synchronized.Implementation<R>();
		// }

		public void success(final R returnValue);
		
		
		public void error(final MobileServiceError error);


		public void put(String key, Object value);


		public Object get(String key);
		
		
		
		public static class Implementation<R> implements ServiceCall<R>
		{
			private final Consumer<R>					successCallback;
			private final Consumer<MobileServiceError>	errorCallback;
			private final Map<String, Object>			values	= new HashMap<>();
			
			
			public Implementation(final Consumer<R> successCallback,
					final Consumer<MobileServiceError> errorCallback)
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
			public void error(final MobileServiceError error)
			{
				if(this.errorCallback != null)
				{
					this.errorCallback.accept(error);
				}
			}


			@Override
			public void put(final String key, final Object value)
			{
				this.values.put(key,value);
			}


			@Override
			public Object get(final String key)
			{
				return this.values.get(key);
			}
		}

		// public static interface Synchronized<R> extends ServiceCall<R>
		// {
		// public R execute(String script) throws MobileServiceException;
		//
		//
		//
		// public static class Implementation<R> implements Synchronized<R>
		// {
		// private R returnValue;
		// private MobileServiceException exception;
		// private final CountDownLatch countDownLatch = new CountDownLatch(1);
		//
		//
		// @Override
		// public R execute(final String script) throws MobileServiceException
		// {
		// final JavaScript javaScript = Page.getCurrent().getJavaScript();
		//
		// try
		// {
		// CompletableFuture.runAsync(() -> {
		// try
		// {
		// javaScript.execute(script);
		// this.countDownLatch.await();
		// }
		// catch(final InterruptedException e)
		// {
		// throw new RuntimeException(e);
		// }
		// }).get();
		// }
		// catch(InterruptedException | ExecutionException e)
		// {
		// throw new RuntimeException(e);
		// }
		//
		// if(this.exception != null)
		// {
		// throw this.exception;
		// }
		//
		// return this.returnValue;
		// }
		//
		//
		// @Override
		// public void success(final R returnValue)
		// {
		// this.returnValue = returnValue;
		// this.countDownLatch.countDown();
		// }
		//
		//
		// @Override
		// public void error(final MobileServiceError error)
		// {
		// this.exception = new MobileServiceException(error);
		// this.countDownLatch.countDown();
		// }
		// }
		// }
	}
}
