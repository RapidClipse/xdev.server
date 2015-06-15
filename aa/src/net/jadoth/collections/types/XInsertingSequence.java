package net.jadoth.collections.types;



/**
 * @author Thomas Muenz
 *
 */
public interface XInsertingSequence<E> extends XExtendingSequence<E>
{
	public interface Creator<E> extends XExtendingSequence.Creator<E>
	{
		@Override
		public XInsertingSequence<E> newInstance();
	}



	public boolean insert(int index, E element);

	public boolean nullInsert(int index);

	@SuppressWarnings("unchecked")
	public int insertAll(int index, E... elements);

	public int insertAll(int index, E[] elements, int offset, int length);

	public int insertAll(int index, XGettingCollection<? extends E> elements);



	@SuppressWarnings("unchecked")
	@Override
	public XInsertingSequence<E> addAll(E... elements);

	@Override
	public XInsertingSequence<E> addAll(E[] elements, int srcStartIndex, int srcLength);

	@Override
	public XInsertingSequence<E> addAll(XGettingCollection<? extends E> elements);

	@SuppressWarnings("unchecked")
	@Override
	public XInsertingSequence<E> prependAll(E... elements);

	@Override
	public XInsertingSequence<E> prependAll(E[] elements, int srcStartIndex, int srcLength);

	@Override
	public XInsertingSequence<E> prependAll(XGettingCollection<? extends E> elements);

}
