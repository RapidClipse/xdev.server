package net.jadoth.collections.types;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import net.jadoth.collections.XIndexIterable;
import net.jadoth.collections.interfaces.ExtendedSequence;
import net.jadoth.exceptions.IndexBoundsException;
import net.jadoth.functional.Procedure;

/**
 *
 * @author Thomas Muenz
 */
public interface XGettingSequence<E> extends XGettingCollection<E>, ExtendedSequence<E>, XIndexIterable<E>
{
	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * Creates a true copy of this list which references th same elements in the same order as this list does
	 * at the time the method is called. The elements themselves are NOT copied (no deep copying).<br>
	 * The type of the returned list is the same as of this list if possible (i.e.: a SubList can not meaningful
	 * return a true copy that references its elements but still is a SubList)
	 *
	 * @return a copy of this list
	 */
	@Override
	public XGettingSequence<E> copy();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public XImmutableSequence<E> immure();

	/**
	 * Gets the first element in the collection. This is a parameterless alias vor {@code at(0)}.
	 * <p>
	 * {@code first() is an alias for this method for consistency reasons with last()}.
	 * <p>
	 *
	 * @see #at(int)
	 * @see #first()
	 * @see #last()
	 * @return the first element.
	 */
	@Override
	public E get() throws NoSuchElementException;


	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	/*
	 * This method was named "get(int)" for a long time. However, this common name has numerous problems:
	 * - collides with get(Integer key) of a Map<Integer, ?> type
	 * - collides with get()
	 * - "get" for accessing an index is actually quite blurry. Get what and how? The precise name would be "getElementAtIndex(int)".
	 * However this is quite verbose, so a shortening compromise has to be made. So the question is, what is the preferable compromise:
	 * an ubiquitous "get" because in doubt anything and everything is called "get", or a linguistic reference to the index accessing nature, the "... at ..." relation?
	 * Considering the other two problems and considering that "at" is even a little shorter than "get", the best decision for clarity and expressiveness is already made.
	 */
	public E at(int index) throws IndexBoundsException; // get element at index or throw IndexOutOfBoundsException if invalid index

	public E first() throws IndexBoundsException; // get first element or throw IndexOutOfBoundsException if empty

	public E last() throws IndexBoundsException;  // get last  element or throw IndexOutOfBoundsException if empty

	public E poll();  // get first element or null if empty (like polling an empty collection to get filled)

	public E peek();  // get last  element or null if empty (like peeking on a stack without popping)

	public int maxIndex(Comparator<? super E> comparator);

	public int minIndex(Comparator<? super E> comparator);

	public int indexOf(E element);

	public int indexBy(Predicate<? super E> predicate);

	public int lastIndexOf(E element);

	public int lastIndexBy(Predicate<? super E> predicate);

	/**
	 * Iterates through the collection and returns the index of the last element that the passed {@link Predicate}
	 * applied to ("scanning").
	 *
	 * @param predicate
	 * @return the index of the last positively tested element.
	 */
	public int scan(Predicate<? super E> predicate);

	public boolean isSorted(Comparator<? super E> comparator);

	public XGettingSequence<E> toReversed();

	public <T extends Procedure<? super E>> T copySelection(T target, int... indices);

	public <T> T[] copyTo(T[] target, int targetOffset, int offset, int length);

	@Override
	public XGettingSequence<E> view();

	public XGettingSequence<E> view(int lowIndex, int highIndex);

	public XGettingSequence<E> range(int lowIndex, int highIndex);



	public interface Factory<E> extends XGettingCollection.Creator<E>
	{
		@Override
		public XGettingSequence<E> newInstance();
	}

}
