package net.jadoth.collections.types;

import java.util.function.Function;
import java.util.function.Predicate;

import net.jadoth.collections.interfaces.ReleasingCollection;


public interface XReplacingBag<E> extends XGettingCollection<E>, ReleasingCollection<E>
{
	public interface Factory<E> extends XGettingCollection.Creator<E>
	{
		@Override
		public XReplacingBag<E> newInstance();
	}

	public boolean replaceOne(E element, E replacement);

	public int replace(E element, E replacement);

	public int replaceAll(XGettingCollection<? extends E> elements, E replacement);

	public boolean replaceOne(Predicate<? super E> predicate, E substitute);
	
	public int replace(Predicate<? super E> predicate, E substitute);
		
	public int modify(Function<E, E> mapper);

	public int modify(Predicate<? super E> predicate, Function<E, E> mapper);

	@Override
	public XReplacingBag<E> copy();
}
