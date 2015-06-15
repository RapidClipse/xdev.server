package net.jadoth.collections.types;

import java.util.Comparator;

import net.jadoth.collections.interfaces.ReleasingCollection;

/**
 * @author Thomas Muenz
 *
 */
public interface XSettingSequence<E> extends XSortableSequence<E>, ReleasingCollection<E>
{
	public interface Creator<E> extends XSortableSequence.Creator<E>
	{
		@Override
		public XSettingSequence<E> newInstance();
	}


	public boolean set(int index, E element);

	public E setGet(int index, E element);

	// intentionally not returning old element for performance reasons. set(int, E) does that already.
	public void setFirst(E element);

	public void setLast(E element);

	@SuppressWarnings("unchecked")
	public XSettingSequence<E> setAll(int index, E... elements);

	public XSettingSequence<E> set(int index, E[] elements, int offset, int length);

	public XSettingSequence<E> set(int index, XGettingSequence<? extends E> elements, int offset, int length);



	@Override
	public XSettingSequence<E> swap(int indexA, int indexB);

	@Override
	public XSettingSequence<E> swap(int indexA, int indexB, int length);

	@Override
	public XSettingSequence<E> reverse();

	@Override
	public XSettingSequence<E> sort(Comparator<? super E> comparator);

	@Override
	public XSettingSequence<E> copy();

	@Override
	public XSettingSequence<E> toReversed();

	@Override
	public XSettingSequence<E> range(int fromIndex, int toIndex);

}
