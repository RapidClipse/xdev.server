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


import java.util.Arrays;


/**
 * @author XDEV Software
 *
 */
public class NdefMessage
{
	private int		tnf;
	private byte[]	type;
	private byte[]	id;
	private byte[]	payload;
					
					
	/**
	 *
	 */
	public NdefMessage()
	{
		super();
	}


	/**
	 * @param tnf
	 * @param type
	 * @param id
	 * @param payload
	 */
	public NdefMessage(final int tnf, final byte[] type, final byte[] id, final byte[] payload)
	{
		super();
		this.tnf = tnf;
		this.type = type;
		this.id = id;
		this.payload = payload;
	}


	/**
	 * @return the tnf
	 */
	public int getTnf()
	{
		return this.tnf;
	}
	
	
	/**
	 * @param tnf
	 *            the tnf to set
	 */
	public void setTnf(final int tnf)
	{
		this.tnf = tnf;
	}
	
	
	/**
	 * @return the type
	 */
	public byte[] getType()
	{
		return this.type;
	}
	
	
	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final byte[] type)
	{
		this.type = type;
	}
	
	
	/**
	 * @return the id
	 */
	public byte[] getId()
	{
		return this.id;
	}
	
	
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final byte[] id)
	{
		this.id = id;
	}
	
	
	/**
	 * @return the payload
	 */
	public byte[] getPayload()
	{
		return this.payload;
	}
	
	
	/**
	 * @param payload
	 *            the payload to set
	 */
	public void setPayload(final byte[] payload)
	{
		this.payload = payload;
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "NdefMessage [tnf=" + this.tnf + ", type=" + Arrays.toString(this.type) + ", id="
				+ Arrays.toString(this.id) + ", payload=" + Arrays.toString(this.payload) + "]";
	}
	
}
