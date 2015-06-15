package net.jadoth.collections.types;

public interface XRemovingSequence<E> extends XRemovingCollection<E>
{
	public XRemovingSequence<E> removeRange(int offset, int length);

	public XRemovingSequence<E> retainRange(int offset, int length);

	public int removeSelection(int[] indices);


	public interface Factory<E> extends XRemovingCollection.Factory<E>
	{
		@Override
		public XRemovingSequence<E> newInstance();
	}

}
