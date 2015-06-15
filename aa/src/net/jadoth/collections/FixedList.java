package net.jadoth.collections;

import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.old.OldSettingList;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.collections.types.XList;
import net.jadoth.collections.types.XSettingList;
import net.jadoth.exceptions.IndexBoundsException;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.IndexProcedure;
import net.jadoth.functional.JadothEqualators;
import net.jadoth.functional.Procedure;
import net.jadoth.math.JadothMath;
import net.jadoth.util.Composition;
import net.jadoth.util.Equalator;
import net.jadoth.util.iterables.ReadOnlyListIterator;


/**
 * Full scale general purpose implementation of extended collection type {@link XList}.
 * <p>
 * This array-backed implementation is optimal for all needs of a list that do not require frequent structural
 * modification (insert or remove) of single elements before the end of the list.<br>
 * It is recommended to use this implementation as default list type until concrete performance deficiencies are
 * identified. If used properly (e.g. always ensure enough capacity, make use of batch procedures like
 * {@link #inputAll(int, Object...)}, {@link #removeRange(int, int)}, etc.), this implementation has equal or
 * massively superior performance to linked-list implementation is most cases.
 * <p>
 * This implementation is NOT synchronized and thus should only be used by a
 * single thread or in a thread-safe manner (i.e. read-only as soon as multiple threads access it).<br>
 * See {@link SynchList} wrapper class to use a list in a synchronized manner.
 * <p>
 * Note that this List implementation does NOT keep track of modification count as JDK's collection implementations do
 * (and thus never throws a {@link ConcurrentModificationException}), for two reasons:<br>
 * 1.) It is already explicitely declared thread-unsafe and for single-thread (or thread-safe)
 * use only.<br>
 * 2.) The common modCount-concurrency exception behaviour ("failfast") has buggy and inconsistent behaviour by
 * throwing {@link ConcurrentModificationException} even in single thread use, i.e. when iterating over a collection
 * and removing more than one element of it without using the iterator's method.<br>
 * <br>
 * Current conclusion is that the JDK's failfast implementations buy unneeded (and even unreliable as stated by
 * official guides) concurrency modification recognition at the cost of performance loss and even a bug when already
 * used in a thread-safe manner.
 * <p>
 * Also note that by being an extended collection, this implementation offers various functional and batch procedures
 * to maximize internal iteration potential, eliminating the need to use the ill-conceived external iteration
 * {@link Iterator} paradigm.
 *
 * @author Thomas Muenz
 * @version 0.9, 2011-02-06
 */
public final class FixedList<E> extends AbstractSimpleArrayCollection<E> implements XSettingList<E>, Composition
{
	///////////////////////////////////////////////////////////////////////////
	//  class methods   //
	/////////////////////

	private static String exceptionStringRange(final int size, final int startIndex, final int length)
	{
		return "Range ["+(length < 0 ?startIndex+length+1+";"+startIndex
			:startIndex+";"+(startIndex+length-1))+"] not in [0;"+(size-1)+"]";
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	final Object[] data; // the storage array containing the elements



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	public FixedList(final int initialCapacity)
	{
		super();
		this.data = new Object[initialCapacity];
	}

	public FixedList(final FixedList<? extends E> original) throws NullPointerException
	{
		super();
		this.data = original.data.clone();
	}

	public FixedList(final Collection<? extends E> elements) throws NullPointerException
	{
		super();
		this.data = elements.toArray();
	}

	public FixedList(final XGettingCollection<? extends E> elements) throws NullPointerException
	{
		super();
		this.data = elements.toArray();
	}

	@SafeVarargs
	public FixedList(final E... elements) throws NullPointerException
	{
		super();
		this.data = elements.clone();
	}

	public FixedList(final E[] src, final int srcStart, final int srcLength)
	{
		super();
		// automatically check arguments 8-)
		System.arraycopy(src, srcStart, this.data = new Object[srcLength], 0, srcLength);
	}

	FixedList(final Object[] internalData, final int size)
	{
		super();
		this.data = internalData;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	public static class Creator<E> implements XSettingList.Creator<E>
	{
		private final int initialCapacity;

		public Creator(final int initialCapacity)
		{
			super();
			this.initialCapacity = JadothMath.pow2BoundMaxed(initialCapacity);
		}

		public int getInitialCapacity()
		{
			return this.initialCapacity;
		}

		@Override
		public FixedList<E> newInstance()
		{
			return new FixedList<>(new Object[this.initialCapacity], this.initialCapacity);
		}

	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@SuppressWarnings("unchecked")
	@Override
	protected E[] internalGetStorageArray()
	{
		return (E[])this.data;
	}

	@Override
	protected int internalSize()
	{
		return this.data.length;
	}

	@Override
	protected int[] internalGetSectionIndices()
	{
		return new int[]{0, this.data.length}; // trivial section
	}

	@Override
	public Equalator<? super E> equality()
	{
		return JadothEqualators.identity();
	}

	@Override
	protected int internalCountingAddAll(final E[] elements) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException(); // not supported
	}

	@Override
	protected int internalCountingAddAll(final E[] elements, final int offset, final int length) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException(); // not supported
	}

	@Override
	protected int internalCountingAddAll(final XGettingCollection<? extends E> elements) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException(); // not supported
	}

