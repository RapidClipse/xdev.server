
package net.jadoth.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.old.OldGettingList;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingList;
import net.jadoth.collections.types.XImmutableList;
import net.jadoth.exceptions.IndexBoundsException;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.IndexProcedure;
import net.jadoth.functional.Procedure;
import net.jadoth.util.Equalator;
import net.jadoth.util.iterables.ReadOnlyListIterator;

public class SubListView<E> implements XGettingList<E>
{
	///////////////////////////////////////////////////////////////////////////
	//  class methods   //
	/////////////////////

	static final <E> IndexProcedure<E> offset(
		final IndexProcedure<? super E> procedure,
		final int startIndex,
		final int d
	)
	{
		// tricky 8-)
		return new IndexProcedure<E>(){
			@Override public void accept(final E e, final int index) {
				procedure.accept(e, (index - startIndex)*d);
			}
		};
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	final XGettingList<E> list;
	int startIndex;
	int size;
	int length;
	int d;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	public SubListView(final XGettingList<E> list, final int fromIndex, final int toIndex)
	{
		final int length, size;
		if(fromIndex <= toIndex){
			size = toIndex - fromIndex + 1;
			length = size;
			if(fromIndex < 0 || toIndex >= Jadoth.to_int(list.size())){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(fromIndex, size));
			}
		}
		else {
			size = fromIndex - toIndex + 1;
			length = -size;
			if(toIndex < 0 || fromIndex >= Jadoth.to_int(list.size())){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(toIndex, size));
			}
		}

		this.list = list;
		this.startIndex = fromIndex;
		this.size = size;
		this.length = length;
		this.d = length < 0 ?-1 :1;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	String exceptionStringRange(final int startIndex, final int length)
	{
		return "Range ["+startIndex+';'+(startIndex + length - 1)+"] not in [0;"+(this.size-1)+"]";
	}

	String exceptionStringRange2(final int startIndex, final int endIndex)
	{
		return "Range ["+startIndex+';'+endIndex+"] not in [0;"+(this.size-1)+"]";
	}

	void checkIndex(final int index)
	{
		if(index < 0 || index >= this.size){
			throw new IndexBoundsException(this.size, index);
		}
	}

