package net.jadoth.collections.functions;

import net.jadoth.collections.interfaces.To_double;
import net.jadoth.functional.Aggregator;


public final class AggregateSum<E> implements Aggregator<E, Double>
{
	private final To_double<? super E> getter;

	private double sum = 0;

	public AggregateSum(final To_double<? super E> getter)
	{
		super();
		this.getter = getter;
	}

	@Override
	public final void accept(final E element)
	{
		this.sum += this.getter.apply(element);
	}

	@Override
	public final Double yield()
	{
		return this.sum;
	}

}
