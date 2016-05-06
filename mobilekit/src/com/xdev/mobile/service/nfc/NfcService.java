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

package com.xdev.mobile.service.nfc;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Page;
import com.xdev.mobile.service.MobileService;
import com.xdev.mobile.service.MobileServiceDescriptor;
import com.xdev.mobile.service.MobileServiceError;

import elemental.json.JsonArray;
import elemental.json.JsonObject;


/**
 * This service allows you to read and write NFC tags.
 * <p>
 * Supported platforms:
 * <ul>
 * <li>Android</li>
 * <li>Windows Phone 8</li>
 * <li>Windows (Windows Phone 8.1 and Windows 10)</li>
 * </ul>
 *
 * @author XDEV Software
 *
 */

@MobileServiceDescriptor("nfc-descriptor.xml")
@JavaScript("nfc.js")
public class NfcService extends MobileService
{
	
	/**
	 * Returns the nfc service.<br>
	 * To activate the service it has to be registered in the mobile.xml.
	 *
	 * <pre>
	 * {@code
	 * ...
	 * <service>com.xdev.mobile.service.nfc.NfcService</service>
	 * ...
	 * }
	 * </pre>
	 *
	 * @return the file service if available
	 */
	public static NfcService getInstance()
	{
		return getMobileService(NfcService.class);
	}
	
	
	public NfcService(final AbstractClientConnector connector)
	{
		super(connector);
		
		this.addFunction("nfc_startTagDiscoveredListener_callback",
				this::nfc_startTagDiscoveredListener_callback);
		this.addFunction("nfc_startTagDiscoveredListener_success",
				this::nfc_startTagDiscoveredListener_success);
		this.addFunction("nfc_startTagDiscoveredListener_error",
				this::nfc_startTagDiscoveredListener_error);
		
		this.addFunction("nfc_stopTagDiscoveredListener_success",
				this::nfc_stopTagDiscoveredListener_success);
		this.addFunction("nfc_stopTagDiscoveredListener_error",
				this::nfc_stopTagDiscoveredListener_error);
		
		this.addFunction("nfc_erase_success",this::nfc_erase_success);
		this.addFunction("nfc_erase_error",this::nfc_erase_error);
		
		this.addFunction("nfc_startNdefListener_callback",this::nfc_startNdefListener_callback);
		this.addFunction("nfc_startNdefListener_success",this::nfc_startNdefListener_success);
		this.addFunction("nfc_startNdefListener_error",this::nfc_startNdefListener_error);
		
		this.addFunction("nfc_stopNdefListener_success",this::nfc_stopNdefListener_success);
		this.addFunction("nfc_stopNdefListener_error",this::nfc_stopNdefListener_error);
		
		this.addFunction("nfc_write_success",this::nfc_write_success);
		this.addFunction("nfc_write_error",this::nfc_write_error);
		
		this.addFunction("nfc_makeReadOnly_success",this::nfc_makeReadOnly_success);
		this.addFunction("nfc_makeReadOnly_error",this::nfc_makeReadOnly_error);
		
		this.addFunction("nfc_showSettings_success",this::nfc_showSettings_success);
		this.addFunction("nfc_showSettings_error",this::nfc_showSettings_error);
	}
	
	
	
	private static class StartNdefListenerCall
	{
		final Consumer<Ndef>				callback;
		final Consumer<Object>				successCallback;
		final Consumer<MobileServiceError>	errorCallback;
		
		
		StartNdefListenerCall(final Consumer<Ndef> callback, final Consumer<Object> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			this.callback = callback;
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
		}
	}
	
	
	
	private static class StopNdefListenerCall
	{
		final Consumer<Object>				successCallback;
		final Consumer<MobileServiceError>	errorCallback;
		
		
		StopNdefListenerCall(final Consumer<Object> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
		}
	}
	
	
	
	private static class StartTagDiscoveredListenerCall
	{
		final Consumer<Ndef>				callback;
		final Consumer<Object>				successCallback;
		final Consumer<MobileServiceError>	errorCallback;
		
		
		StartTagDiscoveredListenerCall(final Consumer<Ndef> callback,
				final Consumer<Object> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			this.callback = callback;
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
		}
	}
	
	
	
	private static class StopTagDiscoveredListenerCall
	{
		final Consumer<String>				successCallback;
		final Consumer<MobileServiceError>	errorCallback;
		
		
		StopTagDiscoveredListenerCall(final Consumer<String> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
		}
	}
	
	
	
	private static class EraseTagCall
	{
		final Consumer<String>				successCallback;
		final Consumer<MobileServiceError>	errorCallback;
		
		
		EraseTagCall(final Consumer<String> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
		}
	}
	
	
	
