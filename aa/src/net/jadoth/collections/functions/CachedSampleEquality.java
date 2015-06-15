package net.jadoth.collections.functions;

import java.util.function.Predicate;

import net.jadoth.util.Equalator;

public final class CachedSampleEquality<E> implements Predicate<E>
{
	private final Equalator<? super E> equalator;
	public E sample = null;

	public CachedSampleEquality(final Equalator<? super E> equalator)
	{
		super();
		this.equalator = equalator;
	}
	@Override
	public boolean test(final E e)
	{
		return this.equalator.equal(this.sample, e);
	}

}