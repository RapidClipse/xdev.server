package net.jadoth.collections.functions;

import net.jadoth.functional.Aggregator;


public final class AggregateCount<E> implements Aggregator<E, Long>
{
	private long count = 0;

	@Override
	public final void accept(final E element)
	{
		this.count++;
	}

	@Override
	public final Long yield()
	{
		return this.count;
	}

}
