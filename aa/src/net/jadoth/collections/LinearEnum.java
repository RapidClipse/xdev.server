package net.jadoth.collections;

import static net.jadoth.collections.JadothArrays.removeAllFromArray;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.old.BridgeXSet;
import net.jadoth.collections.types.IdentityEqualityLogic;
import net.jadoth.collections.types.XEnum;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingEnum;
import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.exceptions.ArrayCapacityException;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.IndexProcedure;
import net.jadoth.functional.JadothEqualators;
import net.jadoth.functional.Procedure;
import net.jadoth.math.JadothMath;
import net.jadoth.util.Equalator;
import net.jadoth.util.iterables.ReadOnlyListIterator;


@Deprecated // (31.05.2013)NOTE: not finished yet, just simple copy from BulkList
public final class LinearEnum<E>extends AbstractSimpleArrayCollection<E> implements XEnum<E>, IdentityEqualityLogic
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

	private E[] data; // the storage array containing the elements
	private int size; // the current element count (logical size)



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	/**
	 * Default constructor instantiating an empty instance with default (minimum) capacity.
	 */
	public LinearEnum()
	{
		super();
		this.size = 0;
		this.data = newArray(1);
	}

	/**
	 * Initial capacity constructor instantiating an empty instance with a given initial capacity.
	 * <p>
	 * The actual initial capacity is the highest of the following three values:
	 * <ul>
	 * <li>{@link Integer.MAX_VALUE}, if the given initial capacity is greater than 2^30.</li>
	 * <li>The lowest power of two value that is equal to or greater than the given initial capacity.</li>
	 * <li>The default (minimum) capacity.</li>
	 * </ul>
	 *
	 * @param initialCapacity the desired custom initial capacity.
	 */
	public LinearEnum(final int initialCapacity)
	{
		super();
		this.size = 0;
		this.data = newArray(JadothMath.pow2BoundMaxed(initialCapacity));
	}

	/**
	 * Copy constructor that instantiates a new instance with a copy of the passed original instance's data and same
	 * size.
	 *
	 * @param original the instance to be copied.
	 * @throws NullPointerException if {@code null} was passed.
	 *
	 * @see #copy()
	 */
	public LinearEnum(final LinearEnum<? extends E> original) throws NullPointerException
	{
		super();
		this.size = original.size;
		this.data = original.data.clone();
	}

	/**
	 * Convenience initial data constructor, instantiating a new instance containing all elements of the passed
	 * array. The element size of the new instance will be equal to the passed array's length.
	 * <p>
	 * Note that providing no element at all in the VarArgs parameter will automatically cause the
	 * default constructor {@link #LinearEnum()} to be used instead. Explicitely providing an {@code null} array
	 * reference will cause a {@link NullPointerException}.
	 *
	 * @param elements the initial elements for the new instance.
	 * @throws NullPointerException if an explicit {@code null} array reference was passed.
	 *
	 * @see #LinearEnum()
	 */
	@SafeVarargs
	public LinearEnum(final E... elements) throws NullPointerException
	{
		super();
		System.arraycopy(
			elements,
			0,
			this.data = newArray(JadothMath.pow2BoundMaxed(this.size = elements.length)),
			0,
			this.size
		);
	}

	/**
	 * Detailed initializing constructor allowing to specify initial capacity and a custom array range of initial data.
	 * <p>
	 * The actual initial capacity will be calculated based on the higher of the two values {@code initialCapacity}
	 * and {@code srcLength} as described in {@link #LinearEnum(int)}.
	 * <p>
	 * The specified initial elements array range is copied via {@link System#arraycopy(Object, int, Object, int, int)}.
	 *
	 * @param initialCapacity the desired initial capacity for the new instance.
	 * @param src the source array containg the desired range of initial elements.
	 * @param srcStart the start index of the desired range of initial elements in the source array.
	 * @param srcLength the length of the desired range of initial elements in the source array.
	 */
	public LinearEnum(final int initialCapacity, final E[] src, final int srcStart, final int srcLength)
	{
		super();
		System.arraycopy(
			src,
			srcStart,
			this.data = newArray(JadothMath.pow2BoundMaxed(initialCapacity >= srcLength ?initialCapacity :srcLength)),
			0,
			this.size = srcLength
		);
	}

	/**
	 * Internal constructor to directly supply the storage array instance and size.
	 * <p>
	 * The passed storage array must comply to the power of two aligned size rules as specified in
	 * {@link #LinearEnum(int)} and the size must be consistent to the storage array.<br>
	 * Calling this constructor without complying to these rules will result in a broken instance.
	 * <p>
	 * It is recommended to NOT use this constructor outside collections-framework-internal implementations.
	 *
	 * @param storageArray the array to be used as the storage for the new instance.
	 * @param size the element size of the new instance.
	 */
	LinearEnum(final E[] storageArray, final int size)
	{
		super();
		this.size = size;
		this.data = storageArray;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	private void checkSizeIncreasable()
	{
		if(this.size >= Integer.MAX_VALUE){
			throw new IndexOutOfBoundsException();
		}
	}

	/* this method is highly optimized for performance, yielding up to around 300% the speed of
	 * java.util.ArrayList.add() when adding elements to an already big enough storage.
	 * Moving the storage increase part to a private increaseStorage() would make it faster when
	 * regular increasing is needed, but puzzlingly then the alreay-big-enough performance
	 * advantage drops to around 110% faster instead of 300% faster (even though the single not called
	 * increase method should be removed by HotSpot compiling. Seems there is a bug or at least
	 * some heavy confusion going on there.
	 * As a consequence, storage increasing has NOT been moved to a private method, thus maintaining
	 * the huge alreay-big-enough performance advantage, but making it slower in regular-growth-cases
	 * (also very strange).
	 * Maybe one of the two HotSpot compiling problems improves in the future, so that both cases
	 * of advanced performance are reachable by optimization.
	 */
	void internalAdd(final E element)
	{
		/* notes on algorithm:
		 * - ">=" is significantly faster than "==", probably due to simple sign bit checking?
		 * - assignment inlining increases normal case performance by >10% ^^
		 * - float conversion is automatically capped at MAX_VALUE, whereas "<<= 1" can only reach 2^30 and then crash
		 * - "<<= 1" would speed up normal case by ~5%, but would limit list size to 2^30 instead of MAX_VALUE
		 * - "++this.lastIndex" would be ~5% faster than "this.size++", but would complicate every use of list's size
		 */
		if(this.size >= this.data.length){
			if(this.size >= Integer.MAX_VALUE){
				throw new CapacityExceededException();
			}
			System.arraycopy(this.data, 0, this.data = newArray((int)(this.data.length * 2.0f)), 0, this.size);
		}
		this.data[this.size++] = element;
	}

	private int internalInputArray(final int index, final Object[] elements, final int elementsSize)
	{
		// check for simple case without a required capacity increase
		if(this.data.length - this.size >= elementsSize){
			// simply free up enough space at index and slide in new elements
			System.arraycopy(this.data, index, this.data, index + elementsSize, elementsSize);
			System.arraycopy(elements ,     0, this.data, index               , elementsSize);
			this.size += elementsSize;
			return elementsSize;
		}

		// overflow-safe check for unreachable capacity
		if(Integer.MAX_VALUE - this.size < elementsSize){ // unreachable capacity
			throw new ArrayCapacityException((long)elementsSize + this.size);
		}

		// required and reachable capacity increase
		final int newSize = this.size + elementsSize;
		int newCapacity;
		if(newSize > 1<<30){ // magic number, but also technical limit that will and can not change
			newCapacity = Integer.MAX_VALUE;
		}
		else {
			newCapacity = this.data.length;
			while(newCapacity < newSize){
				newCapacity <<= 1;
			}
		}

		/* copy elements in two steps:
		 *        old array             new array
		 * 1.) [    0; index] -> [        0;    index]
		 * 2.) [index;  size] -> [index+gap; size+gap]
		 *
		 * So it looks like this:
		 * ----1.)----       ----2.)----
		 * |||||||||||_______|||||||||||
		 * where this ^^^^^^^ is exactely enough space (the gap) for inserting "elements"
		 *
		 * this way, all elements are only copied once
		 */
		final E[] data;
		System.arraycopy(this.data,     0, data = newArray(newCapacity), 0, index);
		System.arraycopy(this.data, index, data, index + elementsSize, elementsSize);
		System.arraycopy(elements ,     0, this.data = data,    index, elementsSize);
		this.size = newSize;
		return elementsSize;
	}

	private int internalInputArray(final int index, final E[] elements, final int offset, final int length)
	{
		if(length < 0){
			return this.internalReverseInputArray(index, elements, offset, -length);
		}

		// check for simple case without a required capacity increase
		if(this.data.length - this.size >= length){
			// simply free up enough space at index and slide in new elements
			System.arraycopy(this.data, index, this.data, index + length, length);
			System.arraycopy(elements, offset, this.data, index         , length);
			this.size += length;
			return length;
		}

		// overflow-safe check for unreachable capacity
		if(Integer.MAX_VALUE - this.size < length){ // unreachable capacity
			throw new ArrayCapacityException((long)length + this.size);
		}

		// required and reachable capacity increase
		final int newSize = this.size + length;
		int newCapacity;
		if(newSize > 1<<30){ // magic number, but also technical limit that will and can not change
			newCapacity = Integer.MAX_VALUE;
		}
		else {
			newCapacity = this.data.length;
			while(newCapacity < newSize){
				newCapacity <<= 1;
			}
		}

		/* copy elements in two steps:
		 *        old array             new array
		 * 1.) [    0; index] -> [        0;    index]
		 * 2.) [index;  size] -> [index+gap; size+gap]
		 *
		 * So it looks like this:
		 * ----1.)----       ----2.)----
		 * |||||||||||_______|||||||||||
		 * where this ^^^^^^^ is exactely enough space (the gap) for inserting "elements"
		 *
		 * this way, all elements are only copied once
		 */
		final E[] data;
		System.arraycopy(this.data,     0, data = newArray(newCapacity), 0, index);
		System.arraycopy(this.data, index, data, index + length, length);
		System.arraycopy(elements, offset, this.data = data,    index, length);
		this.size = newSize;
		return length;
	}

	private int internalReverseInputArray(final int index, final E[] elements, final int offset, final int length)
	{
		// check for simple case without a required capacity increase
		if(this.data.length - this.size >= length){
			// simply free up enough space at index and slide in new elements
			System.arraycopy(this.data, index, this.data, index + length, length);
			JadothArrays.reverseArraycopy(elements, offset, this.data, index, length);
			this.size += length;
			return length;
		}

		// overflow-safe check for unreachable capacity
		if(Integer.MAX_VALUE - this.size < length){ // unreachable capacity
			throw new ArrayCapacityException((long)length + this.size);
		}

		// required and reachable capacity increase
		final int newSize = this.size + length;
		int newCapacity;
		if(newSize > 1<<30){ // magic number, but also technical limit that will and can not change
			newCapacity = Integer.MAX_VALUE;
		}
		else {
			newCapacity = this.data.length;
			while(newCapacity < newSize){
				newCapacity <<= 1;
			}
		}

		/* copy elements in two steps:
		 *        old array             new array
		 * 1.) [    0; index] -> [        0;    index]
		 * 2.) [index;  size] -> [index+gap; size+gap]
		 *
		 * So it looks like this:
		 * ----1.)----       ----2.)----
		 * |||||||||||_______|||||||||||
		 * where this ^^^^^^^ is exactely enough space (the gap) for inserting "elements"
		 *
		 * this way, all elements are only copied once
		 */
		final E[] data;
		System.arraycopy(this.data,     0, data = newArray(newCapacity), 0, index);
		System.arraycopy(this.data, index, data, index + length, length);
		JadothArrays.reverseArraycopy(elements, 0, this.data, index, -length);
		this.size = newSize;
		return length;
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

	@Override
	protected int internalCountingAddAll(final E[] elements) throws UnsupportedOperationException
	{
		this.ensureFreeCapacity(elements.length); // increaseCapacity
		System.arraycopy(elements, 0, this.data, this.size, elements.length);
		this.size += elements.length;
		return elements.length;
	}

	@Override
	protected int internalCountingAddAll(final E[] elements, final int offset, final int length) throws UnsupportedOperationException
	{
		if(length >= 0){
			this.ensureFreeCapacity(length); // increaseCapacity
			System.arraycopy(elements, offset, this.data, this.size, length); // automatic bounds checks
			this.size += length;
			return length;
		}

		final int bound;
		if((bound = offset + length) < -1){
			throw new ArrayIndexOutOfBoundsException(bound+1);
		}
		this.ensureFreeCapacity(-length); // increaseCapacity
		final Object[] data = this.data;
		int size = this.size;
		for(int i = offset; i > bound; i--){
			data[size++] = elements[i];
		}
		this.size = size;
		return -length;
	}

	@Override
	protected int internalCountingAddAll(final XGettingCollection<? extends E> elements) throws UnsupportedOperationException
	{
		if(elements instanceof AbstractSimpleArrayCollection){
			return this.internalCountingAddAll(AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements), 0, Jadoth.to_int(elements.size()));
		}
		final int oldSize = this.size;
		elements.copyTo(this);
		return this.size - oldSize;
	}

	@Override
	protected int internalCountingPutAll(final E[] elements) throws UnsupportedOperationException
	{
		this.ensureFreeCapacity(elements.length); // increaseCapacity
		System.arraycopy(elements, 0, this.data, this.size, elements.length);
		this.size += elements.length;
		return elements.length;
	}

	@Override
	protected int internalCountingPutAll(final E[] elements, final int offset, final int length) throws UnsupportedOperationException
	{
		if(length >= 0){
			this.ensureFreeCapacity(length); // increaseCapacity
			System.arraycopy(elements, offset, this.data, this.size, length); // automatic bounds checks
			this.size += length;
			return length;
		}

		final int bound;
		if((bound = offset + length) < -1){
			throw new ArrayIndexOutOfBoundsException(bound+1);
		}
		this.ensureFreeCapacity(-length); // increaseCapacity
		final Object[] data = this.data;
		int size = this.size;
		for(int i = offset; i > bound; i--){
			data[size++] = elements[i];
		}
		this.size = size;
		return -length;
	}

	@Override
	protected int internalCountingPutAll(final XGettingCollection<? extends E> elements) throws UnsupportedOperationException
	{
		if(elements instanceof AbstractSimpleArrayCollection){
			return this.internalCountingAddAll(AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements), 0, Jadoth.to_int(elements.size()));
		}

		final int oldSize = this.size;
		elements.copyTo(this);
		return this.size - oldSize;
	}

	@Override
	public final Equalator<? super E> equality()
	{
		return JadothEqualators.identity();
	}



	///////////////////////////////////////////////////////////////////////////
	// getting methods  //
	/////////////////////

	@Override
	public final LinearEnum<E> copy()
	{
		return new LinearEnum<>(this);
	}

	@Override
	public final ConstLinearEnum<E> immure()
	{
		return new ConstLinearEnum<>(this);
	}

	@Override
	public final LinearEnum<E> toReversed()
	{
		final E[] data, reversedData = newArray((data = this.data).length);
		for(int i = this.size, r = 0; i --> 0;){
			reversedData[r++] = data[i];
		}
		return new LinearEnum<>(reversedData, this.size);
	}

	@Override
	public final E[] toArray(final Class<E> type)
	{
		final E[] array;
		System.arraycopy(this.data, 0, array = JadothArrays.newArray(type, this.size), 0, this.size);
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
		return this.data[this.size - 1];
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

	@Override
	public final E search(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.queryElement(this.data, this.size, predicate, null);
	}

	@Override
	public final E seek(final E sample)
	{
		return AbstractArrayStorage.containsSame(this.data, this.size, sample) ?sample :null;
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

//	@Override
//	public final boolean containsAll(final XGettingCollection<? extends E> elements, final Equalator<? super E> equalator)
//	{
//		return AbstractArrayStorage.containsAll(this.data, this.size, elements, equalator);
//	}

	// boolean querying - equality //

	@Override
	public final boolean equals(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		if(samples == null || !(samples instanceof LinearEnum<?>) || Jadoth.to_int(samples.size()) != this.size){
			return false;
		}
		if(samples == this){
			return true;
		}

		// equivalent to equalsContent()
		return JadothArrays.equals(this.data, 0, ((LinearEnum<? extends E>)samples).data, 0, this.size, equalator);
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
		if(target == this){
			return target; // copying a set logic collection to itself would be a no-op, so spare the effort
		}
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
	public final XGettingEnum<E> view()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME LinearEnum#view()
//		return new ListView<>(this);
	}

	@Override
	public final XGettingEnum<E> view(final int fromIndex, final int toIndex)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME LinearEnum#view()
//		return new SubListView<>(this, fromIndex, toIndex); // range check is done in constructor
	}

	@Override
	public final LinearEnum<E> shiftTo(final int sourceIndex, final int targetIndex)
	{
		if(sourceIndex >= this.size){
			throw new IndexExceededException(this.size, sourceIndex);
		}
		if(targetIndex >= this.size){
			throw new IndexExceededException(this.size, targetIndex);
		}
		if(sourceIndex == targetIndex){
			if(sourceIndex < 0){
				throw new IndexExceededException(this.size, sourceIndex);
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
	public final LinearEnum<E> shiftTo(final int sourceIndex, final int targetIndex, final int length)
	{
		if(sourceIndex + length >= this.size){
			throw new IndexExceededException(this.size, sourceIndex);
		}
		if(targetIndex + length >= this.size){
			throw new IndexExceededException(this.size, targetIndex);
		}
		if(sourceIndex == targetIndex){
			if(sourceIndex < 0){
				throw new IndexExceededException(this.size, sourceIndex);
			}
			return this;
		}

		final E[] shiftlings;
		System.arraycopy(this.data, sourceIndex, shiftlings = newArray(length), 0, length);
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
	public final LinearEnum<E> shiftBy(final int sourceIndex, final int distance)
	{
		return this.shiftTo(sourceIndex, sourceIndex + distance);
	}

	@Override
	public final LinearEnum<E> shiftBy(final int sourceIndex, final int distance, final int length)
	{
		return this.shiftTo(sourceIndex, sourceIndex + distance, length);
	}

	@Override
	public final LinearEnum<E> swap(final int indexA, final int indexB) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		if(indexA >= this.size){
			throw new IndexExceededException(this.size, indexA);
		}
		if(indexB >= this.size){
			throw new IndexExceededException(this.size, indexB);
		}
		final E t = this.data[indexA];
		this.data[indexA] = this.data[indexB];
		this.data[indexB] = t;
		return this;
	}

	@Override
	public final LinearEnum<E> swap(final int indexA, final int indexB, final int length)
	{
		AbstractArrayStorage.swap(this.data, this.size, indexA, indexB, length);
		return this;
	}

	@Override
	public final LinearEnum<E> reverse()
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
	public final LinearEnum<E> setAll(final int offset, final E... elements)
	{
		if(offset < 0 || offset + elements.length > this.size){
			throw new IndexOutOfBoundsException(exceptionStringRange(this.size, offset, offset + elements.length-1));
		}
		System.arraycopy(elements, 0, this.data, offset, elements.length);
		return this;
	}

	@Override
	public final LinearEnum<E> set(final int offset, final E[] src, final int srcIndex, final int srcLength)
	{
		AbstractArrayStorage.set(this.data, this.size, offset, src, srcIndex, srcLength);
		return this;
	}

	@Override
	public final LinearEnum<E> set(final int offset, final XGettingSequence<? extends E> elements, final int elementsOffset, final int elementsLength)
	{
		AbstractArrayStorage.set(this.data, this.size, offset, elements, elementsOffset, elementsLength);
		return this;
	}

	// sorting //

	@Override
	public final LinearEnum<E> sort(final Comparator<? super E> comparator)
	{
		JadothSort.mergesort(this.data, 0, this.size, comparator);
		return this;
	}



	///////////////////////////////////////////////////////////////////////////
	// capacity methods //
	/////////////////////

	@Override
	public final long currentCapacity()
	{
		return this.data.length;
	}

	@Override
	public final long maximumCapacity()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public final boolean isFull()
	{
		return this.size >= Integer.MAX_VALUE;
	}

	@Override
	public final long remainingCapacity()
	{
		return Integer.MAX_VALUE - this.size;
	}

	@Override
	public final int optimize()
	{
		final int requiredCapacity;
		if((requiredCapacity = JadothMath.pow2BoundMaxed(this.size)) != this.data.length){
			System.arraycopy(this.data, 0, this.data = newArray(requiredCapacity), 0, this.size);
		}
		return this.data.length;
	}

	@Override
	public final LinearEnum<E> ensureFreeCapacity(final long requiredFreeCapacity)
	{
		// as opposed to ensureCapacity(size + requiredFreeCapacity), this subtraction is overflow-safe
		if(this.data.length - this.size >= requiredFreeCapacity){
			return this; // already enough free capacity
		}

		// calculate new capacity
		final int newSize = Jadoth.to_int(this.size + requiredFreeCapacity);
		int newCapacity;
		if(newSize > 1<<30){ // magic number, but also technical limit that will and can not change
			newCapacity = Integer.MAX_VALUE;
		}
		else {
			newCapacity = this.data.length;
			while(newCapacity < newSize){
				newCapacity <<= 1;
			}
		}

		// rebuild storage
		final E[] data = newArray(newCapacity);
		System.arraycopy(this.data, 0, data, 0, this.size);
		this.data = data;
		return this;
	}

	@Override
	public final LinearEnum<E> ensureCapacity(final long minCapacity)
	{
		if(minCapacity > this.data.length){
			this.data = newArray(pow2BoundMaxed(minCapacity), this.data, this.size);
		}
		return this;
	}



	///////////////////////////////////////////////////////////////////////////
	//   add methods    //
	/////////////////////

	@Override
	public final void accept(final E element)
	{
		final E[] data = this.data;
		final int size = this.size;
		for(int i = 0; i < size; i++) {
			if(data[i] == element){
				return;
			}
		}
		this.internalAdd(element); // gets inlined, tests showed no performance difference.
	}

	@Override
	public final boolean add(final E element)
	{
		final E[] data = this.data;
		final int size = this.size;
		for(int i = 0; i < size; i++) {
			if(data[i] == element){
				return false;
			}
		}
		this.internalAdd(element); // gets inlined, tests showed no performance difference.
		return true;
	}

	@SafeVarargs
	@Override
	public final LinearEnum<E> addAll(final E... elements)
	{
		final E[] data = this.data;
		final int size = this.size;
		for(int i = 0; i < elements.length; i++) {
			final E e = elements[i];
			for(int j = 0; j < size; j++) {
				if(data[j] == e){
					break;
				}
			}
			this.internalAdd(e);
		}
		return this;
	}

	@Override
	public final LinearEnum<E> addAll(final E[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME LinearEnum#addAll()
	}

	@Override
	public final LinearEnum<E> addAll(final XGettingCollection<? extends E> elements)
	{
		if(elements instanceof AbstractSimpleArrayCollection){
			return this.addAll(
				AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements),
				0,
				Jadoth.to_int(elements.size())
			);
		}
		return elements.copyTo(this);
	}

	@Override
	public final boolean nullAdd()
	{
		final E[] data = this.data;
		final int size = this.size;
		for(int i = 0; i < size; i++) {
			if(data[i] == null){
				return false;
			}
		}
		if(this.size >= this.data.length){
			if(this.size >= Integer.MAX_VALUE) throw new CapacityExceededException();
			System.arraycopy(this.data, 0, this.data = newArray((int)(this.data.length * 2.0f)), 0, this.size);
		}
		this.size++; // as overhang array elements are guaranteed to be null, the array setting can be spared
		return true;
	}



	///////////////////////////////////////////////////////////////////////////
	//   put methods    //
	/////////////////////

	@Override
	public final boolean nullPut()
	{
		return this.nullAdd();
	}

	@Override
	public final boolean put(final E element)
	{
		final E[] data = this.data;
		final int size = this.size;
		for(int i = 0; i < size; i++) {
			if(data[i] == element){
				return false;
			}
		}
		this.internalAdd(element); // gets inlined, tests showed no performance difference.
		return true;
	}

	@SafeVarargs
	@Override
	public final LinearEnum<E> putAll(final E... elements)
	{
		return this.addAll(elements);
	}

	@Override
	public final LinearEnum<E> putAll(final E[] elements, final int offset, final int length)
	{
		return this.addAll(elements, offset, length);
	}

	@Override
	public final LinearEnum<E> putAll(final XGettingCollection<? extends E> elements)
	{
		return elements.copyTo(this);
	}



	///////////////////////////////////////////////////////////////////////////
	// prepend methods //
	////////////////////

	@Override
	public final boolean prepend(final E element)
	{
		if(this.size >= this.data.length){
			if(this.size >= Integer.MAX_VALUE){
				throw new CapacityExceededException();
			}
			System.arraycopy(this.data, 0, this.data = newArray((int)(this.data.length * 2.0f)), 1, this.size);
		}
		else {
			System.arraycopy(this.data, 0, this.data, 1, this.size); // ignore size == 0 corner case
		}
		this.data[0] = element;
		this.size++;
		return true;
	}

	@SafeVarargs
	@Override
	public final LinearEnum<E> prependAll(final E... elements)
	{
		this.internalInputArray(0, elements, elements.length);
		return this;
	}

	@Override
	public final LinearEnum<E> prependAll(final E[] elements, final int offset, final int length)
	{
		this.internalInputArray(0, elements, offset, length);
		return this;
	}

	@Override
	public final LinearEnum<E> prependAll(final XGettingCollection<? extends E> elements)
	{
		this.insertAll(0, elements);
		return this;
	}

	@Override
	public final boolean nullPrepend()
	{
		if(this.size >= this.data.length){
			if(this.size >= Integer.MAX_VALUE) throw new CapacityExceededException();
			System.arraycopy(this.data, 0, this.data = newArray((int)(this.data.length * 2.0f)), 0, this.size);
		}
		else {
			System.arraycopy(this.data, 0, this.data, 1, this.size); // ignore size == 0 corner case
		}
		this.data[0] = null;
		this.size++;
		return true;
	}



	///////////////////////////////////////////////////////////////////////////
	// preput methods  //
	////////////////////

	@Override
	public final boolean preput(final E element)
	{
		if(this.size >= this.data.length){
			if(this.size >= Integer.MAX_VALUE) throw new CapacityExceededException();
			System.arraycopy(this.data, 0, this.data = newArray((int)(this.data.length * 2.0f)), 0, this.size);
		}
		else {
			System.arraycopy(this.data, 0, this.data, 1, this.size); // ignore size == 0 corner case
		}
		this.data[0] = element;
		this.size++;
		return true;
	}

	@SafeVarargs
	@Override
	public final LinearEnum<E> preputAll(final E... elements)
	{
		this.internalInputArray(0, elements, elements.length);
		return this;
	}

	@Override
	public final LinearEnum<E> preputAll(final E[] elements, final int offset, final int length)
	{
		this.internalInputArray(0, elements, offset, length);
		return this;
	}

	@Override
	public final LinearEnum<E> preputAll(final XGettingCollection<? extends E> elements)
	{
		this.inputAll(0, elements);
		return this;
	}

	@Override
	public final boolean nullPreput()
	{
		if(this.size >= this.data.length){
			if(this.size >= Integer.MAX_VALUE) throw new CapacityExceededException();
			System.arraycopy(this.data, 0, this.data = newArray((int)(this.data.length * 2.0f)), 0, this.size);
		}
		else {
			System.arraycopy(this.data, 0, this.data, 1, this.size); // ignore size == 0 corner case
		}
		this.data[0] = null;
		this.size++;
		return true;
	}



	///////////////////////////////////////////////////////////////////////////
	//  insert methods  //
	/////////////////////

	@Override
	public final boolean insert(final int index, final E element)
	{
		this.checkSizeIncreasable();
		if(index >= this.size || index < 0){
			if(index == this.size){
				if(this.size >= this.data.length){
					this.checkSizeIncreasable();
					System.arraycopy(this.data, 0, this.data = newArray((int)(this.data.length * 2.0f)), 0, this.size);
				}
				this.data[this.size++] = element;
				return true;
			}
			throw new IndexExceededException(this.size, index);
		}

		if(this.size >= this.data.length){
			this.checkSizeIncreasable();
			final Object[] oldData = this.data;
			System.arraycopy(this.data, 0, this.data = newArray((int)(this.data.length * 2.0f)), 0, index);
			System.arraycopy(oldData, index, this.data, index+1, this.size - index);
		}
		else {
			System.arraycopy(this.data, index, this.data, index+1, this.size - index);
		}
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
			throw new IndexExceededException(this.size, index);
		}
		return this.internalInputArray(index, elements, elements.length);
	}

	@Override
	public final int insertAll(final int index, final E[] elements, final int offset, final int length)
	{
		if(index >= this.size || index < 0){
			if(index == this.size){
				return this.internalCountingAddAll(elements, offset, length);
			}
			throw new IndexExceededException(this.size, index);
		}
		return this.internalInputArray(index, elements, offset, length);
	}

	@Override
	public final int insertAll(final int index, final XGettingCollection<? extends E> elements)
	{
		if(index >= this.size || index < 0){
			if(index == this.size){
				return this.internalCountingAddAll(elements);
			}
			throw new IndexExceededException(this.size, index);
		}
		@SuppressWarnings("unchecked")
		final Object[] elementsToAdd = elements instanceof AbstractSimpleArrayCollection
			?((AbstractSimpleArrayCollection<? extends E>)elements).internalGetStorageArray()
			:elements.toArray() // anything else is probably not worth the hassle
		;
		return this.internalInputArray(index, elementsToAdd, elementsToAdd.length);
	}

	@Override
	public final boolean nullInsert(final int index)
	{
		return this.insert(0, (E)null);
	}



	///////////////////////////////////////////////////////////////////////////
	//  input methods   //
	/////////////////////

	@Override
	public final boolean input(final int index, final E element)
	{
		if(this.size >= Integer.MAX_VALUE){
			throw new ArrayCapacityException();
		}
		if(index >= this.size || index < 0){
			if(index == this.size){
				if(this.size >= this.data.length){
					this.checkSizeIncreasable();
					System.arraycopy(this.data, 0, this.data = newArray((int)(this.data.length * 2.0f)), 0, this.size);
				}
				this.data[this.size++] = element;
				return true;
			}
			throw new IndexExceededException(this.size, index);
		}

		if(this.size >= this.data.length){
			this.checkSizeIncreasable();
			final E[] oldData = this.data;
			System.arraycopy(this.data, 0, this.data = newArray((int)(this.data.length * 2.0f)), 0, index);
			System.arraycopy(oldData, index, this.data, index+1, this.size - index);
		}
		else {
			System.arraycopy(this.data, index, this.data, index+1, this.size - index);
		}
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
			throw new IndexExceededException(this.size, index);
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
			throw new IndexExceededException(this.size, index);
		}
		return this.internalInputArray(index, elements, offset, length);
	}

	@Override
	public final int inputAll(final int index, final XGettingCollection<? extends E> elements)
	{
		if(index >= this.size || index < 0){
			if(index == this.size){
				return this.internalCountingPutAll(elements);
			}
			throw new IndexExceededException(this.size, index);
		}
		final Object[] elementsToAdd = elements instanceof AbstractSimpleArrayCollection
			?((AbstractSimpleArrayCollection<?>)elements).internalGetStorageArray()
			:elements.toArray() // anything else is probably not worth the hassle
		;
		return this.internalInputArray(index, elementsToAdd, elementsToAdd.length);
	}

	@Override
	public final boolean nullInput(final int index)
	{
		return this.input(0, (E)null);
	}



	///////////////////////////////////////////////////////////////////////////
	//  remove methods  //
	/////////////////////

	@Override
	public final void truncate()
	{
		this.size = 0;
		this.data = newArray(1);
	}

	@Override
	public final int consolidate()
	{
		return 0; // nothing to do here
	}

	// removing - single //

	@Override
	public final boolean removeOne(final E element)
	{
		if(AbstractArrayStorage.removeOne(this.data, this.size, element)){
			this.size--;
			return true;
		}
		return false;
	}

	@Override
	public final E retrieve(final E element)
	{
		final E removedElement;
		if((removedElement = AbstractArrayStorage.retrieve(this.data, this.size, element, AbstractArrayCollection.<E>marker())) != AbstractArrayCollection.<E>marker()){
			this.size--;
			return removedElement;
		}
		return null;
	}

	@Override
	public final E retrieveBy(final Predicate<? super E> predicate)
	{
		final E e;
		if((e = AbstractArrayStorage.retrieve(this.data, this.size, predicate, AbstractArrayCollection.<E>marker())) != AbstractArrayCollection.<E>marker()){
			this.size--;
			return e;
		}
		return null;
	}

	// removing - multiple //

	@Override
	public final int remove(final E element)
	{
		int removeCount;
		this.size -= removeCount = removeAllFromArray(this.data, 0, this.size, element);
		return removeCount;
	}

	@Override
	public final int nullRemove()
	{
		final int removeCount;
		this.size -= removeCount = JadothArrays.removeAllFromArray(this.data, 0, this.size, null);
		return removeCount;
	}

	@Override
	public final E removeAt(final int index) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		if(index >= this.size){
			throw new IndexExceededException(this.size, index);
		}
		final E oldValue = this.data[index];

		final int moveCount;
		if((moveCount = this.size-1 - index) > 0){
			System.arraycopy(this.data, index+1, this.data, index, moveCount);
		}
		this.data[--this.size] = null;

		return oldValue;
	}

	// reducing //

	@Override
	public final int removeBy(final Predicate<? super E> predicate)
	{
		final int removeCount;
		this.size -= removeCount = AbstractArrayStorage.reduce(this.data, this.size, predicate, AbstractArrayCollection.<E>marker());
		return removeCount;
	}

	// retaining //

	@SuppressWarnings("unchecked")
	@Override
	public final int retainAll(final XGettingCollection<? extends E> elements)
	{
		final int removeCount;
		this.size -= removeCount = AbstractArrayStorage.retainAll(
			this.data, this.size, (XGettingCollection<E>)elements, AbstractArrayCollection.<E>marker()
		);
		return removeCount;
	}

	// processing //

	@Override
	public final <P extends Procedure<? super E>> P process(final P procedure)
	{
		this.size -= AbstractArrayStorage.process(this.data, this.size, procedure, AbstractArrayCollection.<E>marker());
		return procedure;
	}

	// moving //

	@Override
	public final <C extends Procedure<? super E>> C moveTo(final C target, final Predicate<? super E> predicate)
	{
		this.size -= AbstractArrayStorage.moveTo(this.data, this.size, target, predicate, AbstractArrayCollection.<E>marker());
		return target;
	}

	@Override
	public final <C extends Procedure<? super E>> C moveSelection(final C target, final int... indices)
	{
		this.size -= AbstractArrayStorage.moveSelection(this.data, this.size, indices, target, AbstractArrayCollection.<E>marker());
		return target;
	}

	// removing - multiple all //

	@Override
	public final int removeAll(final XGettingCollection<? extends E> elements)
	{
		final int removed;
		this.size -= removed = removeAllFromArray(elements, this.data, 0, this.size);
		return removed;
	}

	// removing - duplicates //

	@Override
	public final int removeDuplicates(final Equalator<? super E> equalator)
	{
		final int removeCount;
		this.size -= removeCount = AbstractArrayStorage.removeDuplicates(
			this.data, this.size, equalator, AbstractArrayCollection.<E>marker()
		);
		return removeCount;
	}

	@Override
	public final int removeDuplicates()
	{
		final int removeCount;
		this.size -= removeCount = AbstractArrayStorage.removeDuplicates(this.data, this.size, AbstractArrayCollection.<E>marker());
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
		this.size -= removeCount = AbstractArrayStorage.removeSelection(this.data, this.size, indices, AbstractArrayCollection.<E>marker());
		return removeCount;
	}

	@Override
	public final LinearEnum<E> removeRange(final int startIndex, final int length)
	{
		this.size -= AbstractArrayStorage.removeRange(this.data, this.size, startIndex, length);
		return this;
	}

	@Override
	public final LinearEnum<E> retainRange(final int startIndex, final int length)
	{
		AbstractArrayStorage.retainRange(this.data, this.size, startIndex, length);
		this.size = length;
		return this;
	}

	@Override
	public final XEnum<E> range(final int fromIndex, final int toIndex)
	{
		// range check is done in constructor
		throw new net.jadoth.meta.NotImplementedYetError(); // (31.05.2013)FIXME: SubLinearEnum
//		return new SubList<>(this, fromIndex, toIndex);
	}

	@Override
	public final boolean isEmpty()
	{
		return this.size == 0;
	}

	@Override
	public final Iterator<E> iterator()
	{
		return new ReadOnlyListIterator<>(this);
	}

	@Override
	public final E at(final int index) throws ArrayIndexOutOfBoundsException
	{
		if(index >= this.size){
			throw new IndexExceededException(this.size, index);
		}
		return this.data[index];
	}

	@Override
	public final boolean set(final int index, final E element) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		if(index >= this.size){
			throw new IndexExceededException(this.size, index);
		}
		this.data[index] = element;
		return false;
	}

	@Override
	public final E setGet(final int index, final E element) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		if(index >= this.size){
			throw new IndexExceededException(this.size, index);
		}
		final E old = this.data[index];
		this.data[index] = element;
		return old;
	}

	@Override
	public final E putGet(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XPutGetSet<E>#putGet()
	}

	@Override
	public E addGet(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME LinearEnum#addGet()
	}

	@Override
	public E substitute(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME LinearEnum#substitute()
	}

	@Override
	public final long size()
	{
		return this.size;
	}

	@Override
	public final String toString()
	{
		return AbstractArrayStorage.toString(this.data, this.size);
	}

	@Override
	public final Object[] toArray()
	{
		final Object[] array = newArray(this.size);
		System.arraycopy(this.data, 0, array, 0, this.size);
		return array;
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
	public final OldLinearEnum<E> old()
	{
		return new OldLinearEnum<>(this);
	}

	public static final class OldLinearEnum<E> extends BridgeXSet<E>
	{
		OldLinearEnum(final LinearEnum<E> list)
		{
			super(list);
		}

		@Override
		public final LinearEnum<E> parent()
		{
			return (LinearEnum<E>)super.parent();
		}

	}



	public static final class Creator<E> implements XEnum.Creator<E>
	{
		private final int initialCapacity;

		public Creator(final int initialCapacity)
		{
			super();
			this.initialCapacity = JadothMath.pow2BoundMaxed(initialCapacity);
		}

		public final int getInitialCapacity()
		{
			return this.initialCapacity;
		}

		@Override
		public final LinearEnum<E> newInstance()
		{
			return new LinearEnum<>(AbstractArrayCollection.<E>newArray(this.initialCapacity), this.initialCapacity);
		}

	}

}
