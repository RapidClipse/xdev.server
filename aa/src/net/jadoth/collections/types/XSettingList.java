package net.jadoth.collections.types;

import java.util.Comparator;

public interface XSettingList<E> extends XReplacingBag<E>, XSettingSequence<E>, XGettingList<E>
{
	public interface Creator<E> extends XReplacingBag.Factory<E>, XSettingSequence.Creator<E>, XGettingList.Factory<E>
	{
		@Override
		public XSettingList<E> newInstance();
	}



	public XSettingList<E> fill(int offset, int length, E element);



	@SuppressWarnings("unchecked")
	@Override
	public XSettingList<E> setAll(int index, E... elements);

	@Override
	public XSettingList<E> set(int index, E[] elements, int offset, int length);

	@Override
	public XSettingList<E> set(int index, XGettingSequence<? extends E> elements, int offset, int length);

	@Override
	public XSettingList<E> swap(int indexA, int indexB);

	@Override
	public XSettingList<E> swap(int indexA, int indexB, int length);

	@Override
	public XSettingList<E> reverse();

	@Override
	public XSettingSequence<E> sort(Comparator<? super E> comparator);

	@Override
	public XSettingList<E> copy();

	@Override
	public XSettingList<E> toReversed();

	@Override
	public XSettingList<E> range(int fromIndex, int toIndex);

}
