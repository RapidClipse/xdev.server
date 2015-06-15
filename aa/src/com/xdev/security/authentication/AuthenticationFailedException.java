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

package com.xdev.security.authentication;


/**
 * Exception type to indicate that an authentication attempt failed on a business-logical level 
 * (e.g. wrong username/password).
 * 
 * @author XDEV Software (TM)
 */
public class AuthenticationFailedException extends RuntimeException
{
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * 
	 */
	private static final long serialVersionUID = -1682654011785451553L;



	public AuthenticationFailedException()
	{
		super();
	}

	public AuthenticationFailedException(
		final String    message           ,
		final Throwable cause             ,
		final boolean   enableSuppression ,
		final boolean   writableStackTrace
	)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AuthenticationFailedException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public AuthenticationFailedException(final String message)
	{
		super(message);
	}

	public AuthenticationFailedException(final Throwable cause)
	{
		super(cause);
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	// methods resolving the conflict between querying the message and assembling the message
	public final String message()
	{
		return super.getMessage();
	}

	public String assembleDetailString()
	{
		return "Authentication failed.";
	}

	protected String assembleExplicitMessageAddon()
	{
		final String explicitMessage = super.getMessage();
		return explicitMessage == null ?"" :" ("+explicitMessage+")";
	}

	public String assembleOutputString()
	{
		return this.assembleDetailString() + this.assembleExplicitMessageAddon();
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * Returns an assembled output String due to bad method design in {@link Throwable}.
	 * For the actual message getter, see {@link #message()}.
	 *
	 * @return this exception type's generic string plus an explicit message if present.
	 */
	@Override
	public String getMessage() // intentionally not final to enable subclasses to change the behaviour again
	{
		return this.assembleOutputString();
	}

}
