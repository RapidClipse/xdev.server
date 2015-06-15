package net.jadoth.collections.types;

import net.jadoth.collections.interfaces.CapacityExtendable;
import net.jadoth.collections.interfaces.ExtendedSequence;
import net.jadoth.collections.interfaces.OptimizableCollection;
import net.jadoth.functional.Procedure;

public interface XPrependingSequence<E>
extends Procedure<E>, CapacityExtendable, OptimizableCollection, ExtendedSequence<E>
{
	public interface Creator<E>
	{
		public XPrependingSequence<E> newInstance();
	}



	public boolean prepend(E element);

	public boolean nullPrepend();

	@SuppressWarnings("unchecked")
	public XPrependingSequence<E> prependAll(E... elements);

	public XPrependingSequence<E> prependAll(E[] elements, int srcStartIndex, int srcLength);

	public XPrependingSequence<E> prependAll(XGettingCollection<? extends E> elements);

}
