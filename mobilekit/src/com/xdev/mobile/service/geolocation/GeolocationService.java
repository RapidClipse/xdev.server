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

package com.xdev.mobile.service.geolocation;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Page;
import com.xdev.mobile.config.MobileServiceConfiguration;
import com.xdev.mobile.service.AbstractMobileService;
import com.xdev.mobile.service.annotations.MobileService;
import com.xdev.mobile.service.annotations.Plugin;
import com.xdev.mobile.service.geolocation.GeolocationServiceError.Reason;

import elemental.json.JsonArray;
import elemental.json.JsonNumber;
import elemental.json.JsonObject;
import elemental.json.JsonValue;


/**
 * This service provides information about the device's location, such as
 * latitude and longitude. Common sources of location information include Global
 * Positioning System (GPS) and location inferred from network signals such as
 * IP address, RFID, WiFi and Bluetooth MAC addresses, and GSM/CDMA cell IDs.
 * There is no guarantee that the API returns the device's actual location.
 * <p>
 * <b>WARNING</b>:<br>
 * Collection and use of geolocation data raises important privacy issues. Your
 * app's privacy policy should discuss how the app uses geolocation data,
 * whether it is shared with any other parties, and the level of precision of
 * the data (for example, coarse, fine, ZIP code level, etc.). Geolocation data
 * is generally considered sensitive because it can reveal user's whereabouts
 * and, if stored, the history of their travels. Therefore, in addition to the
 * app's privacy policy, you should strongly consider providing a just-in-time
 * notice before the app accesses geolocation data (if the device operating
 * system doesn't do so already). That notice should provide the same
 * information noted above, as well as obtaining the user's permission (e.g., by
 * presenting choices for OK and No Thanks). For more information, please see
 * the <a href=
 * "http://cordova.apache.org/docs/en/latest/guide/appdev/privacy/index.html">
 * Privacy Guide</a>.
 *
 * @author XDEV Software
 *
 */

@MobileService(plugins = @Plugin(name = "cordova-plugin-geolocation", spec = "2.4.0"))
@JavaScript("geolocation.js")
public class GeolocationService extends AbstractMobileService implements GeolocationServiceAccess
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
	 * @return the geolocation service if available
	 */
	public static GeolocationServiceAccess getInstance()
	{
		return getMobileService(GeolocationService.class);
	}

	private final Map<String, ServiceCall<Position, GeolocationServiceError>>		getCalls	= new HashMap<>();
	private final Map<String, ServiceCall<PositionWatch, GeolocationServiceError>>	watchCalls	= new HashMap<>();


	public GeolocationService(final AbstractClientConnector connector,
			final MobileServiceConfiguration configuration)
	{
		super(connector,configuration);

		this.addFunction("geolocation_get_success",this::geolocation_get_success);
		this.addFunction("geolocation_get_error",this::geolocation_get_error);

		this.addFunction("geolocation_watch_success",this::geolocation_watch_success);
		this.addFunction("geolocation_watch_error",this::geolocation_watch_error);
	}


	@Override
	public synchronized void getCurrentPosition(final GeolocationOptions options,
			final Consumer<Position> successCallback,
			final Consumer<GeolocationServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final ServiceCall<Position, GeolocationServiceError> call = ServiceCall.New(successCallback,
				errorCallback);
		this.getCalls.put(id,call);

		final StringBuilder js = new StringBuilder();
		js.append("geolocation_get('").append(id).append("',");
		js.append(toJson(options));
		js.append(");");
		Page.getCurrent().getJavaScript().execute(js.toString());
	}


	private void geolocation_get_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<Position, GeolocationServiceError> call = this.getCalls.remove(id);
		if(call != null)
		{
			final JsonObject jsonObject = arguments.getObject(1);
			final Position position = parsePosition(jsonObject);
			call.success(position);
		}
	}


	private void geolocation_get_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<Position, GeolocationServiceError> call = this.getCalls.remove(id);
		if(call != null)
		{
			call.error(createGeolocationServiceError(arguments.get(1)));
		}
	}


	@Override
	public synchronized void watchPosition(final GeolocationOptions options,
			final Consumer<PositionWatch> successCallback,
			final Consumer<GeolocationServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final ServiceCall<PositionWatch, GeolocationServiceError> call = ServiceCall
				.New(successCallback,errorCallback);
		this.watchCalls.put(id,call);

		final StringBuilder js = new StringBuilder();
		js.append("geolocation_watch('").append(id).append("',");
		js.append(toJson(options));
		js.append(");");

		Page.getCurrent().getJavaScript().execute(js.toString());
	}


	private String toJson(final GeolocationOptions options)
	{
		final List<String> list = new ArrayList<>();

		final Boolean enableHighAccuracy = options.getEnableHighAccuracy();
		if(enableHighAccuracy != null)
		{
			list.add("enableHighAccuracy:" + enableHighAccuracy);
		}

		final Long timeout = options.getTimeout();
		if(timeout != null)
		{
			list.add("timeout:" + timeout);
		}

		final Long maximumAge = options.getMaximumAge();
		if(maximumAge != null)
		{
			list.add("maximumAge:" + maximumAge);
		}

		return list.stream().collect(Collectors.joining(",","{","}"));
	}


	private void geolocation_watch_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<PositionWatch, GeolocationServiceError> call = this.watchCalls.get(id);
		if(call != null)
		{
			final JsonObject jsonObject = arguments.getObject(1);
			final Position position = parsePosition(jsonObject);
			final String watchID = arguments.getString(2);
			final PositionWatch watch = new PositionWatch(position,watchID);
			call.success(watch);
		}
	}


	private void geolocation_watch_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<PositionWatch, GeolocationServiceError> call = this.watchCalls.remove(id);
		if(call != null)
		{
			call.error(createGeolocationServiceError(arguments.get(1)));
		}
	}
	
	
	@Override
	public void clearWatch(final String watchID)
	{
		final StringBuilder js = new StringBuilder();
		js.append("geolocation_clear_watch(");
		js.append(toLiteral(watchID)).append(");");

		Page.getCurrent().getJavaScript().execute(js.toString());
	}


	private Position parsePosition(final JsonObject object)
	{
		final JsonObject coordsObj = object.getObject("coords");

		final Coordinates coords = new Coordinates(getDouble(coordsObj,"latitude"),
				getDouble(coordsObj,"longitude"),getDouble(coordsObj,"altitude"),
				getDouble(coordsObj,"accuracy"),getDouble(coordsObj,"altitudeAccuracy"),
				getDouble(coordsObj,"heading"),getDouble(coordsObj,"speed"));

		final long timestamp = (long)getDouble(object,"timestamp");

		return new Position(coords,timestamp);
	}


	private double getDouble(final JsonObject object, final String key)
	{
		if(object.hasKey(key))
		{
			final JsonValue value = object.get(key);
			if(value instanceof JsonNumber)
			{
				return value.asNumber();
			}
		}
		return -1;
	}


	private GeolocationServiceError createGeolocationServiceError(final JsonObject error)
	{
		String message = "";
		Reason reason = null;

		try
		{
			message = error.getString("message");
		}
		catch(final Exception e)
		{
			// swallow
		}

		try
		{
			reason = Reason.getByCode((int)error.getNumber("code"));
		}
		catch(final Exception e)
		{
			// swallow
		}

		return new GeolocationServiceError(this,message,reason);
	}
}
