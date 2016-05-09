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

package com.xdev.mobile.service.file;


import com.xdev.mobile.service.MobileService;
import com.xdev.mobile.service.MobileServiceError;


/**
 * @author XDEV Software
 *
 */
public class FileServiceError extends MobileServiceError
{
	public static enum Reason
	{
		NOT_FOUND_ERR(1),
		SECURITY_ERR(2),
		ABORT_ERR(3),
		NOT_READABLE_ERR(4),
		ENCODING_ERR(5),
		NO_MODIFICATION_ALLOWED_ERR(6),
		INVALID_STATE_ERR(7),
		SYNTAX_ERR(8),
		INVALID_MODIFICATION_ERR(9),
		QUOTA_EXCEEDED_ERR(10),
		TYPE_MISMATCH_ERR(11),
		PATH_EXISTS_ERR(12);
		
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
	
	
	public FileServiceError(final MobileService source, final String message, final Reason reason)
	{
		super(source,message);
		
		this.reason = reason;
	}
	
	
	public Reason getReason()
	{
		return this.reason;
	}
}
