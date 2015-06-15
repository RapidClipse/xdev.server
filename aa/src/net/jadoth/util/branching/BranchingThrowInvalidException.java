/**
 * 
 */
package net.jadoth.util.branching;

/**
 * @author Thomas Muenz
 *
 */
public class BranchingThrowInvalidException extends RuntimeException
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////
	
	/**
	 * 
	 */

	
	
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////
	
	/**
	 * 
	 */
	public BranchingThrowInvalidException()
	{
		super();
	}

	/**
	 * @param message
	 */
	public BranchingThrowInvalidException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public BranchingThrowInvalidException(BranchingThrow cause)
	{
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BranchingThrowInvalidException(String message, BranchingThrow cause)
	{
		super(message, cause);
	}

}
