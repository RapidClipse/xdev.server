package net.jadoth.collections.types;

import java.util.Comparator;
import java.util.RandomAccess;

import net.jadoth.functional.Aggregator;
import net.jadoth.functional.Procedure;

/**
 * Extended List interface with a ton of badly needed list procedures like distinction between identity and equality
 * element comparison, procedure range specification, higher order (functional) procedures, proper toArray() methods,
 * etc.<br>
 * <br>
 * All {@link XList} implementations have to have {@link RandomAccess} behaviour.<br>
 * Intelligent implementations make non-random-access implementations like simple linked lists obsolete.
 *
 * @author Thomas Muenz
 *
 */
public interface XList<E> extends XBasicList<E>, XIncreasingList<E>, XDecreasingList<E>, XSequence<E>
{
	public interface Creator<E> extends XBasicList.Creator<E>, XIncreasingList.Creator<E>, XDecreasingList.Creator<E>
	{
		@Override
		public XList<E> newInstance();
	}


	@Override
	public default Aggregator<E, ? extends XList<E>> collector()
	{
		return new Aggregator<E, XList<E>>()
		{
			@Override
			public void accept(final E element)
			{
				XList.this.add(element);
			}

			@Override
			public XList<E> yield()
			{
				return XList.this;
			}
		};
	}



	@SuppressWarnings("unchecked")
	@Override
	public XList<E> addAll(E... elements);

	@Override
	public XList<E> addAll(E[] elements, int offset, int length);

	@Override
	public XList<E> addAll(XGettingCollection<? extends E> elements);

	@SuppressWarnings("unchecked")
	@Override
	public XList<E> putAll(E... elements);

	@Override
	public XList<E> putAll(E[] elements, int offset, int length);

	@Override
	public XList<E> putAll(XGettingCollection<? extends E> elements);

	@SuppressWarnings("unchecked")
	@Override
	public XList<E> prependAll(E... elements);

	@Override
	public XList<E> prependAll(E[] elements, int offset, int length);

	@Override
	public XList<E> prependAll(XGettingCollection<? extends E> elements);

	@SuppressWarnings("unchecked")
	@Override
	public XList<E> preputAll(E... elements);

	@Override
	public XList<E> preputAll(E[] elements, int offset, int length);

	@Override
	public XList<E> preputAll(XGettingCollection<? extends E> elements);

	@Override
	public XList<E> setAll(int index, @SuppressWarnings("unchecked") E... elements);

	@Override
	public XList<E> set(int index, E[] elements, int offset, int length);

	@Override
	public XList<E> set(int index, XGettingSequence<? extends E> elements, int offset, int length);

	@Override
	public XList<E> swap(int indexA, int indexB);

	@Override
	public XList<E> swap(int indexA, int indexB, int length);

	@Override
	public XList<E> retainRange(int offset, int length);

	@Override
	public XList<E> copy();

	@Override
	public XList<E> toReversed();

	@Override
	public XList<E> reverse();

	@Override
	public XList<E> range(int fromIndex, int toIndex);

	@Override
	public XList<E> fill(int offset, int length, E element);

	@Override
	public XList<E> sort(Comparator<? super E> comparator);

	@Override
	public XList<E> shiftTo(int sourceIndex, int targetIndex);

	@Override
	public XList<E> shiftTo(int sourceIndex, int targetIndex, int length);

	@Override
	public XList<E> shiftBy(int sourceIndex, int distance);

	@Override
	public XList<E> shiftBy(int sourceIndex, int distance, int length);

	@Override
	public <P extends Procedure<? super E>> P iterate(P procedure);

}
