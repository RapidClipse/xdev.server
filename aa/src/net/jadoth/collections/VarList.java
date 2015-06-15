package net.jadoth.collections;

import static net.jadoth.Jadoth.checkArrayRange;

import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.Function;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.old.OldList;
import net.jadoth.collections.types.IdentityEqualityLogic;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingList;
import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.collections.types.XList;
import net.jadoth.exceptions.ArrayCapacityException;
import net.jadoth.exceptions.IndexBoundsException;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.IndexProcedure;
import net.jadoth.functional.Procedure;
import net.jadoth.functional.ToArrayAggregator;
import net.jadoth.hash.JadothHash;
import net.jadoth.util.Composition;
import net.jadoth.util.Equalator;
import net.jadoth.util.branching.ThrowBreak;
import net.jadoth.util.chars.VarString;


// (08.04.2014 TMuenz)FIXME: VarList Work in Progress
@SuppressWarnings("all")
public final class VarList<E> implements Composition, XList<E>, IdentityEqualityLogic
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	// anything below thwarts the whole idea of a segmented storage
	static final int MINIMUM_SEGMENT_LENGTH = 8;

	// 256 yields about equal collecting performance as BulkList, with and without explicit initial capacity.
	static final int DEFAULT_SEGMENT_LENGTH = 256;

	// max capacity is max int segments array, each segment with max int array length
	static final long MAXIMUM_CAPACITY = 1L * Integer.MAX_VALUE * Integer.MAX_VALUE;



	///////////////////////////////////////////////////////////////////////////
	// class methods    //
	/////////////////////

	@SuppressWarnings("unchecked")
	protected static final <E> E[] newArray(final int length)
	{
		return (E[])new Object[length];
	}

	@SuppressWarnings("unchecked")
	protected static final <E> E[][] newStorage(final int length)
	{
		return (E[][])new Object[length][];
	}

	@SuppressWarnings("unchecked")
	protected static final <E> E[][] newStorage(final int length, final int segmentLength)
	{
		return (E[][])new Object[length][segmentLength];
	}

	private static int min(final int a, final int b)
	{
		// >= and < are faster than <= and > (probably due to simple sign bit checking). JDK guys should know that.
        return a >= b ?b :a;
    }

	private static int cap_int(final long value)
	{
		return value >= Integer.MAX_VALUE ?Integer.MAX_VALUE :(int)value;
	}

	private static int segmentCount(final int minimumCapacity, final int segmentLength)
	{
		int segmentCount = 1;
		while(segmentCount * segmentLength < minimumCapacity)
		{
			segmentCount++;
		}
		return segmentCount;
	}

	public final static <E> VarList<E> New()
	{
		final E[][] storage = newStorage(1, DEFAULT_SEGMENT_LENGTH);
		return new VarList<>(DEFAULT_SEGMENT_LENGTH, storage, new int[1], 0, storage[0], 0, 0);
	}

	public final static <E> VarList<E> New(final int initialCapacity)
	{
		final int segmentCount = segmentCount(initialCapacity, DEFAULT_SEGMENT_LENGTH);

		final E[][] storage = newStorage(segmentCount, DEFAULT_SEGMENT_LENGTH);
		return new VarList<>(DEFAULT_SEGMENT_LENGTH, storage, new int[segmentCount], 0, storage[0], 0, 0);
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	private final int   segmentLength;
	private       long  size         ;
	private       int   prevCount    ;
	private       int   lastSize     ;
	private       E[][] segments     ;
	private       int[] prevSizes    ;
	private       E[]   lastSegment  ;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	VarList(
		final int   segmentLength,
		final E[][] segments     ,
		final int[] prevSizes    ,
		final int   prevCount    ,
		final E[]   lastSegment  ,
		final int   lastSize     ,
		final int   size
	)
	{
		super();
		this.segmentLength = segmentLength;
		this.size          = size         ;
		this.segments      = segments     ;
		this.prevSizes     = prevSizes    ;
		this.prevCount     = prevCount    ;
		this.lastSegment   = lastSegment  ;
		this.lastSize      = lastSize     ;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	private void increaseStorage()
	{
		this.prevSizes[this.prevCount++] = this.lastSize;
		if(this.prevCount >= this.prevSizes.length){
			this.internalIncreaseSegmentCount(
				min(this.segments.length << 1, this.segments.length + (this.segmentLength >> 1))
			);
		}
		if(this.segments[this.prevCount] == null){
			this.segments[this.prevCount] = newArray(this.segmentLength);
		}
		this.lastSegment = this.segments[this.prevCount];
		this.lastSize = 0;
	}

	private void internalEnsureSegmentCount(final int segmentCount)
	{
		if(this.segments.length >= segmentCount){
			return; // already enough segments
		}
		this.internalIncreaseSegmentCount(segmentCount);
	}

	private void internalIncreaseSegmentCount(final int segmentCount)
	{
//		System.out.println("Segment count: "+segmentCount);

		// must copy all segment buckets in case there are already pre-cached empty segments
		final int segmentsLength = this.segments.length;
		System.arraycopy(this.segments , 0, this.segments  = newStorage(segmentCount), 0, segmentsLength);
		System.arraycopy(this.prevSizes, 0, this.prevSizes = new int[segmentCount]   , 0, this.prevCount);
	}

	final void internalAdd(final E element)
	{
		// bound check has amazingly no performance impact. Maybe due to pipelining or so.
		if(this.size >= Integer.MAX_VALUE){
			throw new CapacityExceededException();
		}
		if(this.lastSize >= this.segmentLength){
			this.increaseStorage();
		}
		this.lastSegment[this.lastSize++] = element;
		this.size++;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public final void accept(final E element)
	{
		this.internalAdd(element);
	}

	@Override
	public final boolean add(final E element)
	{
		this.internalAdd(element);
		return true;
	}

	@Override
	public final boolean nullAdd()
	{
		this.internalAdd(null);
		return true;
	}

	@Override
	public final boolean nullAllowed()
	{
		return true;
	}

	@Override
	public final boolean hasVolatileElements()
	{
		return false;
	}

	@Override
	public final VarList<E> ensureFreeCapacity(final long minimalFreeCapacity)
	{
		// overflow-safe check for unreachable capacity
		if(Integer.MAX_VALUE - this.size < minimalFreeCapacity){
			throw new ArrayCapacityException(minimalFreeCapacity + this.size);
		}

		this.ensureCapacity(checkArrayRange(this.size + minimalFreeCapacity));
		return this;
	}

	@Override
	public final VarList<E> ensureCapacity(final long minimalCapacity)
	{
		if(this.currentCapacity() >= minimalCapacity){
			return this; // already enough capacity
		}
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME VarList2#ensureCapacity()
	}

	@Override
	public final long currentCapacity()
	{
		return (this.prevCount + 1) * this.segmentLength;

		// algorithm below calculates current free (trailing) capacity, not total capacity
//		final E[][] segments = this.segments;
//
//		// account for free buckets in current segment
//		final int currentCapacity = this.segmentLength - this.lastSize;
//
//		// count every existing (non-null) segment after the latest
//		int spareSegmentCount = 0;
//		for(int i = this.prevCount + 1; i < segments.length; i++){
//			if(segments[i] == null){
//				break; // abort on first empty segment slot
//			}
//			spareSegmentCount++;
//		}
//		return currentCapacity + spareSegmentCount * this.segmentLength;
	}

	@Override
	public final long maximumCapacity()
	{
		return MAXIMUM_CAPACITY;
	}

	@Override
	public final long remainingCapacity()
	{
		return MAXIMUM_CAPACITY - this.size();
	}

	@Override
	public final boolean isFull()
	{
		return this.size >= Integer.MAX_VALUE;
	}

	@Override
	public final long size()
	{
		return checkArrayRange(this.size);
	}

	@Override
	public final boolean isEmpty()
	{
		return this.size == 0;
	}

	@Override
	public final int optimize()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME OptimizableCollection#optimize()
	}

	@Override
	public final boolean put(final E element)
	{
		this.internalAdd(element);
		return true;
	}

	@Override
	public final boolean nullPut()
	{
		this.internalAdd(null);
		return true;
	}

	@Override
	public final E get()
	{
		return this.segments[0][0];
	}

	@Override
	public final Iterator<E> iterator()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#iterator()
	}

	@Override
	public final Object[] toArray()
	{
		return this.iterate(new ToArrayAggregator<>(newArray(Jadoth.to_int(this.size())))).yield();
	}

	@Override
	public final E[] toArray(final Class<E> type)
	{
		return this.iterate(new ToArrayAggregator<>(JadothArrays.newArray(type, Jadoth.to_int(this.size())))).yield();
	}

	@Override
	public final Equalator<? super E> equality()
	{
		return JadothHash.hashEqualityIdentity();
	}

	@Override
	public final boolean equals(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#equals()
	}

	@Override
	public final boolean equalsContent(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#equalsContent()
	}

	@Override
	public final boolean nullContained()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#nullContained()
	}

	@Override
	public final boolean containsId(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#containsId()
	}

	@Override
	public final boolean contains(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#contains()
	}

	@Override
	public final boolean containsSearched(final Predicate<? super E> predicate)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#containsSearched()
	}

	@Override
	public final boolean containsAll(final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#containsAll()
	}

	@Override
	public final boolean applies(final Predicate<? super E> predicate)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#applies()
	}

	@Override
	public final int count(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#count()
	}

	@Override
	public final int countBy(final Predicate<? super E> predicate)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#count()
	}

	@Override
	public final boolean hasDistinctValues()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#hasDistinctValues()
	}

	@Override
	public final boolean hasDistinctValues(final Equalator<? super E> equalator)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#hasDistinctValues()
	}

	@Override
	public final E search(final Predicate<? super E> predicate)
	{
		final int index[] = this.prevSizes, indexSize = this.prevCount, currentSize = this.lastSize;
		final E[] segments[] = this.segments, currentSegment = this.lastSegment;

		try{
			for(int i = 0; i < indexSize; i++)
			{
				final int segmentSize = index[i];
				final E[] segment     = segments[i];
				for(int s = 0; s < segmentSize; s++)
				{
					if(predicate.test(segment[s])){
						return segment[s];
					}
				}
			}
			for(int s = 0; s < currentSize; s++)
			{
				if(predicate.test(currentSegment[s])){
					return currentSegment[s];
				}
			}
		}
		catch(final ThrowBreak b)
		{
			// abort and fall through to returning null
		}

		return null;
	}

	@Override
	public final E seek(final E sample)
	{
		return this.search(e -> e == sample);
	}

	@Override
	public final E max(final Comparator<? super E> comparator)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#max()
	}

	@Override
	public final E min(final Comparator<? super E> comparator)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#min()
	}

	@Override
	public final <T extends Procedure<? super E>> T distinct(final T target)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#distinct()
	}

	@Override
	public final <T extends Procedure<? super E>> T distinct(final T target, final Equalator<? super E> equalator)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#distinct()
	}

	@Override
	public final <T extends Procedure<? super E>> T copyTo(final T target)
	{
		this.iterate(e -> target.accept(e));
		return target;
	}

	@Override
	public final <T extends Procedure<? super E>> T filterTo(final T target, final Predicate<? super E> predicate)
	{

		this.iterate(e ->
		{
			if(predicate.test(e)){
				target.accept(e);
			}
		});
		return target;
	}

	@Override
	public final <T> T[] copyTo(final T[] target, final int targetOffset)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#copyTo()
	}

	@Override
	public final <T extends Procedure<? super E>> T union(final XGettingCollection<? extends E> other, final Equalator<? super E> equalator, final T target)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#union()
	}

	@Override
	public final <T extends Procedure<? super E>> T intersect(final XGettingCollection<? extends E> other, final Equalator<? super E> equalator, final T target)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#intersect()
	}

	@Override
	public final <T extends Procedure<? super E>> T except(final XGettingCollection<? extends E> other, final Equalator<? super E> equalator, final T target)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingCollection<E>#except()
	}

	@Override
	public final <P extends Procedure<? super E>> P iterate(final P procedure)
	{
		final int index[] = this.prevSizes, indexSize = this.prevCount, currentSize = this.lastSize;
		final E[] segments[] = this.segments, currentSegment = this.lastSegment;

		try{
			for(int i = 0; i < indexSize; i++)
			{
				final int segmentSize = index[i];
				final E[] segment     = segments[i];
				for(int s = 0; s < segmentSize; s++)
				{
					procedure.accept(segment[s]);
				}
			}
			for(int s = 0; s < currentSize; s++)
			{
				procedure.accept(currentSegment[s]);
			}
		}
		catch(final ThrowBreak b)
		{
			// abort and fall through to return
		}

		return procedure;
	}

	@Override
	public final <IP extends IndexProcedure<? super E>> IP iterate(final IP procedure)
	{
		final int index[] = this.prevSizes, indexSize = this.prevCount, currentSize = this.lastSize;
		final E[] segments[] = this.segments, currentSegment = this.lastSegment;

		int idx = 0;
		try{
			for(int i = 0; i < indexSize; i++)
			{
				final int segmentSize = index[i];
				final E[] segment     = segments[i];
				for(int s = 0; s < segmentSize; s++)
				{
					procedure.accept(segment[s], idx++);
				}
			}
			for(int s = 0; s < currentSize; s++)
			{
				procedure.accept(currentSegment[s], idx++);
			}
		}
		catch(final ThrowBreak b)
		{
			// abort and fall through to return
		}

		return procedure;
	}

	@Override
	public final <A> A join(final BiProcedure<? super E, ? super A> joiner, final A aggregate)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XJoinable<E>#join()
	}

	@Override
	public final void clear()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XRemovingCollection<E>#clear()
	}

	@Override
	public final void truncate()
	{
		this.prevSizes = new int[1];
		this.lastSegment = (this.segments = newStorage(1, this.segmentLength))[0];
		this.lastSize = this.prevCount = 0;
		this.size = 0;
	}

	@Override
	public final int consolidate()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XRemovingCollection<E>#consolidate()
	}

	@Override
	public final int nullRemove()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XRemovingCollection<E>#nullRemove()
	}

	@Override
	public final boolean removeOne(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XRemovingCollection<E>#removeOne()
	}

	@Override
	public final int remove(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XRemovingCollection<E>#remove()
	}

	@Override
	public final int removeAll(final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XRemovingCollection<E>#removeAll()
	}

	@Override
	public final int retainAll(final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XRemovingCollection<E>#retainAll()
	}

	@Override
	public final int removeDuplicates()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XRemovingCollection<E>#removeDuplicates()
	}

	private E internalRemoveGetFirst()
	{
		final E first;
		if(this.prevCount == 0){
			first = this.lastSegment[0];
			System.arraycopy(this.lastSegment, 1, this.lastSegment, 0, --this.lastSize);
			this.lastSegment[this.lastSize] = null;
		}
		else {
			first = this.segments[0][0];
			System.arraycopy(this.segments[0], 1, this.segments[0], 0, this.segmentLength - 1);
			this.prevSizes[0]--;
		}

		this.size--;
		return first;
	}

	@Override
	public final E fetch()
	{
		if(this.size == 0){
			throw new IndexBoundsException(0, 0, 0);
		}
		return this.internalRemoveGetFirst();
	}

	@Override
	public final E pinch()
	{
		if(this.size == 0){
			return null;
		}
		return this.internalRemoveGetFirst();
	}

	@Override
	public final E retrieve(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XProcessingCollection<E>#retrieve()
	}

	@Override
	public final E retrieveBy(final Predicate<? super E> predicate)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XProcessingCollection<E>#retrieve()
	}

	@Override
	public final int removeDuplicates(final Equalator<? super E> equalator)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XProcessingCollection<E>#removeDuplicates()
	}

	@Override
	public final int removeBy(final Predicate<? super E> predicate)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XProcessingCollection<E>#remove()
	}

	@Override
	public final <C extends Procedure<? super E>> C moveTo(final C target, final Predicate<? super E> predicate)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XProcessingCollection<E>#moveTo()
	}

	@Override
	public final <P extends Procedure<? super E>> P process(final P processor)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Processable<E>#process()
	}

	@Override
	public final E at(final int index)
	{
		if(index < 0 || index >= this.size){
			throw new IndexBoundsException(0, this.size, index);
		}

		final int segmentSizes[] = this.prevSizes, aSize = this.prevCount;

		int idx = index;
		for(int i = 0; i < aSize; i++)
		{
			if(idx < segmentSizes[i]){
				return this.segments[i][idx];
			}
			idx -= segmentSizes[i];
		}
		return this.lastSegment[idx];
	}

	@Override
	public final E first()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingSequence<E>#first()
	}

	@Override
	public final E last()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingSequence<E>#last()
	}

	@Override
	public final E poll()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingSequence<E>#poll()
	}

	@Override
	public final E peek()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingSequence<E>#peek()
	}

	@Override
	public final int maxIndex(final Comparator<? super E> comparator)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingSequence<E>#maxIndex()
	}

	@Override
	public final int minIndex(final Comparator<? super E> comparator)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingSequence<E>#minIndex()
	}

	@Override
	public final int indexOf(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingSequence<E>#indexOf()
	}

	@Override
	public final int indexBy(final Predicate<? super E> predicate)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingSequence<E>#indexOf()
	}

	@Override
	public final int lastIndexOf(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingSequence<E>#lastIndexOf()
	}

	@Override
	public final int lastIndexBy(final Predicate<? super E> predicate)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingSequence<E>#lastIndexOf()
	}

	@Override
	public final int scan(final Predicate<? super E> predicate)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingSequence<E>#scan()
	}

	@Override
	public final boolean isSorted(final Comparator<? super E> comparator)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingSequence<E>#isSorted()
	}

	@Override
	public final <T extends Procedure<? super E>> T copySelection(final T target, final int... indices)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingSequence<E>#copySelection()
	}

	@Override
	public final <T> T[] copyTo(final T[] target, final int targetOffset, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingSequence<E>#copyTo()
	}

	@Override
	public final E removeAt(final int index)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XProcessingSequence<E>#remove()
	}

	@Override
	public final E pop()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XProcessingSequence<E>#pop()
	}

	@Override
	public final E pick()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XProcessingSequence<E>#pick()
	}

	@Override
	public final <C extends Procedure<? super E>> C moveSelection(final C target, final int... indices)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XProcessingSequence<E>#moveSelection()
	}

	@Override
	public final VarList<E> removeRange(final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XRemovingSequence<E>#removeRange()
	}

	@Override
	public final int removeSelection(final int[] indices)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XRemovingSequence<E>#removeSelection()
	}

	@Override
	public final ConstList<E> immure()
	{
		return new ConstList<>(this);
	}

	@Override
	public final ListIterator<E> listIterator()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingList<E>#listIterator()
	}

	@Override
	public final ListIterator<E> listIterator(final int index)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingList<E>#listIterator()
	}

	@Override
	public final OldList<E> old()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingList<E>#old()
	}

	@Override
	public final XGettingList<E> view()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingList<E>#view()
	}

	@Override
	public final XGettingList<E> view(final int lowIndex, final int highIndex)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XGettingList<E>#view()
	}

	@Override
	public final boolean input(final int index, final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XInputtingSequence<E>#input()
	}

	@Override
	public final boolean nullInput(final int index)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XInputtingSequence<E>#nullInput()
	}

	@Override
	public final int inputAll(final int index, final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XInputtingSequence<E>#inputAll()
	}

	@Override
	public final int inputAll(final int index, final E[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XInputtingSequence<E>#inputAll()
	}

	@Override
	public final int inputAll(final int index, final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XInputtingSequence<E>#inputAll()
	}

	@Override
	public final boolean insert(final int index, final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XInsertingSequence<E>#insert()
	}

	@Override
	public final boolean nullInsert(final int index)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XInsertingSequence<E>#nullInsert()
	}

	@Override
	public final int insertAll(final int index, final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XInsertingSequence<E>#insertAll()
	}

	@Override
	public final int insertAll(final int index, final E[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XInsertingSequence<E>#insertAll()
	}

	@Override
	public final int insertAll(final int index, final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XInsertingSequence<E>#insertAll()
	}

	@Override
	public final boolean prepend(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XPrependingSequence<E>#prepend()
	}

	@Override
	public final boolean nullPrepend()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XPrependingSequence<E>#nullPrepend()
	}

	@Override
	public final boolean preput(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XPreputtingSequence<E>#preput()
	}

	@Override
	public final boolean nullPreput()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XPreputtingSequence<E>#nullPreput()
	}

	@Override
	public final boolean replaceOne(final E element, final E replacement)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XReplacingBag<E>#replaceOne()
	}

	@Override
	public final int replace(final E element, final E replacement)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XReplacingBag<E>#replace()
	}

	@Override
	public final int replaceAll(final XGettingCollection<? extends E> elements, final E replacement)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XReplacingBag<E>#replaceAll()
	}

	@Override
	public final boolean replaceOne(final Predicate<? super E> predicate, final E substitute)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XReplacingBag<E>#replaceOne()
	}

	@Override
	public final int replace(final Predicate<? super E> predicate, final E substitute)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XReplacingBag<E>#replace()
	}

	@Override
	public final int modify(final Function<E, E> mapper)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XReplacingBag<E>#modify()
	}

	@Override
	public final int modify(final Predicate<? super E> predicate, final Function<E, E> mapper)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XReplacingBag<E>#modify()
	}

	@Override
	public final boolean set(final int index, final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XSettingSequence<E>#set()
	}

	@Override
	public final E setGet(final int index, final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XSettingSequence<E>#setGet()
	}

	@Override
	public final void setFirst(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XSettingSequence<E>#setFirst()
	}

	@Override
	public final void setLast(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XSettingSequence<E>#setLast()
	}

	@Override
	public final VarList<E> addAll(final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#addAll()
	}

	@Override
	public final VarList<E> addAll(final E[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#addAll()
	}

	@Override
	public final VarList<E> addAll(final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#addAll()
	}

	@Override
	public final VarList<E> putAll(final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#putAll()
	}

	@Override
	public final VarList<E> putAll(final E[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#putAll()
	}

	@Override
	public final VarList<E> putAll(final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#putAll()
	}

	@Override
	public final VarList<E> prependAll(final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#prependAll()
	}

	@Override
	public final VarList<E> prependAll(final E[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#prependAll()
	}

	@Override
	public final VarList<E> prependAll(final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#prependAll()
	}

	@Override
	public final VarList<E> preputAll(final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#preputAll()
	}

	@Override
	public final VarList<E> preputAll(final E[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#preputAll()
	}

	@Override
	public final VarList<E> preputAll(final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#preputAll()
	}

	@Override
	public final VarList<E> setAll(final int index, final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#setAll()
	}

	@Override
	public final VarList<E> set(final int index, final E[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#set()
	}

	@Override
	public final VarList<E> set(final int index, final XGettingSequence<? extends E> elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#set()
	}

	@Override
	public final VarList<E> swap(final int indexA, final int indexB)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#swap()
	}

	@Override
	public final VarList<E> swap(final int indexA, final int indexB, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#swap()
	}

	@Override
	public final VarList<E> retainRange(final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#retainRange()
	}

	@Override
	public final VarList<E> copy()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#copy()
	}

	@Override
	public final VarList<E> toReversed()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#toReversed()
	}

	@Override
	public final VarList<E> reverse()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#reverse()
	}

	@Override
	public final VarList<E> range(final int fromIndex, final int toIndex)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#range()
	}

	@Override
	public final VarList<E> fill(final int offset, final int length, final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#fill()
	}

	@Override
	public final VarList<E> sort(final Comparator<? super E> comparator)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#sort()
	}

	@Override
	public final VarList<E> shiftTo(final int sourceIndex, final int targetIndex)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#shiftTo()
	}

	@Override
	public final VarList<E> shiftTo(final int sourceIndex, final int targetIndex, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#shiftTo()
	}

	@Override
	public final VarList<E> shiftBy(final int sourceIndex, final int distance)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#shiftBy()
	}

	@Override
	public final VarList<E> shiftBy(final int sourceIndex, final int distance, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME XList<E>#shiftBy()
	}

	@Override
	public final String toString()
	{
		final VarString vs = VarString.New(cap_int(this.size * 4L)).add('[');
		this.iterate(e -> vs.add(e).add(','));
		if(this.isEmpty()){
			vs.blank();
		}
		return vs.setLast(']').toString();
	}

}
