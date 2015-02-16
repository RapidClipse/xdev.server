
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
