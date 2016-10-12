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

package com.xdev.security.authorization;


/**
 * An exception type indicating that an authorization was not successful. The cause of such an exception does
 * not necessarily have to be a problem, but is usually just a control flow information, that validation of
 * credentials failed on the business-logical level.
 *
 * @author XDEV Software (TM)
 */
public class AuthorizationException extends RuntimeException
{
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	public AuthorizationException()
	{
		super();
	}

	public AuthorizationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AuthorizationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public AuthorizationException(final String message)
	{
		super(message);
	}

	public AuthorizationException(final Throwable cause)
	{
		super(cause);
	}

}
