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


/**
 * The ContactAddress object stores the properties of a single address of a
 * contact. A {@link Contact} object may include more than one address in a
 * ContactAddress list.
 *
 * @author XDEV Software
 *
 */
public class ContactAddress
{
	private String	id;
	private boolean	pref;
	private String	type;
	private String	formatted;
	private String	streetAddress;
	private String	locality;
	private String	region;
	private String	postalCode;
	private String	country;


	public ContactAddress()
	{
	}


	public ContactAddress(final String id, final boolean pref, final String type,
			final String formatted, final String streetAddress, final String locality,
			final String region, final String postalCode, final String country)
	{
		this.id = id;
		this.pref = pref;
		this.type = type;
		this.formatted = formatted;
		this.streetAddress = streetAddress;
		this.locality = locality;
		this.region = region;
		this.postalCode = postalCode;
		this.country = country;
	}


	public String getId()
	{
		return this.id;
	}


	public void setId(final String id)
	{
		this.id = id;
	}


	/**
	 * Set to <code>true</code> if this ContactAddress contains the user's
	 * preferred value.
	 */
	public boolean isPref()
	{
		return this.pref;
	}
	
	
	/**
	 * Set to <code>true</code> if this ContactAddress contains the user's
	 * preferred value.
	 */
	public void setPref(final boolean pref)
	{
		this.pref = pref;
	}
	
	
	/**
	 * A string indicating what type of field this is, &quot;home&quot; for
	 * example.
	 */
	public String getType()
	{
		return this.type;
	}
	
	
	/**
	 * A string indicating what type of field this is, &quot;home&quot; for
	 * example.
	 */
	public void setType(final String type)
	{
		this.type = type;
	}
	
	
	/**
	 * The full address formatted for display.
	 */
	public String getFormatted()
	{
		return this.formatted;
	}
	
	
	/**
	 * The full address formatted for display.
	 */
	public void setFormatted(final String formatted)
	{
		this.formatted = formatted;
	}
	
	
	/**
	 * The full street address.
	 */
	public String getStreetAddress()
	{
		return this.streetAddress;
	}
	
	
	/**
	 * The full street address.
	 */
	public void setStreetAddress(final String streetAddress)
	{
		this.streetAddress = streetAddress;
	}
	
	
	/**
	 * The city or locality.
	 */
	public String getLocality()
	{
		return this.locality;
	}
	
	
	/**
	 * The city or locality.
	 */
	public void setLocality(final String locality)
	{
		this.locality = locality;
	}
	
	
	/**
	 * The state or region.
	 */
	public String getRegion()
	{
		return this.region;
	}
	
	
	/**
	 * The state or region.
	 */
	public void setRegion(final String region)
	{
		this.region = region;
	}
	
	
	/**
	 * The zip code or postal code.
	 */
	public String getPostalCode()
	{
		return this.postalCode;
	}
	
	
	/**
	 * The zip code or postal code.
	 */
	public void setPostalCode(final String postalCode)
	{
		this.postalCode = postalCode;
	}
	
	
	/**
	 * The country name.
	 */
	public String getCountry()
	{
		return this.country;
	}
	
	
	/**
	 * The country name.
	 */
	public void setCountry(final String country)
	{
		this.country = country;
	}


	@Override
	public String toString()
	{
		if(this.formatted != null && this.formatted.length() > 0)
		{
			return this.formatted;
		}

		return super.toString();
	}
}
