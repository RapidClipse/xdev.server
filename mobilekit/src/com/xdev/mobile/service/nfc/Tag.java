/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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
import java.util.Collection;


/**
 * @author XDEV Software
 *
 */
public class Tag
{
	private final boolean				isWritable;
	private final byte[]				id;
	private final Collection<String>	techTypes;
	private final String				type;
	private final boolean				canMakeReadOnly;
	private final double				maxSize;
	private final Collection<Record>	ndefMessage;
	
	
	Tag(final boolean isWritable, final byte[] id, final Collection<String> techTypes,
			final String type, final boolean canMakeReadOnly, final double maxSize,
			final Collection<Record> ndefMessage)
	{
		this.isWritable = isWritable;
		this.id = id;
		this.techTypes = techTypes;
		this.type = type;
		this.canMakeReadOnly = canMakeReadOnly;
		this.maxSize = maxSize;
		this.ndefMessage = ndefMessage;
	}
	
	
	/**
	 * @return the isWritable
	 */
	public boolean isWritable()
	{
		return this.isWritable;
	}
	
	
	/**
	 * @return the id
	 */
	public byte[] getId()
	{
		return this.id;
	}
	
	
	/**
	 * @return the techTypes
	 */
	public Collection<String> getTechTypes()
	{
		return this.techTypes;
	}
	
	
	/**
	 * @return the type
	 */
	public String getType()
	{
		return this.type;
	}
	
	
	/**
	 * @return the canMakeReadOnly
	 */
	public boolean isCanMakeReadOnly()
	{
		return this.canMakeReadOnly;
	}
	
	
	/**
	 * @return the maxSize
	 */
	public double getMaxSize()
	{
		return this.maxSize;
	}
	
	
	/**
	 * @return the ndefMessage
	 */
	public Collection<Record> getNdefMessage()
	{
		return this.ndefMessage;
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Tag [isWritable=" + this.isWritable + ", id=" + Arrays.toString(this.id)
				+ ", techTypes=" + this.techTypes + ", type=" + this.type + ", canMakeReadOnly="
				+ this.canMakeReadOnly + ", maxSize=" + this.maxSize + ", ndefMessage="
				+ this.ndefMessage + "]";
	}
}
