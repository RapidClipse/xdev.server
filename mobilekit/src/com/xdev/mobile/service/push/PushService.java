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

package com.xdev.mobile.service.push;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
 * This service provides a way to send and receive push messages.
 *
 * @author XDEV Software
 *
 */
@MobileService(plugins = @Plugin(name = "phonegap-plugin-push", spec = "1.7.0") )
@JavaScript("push.js")
public class PushService extends AbstractMobileService
{
	/**
	 * Returns the push service.<br>
	 * To activate the service it has to be registered in the mobile.xml.
	 *
	 * <pre>
	 * {@code
	 * ...
	 * <service>com.xdev.mobile.service.push.PushService</service>
	 * ...
	 * }
	 * </pre>
	 *
	 * @return the push service if available
	 */
	public static PushService getInstance()
	{
		return getMobileService(PushService.class);
	}
	
	private final Set<Consumer<RegistrationData>>				registrationListener	= new HashSet<>();
	private final Set<Consumer<NotificationData>>				notificationListener	= new HashSet<>();
	private final Set<Consumer<MobileServiceError>>				errorListener			= new HashSet<>();
	private final Map<String, Set<Consumer<NotificationData>>>	actionCallbackHandlers	= new HashMap<>();
	
	
	public PushService(final AbstractClientConnector target,
			final MobileServiceConfiguration configuration)
	{
		super(target,configuration);
		
		this.addFunction("push_on_registration",this::push_on_registration);
		this.addFunction("push_on_notification",this::push_on_notification);
		this.addFunction("push_on_error",this::push_on_error);
		this.addFunction("push_action_callback",this::push_action_callback);
	}
	
	
	public void init(final String androidSenderID)
	{
		final StringBuilder js = new StringBuilder();
		
		js.append("push_init(");
		js.append(toLiteral(androidSenderID));
		js.append(");");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	public void addRegistrationListener(final Consumer<RegistrationData> consumer)
	{
		if(this.registrationListener.add(consumer) && this.registrationListener.size() == 1)
		{
			Page.getCurrent().getJavaScript().execute("push_addRegistrationListener();");
		}
	}
	
	
	public void removeRegistrationListener(final Consumer<RegistrationData> consumer)
	{
		if(this.registrationListener.remove(consumer) && this.registrationListener.isEmpty())
		{
			Page.getCurrent().getJavaScript().execute("push_removeRegistrationListener();");
		}
	}
	
	
	private void push_on_registration(final JsonArray arguments)
	{
		final JsonObject obj = arguments.getObject(0);
		final RegistrationData data = new RegistrationData(obj.getString("registrationId"));
		this.registrationListener.forEach(c -> c.accept(data));
	}
	
	
	public void addNotificationListener(final Consumer<NotificationData> consumer)
	{
		if(this.notificationListener.add(consumer) && this.notificationListener.size() == 1)
		{
			Page.getCurrent().getJavaScript().execute("push_addNotificationListener();");
		}
	}
	
	
	public void removeNotificationListener(final Consumer<NotificationData> consumer)
	{
		if(this.notificationListener.remove(consumer) && this.notificationListener.isEmpty())
		{
			Page.getCurrent().getJavaScript().execute("push_removeNotificationListener();");
		}
	}
	
	
	private void push_on_notification(final JsonArray arguments)
	{
		final JsonObject obj = arguments.getObject(0);
		final NotificationData data = toNotificationData(obj);
		this.notificationListener.forEach(c -> c.accept(data));
	}
	
	
	@SuppressWarnings("unchecked")
	private NotificationData toNotificationData(final JsonObject obj)
	{
		Map<String, Object> additionalData = null;
		final JsonObject additionalDataObj = obj.getObject("additionalData");
		if(additionalDataObj != null)
		{
			final Gson gson = new Gson();
			additionalData = gson.fromJson(
					gson.fromJson(additionalDataObj.toJson(),com.google.gson.JsonObject.class),
					HashMap.class);
		}
		final NotificationData data = new NotificationData(obj.getString("message"),
				obj.getString("title"),additionalData);
		return data;
	}
	
	
	public void addErrorListener(final Consumer<MobileServiceError> consumer)
	{
		if(this.errorListener.add(consumer) && this.errorListener.size() == 1)
		{
			Page.getCurrent().getJavaScript().execute("push_addErrorListener();");
		}
	}
	
	
	public void removeErrorListener(final Consumer<MobileServiceError> consumer)
	{
		if(this.errorListener.remove(consumer) && this.errorListener.isEmpty())
		{
			Page.getCurrent().getJavaScript().execute("push_removeErrorListener();");
		}
	}
	
	
	private void push_on_error(final JsonArray arguments)
	{
		final JsonObject obj = arguments.getObject(0);
		final MobileServiceError error = new MobileServiceError(this,obj.getString("message"));
		this.errorListener.forEach(c -> c.accept(error));
	}
	
	
	// public void registerCallbackActions(final String... names)
	// {
	// if(names != null && names.length > 0)
	// {
	// final StringBuilder sb = new StringBuilder();
	// sb.append("push_register_callbacks([");
	// Arrays.stream(names).map(this::toLiteral).collect(Collectors.joining(","));
	// sb.append("])");
	// Page.getCurrent().getJavaScript().execute(sb.toString());
	// }
	// }
	
	public void addActionCallbackHandler(final String name,
			final Consumer<NotificationData> handler)
	{
		Set<Consumer<NotificationData>> handlers = this.actionCallbackHandlers.get(name);
		if(handlers == null)
		{
			handlers = new HashSet<>();
			this.actionCallbackHandlers.put(name,handlers);
			
			Page.getCurrent().getJavaScript()
					.execute("push_register_action_callback(" + toLiteral(name) + ");");
		}
		handlers.add(handler);
	}
	
	
	private void push_action_callback(final JsonArray arguments)
	{
		final String name = arguments.getString(0);
		final Set<Consumer<NotificationData>> handlers = this.actionCallbackHandlers.get(name);
		if(handlers != null)
		{
			final JsonObject obj = arguments.getObject(1);
			final NotificationData data = toNotificationData(obj);
			handlers.forEach(handler -> handler.accept(data));
		}
	}
}