	private static class WriteCall
	{
		final Consumer<String>				successCallback;
		final Consumer<MobileServiceError>	errorCallback;
		
		
		WriteCall(final Consumer<String> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
		}
	}
	
	
	
	private static class MakeReadOnlyCall
	{
		final Consumer<String>				successCallback;
		final Consumer<MobileServiceError>	errorCallback;
		
		
		MakeReadOnlyCall(final Consumer<String> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
		}
	}
	
	
	
	private static class ShowSettingsCall
	{
		final Consumer<String>				successCallback;
		final Consumer<MobileServiceError>	errorCallback;
		
		
		ShowSettingsCall(final Consumer<String> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
		}
	}
	
	private final Map<String, StartNdefListenerCall>			startNdefListenerCalls			= new HashMap<>();
	private final Map<String, StopNdefListenerCall>				stopNdefListenerCalls			= new HashMap<>();
	private final Map<String, StartTagDiscoveredListenerCall>	startTagDiscoveredListenerCalls	= new HashMap<>();
	private final Map<String, StopTagDiscoveredListenerCall>	stopTagDiscoveredListenerCalls	= new HashMap<>();
	private final Map<String, EraseTagCall>						eraseTagCalls					= new HashMap<>();
	private final Map<String, WriteCall>						writeCalls						= new HashMap<>();
	private final Map<String, MakeReadOnlyCall>					makeReadOnlyCalls				= new HashMap<>();
	private final Map<String, ShowSettingsCall>					showSettingsCalls				= new HashMap<>();
	
	
	private void nfc_startTagDiscoveredListener_callback(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final StartTagDiscoveredListenerCall call = this.startTagDiscoveredListenerCalls.get(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final Gson gson = new Gson();
		final JsonObject jsonObject = arguments.getObject(1);
		
		final Ndef ndef = gson.fromJson(jsonObject.toJson(),Ndef.class);
		
		call.callback.accept(ndef);
	}
	
	
	private void nfc_startTagDiscoveredListener_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final StartTagDiscoveredListenerCall call = this.startTagDiscoveredListenerCalls.get(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final String returnString = arguments.getString(1);
		
		call.successCallback.accept(returnString);
	}
	
	
	private void nfc_startTagDiscoveredListener_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final StartTagDiscoveredListenerCall call = this.startTagDiscoveredListenerCalls.remove(id);
		if(call == null || call.errorCallback == null)
		{
			return;
		}
		call.errorCallback.accept(new MobileServiceError(this,arguments.get(1).asString()));
	}
	
	
	private void nfc_stopTagDiscoveredListener_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final StopTagDiscoveredListenerCall call = this.stopTagDiscoveredListenerCalls.remove(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final String returnString = arguments.getString(1);
		
		call.successCallback.accept(returnString);
	}
	
	
	private void nfc_stopTagDiscoveredListener_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final StopTagDiscoveredListenerCall call = this.stopTagDiscoveredListenerCalls.remove(id);
		if(call == null || call.errorCallback == null)
		{
			return;
		}
		call.errorCallback.accept(new MobileServiceError(this,arguments.get(1).asString()));
	}
	
	
	private void nfc_startNdefListener_callback(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final StartNdefListenerCall call = this.startNdefListenerCalls.get(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final Gson gson = new Gson();
		final JsonObject jsonObject = arguments.getObject(1);
		
		final Ndef ndef = gson.fromJson(jsonObject.toJson(),Ndef.class);
		
		call.callback.accept(ndef);
	}
	
	
	private void nfc_startNdefListener_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final StartNdefListenerCall call = this.startNdefListenerCalls.get(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final String returnString = arguments.getString(1);
		
		call.successCallback.accept(returnString);
	}
	
	
	private void nfc_startNdefListener_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final StartNdefListenerCall call = this.startNdefListenerCalls.remove(id);
		if(call == null || call.errorCallback == null)
		{
			return;
		}
		call.errorCallback.accept(new MobileServiceError(this,arguments.get(1).asString()));
	}
	
	
	private void nfc_stopNdefListener_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final StopNdefListenerCall call = this.stopNdefListenerCalls.get(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final String returnString = arguments.getString(1);
		
		call.successCallback.accept(returnString);
	}
	
	
	private void nfc_stopNdefListener_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final StopNdefListenerCall call = this.stopNdefListenerCalls.remove(id);
		if(call == null || call.errorCallback == null)
		{
			return;
		}
		call.errorCallback.accept(new MobileServiceError(this,arguments.get(1).asString()));
	}
	
	
	private void nfc_erase_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final EraseTagCall call = this.eraseTagCalls.get(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final String returnString = arguments.getString(1);
		
		call.successCallback.accept(returnString);
	}
	
	
	private void nfc_erase_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final EraseTagCall call = this.eraseTagCalls.remove(id);
		if(call == null || call.errorCallback == null)
		{
			return;
		}
		call.errorCallback.accept(new MobileServiceError(this,arguments.get(1).asString()));
	}
	
	
	private void nfc_write_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final WriteCall call = this.writeCalls.get(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final String returnString = arguments.getString(1);
		
		call.successCallback.accept(returnString);
	}
	
	
	private void nfc_write_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final EraseTagCall call = this.eraseTagCalls.remove(id);
		if(call == null || call.errorCallback == null)
		{
			return;
		}
		call.errorCallback.accept(new MobileServiceError(this,arguments.get(1).asString()));
	}
	
	
	private void nfc_makeReadOnly_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final WriteCall call = this.writeCalls.get(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final String returnString = arguments.getString(1);
		
		call.successCallback.accept(returnString);
	}
	
	
	private void nfc_makeReadOnly_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final MakeReadOnlyCall call = this.makeReadOnlyCalls.remove(id);
		if(call == null || call.errorCallback == null)
		{
			return;
		}
		call.errorCallback.accept(new MobileServiceError(this,arguments.get(1).asString()));
	}
	
	
	private void nfc_showSettings_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ShowSettingsCall call = this.showSettingsCalls.remove(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final String returnString = arguments.getString(1);
		
		call.successCallback.accept(returnString);
	}
	
	
	private void nfc_showSettings_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ShowSettingsCall call = this.showSettingsCalls.remove(id);
		if(call == null || call.errorCallback == null)
		{
			return;
		}
		call.errorCallback.accept(new MobileServiceError(this,arguments.get(1).asString()));
	}
	
	
	public synchronized void startTagDiscoveredListener(final Consumer<Ndef> callback,
			final Consumer<Object> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		removeAllListener();
		final String id = generateCallerID();
		final StartTagDiscoveredListenerCall call = new StartTagDiscoveredListenerCall(callback,
				successCallback,errorCallback);
		this.startTagDiscoveredListenerCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_startTagDiscoveredListener('").append(id).append("');");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	public synchronized void stopTagDiscoveredListener(final Consumer<String> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		
		final String id = generateCallerID();
		final StopTagDiscoveredListenerCall call = new StopTagDiscoveredListenerCall(
				successCallback,errorCallback);
		
		this.stopTagDiscoveredListenerCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_stopTagDiscoveredListener('").append(id).append("');");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	public synchronized void startNdefListener(final Consumer<Ndef> callback,
			final Consumer<Object> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		
		removeAllListener();
		
		final String id = generateCallerID();
		final StartNdefListenerCall call = new StartNdefListenerCall(callback,successCallback,
				errorCallback);
		this.startNdefListenerCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_startNdefListener('").append(id).append("');");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	public synchronized void stopNdefListener(final Consumer<Object> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		
		final String id = generateCallerID();
		final StopNdefListenerCall call = new StopNdefListenerCall(successCallback,errorCallback);
		this.stopNdefListenerCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_stopNdefListener('").append(id).append("');");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	public synchronized void makeReadOnly(final Consumer<String> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		
		removeAllListener();
		
		final String id = generateCallerID();
		final MakeReadOnlyCall call = new MakeReadOnlyCall(successCallback,errorCallback);
		this.makeReadOnlyCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_makeReadOnly('").append(id).append("');");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	public synchronized void showSettings(final Consumer<String> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		
		final String id = generateCallerID();
		final ShowSettingsCall call = new ShowSettingsCall(successCallback,errorCallback);
		this.showSettingsCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_showSettings('").append(id).append("');");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	public synchronized void eraseTag(final Consumer<String> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		removeAllListener();
		final String id = generateCallerID();
		final EraseTagCall call = new EraseTagCall(successCallback,errorCallback);
		this.eraseTagCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_erase_tag('").append(id).append("');");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	public synchronized void writeText(final String message, final Consumer<String> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		removeAllListener();
		final String id = generateCallerID();
		final WriteCall call = new WriteCall(successCallback,errorCallback);
		this.writeCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_write_text('").append(id).append("','").append(message).append("');");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	public synchronized void removeAllListener()
	{
		// clear all Maps because of highlander
		this.startNdefListenerCalls.clear();
		this.stopNdefListenerCalls.clear();
		this.startTagDiscoveredListenerCalls.clear();
		this.stopTagDiscoveredListenerCalls.clear();
		this.eraseTagCalls.clear();
		this.writeCalls.clear();
		this.makeReadOnlyCalls.clear();
		this.showSettingsCalls.clear();
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_remove_all_Listener();");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
}
