package net.jadoth.collections;

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
import net.jadoth.collections.types.XImmutableList;
import net.jadoth.collections.types.XList;
import net.jadoth.collections.types.XSettingList;
import net.jadoth.exceptions.IndexBoundsException;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.IndexProcedure;
import net.jadoth.functional.JadothEqualators;
import net.jadoth.functional.Procedure;
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
public final class ArrayAccessor<E> extends AbstractSimpleArrayCollection<E> implements XSettingList<E>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	private static final Object   MARKER = new Object() ;
	private static final Object[] DUMMY  = new Object[0];



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

	private E[] data; // the storage array containing the elements
	private int size; // the current element count (logical size)



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	@SuppressWarnings("unchecked")
	public ArrayAccessor()
	{
		super();
		this.data = (E[])DUMMY;
		this.size = 0;
	}

	public ArrayAccessor(final ArrayAccessor<? extends E> original) throws NullPointerException
	{
		super();
		this.data = original.data;
		this.size = original.size;
	}

	@SafeVarargs
	public ArrayAccessor(final E... elements) throws NullPointerException
	{
		super();
		this.data = elements;
		this.size = elements.length;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	public E[] getArray()
	{
		return this.data == DUMMY ?null :(E[])this.data;
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
	// setters          //
	/////////////////////

	@SuppressWarnings("unchecked")
	public ArrayAccessor<E> setArray(final E[] array)
	{
		if(array == null){
			this.data = (E[])DUMMY;
			this.size = 0;
			return this;
		}

		if(this.size < 0 || this.size > array.length){
			throw new ArrayIndexOutOfBoundsException(this.size);
		}
		this.data = array;
		this.size = array.length;
		return this;
	}


	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	protected E[] internalGetStorageArray()
	{
		return this.data;
	}

	@Override
	protected int internalSize()
	{
		return this.size;
	}

	@Override
	protected int[] internalGetSectionIndices()
	{
		return new int[]{0, this.size}; // trivial section
	}



	///////////////////////////////////////////////////////////////////////////
	// getting methods  //
	/////////////////////

	@Override
	public ArrayAccessor<E> copy()
	{
		return new ArrayAccessor<>(this);
	}

	@Override
	public XImmutableList<E> immure()
	{
		return new ConstList<>(this);
	}

	@Override
	public ArrayAccessor<E> toReversed()
	{
		final E[] rData = JadothArrays.newArrayBySample(this.data, this.size);
		final E[] data = this.data;
		for(int i = this.size, r = 0; i --> 0;){
			rData[r++] = data[i];
		}
		return new ArrayAccessor<>(rData);
	}

	@Override
	public E[] toArray(final Class<E> type)
	{
		final E[] array = JadothArrays.newArray(type, this.size);
		System.arraycopy(this.data, 0, array, 0, this.size);
		return array;
	}

	// executing //

	@Override
	public <P extends Procedure<? super E>> P iterate(final P procedure)
	{
		AbstractArrayStorage.iterate(this.data, this.size, procedure);
		return procedure;
	}

	@Override
	public final <A> A join(final BiProcedure<? super E, ? super A> joiner, final A aggregate)
	{
		AbstractArrayStorage.join(this.data, this.size, joiner, aggregate);
		return aggregate;
	}

	@Override
	public final <P extends IndexProcedure<? super E>> P iterate(final P procedure)
	{
		AbstractArrayStorage.iterate(this.data, this.size, procedure);
		return procedure;
	}

	// count querying //

	@Override
	public int count(final E element)
	{
		return AbstractArrayStorage.count(this.data, this.size, element);
	}

	@Override
	public int countBy(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.conditionalCount(this.data, this.size, predicate);
	}

	// index querying //

	@Override
	public int indexOf(final E element)
	{
		return AbstractArrayStorage.indexOf(this.data, this.size, element);
	}

	@Override
	public int indexBy(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.conditionalIndexOf(this.data, this.size, predicate);
	}

	@Override
	public int lastIndexOf(final E element)
	{
		return AbstractArrayStorage.rngIndexOF(this.data, this.size, this.size - 1, -this.size, element);
	}

	@Override
	public int lastIndexBy(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.lastIndexOf(this.data, this.size, predicate);
	}

	@Override
	public int maxIndex(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.maxIndex(this.data, this.size, comparator);
	}

	@Override
	public int minIndex(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.minIndex(this.data, this.size, comparator);
	}

	@Override
	public int scan(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.scan(this.data, this.size, predicate);
	}

	// element querying //

	@Override
	public E get()
	{
		return this.data[0];
	}

	@Override
	public E first()
	{
		return this.data[0];
	}

	@Override
	public E last()
	{
		return this.data[this.size - 1];
	}

	@Override
	public E poll()
	{
		return this.size == 0 ?null :(E)this.data[0];
	}

	@Override
	public E peek()
	{
		return this.size == 0 ?null :(E)this.data[this.size - 1];
	}

//	@Override
//	public E search(final E sample, final Equalator<? super E> equalator)
//	{
//		return AbstractArrayStorage.find(this.data, this.size, sample, equalator);
//	}

	@Override
	public E search(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.queryElement(this.data, this.size, predicate, null);
	}

	@Override
	public E seek(final E sample)
	{
		return AbstractArrayStorage.containsSame(this.data, this.size, sample) ?sample :null;
	}

	@Override
	public E max(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.max(this.data, this.size, comparator);
	}

	@Override
	public E min(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.min(this.data, this.size, comparator);
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

	@Override
	public boolean isSorted(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.isSorted(this.data, this.size, comparator);
	}

	@Override
	public boolean hasDistinctValues()
	{
		return AbstractArrayStorage.hasDistinctValues(this.data, this.size);
	}

	@Override
	public boolean hasDistinctValues(final Equalator<? super E> equalator)
	{
		return AbstractArrayStorage.hasDistinctValues(this.data, this.size, equalator);
	}

	// boolean querying - applies //

	@Override
	public boolean containsSearched(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.contains(this.data, this.size, predicate);
	}

	@Override
	public boolean applies(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.applies(this.data, this.size, predicate);
	}

	// boolean querying - contains //

	@Override
	public boolean nullContained()
	{
		return AbstractArrayStorage.nullContained(this.data, this.size);
	}

	@Override
	public boolean containsId(final E element)
	{
		return AbstractArrayStorage.containsSame(this.data, this.size, element);
	}

	@Override
	public boolean contains(final E element)
	{
		return AbstractArrayStorage.containsSame(this.data, this.size, element);
	}

	@Override
	public boolean containsAll(final XGettingCollection<? extends E> elements)
	{
		return AbstractArrayStorage.containsAll(this.data, this.size, elements);
	}

//	@Override
//	public boolean containsAll(final XGettingCollection<? extends E> elements, final Equalator<? super E> equalator)
//	{
//		return AbstractArrayStorage.containsAll(this.data, this.size, elements, equalator);
//	}

	// boolean querying - equality //

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		if(samples == null || !(samples instanceof ArrayAccessor<?>) || Jadoth.to_int(samples.size()) != this.size){
			return false;
		}
		if(samples == this){
			return true;
		}

		// equivalent to equalsContent()
		return JadothArrays.equals(this.data, 0, ((ArrayAccessor<?>)samples).data, 0, this.size, (Equalator<Object>)equalator);
	}

	@Override
	public boolean equalsContent(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		if(samples == null || Jadoth.to_int(samples.size()) != this.size){
			return false;
		}
		if(samples == this){
			return true;
		}
		return AbstractArrayStorage.equalsContent(this.data, this.size, samples, equalator);
	}

	// data set procedures //

	@Override
	public <C extends Procedure<? super E>> C intersect(
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		return AbstractArrayStorage.intersect(this.data, this.size, samples, equalator, target);
	}

	@Override
	public <C extends Procedure<? super E>> C except(
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		return AbstractArrayStorage.except(this.data, this.size, samples, equalator, target);
	}

	@Override
	public <C extends Procedure<? super E>> C union(
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		return AbstractArrayStorage.union(this.data, this.size, samples, equalator, target);
	}

	@Override
	public <C extends Procedure<? super E>> C copyTo(final C target)
	{
		return AbstractArrayStorage.copyTo(this.data, this.size, target);
	}

	@Override
	public <C extends Procedure<? super E>> C filterTo(final C target, final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.copyTo(this.data, this.size, target, predicate);
	}

	@Override
	public <T> T[] copyTo(final T[] target, final int offset)
	{
		System.arraycopy(this.data, 0, target, offset, this.size);
		return target;
	}

	@Override
	public <T> T[] copyTo(final T[] target, final int targetOffset, final int offset, final int length)
	{
		return AbstractArrayStorage.rngCopyTo(this.data, this.size, offset, length, target, targetOffset);
	}

	public <T> T[] rngCopyTo(final int startIndex, final int length, final T[] target, final int offset)
	{
		return AbstractArrayStorage.rngCopyTo(
			this.data, this.size, startIndex, length,  target, offset
		);
	}

	@Override
	public <C extends Procedure<? super E>> C distinct(final C target)
	{
		return AbstractArrayStorage.distinct(this.data, this.size, target);
	}

	@Override
	public <C extends Procedure<? super E>> C distinct(final C target, final Equalator<? super E> equalator)
	{
		return AbstractArrayStorage.distinct(this.data, this.size, target, equalator);
	}

	@Override
	public <C extends Procedure<? super E>> C copySelection(final C target, final int... indices)
	{
		return AbstractArrayStorage.copySelection(this.data, this.size, indices, target);
	}



	///////////////////////////////////////////////////////////////////////////
	// setting methods  //
	/////////////////////

	@Override
	public ListView<E> view()
	{
		return new ListView<>(this);
	}

	@Override
	public SubListView<E> view(final int fromIndex, final int toIndex)
	{
		return new SubListView<>(this, fromIndex, toIndex); // range check is done in constructor
	}

	@Override
	public ArrayAccessor<E> shiftTo(final int sourceIndex, final int targetIndex)
	{
		if(sourceIndex >= this.size){
			throw new IndexBoundsException(this.size, sourceIndex);
		}
		if(targetIndex >= this.size){
			throw new IndexBoundsException(this.size, targetIndex);
		}
		if(sourceIndex == targetIndex){
			if(sourceIndex < 0){
				throw new IndexBoundsException(this.size, sourceIndex);
			}
			return this;
		}

		final E shiftling = this.data[sourceIndex];
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
	public ArrayAccessor<E> shiftTo(final int sourceIndex, final int targetIndex, final int length)
	{
		if(sourceIndex + length >= this.size){
			throw new IndexBoundsException(this.size, sourceIndex);
		}
		if(targetIndex + length >= this.size){
			throw new IndexBoundsException(this.size, targetIndex);
		}
		if(sourceIndex == targetIndex){
			if(sourceIndex < 0){
				throw new IndexBoundsException(this.size, sourceIndex);
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
	public ArrayAccessor<E> shiftBy(final int sourceIndex, final int distance)
	{
		return this.shiftTo(sourceIndex, sourceIndex + distance);
	}

	@Override
	public ArrayAccessor<E> shiftBy(final int sourceIndex, final int distance, final int length)
	{
		return this.shiftTo(sourceIndex, sourceIndex + distance, length);
	}

	@Override
	public ArrayAccessor<E> swap(final int indexA, final int indexB) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		if(indexA >= this.size){
			throw new IndexBoundsException(this.size, indexA);
		}
		if(indexB >= this.size){
			throw new IndexBoundsException(this.size, indexB);
		}
		final E t = this.data[indexA];
		this.data[indexA] = this.data[indexB];
		this.data[indexB] = t;
		return this;
	}

	@Override
	public ArrayAccessor<E> swap(final int indexA, final int indexB, final int length)
	{
		AbstractArrayStorage.swap(this.data, this.size, indexA, indexB, length);
		return this;
	}

	@Override
	public ArrayAccessor<E> reverse()
	{
		AbstractArrayStorage.reverse(this.data, this.size);
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
		this.data[this.size-1] = element;
	}

	@SafeVarargs
	@Override
	public final ArrayAccessor<E> setAll(final int offset, final E... elements)
	{
		if(offset < 0 || offset + elements.length > this.size){
			throw new IndexOutOfBoundsException(exceptionStringRange(this.size, offset, offset + elements.length-1));
		}
		System.arraycopy(elements, 0, this.data, offset, elements.length);
		return this;
	}

	@Override
	public ArrayAccessor<E> set(final int offset, final E[] src, final int srcIndex, final int srcLength)
	{
		AbstractArrayStorage.set(this.data, this.size, offset, src, srcIndex, srcLength);
		return this;
	}

	@Override
	public ArrayAccessor<E> set(final int offset, final XGettingSequence<? extends E> elements, final int elementsOffset, final int elementsLength)
	{
		AbstractArrayStorage.set(this.data, this.size, offset, elements, elementsOffset, elementsLength);
		return this;
	}

	@Override
	public ArrayAccessor<E> fill(final int offset, final int length, final E element)
	{
		AbstractArrayStorage.fill(this.data, this.size, offset, length, element);
		return this;
	}

	// sorting //

	@Override
	public ArrayAccessor<E> sort(final Comparator<? super E> comparator)
	{
		JadothSort.mergesort(this.data, 0, this.size, comparator);
		return this;
	}

	// replacing - single //

	@Override
	public boolean replaceOne(final E element, final E replacement)
	{
		return AbstractArrayStorage.replaceOne(this.data, this.size, element, replacement);
	}

//	@Override
//	public boolean replaceOne(final E sample, final Equalator<? super E> equalator, final E replacement)
//	{
//		return AbstractArrayStorage.replaceOne(this.data, this.size, sample, replacement, equalator);
//	}

	@Override
	public boolean replaceOne(final Predicate<? super E> predicate, final E substitute)
	{
		return AbstractArrayStorage.substituteOne(this.data, this.size, predicate, substitute);
	}

	// replacing - multiple //

	@Override
	public int replace(final E element, final E replacement)
	{
		return AbstractArrayStorage.replace(this.data, this.size, element, replacement);
	}

//	@Override
//	public int replace(final E sample, final Equalator<? super E> equalator, final E replacement)
//	{
//		return AbstractArrayStorage.replace(this.data, this.size, sample, replacement, equalator);
//	}

	@Override
	public int replace(final Predicate<? super E> predicate, final E substitute)
	{
		return AbstractArrayStorage.substitute(this.data, this.size, predicate, substitute);
	}

//	@Override
//	public int replace(final CtrlPredicate<? super E> predicate, final E substitute)
//	{
//		return AbstractArrayStorage.replace(this.data, this.size, predicate, substitute);
//	}

	// replacing - multiple all //

	@Override
	public int replaceAll(final XGettingCollection<? extends E> elements, final E replacement)
	{
		return AbstractArrayStorage.replaceAll(this.data, this.size, elements, replacement, MARKER);
	}

//	@Override
//	public int replaceAll(
//		final XGettingCollection<? extends E> samples,
//		final Equalator<? super E> equalator,
//		final E newElement
//	)
//	{
//		return AbstractArrayStorage.replaceAll(this.data, this.size, samples, newElement, equalator);
//	}

	@Override
	public int modify(final Function<E, E> mapper)
	{
		return AbstractArrayStorage.modify(this.data, this.size, mapper);
	}

	@Override
	public int modify(final Predicate<? super E> predicate, final Function<E, E> mapper)
	{
		return AbstractArrayStorage.modify(this.data, this.size, predicate, mapper);
	}

//	@Override
//	public int modify(final CtrlPredicate<? super E> predicate, final Function<E, E> mapper)
//	{
//		return AbstractArrayStorage.modify(this.data, this.size, predicate, mapper);
//	}



	///////////////////////////////////////////////////////////////////////////
	// java.util.list and derivatives  //
	////////////////////////////////////

	@Override
	public boolean isEmpty()
	{
		return this.size == 0;
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
		if(index >= this.size){
			throw new IndexBoundsException(this.size, index);
		}
		this.data[index] = element;
		return false;
	}

	@Override
	public E setGet(final int index, final E element) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		if(index >= this.size){
			throw new IndexBoundsException(this.size, index);
		}
		final E old = this.data[index];
		this.data[index] = element;
		return old;
	}

	@Override
	public long size()
	{
		return this.size;
	}

	@Override
	public long maximumCapacity()
	{
		return this.size; // size is always array length
	}

	@Override
	public boolean isFull()
	{
		return true; // array is always "full"
	}

	@Override
	public long remainingCapacity()
	{
		return 0;
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
		return AbstractArrayStorage.toString(this.data, this.size);
	}

	@Override
	public Object[] toArray()
	{
		final Object[] array;
		System.arraycopy(this.data, 0, array = new Object[this.size], 0, this.size);
		return array;
	}

	@Override
	public E at(final int index) throws ArrayIndexOutOfBoundsException
	{
		if(index >= this.size){
			throw new IndexBoundsException(this.size, index);
		}
		return this.data[index];
	}

	@Deprecated
	@Override
	public boolean equals(final Object o)
	{
		//trivial escape conditions
		if(o == this){
			return true;
		}
		// (09.04.2012)FIXME: what's List supposed to do here? (check all other occurances as well)
		if(o == null || !(o instanceof List<?>)){
			return false;
		}

		final List<?> list = (List<?>)o;
		if(this.size != list.size()){
			return false; //lists can only be equal if they have the same length
		}

		final Object[] data = this.data;
		int i = 0;
		for(final Object e2 : list) { //use iterator for passed list as it could be a non-random-access list
			final Object e1 = data[i++];
			if(e1 == null){ //null-handling escape conditions
				if(e2 != null){
					return false;
				}
				continue;
			}
			if(!e1.equals(e2)){
				return false;
			}
		}
		return true; //no un-equal element found, so lists must be equal
	}

	@Deprecated
	@Override
	public int hashCode()
	{
		return JadothArrays.arrayHashCode(this.data, this.size);
	}


	@Override
	public OldArrayAccessor<E> old()
	{
		return new OldArrayAccessor<>(this);
	}

	public static final class OldArrayAccessor<E> extends OldSettingList<E>
	{
		OldArrayAccessor(final ArrayAccessor<E> list)
		{
			super(list);
		}

		@Override
		public ArrayAccessor<E> parent()
		{
			return (ArrayAccessor<E>)super.parent();
		}

	}



}
