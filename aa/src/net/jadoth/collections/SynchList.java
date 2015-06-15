package net.jadoth.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.Function;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.old.BridgeXList;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.collections.types.XImmutableList;
import net.jadoth.collections.types.XList;
import net.jadoth.concurrent.Synchronized;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.IndexProcedure;
import net.jadoth.functional.Procedure;
import net.jadoth.util.Equalator;
import net.jadoth.util.iterables.SynchronizedIterator;
import net.jadoth.util.iterables.SynchronizedListIterator;


/**
 * Synchronization wrapper class that wraps an {@link XList} instance in public synchronized delegate methods.
 *
 * @author Thomas Muenz
 *
 */
public final class SynchList<E> implements XList<E>, Synchronized
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	/**
	 * The {@link XList} instance to be wrapped (synchronized).
	 */
	private final XList<E> subject;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	/**
	 * Creates a new {@link SynchList} that wraps the passed {@link XList} instance.
	 *
	 * @param list the {@link XList} instance to be wrapped (synchronized).
	 */
	public SynchList(final XList<E> list)
	{
		super();
		this.subject = list;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public final synchronized Equalator<? super E> equality()
	{
		return this.subject.equality();
	}



	///////////////////////////////////////////////////////////////////////////
	//   add methods    //
	/////////////////////

	@Override
	public final synchronized void accept(final E e)
	{
		this.subject.accept(e);
	}

	@SafeVarargs
	@Override
	public final synchronized SynchList<E> addAll(final E... elements)
	{
		this.subject.addAll(elements);
		return this;
	}

	@Override
	public final synchronized boolean add(final E e)
	{
		return this.subject.add(e);
	}

	@Override
	public final synchronized SynchList<E> addAll(final E[] elements, final int offset, final int length)
	{
		this.subject.addAll(elements, offset, length);
		return this;
	}

	@Override
	public final synchronized SynchList<E> addAll(final XGettingCollection<? extends E> elements)
	{
		this.subject.addAll(elements);
		return this;
	}

	@Override
	public final synchronized boolean nullAdd()
	{
		return this.subject.nullAdd();
	}



	///////////////////////////////////////////////////////////////////////////
	//   put methods    //
	/////////////////////

	@Override
	public final synchronized boolean put(final E e)
	{
		return this.subject.put(e);
	}

	@SafeVarargs
	@Override
	public final synchronized SynchList<E> putAll(final E... elements)
	{
		this.subject.putAll(elements);
		return this;
	}

	@Override
	public final synchronized SynchList<E> putAll(final XGettingCollection<? extends E> elements)
	{
		this.subject.putAll(elements);
		return this;
	}

	@Override
	public final synchronized SynchList<E> putAll(final E[] elements, final int offset, final int length)
	{
		this.subject.putAll(elements, offset, length);
		return this;
	}

	@Override
	public final synchronized boolean nullPut()
	{
		return this.subject.nullPut();
	}



	///////////////////////////////////////////////////////////////////////////
	// prepend methods //
	////////////////////

	@Override
	public final synchronized boolean prepend(final E element)
	{
		return this.subject.prepend(element);
	}

	@SafeVarargs
	@Override
	public final synchronized SynchList<E> prependAll(final E... elements)
	{
		this.subject.prependAll(elements);
		return this;
	}

	@Override
	public final synchronized SynchList<E> prependAll(final E[] elements, final int offset, final int length)
	{
		this.subject.prependAll(elements, offset, length);
		return this;
	}

	@Override
	public final synchronized SynchList<E> prependAll(final XGettingCollection<? extends E> elements)
	{
		this.subject.prependAll(elements);
		return this;
	}

	@Override
	public final synchronized boolean nullPrepend()
	{
		return this.subject.nullPrepend();
	}



	///////////////////////////////////////////////////////////////////////////
	// preput methods  //
	////////////////////

	@Override
	public final synchronized boolean preput(final E element)
	{
		return this.subject.preput(element);
	}

	@SafeVarargs
	@Override
	public final synchronized SynchList<E> preputAll(final E... elements)
	{
		this.subject.preputAll(elements);
		return this;
	}

	@Override
	public final synchronized SynchList<E> preputAll(final E[] elements, final int offset, final int length)
	{
		this.subject.preputAll(elements, offset, length);
		return this;
	}

	@Override
	public final synchronized SynchList<E> preputAll(final XGettingCollection<? extends E> elements)
	{
		this.subject.preputAll(elements);
		return this;
	}

	@Override
	public final synchronized boolean nullPreput()
	{
		return this.subject.nullPreput();
	}



	///////////////////////////////////////////////////////////////////////////
	//  insert methods  //
	/////////////////////

	@Override
	public final synchronized boolean insert(final int index, final E element)
	{
		return this.subject.insert(index, element);
	}

	@SafeVarargs
	@Override
	public final synchronized int insertAll(final int index, final E... elements)
	{
		return this.subject.insertAll(index, elements);
	}

	@Override
	public final synchronized int insertAll(final int index, final E[] elements, final int offset, final int length)
	{
		return this.subject.insertAll(index, elements, offset, length);
	}

	@Override
	public final synchronized int insertAll(final int index, final XGettingCollection<? extends E> elements)
	{
		return this.subject.insertAll(index, elements);
	}

	@Override
	public final synchronized boolean nullInsert(final int index)
	{
		return this.subject.nullInsert(index);
	}



	///////////////////////////////////////////////////////////////////////////
	//  input methods   //
	/////////////////////

	@Override
	public final synchronized boolean input(final int index, final E element)
	{
		return this.subject.input(index, element);
	}

	@SafeVarargs
	@Override
	public final synchronized int inputAll(final int index, final E... elements)
	{
		return this.subject.inputAll(index, elements);
	}

	@Override
	public final synchronized int inputAll(final int index, final E[] elements, final int offset, final int length)
	{
		return this.subject.inputAll(index, elements, offset, length);
	}

	@Override
	public final synchronized int inputAll(final int index, final XGettingCollection<? extends E> elements)
	{
		return this.subject.inputAll(index, elements);
	}

	@Override
	public final synchronized boolean nullInput(final int index)
	{
		return this.subject.nullInput(index);
	}

//	@Override
//	public final synchronized <R> R aggregate(final Aggregator<? super E, R> aggregate)
//	{
//		return this.subject.iterate(aggregate);
//	}

	@Override
	public final synchronized boolean containsSearched(final Predicate<? super E> predicate)
	{
		return this.subject.containsSearched(predicate);
	}

	@Override
	public final synchronized boolean applies(final Predicate<? super E> predicate)
	{
		return this.subject.applies(predicate);
	}

	@Override
	public final synchronized void clear()
	{
		this.subject.clear();
	}

	@Override
	public final synchronized int consolidate()
	{
		return this.subject.consolidate();
	}

//	@Override
//	public final synchronized boolean contains(final E sample, final Equalator<? super E> equalator)
//	{
//		return this.subject.contains(sample, equalator);
//	}

	@Override
	public final synchronized boolean contains(final E element)
	{
		return this.subject.contains(element);
	}

	@Override
	public final synchronized boolean containsAll(final XGettingCollection<? extends E> elements)
	{
		return this.subject.containsAll(elements);
	}

//	@Override
//	public final synchronized boolean containsAll(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
//	{
//		return this.subject.containsAll(samples, equalator);
//	}

	@Override
	public final synchronized boolean containsId(final E element)
	{
		return this.subject.containsId(element);
	}

	@Override
	public final synchronized SynchList<E> copy()
	{
		return new SynchList<>(this.subject);
	}

	@Override
	public final synchronized XImmutableList<E> immure()
	{
		return this.subject.immure();
	}

	@Override
	public final synchronized <C extends Procedure<? super E>> C copySelection(final C target, final int... indices)
	{
		return this.subject.copySelection(target, indices);
	}

	@Override
	public final synchronized <C extends Procedure<? super E>> C filterTo(final C target, final Predicate<? super E> predicate)
	{
		return this.subject.filterTo(target, predicate);
	}

	@Override
	public final synchronized <T> T[] copyTo(final T[] target, final int offset)
	{
		return this.subject.copyTo(target, offset);
	}

	@Override
	public final synchronized <T> T[] copyTo(final T[] target, final int targetOffset, final int offset, final int length)
	{
		return this.subject.copyTo(target, targetOffset, offset, length);
	}

	@Override
	public final synchronized <C extends Procedure<? super E>> C copyTo(final C target)
	{
		return this.subject.copyTo(target);
	}

//	@Override
//	public final synchronized int count(final E sample, final Equalator<? super E> equalator)
//	{
//		return this.subject.count(sample, equalator);
//	}

	@Override
	public final synchronized int count(final E element)
	{
		return this.subject.count(element);
	}

	@Override
	public final synchronized int countBy(final Predicate<? super E> predicate)
	{
		return this.subject.countBy(predicate);
	}

	@Override
	public final synchronized <C extends Procedure<? super E>> C distinct(final C target, final Equalator<? super E> equalator)
	{
		return this.subject.distinct(target, equalator);
	}

	@Override
	public final synchronized <C extends Procedure<? super E>> C distinct(final C target)
	{
		return this.subject.distinct(target);
	}

	@Override
	public final synchronized SynchList<E> ensureFreeCapacity(final long minimalFreeCapacity)
	{
		this.subject.ensureFreeCapacity(minimalFreeCapacity);
		return this;
	}

	@Override
	public final synchronized SynchList<E> ensureCapacity(final long minimalCapacity)
	{
		this.subject.ensureCapacity(minimalCapacity);
		return this;
	}

	@Deprecated
	@Override
	public final synchronized boolean equals(final Object o)
	{
		return this.subject.equals(o);
	}

	@Override
	public final synchronized boolean equals(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		return this.subject.equals(samples, equalator);
	}

	@Override
	public final synchronized boolean equalsContent(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		return this.subject.equalsContent(samples, equalator);
	}

	@Override
	public final synchronized <C extends Procedure<? super E>> C except(final XGettingCollection<? extends E> other, final Equalator<? super E> equalator, final C target)
	{
		return this.subject.except(other, equalator, target);
	}

	@Override
	public final synchronized <P extends Procedure<? super E>> P iterate(final P procedure)
	{
		this.subject.iterate(procedure);
		return procedure;
	}

	@Override
	public final synchronized <P extends IndexProcedure<? super E>> P iterate(final P procedure)
	{
		this.subject.iterate(procedure);
		return procedure;
	}

	@Override
	public final synchronized <A> A join(final BiProcedure<? super E, ? super A> joiner, final A aggregate)
	{
		return this.subject.join(joiner, aggregate);
	}

	@Override
	public final synchronized SynchList<E> fill(final int offset, final int length, final E element)
	{
		this.subject.fill(offset, length, element);
		return this;
	}

	@Override
	public final synchronized E at(final int index)
	{
		return this.subject.at(index);
	}

	@Override
	public final synchronized E get()
	{
		return this.subject.get();
	}

	@Override
	public final synchronized E first()
	{
		return this.subject.first();
	}

	@Override
	public final synchronized E last()
	{
		return this.subject.last();
	}

	@Override
	public final synchronized E poll()
	{
		return this.subject.poll();
	}

	@Override
	public final synchronized E peek()
	{
		return this.subject.peek();
	}

	@Deprecated
	@Override
	public final synchronized int hashCode()
	{
		return this.subject.hashCode();
	}

	@Override
	public final synchronized boolean hasDistinctValues(final Equalator<? super E> equalator)
	{
		return this.subject.hasDistinctValues(equalator);
	}

	@Override
	public final synchronized boolean hasDistinctValues()
	{
		return this.subject.hasDistinctValues();
	}

	@Override
	public final synchronized boolean hasVolatileElements()
	{
		return this.subject.hasVolatileElements();
	}

//	@Override
//	public final synchronized int indexOf(final E sample, final Equalator<? super E> equalator)
//	{
//		return this.subject.indexOf(sample, equalator);
//	}

	@Override
	public final synchronized int indexOf(final E element)
	{
		return this.subject.indexOf(element);
	}

	@Override
	public final synchronized int indexBy(final Predicate<? super E> predicate)
	{
		return this.subject.indexBy(predicate);
	}

	@Override
	public final synchronized <C extends Procedure<? super E>> C intersect(final XGettingCollection<? extends E> other, final Equalator<? super E> equalator, final C target)
	{
		return this.subject.intersect(other, equalator, target);
	}

	@Override
	public final synchronized boolean isEmpty()
	{
		return this.subject.isEmpty();
	}

	@Override
	public final synchronized boolean isSorted(final Comparator<? super E> comparator)
	{
		return this.subject.isSorted(comparator);
	}

	@Override
	public final synchronized Iterator<E> iterator()
	{
		return new SynchronizedIterator<>(this.subject.iterator());
	}

	@Override
	public final synchronized int lastIndexBy(final Predicate<? super E> predicate)
	{
		return this.subject.lastIndexBy(predicate);
	}

	@Override
	public final synchronized int lastIndexOf(final E element)
	{
		return this.subject.lastIndexOf(element);
	}

	@Override
	public final synchronized ListIterator<E> listIterator()
	{
		return new SynchronizedListIterator<>(this.subject.listIterator());
	}

	@Override
	public final synchronized ListIterator<E> listIterator(final int index)
	{
		return new SynchronizedListIterator<>(this.subject.listIterator(index));
	}

	@Override
	public final synchronized E max(final Comparator<? super E> comparator)
	{
		return this.subject.max(comparator);
	}

	@Override
	public final synchronized int maxIndex(final Comparator<? super E> comparator)
	{
		return this.subject.maxIndex(comparator);
	}

	@Override
	public final synchronized E min(final Comparator<? super E> comparator)
	{
		return this.subject.min(comparator);
	}

	@Override
	public final synchronized int minIndex(final Comparator<? super E> comparator)
	{
		return this.subject.minIndex(comparator);
	}

	@Override
	public final synchronized <C extends Procedure<? super E>> C moveSelection(final C target, final int... indices)
	{
		return this.subject.moveSelection(target, indices);
	}

	@Override
	public final synchronized <C extends Procedure<? super E>> C moveTo(final C target, final Predicate<? super E> predicate)
	{
		return this.subject.moveTo(target, predicate);
	}

	@Override
	public final <P extends Procedure<? super E>> P process(final P procedure)
	{
		return this.subject.process(procedure);
	}

	@Override
	public final synchronized int removeBy(final Predicate<? super E> predicate)
	{
		return this.subject.removeBy(predicate);
	}

	@Override
	public final synchronized int remove(final E element)
	{
		return this.subject.remove(element);
	}

	@Override
	public final synchronized E removeAt(final int index)
	{
		return this.subject.removeAt(index);
	}

	@Override
	public final synchronized int removeAll(final XGettingCollection<? extends E> elements)
	{
		return this.subject.removeAll(elements);
	}

	@Override
	public final synchronized int removeDuplicates(final Equalator<? super E> equalator)
	{
		return this.subject.removeDuplicates(equalator);
	}

	@Override
	public final synchronized int removeDuplicates()
	{
		return this.subject.removeDuplicates();
	}

	@Override
	public final synchronized E fetch()
	{
		return this.subject.fetch();
	}

	@Override
	public final synchronized E pop()
	{
		return this.subject.pop();
	}

	@Override
	public final synchronized E pinch()
	{
		return this.subject.pinch();
	}

	@Override
	public final synchronized E pick()
	{
		return this.subject.pick();
	}

	@Override
	public final synchronized boolean removeOne(final E element)
	{
		return this.subject.removeOne(element);
	}

//	@Override
//	public final synchronized boolean removeOne(final E sample, final Equalator<? super E> equalator)
//	{
//		return this.subject.removeOne(sample, equalator);
//	}

	@Override
	public final synchronized E retrieve(final E element)
	{
		return this.subject.retrieve(element);
	}

	@Override
	public final synchronized E retrieveBy(final Predicate<? super E> predicate)
	{
		return this.subject.retrieveBy(predicate);
	}

	@Override
	public final synchronized SynchList<E> removeRange(final int startIndex, final int length)
	{
		this.subject.removeRange(startIndex, length);
		return this;
	}

	@Override
	public final synchronized SynchList<E> retainRange(final int startIndex, final int length)
	{
		this.subject.retainRange(startIndex, length);
		return this;
	}

	@Override
	public final synchronized int removeSelection(final int[] indices)
	{
		return this.subject.removeSelection(indices);
	}

//	@Override
//	public final synchronized int replace(final E oldElement, final Equalator<? super E> equalator, final E replacement)
//	{
//		return this.subject.replace(oldElement, equalator, replacement);
//	}

	@Override
	public final synchronized int replace(final E element, final E replacement)
	{
		return this.subject.replace(element, replacement);
	}

	@Override
	public final synchronized int replace(final Predicate<? super E> predicate, final E substitute)
	{
		return this.subject.replace(predicate, substitute);
	}

//	@Override
//	public final synchronized int replace(final CtrlPredicate<? super E> predicate, final E substitute)
//	{
//		return this.subject.replace(predicate, substitute);
//	}

	@Override
	public final synchronized int modify(final Function<E, E> mapper)
	{
		return this.subject.modify(mapper);
	}

	@Override
	public final int modify(final Predicate<? super E> predicate, final Function<E, E> mapper)
	{
		return this.subject.modify(predicate, mapper);
	}

//	@Override
//	public final int modify(final CtrlPredicate<? super E> predicate, final Function<E, E> mapper)
//	{
//		return this.subject.modify(predicate, mapper);
//	}

//	@Override
//	public final synchronized int replaceAll(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator, final E newElement)
//	{
//		return this.subject.replaceAll(samples, equalator, newElement);
//	}

	@Override
	public final synchronized int replaceAll(final XGettingCollection<? extends E> elements, final E replacement)
	{
		return this.subject.replaceAll(elements, replacement);
	}

//	@Override
//	public final synchronized boolean replaceOne(final E sample, final Equalator<? super E> equalator, final E replacement)
//	{
//		return this.subject.replaceOne(sample, equalator, replacement);
//	}

	@Override
	public final synchronized boolean replaceOne(final E element, final E replacement)
	{
		return this.subject.replaceOne(element, replacement);
	}

	@Override
	public final synchronized boolean replaceOne(final Predicate<? super E> predicate, final E substitute)
	{
		return this.subject.replaceOne(predicate, substitute);
	}

//	@Override
//	public final synchronized int retainAll(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
//	{
//		return this.subject.retainAll(samples, equalator);
//	}

	@Override
	public final synchronized int retainAll(final XGettingCollection<? extends E> elements)
	{
		return this.subject.retainAll(elements);
	}

	@Override
	public final synchronized SynchList<E> reverse()
	{
		this.subject.reverse();
		return this;
	}

	@Override
	public final synchronized int scan(final Predicate<? super E> predicate)
	{
		return this.subject.scan(predicate);
	}

//	@Override
//	public final synchronized E search(final E sample, final Equalator<? super E> equalator)
//	{
//		return this.subject.search(sample, equalator);
//	}

	@Override
	public final synchronized E seek(final E sample)
	{
		return this.subject.seek(sample);
	}

	@Override
	public final synchronized E search(final Predicate<? super E> predicate)
	{
		return this.subject.search(predicate);
	}

	@SafeVarargs
	@Override
	public final synchronized SynchList<E> setAll(final int offset, final E... elements)
	{
		this.subject.setAll(offset, elements);
		return this;
	}

	@Override
	public final synchronized boolean set(final int index, final E element)
	{
		return this.subject.set(index, element);
	}

	@Override
	public final synchronized E setGet(final int index, final E element)
	{
		return this.subject.setGet(index, element);
	}

	@Override
	public final synchronized SynchList<E> set(final int offset, final E[] src, final int srcIndex, final int srcLength)
	{
		this.subject.set(offset, src, srcIndex, srcLength);
		return this;
	}

	@Override
	public final synchronized SynchList<E> set(final int offset, final XGettingSequence<? extends E> elements, final int elementsOffset, final int elementsLength)
	{
		this.subject.set(offset, elements, elementsOffset, elementsLength);
		return this;
	}

	@Override
	public final synchronized void setFirst(final E element)
	{
		this.subject.setFirst(element);
	}

	@Override
	public final synchronized void setLast(final E element)
	{
		this.subject.setLast(element);
	}

	@Override
	public final synchronized int optimize()
	{
		return this.subject.optimize();
	}

	@Override
	public final synchronized long size()
	{
		return Jadoth.to_int(this.subject.size());
	}

	@Override
	public final synchronized SynchList<E> sort(final Comparator<? super E> comparator)
	{
		this.subject.sort(comparator);
		return this;
	}

	@Override
	public final synchronized XList<E> range(final int fromIndex, final int toIndex)
	{
		// (28.01.2011 TM)NOTE: not sure if this is sufficient ^^
		return new SynchList<>(this.subject.range(fromIndex, toIndex));
	}

	@Override
	public final synchronized SynchList<E> shiftTo(final int sourceIndex, final int targetIndex)
	{
		this.subject.shiftTo(sourceIndex, targetIndex);
		return this;
	}

	@Override
	public final synchronized SynchList<E> shiftTo(final int sourceIndex, final int targetIndex, final int length)
	{
		this.subject.shiftTo(sourceIndex, targetIndex, length);
		return this;
	}

	@Override
	public final synchronized SynchList<E> shiftBy(final int sourceIndex, final int distance)
	{
		this.subject.shiftTo(sourceIndex, distance);
		return this;
	}

	@Override
	public final synchronized SynchList<E> shiftBy(final int sourceIndex, final int distance, final int length)
	{
		this.subject.shiftTo(sourceIndex, distance, length);
		return this;
	}

	@Override
	public final synchronized SynchList<E> swap(final int indexA, final int indexB, final int length)
	{
		this.subject.swap(indexA, indexB, length);
		return this;
	}

	@Override
	public final synchronized SynchList<E> swap(final int indexA, final int indexB)
	{
		this.subject.swap(indexA, indexB);
		return this;
	}

	@Override
	public final synchronized Object[] toArray()
	{
		return this.subject.toArray();
	}

	@Override
	public final synchronized E[] toArray(final Class<E> type)
	{
		return this.subject.toArray(type);
	}

	@Override
	public final synchronized SynchList<E> toReversed()
	{
		this.subject.toReversed();
		return this;
	}

	@Override
	public final synchronized void truncate()
	{
		this.subject.truncate();
	}

	@Override
	public final synchronized <C extends Procedure<? super E>> C union(final XGettingCollection<? extends E> other, final Equalator<? super E> equalator, final C target)
	{
		return this.subject.union(other, equalator, target);
	}

	@Override
	public final synchronized long currentCapacity()
	{
		return this.subject.currentCapacity();
	}

	@Override
	public final synchronized long maximumCapacity()
	{
		return this.subject.maximumCapacity();
	}

	@Override
	public final synchronized boolean isFull()
	{
		return this.subject.isFull();
	}

	@Override
	public final synchronized long remainingCapacity()
	{
		return this.subject.remainingCapacity();
	}

	@Override
	public final synchronized boolean nullAllowed()
	{
		return this.subject.nullAllowed();
	}

	@Override
	public final synchronized boolean nullContained()
	{
		return this.subject.nullContained();
	}

	@Override
	public final synchronized int nullRemove()
	{
		return this.subject.nullRemove();
	}

	@Override
	public final synchronized ListView<E> view()
	{
		return new ListView<>(this);
	}

	@Override
	public final synchronized SubListView<E> view(final int fromIndex, final int toIndex)
	{
		return new SubListView<>(this, fromIndex, toIndex);
	}



	@Override
	public final OldSynchList<E> old()
	{
		return new OldSynchList<>(this);
	}

	public static final class OldSynchList<E> extends BridgeXList<E>
	{
		OldSynchList(final SynchList<E> list)
		{
			super(list);
		}

		@Override
		public final SynchList<E> parent()
		{
			return (SynchList<E>)super.parent();
		}

	}

}
