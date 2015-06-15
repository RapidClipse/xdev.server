package net.jadoth.collections.types;

import java.util.Comparator;

import net.jadoth.collections.sorting.Sortable;

/**
 * @author Thomas Muenz
 *
 */
public interface XSortableSequence<E> extends XGettingSequence<E>, Sortable<E>, XOrderingSequence<E>
{
	// (01.12.2011 TM)XXX: what about XOrderingList? At least for content behaviour ensurance.

	public interface Creator<E> extends XGettingSequence.Factory<E>
	{
		@Override
		public XGettingSequence<E> newInstance();
	}



	@Override
	public XSortableSequence<E> shiftTo(int sourceIndex, int targetIndex);
	@Override
	public XSortableSequence<E> shiftTo(int sourceIndex, int targetIndex, int length);
	@Override
	public XSortableSequence<E> shiftBy(int sourceIndex, int distance);
	@Override
	public XSortableSequence<E> shiftBy(int sourceIndex, int distance, int length);

	@Override
	public XSortableSequence<E> swap(int indexA, int indexB);
	@Override
	public XSortableSequence<E> swap(int indexA, int indexB, int length);

	@Override
	public XSortableSequence<E> reverse();

	@Override
	public XSortableSequence<E> sort(Comparator<? super E> comparator);

	@Override
	public XSortableSequence<E> copy();

	@Override
	public XSortableSequence<E> toReversed();

}
