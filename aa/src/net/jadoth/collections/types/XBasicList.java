package net.jadoth.collections.types;

import net.jadoth.collections.sorting.XLadder;


/**
 * Intermediate list type providing getting, adding, removing concearns to act as a common super type for
 * {@link XList} and {@link XLadder}. This is necessary because {@link XLadder} cannot provide
 * the otherwise typical list concearns like inserting, ordering, setting due to the limitations of the characteristic
 * of being always sorted.
 *
 * @author Thomas Muenz
 *
 * @param <E>
 */
public interface XBasicList<E> extends XBag<E>, XBasicSequence<E>, XPutGetList<E>, XProcessingList<E>
{
	public interface Creator<E>
	extends
	XBag.Factory<E>,
	XBasicSequence.Factory<E>,
	XPutGetList.Factory<E>,
	XProcessingList.Factory<E>
	{
		@Override
		public XBasicList<E> newInstance();
	}



	@SuppressWarnings("unchecked")
	@Override
	public XBasicList<E> putAll(E... elements);

	@Override
	public XBasicList<E> putAll(E[] elements, int srcStartIndex, int srcLength);

	@Override
	public XBasicList<E> putAll(XGettingCollection<? extends E> elements);

	@SuppressWarnings("unchecked")
	@Override
	public XBasicList<E> addAll(E... elements);

	@Override
	public XBasicList<E> addAll(E[] elements, int srcStartIndex, int srcLength);

	@Override
	public XBasicList<E> addAll(XGettingCollection<? extends E> elements);

	@Override
	public XBasicList<E> copy();

	@Override
	public XBasicList<E> toReversed();

}
