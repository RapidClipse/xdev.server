package net.jadoth.util.iterables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Predicate;

import net.jadoth.collections.JadothArrays;
import net.jadoth.collections.XUtilsCollection;
import net.jadoth.collections.old.OldCollections;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XInsertingList;
import net.jadoth.collections.types.XProcessingCollection;
import net.jadoth.functional.Procedure;

/**
 * Util class for handling {@link Iterable} instances in Collections 2.0.<br>
 * As {@link Iterable}s have to be externally iterated anyway, there's no sense in implementing them
 * inherently in concrete implementations.<br>
 * Note that "Collections 1.0" (java.util) collections have to be iterated externally as well, so for many procedures,
 * the have to be treated as {@link Iterable}s and processed by the util methods in this class.
 *
 * @author Thomas Muenz.
 *
 */
public abstract class JadothIterables
{
	public static <E, C extends Procedure<? super E>> C putAll(final C target, final Iterable<? extends E> iterable)
	{
		if(iterable instanceof XGettingCollection<?>){
			return ((XGettingCollection<? extends E>)iterable).copyTo(target);
		}

		for(final E e : iterable) {
			target.accept(e);
		}

		return target;
	}

	public static <E, C extends Procedure<? super E>> C putAll(
		final C target,
		final Iterable<? extends E> iterable,
		final Predicate<? super E> predicate
	)
	{
		if(iterable instanceof XGettingCollection<?>){
			return ((XGettingCollection<? extends E>)iterable).filterTo(target, predicate);
		}

		for(final E e : iterable) {
			if(predicate.test(e)){
				target.accept(e);
			}
		}

		return target;
	}

	public static <E, C extends XInsertingList<? super E>> C insert(
		final C target,
		final int index,
		final Iterable<? extends E> iterable
	)
	{
		if(iterable instanceof XGettingCollection<?>){
			target.insertAll(index, (XGettingCollection<? extends E>)iterable);
			return target;
		}

		for(final E e : iterable) {
			target.add(e);
		}

		return target;
	}

	public static <S, E extends S, C extends XInsertingList<S>> C insert(
		final C target,
		int index,
		final Iterable<E> iterable,
		final Predicate<? super S> predicate
	)
	{
		if(iterable instanceof XGettingCollection<?>){
			XUtilsCollection.insert(target, index, (XGettingCollection<E>)iterable, predicate);
			return target;
		}

		for(final E e : iterable) {
			if(predicate.test(e)){
				target.insert(index++, e);
			}
		}

		return target;
	}

	public static <E> boolean containsAll(
		final XGettingCollection<? super E> collection,
		final Iterable<? extends E> iterable
	)
	{
		if(iterable instanceof XGettingCollection<?>){
			return collection.containsAll((XGettingCollection<? extends E>)iterable);
		}

		for(final E e : iterable) {
			if(!collection.contains(e)) return false;
		}
		return true;
	}

	public static <E> int removeAll(
		final XProcessingCollection<? super E> collection,
		final Iterable<? extends E> iterable
	)
	{
		if(iterable instanceof XGettingCollection<?>){
			return collection.removeAll((XGettingCollection<? extends E>)iterable);
		}

		int removeCount = 0;
		for(final E e : iterable) {
			removeCount += collection.remove(e);
		}
		return removeCount;
	}

	public static <T> void reduce(final Iterator<T> iterator, final Predicate<? super T> reductionPredicate)
	{
		while(iterator.hasNext()){
			if(reductionPredicate.test(iterator.next())){
				iterator.remove();
			}
		}
	}

	public static final <T> boolean applies(final Iterable<T> elements, final Predicate<? super T> predicate)
	{
		for(final T t : elements) {
			if(predicate.test(t)) return true;
		}
		return false;
	}

	public static final <T> T search(final Iterable<T> elements, final Predicate<? super T> predicate)
	{
		for(final T t : elements) {
			if(predicate.test(t)) return t;
		}
		return null;
	}

	public static final <T> int count(final Iterable<T> collection, final Predicate<? super T> predicate)
	{
		int count = 0;
		for(final T t : collection) {
			if(predicate.test(t)) count++;
		}
		return count;
	}

	public static final <T> T[] toArray(final Iterable<? extends T> iterable, final Class<T> elementType)
	{
		final ArrayList<? extends T> list = OldCollections.ArrayList(1024, iterable);
		return list.toArray(JadothArrays.newArray(elementType, list.size()));
	}

	@SafeVarargs
	public static <T> Iterable<T> iterate(final Iterable<T>... iterables)
	{
		return new ChainedIterables<>(iterables);
	}

	/**
	 * Wraps <code>array</code> of type <code>T[]</code> in an instance of <code>Iterable<T></code>
	 *
	 * @param <T> the type of <code>array</code>'s elements and of the created <code>Iterable</code>
	 * @param array the array to be wrapped
	 * @return the <code>Iterable<T></code> wrapping <code>T[] array</code>.
	 */
	public static <T> Iterable<T> iterate(final T[] array)
	{
		return new ArrayIterable<>(array);
	}

	@SafeVarargs
	public static <T> Iterable<T> iterate(final T[]... arrays)
	{
		return new ChainedArraysIterable<>(arrays);
	}

}
