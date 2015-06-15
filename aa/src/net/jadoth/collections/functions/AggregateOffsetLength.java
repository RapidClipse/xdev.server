package net.jadoth.collections.functions;

import net.jadoth.Jadoth;
import net.jadoth.functional.Aggregator;

public final class AggregateOffsetLength<E, R> implements Aggregator<E, R>
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	private int offset;
	private int length;
	private final Aggregator<? super E, R> aggregate;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	public AggregateOffsetLength(final int offset, final int length, final Aggregator<? super E, R> aggregate)
	{
		super();
		this.offset    = offset   ;
		this.length    = length   ;
		this.aggregate = aggregate;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public final void accept(final E element)
	{
		if(this.offset > 0){
			this.offset--;
			return;
		}
		this.aggregate.accept(element);
		if(--this.length == 0) throw Jadoth.BREAK;
	}

	@Override
	public final R yield()
	{
		return this.aggregate.yield();
	}

}