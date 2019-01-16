/*
 * Copyright (C) 2013-2019 by XDEV Software, All Rights Reserved.
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

package com.xdev.mobile.service.compass;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Page;
import com.xdev.mobile.config.MobileServiceConfiguration;
import com.xdev.mobile.service.AbstractMobileService;
import com.xdev.mobile.service.annotations.MobileService;
import com.xdev.mobile.service.annotations.Plugin;
import com.xdev.mobile.service.compass.CompassServiceError.Reason;

import elemental.json.JsonArray;
import elemental.json.JsonObject;


/**
 * This service provides access to the device's compass. The compass is a sensor
 * that detects the direction or heading that the device is pointed, typically
 * from the top of the device. It measures the heading in degrees from 0 to
 * 359.99, where 0 is north.
 *
 * @author XDEV Software
 *
 */

@MobileService(plugins = @Plugin(name = "cordova-plugin-device-orientation", spec = "1.0.4"))
@JavaScript("compass.js")
public class CompassService extends AbstractMobileService implements CompassServiceAccess
{

	/**
	 * Returns the compass service.<br>
	 * To activate the service it has to be registered in the mobile.xml.
	 *
	 * <pre>
	 * {@code
	 * ...
	 * <service>com.xdev.mobile.service.compass.CompassService</service>
	 * ...
	 * }
	 * </pre>
	 *
	 * @return the compass service if available
	 */
	public static CompassServiceAccess getInstance()
	{
		return getMobileService(CompassService.class);
	}

	private final Map<String, ServiceCall<Heading, CompassServiceError>>		getCalls	= new HashMap<>();
	private final Map<String, ServiceCall<HeadingWatch, CompassServiceError>>	watchCalls	= new HashMap<>();


	public CompassService(final AbstractClientConnector connector,
			final MobileServiceConfiguration configuration)
	{
		super(connector,configuration);

		this.addFunction("compass_get_success",this::compass_get_success);
		this.addFunction("compass_get_error",this::compass_get_error);

		this.addFunction("compass_watch_success",this::compass_watch_success);
		this.addFunction("compass_watch_error",this::compass_watch_error);
	}


	@Override
	public synchronized void getCurrentHeading(final Consumer<Heading> successCallback,
			final Consumer<CompassServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final ServiceCall<Heading, CompassServiceError> call = ServiceCall.New(successCallback,
				errorCallback);
		this.getCalls.put(id,call);

		final StringBuilder js = new StringBuilder();
		js.append("compass_get(").append(toLiteral(id)).append(");");
		Page.getCurrent().getJavaScript().execute(js.toString());
	}


	private void compass_get_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<Heading, CompassServiceError> call = this.getCalls.remove(id);
		if(call != null)
		{
			final JsonObject jsonObject = arguments.getObject(1);
			final Heading heading = new Gson().fromJson(jsonObject.toJson(),Heading.class);
			call.success(heading);
		}
	}


	private void compass_get_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<Heading, CompassServiceError> call = this.getCalls.remove(id);
		if(call != null)
		{
			call.error(createGeolocationServiceError(arguments.get(1)));
		}
	}


	@Override
	public synchronized void watchHeading(final CompassOptions options,
			final Consumer<HeadingWatch> successCallback,
			final Consumer<CompassServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final ServiceCall<HeadingWatch, CompassServiceError> call = ServiceCall.New(successCallback,
				errorCallback);
		this.watchCalls.put(id,call);

		final StringBuilder js = new StringBuilder();
		js.append("compass_watch(").append(toLiteral(id)).append(",");
		js.append(toJson(options));
		js.append(");");

		Page.getCurrent().getJavaScript().execute(js.toString());
	}


	private String toJson(final CompassOptions options)
	{
		return "{frequency:" + options.getFrequency() + "}";
	}


	private void compass_watch_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<HeadingWatch, CompassServiceError> call = this.watchCalls.get(id);
		if(call != null)
		{
			final JsonObject jsonObject = arguments.getObject(1);
			final Heading heading = new Gson().fromJson(jsonObject.toJson(),Heading.class);
			final String watchID = arguments.getString(2);
			final HeadingWatch watch = new HeadingWatch(heading,watchID);
			call.success(watch);
		}
	}


	private void compass_watch_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<HeadingWatch, CompassServiceError> call = this.watchCalls.remove(id);
		if(call != null)
		{
			call.error(createGeolocationServiceError(arguments.get(1)));
		}
	}


	@Override
	public void clearWatch(final String watchID)
	{
		final StringBuilder js = new StringBuilder();
		js.append("compass_clear_watch(");
		js.append(toLiteral(watchID)).append(");");

		Page.getCurrent().getJavaScript().execute(js.toString());
	}


	private CompassServiceError createGeolocationServiceError(final JsonObject error)
	{
		Reason reason = null;

		try
		{
			reason = Reason.getByCode((int)error.getNumber("code"));
		}
		catch(final Exception e)
		{
			// swallow
		}

		return new CompassServiceError(this,"",reason);
	}
}
