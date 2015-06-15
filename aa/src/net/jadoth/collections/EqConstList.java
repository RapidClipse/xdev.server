package net.jadoth.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.functions.IsCustomEqual;
import net.jadoth.collections.old.OldGettingList;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingList;
import net.jadoth.collections.types.XImmutableList;
import net.jadoth.collections.types.XList;
import net.jadoth.collections.types.XSettingList;
import net.jadoth.exceptions.IndexBoundsException;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.IndexProcedure;
import net.jadoth.functional.Procedure;
import net.jadoth.util.Composition;
import net.jadoth.util.Equalator;
import net.jadoth.util.iterables.ReadOnlyListIterator;


/**
 * Immutable implementation of extended collection type {@link XGettingList}.
 * <p>
 * For mutable extended lists (implementors of {@link XSettingList}, {@link XList}), see {@link FixedList},
 * {@link LimitList}, {@link BulkList}.
 * <p>
 * As instances of this class are completely immutable after creation, this list is automatically thread-safe.
 * <p>
 * Also note that by being an extended collection, this implementation offers various functional and batch procedures
 * to maximize internal iteration potential, eliminating the need to use the ill-conceived external iteration
 * {@link Iterator} paradigm.
 *
 * @author Thomas Muenz
 * @version 0.91, 2011-02-28
 */
