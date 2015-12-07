/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
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
 */

package com.xdev.mobile.service.contacts;


/**
 * @author XDEV Software
 *
 */
public enum ContactFieldType
{
	ADDRESSES("addresses"),
	BIRTHDAY("birthday"),
	CATEGORIES("categories"),
	COUNTRY("country"),
	DEPARTMENT("department"),
	DISPLAY_NAME("displayName"),
	EMAILS("emails"),
	FAMILY_NAME("familyName"),
	FORMATTED("formatted"),
	GIVEN_NAME("givenName"),
	HONORIFIC_PREFIX("honorificPrefix"),
	HONORIFICS_UFFIX("honorificSuffix"),
	ID("id"),
	IMS("ims"),
	LOCALITY("locality"),
	MIDDLE_NAME("middleName"),
	NAME("name"),
	NICKNAME("nickname"),
	NOTE("note"),
	ORGANIZATIONS("organizations"),
	PHONE_NUMBERS("phoneNumbers"),
	PHOTOS("photos"),
	POSTAL_CODE("postalCode"),
	REGION("region"),
	STREET_ADDRESS("streetAddress"),
	TITLE("title"),
	URLS("urls");

	private String fieldName;


	private ContactFieldType(final String fieldName)
	{
		this.fieldName = fieldName;
	}


	public String getFieldName()
	{
		return this.fieldName;
	}
}
