package net.jadoth.collections.types;

import java.util.ListIterator;

import net.jadoth.collections.interfaces.ExtendedList;
import net.jadoth.collections.old.OldList;

/**
 * @author Thomas Muenz
 *
 */
public interface XGettingList<E> extends XGettingSequence<E>, XGettingBag<E>, ExtendedList<E>
{
	public interface Factory<E> extends XGettingCollection.Creator<E>
	{
		@Override
		public XGettingList<E> newInstance();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public XImmutableList<E> immure();

	// java.util.List List Iterators
	public ListIterator<E> listIterator();
	public ListIterator<E> listIterator(int index);

	@Override
	public OldList<E> old();

	@Override
	public XGettingList<E> copy();

	@Override
	public XGettingList<E> toReversed();

	@Override
	public XGettingList<E> view();
	
	@Override
	public XGettingList<E> view(int lowIndex, int highIndex);

	@Override
	public XGettingList<E> range(int fromIndex, int toIndex);

}
