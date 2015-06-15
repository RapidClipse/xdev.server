package net.jadoth.exceptions;

import java.io.IOException;

public class IORuntimeException extends WrapperRuntimeException
{
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * 
	 */
	private static final long serialVersionUID = 2757080605093907417L;



	public IORuntimeException(final IOException actual)
	{
		super(actual);
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public IOException getActual()
	{
		return (IOException)super.getActual(); // safe via constructor
	}



}

