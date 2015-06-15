/**
 *
 */
package net.jadoth.exceptions;

/**
 * @author Thomas Muenz
 *
 */
public class NumberRangeException extends RuntimeException
{
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	/**
	 * 
	 */
	private static final long serialVersionUID = 8920723046337228300L;

	public NumberRangeException()
	{
		super();
	}

	public NumberRangeException(final String message)
	{
		super(message);
	}

	public NumberRangeException(final Throwable cause)
	{
		super(cause);
	}

	public NumberRangeException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

}
