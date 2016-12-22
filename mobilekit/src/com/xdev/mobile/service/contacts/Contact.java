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


import java.util.Collection;

import org.apache.commons.lang3.StringUtils;


/**
 * The Contact object represents a user's contact. Contacts can be created,
 * stored, or removed from the device contacts database. Contacts can also be
 * retrieved (individually or in bulk) from the database by invoking the
 * {@link ContactsService#find(ContactFindOptions, java.util.function.Consumer, java.util.function.Consumer)}
 * method.
 *
 * @author XDEV Software
 *
 */
public class Contact
{
	private String							id;
	private String							displayName;
	private ContactName						name;
	private String							nickname;
	private Collection<ContactField>		phoneNumbers;
	private Collection<ContactField>		emails;
	private Collection<ContactAddress>		addresses;
	private Collection<ContactField>		ims;
	private Collection<ContactOrganization>	organizations;
	private String							birthday;
	private String							note;
	private Collection<ContactField>		photos;
	private Collection<ContactField>		categories;
	private Collection<ContactField>		urls;


	public Contact()
	{
	}


	public Contact(final String id, final String displayName, final ContactName name,
			final String nickname, final Collection<ContactField> phoneNumbers,
			final Collection<ContactField> emails, final Collection<ContactAddress> addresses,
			final Collection<ContactField> ims, final Collection<ContactOrganization> organizations,
			final String birthday, final String note, final Collection<ContactField> photos,
			final Collection<ContactField> categories, final Collection<ContactField> urls)
	{
		this.id = id;
		this.displayName = displayName;
		this.name = name;
		this.nickname = nickname;
		this.phoneNumbers = phoneNumbers;
		this.emails = emails;
		this.addresses = addresses;
		this.ims = ims;
		this.organizations = organizations;
		this.birthday = birthday;
		this.note = note;
		this.photos = photos;
		this.categories = categories;
		this.urls = urls;
	}


	/**
	 * A globally unique identifier.
	 */
	public String getId()
	{
		return this.id;
	}


	/**
	 * A globally unique identifier.
	 */
	public Contact setId(final String id)
	{
		this.id = id;
		return this;
	}


	/**
	 * The name of this Contact, suitable for display to end users.
	 */
	public String getDisplayName()
	{
		return this.displayName;
	}


	/**
	 * The name of this Contact, suitable for display to end users.
	 */
	public Contact setDisplayName(final String displayName)
	{
		this.displayName = displayName;
		return this;
	}


	/**
	 * An object containing all components of a persons name.
	 */
	public ContactName getName()
	{
		return this.name;
	}


	/**
	 * An object containing all components of a persons name.
	 */
	public Contact setName(final ContactName name)
	{
		this.name = name;
		return this;
	}


	/**
	 * A casual name by which to address the contact.
	 */
	public String getNickname()
	{
		return this.nickname;
	}


	/**
	 * A casual name by which to address the contact.
	 */
	public Contact setNickname(final String nickname)
	{
		this.nickname = nickname;
		return this;
	}


	/**
	 * A list of all the contact's phone numbers.
	 */
	public Collection<ContactField> getPhoneNumbers()
	{
		return this.phoneNumbers;
	}


	/**
	 * A list of all the contact's phone numbers.
	 */
	public Contact setPhoneNumbers(final Collection<ContactField> phoneNumbers)
	{
		this.phoneNumbers = phoneNumbers;
		return this;
	}


	/**
	 * A list of all the contact's email addresses.
	 */
	public Collection<ContactField> getEmails()
	{
		return this.emails;
	}


	/**
	 * A list of all the contact's email addresses.
	 */
	public Contact setEmails(final Collection<ContactField> emails)
	{
		this.emails = emails;
		return this;
	}


	/**
	 * A list of all the contact's addresses.
	 */
	public Collection<ContactAddress> getAddresses()
	{
		return this.addresses;
	}


	/**
	 * A list of all the contact's addresses.
	 */
	public Contact setAddresses(final Collection<ContactAddress> addresses)
	{
		this.addresses = addresses;
		return this;
	}


	/**
	 * A list of all the contact's IM addresses.
	 */
	public Collection<ContactField> getIms()
	{
		return this.ims;
	}


	/**
	 * A list of all the contact's IM addresses.
	 */
	public Contact setIms(final Collection<ContactField> ims)
	{
		this.ims = ims;
		return this;
	}


	/**
	 * A list of all the contact's organizations.
	 */
	public Collection<ContactOrganization> getOrganizations()
	{
		return this.organizations;
	}


	/**
	 * A list of all the contact's organizations.
	 */
	public Contact setOrganizations(final Collection<ContactOrganization> organizations)
	{
		this.organizations = organizations;
		return this;
	}


	/**
	 * The birthday of the contact.
	 */
	public String getBirthday()
	{
		return this.birthday;
	}


	/**
	 * The birthday of the contact.
	 */
	public Contact setBirthday(final String birthday)
	{
		this.birthday = birthday;
		return this;
	}


	/**
	 * A note about the contact.
	 */
	public String getNote()
	{
		return this.note;
	}


	/**
	 * A note about the contact.
	 */
	public Contact setNote(final String note)
	{
		this.note = note;
		return this;
	}


	/**
	 * A list of the contact's photos.
	 */
	public Collection<ContactField> getPhotos()
	{
		return this.photos;
	}


	/**
	 * A list of the contact's photos.
	 */
	public Contact setPhotos(final Collection<ContactField> photos)
	{
		this.photos = photos;
		return this;
	}


	/**
	 * A list of all the user-defined categories associated with the contact.
	 */
	public Collection<ContactField> getCategories()
	{
		return this.categories;
	}


	/**
	 * A list of all the user-defined categories associated with the contact.
	 */
	public Contact setCategories(final Collection<ContactField> categories)
	{
		this.categories = categories;
		return this;
	}


	/**
	 * A list of web pages associated with the contact.
	 */
	public Collection<ContactField> getUrls()
	{
		return this.urls;
	}


	/**
	 * A list of web pages associated with the contact.
	 */
	public Contact setUrls(final Collection<ContactField> urls)
	{
		this.urls = urls;
		return this;
	}


	@Override
	public String toString()
	{
		if(StringUtils.isNotBlank(this.displayName))
		{
			return this.displayName;
		}

		if(this.name != null && this.name.hasToStringContent())
		{
			return this.name.toString();
		}

		if(StringUtils.isNotBlank(this.nickname))
		{
			return this.nickname;
		}

		return super.toString();
	}
}
