package net.jadoth.collections;

import static net.jadoth.Jadoth.checkArrayRange;
import static net.jadoth.collections.JadothArrays.removeAllFromArray;

import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.functions.KamikazeArrayAdder;
import net.jadoth.collections.old.BridgeXList;
import net.jadoth.collections.types.IdentityEqualityLogic;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.collections.types.XList;
import net.jadoth.exceptions.IndexBoundsException;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.IndexProcedure;
import net.jadoth.functional.JadothEqualators;
import net.jadoth.functional.Procedure;
import net.jadoth.util.Composition;
import net.jadoth.util.Equalator;
import net.jadoth.util.iterables.GenericListIterator;


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
public final class LimitList<E> extends AbstractSimpleArrayCollection<E>
implements XList<E>, Composition, IdentityEqualityLogic
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	// internal marker object for marking to be removed buckets for batch removal and null ambiguity resolution
	private static final Object MARKER = new Object();



	///////////////////////////////////////////////////////////////////////////
	//  class methods   //
	/////////////////////

	// (23.10.2013 TM)XXX: replace exception strings by proper exceptions (stupid)
	private static String exceptionStringRange(final int size, final int startIndex, final int length)
	{
		return "Range ["+(length < 0 ?startIndex+length+1+";"+startIndex
			:startIndex+";"+(startIndex+length-1))+"] not in [0;"+(size-1)+"]";
	}

	public static final <E> LimitList<E> New(final long initialCapacity)
	{
		return new LimitList<>(checkArrayRange(initialCapacity));
	}

	@SafeVarargs
	public static final <E> LimitList<E> New(final E... initialElements)
	{
		return new LimitList<>(initialElements);
	}

	public static final <E> LimitList<E> New(final XGettingCollection<E> initialElements)
	{
		return new LimitList<>(initialElements);
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	final E[] data ; // the storage array containing the elements
	final int limit; // redundant limit checking value (for performance reasons). For free due to memory alignment.
	int       size ; // the current element count (logical size)



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	public LimitList(final int initialCapacity)
	{
		super();
		this.size = 0;
		this.data = newArray(initialCapacity);
		this.limit = initialCapacity;
	}

	public LimitList(final LimitList<? extends E> original) throws NullPointerException
	{
		super();
		this.size = original.size;
		this.data = original.data.clone();
		this.limit = this.data.length;
	}

	public LimitList(final Collection<? extends E> elements) throws NullPointerException
	{
		super();
		elements.toArray(this.data = newArray(this.size = elements.size()));
		this.limit = this.data.length;
	}

	public LimitList(final XGettingCollection<? extends E> elements) throws NullPointerException
	{
		super();
		if(elements.hasVolatileElements()){
			final E[] buffer = bendArray(elements.toArray()); // reduce to only actual elements
			this.data = newArray(this.size = buffer.length);
			System.arraycopy(buffer, 0, this.data, 0, this.size);
		}
		else {
			this.data = bendArray(elements.copyTo(new Object[this.size = Jadoth.to_int(elements.size())], 0));
		}
		this.limit = this.data.length;
	}

	@SafeVarargs
	public LimitList(final E... elements) throws NullPointerException
	{
		super();
		System.arraycopy(
			elements, 0,
			this.data = newArray(this.size = elements.length), 0,
			this.size
		);
		this.limit = this.data.length;
	}

	public LimitList(final int initialCapacity, final E[] src, final int srcStart, final int srcLength)
	{
		super();
		this.data = newArray(initialCapacity);
		System.arraycopy(src, srcStart, this.data, 0, srcLength); // automatically check arguments 8-)
		this.size = srcLength; // srcLength has already been checked above
		this.limit = initialCapacity;
	}

	LimitList(final E[] internalData, final int size)
	{
		super();
		this.size = size;
		this.data = internalData;
		this.limit = internalData.length;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	private int internalInsertArray(final int index, final Object[] elements, final int elementsSize)
	{
		// (02.07.2011)FIXME: adaptive inserting
		// (02.07.2011)FIXME: internalInputArray
		// check for necessary capacity increase
		if(this.limit - this.size < elementsSize){
			throw new IndexBoundsException(this.limit, elementsSize - 1);
		}

		// simply free up enough space at index and slide in new elements
		System.arraycopy(this.data, index, this.data, index + elementsSize, this.size - index);
		System.arraycopy(elements, 0, this.data, index, elementsSize);
		this.size += elementsSize;
		return elementsSize;
	}

	private int internalInsertArray(final int index, final Object[] elements, final int offset, final int length)
	{
		if(length < 0){
			// check for necessary capacity increase
			if(this.limit - this.size < -length){
				throw new IndexBoundsException(this.limit, -length - 1);
			}

			// simply free up enough space at index and slide in new elements
			System.arraycopy(this.data, index, this.data, index - length, this.size - index);
			JadothArrays.reverseArraycopy(elements, offset, elements , index, -length);
			this.size -= length;
			return -length;
		}

		// check for necessary capacity increase
		if(this.limit - this.size < length){
			throw new IndexBoundsException(this.limit, length - 1);
		}

		// simply free up enough space at index and slide in new elements
		System.arraycopy(this.data, index, this.data, index + length, this.size - index);
		System.arraycopy(elements ,     0, this.data, index         , length           );
		this.size += length;
		return length;
	}

	private int internalInputArray(final int index, final Object[] elements, final int elementsSize)
	{
		// check for necessary capacity increase
		if(this.limit - this.size < elementsSize){
			throw new IndexBoundsException(this.limit, elementsSize - 1);
		}

		// simply free up enough space at index and slide in new elements
		System.arraycopy(this.data, index, this.data, index + elementsSize, this.size - index);
		System.arraycopy(elements, 0, this.data, index, elementsSize);
		this.size += elementsSize;
		return elementsSize;
	}

	private int internalInputArray(final int index, final Object[] elements, final int offset, final int length)
	{
		if(length < 0){
			// check for necessary capacity increase
			if(this.limit - this.size < -length){
				throw new IndexBoundsException(this.limit, -length - 1);
			}

			// simply free up enough space at index and slide in new elements
			System.arraycopy(this.data, index, this.data, index - length, this.size - index);
			JadothArrays.reverseArraycopy(elements, offset, elements , index, -length);
			this.size -= length;
			return -length;
		}

		// check for necessary capacity increase
		if(this.limit - this.size < length){
			throw new IndexBoundsException(this.limit, length - 1);
		}

		// simply free up enough space at index and slide in new elements
		System.arraycopy(this.data, index, this.data, index + length, this.size - index);
		System.arraycopy(elements ,     0, this.data, index         , length           );
		this.size += length;
		return length;
	}


	public static final class Creator<E> implements XList.Creator<E>
	{
		private final int initialCapacity;

		public Creator(final int initialCapacity)
		{
			super();
			if(initialCapacity < 0){
				throw new IllegalArgumentException("initial capacity may not be negative.");
			}
			this.initialCapacity = initialCapacity;
		}

		public final int getInitialCapacity()
		{
			return this.initialCapacity;
		}

		@Override
		public final LimitList<E> newInstance()
		{
			return new LimitList<>(AbstractArrayCollection.<E>newArray(this.initialCapacity), this.initialCapacity);
		}

	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	protected final E[] internalGetStorageArray()
	{
		return this.data;
	}

	@Override
	protected final int internalSize()
	{
		return this.size;
	}

	@Override
	protected final int[] internalGetSectionIndices()
	{
		return new int[]{0, this.size}; // trivial section
	}

	@Override
	public final Equalator<? super E> equality()
	{
		return JadothEqualators.identity();
	}

	@Override
	protected final int internalCountingAddAll(final E[] elements) throws UnsupportedOperationException
	{
		if(this.limit - this.size >= elements.length){
			System.arraycopy(elements, 0, this.data, this.size, elements.length);
			this.size += elements.length;
			return elements.length;
		}

		final int partialLength;
		System.arraycopy(elements, 0, this.data, this.size, partialLength = this.limit - this.size);
		this.size = this.limit; // list is now "full", of course, addition can be spared
		return partialLength;
	}


	@Override
	protected final int internalCountingAddAll(final E[] elements, final int offset, final int length) throws UnsupportedOperationException
	{
		// (28.06.2011)XXX: internalCountingAddAll
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
//		if(length >= 0){
//			this.ensureFreeCapacity(length); // increaseCapacity
//			System.arraycopy(elements, offset, this.data, this.size, length); // automatic bounds checks
//			this.size += length;
//			return length;
//		}
//
//		final int bound;
//		if((bound = offset + length) < -1){
//			throw new ArrayIndexOutOfBoundsException(bound+1);
//		}
//		this.ensureFreeCapacity(-length); // increaseCapacity
//		final Object[] data = this.data;
//		int size = this.size;
//		for(int i = offset; i > bound; i--){
//			data[size++] = elements[i];
//		}
//		this.size = size;
//		return -length;
	}

	@Override
	protected final int internalCountingAddAll(final XGettingCollection<? extends E> elements) throws UnsupportedOperationException
	{
		if(elements instanceof AbstractSimpleArrayCollection<?>){
			return this.internalCountingAddAll(
				AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements),
				0,
				Jadoth.to_int(elements.size())
			);
		}

		final int oldSize = this.size;
		try{
			this.size = elements.iterate(new KamikazeArrayAdder<E>(this.data, oldSize)).yield();
		}
		catch(final ArrayIndexOutOfBoundsException e){ // Kamikaze!
			this.size = this.limit; // list is now "full", of course, addition can be spared
		}
		return this.size - oldSize;
	}

	@Override
	protected final int internalCountingPutAll(final E[] elements) throws UnsupportedOperationException
	{
		this.ensureFreeCapacity(elements.length); // ensure capacity
		System.arraycopy(elements, 0, this.data, this.size, elements.length);
		this.size += elements.length;
		return elements.length;
	}

	@Override
	protected final int internalCountingPutAll(final E[] elements, final int offset, final int length) throws UnsupportedOperationException
	{
		// (30.06.2011 TM)XXX: internalCountingPutAll
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
//		if(length >= 0){
//			this.ensureFreeCapacity(length); // increaseCapacity
//			System.arraycopy(elements, offset, this.data, this.size, length); // automatic bounds checks
//			this.size += length;
//			return length;
//		}
//
//		final int bound;
//		if((bound = offset + length) < -1){
//			throw new ArrayIndexOutOfBoundsException(bound+1);
//		}
//		this.ensureFreeCapacity(-length); // increaseCapacity
//		final Object[] data = this.data;
//		int size = this.size;
//		for(int i = offset; i > bound; i--){
//			data[size++] = elements[i];
//		}
//		this.size = size;
//		return -length;
	}

	@Override
	protected final int internalCountingPutAll(final XGettingCollection<? extends E> elements) throws UnsupportedOperationException
	{
		if(elements instanceof AbstractSimpleArrayCollection<?>){
			return this.internalCountingAddAll(AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements), 0, Jadoth.to_int(elements.size()));
		}

		this.ensureFreeCapacity(Jadoth.to_int(elements.size())); // prevents Kamikaze later on :D
		final int oldSize;
		return (this.size = elements.iterate(new KamikazeArrayAdder<E>(this.data, oldSize = this.size)).yield()) - oldSize;
	}



	///////////////////////////////////////////////////////////////////////////
	// getting methods  //
	/////////////////////

	@Override
	public final LimitList<E> copy()
	{
		return new LimitList<>(this);
	}

	@Override
	public final ConstList<E> immure()
	{
		return new ConstList<>(this);
	}

	@Override
	public final LimitList<E> toReversed()
	{
		final E[] rData = newArray(this.limit);
		final E[] data = this.data;
		for(int i = this.size, r = 0; i --> 0;){
			rData[r++] = data[i];
		}
		return new LimitList<>(rData, this.size);
	}

	@Override
	public final E[] toArray(final Class<E> type)
	{
		final E[] array = JadothArrays.newArray(type, this.size);
		System.arraycopy(this.data, 0, array, 0, this.size);
		return array;
	}

	// executing //

	@Override
	public final <P extends Procedure<? super E>> P iterate(final P procedure)
	{
		AbstractArrayStorage.iterate(this.data, this.size, procedure);
		return procedure;
	}

	@Override
	public final <P extends IndexProcedure<? super E>> P iterate(final P procedure)
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
	public final int count(final E element)
	{
		return AbstractArrayStorage.count(this.data, this.size, element);
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public final int count(final E sample, final Equalator<? super E> equalator)
//	{
//		return AbstractArrayStorage.count((E[])this.data, this.size, sample, equalator);
//	}

	@Override
	public final int countBy(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.conditionalCount(this.data, this.size, predicate);
	}

	// index querying //

	@Override
	public final int indexOf(final E element)
	{
		return AbstractArrayStorage.indexOf(this.data, this.size, element);
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public final int indexOf(final E sample, final Equalator<? super E> equalator)
//	{
//		return AbstractArrayStorage.indexOf((E[])this.data, this.size, sample, equalator);
//	}

	@Override
	public final int indexBy(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.conditionalIndexOf(this.data, this.size, predicate);
	}

	@Override
	public final int lastIndexOf(final E element)
	{
		return AbstractArrayStorage.rngIndexOF(this.data, this.size, this.size - 1, -this.size, element);
	}

	@Override
	public final int lastIndexBy(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.lastIndexOf(this.data, this.size, predicate);
	}

	@Override
	public final int maxIndex(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.maxIndex(this.data, this.size, comparator);
	}

	@Override
	public final int minIndex(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.minIndex(this.data, this.size, comparator);
	}

	@Override
	public final int scan(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.scan(this.data, this.size, predicate);
	}

	// element querying //

	@Override
	public final E get()
	{
		return this.data[0];
	}

	@Override
	public final E first()
	{
		return this.data[0];
	}

	@Override
	public final E last()
	{
		return this.data[this.size-1];
	}

	@Override
	public final E poll()
	{
		return this.size == 0 ?null :(E)this.data[0];
	}

	@Override
	public final E peek()
	{
		return this.size == 0 ?null :(E)this.data[this.size - 1];
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public final E search(final E sample, final Equalator<? super E> equalator)
//	{
//		return AbstractArrayStorage.find((E[])this.data, this.size, sample, equalator);
//	}

	@Override
	public final E seek(final E sample)
	{
		return AbstractArrayStorage.containsSame(this.data, this.limit, sample) ?sample :null;
	}

	@Override
	public final E search(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.queryElement(this.data, this.size, predicate, null);
	}

	@Override
	public final E max(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.max(this.data, this.size, comparator);
	}

	@Override
	public final E min(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.min(this.data, this.size, comparator);
	}

	// boolean querying //

	@Override
	public final boolean hasVolatileElements()
	{
		return false;
	}

	@Override
	public final boolean nullAllowed()
	{
		return true;
	}

	@Override
	public final boolean isSorted(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.isSorted(this.data, this.size, comparator);
	}

	@Override
	public final boolean hasDistinctValues()
	{
		return AbstractArrayStorage.hasDistinctValues(this.data, this.size);
	}

	@Override
	public final boolean hasDistinctValues(final Equalator<? super E> equalator)
	{
		return AbstractArrayStorage.hasDistinctValues(this.data, this.size, equalator);
	}

	// boolean querying - applies //

	@Override
	public final boolean containsSearched(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.contains(this.data, this.size, predicate);
	}

	@Override
	public final boolean applies(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.applies(this.data, this.size, predicate);
	}

	// boolean querying - contains //

	@Override
	public final boolean nullContained()
	{
		return AbstractArrayStorage.nullContained(this.data, this.size);
	}

	@Override
	public final boolean containsId(final E element)
	{
		return AbstractArrayStorage.containsSame(this.data, this.size, element);
	}

	@Override
	public final boolean contains(final E element)
	{
		return AbstractArrayStorage.containsSame(this.data, this.size, element);
	}

	@Override
	public final boolean containsAll(final XGettingCollection<? extends E> elements)
	{
		return AbstractArrayStorage.containsAll(this.data, this.size, elements);
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public final boolean containsAll(final XGettingCollection<? extends E> elements, final Equalator<? super E> equalator)
//	{
//		return AbstractArrayStorage.containsAll((E[])this.data, this.size, elements, equalator);
//	}

	// boolean querying - equality //

	@SuppressWarnings("unchecked")
	@Override
	public final boolean equals(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		if(samples == null || !(samples instanceof LimitList<?>) || Jadoth.to_int(samples.size()) != this.size){
			return false;
		}
		if(samples == this){
			return true;
		}

		// equivalent to equalsContent()
		return JadothArrays.equals(this.data, 0, ((LimitList<?>)samples).data, 0, this.size, (Equalator<Object>)equalator);
	}

	@Override
	public final boolean equalsContent(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
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
	public final <C extends Procedure<? super E>> C intersect(
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		return AbstractArrayStorage.intersect(this.data, this.size, samples, equalator, target);
	}

	@Override
	public final <C extends Procedure<? super E>> C except(
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		return AbstractArrayStorage.except(this.data, this.size, samples, equalator, target);
	}

	@Override
	public final <C extends Procedure<? super E>> C union(
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		return AbstractArrayStorage.union(this.data, this.size, samples, equalator, target);
	}

	@Override
	public final <C extends Procedure<? super E>> C copyTo(final C target)
	{
		return AbstractArrayStorage.copyTo(this.data, this.size, target);
	}

	@Override
	public final <C extends Procedure<? super E>> C filterTo(final C target, final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.copyTo(this.data, this.size, target, predicate);
	}

	@Override
	public final <T> T[] copyTo(final T[] target, final int offset)
	{
		System.arraycopy(this.data, 0, target, offset, this.size);
		return target;
	}

	@Override
	public final <T> T[] copyTo(final T[] target, final int targetOffset, final int offset, final int length)
	{
		return AbstractArrayStorage.rngCopyTo(this.data, this.size, offset, length, target, targetOffset);
	}

	@Override
	public final <C extends Procedure<? super E>> C distinct(final C target)
	{
		return AbstractArrayStorage.distinct(this.data, this.size, target);
	}

	@Override
	public final <C extends Procedure<? super E>> C distinct(final C target, final Equalator<? super E> equalator)
	{
		return AbstractArrayStorage.distinct(this.data, this.size, target, equalator);
	}

	@Override
	public final <C extends Procedure<? super E>> C copySelection(final C target, final int... indices)
	{
		return AbstractArrayStorage.copySelection(this.data, this.size, indices, target);
	}



	///////////////////////////////////////////////////////////////////////////
	// setting methods  //
	/////////////////////

	@Override
	public final ListView<E> view()
	{
		return new ListView<>(this);
	}

	@Override
	public final SubListView<E> view(final int fromIndex, final int toIndex)
	{
		return new SubListView<>(this, fromIndex, toIndex); // range check is done in constructor
	}

	@Override
	public final LimitList<E> shiftTo(final int sourceIndex, final int targetIndex)
	{
		if(sourceIndex >= this.limit){
			throw new IndexBoundsException(this.limit, sourceIndex);
		}
		if(targetIndex >= this.limit){
			throw new IndexBoundsException(this.limit, targetIndex);
		}
		if(sourceIndex == targetIndex){
			if(sourceIndex < 0){
				throw new IndexBoundsException(this.limit, sourceIndex);
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
	public final LimitList<E> shiftTo(final int sourceIndex, final int targetIndex, final int length)
	{
		if(sourceIndex + length >= this.limit){
			throw new IndexBoundsException(this.limit, sourceIndex);
		}
		if(targetIndex + length >= this.limit){
			throw new IndexBoundsException(this.limit, targetIndex);
		}
		if(sourceIndex == targetIndex){
			if(sourceIndex < 0){
				throw new IndexBoundsException(this.limit, sourceIndex);
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
	public final LimitList<E> shiftBy(final int sourceIndex, final int distance)
	{
		return this.shiftTo(sourceIndex, sourceIndex + distance);
	}

	@Override
	public final LimitList<E> shiftBy(final int sourceIndex, final int distance, final int length)
	{
		return this.shiftTo(sourceIndex, sourceIndex + distance, length);
	}

	@Override
	public final LimitList<E> swap(final int indexA, final int indexB) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
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
	public final LimitList<E> swap(final int indexA, final int indexB, final int length)
	{
		AbstractArrayStorage.swap(this.data, this.size, indexA, indexB, length);
		return this;
	}

	@Override
	public final LimitList<E> reverse()
	{
		AbstractArrayStorage.reverse(this.data, this.size);
		return this;
	}

	// direct setting //

	@Override
	public final void setFirst(final E element)
	{
		this.data[0] = element;
	}

	@Override
	public final void setLast(final E element)
	{
		this.data[this.size-1] = element;
	}

	@SafeVarargs
	@Override
	public final LimitList<E> setAll(final int offset, final E... elements)
	{
		if(offset < 0 || offset + elements.length > this.size){
			throw new IndexOutOfBoundsException(exceptionStringRange(this.size, offset, offset + elements.length-1));
		}
		System.arraycopy(elements, 0, this.data, offset, elements.length);
		return this;
	}

	@Override
	public final LimitList<E> set(final int offset, final E[] src, final int srcIndex, final int srcLength)
	{
		AbstractArrayStorage.set(this.data, this.size, offset, src, srcIndex, srcLength);
		return this;
	}

	@Override
	public final LimitList<E> set(final int offset, final XGettingSequence<? extends E> elements, final int elementsOffset, final int elementsLength)
	{
		AbstractArrayStorage.set(this.data, this.size, offset, elements, elementsOffset, elementsLength);
		return this;
	}

	@Override
	public final LimitList<E> fill(final int offset, final int length, final E element)
	{
		AbstractArrayStorage.fill(this.data, this.size, offset, length, element);
		return this;
	}

	// sorting //

	@Override
	public final LimitList<E> sort(final Comparator<? super E> comparator)
	{
		JadothSort.mergesort(this.data, 0, this.size, comparator);
		return this;
	}

	// replacing - single //

	@Override
	public final boolean replaceOne(final E element, final E replacement)
	{
		return AbstractArrayStorage.replaceOne(this.data, this.size, element, replacement);
	}

	@Override
	public final boolean replaceOne(final Predicate<? super E> predicate, final E substitute)
	{
		return AbstractArrayStorage.substituteOne(this.data, this.size, predicate, substitute);
	}

	// replacing - multiple //

	@Override
	public final int replace(final E element, final E replacement)
	{
		return AbstractArrayStorage.replace(this.data, this.size, element, replacement);
	}

	@Override
	public final int replace(final Predicate<? super E> predicate, final E substitute)
	{
		return AbstractArrayStorage.substitute(this.data, this.size, predicate, substitute);
	}

	@Override
	public final int replaceAll(final XGettingCollection<? extends E> elements, final E replacement)
	{
		return AbstractArrayStorage.replaceAll(this.data, this.size, elements, replacement, MARKER);
	}

	@Override
	public final int modify(final Function<E, E> mapper)
	{
		return AbstractArrayStorage.modify(this.data, this.size, mapper);
	}

	@Override
	public final int modify(final Predicate<? super E> predicate, final Function<E, E> mapper)
	{
		return AbstractArrayStorage.modify(this.data, this.size, predicate, mapper);
	}



	///////////////////////////////////////////////////////////////////////////
	//  adding methods  //
	/////////////////////

	@Override
	public final long currentCapacity()
	{
		return this.limit;
	}

	@Override
	public final long maximumCapacity()
	{
		return this.limit;
	}

	@Override
	public final boolean isFull()
	{
		return this.size >= this.limit;
	}

	@Override
	public final long remainingCapacity()
	{
		return this.limit - this.size;
	}

	@Override
	public final int optimize()
	{
		return this.limit;
	}

	@Override
	public final LimitList<E> ensureFreeCapacity(final long requiredFreeCapacity)
	{
		// as opposed to ensureCapacity(size + requiredFreeCapacity), this subtraction is overflow-safe
		if(this.limit - this.size >= requiredFreeCapacity){
			throw new IndexBoundsException(this.limit, requiredFreeCapacity - 1);
		}
		return this;
	}

	@Override
	public final LimitList<E> ensureCapacity(final long minCapacity)
	{
		if(minCapacity > this.limit){
			throw new IndexBoundsException(this.limit, minCapacity - 1);
		}
		return this;
	}

	@Override
	public final boolean add(final E element)
	{
		if(this.size >= this.limit){
			throw new IndexOutOfBoundsException();
		}
		this.data[this.size++] = element;
		return true;
	}

	@Override
	public final boolean nullAdd()
	{
		if(this.size >= this.limit){
			throw new IndexOutOfBoundsException();
		}
		this.size++; // as overhang array elements are guaranteed to be null, the array setting can be spared
		return true;
	}

	@SafeVarargs
	@Override
	public final LimitList<E> addAll(final E... elements)
	{
		System.arraycopy(elements, 0, this.data, this.size, elements.length);
		this.size += elements.length;
		return this;
	}

	@Override
	public final LimitList<E> addAll(final E[] elements, final int offset, final int length)
	{
		// (03.07.2011)FIXME: fix all add~() methods
		if(length == 0) return this; // skip sanity checks
		else if(length > 0){
			this.ensureFreeCapacity(length); // check capacity
			System.arraycopy(elements, offset, this.data, this.size, length); // automatic bounds checks
			this.size += length;
		}
		else {
			final int bound;
			if((bound = offset + length) < -1){
				throw new ArrayIndexOutOfBoundsException(bound+1);
			}
			this.ensureFreeCapacity(-length); // check capacity
			final Object[] data = this.data;
			int size = this.size;
			for(int i = offset; i > bound; i--){
				data[size++] = elements[i];
			}
			this.size = size;
		}
		return this;
	}

	@Override
	public final LimitList<E> addAll(final XGettingCollection<? extends E> elements)
	{
		return elements.copyTo(this);
	}



	@Override
	public final boolean nullPut()
	{
		return this.nullAdd();
	}

	@Override
	public final void accept(final E element)
	{
		if(this.size >= this.limit){
			throw new IndexOutOfBoundsException();
		}
		this.data[this.size++] = element;
	}

	@Override
	public final boolean put(final E element)
	{
		if(this.size >= this.limit){
			throw new IndexOutOfBoundsException();
		}
		this.data[this.size++] = element;
		return true;
	}

	@SafeVarargs
	@Override
	public final LimitList<E> putAll(final E... elements)
	{
		return this.addAll(elements);
	}

	@Override
	public final LimitList<E> putAll(final E[] elements, final int offset, final int length)
	{
		return this.addAll(elements, offset, length);
	}

	@Override
	public final LimitList<E> putAll(final XGettingCollection<? extends E> elements)
	{
		return elements.copyTo(this);
	}



	///////////////////////////////////////////////////////////////////////////
	//  insert methods  //
	/////////////////////

	@Override
	public final boolean prepend(final E element)
	{
		if(this.size >= this.limit){
			throw new IndexBoundsException(this.limit);
		}
		System.arraycopy(this.data, 0, this.data, 1, this.size); // ignore size == 0 corner case
		this.data[0] = element;
		this.size++;
		return true;
	}

	@Override
	public final boolean insert(final int index, final E element)
	{
		if(this.size >= this.limit){
			throw new IndexBoundsException(this.limit);
		}
		if(index >= this.size || index < 0){
			if(index == this.size){
				this.data[this.size++] = element;
				return true;
			}
			throw new IndexBoundsException(this.size, index);
		}

		System.arraycopy(this.data, index, this.data, index+1, this.size - index);
		this.data[index] = element;
		this.size++;
		return true;
	}

	@SafeVarargs
	@Override
	public final int insertAll(final int index, final E... elements) throws IndexOutOfBoundsException
	{
		if(index >= this.size || index < 0){
			if(index == this.size){
				return this.internalCountingAddAll(elements);
			}
			throw new IndexBoundsException(this.size, index);
		}
		return this.internalInsertArray(index, elements, elements.length);
	}

	@Override
	public final int insertAll(final int index, final E[] elements, final int offset, final int length)
	{
		if(index >= this.size || index < 0){
			if(index == this.size){
				return this.internalCountingAddAll(elements, offset, length);
			}
			throw new IndexBoundsException(this.size, index);
		}
		return this.internalInsertArray(index, elements, offset, length);
	}

	@Override
	public final int insertAll(final int index, final XGettingCollection<? extends E> elements)
	{
		if(index >= this.size ||index < 0){
			if(index == this.size){
				return this.internalCountingAddAll(elements);
			}
			throw new IndexBoundsException(this.size, index);
		}
		final Object[] elementsToAdd = elements instanceof AbstractSimpleArrayCollection<?>
			?AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements)
			:elements.toArray() // anything else is probably not worth the hassle
		;
		return this.internalInsertArray(index, elementsToAdd, elementsToAdd.length);
	}

	@Override
	public final boolean preput(final E element)
	{
		if(this.size >= this.limit){
			throw new IndexBoundsException(this.limit);
		}
		System.arraycopy(this.data, 0, this.data, 1, this.size); // ignore size == 0 corner case
		this.data[0] = element;
		this.size++;
		return true;
	}

	@Override
	public final boolean input(final int index, final E element)
	{
		if(this.size >= this.limit){
			throw new IndexBoundsException(this.limit);
		}
		if(index >= this.size || index < 0){
			if(index == this.size){
				this.data[this.size++] = element;
				return true;
			}
			throw new IndexBoundsException(this.size, index);
		}

		System.arraycopy(this.data, index, this.data, index+1, this.size - index);
		this.data[index] = element;
		this.size++;
		return true;
	}

	@SafeVarargs
	@Override
	public final int inputAll(final int index, final E... elements) throws IndexOutOfBoundsException
	{
		if(index >= this.size || index < 0){
			if(index == this.size){
				return this.internalCountingPutAll(elements);
			}
			throw new IndexBoundsException(this.size, index);
		}
		return this.internalInputArray(index, elements, elements.length);
	}

	@Override
	public final int inputAll(final int index, final E[] elements, final int offset, final int length)
	{
		if(index >= this.size || index < 0){
			if(index == this.size){
				return this.internalCountingPutAll(elements, offset, length);
			}
			throw new IndexBoundsException(this.size, index);
		}
		return this.internalInputArray(index, elements, offset, length);
	}

	@Override
	public final int inputAll(final int index, final XGettingCollection<? extends E> elements)
	{
		if(index >= this.size ||index < 0){
			if(index == this.size){
				return this.internalCountingPutAll(elements);
			}
			throw new IndexBoundsException(this.size, index);
		}
		final Object[] elementsToAdd = elements instanceof AbstractSimpleArrayCollection<?>
			?AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements)
			:elements.toArray() // anything else is probably not worth the hassle
		;
		return this.internalInputArray(index, elementsToAdd, elementsToAdd.length);
	}



	///////////////////////////////////////////////////////////////////////////
	//  remove methods  //
	/////////////////////

	@Override
	public final void truncate()
	{
		final Object[] data = this.data;
		for(int i = this.size; i --> 0;){
			data[i] = null;
		}
		this.size = 0;
	}

	@Override
	public final int consolidate()
	{
		return 0; // nothing to do here
	}

	// removing - single //

	@SuppressWarnings("unchecked")
	@Override
	public final E retrieve(final E element)
	{
		final E removedElement;
		if((removedElement = AbstractArrayStorage.retrieve(this.data, this.size, element, (E)MARKER)) != MARKER){
			this.size--;
			return removedElement;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final E retrieveBy(final Predicate<? super E> predicate)
	{
		final E e;
		if((e = AbstractArrayStorage.retrieve(this.data, this.size, predicate, (E)MARKER)) != MARKER){
			this.size--;
			return e;
		}
		return null;
	}

	@Override
	public final boolean removeOne(final E element)
	{
		if(AbstractArrayStorage.removeOne(this.data, this.size, element)){
			this.size--;
			return true;
		}
		return false;
	}

//	@Override
//	@SuppressWarnings("unchecked")
//	public final boolean removeOne(final E sample, final Equalator<? super E> equalator)
//	{
//		if(AbstractArrayStorage.removeOne((E[])this.data, this.size, sample, equalator)){
//			this.size--;
//			return true;
//		}
//		return false;
//	}

	// removing - multiple //

	@Override
	public final int remove(final E element)
	{
		int removeCount;
		this.size -= removeCount = removeAllFromArray(this.data, 0, this.size, element);
		return removeCount;
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public final int remove(final E sample, final Equalator<? super E> equalator)
//	{
//		final int removeCount;
//		this.size -= removeCount = removeAllFromArray(
//			sample, this.data, 0, this.size, this.data, 0, this.size, (Equalator<Object>)equalator
//		);
//		return removeCount;
//	}

	@Override
	public final int nullRemove()
	{
		final int removeCount;
		this.size -= removeCount = JadothArrays.removeAllFromArray(this.data, 0, this.size, null);
		return removeCount;
	}

	// reducing //

	@SuppressWarnings("unchecked")
	@Override
	public final int removeBy(final Predicate<? super E> predicate)
	{
		final int removeCount;
		this.size -= removeCount = AbstractArrayStorage.reduce(this.data, this.size, predicate, (E)MARKER);
		return removeCount;
	}

	// retaining //

	@SuppressWarnings("unchecked")
	@Override
	public final int retainAll(final XGettingCollection<? extends E> elements)
	{
		final int removeCount;
		this.size -= removeCount = AbstractArrayStorage.retainAll(
			this.data, this.size, (XGettingCollection<E>)elements, (E)MARKER
		);
		return removeCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <P extends Procedure<? super E>> P process(final P procedure)
	{
		this.size -= AbstractArrayStorage.process(this.data, this.size, procedure, (E)MARKER);
		return procedure;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <C extends Procedure<? super E>> C moveTo(final C target, final Predicate<? super E> predicate)
	{
		this.size -= AbstractArrayStorage.moveTo(this.data, this.size, target, predicate, (E)MARKER);
		return target;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final <C extends Procedure<? super E>> C moveSelection(final C target, final int... indices)
	{
		this.size -= AbstractArrayStorage.moveSelection(this.data, this.size, indices, target, (E)MARKER);
		return target;
	}

	// removing - multiple all //

	@Override
	public final int removeAll(final XGettingCollection<? extends E> elements)
	{
		final int removed = removeAllFromArray(elements, this.data, 0, this.size);
		this.size -= removed;
		return removed;
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public final int removeAll(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
//	{
//		final int removed;
//		this.size -= removed = removeAllFromArray(
//			(XGettingCollection<E>)samples, (E[])this.data, 0, this.size, (E[])this.data, 0, this.size, false, equalator
//		);
//		return removed;
//	}

	// removing - duplicates //

	@SuppressWarnings("unchecked")
	@Override
	public final int removeDuplicates(final Equalator<? super E> equalator)
	{
		final int removeCount;
		this.size -= removeCount = AbstractArrayStorage.removeDuplicates(
			this.data, this.size, equalator, (E)MARKER
		);
		return removeCount;
	}

	@Override
	public final int removeDuplicates()
	{
		final int removeCount;
		this.size -= removeCount = AbstractArrayStorage.removeDuplicates(this.data, this.size, MARKER);
		return removeCount;
	}

	// removing - indexed //

	@Override
	public final E fetch()
	{
		final E element = this.data[0];
		System.arraycopy(this.data, 1, this.data, 0, --this.size);
		this.data[this.size] = null;
		return element;
	}

	@Override
	public final E pop()
	{
		final E element = this.data[this.size - 1]; // get element and provoke index exception
		this.data[--this.size] = null; // update state
		return element;
	}

	@Override
	public final E pinch()
	{
		if(this.size == 0){
			return null;
		}
		final E element = this.data[0];
		System.arraycopy(this.data, 1, this.data, 0, --this.size);
		this.data[this.size] = null;
		return element;
	}

	@Override
	public final E pick()
	{
		if(this.size == 0){
			return null;
		}
		final E element = this.data[--this.size];
		this.data[this.size] = null;
		return element;
	}

	@Override
	public final int removeSelection(final int[] indices)
	{
		final int removeCount;
		this.size -= removeCount = AbstractArrayStorage.removeSelection(this.data, this.size, indices, MARKER);
		return removeCount;
	}

	@Override
	public final LimitList<E> removeRange(final int startIndex, final int length)
	{
		this.size -= AbstractArrayStorage.removeRange(this.data, this.size, startIndex, length);
		return this;
	}

	@Override
	public final LimitList<E> retainRange(final int startIndex, final int length)
	{
		AbstractArrayStorage.retainRange(this.data, this.size, startIndex, length);
		this.size = length;
		return this;
	}



	///////////////////////////////////////////////////////////////////////////
	// java.util.list and derivatives  //
	////////////////////////////////////

	@Override
	public final boolean isEmpty()
	{
		return this.size == 0;
	}

	@Override
	public final Iterator<E> iterator()
	{
		return new GenericListIterator<>(this);
	}

	@Override
	public final ListIterator<E> listIterator()
	{
		return new GenericListIterator<>(this);
	}

	@Override
	public final ListIterator<E> listIterator(final int index)
	{
		return new GenericListIterator<>(this, index);
	}

	@Override
	public final boolean set(final int index, final E element) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		if(index >= this.size){
			throw new IndexBoundsException(this.size, index);
		}
		this.data[index] = element;
		return false;
	}

	@Override
	public final E setGet(final int index, final E element) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		if(index >= this.size){
			throw new IndexBoundsException(this.size, index);
		}
		final E old = this.data[index];
		this.data[index] = element;
		return old;
	}

	@Override
	public final long size()
	{
		return this.size;
	}

	@Override
	public final SubList<E> range(final int fromIndex, final int toIndex)
	{
		// range check is done in constructor
		return new SubList<>(this, fromIndex, toIndex);
	}

	@Override
	public final String toString()
	{
		return AbstractArrayStorage.toString(this.data, this.size);
	}

	@Override
	public final Object[] toArray()
	{
		final Object[] array = new Object[this.size];
		System.arraycopy(this.data, 0, array, 0, this.size);
		return array;
	}

	@Override
	public final E at(final int index) throws ArrayIndexOutOfBoundsException
	{
		if(index >= this.size){
			throw new IndexBoundsException(this.size, index);
		}
		return this.data[index];
	}

	@Override
	public final void clear()
	{
		final Object[] data = this.data;
		for(int i = this.size; i --> 0;){
			data[i] = null;
		}
		this.size = 0;
	}

	@Override
	public final E removeAt(final int index) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		if(index >= this.size){
			throw new IndexBoundsException(this.size, index);
		}
		final E oldValue = this.data[index];

		final int moveCount;
		if((moveCount = this.size-1 - index) > 0){
			System.arraycopy(this.data, index+1, this.data, index, moveCount);
		}
		this.data[--this.size] = null;

		return oldValue;
	}

	@Deprecated
	@Override
	public final boolean equals(final Object o)
	{
		//trivial escape conditions
		if(o == this){
			return true;
		}
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
	public final int hashCode()
	{
		return JadothArrays.arrayHashCode(this.data, this.size);
	}



	@Override
	public final OldLimitList<E> old()
	{
		return new OldLimitList<>(this);
	}

	public static final class OldLimitList<E> extends BridgeXList<E>
	{
		OldLimitList(final LimitList<E> list)
		{
			super(list);
		}

		@Override
		public final LimitList<E> parent()
		{
			return (LimitList<E>)super.parent();
		}

	}


	// (03.07.2011)FIXME: LimitList pre~() stuff

	@Override
	public final boolean nullInput(final int index)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

	@Override
	public final boolean nullInsert(final int index)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

	@Override
	public final boolean nullPrepend()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

	@SafeVarargs
	@Override
	public final LimitList<E> prependAll(final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

	@Override
	public final LimitList<E> prependAll(final E[] elements, final int srcStartIndex, final int srcLength)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

	@Override
	public final LimitList<E> prependAll(final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

	@Override
	public final boolean nullPreput()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

	@SafeVarargs
	@Override
	public final LimitList<E> preputAll(final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

	@Override
	public final LimitList<E> preputAll(final E[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

	@Override
	public final LimitList<E> preputAll(final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

}
