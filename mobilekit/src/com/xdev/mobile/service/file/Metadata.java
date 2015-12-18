/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xdev.mobile.service.file;


import java.util.Date;


/**
 * @author XDEV Software
 *
 */
public class Metadata
{
	
	private Date	modificationTime;


	public Metadata()
	{
		
	}


	/**
	 * @param modificationTime
	 */
	public Metadata(final Date modificationTime)
	{
		super();
		this.modificationTime = modificationTime;
	}
	
	
	/**
	 * @return the modificationTime
	 */
	public Date getModificationTime()
	{
		return this.modificationTime;
	}
	
	
	/**
	 * @param modificationTime
	 *            the modificationTime to set
	 */
	public void setModificationTime(final Date modificationTime)
	{
		this.modificationTime = modificationTime;
	}
	
}
