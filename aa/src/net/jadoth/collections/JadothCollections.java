package net.jadoth.collections;

import static net.jadoth.Jadoth.notNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

import net.jadoth.Jadoth;
import net.jadoth.collections.types.XCollection;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XList;
import net.jadoth.collections.types.XSet;
import net.jadoth.concurrent.ThreadSafe;
import net.jadoth.functional.AvgInteger;
import net.jadoth.functional.AvgIntegerNonNull;
import net.jadoth.functional.Procedure;
import net.jadoth.functional.SumInteger;

/**
 * @author Thomas Muenz
 *
 */
public final class JadothCollections
{
	public static final boolean containsNull(final Collection<?> c) throws NullPointerException
	{
		notNull(c);
		try {
			return c.contains(null);
		}
		catch(final NullPointerException e) {
			//collection implementations not supporting null values may throw an exception according to contract
			return false;
		}
	}



	///////////////////////////////////////////////////////////////////////////
	// collection aggregates  //
	///////////////////////////


	public static final transient Comparator<Boolean> COMPARE_BOOLEANS = new Comparator<Boolean>(){
		@Override
		public int compare(final Boolean o1, final Boolean o2)
		{
			if(o1 == o2)   return  0;
			if(o1 == null) return -1;
			if(o2 == null) return  1;
			if(o1.booleanValue()) return o2.booleanValue() ?0 :1;
			return o2.booleanValue() ?-1 :0;
		}
	};
	public static final transient Comparator<Byte>    COMPARE_BYTES    = new Comparator<Byte>(){
		@Override
		public int compare(final Byte o1, final Byte o2)
		{
			if(o1 == o2)   return  0;
			if(o1 == null) return -1;
			if(o2 == null) return  1;
			if(o1.byteValue() < o2.byteValue()) return -1;
			if(o1.byteValue() > o2.byteValue()) return  1;
			return 0;
		}
	};
	public static final transient Comparator<Short>   COMPARE_SHORTS   = new Comparator<Short>(){
		@Override
		public int compare(final Short o1, final Short o2)
		{
			if(o1 == o2)   return  0;
			if(o1 == null) return -1;
			if(o2 == null) return  1;
			if(o1.shortValue() < o2.shortValue()) return -1;
			if(o1.shortValue() > o2.shortValue()) return  1;
			return 0;
		}
	};
	public static final transient Comparator<Integer> COMPARE_INTEGERS = new Comparator<Integer>(){
		@Override
		public int compare(final Integer o1, final Integer o2)
		{
			if(o1 == o2)   return  0;
			if(o1 == null) return -1;
			if(o2 == null) return  1;
			if(o1.intValue() < o2.intValue()) return -1;
			if(o1.intValue() > o2.intValue()) return  1;
			return 0;
		}
	};
	public static final transient Comparator<Long>    COMPARE_LONGS    = new Comparator<Long>(){
		@Override
		public int compare(final Long o1, final Long o2)
		{
			if(o1 == o2)   return  0;
			if(o1 == null) return -1;
			if(o2 == null) return  1;
			if(o1.longValue() < o2.longValue()) return -1;
			if(o1.longValue() > o2.longValue()) return  1;
			return 0;
		}
	};
	public static final transient Comparator<Float>   COMPARE_FLOATS   = new Comparator<Float>(){
		@Override
		public int compare(final Float o1, final Float o2)
		{
			if(o1 == o2)   return  0;
			if(o1 == null) return -1;
			if(o2 == null) return  1;
			if(o1.floatValue() < o2.floatValue()) return -1;
			if(o1.floatValue() > o2.floatValue()) return  1;
			return 0;
		}
	};
	public static final transient Comparator<Double>  COMPARE_DOUBLES  = new Comparator<Double>(){
		@Override
		public int compare(final Double o1, final Double o2)
		{
			if(o1 == o2)   return  0;
			if(o1 == null) return -1;
			if(o2 == null) return  1;
			if(o1.doubleValue() < o2.doubleValue()) return -1;
			if(o1.doubleValue() > o2.doubleValue()) return  1;
			return 0;
		}
	};

	public static final boolean isEmpty(final XGettingCollection<?> c)
	{
		return c == null || c.isEmpty();
	}

	public static final Integer count(final XGettingCollection<?> c)
	{
		return Jadoth.to_int(c.size()); //kind of stupid
	}

	public static final Integer sum(final XGettingCollection<Integer> ints)
	{
		return ints.iterate(new SumInteger()).yield();
	}
	public static final Integer avg(final XGettingCollection<Integer> ints)
	{
		return new AvgInteger(ints).yield();
	}
	public static final Integer avg(final XGettingCollection<Integer> ints, final boolean includeNulls)
	{
		return (includeNulls ?new AvgInteger(ints) :new AvgIntegerNonNull(ints)).yield();
	}
	public static final Integer max(final XGettingCollection<Integer> ints)
	{
		return ints.max(COMPARE_INTEGERS);
	}
	public static final Integer min(final XGettingCollection<Integer> ints)
	{
		return ints.min(COMPARE_INTEGERS);
	}


