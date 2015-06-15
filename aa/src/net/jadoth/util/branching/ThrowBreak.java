/**
 *
 */
package net.jadoth.util.branching;

/**
 * Thrown to signals the outer context to break the current loop,
 * normally proceeding with the actions following the loop.
 *
 * @author Thomas Muenz
 *
 */
public class ThrowBreak extends BranchingThrow
{
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	public ThrowBreak()
	{
		super();
	}

	public ThrowBreak(final Throwable cause)
	{
		super(cause);
	}

	public ThrowBreak(final Object hint)
	{
		super(hint);
	}

	public ThrowBreak(final Object hint, final Throwable cause)
	{
		super(hint, cause);
	}



}
