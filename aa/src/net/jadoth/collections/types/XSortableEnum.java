package net.jadoth.collections.types;

import java.util.Comparator;

/**
 * @author Thomas Muenz
 *
 */
public interface XSortableEnum<E> extends XSortableSequence<E>, XGettingEnum<E>, XOrderingEnum<E>
{
	public interface Creator<E> extends XSortableSequence.Creator<E>, XGettingEnum.Creator<E>
	{
		@Override
		public XSortableEnum<E> newInstance();
	}


	// (06.07.2011 TM)FIXME: XSortableEnum: elemental shift
//	public boolean shiftTo(E element, Equalator<? super E> equalator, int targetIndex);
//	public boolean shiftBy(E element, Equalator<? super E> equalator, int targetIndex);
//	public boolean shiftToStart(E element, Equalator<? super E> equalator, int targetIndex);
//	public boolean shiftToEnd(E element, Equalator<? super E> equalator, int targetIndex);


	@Override
	public XSortableEnum<E> shiftTo(int sourceIndex, int targetIndex);
	@Override
	public XSortableEnum<E> shiftTo(int sourceIndex, int targetIndex, int length);
	@Override
	public XSortableEnum<E> shiftBy(int sourceIndex, int distance);
	@Override
	public XSortableEnum<E> shiftBy(int sourceIndex, int distance, int length);

	@Override
	public XSortableEnum<E> swap(int indexA, int indexB);
	@Override
	public XSortableEnum<E> swap(int indexA, int indexB, int length);

	@Override
	public XSortableEnum<E> reverse();



	@Override
	public XSortableEnum<E> copy();

	@Override
	public XSortableEnum<E> toReversed();

	@Override
	public XSortableEnum<E> sort(Comparator<? super E> comparator);

}