	void checkVector(final int startIndex, final int length)
	{
		if(length >= 0){
			if(startIndex < 0 || startIndex + length > this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, length));
			}
		}
		else {
			if(startIndex + length < -1 || startIndex >= this.size){
				throw new IndexOutOfBoundsException(this.exceptionStringRange(startIndex, length));
			}
		}
	}

	void checkRange(final int startIndex, final int endIndex)
	{
		if(startIndex < 0 || endIndex >= this.size){
			throw new IndexOutOfBoundsException(this.exceptionStringRange2(startIndex, endIndex));
		}
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public Equalator<? super E> equality()
	{
		return this.list.equality();
	}

	@Override
	public boolean containsSearched(final Predicate<? super E> predicate)
	{
		return  XUtilsCollection.rngApplies(this.list, this.startIndex, this.length, predicate);
	}

	@Override
	public boolean applies(final Predicate<? super E> predicate)
	{
		return  XUtilsCollection.rngAppliesAll(this.list, this.startIndex, this.length, predicate);
	}

//	@Override
//	public boolean contains(final E sample, final Equalator<? super E> equalator)
//	{
//		return  XUtilsCollection.rngContains(this.list, this.startIndex, this.length, sample, equalator);
//	}

	@Override
	public boolean nullAllowed()
	{
		return this.list.nullAllowed();
	}

	@Override
	public boolean nullContained()
	{
		return  XUtilsCollection.rngContainsNull(this.list, this.startIndex, this.length);
	}

	@Override
	public int countBy(final Predicate<? super E> predicate)
	{
		return  XUtilsCollection.rngCount(this.list, this.startIndex, this.length, predicate);
	}

//	@Override
//	public int count(final E sample, final Equalator<? super E> equalator)
//	{
//		return  XUtilsCollection.rngCount(this.list, this.startIndex, this.length, sample, equalator);
//	}

//	@Override
//	public E search(final E sample, final Equalator<? super E> equalator)
//	{
//		return  XUtilsCollection.rngFind(this.list, this.startIndex, this.length, sample, equalator);
//	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		if(samples == null || Jadoth.to_int(samples.size()) != this.size || !(samples instanceof SubList<?>)) return false;
		if(samples == this) return true;
		return  XUtilsCollection.rngEqualsContent(this.list, this.startIndex, this.length, (SubList<E>)samples, equalator);
	}

	@Override
	public <C extends Procedure<? super E>> C except(final XGettingCollection<? extends E> other, final Equalator<? super E> equalator, final C target)
	{
		return XUtilsCollection.rngExcept(this.list, this.startIndex, this.length, other, equalator, target);
	}

	@Override
	public boolean hasDistinctValues(final Equalator<? super E> equalator)
	{
		return  XUtilsCollection.rngHasUniqueValues(this.list, this.startIndex, this.length, equalator);
	}

	@Override
	public boolean contains(final E element)
	{
		return  XUtilsCollection.rngContains(this.list, this.startIndex, this.length, element);
	}

	@Override
	public int count(final E element)
	{
		return  XUtilsCollection.rngCount(this.list, this.startIndex, this.length, element);
	}

	@Override
	public boolean hasDistinctValues()
	{
		return  XUtilsCollection.rngHasUniqueValues(this.list, this.startIndex, this.length);
	}

	@Override
	public <C extends Procedure<? super E>> C intersect(final XGettingCollection<? extends E> other, final Equalator<? super E> equalator, final C target)
	{
		return XUtilsCollection.rngIntersect(this.list, this.startIndex, this.length, other, equalator, target);
	}

	@Override
	public boolean isEmpty()
	{
		return this.size == 0;
	}

	@Override
	public E max(final Comparator<? super E> comparator)
	{
		return  XUtilsCollection.rngMax(this.list, this.startIndex, this.length, comparator);
	}

	@Override
	public E min(final Comparator<? super E> comparator)
	{
		return  XUtilsCollection.rngMin(this.list, this.startIndex, this.length, comparator);
	}

	@Override
	public SubListView<E> copy()
	{
		return new SubListView<>(this.list, this.startIndex, this.getEndIndex());
	}

	protected int[] shiftIndices(final int[] indices)
	{
		// shift indices, determine min and max, check range
		final int len, startIndex = this.startIndex;
		final int[] shifted = new int[len = indices.length];
		int min = Integer.MAX_VALUE, max = 0;

		for(int i = 0, idx; i < len; i++){
			idx = indices[i];
			if(idx < min){
				min = idx;
			}
			if(idx > max){
				max = idx;
			}
			shifted[i] = idx + startIndex;
		}
		this.checkRange(min, max);
		return shifted;
	}

	public int getEndIndex()
	{
		return this.startIndex + this.length - this.d;
	}

	@Override
	public <C extends Procedure<? super E>> C copySelection(final C target, final int... indices)
	{
		return this.list.copySelection(target, this.shiftIndices(indices));
	}

	@Override
	public E at(final int index)
	{
		this.checkIndex(index);
		return this.list.at(this.startIndex + index - this.d);
	}

	@Override
	public E get()
	{
		return this.list.at(this.startIndex);
	}

	@Override
	public E first()
	{
		return this.list.at(this.startIndex);
	}

	@Override
	public E last()
	{
		return this.list.at(this.getEndIndex());
	}

	@Override
	public E poll()
	{
		return this.size == 0 ?null :this.list.at(this.startIndex);
	}

	@Override
	public E peek()
	{
		return this.size == 0 ?null :this.list.at(this.getEndIndex());
	}

	@Override
	public int indexOf(final E element)
	{
		return XUtilsCollection.rngIndexOF(this.list, this.startIndex, this.length, element);
	}