public final class EqConstList<E> extends AbstractSimpleArrayCollection<E> implements XImmutableList<E>, Composition
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	private final E[]                  data     ; // the storage array containing the elements
	private final Equalator<? super E> equalator;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	public EqConstList(final Equalator<? super E> equalator, final int initialCapacity)
	{
		super();
		this.data = newArray(initialCapacity);
		this.equalator = equalator;
	}

	@SuppressWarnings("unchecked")
	public EqConstList(final EqConstList<? extends E> original) throws NullPointerException
	{
		super();
		this.data = original.data.clone();
		this.equalator = (Equalator<? super E>)original.equalator;
	}

	@SuppressWarnings("unchecked")
	public EqConstList(final Equalator<? super E> equalator, final XGettingCollection<? extends E> elements) throws NullPointerException
	{
		super();
		this.data = (E[])elements.toArray();
		this.equalator = equalator;
	}

	@SafeVarargs
	public EqConstList(final Equalator<? super E> equalator, final E... elements) throws NullPointerException
	{
		super();
		this.data = elements.clone();
		this.equalator = equalator;
	}

	public EqConstList(final Equalator<? super E> equalator, final E[] src, final int srcStart, final int srcLength)
	{
		super();
		// automatically check arguments 8-)
		System.arraycopy(src, srcStart, this.data = newArray(srcLength), 0, srcLength);
		this.equalator = equalator;
	}

	EqConstList(final Equalator<? super E> equalator, final E[] internalData, final int size)
	{
		super();
		this.data = internalData;
		this.equalator = equalator;
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
		return this.equalator;
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
	public EqConstList<E> copy()
	{
		return new EqConstList<>(this);
	}

	@Override
	public EqConstList<E> immure()
	{
		return this;
	}

	@Override
	public EqConstList<E> toReversed()
	{
		final E[] data = this.data;
		final E[] rData = newArray(data.length);
		for(int i = data.length, r = 0; i --> 0;){
			rData[r++] = data[i];
		}
		return new EqConstList<>(this.equalator, rData, data.length);
	}

	@Override
	public E[] toArray(final Class<E> type)
	{
		final E[] array = JadothArrays.newArray(type, this.data.length);
		System.arraycopy(this.data, 0, array, 0, this.data.length);
		return array;
	}

	// executing //

	@Override
	public final <P extends Procedure<? super E>> P iterate(final P procedure)
	{
		AbstractArrayStorage.iterate(this.data, this.data.length, procedure);
		return procedure;
	}

	@Override
	public final <P extends IndexProcedure<? super E>> P iterate(final P procedure)
	{
		AbstractArrayStorage.iterate(this.data, this.data.length, procedure);
		return procedure;
	}

	@Override
	public final <A> A join(final BiProcedure<? super E, ? super A> joiner, final A aggregate)
	{
		AbstractArrayStorage.join(this.data, this.data.length, joiner, aggregate);
		return aggregate;
	}

	@Override
	public int count(final E element)
	{
		return AbstractArrayStorage.conditionalCount(this.data, this.data.length, new IsCustomEqual<>(this.equalator, element));
	}

	@Override
	public int countBy(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.conditionalCount(this.data, this.data.length, predicate);
	}

	// index querying //

	@Override
	public int indexOf(final E element)
	{
		return AbstractArrayStorage.conditionalIndexOf(this.data, this.data.length, new IsCustomEqual<>(this.equalator, element));
	}

	@Override
	public int indexBy(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.conditionalIndexOf(this.data, this.data.length, predicate);
	}

	@Override
	public int lastIndexOf(final E element)
	{
		return AbstractArrayStorage.reverseConditionalIndexOf(this.data, this.data.length - 1, 0, new IsCustomEqual<>(this.equalator, element));
	}

	@Override
	public int lastIndexBy(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.lastIndexOf(this.data, this.data.length, predicate);
	}

	@Override
	public int maxIndex(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.maxIndex(this.data, this.data.length, comparator);
	}

	@Override
	public int minIndex(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.minIndex(this.data, this.data.length, comparator);
	}

	@Override
	public int scan(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.scan(this.data, this.data.length, predicate);
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
		return this.data[this.data.length - 1];
	}

	@Override
	public E poll()
	{
		return this.data.length == 0 ?null :(E)this.data[0];
	}

	@Override
	public E peek()
	{
		return this.data.length == 0 ?null :(E)this.data[this.data.length - 1];
	}

	@Override
	public E seek(final E sample)
	{
		return AbstractArrayStorage.queryElement(this.data, this.data.length, new IsCustomEqual<>(this.equalator, sample), null);
	}

	@Override
	public E search(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.queryElement(this.data, this.data.length, predicate, null);
	}

	@Override
	public E max(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.max(this.data, this.data.length, comparator);
	}

	@Override
	public E min(final Comparator<? super E> comparator)
	{
		return AbstractArrayStorage.min(this.data, this.data.length, comparator);
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
		return AbstractArrayStorage.isSorted(this.data, this.data.length, comparator);
	}

	@Override
	public boolean hasDistinctValues()
	{
		return AbstractArrayStorage.hasDistinctValues(this.data, this.data.length, this.equalator);
	}

	@Override
	public boolean hasDistinctValues(final Equalator<? super E> equalator)
	{
		return AbstractArrayStorage.hasDistinctValues(this.data, this.data.length, equalator);
	}

	// boolean querying - applies //

	@Override
	public boolean containsSearched(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.contains(this.data, this.data.length, predicate);
	}

	@Override
	public boolean applies(final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.applies(this.data, this.data.length, predicate);
	}

	// boolean querying - contains //

	@Override
	public boolean nullContained()
	{
		return AbstractArrayStorage.nullContained(this.data, this.data.length);
	}

	@Override
	public boolean containsId(final E element)
	{
		return AbstractArrayStorage.containsSame(this.data, this.data.length, element);
	}

	@Override
	public boolean contains(final E element)
	{
		return AbstractArrayStorage.contains(this.data, this.data.length, new IsCustomEqual<>(this.equalator, element));
	}

	@Override
	public boolean containsAll(final XGettingCollection<? extends E> elements)
	{
		return AbstractArrayStorage.containsAll(this.data, this.data.length, elements, this.equalator);
	}

//	@Override
//	public boolean containsAll(final XGettingCollection<? extends E> elements, final Equalator<? super E> equalator)
//	{
//		return AbstractArrayStorage.containsAll(this.data, this.data.length, elements, equalator);
//	}

	// boolean querying - equality //

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		if(samples == null || !(samples instanceof EqConstList<?>) || Jadoth.to_int(samples.size()) != this.data.length) return false;
		if(samples == this) return true;

		// equivalent to equalsContent()
		return JadothArrays.equals(this.data, 0, ((EqConstList<?>)samples).data, 0, this.data.length, (Equalator<Object>)equalator);
	}

	@Override
	public boolean equalsContent(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		if(samples == null || Jadoth.to_int(samples.size()) != this.data.length){
			return false;
		}
		if(samples == this){
			return true;
		}
		return AbstractArrayStorage.equalsContent(this.data, this.data.length, samples, equalator);
	}

	// data set procedures //

	@Override
	public <C extends Procedure<? super E>> C intersect(
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		return AbstractArrayStorage.intersect(this.data, this.data.length, samples, equalator, target);
	}

	@Override
	public <C extends Procedure<? super E>> C except(
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		return AbstractArrayStorage.except(this.data, this.data.length, samples, equalator, target);
	}

	@Override
	public <C extends Procedure<? super E>> C union(
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		return AbstractArrayStorage.union(this.data, this.data.length, samples, equalator, target);
	}

	@Override
	public <C extends Procedure<? super E>> C copyTo(final C target)
	{
		return AbstractArrayStorage.copyTo(this.data, this.data.length, target);
	}

	@Override
	public <C extends Procedure<? super E>> C filterTo(final C target, final Predicate<? super E> predicate)
	{
		return AbstractArrayStorage.copyTo(this.data, this.data.length, target, predicate);
	}

	@Override
	public <T> T[] copyTo(final T[] target, final int offset)
	{
		System.arraycopy(this.data, 0, target, offset, this.data.length);
		return target;
	}

	@Override
	public <T> T[] copyTo(final T[] target, final int targetOffset, final int offset, final int length)
	{
		return AbstractArrayStorage.rngCopyTo(this.data, this.data.length, offset, length, target, targetOffset);
	}

	public <T> T[] rngCopyTo(final int startIndex, final int length, final T[] target, final int offset)
	{
		return AbstractArrayStorage.rngCopyTo(
			this.data, this.data.length, startIndex, length,  target, offset
		);
	}

	@Override
	public <C extends Procedure<? super E>> C distinct(final C target)
	{
		return AbstractArrayStorage.distinct(this.data, this.data.length, target, this.equalator);
	}

	@Override
	public <C extends Procedure<? super E>> C distinct(final C target, final Equalator<? super E> equalator)
	{
		return AbstractArrayStorage.distinct(this.data, this.data.length, target, equalator);
	}

	@Override
	public <C extends Procedure<? super E>> C copySelection(final C target, final int... indices)
	{
		return AbstractArrayStorage.copySelection(this.data, this.data.length, indices, target);
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
	public long size()
	{
		return this.data.length;
	}

	@Override
	public long maximumCapacity()
	{
		return this.data.length;
	}

	@Override
	public boolean isFull()
	{
		return true;
	}

	@Override
	public long remainingCapacity()
	{
		return 0;
	}

	@Override
	public EqConstList<E> view()
	{
		return this;
	}

	@Override
	public EqConstList<E> view(final int lowIndex, final int highIndex)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

	@Override
	public EqConstList<E> range(final int fromIndex, final int toIndex)
	{
		// range check is done in constructor
		// (14.06.2011 TM)FIXME: SubConstList
		throw new UnsupportedOperationException();
//		return new SubListView<>(this, fromIndex, toIndex);
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

	@Override
	public E at(final int index) throws ArrayIndexOutOfBoundsException
	{
		if(index >= this.data.length){
			throw new IndexBoundsException(this.data.length, index);
		}
		return this.data[index];
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
	public OldConstList<E> old()
	{
		return new OldConstList<>(this);
	}

	public static final class OldConstList<E> extends OldGettingList<E>
	{
		OldConstList(final EqConstList<E> list)
		{
			super(list);
		}

		@Override
		public EqConstList<E> parent()
		{
			return (EqConstList<E>)super.parent();
		}

	}

}
