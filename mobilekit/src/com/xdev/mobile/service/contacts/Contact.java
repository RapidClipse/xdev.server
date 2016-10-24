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


/**
 * @author XDEV Software
 *		
 */
public class Contact
{
	private String							id;
	private String							rawId;
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
	
	
	public Contact(final String id, final String rawId, final String displayName,
			final ContactName name, final String nickname,
			final Collection<ContactField> phoneNumbers, final Collection<ContactField> emails,
			final Collection<ContactAddress> addresses, final Collection<ContactField> ims,
			final Collection<ContactOrganization> organizations, final String birthday,
			final String note, final Collection<ContactField> photos,
			final Collection<ContactField> categories, final Collection<ContactField> urls)
	{
		this.id = id;
		this.rawId = rawId;
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
	
	
	public String getId()
	{
		return this.id;
	}
	
	
	public void setId(final String id)
	{
		this.id = id;
	}
	
	
	public String getRawId()
	{
		return this.rawId;
	}
	
	
	public void setRawId(final String rawId)
	{
		this.rawId = rawId;
	}
	
	
	public String getDisplayName()
	{
		return this.displayName;
	}
	
	
	public void setDisplayName(final String displayName)
	{
		this.displayName = displayName;
	}
	
	
	public ContactName getName()
	{
		return this.name;
	}
	
	
	public void setName(final ContactName name)
	{
		this.name = name;
	}
	
	
	public String getNickname()
	{
		return this.nickname;
	}
	
	
	public void setNickname(final String nickname)
	{
		this.nickname = nickname;
	}
	
	
	public Collection<ContactField> getPhoneNumbers()
	{
		return this.phoneNumbers;
	}
	
	
	public void setPhoneNumbers(final Collection<ContactField> phoneNumbers)
	{
		this.phoneNumbers = phoneNumbers;
	}
	
	
	public Collection<ContactField> getEmails()
	{
		return this.emails;
	}
	
	
	public void setEmails(final Collection<ContactField> emails)
	{
		this.emails = emails;
	}
	
	
	public Collection<ContactAddress> getAddresses()
	{
		return this.addresses;
	}
	
	
	public void setAddresses(final Collection<ContactAddress> addresses)
	{
		this.addresses = addresses;
	}
	
	
	public Collection<ContactField> getIms()
	{
		return this.ims;
	}
	
	
	public void setIms(final Collection<ContactField> ims)
	{
		this.ims = ims;
	}
	
	
	public Collection<ContactOrganization> getOrganizations()
	{
		return this.organizations;
	}
	
	
	public void setOrganizations(final Collection<ContactOrganization> organizations)
	{
		this.organizations = organizations;
	}
	
	
	public String getBirthday()
	{
		return this.birthday;
	}
	
	
	public void setBirthday(final String birthday)
	{
		this.birthday = birthday;
	}
	
	
	public String getNote()
	{
		return this.note;
	}
	
	
	public void setNote(final String note)
	{
		this.note = note;
	}
	
	
	public Collection<ContactField> getPhotos()
	{
		return this.photos;
	}
	
	
	public void setPhotos(final Collection<ContactField> photos)
	{
		this.photos = photos;
	}
	
	
	public Collection<ContactField> getCategories()
	{
		return this.categories;
	}
	
	
	public void setCategories(final Collection<ContactField> categories)
	{
		this.categories = categories;
	}
	
	
	public Collection<ContactField> getUrls()
	{
		return this.urls;
	}
	
	
	public void setUrls(final Collection<ContactField> urls)
	{
		this.urls = urls;
	}
	
	
	@Override
	public String toString()
	{
		if(this.displayName != null && this.displayName.length() > 0)
		{
			return this.displayName;
		}
		
		return super.toString();
	}
}
