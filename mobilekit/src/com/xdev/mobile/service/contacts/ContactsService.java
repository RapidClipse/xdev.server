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

package com.xdev.mobile.service.contacts;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * Service which provides access to the device contacts database.
 *
 * @author XDEV Software
 *		
 */
@JavaScript("contacts.js")
public class ContactsService extends MobileService
{
	/**
	 * Returns the contacts service.<br>
	 * To activate the service it has to be registered in the mobile.xml.
	 *
	 * <pre>
	 * {@code
	 * ...
	 * <service>com.xdev.mobile.service.contacts.ContactsService</service>
	 * ...
	 * }
	 * </pre>
	 *
	 * @see MobileUI#getMobileService(Class)
	 * @return the contacts service if available
	 */
	public static ContactsService getInstance()
	{
		return getServiceHelper(ContactsService.class);
	}
	
	
	
	private static class FindCall
	{
		final Consumer<List<Contact>>		successCallback;
		final Consumer<MobileServiceError>	errorCallback;
											
											
		FindCall(final Consumer<List<Contact>> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
		}
	}
	
	
	
	private static class PickCall
	{
		final Consumer<Contact>				successCallback;
		final Consumer<MobileServiceError>	errorCallback;
											
											
		PickCall(final Consumer<Contact> successCallback,
				final Consumer<MobileServiceError> errorCallback)
		{
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
		}
	}
	
	private final Map<String, FindCall>	findCalls	= new HashMap<>();
	private final Map<String, PickCall>	pickCalls	= new HashMap<>();
													
													
	public ContactsService(final AbstractClientConnector connector)
	{
		super(connector);
		
		this.addFunction("contacts_find_success",this::contacts_find_success);
		this.addFunction("contacts_find_error",this::contacts_find_error);
		
		this.addFunction("contacts_pick_success",this::contacts_pick_success);
		this.addFunction("contacts_pick_error",this::contacts_pick_error);
	}
	
	
	/**
	 * Finds contacts in the device contacts database.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>BlackBerry 10</li>
	 * <li>Firefox OS</li>
	 * <li>iOS</li>
	 * <li>Windows Phone 8</li>
	 * <li>Windows (Windows Phone 8.1 and Windows 10)</li>
	 * </ul>
	 */
	public synchronized void find(final ContactFindOptions options,
			final Consumer<List<Contact>> successCallback)
	{
		this.find(options,successCallback,null);
	}
	
	
	/**
	 * Finds contacts in the device contacts database.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>BlackBerry 10</li>
	 * <li>Firefox OS</li>
	 * <li>iOS</li>
	 * <li>Windows Phone 8</li>
	 * <li>Windows (Windows Phone 8.1 and Windows 10)</li>
	 * </ul>
	 */
	public synchronized void find(final ContactFindOptions options,
			final Consumer<List<Contact>> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final FindCall call = new FindCall(successCallback,errorCallback);
		this.findCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		appendFields(js,options);
		appendOptions(js,options);
		js.append("contacts_find('").append(id).append("',fields,options);");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void appendFields(final StringBuilder js, final ContactFindOptions options)
	{
		js.append("var fields = [");
		final ContactFieldType[] filterFields = options.getFilterFields();
		if(filterFields == null || filterFields.length == 0)
		{
			js.append("'*'");
		}
		else
		{
			for(int i = 0; i < filterFields.length; i++)
			{
				if(i > 0)
				{
					js.append(",");
				}
				js.append("navigator.contacts.fieldType.");
				js.append(filterFields[i].getFieldName());
			}
		}
		js.append("];\n");
	}
	
	
	private void appendOptions(final StringBuilder js, final ContactFindOptions options)
	{
		js.append("var options = new ContactFindOptions();\n");
		js.append("options.filter = '").append(options.getFilter().replace("'","\\'"))
				.append("';\n");
		js.append("options.multiple = ").append(options.isMultiple()).append(";\n");
		js.append("options.hasPhoneNumber = ").append(options.isMustHavePhoneNumber())
				.append(";\n");
		final ContactFieldType[] desiredFields = options.getDesiredFields();
		if(desiredFields != null && desiredFields.length > 0)
		{
			js.append("options.desiredFields = [");
			for(int i = 0; i < desiredFields.length; i++)
			{
				if(i > 0)
				{
					js.append(",");
				}
				js.append("navigator.contacts.fieldType.");
				js.append(desiredFields[i].getFieldName());
			}
			js.append("];\n");
		}
	}
	
	
	private void contacts_find_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final FindCall call = this.findCalls.remove(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final List<Contact> contacts = new ArrayList<Contact>();
		
		final JsonArray arrayData = arguments.get(1);
		for(int i = 0; i < arrayData.length(); i++)
		{
			final Gson gson = new Gson();
			final JsonObject jsonObject = arrayData.get(i);
			final Contact contact = gson.fromJson(jsonObject.toJson(),Contact.class);
			contacts.add(contact);
		}
		
		call.successCallback.accept(contacts);
	}
	
	
	private void contacts_find_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final FindCall call = this.findCalls.remove(id);
		if(call == null || call.errorCallback == null)
		{
			return;
		}
		
		call.errorCallback.accept(new MobileServiceError(this,arguments.getString(1)));
	}
	
	
	/**
	 * Launches the Contact Picker to select a single contact.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * <li>Windows Phone 8</li>
	 * <li>Windows 8</li>
	 * <li>Windows</li>
	 * </ul>
	 */
	public void pickContact(final Consumer<Contact> successCallback)
	{
		pickContact(successCallback,null);
	}
	
	
	/**
	 * Launches the Contact Picker to select a single contact.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * <li>Windows Phone 8</li>
	 * <li>Windows 8</li>
	 * <li>Windows</li>
	 * </ul>
	 */
	public void pickContact(final Consumer<Contact> successCallback,
			final Consumer<MobileServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final PickCall call = new PickCall(successCallback,errorCallback);
		this.pickCalls.put(id,call);
		
		final StringBuilder js = new StringBuilder();
		js.append("contacts_pick('").append(id).append("');");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	private void contacts_pick_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final PickCall call = this.pickCalls.remove(id);
		if(call == null || call.successCallback == null)
		{
			return;
		}
		
		final Gson gson = new Gson();
		final JsonObject jsonObject = arguments.getObject(1);
		final Contact contact = gson.fromJson(jsonObject.toJson(),Contact.class);
		
		call.successCallback.accept(contact);
	}
	
	
	private void contacts_pick_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final PickCall call = this.pickCalls.remove(id);
		if(call == null || call.errorCallback == null)
		{
			return;
		}
		
		call.errorCallback.accept(new MobileServiceError(this,arguments.getString(1)));
	}
}
