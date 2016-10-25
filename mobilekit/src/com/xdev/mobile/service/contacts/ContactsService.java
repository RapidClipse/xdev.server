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
import com.xdev.mobile.config.MobileServiceConfiguration;
import com.xdev.mobile.service.AbstractMobileService;
import com.xdev.mobile.service.annotations.MobileService;
import com.xdev.mobile.service.annotations.Plugin;
import com.xdev.mobile.service.contacts.ContactsServiceError.Reason;

import elemental.json.JsonArray;
import elemental.json.JsonObject;


/**
 * Service which provides access to the device contacts database.
 * <p>
 * <b>Warning:</b><br>
 * Collection and use of contact data raises important privacy issues. Your
 * app's privacy policy should discuss how the app uses contact data and whether
 * it is shared with any other parties. Contact information is considered
 * sensitive because it reveals the people with whom a person communicates.
 * Therefore, in addition to the app's privacy policy, you should strongly
 * consider providing a just-in-time notice before the app accesses or uses
 * contact data, if the device operating system doesn't do so already. That
 * notice should provide the same information noted above, as well as obtaining
 * the user's permission (e.g., by presenting choices for OK and No Thanks).
 * Note that some app marketplaces may require the app to provide a just-in-time
 * notice and obtain the user's permission before accessing contact data. A
 * clear and easy-to-understand user experience surrounding the use of contact
 * data helps avoid user confusion and perceived misuse of contact data. For
 * more information, please see the <a href=
 * "http://cordova.apache.org/docs/en/latest/guide/appdev/privacy/index.html">
 * Privacy Guide</a>.
 *
 *
 * @author XDEV Software
 *
 */
@MobileService(plugins = @Plugin(name = "cordova-plugin-contacts", spec = "2.2.0"))
@JavaScript("contacts.js")
public class ContactsService extends AbstractMobileService implements ContactsServiceAccess
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
	 * @return the contacts service if available
	 */
	public static ContactsServiceAccess getInstance()
	{
		return getMobileService(ContactsService.class);
	}

	private final Map<String, ServiceCall<List<Contact>, ContactsServiceError>>	findCalls	= new HashMap<>();
	private final Map<String, ServiceCall<Contact, ContactsServiceError>>		pickCalls	= new HashMap<>();
	private final Map<String, ServiceCall<Contact, ContactsServiceError>>		saveCalls	= new HashMap<>();


	public ContactsService(final AbstractClientConnector connector,
			final MobileServiceConfiguration configuration)
	{
		super(connector,configuration);

		this.addFunction("contacts_find_success",this::contacts_find_success);
		this.addFunction("contacts_find_error",this::contacts_find_error);

		this.addFunction("contacts_pick_success",this::contacts_pick_success);
		this.addFunction("contacts_pick_error",this::contacts_pick_error);

		this.addFunction("contacts_save_success",this::contacts_save_success);
		this.addFunction("contacts_save_error",this::contacts_save_error);
	}


	@Override
	public synchronized void find(final ContactFindOptions options,
			final Consumer<List<Contact>> successCallback,
			final Consumer<ContactsServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final ServiceCall<List<Contact>, ContactsServiceError> call = ServiceCall
				.New(successCallback,errorCallback);
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
		final ServiceCall<List<Contact>, ContactsServiceError> call = this.findCalls.remove(id);
		if(call != null)
		{
			final List<Contact> contacts = new ArrayList<Contact>();

			final JsonArray arrayData = arguments.get(1);
			for(int i = 0; i < arrayData.length(); i++)
			{
				final Gson gson = new Gson();
				final JsonObject jsonObject = arrayData.get(i);
				final Contact contact = gson.fromJson(jsonObject.toJson(),Contact.class);
				contacts.add(contact);
			}

			call.success(contacts);
		}
	}


	private void contacts_find_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<List<Contact>, ContactsServiceError> call = this.findCalls.remove(id);
		if(call != null)
		{
			call.error(createContactsServiceError(arguments.get(1).asString()));
		}
	}


	@Override
	public void pickContact(final Consumer<Contact> successCallback,
			final Consumer<ContactsServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final ServiceCall<Contact, ContactsServiceError> call = ServiceCall.New(successCallback,
				errorCallback);
		this.pickCalls.put(id,call);

		final StringBuilder js = new StringBuilder();
		js.append("contacts_pick('").append(id).append("');");

		Page.getCurrent().getJavaScript().execute(js.toString());
	}


	private void contacts_pick_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<Contact, ContactsServiceError> call = this.pickCalls.remove(id);
		if(call != null)
		{
			final Gson gson = new Gson();
			final JsonObject jsonObject = arguments.getObject(1);
			final Contact contact = gson.fromJson(jsonObject.toJson(),Contact.class);

			call.success(contact);
		}
	}


	private void contacts_pick_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<Contact, ContactsServiceError> call = this.pickCalls.remove(id);
		if(call != null)
		{
			call.error(createContactsServiceError(arguments.get(1).asString()));
		}
	}


	@Override
	public void save(final Contact contact, final Consumer<Contact> successCallback,
			final Consumer<ContactsServiceError> errorCallback)
	{
		final String id = generateCallerID();
		final ServiceCall<Contact, ContactsServiceError> call = ServiceCall.New(successCallback,
				errorCallback);
		this.saveCalls.put(id,call);

		final Gson gson = new Gson();
		final String json = gson.toJson(contact);
		final StringBuilder js = new StringBuilder();
		js.append("contacts_save('").append(id).append("','").append(json).append("')");

		Page.getCurrent().getJavaScript().execute(js.toString());
	}


	private void contacts_save_success(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<Contact, ContactsServiceError> call = this.saveCalls.remove(id);
		if(call != null)
		{
			final Gson gson = new Gson();
			final JsonObject jsonObject = arguments.getObject(1);
			final Contact contact = gson.fromJson(jsonObject.toJson(),Contact.class);

			call.success(contact);
		}
	}


	private void contacts_save_error(final JsonArray arguments)
	{
		final String id = arguments.getString(0);
		final ServiceCall<Contact, ContactsServiceError> call = this.saveCalls.remove(id);
		if(call != null)
		{
			call.error(createContactsServiceError(arguments.get(1).asString()));
		}
	}


	private ContactsServiceError createContactsServiceError(final String message)
	{
		Reason reason = null;

		try
		{
			final int code = Integer.parseInt(message);
			reason = Reason.getByCode(code);
		}
		catch(final Exception e)
		{
			// swallow
		}

		return new ContactsServiceError(this,message,reason);
	}
}