	/**
	 * Convenience method for <code>new ArrayList<E>(xCollection)</code>.
	 * <p>
	 *
	 * @param <E> the collection element type.
	 * @param xCollection the extended collection implementation whore content shall be copied a new
	 *        {@link ArrayList} instance.
	 * @return a new {@link ArrayList} instance containing all elements of the passed {@link XGettingCollection}.
	 */
	public static final <E> ArrayList<E> ArrayList(final XGettingCollection<? extends E> xCollection)
	{
		// ArrayList collection constructor already uses toArray() directly as elementData
		return new ArrayList<>(xCollection.old());
	}

	@SafeVarargs
	public static final <E> ArrayList<E> ArrayList(final E... elements)
	{
		if(elements == null){
			return new ArrayList<>();
		}

		final ArrayList<E> list = new ArrayList<>(elements.length);
		for(int i = 0; i < elements.length; i++) {
			list.add(elements[i]);
		}
		return list;
	}


	public static final <E> LinkedList<E> LinkedList(final XGettingCollection<? extends E> xCollection)
	{
		final LinkedList<E> linkedList = new LinkedList<>();

		// current array-backed list implementations all have none-volatile elements, but just to be sure.
		if(!xCollection.hasVolatileElements() && xCollection instanceof AbstractSimpleArrayCollection<?>){
			// fastest way: just iterate over storage array
			final E[] elements = AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)xCollection);
			for(int i = 0, size = Jadoth.to_int(xCollection.size()); i < size; i++) {
				linkedList.add(elements[i]);
			}
			return linkedList;
		}

		/* still faster than LinkedList.addAll() with intermediate Object[] creation:
		 * iterate source collection with stateless adding function instance.
		 */
		xCollection.iterate(new Procedure<E>(){ @Override public void accept(final E e) {
			linkedList.add(e);
		}});
		return linkedList;
	}


	/**
	 * Ensures that the returned {@link XList} instance based on the passed list is thread safe to use.<br>
	 * This normally means wrapping the passed list in a {@link SynchList}, making it effectively synchronized.<br>
	 * If the passed list already is thread safe (indicated by the marker interface {@link ThreadSafe}), then the list
	 * itself is returned without further actions. This automatically ensures that a {@link SynchList} is not
	 * redundantly wrapped again in another {@link SynchList}.
	 *
	 * @param <E> the element type.
	 * @param list the {@link XList} instance to be synchronized.
	 * @return a thread safe {@link XList} using the passed list.
	 */
	public static <E> XList<E> synchronize(final XList<E> list)
	{
		// if type of passed list is already thread safe, there's no need to wrap it in a SynchronizedXList
		if(list instanceof ThreadSafe){
			return list;
		}
		// wrap not thread safe list types in a SynchronizedXList
		return new SynchList<>(list);
	}


	/**
	 * Ensures that the returned {@link XSet} instance based on the passed set is thread safe to use.<br>
	 * This normally means wrapping the passed set in a {@link SynchSet}, making it effectively synchronized.<br>
	 * If the passed set already is thread safe (indicated by the marker interface {@link ThreadSafe}), then the set
	 * itself is returned without further actions. This automatically ensures that a {@link SynchSet} is not
	 * redundantly wrapped again in another {@link SynchSet}.
	 *
	 * @param <E> the element type.
	 * @param set the {@link XSet} instance to be synchronized.
	 * @return a thread safe {@link XSet} using the passed set.
	 */
	public static <E> XSet<E> synchronize(final XSet<E> set)
	{
		// if type of passed set is already thread safe, there's no need to wrap it in a SynchronizedXSet
		if(set instanceof ThreadSafe){
			return set;
		}
		// wrap not thread safe set types in a SynchronizedXSet
		return new SynchSet<>(set);
	}

	/**
	 * Ensures that the returned {@link XCollection} instance based on the passed collection is thread safe to use.<br>
	 * This normally means wrapping the passed collection in a {@link SynchCollection}, making it effectively synchronized.<br>
	 * If the passed collection already is thread safe (indicated by the marker interface {@link ThreadSafe}), then the collection
	 * itself is returned without further actions. This automatically ensures that a {@link SynchCollection} is not
	 * redundantly wrapped again in another {@link SynchCollection}.
	 *
	 * @param <E> the element type.
	 * @param collection the {@link XCollection} instance to be synchronized.
	 * @return a thread safe {@link XCollection} using the passed collection.
	 */
	public static <E> XCollection<E> synchronize(final XCollection<E> collection)
	{
		// if type of passed collection is already thread safe, there's no need to wrap it in a SynchronizedXCollection
		if(collection instanceof ThreadSafe){
			return collection;
		}
		// wrap not thread safe set types in a SynchronizedXCollection
		return new SynchCollection<>(collection);
	}


	public static <T> T[] toArray(final Collection<? extends T> collection, final Class<T> type)
	{
		final T[] array = JadothArrays.newArray(type, collection.size());
		collection.toArray(array);
		return array;
	}




	private JadothCollections() { throw new UnsupportedOperationException(); } // static only
}
