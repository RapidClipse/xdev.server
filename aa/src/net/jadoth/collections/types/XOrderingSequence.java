package net.jadoth.collections.types;

import net.jadoth.collections.interfaces.ExtendedSequence;

/**
 * @author Thomas Muenz
 *
 */
public interface XOrderingSequence<E> extends ExtendedSequence<E>
{
	public XOrderingSequence<E> shiftTo(int sourceIndex, int targetIndex);
	public XOrderingSequence<E> shiftTo(int sourceIndex, int targetIndex, int length);
	public XOrderingSequence<E> shiftBy(int sourceIndex, int distance);
	public XOrderingSequence<E> shiftBy(int sourceIndex, int distance, int length);

	public XOrderingSequence<E> swap(int indexA, int indexB);
	public XOrderingSequence<E> swap(int indexA, int indexB, int length);

	public XOrderingSequence<E> reverse();

}
