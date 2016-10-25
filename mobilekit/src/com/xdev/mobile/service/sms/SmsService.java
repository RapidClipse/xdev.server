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

package com.xdev.mobile.service.sms;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Page;
import com.xdev.mobile.config.MobileServiceConfiguration;
import com.xdev.mobile.service.AbstractMobileService;
import com.xdev.mobile.service.MobileServiceError;
import com.xdev.mobile.service.annotations.MobileService;
import com.xdev.mobile.service.annotations.Plugin;

import elemental.json.JsonArray;


/**
 * Service to easily send SMS.
 *
 * @author XDEV Software
 *
 */

@MobileService(plugins = @Plugin(name = "cordova-sms-plugin", spec = "0.1.11"))
@JavaScript("sms.js")
public class SmsService extends AbstractMobileService implements SmsServiceAccess
{
	
	/**
	 * Returns the SMS service.<br>
	 * To activate the service it has to be registered in the mobile.xml.
	 *
	 * <pre>
	 * {@code
	 * ...
	 * <service>com.xdev.mobile.service.sms.SmsService</service>
	 * ...
	 * }
	 * </pre>
	 *
	 * @return the SMS service if available
	 */
	public static SmsServiceAccess getInstance()
	{
		return getMobileService(SmsService.class);
	}
	
	private final Map<String, ServiceCall<Object, MobileServiceError>> sendCalls = new HashMap<>();
	
	
	public SmsService(final AbstractClientConnector connector,
			final MobileServiceConfiguration configuration)
	{
		super(connector,configuration);
		
		this.addFunction("sms_send_success",this::sms_send_success);
		this.addFunction("sms_send_error",this::sms_send_error);
	}
	
	
	@Override
	public void send(final String number, final String message, final SmsOptions options,
			final Runnable successCallback, final Consumer<MobileServiceError> errorCallback)
	{
		final Consumer<Object> consumerWrapper = successCallback != null
				? object -> successCallback.run() : null;
		
		final String id = generateCallerID();
		final ServiceCall<Object, MobileServiceError> call = ServiceCall.New(consumerWrapper,
				errorCallback);
		this.sendCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("sms_send(").append(toLiteral(id)).append(",").append(toLiteral(number))
				.append(",").append(toLiteral(message)).append(",").append(toJson(options))
				.append(");");
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private String toJson(final SmsOptions options)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("{replaceLineBreaks:").append(options.isReplaceLineBreaks()).append(",android:{")
				.append("intent:").append(options.isAndroidNativeApp() ? "'INTENT'" : "''")
				.append("}}");
		return sb.toString();
	}
	
	
	private void sms_send_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<Object, MobileServiceError> call = this.sendCalls.remove(id);
		if(call != null)
		{
			call.success(null);
		}
	}
	
	
	private void sms_send_error(final JsonArray arguments)
	{
		callError(arguments,this.sendCalls,true);
	}
}
