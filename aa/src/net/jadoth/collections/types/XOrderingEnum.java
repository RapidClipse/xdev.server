package net.jadoth.collections.types;


/**
 * @author Thomas Muenz
 *
 */
public interface XOrderingEnum<E> extends XOrderingSequence<E>
{



	// (06.07.2011 TM)FIXME: XOrderingEnum: elemental shift
//	public boolean shiftTo(E element, int targetIndex);
//	public boolean shiftBy(E element, int targetIndex);
//	public boolean shiftToStart(E element, int targetIndex);
//	public boolean shiftToEnd(E element, int targetIndex);


	@Override
	public XOrderingEnum<E> shiftTo(int sourceIndex, int targetIndex);
	@Override
	public XOrderingEnum<E> shiftTo(int sourceIndex, int targetIndex, int length);
	@Override
	public XOrderingEnum<E> shiftBy(int sourceIndex, int distance);
	@Override
	public XOrderingEnum<E> shiftBy(int sourceIndex, int distance, int length);

	@Override
	public XOrderingEnum<E> swap(int indexA, int indexB);
	@Override
	public XOrderingEnum<E> swap(int indexA, int indexB, int length);

	@Override
	public XOrderingEnum<E> reverse();

}