//	@Override
//	public int indexOf(final E sample, final Equalator<? super E> equalator)
//	{
//		return XUtilsCollection.rngIndexOf(this.list, this.startIndex, this.length, sample, equalator);
//	}

	@Override
	public int indexBy(final Predicate<? super E> predicate)
	{
		return XUtilsCollection.rngIndexOf(this.list, this.startIndex, this.length, predicate);
	}

	@Override
	public boolean isSorted(final Comparator<? super E> comparator)
	{
		return XUtilsCollection.rngIsSorted(this.list, this.startIndex, this.length, comparator);
	}

	@Override
	public int lastIndexOf(final E element)
	{
		return XUtilsCollection.rngIndexOF(this.list, this.getEndIndex(), -this.length, element);
	}

	@Override
	public int lastIndexBy(final Predicate<? super E> predicate)
	{
		return XUtilsCollection.rngIndexOf(this.list, this.getEndIndex(), -this.length, predicate);
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
	public int maxIndex(final Comparator<? super E> comparator)
	{
		return XUtilsCollection.rngMaxIndex(this.list, this.startIndex, this.length, comparator);
	}

	@Override
	public int minIndex(final Comparator<? super E> comparator)
	{
		return XUtilsCollection.rngMinIndex(this.list, this.startIndex, this.length, comparator);
	}

	@Override
	public int scan(final Predicate<? super E> predicate)
	{
		return XUtilsCollection.rngScan(this.list, this.startIndex, this.length, predicate);
	}

	@Override
	public long size()
	{
		return this.size;
	}

	@Override
	public long maximumCapacity()
	{
		return this.list.maximumCapacity() - Jadoth.to_int(this.list.size()) + this.size; // complicated ^^
	}

	@Override
	public boolean isFull()
	{
		return this.list.isFull();
	}

	@Override
	public long remainingCapacity()
	{
		return this.list.remainingCapacity();
	}

	@Override
	public SubListView<E> view()
	{
		return this;
	}

	@Override
	public SubListView<E> view(final int lowIndex, final int highIndex)
	{
		this.checkRange(lowIndex, highIndex);
		return new SubListView<>(this.list, this.startIndex + lowIndex*this.d, this.startIndex + highIndex*this.d);
	}

	@Override
	public SubListView<E> range(final int lowIndex, final int highIndex)
	{
		this.checkRange(lowIndex, highIndex);
		return new SubListView<>(this.list, this.startIndex + lowIndex*this.d, this.startIndex + highIndex*this.d);
	}

	@Override
	public Object[] toArray()
	{
		return XUtilsCollection.rngToArray(this.list, this.startIndex, this.length);
	}

	@Override
	public XImmutableList<E> immure()
	{
		return new ConstList<>(this.toArray(), this.size);
	}

	@Override
	public SubListView<E> toReversed()
	{
		return new SubListView<>(this.list, this.getEndIndex(), this.startIndex);
	}

//	@Override
//	public <R> R aggregate(final Aggregator<? super E, R> aggregate)
//	{
//		return XUtilsCollection.rngAggregate(this.list, this.startIndex, this.length, aggregate);
//	}

//	@Override
//	public boolean containsAll(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
//	{
//		return XUtilsCollection.rngContainsAll(this.list, this.startIndex, this.length, samples, equalator);
//	}

	@Override
	public boolean containsAll(final XGettingCollection<? extends E> elements)
	{
		return XUtilsCollection.rngContainsAll(this.list, this.startIndex, this.length, elements);
	}

	@Override
	public boolean containsId(final E element)
	{
		return XUtilsCollection.rngContainsId(this.list, this.startIndex, this.length, element);
	}

	@Override
	public <C extends Procedure<? super E>> C copyTo(final C target)
	{
		return XUtilsCollection.rngCopyTo(this.list, this.startIndex, this.length, target);
	}

	@Override
	public <C extends Procedure<? super E>> C filterTo(final C target, final Predicate<? super E> predicate)
	{
		return XUtilsCollection.rngCopyTo(this.list, this.startIndex, this.length, target, predicate);
	}

	@Override
	public <T> T[] copyTo(final T[] target, final int offset)
	{
		return this.list.copyTo(target, offset, this.startIndex, this.length);
	}

	@Override
	public <C extends Procedure<? super E>> C distinct(final C target)
	{
		return XUtilsCollection.rngDistinct(this.list, this.startIndex, this.length, target);
	}

	@Override
	public <C extends Procedure<? super E>> C distinct(final C target, final Equalator<? super E> equalator)
	{
		return XUtilsCollection.rngDistinct(this.list, this.startIndex, this.length, target, equalator);
	}

	@Override
	public boolean hasVolatileElements()
	{
		return this.list.hasVolatileElements();
	}

	@Override
	public E seek(final E sample)
	{
		return XUtilsCollection.rngGet(this.list, this.startIndex, this.length, sample, this.list.equality());
	}

	@Override
	public E search(final Predicate<? super E> predicate)
	{
		return XUtilsCollection.rngSearch(this.list, this.startIndex, this.length, predicate);
	}

	@Override
	public E[] toArray(final Class<E> type)
	{
		return XUtilsCollection.rngToArray(this.list, this.startIndex, this.length, type);
	}

	@Override
	public <C extends Procedure<? super E>> C union(final XGettingCollection<? extends E> other, final Equalator<? super E> equalator, final C target)
	{
		return XUtilsCollection.rngUnion(this.list, this.startIndex, this.length, other, equalator, target);
	}

	@Override
	public final <P extends Procedure<? super E>> P iterate(final P procedure)
	{
		XUtilsCollection.rngIterate(this.list, this.startIndex, this.length, procedure);
		return procedure;
	}

	@Override
	public final <P extends IndexProcedure<? super E>> P iterate(final P procedure)
	{
		XUtilsCollection.rngIterate(this.list, this.startIndex, this.length, offset(procedure, this.startIndex, this.d));
		return procedure;
	}

	@Override
	public final <A> A join(final BiProcedure<? super E, ? super A> joiner, final A aggregate)
	{
		XUtilsCollection.rngJoin(this.list, this.startIndex, this.length, joiner, aggregate);
		return aggregate;
	}

	@Override
	public boolean equalsContent(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}


	@Override
	public OldSubListView<E> old()
	{
		return new OldSubListView<>(this);
	}

	static class OldSubListView<E> extends OldGettingList<E>
	{
		OldSubListView(final SubListView<E> list)
		{
			super(list);
		}

		@Override
		public SubListView<E> parent()
		{
			return (SubListView<E>)super.parent();
		}

	}

//	@Override
//	public SubListView<E> iterate(final CtrlProcedure<? super E> procedure)
//	{
//		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
//	}

//	@Override
//	public SubListView<E> iterate(final CtrlIndexProcedure<? super E> procedure)
//	{
////		XSequences.rngIterate(this.list, this.startIndex, this.length, procedure);
//		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
//	}

	@Override
	public <T> T[] copyTo(final T[] target, final int targetOffset, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

}
