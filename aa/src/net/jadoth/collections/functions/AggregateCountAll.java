package net.jadoth.collections.functions;

import net.jadoth.functional.Aggregator;

public class AggregateCountAll<E> implements Aggregator<E, Integer>
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	private int count = 0;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	public AggregateCountAll()
	{
		super();
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public final void accept(final E element)
	{
		this.count++;
	}

	@Override
	public final Integer yield()
	{
		return this.count;
	}

}
