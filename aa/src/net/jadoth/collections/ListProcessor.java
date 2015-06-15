
package net.jadoth.collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.old.OldRemovingList;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XImmutableList;
import net.jadoth.collections.types.XProcessingList;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.IndexProcedure;
import net.jadoth.functional.Procedure;
import net.jadoth.util.Equalator;
import net.jadoth.util.iterables.ReadOnlyListIterator;
public final class ListProcessor<E> implements XProcessingList<E>
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	private final XProcessingList<E> subject;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	public ListProcessor(final XProcessingList<E> list)
	{
		super();
		this.subject = list;
	}



	///////////////////////////////////////////////////////////////////////////
	// constant override methods //
	//////////////////////////////

	@Override
	public XImmutableList<E> immure()
	{
		return this.subject.immure();
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public Equalator<? super E> equality()
	{
		return this.subject.equality();
	}

//	@Override
//	public <R> R aggregate(final Aggregator<? super E, R> aggregate)
//	{
//		return this.subject.iterate(aggregate);
//	}

	@Override
	public boolean containsSearched(final Predicate<? super E> predicate)
	{
		return this.subject.containsSearched(predicate);
	}

	@Override
	public boolean applies(final Predicate<? super E> predicate)
	{
		return this.subject.applies(predicate);
	}

	@Override
	public void clear()
	{
		this.subject.clear();
	}

//	@Override
//	public boolean contains(final E sample, final Equalator<? super E> equalator)
//	{
//		return this.subject.contains(sample, equalator);
//	}

	@Override
	public boolean contains(final E element)
	{
		return this.subject.contains(element);
	}

	@Override
	public boolean nullAllowed()
	{
		return true;
	}

	@Override
	public boolean nullContained()
	{
		return this.subject.nullContained();
	}

	@Override
	public boolean containsAll(final XGettingCollection<? extends E> elements)
	{
		return this.subject.containsAll(elements);
	}

//	@Override
//	public boolean containsAll(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
//	{
//		return this.subject.containsAll(samples, equalator);
//	}

	@Override
	public boolean containsId(final E element)
	{
		return this.subject.containsId(element);
	}

	@Override
	public ListProcessor<E> copy()
	{
		this.subject.copy();
		return this;
	}

	@Override
	public <C extends Procedure<? super E>> C copySelection(final C target, final int... indices)
	{
		return this.subject.copySelection(target, indices);
	}

	@Override
	public <C extends Procedure<? super E>> C filterTo(final C target, final Predicate<? super E> predicate)
	{
		return this.subject.filterTo(target, predicate);
	}

	@Override
	public <T> T[] copyTo(final T[] target, final int offset)
	{
		return this.subject.copyTo(target, offset);
	}

	@Override
	public <T> T[] copyTo(final T[] target, final int targetOffset, final int offset, final int length)
	{
		return this.subject.copyTo(target, targetOffset, offset, length);
	}

	@Override
	public <C extends Procedure<? super E>> C copyTo(final C target)
	{
		return this.subject.copyTo(target);
	}

//	@Override
//	public int count(final E sample, final Equalator<? super E> equalator)
//	{
//		return this.subject.count(sample, equalator);
//	}

	@Override
	public int count(final E element)
	{
		return this.subject.count(element);
	}

	@Override
	public int countBy(final Predicate<? super E> predicate)
	{
		return this.subject.countBy(predicate);
	}

	@Override
	public <C extends Procedure<? super E>> C distinct(final C target, final Equalator<? super E> equalator)
	{
		return this.subject.distinct(target, equalator);
	}

	@Override
	public <C extends Procedure<? super E>> C distinct(final C target)
	{
		return this.subject.distinct(target);
	}

	@Deprecated
	@Override
	public boolean equals(final Object o)
	{
		return this.subject.equals(o);
	}

	@Override
	public boolean equals(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		return this.subject.equals(samples, equalator);
	}

	@Override
	public boolean equalsContent(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		return this.subject.equalsContent(samples, equalator);
	}

	@Override
	public <C extends Procedure<? super E>> C except(final XGettingCollection<? extends E> other, final Equalator<? super E> equalator, final C target)
	{
		return this.subject.except(other, equalator, target);
	}

	@Override
	public final <P extends Procedure<? super E>> P iterate(final P procedure)
	{
		return this.subject.iterate(procedure);
	}

	@Override
	public final <P extends IndexProcedure<? super E>> P iterate(final P procedure)
	{
		return this.subject.iterate(procedure);
	}

	@Override
	public final <A> A join(final BiProcedure<? super E, ? super A> joiner, final A aggregate)
	{
		return this.subject.join(joiner, aggregate);
	}

	@Override
	public E at(final int index)
	{
		return this.subject.at(index);
	}

	@Override
	public E get()
	{
		return this.subject.get();
	}

	@Override
	public E first()
	{
		return this.subject.first();
	}

	@Override
	public E last()
	{
		return this.subject.last();
	}

	@Override
	public E poll()
	{
		return this.subject.poll();
	}

	@Override
	public E peek()
	{
		return this.subject.peek();
	}

	@Deprecated
	@Override
	public int hashCode()
	{
		return this.subject.hashCode();
	}

	@Override
	public boolean hasDistinctValues(final Equalator<? super E> equalator)
	{
		return this.subject.hasDistinctValues(equalator);
	}

	@Override
	public boolean hasDistinctValues()
	{
		return this.subject.hasDistinctValues();
	}

	@Override
	public boolean hasVolatileElements()
	{
		return this.subject.hasVolatileElements();
	}

//	@Override
//	public int indexOf(final E sample, final Equalator<? super E> equalator)
//	{
//		return this.subject.indexOf(sample, equalator);
//	}

	@Override
	public int indexOf(final E element)
	{
		return this.subject.indexOf(element);
	}

	@Override
	public int indexBy(final Predicate<? super E> predicate)
	{
		return this.subject.indexBy(predicate);
	}

	@Override
	public <C extends Procedure<? super E>> C intersect(final XGettingCollection<? extends E> other, final Equalator<? super E> equalator, final C target)
	{
		return this.subject.intersect(other, equalator, target);
	}

	@Override
	public boolean isEmpty()
	{
		return this.subject.isEmpty();
	}

	@Override
	public boolean isSorted(final Comparator<? super E> comparator)
	{
		return this.subject.isSorted(comparator);
	}

	@Override
	public int lastIndexBy(final Predicate<? super E> predicate)
	{
		return this.subject.lastIndexBy(predicate);
	}

	@Override
	public int lastIndexOf(final E element)
	{
		return this.subject.lastIndexOf(element);
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
	public E max(final Comparator<? super E> comparator)
	{
		return this.subject.max(comparator);
	}

	@Override
	public int maxIndex(final Comparator<? super E> comparator)
	{
		return this.subject.maxIndex(comparator);
	}

	@Override
	public E min(final Comparator<? super E> comparator)
	{
		return this.subject.min(comparator);
	}

	@Override
	public int minIndex(final Comparator<? super E> comparator)
	{
		return this.subject.minIndex(comparator);
	}

	@Override
	public int scan(final Predicate<? super E> predicate)
	{
		return this.subject.scan(predicate);
	}

//	@Override
//	public E search(final E sample, final Equalator<? super E> equalator)
//	{
//		return this.subject.search(sample, equalator);
//	}

	@Override
	public E seek(final E sample)
	{
		return this.subject.seek(sample);
	}

	@Override
	public E search(final Predicate<? super E> predicate)
	{
		return this.subject.search(predicate);
	}

	@Override
	public long size()
	{
		return Jadoth.to_int(this.subject.size());
	}

	@Override
	public long maximumCapacity()
	{
		return this.subject.maximumCapacity();
	}

	@Override
	public boolean isFull()
	{
		return this.subject.isFull();
	}

	@Override
	public long remainingCapacity()
	{
		return this.subject.remainingCapacity();
	}

	@Override
	public SubListProcessor<E> range(final int fromIndex, final int toIndex)
	{
		return new SubListProcessor<>(this, fromIndex, toIndex);
	}

	@Override
	public Object[] toArray()
	{
		return this.subject.toArray();
	}

	@Override
	public E[] toArray(final Class<E> type)
	{
		return this.subject.toArray(type);
	}

	@Override
	public ListProcessor<E> toReversed()
	{
		this.subject.toReversed();
		return this;
	}

	@Override
	public <C extends Procedure<? super E>> C union(final XGettingCollection<? extends E> other, final Equalator<? super E> equalator, final C target)
	{
		return this.subject.union(other, equalator, target);
	}



	///////////////////////////////////////////////////////////////////////////
	// removing methods  //
	//////////////////////

	@Override
	public int consolidate()
	{
		return this.subject.consolidate();
	}

	@Override
	public <C extends Procedure<? super E>> C moveSelection(final C target, final int... indices)
	{
		return this.subject.moveSelection(target, indices);
	}

	@Override
	public <C extends Procedure<? super E>> C moveTo(final C target, final Predicate<? super E> predicate)
	{
		return this.subject.moveTo(target, predicate);
	}

	@Override
	public final <P extends Procedure<? super E>> P process(final P procedure)
	{
		this.subject.process(procedure);
		return procedure;
	}

	@Override
	public int removeBy(final Predicate<? super E> predicate)
	{
		return this.subject.removeBy(predicate);
	}

	@Override
	public int remove(final E element)
	{
		return this.subject.remove(element);
	}

	@Override
	public E removeAt(final int index)
	{
		return this.subject.removeAt(index);
	}

//	@Override
//	public int removeAll(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
//	{
//		return this.subject.removeAll(samples, equalator);
//	}

	@Override
	public int removeAll(final XGettingCollection<? extends E> elements)
	{
		return this.subject.removeAll(elements);
	}

	@Override
	public int removeDuplicates(final Equalator<? super E> equalator)
	{
		return this.subject.removeDuplicates(equalator);
	}

	@Override
	public int removeDuplicates()
	{
		return this.subject.removeDuplicates();
	}

	@Override
	public E fetch()
	{
		return this.subject.fetch();
	}

	@Override
	public E pop()
	{
		return this.subject.pop();
	}

	@Override
	public E pinch()
	{
		return this.subject.pinch();
	}

	@Override
	public E pick()
	{
		return this.subject.pick();
	}

	@Override
	public E retrieve(final E element)
	{
		return this.subject.retrieve(element);
	}

	@Override
	public E retrieveBy(final Predicate<? super E> predicate)
	{
		return this.subject.retrieveBy(predicate);
	}

	@Override
	public boolean removeOne(final E element)
	{
		return this.subject.removeOne(element);
	}

//	@Override
//	public boolean removeOne(final E sample, final Equalator<? super E> equalator)
//	{
//		return this.subject.removeOne(sample, equalator);
//	}

	@Override
	public ListProcessor<E> removeRange(final int startIndex, final int length)
	{
		this.subject.removeRange(startIndex, length);
		return this;
	}

	@Override
	public ListProcessor<E> retainRange(final int startIndex, final int length)
	{
		this.subject.retainRange(startIndex, length);
		return this;
	}

	@Override
	public int removeSelection(final int[] indices)
	{
		return this.subject.removeSelection(indices);
	}

//	@Override
//	public int retainAll(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
//	{
//		return this.subject.retainAll(samples, equalator);
//	}

	@Override
	public int retainAll(final XGettingCollection<? extends E> elements)
	{
		return this.subject.retainAll(elements);
	}

	@Override
	public int optimize()
	{
		return this.subject.optimize();
	}

	@Override
	public ListView<E> view()
	{
		return new ListView<>(this);
	}

	@Override
	public SubListView<E> view(final int fromIndex, final int toIndex)
	{
		// range check is done in Constructor already
		return new SubListView<>(this, fromIndex, toIndex);
	}

	@Override
	public void truncate()
	{
		this.subject.truncate();
	}

	@Override
	public int nullRemove()
	{
		return this.subject.nullRemove();
	}



	@Override
	public OldListProcessor<E> old()
	{
		return new OldListProcessor<>(this);
	}

	public static final class OldListProcessor<E> extends OldRemovingList<E>
	{
		OldListProcessor(final ListProcessor<E> list)
		{
			super(list);
		}

		@Override
		public ListProcessor<E> parent()
		{
			return (ListProcessor<E>)super.parent();
		}

	}

}
