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
 * Search options to filter contacts.
 *
 * @author XDEV Software
 *		
 */
public class ContactFindOptions
{
	private String				filter				= "";
	private ContactFieldType[]	filterFields;
	private boolean				multiple			= true;
	private boolean				mustHavePhoneNumber	= false;
	private ContactFieldType[]	desiredFields;
								
								
	public ContactFindOptions()
	{
	}
	
	
	/**
	 *
	 * @param filter
	 *            The search string used to find contacts
	 * @param filterFields
	 *            [optional] Fields to search in
	 * @return
	 */
	public ContactFindOptions filter(final String filter, final ContactFieldType... filterFields)
	{
		this.filter = filter;
		this.filterFields = filterFields;
		return this;
	}
	
	
	/**
	 * The search only returns the first result.
	 *
	 * @see #multipleResults()
	 */
	public ContactFindOptions singleResult()
	{
		this.multiple = false;
		return this;
	}
	
	
	/**
	 * The search returns all results.
	 *
	 * @see #singleResult()
	 */
	public ContactFindOptions multipleResults()
	{
		this.multiple = true;
		return this;
	}
	
	
	/**
	 * Filters the search to only return contacts with a phone number informed.
	 * (Android only)
	 */
	public ContactFindOptions mustHavePhoneNumer()
	{
		this.mustHavePhoneNumber = true;
		return this;
	}
	
	
	/**
	 * Limit contact fields to be returned back.
	 */
	public ContactFindOptions returnFields(final ContactFieldType... fieldTypes)
	{
		this.desiredFields = fieldTypes;
		return this;
	}
	
	
	public String getFilter()
	{
		return this.filter;
	}
	
	
	public ContactFieldType[] getFilterFields()
	{
		return this.filterFields;
	}
	
	
	public boolean isMultiple()
	{
		return this.multiple;
	}
	
	
	public boolean isMustHavePhoneNumber()
	{
		return this.mustHavePhoneNumber;
	}
	
	
	public ContactFieldType[] getDesiredFields()
	{
		return this.desiredFields;
	}
}
