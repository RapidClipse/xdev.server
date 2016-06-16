/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
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

package com.xdev.mobile.service.push;


import java.util.Map;


/**
 * @author XDEV Software
 * 		
 */
public class NotificationData
{
	private final String				message;
	private final String				title;
	private final Map<String, Object>	additionalData;


	public NotificationData(final String message, final String title,
			final Map<String, Object> additionalData)
	{
		this.message = message;
		this.title = title;
		this.additionalData = additionalData;
	}


	public String getMessage()
	{
		return this.message;
	}


	public String getTitle()
	{
		return this.title;
	}


	public Map<String, Object> getAdditionalData()
	{
		return this.additionalData;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "message: " + this.message + "\ntitle: " + this.title + "\nadditionalData: "
				+ this.additionalData;
	}
}
