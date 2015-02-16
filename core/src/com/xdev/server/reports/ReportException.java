package com.xdev.server.reports;


public class ReportException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3595929821122975443L;


	public ReportException()
	{
		super();
	}
	

	public ReportException(String message, Throwable cause)
	{
		super(message,cause);
	}
	

	public ReportException(String message)
	{
		super(message);
	}
	

	public ReportException(Throwable cause)
	{
		super(cause);
	}
}
