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

package com.xdev.mobile.service.accelerometer;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Page;
import com.xdev.mobile.config.MobileServiceConfiguration;
import com.xdev.mobile.service.AbstractMobileService;
import com.xdev.mobile.service.MobileServiceError;
import com.xdev.mobile.service.annotations.MobileService;
import com.xdev.mobile.service.annotations.Plugin;

import elemental.json.JsonArray;
import elemental.json.JsonObject;


/**
 * This service provides access to the device's accelerometer. The accelerometer
 * is a motion sensor that detects the change (delta) in movement relative to
 * the current device orientation, in three dimensions along the x, y, and z
 * axis.
 *
 * @author XDEV Software
 *
 */

@MobileService(plugins = @Plugin(name = "cordova-plugin-device-motion", spec = "1.2.2"))
@JavaScript("accelerometer.js")
public class AccelerometerService extends AbstractMobileService
		implements AccelerometerServiceAccess
{
	
	/**
	 * Returns the accelerometer service.<br>
	 * To activate the service it has to be registered in the mobile.xml.
	 *
	 * <pre>
	 * {@code
	 * ...
	 * <service>com.xdev.mobile.service.accelerometer.AccelerometerService</service>
	 * ...
	 * }
	 * </pre>
	 *
	 * @return the accelerometer service if available
	 */
	public static AccelerometerServiceAccess getInstance()
	{
		return getMobileService(AccelerometerService.class);
	}
	
	private final Map<String, ServiceCall<Acceleration, MobileServiceError>>		getCalls	= new HashMap<>();
	private final Map<String, ServiceCall<AccelerationWatch, MobileServiceError>>	watchCalls	= new HashMap<>();
	
	
	public AccelerometerService(final AbstractClientConnector connector,
			final MobileServiceConfiguration configuration)
	{
		super(connector,configuration);
		
		this.addFunction("accelerometer_get_success",this::accelerometer_get_success);
		this.addFunction("accelerometer_get_error",this::accelerometer_get_error);
		
		this.addFunction("accelerometer_watch_success",this::accelerometer_watch_success);
		this.addFunction("accelerometer_watch_error",this::accelerometer_watch_error);
	}
	
	
	@Override
	public synchronized void getCurrentAcceleration(final Consumer<Acceleration> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final ServiceCall<Acceleration, MobileServiceError> call = ServiceCall.New(successCallback,
				errorCallback);
		this.getCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("accelerometer_get(").append(toLiteral(id)).append(");");
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void accelerometer_get_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<Acceleration, MobileServiceError> call = this.getCalls.remove(id);
		if(call != null)
		{
			final JsonObject jsonObject = arguments.getObject(1);
			final Acceleration acceleration = new Gson().fromJson(jsonObject.toJson(),
					Acceleration.class);
			call.success(acceleration);
		}
	}
	
	
	private void accelerometer_get_error(final JsonArray arguments)
	{
		callError(arguments,this.getCalls,true);
	}
	
	
	@Override
	public synchronized void watchAcceleration(final AccelerometerOptions options,
			final Consumer<AccelerationWatch> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final ServiceCall<AccelerationWatch, MobileServiceError> call = ServiceCall
				.New(successCallback,errorCallback);
		this.watchCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("accelerometer_watch(").append(toLiteral(id)).append(",");
		js.append(toJson(options));
		js.append(");");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private String toJson(final AccelerometerOptions options)
	{
		return "{frequency:" + options.getFrequency() + "}";
	}
	
	
	private void accelerometer_watch_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<AccelerationWatch, MobileServiceError> call = this.watchCalls.get(id);
		if(call != null)
		{
			final JsonObject jsonObject = arguments.getObject(1);
			final Acceleration acceleration = new Gson().fromJson(jsonObject.toJson(),
					Acceleration.class);
			final String watchID = arguments.getString(2);
			final AccelerationWatch watch = new AccelerationWatch(acceleration,watchID);
			call.success(watch);
		}
	}
	
	
	private void accelerometer_watch_error(final JsonArray arguments)
	{
		callError(arguments,this.watchCalls,false);
	}
	
	
	@Override
	public void clearWatch(final String watchID)
	{
		final StringBuilder js = new StringBuilder();
		js.append("accelerometer_clear_watch(");
		js.append(toLiteral(watchID)).append(");");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
}
