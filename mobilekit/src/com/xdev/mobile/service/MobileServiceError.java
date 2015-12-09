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

package com.xdev.mobile.service;


/**
 * @author XDEV Software
 * 
 */
public class MobileServiceError
{
	private final MobileService	source;
	private final String		message;
	
	
	public MobileServiceError(final MobileService source, final String message)
	{
		this.source = source;
		this.message = message;
	}
	
	
	public MobileService getSource()
	{
		return this.source;
	}


	public String getMessage()
	{
		return this.message;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "MobileServiceError [source=" + this.source + ", message=" + this.message + "]";
	}
}
