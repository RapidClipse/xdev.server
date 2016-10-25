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

package com.xdev.mobile.service.compass;


import com.xdev.mobile.service.MobileServiceError;


/**
 * @author XDEV Software
 *
 */
public class CompassServiceError extends MobileServiceError
{
	public static enum Reason
	{
		INTERNAL_ERR(0), COMPASS_NOT_SUPPORTED(20);
		
		private final int code;
		
		
		private Reason(final int code)
		{
			this.code = code;
		}
		
		
		public static Reason getByCode(final int code)
		{
			for(final Reason reason : values())
			{
				if(reason.code == code)
				{
					return reason;
				}
			}
			return null;
		}
	}
	
	private final Reason reason;
	
	
	public CompassServiceError(final CompassService source, final String message,
			final Reason reason)
	{
		super(source,message);
		
		this.reason = reason;
	}
	
	
	@Override
	public CompassService getSource()
	{
		return (CompassService)super.getSource();
	}
	
	
	public Reason getReason()
	{
		return this.reason;
	}
}
