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


/**
 * @author XDEV Software
 *
 */
public class Ndef
{
	private boolean	isTrusted;
	private Tag		tag;


	public Ndef()
	{
		super();
	}


	/**
	 * @param isTrusted
	 * @param tag
	 */
	public Ndef(final boolean isTrusted, final Tag tag)
	{
		super();
		this.isTrusted = isTrusted;
		this.tag = tag;
	}
	
	
	/**
	 * @return the isTrusted
	 */
	public boolean isTrusted()
	{
		return this.isTrusted;
	}
	
	
	/**
	 * @param isTrusted
	 *            the isTrusted to set
	 */
	public void setTrusted(final boolean isTrusted)
	{
		this.isTrusted = isTrusted;
	}
	
	
	/**
	 * @return the tag
	 */
	public Tag getTag()
	{
		return this.tag;
	}
	
	
	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(final Tag tag)
	{
		this.tag = tag;
	}
	
	
	@Override
	public String toString()
	{
		return "Ndef [isTrusted=" + this.isTrusted + ", tag=" + this.tag + "]";
	}
	
}
