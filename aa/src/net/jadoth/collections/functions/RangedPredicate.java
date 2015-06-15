package net.jadoth.collections.functions;

import java.util.function.Predicate;

import net.jadoth.Jadoth;

public final class RangedPredicate<E> implements Predicate<E>
{
	private int offset;
	private int length;
	private final Predicate<? super E> predicate;

	public RangedPredicate(final int offset, final int length, final Predicate<? super E> predicate)
	{
		super();
		this.offset = offset;
		this.length = length;
		this.predicate = predicate;
	}

	@Override
	public boolean test(final E e)
	{
		if(this.offset > 0){
			this.offset--;
			return false;
		}
		if(this.predicate.test(e)){
			return true;
		}
		if(--this.length <= 0) throw Jadoth.BREAK;
		return false;
	}

}