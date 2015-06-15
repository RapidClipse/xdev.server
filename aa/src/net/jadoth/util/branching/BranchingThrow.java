package net.jadoth.util.branching;

/**
 * @author Thomas Muenz
 *
 */
public abstract class BranchingThrow extends RuntimeException
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	private final Object hint;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	protected BranchingThrow()
	{
		super();
		this.hint = null;
	}

	protected BranchingThrow(final Throwable cause)
	{
		super(cause);
		this.hint = null;
	}

	protected BranchingThrow(final Object hint)
	{
		super();
		this.hint = hint;
	}

	protected BranchingThrow(final Object hint, final Throwable cause)
	{
		super(cause);
		this.hint = hint;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	public Object getHint()
	{
		return this.hint;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public synchronized BranchingThrow fillInStackTrace()
	{
		return this;
	}



}
