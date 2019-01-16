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

package com.xdev.mobile.service.contacts;


import java.util.List;
import java.util.function.Consumer;


/**
 * @author XDEV Software
 *
 */
public interface ContactsServiceAccess
{
	/**
	 * Finds contacts in the device contacts database.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * <li>Windows Phone 8</li>
	 * <li>Windows</li>
	 * </ul>
	 */
	default public void find(final ContactFindOptions options,
			final Consumer<List<Contact>> successCallback)
	{
		find(options,successCallback,null);
	}


	/**
	 * Finds contacts in the device contacts database.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * <li>Windows Phone 8</li>
	 * <li>Windows</li>
	 * </ul>
	 */
	public void find(ContactFindOptions options, Consumer<List<Contact>> successCallback,
			Consumer<ContactsServiceError> errorCallback);


	/**
	 * Launches the Contact Picker to select a single contact.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * <li>Windows Phone 8</li>
	 * <li>Windows</li>
	 * </ul>
	 */
	default public void pickContact(final Consumer<Contact> successCallback)
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
	 * <li>Windows</li>
	 * </ul>
	 */
	public void pickContact(Consumer<Contact> successCallback,
			Consumer<ContactsServiceError> errorCallback);


	/**
	 * Saves a new contact to the device contacts database, or updates an
	 * existing contact if a contact with the same id already exists.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * <li>Windows Phone 8</li>
	 * <li>Windows</li>
	 * </ul>
	 */
	default public void save(final Contact contact, final Consumer<Contact> successCallback)
	{
		save(contact,successCallback,null);
	}


	/**
	 * Saves a new contact to the device contacts database, or updates an
	 * existing contact if a contact with the same id already exists.
	 * <p>
	 * Supported platforms:
	 * <ul>
	 * <li>Android</li>
	 * <li>iOS</li>
	 * <li>Windows Phone 8</li>
	 * <li>Windows</li>
	 * </ul>
	 */
	public void save(Contact contact, Consumer<Contact> successCallback,
			Consumer<ContactsServiceError> errorCallback);
}
