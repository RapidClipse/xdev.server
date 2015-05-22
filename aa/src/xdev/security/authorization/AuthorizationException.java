/*
 * XDEV Enterprise Framework
 * 
 * Copyright (c) 2011 - 2014, XDEV Software and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
package xdev.security.authorization;


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

	/**
	 * 
	 */
	private static final long serialVersionUID = 746255165233465200L;

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