	@Override
	protected int internalCountingPutAll(final E[] elements) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException(); // not supported
	}

	@Override
	protected int internalCountingPutAll(final E[] elements, final int offset, final int length) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException(); // not supported
	}

	@Override
	protected int internalCountingPutAll(final XGettingCollection<? extends E> elements) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException(); // not supported
	}



	///////////////////////////////////////////////////////////////////////////
	// getting methods  //
	/////////////////////

	@Override
	public FixedList<E> copy()
	{
		return new FixedList<>(this);
	}

	@Override
	public ConstList<E> immure()
	{
		return new ConstList<>(this);
	}

	@Override
	public FixedList<E> toReversed()
	{
		final Object[] rData = new Object[this.data.length];
		final Object[] data = this.data;
		for(int i = data.length, r = 0; i --> 0;){
			rData[r++] = data[i];
		}
		return new FixedList<>(rData, data.length);
	}

	@Override
	public E[] toArray(final Class<E> type)
	{
		final E[] array = JadothArrays.newArray(type, this.data.length);
		System.arraycopy(this.data, 0, array, 0, this.data.length);
		return array;
	}

	// executing //

	@SuppressWarnings("unchecked")
	@Override
	public final <P extends Procedure<? super E>> P iterate(final P procedure)
	{
		AbstractArrayStorage.iterate((E[])this.data, this.data.length, procedure);
		return procedure;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <P extends IndexProcedure<? super E>> P iterate(final P procedure)
	{
		AbstractArrayStorage.iterate((E[])this.data, this.data.length, procedure);
		return procedure;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <A> A join(final BiProcedure<? super E, ? super A> joiner, final A aggregate)
	{
		AbstractArrayStorage.join((E[])this.data, this.data.length, joiner, aggregate);
		return aggregate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int count(final E element)
	{
		return AbstractArrayStorage.count((E[])this.data, this.data.length, element);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int countBy(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.conditionalCount((E[])this.data, this.data.length, predicate);
	}

	// index querying //

	@SuppressWarnings("unchecked")
	@Override
	public int indexOf(final E element)
	{
		return AbstractArrayStorage.indexOf((E[])this.data, this.data.length, element);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int indexBy(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.conditionalIndexOf((E[])this.data, this.data.length, predicate);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int lastIndexOf(final E element)
	{
		return AbstractArrayStorage.rngIndexOF((E[])this.data, this.data.length, this.data.length - 1, -this.data.length, element);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int lastIndexBy(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.lastIndexOf((E[])this.data, this.data.length, predicate);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int maxIndex(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.maxIndex((E[])this.data, this.data.length, comparator);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int minIndex(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.minIndex((E[])this.data, this.data.length, comparator);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int scan(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.scan((E[])this.data, this.data.length, predicate);
	}

	// element querying //

	@SuppressWarnings("unchecked")
	@Override
	public E get()
	{
		return (E)this.data[0];
	}

	@SuppressWarnings("unchecked")
	@Override
	public E first()
	{
		return (E)this.data[0];
	}

	@SuppressWarnings("unchecked")
	@Override
	public E last()
	{
		return (E)this.data[this.data.length - 1];
	}

	@SuppressWarnings("unchecked")
	@Override
	public E poll()
	{
		return this.data.length == 0 ?null :(E)this.data[0];
	}

	@SuppressWarnings("unchecked")
	@Override
	public E peek()
	{
		return this.data.length == 0 ?null :(E)this.data[this.data.length - 1];
	}

	@SuppressWarnings("unchecked")
	@Override
	public E seek(final E sample)
	{
		return AbstractArrayStorage.containsSame((E[])this.data, this.data.length, sample) ?sample :null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E search(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.queryElement((E[])this.data, this.data.length, predicate, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public E max(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.max((E[])this.data, this.data.length, comparator);
	}

	@SuppressWarnings("unchecked")
	@Override
	public E min(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.min((E[])this.data, this.data.length, comparator);
	}

	// boolean querying //

	@Override
	public boolean hasVolatileElements()
	{
		return false;
	}

	@Override
	public boolean nullAllowed()
	{
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isSorted(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.isSorted((E[])this.data, this.data.length, comparator);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasDistinctValues()
	{
		return AbstractArrayStorage.hasDistinctValues((E[])this.data, this.data.length);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasDistinctValues(final Equalator<? super E> equalator)
	{
		return AbstractArrayStorage.hasDistinctValues((E[])this.data, this.data.length, equalator);
	}

	// boolean querying - applies //

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsSearched(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.contains((E[])this.data, this.data.length, predicate);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean applies(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.applies((E[])this.data, this.data.length, predicate);
	}

	// boolean querying - contains //

	@SuppressWarnings("unchecked")
	@Override
	public boolean nullContained()
	{
		return AbstractArrayStorage.nullContained((E[])this.data, this.data.length);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsId(final E element)
	{
		return AbstractArrayStorage.containsSame((E[])this.data, this.data.length, element);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(final E element)
	{
		return AbstractArrayStorage.containsSame((E[])this.data, this.data.length, element);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsAll(final XGettingCollection<? extends E> elements)
	{
		return AbstractArrayStorage.containsAll((E[])this.data, this.data.length, elements);
	}

	// boolean querying - equality //

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		if(samples == null || !(samples instanceof FixedList<?>) || Jadoth.to_int(samples.size()) != this.data.length) return false;
		if(samples == this) return true;

		// equivalent to equalsContent()
		return JadothArrays.equals(this.data, 0, ((FixedList<?>)samples).data, 0, this.data.length, (Equalator<Object>)equalator);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equalsContent(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		if(samples == null || Jadoth.to_int(samples.size()) != this.data.length){
			return false;
		}
		if(samples == this){
			return true;
		}
		return AbstractArrayStorage.equalsContent((E[])this.data, this.data.length, samples, equalator);
	}

	// data set procedures //

	@SuppressWarnings("unchecked")
	@Override
	public <C extends Procedure<? super E>> C intersect(
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		return AbstractArrayStorage.intersect((E[])this.data, this.data.length, samples, equalator, target);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C extends Procedure<? super E>> C except(
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		return AbstractArrayStorage.except((E[])this.data, this.data.length, samples, equalator, target);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C extends Procedure<? super E>> C union(
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		return AbstractArrayStorage.union((E[])this.data, this.data.length, samples, equalator, target);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C extends Procedure<? super E>> C copyTo(final C target)
	{
		return AbstractArrayStorage.copyTo((E[])this.data, this.data.length, target);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C extends Procedure<? super E>> C filterTo(final C target, final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.copyTo((E[])this.data, this.data.length, target, predicate);
	}

	@Override
	public <T> T[] copyTo(final T[] target, final int offset)
	{
		System.arraycopy(this.data, 0, target, offset, this.data.length);
		return target;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] copyTo(final T[] target, final int targetOffset, final int offset, final int length)
	{
		return AbstractArrayStorage.rngCopyTo((E[])this.data, this.data.length, offset, length, target, targetOffset);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C extends Procedure<? super E>> C distinct(final C target)
	{
		return AbstractArrayStorage.distinct((E[])this.data, this.data.length, target);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C extends Procedure<? super E>> C distinct(final C target, final Equalator<? super E> equalator)
	{
		return AbstractArrayStorage.distinct((E[])this.data, this.data.length, target, equalator);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C extends Procedure<? super E>> C copySelection(final C target, final int... indices)
	{
		return AbstractArrayStorage.copySelection((E[])this.data, this.data.length, indices, target);
	}



	///////////////////////////////////////////////////////////////////////////
	// setting methods  //
	/////////////////////

	@Override
	public SubListView<E> view(final int fromIndex, final int toIndex)
	{
		return new SubListView<>(this, fromIndex, toIndex); // range check is done in constructor
	}

	@Override
	public FixedList<E> swap(final int indexA, final int indexB) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		if(indexA >= this.data.length){
			throw new IndexBoundsException(this.data.length, indexA);
		}
		if(indexB >= this.data.length){
			throw new IndexBoundsException(this.data.length, indexB);
		}
		final Object t = this.data[indexA];
		this.data[indexA] = this.data[indexB];
		this.data[indexB] = t;
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FixedList<E> swap(final int indexA, final int indexB, final int length)
	{
		AbstractArrayStorage.swap((E[])this.data, this.data.length, indexA, indexB, length);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FixedList<E> reverse()
	{
		AbstractArrayStorage.reverse((E[])this.data, this.data.length);
		return this;
	}

	// direct setting //

	@Override
	public void setFirst(final E element)
	{
		this.data[0] = element;
	}

	@Override
	public void setLast(final E element)
	{
		this.data[this.data.length-1] = element;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FixedList<E> setAll(final int offset, final E... elements)
	{
		if(offset < 0 || offset + elements.length > this.data.length){
			throw new IndexOutOfBoundsException(exceptionStringRange(this.data.length, offset, offset + elements.length-1));
		}
		System.arraycopy(elements, 0, this.data, offset, elements.length);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FixedList<E> set(final int offset, final E[] src, final int srcIndex, final int srcLength)
	{
		AbstractArrayStorage.set((E[])this.data, this.data.length, offset, src, srcIndex, srcLength);
		return this;
	}

	@Override
	public FixedList<E> set(final int offset, final XGettingSequence<? extends E> elements, final int elementsOffset, final int elementsLength)
	{
		AbstractArrayStorage.set(this.data, this.data.length, offset, elements, elementsOffset, elementsLength);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FixedList<E> fill(final int offset, final int length, final E element)
	{
		AbstractArrayStorage.fill((E[])this.data, this.data.length, offset, length, element);
		return this;
	}

	// sorting //

	@SuppressWarnings("unchecked")
	@Override
	public FixedList<E> sort(final Comparator<? super E> comparator)
	{
		JadothSort.mergesort(this.data, 0, this.data.length, (Comparator<Object>)comparator);
		return this;
	}

	// replacing - single //

	@SuppressWarnings("unchecked")
	@Override
	public boolean replaceOne(final E element, final E replacement)
	{
		return AbstractArrayStorage.replaceOne((E[])this.data, this.data.length, element, replacement);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean replaceOne(final Predicate<? super E> predicate, final E substitute)
	{
		return AbstractArrayStorage.substituteOne((E[])this.data, this.data.length, predicate, substitute);
	}

	// replacing - multiple //

	@SuppressWarnings("unchecked")
	@Override
	public int replace(final E element, final E replacement)
	{
		return AbstractArrayStorage.replace((E[])this.data, this.data.length, element, replacement);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int replace(final Predicate<? super E> predicate, final E substitute)
	{
		return AbstractArrayStorage.substitute((E[])this.data, this.data.length, predicate, substitute);
	}

	@Override
	public int replaceAll(final XGettingCollection<? extends E> elements, final E replacement)
	{
		return AbstractArrayStorage.replaceAll(this.data, this.data.length, elements, replacement, marker());
	}

	@SuppressWarnings("unchecked")
	@Override
	public int modify(final Function<E, E> mapper)
	{
		return AbstractArrayStorage.modify((E[])this.data, this.data.length, mapper);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int modify(final Predicate<? super E> predicate, final Function<E, E> mapper)
	{
		return AbstractArrayStorage.modify((E[])this.data, this.data.length, predicate, mapper);
	}



	///////////////////////////////////////////////////////////////////////////
	// java.util.list and derivatives  //
	////////////////////////////////////

	@Override
	public boolean isEmpty()
	{
		return this.data.length == 0;
	}

	@Override
	public Iterator<E> iterator()
	{
		return new ReadOnlyListIterator<>(this);
	}

	@Override
	public ListIterator<E> listIterator()
	{
		return new ReadOnlyListIterator<>(this);
	}

	@Override
	public ListIterator<E> listIterator(final int index)
	{
		return new ReadOnlyListIterator<>(this, index);
	}

	@Override
	public boolean set(final int index, final E element) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		if(index >= this.data.length){
			throw new IndexBoundsException(this.data.length, index);
		}
		this.data[index] = element;
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E setGet(final int index, final E element) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		if(index >= this.data.length){
			throw new IndexBoundsException(this.data.length, index);
		}
		final E old = (E)this.data[index];
		this.data[index] = element;
		return old;
	}

	@Override
	public long size()
	{
		return this.data.length;
	}

	@Override
	public long maximumCapacity()
	{
		return this.data.length; // max capacity is always array length
	}

	@Override
	public boolean isFull()
	{
		return true; // fixed list is always "full"
	}

	@Override
	public long remainingCapacity()
	{
		return 0;
	}

	@Override
	public ListView<E> view()
	{
		return new ListView<>(this);
	}

	@Override
	public FixedList<E> shiftTo(final int sourceIndex, final int targetIndex)
	{
		if(sourceIndex >= this.data.length){
			throw new IndexBoundsException(this.data.length, sourceIndex);
		}
		if(targetIndex >= this.data.length){
			throw new IndexBoundsException(this.data.length, targetIndex);
		}
		if(sourceIndex == targetIndex){
			if(sourceIndex < 0){
				throw new IndexBoundsException(this.data.length, sourceIndex);
			}
			return this;
		}

		final Object shiftling = this.data[sourceIndex];
		if(sourceIndex < targetIndex){
			System.arraycopy(this.data, sourceIndex + 1, this.data, sourceIndex, targetIndex - sourceIndex);
		}
		else {
			System.arraycopy(this.data, targetIndex, this.data, targetIndex + 1, sourceIndex - targetIndex);
		}

		this.data[targetIndex] = shiftling;
		return this;
	}

	@Override
	public FixedList<E> shiftTo(final int sourceIndex, final int targetIndex, final int length)
	{
		if(sourceIndex + length >= this.data.length){
			throw new IndexBoundsException(this.data.length, sourceIndex);
		}
		if(targetIndex + length >= this.data.length){
			throw new IndexBoundsException(this.data.length, targetIndex);
		}
		if(sourceIndex == targetIndex){
			if(sourceIndex < 0){
				throw new IndexBoundsException(this.data.length, sourceIndex);
			}
			return this;
		}

		final Object[] shiftlings;
		System.arraycopy(this.data, sourceIndex, shiftlings = new Object[length], 0, length);
		if(sourceIndex < targetIndex){
			System.arraycopy(this.data, sourceIndex + length, this.data, sourceIndex, targetIndex - sourceIndex);
		}
		else {
			System.arraycopy(this.data, targetIndex, this.data, targetIndex + length, sourceIndex - targetIndex);
		}

		System.arraycopy(shiftlings, 0, this.data, targetIndex, length);
		return this;
	}

	@Override
	public FixedList<E> shiftBy(final int sourceIndex, final int distance)
	{
		return this.shiftTo(sourceIndex, sourceIndex + distance);
	}

	@Override
	public FixedList<E> shiftBy(final int sourceIndex, final int distance, final int length)
	{
		return this.shiftTo(sourceIndex, sourceIndex + distance, length);
	}

	@Override
	public SubListAccessor<E> range(final int fromIndex, final int toIndex)
	{
		// range check is done in constructor
		return new SubListAccessor<>(this, fromIndex, toIndex);
	}

	@Override
	public String toString()
	{
		return AbstractArrayStorage.toString(this.data, this.data.length);
	}

	@Override
	public Object[] toArray()
	{
		return this.data.clone();
	}

	@SuppressWarnings("unchecked")
	@Override
	public E at(final int index) throws ArrayIndexOutOfBoundsException
	{
		if(index >= this.data.length){
			throw new IndexBoundsException(this.data.length, index);
		}
		return (E)this.data[index];
	}

	@Deprecated
	@Override
	public boolean equals(final Object o)
	{
		//trivial escape conditions
		if(o == this) return true;
		if(o == null || !(o instanceof List<?>)) return false;

		final List<?> list = (List<?>)o;
		if(this.data.length != list.size()) return false; //lists can only be equal if they have the same length

		final Object[] data = this.data;
		int i = 0;
		for(final Object e2 : list) { //use iterator for passed list as it could be a non-random-access list
			final Object e1 = data[i++];
			if(e1 == null){ //null-handling escape conditions
				if(e2 != null) return false;
				continue;
			}
			if(!e1.equals(e2)) return false;
		}
		return true; //no un-equal element found, so lists must be equal
	}

	@Deprecated
	@Override
	public int hashCode()
	{
		return JadothArrays.arrayHashCode(this.data, this.data.length);
	}



	@Override
	public OldFixedList<E> old()
	{
		return new OldFixedList<>(this);
	}

	public static final class OldFixedList<E> extends OldSettingList<E>
	{
		OldFixedList(final FixedList<E> list)
		{
			super(list);
		}

		@Override
		public FixedList<E> parent()
		{
			return (FixedList<E>)super.parent();
		}

	}

}
