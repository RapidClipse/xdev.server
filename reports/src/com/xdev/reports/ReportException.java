/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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

package com.xdev.reports;


/**
 * @author XDEV Software
 *
 */
public class ReportException extends RuntimeException
{
	public ReportException()
	{
		super();
	}
	
	
	public ReportException(final String message, final Throwable cause,
			final boolean enableSuppression, final boolean writableStackTrace)
	{
		super(message,cause,enableSuppression,writableStackTrace);
	}
	
	
	public ReportException(final String message, final Throwable cause)
	{
		super(message,cause);
	}
	
	
	public ReportException(final String message)
	{
		super(message);
	}
	
	
	public ReportException(final Throwable cause)
	{
		super(cause);
	}
}
