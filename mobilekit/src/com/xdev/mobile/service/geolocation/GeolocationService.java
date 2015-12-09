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

package com.xdev.mobile.service.geolocation;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Page;
import com.xdev.mobile.service.MobileService;
import com.xdev.mobile.service.MobileServiceError;
import com.xdev.mobile.ui.MobileUI;

import elemental.json.JsonArray;
import elemental.json.JsonObject;


/**
 * @author XDEV Software
 *
 */

@JavaScript("geolocation.js")
public class GeolocationService extends MobileService
{
	
	/**
	 * Returns the geolocation service.<br>
	 * To activate the service it has to be registered in the mobile.xml.
	 *
	 * <pre>
	 * {@code
	 * ...
	 * <service>com.xdev.mobile.service.geolocation.GeolocationService</service>
	 * ...
	 * }
	 * </pre>
	 *
	 * @see MobileUI#getMobileService(Class)
	 * @return the geolocation service if available
	 */
	public static GeolocationService getInstance()
	{
		return getServiceHelper(GeolocationService.class);
	}
	
	
	
	private static class GetCall
	{
		final Consumer<Position>			successCallback;
		final Consumer<MobileServiceError>	errorCallback;
		
		
		GetCall(final Consumer<Position> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
		}
	}
	
	
	
	private static class WatchCall
	{
		final Consumer<Geolocation>			successCallback;
		final Consumer<MobileServiceError>	errorCallback;
		
		
		WatchCall(final Consumer<Geolocation> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
		}
	}

	private final Map<String, GetCall>		getCalls	= new HashMap<>();
	private final Map<String, WatchCall>	watchcalls	= new HashMap<>();
	
	
	public GeolocationService(final AbstractClientConnector connector)
	{
		super(connector);
		
		this.addFunction("geolocation_get_success",this::geolocation_get_success);
		this.addFunction("geolocation_get_error",this::geolocation_get_error);
		
		this.addFunction("geolocation_watch_success",this::geolocation_watch_success);
		this.addFunction("geolocation_watch_error",this::geolocation_watch_error);

		this.addFunction("geolocation_get_future_success",this::geolocation_get_future_success);
		this.addFunction("geolocation_get_future_error",this::geolocation_get_future_error);
		
	}
	
	
	private void geolocation_get_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final GetCall call = this.getCalls.remove(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final Gson gson = new Gson();
		final JsonObject jsonObject = arguments.getObject(1);
		final Position position = gson.fromJson(jsonObject.toJson(),Position.class);
		
		call.successCallback.accept(position);
	}
	
	
	private void geolocation_get_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final GetCall call = this.getCalls.remove(id);
		if(call == null || call.errorCallback == null)
		{
			return;
		}
		call.errorCallback.accept(new MobileServiceError(this,arguments.get(1).asString()));
	}
	
	
	public synchronized void get(final Consumer<Position> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final GetCall call = new GetCall(successCallback,errorCallback);
		this.getCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("geolocation_get('").append(id).append("');");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}


	public synchronized void watch(final Consumer<Geolocation> successCallback,
			final Consumer<MobileServiceError> errorCallback, final int timeout)
	{
		final String id = generateCallerID();
		final WatchCall call = new WatchCall(successCallback,errorCallback);
		this.watchcalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("geolocation_watch('").append(id).append("',");
		appendTimeout(js,timeout);
		js.append(");");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void appendTimeout(final StringBuilder js, final int timeout)
	{
		js.append("{ ");
		js.append("timeout: ").append(timeout).append(" }");
	}


	private void geolocation_watch_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final WatchCall call = this.watchcalls.get(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final Gson gson = new Gson();
		final JsonObject jsonObject = arguments.getObject(1);
		final Position position = gson.fromJson(jsonObject.toJson(),Position.class);
		final double watchID = arguments.getNumber(2);
		final Geolocation geolocation = new Geolocation(position,watchID);
		
		call.successCallback.accept(geolocation);
	}
	
	
	private void geolocation_watch_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final WatchCall call = this.watchcalls.get(id);
		if(call == null || call.errorCallback == null)
		{
			return;
		}
		call.errorCallback.accept(new MobileServiceError(this,arguments.get(1).asString()));
	}


	public void clearWatch(final double watchID)
	{
		final StringBuilder js = new StringBuilder();
		js.append("geolocation_clear_watch(");
		js.append(watchID).append(");");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}

	Map<String, Position>	waitMap	= new HashMap<>();


	private void geolocation_get_future_success(final JsonArray arguments)
	{
		final Gson gson = new Gson();
		final JsonObject jsonObject = arguments.getObject(1);
		final Position position = gson.fromJson(jsonObject.toJson(),Position.class);

		this.waitMap.put(this.toString(),position);
		this.notify();
	}
	
	
	private void geolocation_get_future_error(final JsonArray arguments)
	{
		System.out.println(arguments.getString(1));
	}
}
