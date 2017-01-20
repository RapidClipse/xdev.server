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

package com.xdev.mobile.service.deviceinfo;


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
 * Service which describes the device's hardware and software.
 *
 *
 * @author XDEV Software
 *
 */
@MobileService(plugins = @Plugin(name = "cordova-plugin-device", spec = "1.1.3"))
@JavaScript("deviceinfo.js")
public class DeviceInfoService extends AbstractMobileService implements DeviceInfoServiceAccess
{
	/**
	 * Returns the device info service.<br>
	 * To activate the service it has to be registered in the mobile.xml.
	 *
	 * <pre>
	 * {@code
	 * ...
	 * <service>com.xdev.mobile.service.deviceinfo.DeviceInfoService</service>
	 * ...
	 * }
	 * </pre>
	 *
	 * @return the device info service if available
	 */
	public static DeviceInfoServiceAccess getInstance()
	{
		return getMobileService(DeviceInfoService.class);
	}
	
	private final Map<String, ServiceCall<DeviceInfo, MobileServiceError>> getCalls = new HashMap<>();
	
	
	public DeviceInfoService(final AbstractClientConnector connector,
			final MobileServiceConfiguration configuration)
	{
		super(connector,configuration);
		
		this.addFunction("deviceinfo_callback",this::deviceinfo_callback);
	}
	
	
	@Override
	public void getDeviceInfo(final Consumer<DeviceInfo> callback)
	{
		final String id = generateCallerID();
		final ServiceCall<DeviceInfo, MobileServiceError> call = ServiceCall.New(callback,null);
		this.getCalls.put(id,call);

		final StringBuilder js = new StringBuilder();
		js.append("deviceinfo_get(").append(toLiteral(id)).append(");");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void deviceinfo_callback(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<DeviceInfo, MobileServiceError> call = this.getCalls.remove(id);
		if(call != null)
		{
			final Gson gson = new Gson();
			final JsonObject jsonObject = arguments.getObject(1);
			final DeviceInfo info = gson.fromJson(jsonObject.toJson(),DeviceInfo.class);
			
			call.success(info);
		}
	}
}
