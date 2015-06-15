package net.jadoth.collections;

public class CapacityExceededException extends IndexExceededException
{
	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * 
	 */
	private static final long serialVersionUID = 322240304189651283L;

	public CapacityExceededException()
	{
		super();
	}

	public CapacityExceededException(final String message)
	{
		super(message);
	}

	public CapacityExceededException(final int bound, final int index, final String message)
	{
		super(bound, index, message);
	}

	public CapacityExceededException(final int bound, final int index)
	{
		super(bound, index);
	}



}
