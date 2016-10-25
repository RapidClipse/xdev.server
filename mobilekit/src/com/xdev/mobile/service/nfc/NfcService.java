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
import com.xdev.mobile.config.MobileServiceConfiguration;
import com.xdev.mobile.service.AbstractMobileService;
import com.xdev.mobile.service.MobileServiceError;
import com.xdev.mobile.service.annotations.MobileService;
import com.xdev.mobile.service.annotations.Plugin;

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

@MobileService(plugins = @Plugin(name = "phonegap-nfc", spec = "0.6.6"))
@JavaScript("nfc.js")
public class NfcService extends AbstractMobileService implements NfcServiceAccess
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
	 * @return the nfc service if available
	 */
	public static NfcServiceAccess getInstance()
	{
		return getMobileService(NfcService.class);
	}
	
	
	
	protected static class NfcCallbackServiceCall
			extends ServiceCall.Implementation<String, MobileServiceError>
	{
		private final Consumer<Ndef> callback;
		
		
		public NfcCallbackServiceCall(final Consumer<String> successCallback,
				final Consumer<MobileServiceError> errorCallback, final Consumer<Ndef> callback)
		{
			super(successCallback,errorCallback);
			this.callback = callback;
		}
		
		
		public void callback(final Ndef ndef)
		{
			if(this.callback != null)
			{
				this.callback.accept(ndef);
			}
		}
	}
	
	private final Map<String, NfcCallbackServiceCall>					startNdefListenerCalls			= new HashMap<>();
	private final Map<String, ServiceCall<String, MobileServiceError>>	removeNdefListenerCalls			= new HashMap<>();
	private final Map<String, NfcCallbackServiceCall>					startTagDiscoveredListenerCalls	= new HashMap<>();
	private final Map<String, ServiceCall<String, MobileServiceError>>	stopTagDiscoveredListenerCalls	= new HashMap<>();
	private final Map<String, ServiceCall<String, MobileServiceError>>	eraseTagCalls					= new HashMap<>();
	private final Map<String, ServiceCall<String, MobileServiceError>>	writeCalls						= new HashMap<>();
	private final Map<String, ServiceCall<String, MobileServiceError>>	makeReadOnlyCalls				= new HashMap<>();
	private final Map<String, ServiceCall<String, MobileServiceError>>	showSettingsCalls				= new HashMap<>();
	
	
	public NfcService(final AbstractClientConnector connector,
			final MobileServiceConfiguration configuration)
	{
		super(connector,configuration);
		
		this.addFunction("nfc_startNdefListener_success",this::nfc_startNdefListener_success);
		this.addFunction("nfc_startNdefListener_error",this::nfc_startNdefListener_error);
		this.addFunction("nfc_startNdefListener_callback",this::nfc_startNdefListener_callback);
		
		this.addFunction("nfc_stopNdefListener_success",this::nfc_stopNdefListener_success);
		this.addFunction("nfc_stopNdefListener_error",this::nfc_stopNdefListener_error);
		
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
		
		this.addFunction("nfc_write_success",this::nfc_write_success);
		this.addFunction("nfc_write_error",this::nfc_write_error);
		
		this.addFunction("nfc_makeReadOnly_success",this::nfc_makeReadOnly_success);
		this.addFunction("nfc_makeReadOnly_error",this::nfc_makeReadOnly_error);
		
		this.addFunction("nfc_showSettings_success",this::nfc_showSettings_success);
		this.addFunction("nfc_showSettings_error",this::nfc_showSettings_error);
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.xdev.mobile.service.nfc.NfcServiceInterface#startNdefListener(java.
	 * util.function.Consumer, java.util.function.Consumer,
	 * java.util.function.Consumer)
	 */
	@Override
	public synchronized void startNdefListener(final Consumer<Ndef> callback,
			final Consumer<String> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		removeAllListener();
		
		final String id = generateCallerID();
		final NfcCallbackServiceCall call = new NfcCallbackServiceCall(successCallback,
				errorCallback,callback);
		this.startNdefListenerCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_startNdefListener(").append(toLiteral(id)).append(");");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void nfc_startNdefListener_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final NfcCallbackServiceCall call = this.startNdefListenerCalls.get(id);
		if(call != null)
		{
			call.success(arguments.getString(1));
		}
	}
	
	
	private void nfc_startNdefListener_error(final JsonArray arguments)
	{
		callError(arguments,this.startNdefListenerCalls,true);
	}
	
	
	private void nfc_startNdefListener_callback(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final NfcCallbackServiceCall call = this.startNdefListenerCalls.get(id);
		if(call != null)
		{
			final JsonObject jsonObject = arguments.getObject(1);
			final Ndef ndef = new Gson().fromJson(jsonObject.toJson(),Ndef.class);
			call.callback(ndef);
		}
	}
	
	
	@Override
	public synchronized void stopNdefListener(final Consumer<String> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final ServiceCall<String, MobileServiceError> call = ServiceCall.New(successCallback,
				errorCallback);
		this.removeNdefListenerCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_stopNdefListener(").append(toLiteral(id)).append(");");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void nfc_stopNdefListener_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<String, MobileServiceError> call = this.removeNdefListenerCalls.get(id);
		if(call != null)
		{
			call.success(arguments.getString(1));
		}
	}
	
	
	private void nfc_stopNdefListener_error(final JsonArray arguments)
	{
		callError(arguments,this.removeNdefListenerCalls,true);
	}
	
	
	@Override
	public synchronized void startTagDiscoveredListener(final Consumer<Ndef> callback,
			final Consumer<String> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		removeAllListener();
		
		final String id = generateCallerID();
		final NfcCallbackServiceCall call = new NfcCallbackServiceCall(successCallback,
				errorCallback,callback);
		this.startTagDiscoveredListenerCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_startTagDiscoveredListener(").append(toLiteral(id)).append(");");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void nfc_startTagDiscoveredListener_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final NfcCallbackServiceCall call = this.startTagDiscoveredListenerCalls.get(id);
		if(call != null)
		{
			call.success(arguments.getString(1));
		}
	}
	
	
	private void nfc_startTagDiscoveredListener_error(final JsonArray arguments)
	{
		callError(arguments,this.startTagDiscoveredListenerCalls,true);
	}
	
	
	private void nfc_startTagDiscoveredListener_callback(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final NfcCallbackServiceCall call = this.startTagDiscoveredListenerCalls.get(id);
		if(call != null)
		{
			final JsonObject jsonObject = arguments.getObject(1);
			final Ndef ndef = new Gson().fromJson(jsonObject.toJson(),Ndef.class);
			call.callback(ndef);
		}
	}
	
	
	@Override
	public synchronized void stopTagDiscoveredListener(final Consumer<String> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final ServiceCall<String, MobileServiceError> call = ServiceCall.New(successCallback,
				errorCallback);
		
		this.stopTagDiscoveredListenerCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_stopTagDiscoveredListener(").append(toLiteral(id)).append(");");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void nfc_stopTagDiscoveredListener_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<String, MobileServiceError> call = this.stopTagDiscoveredListenerCalls
				.remove(id);
		if(call != null)
		{
			call.success(arguments.getString(1));
		}
	}
	
	
	private void nfc_stopTagDiscoveredListener_error(final JsonArray arguments)
	{
		callError(arguments,this.stopTagDiscoveredListenerCalls,true);
	}
	
	
	@Override
	public synchronized void eraseTag(final Consumer<String> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		removeAllListener();
		final String id = generateCallerID();
		final ServiceCall<String, MobileServiceError> call = ServiceCall.New(successCallback,
				errorCallback);
		this.eraseTagCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_erase_tag(").append(toLiteral(id)).append(");");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void nfc_erase_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<String, MobileServiceError> call = this.eraseTagCalls.get(id);
		if(call != null)
		{
			call.success(arguments.getString(1));
		}
	}
	
	
	private void nfc_erase_error(final JsonArray arguments)
	{
		callError(arguments,this.eraseTagCalls,true);
	}
	
	
	@Override
	public synchronized void write(final Consumer<String> successCallback,
			final Consumer<MobileServiceError> errorCallback, final Record... records)
	{
		if(records == null || records.length == 0)
		{
			throw new IllegalArgumentException("At least one record is required");
		}
		
		removeAllListener();
		
		final String id = generateCallerID();
		final ServiceCall<String, MobileServiceError> call = ServiceCall.New(successCallback,
				errorCallback);
		this.writeCalls.put(id,call);
		
		final StringBuilder message = new StringBuilder();
		message.append("[");
		for(int i = 0; i < records.length; i++)
		{
			if(i > 0)
			{
				message.append(",");
			}
			final Record record = records[i];
			switch(record.getTypeNameFormat())
			{
				case TNF_WELL_KNOWN:
				{
					switch(record.getRecordType())
					{
						case RTD_TEXT:
						{
							message.append("ndef.textRecord(")
									.append(toLiteral(record.getPayloadAsString())).append(",null,")
									.append(toLiteral(record.getIdAsString())).append(")");
						}
						break;
					
						case RTD_URI:
						{
							message.append("ndef.uriRecord(")
									.append(toLiteral(record.getPayloadAsString())).append(",")
									.append(toLiteral(record.getIdAsString())).append(")");
						}
						break;
					
						default:
							throw new IllegalArgumentException("Unsupported record");
					}
				}
				break;
			
				case TNF_ABSOLUTE_URI:
				{
					message.append("ndef.absoluteUriRecord(")
							.append(toLiteral(record.getTypeAsString())).append(",")
							.append(toLiteral(record.getPayloadAsString())).append(",")
							.append(toLiteral(record.getIdAsString())).append(")");
				}
				break;
			
				case TNF_MIME_MEDIA:
				{
					message.append("ndef.mimeMediaRecord(")
							.append(toLiteral(record.getTypeAsString())).append(",")
							.append(toLiteral(record.getPayloadAsString())).append(",")
							.append(toLiteral(record.getIdAsString())).append(")");
				}
				break;
			
				default:
					throw new IllegalArgumentException("Unsupported record");
			}
		}
		message.append("]");
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_write(").append(toLiteral(id)).append(",").append(message).append(");");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void nfc_write_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<String, MobileServiceError> call = this.writeCalls.get(id);
		if(call != null)
		{
			call.success(arguments.getString(1));
		}
	}
	
	
	private void nfc_write_error(final JsonArray arguments)
	{
		callError(arguments,this.writeCalls,true);
	}
	
	
	@Override
	public synchronized void makeReadOnly(final Consumer<String> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		removeAllListener();
		
		final String id = generateCallerID();
		final ServiceCall<String, MobileServiceError> call = ServiceCall.New(successCallback,
				errorCallback);
		this.makeReadOnlyCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_makeReadOnly(").append(toLiteral(id)).append(");");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void nfc_makeReadOnly_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<String, MobileServiceError> call = this.makeReadOnlyCalls.get(id);
		if(call != null)
		{
			call.success(arguments.getString(1));
		}
	}
	
	
	private void nfc_makeReadOnly_error(final JsonArray arguments)
	{
		callError(arguments,this.makeReadOnlyCalls,true);
	}
	
	
	@Override
	public synchronized void showSettings(final Consumer<String> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final ServiceCall<String, MobileServiceError> call = ServiceCall.New(successCallback,
				errorCallback);
		this.showSettingsCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("nfc_showSettings(").append(toLiteral(id)).append(");");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void nfc_showSettings_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<String, MobileServiceError> call = this.showSettingsCalls.remove(id);
		if(call != null)
		{
			call.success(arguments.getString(1));
		}
	}
	
	
	private void nfc_showSettings_error(final JsonArray arguments)
	{
		callError(arguments,this.showSettingsCalls,true);
	}
	
	
	@Override
	public synchronized void removeAllListener()
	{
		// clear all Maps because of highlander
		this.startNdefListenerCalls.clear();
		this.removeNdefListenerCalls.clear();
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
