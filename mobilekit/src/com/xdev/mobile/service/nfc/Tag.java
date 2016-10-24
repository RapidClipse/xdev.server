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
import java.util.Collection;


/**
 * @author XDEV Software
 *
 */
public class Tag
{
	private boolean				isWritable;
	private byte[]				id;
	private Collection<String>	techTypes;
	private String				type;
	private boolean				canMakeReadOnly;
	private double				maxSize;
	private Collection<Record>	ndefMessage;


	/**
	 *
	 */
	public Tag()
	{
		super();
	}


	/**
	 * @param isWritable
	 * @param id
	 * @param techTypes
	 * @param type
	 * @param canMakeReadOnly
	 * @param maxSize
	 * @param ndefMessage
	 */
	public Tag(final boolean isWritable, final byte[] id, final Collection<String> techTypes,
			final String type, final boolean canMakeReadOnly, final double maxSize,
			final Collection<Record> ndefMessage)
	{
		super();
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
	 * @param isWritable
	 *            the isWritable to set
	 */
	public void setWritable(final boolean isWritable)
	{
		this.isWritable = isWritable;
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
	 * @return the techTypes
	 */
	public Collection<String> getTechTypes()
	{
		return this.techTypes;
	}


	/**
	 * @param techTypes
	 *            the techTypes to set
	 */
	public void setTechTypes(final Collection<String> techTypes)
	{
		this.techTypes = techTypes;
	}


	/**
	 * @return the type
	 */
	public String getType()
	{
		return this.type;
	}


	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final String type)
	{
		this.type = type;
	}


	/**
	 * @return the canMakeReadOnly
	 */
	public boolean isCanMakeReadOnly()
	{
		return this.canMakeReadOnly;
	}


	/**
	 * @param canMakeReadOnly
	 *            the canMakeReadOnly to set
	 */
	public void setCanMakeReadOnly(final boolean canMakeReadOnly)
	{
		this.canMakeReadOnly = canMakeReadOnly;
	}


	/**
	 * @return the maxSize
	 */
	public double getMaxSize()
	{
		return this.maxSize;
	}


	/**
	 * @param maxSize
	 *            the maxSize to set
	 */
	public void setMaxSize(final double maxSize)
	{
		this.maxSize = maxSize;
	}


	/**
	 * @return the ndefMessage
	 */
	public Collection<Record> getNdefMessage()
	{
		return this.ndefMessage;
	}


	/**
	 * @param ndefMessage
	 *            the ndefMessage to set
	 */
	public void setNdefMessage(final Collection<Record> ndefMessage)
	{
		this.ndefMessage = ndefMessage;
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
