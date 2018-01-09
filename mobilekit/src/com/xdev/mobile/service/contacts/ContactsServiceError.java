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

package com.xdev.mobile.service.contacts;


import com.xdev.mobile.service.MobileServiceError;


/**
 * @author XDEV Software
 * 
 */
public class ContactsServiceError extends MobileServiceError
{
	public static enum Reason
	{
		UNKNOWN_ERROR(0),
		INVALID_ARGUMENT_ERROR(1),
		TIMEOUT_ERROR(2),
		PENDING_OPERATION_ERROR(3),
		IO_ERROR(4),
		NOT_SUPPORTED_ERROR(5),
		OPERATION_CANCELLED_ERROR(6),
		PERMISSION_DENIED_ERROR(20);
		
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
	
	
	public ContactsServiceError(final ContactsService source, final String message,
			final Reason reason)
	{
		super(source,message);
		
		this.reason = reason;
	}


	@Override
	public ContactsService getSource()
	{
		return (ContactsService)super.getSource();
	}
	
	
	public Reason getReason()
	{
		return this.reason;
	}
}
