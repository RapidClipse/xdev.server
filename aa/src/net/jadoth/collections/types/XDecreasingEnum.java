package net.jadoth.collections.types;

import java.util.Comparator;

/**
 * Intermediate list type that combines all list aspects except increasing (adding and inserting), effectively causing
 * instances of this list type to maintain its size or shrink, but never grow.
 * <p>
 * This type is primarily used for the values list of a map, which can offer all functionality except adding
 * values (without mapping it to a key).
 *
 * @author Thomas Muenz
 */
public interface XDecreasingEnum<E> extends XProcessingEnum<E>, XSettingEnum<E>, XDecreasingSequence<E>
{
	public interface Creator<E> extends XProcessingEnum.Creator<E>, XSettingEnum.Creator<E>, XDecreasingSequence.Creator<E>
	{
		@Override
		public XDecreasingEnum<E> newInstance();
	}



	@SuppressWarnings("unchecked")
	@Override
	public XDecreasingEnum<E> setAll(int index, E... elements);

	@Override
	public XDecreasingEnum<E> set(int index, E[] elements, int offset, int length);

	@Override
	public XDecreasingEnum<E> set(int index, XGettingSequence<? extends E> elements, int offset, int length);

	@Override
	public XDecreasingEnum<E> swap(int indexA, int indexB);

	@Override
	public XDecreasingEnum<E> swap(int indexA, int indexB, int length);

	@Override
	public XDecreasingEnum<E> copy();

	@Override
	public XDecreasingEnum<E> toReversed();

	@Override
	public XDecreasingEnum<E> reverse();

	@Override
	public XDecreasingEnum<E> range(int fromIndex, int toIndex);

	@Override
	public XDecreasingEnum<E> sort(Comparator<? super E> comparator);

}
