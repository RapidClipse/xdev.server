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
 
package com.xdev.server.lang;


public class CommandException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -978450443156223666L;
	
	
	public CommandException()
	{
		super();
	}
	
	
	public CommandException(String message, Throwable cause)
	{
		super(message,cause);
	}
	
	
	public CommandException(String message)
	{
		super(message);
	}
	
	
	public CommandException(Throwable cause)
	{
		super(cause);
	}
	
	
	public static void throwMissingParameter(String name) throws CommandException
	{
		throw new CommandException("Missing parameter: " + name);
	}
}
