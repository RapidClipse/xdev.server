package net.jadoth.collections;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.types.XDecreasingList;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.collections.types.XProcessingList;
import net.jadoth.collections.types.XSortableSequence;
import net.jadoth.functional.Procedure;
import net.jadoth.util.Equalator;

/**
 * @author Thomas Muenz
 *
 */
public class SubListProcessor<E> extends SubListView<E> implements XDecreasingList<E>
{
	/* (12.07.2012 TM)FIXME: complete SubListProcessor implementation
	 * See all "FIX-ME"s
	 */

	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	public SubListProcessor(final XProcessingList<E> list, final int fromIndex, final int toIndex)
	{
		super(list, fromIndex, toIndex);
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	private void internalClear()
	{
		this.size = 0;
		this.length = 0;
		this.d = 1;
	}

	private void decrement()
	{
		this.size -= 1;
		this.length -= this.d;
	}

	private void decrement(final long amount)
	{
		this.size -= amount;
		this.length -= amount*this.d;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public final void clear()
	{
		((XProcessingList<E>)this.list).removeRange(this.startIndex, this.length);
		this.internalClear();
	}

	@Override
	public final <P extends Procedure<? super E>> P process(final P procedure)
	{
		final long oldListSize = ((XProcessingList<E>)this.list).size();
//		((XRemovingList<E>)this.list).rngProcess(this.startIndex, this.length, procedure);
		this.decrement(oldListSize - ((XProcessingList<E>)this.list).size());
		return procedure;
	}

	@Override
	public final int removeDuplicates(final Equalator<? super E> equalator)
	{
		final long removeCount, oldListSize = ((XProcessingList<E>)this.list).size();
//		((XRemovingList<E>)this.list).rngRemoveDuplicates(this.startIndex, this.length, keepMultipleNullValues, equalator);
		this.decrement(removeCount = oldListSize - ((XProcessingList<E>)this.list).size());
		return Jadoth.to_int(removeCount);
	}

	@Override
	public final int remove(final E element)
	{
		final long removeCount, oldListSize = ((XProcessingList<E>)this.list).size();
//		((XRemovingList<E>)this.list).rngRemove(this.startIndex, this.length, element, 0, null);
		this.decrement(removeCount = oldListSize - ((XProcessingList<E>)this.list).size());
		return Jadoth.to_int(removeCount);
	}

	@Override
	public final int removeAll(final XGettingCollection<? extends E> samples)
	{
		final long removeCount, oldListSize = ((XProcessingList<E>)this.list).size();
//		((XRemovingList<E>)this.list).rngRemoveAll(this.startIndex, this.length, collection, 0, null);
		this.decrement(removeCount = oldListSize - ((XProcessingList<E>)this.list).size());
		return Jadoth.to_int(removeCount);
	}

	@Override
	public final int removeDuplicates()
	{
		final long removeCount, oldListSize = ((XProcessingList<E>)this.list).size();
//		((XRemovingList<E>)this.list).rngRemoveDuplicates(this.startIndex, this.length, keepMultipleNullValues);
		this.decrement(removeCount = oldListSize - ((XProcessingList<E>)this.list).size());
		return Jadoth.to_int(removeCount);
	}

	@Override
	public final E retrieve(final E element)
	{
		final int oldListSize = Jadoth.to_int(this.list.size());
		final E e = XUtilsCollection.rngRetrieve((XProcessingList<E>)this.list, this.startIndex, this.length, element);
		this.decrement(oldListSize - Jadoth.to_int(this.list.size()));
		return e;
	}

	@Override
	public final E retrieveBy(final Predicate<? super E> predicate)
	{
		final int oldListSize = Jadoth.to_int(this.list.size());
		final E e = XUtilsCollection.rngRetrieve((XProcessingList<E>)this.list, this.startIndex, this.length, predicate);
		this.decrement(oldListSize - Jadoth.to_int(this.list.size()));
		return e;
	}

	@Override
	public final boolean removeOne(final E element)
	{
		if(XUtilsCollection.rngRemoveOne((XProcessingList<E>)this.list, this.startIndex, this.length, element)){
			this.decrement();
			return true;
		}
		return false;
	}

//	@Override
//	public final boolean removeOne(final E sample, final Equalator<? super E> equalator)
//	{
//		if(XUtilsCollection.rngRemoveOne((XProcessingList<E>)this.list, this.startIndex, this.length, sample, equalator)){
//			this.decrement();
//			return true;
//		}
//		return false;
//	}

	@Override
	public final int retainAll(final XGettingCollection<? extends E> samples)
	{
		final long removeCount, oldListSize = ((XProcessingList<E>)this.list).size();
//		((XRemovingList<E>)this.list).rngRetainAll(this.startIndex, this.length, collection);
		this.decrement(removeCount = oldListSize - ((XProcessingList<E>)this.list).size());
		return Jadoth.to_int(removeCount);
	}

	@Override
	public final int removeBy(final Predicate<? super E> predicate)
	{
		final long removeCount, oldListSize = ((XProcessingList<E>)this.list).size();
//		((XRemovingList<E>)this.list).rngReduce(this.startIndex, this.length, predicate, 0, null);
		this.decrement(removeCount = oldListSize - ((XProcessingList<E>)this.list).size());
		return Jadoth.to_int(removeCount);
	}

	@Override
	public final void truncate()
	{
		((XProcessingList<E>)this.list).removeRange(this.startIndex, this.length);
		this.internalClear();
	}

	@Override
	public final SubListProcessor<E> range(final int fromIndex, final int toIndex)
	{
		this.checkRange(fromIndex, toIndex);
		return new SubListProcessor<>(
			(XProcessingList<E>)this.list,
			this.startIndex + fromIndex*this.d,
			this.startIndex + toIndex*this.d
		);
	}

	@Override
	public final int consolidate()
	{
		return ((XProcessingList<E>)this.list).consolidate() > 0 ?1 :0;
	}

	@Override
	public final <C extends Procedure<? super E>> C moveTo(final C target, final Predicate<? super E> predicate)
	{
		final long oldListSize = ((XProcessingList<E>)this.list).size();
//		((XRemovingList<E>)this.list).rngMoveTo(this.startIndex, this.length, target, predicate, 0, null);
		this.decrement(oldListSize - ((XProcessingList<E>)this.list).size());
		return target;
	}

	@Override
	public final int optimize()
	{
		return ((XProcessingList<E>)this.list).optimize();
	}

	@Override
	public final <C extends Procedure<? super E>> C moveSelection(final C target, final int... indices)
	{
		final long oldListSize = ((XProcessingList<E>)this.list).size();
		((XProcessingList<E>)this.list).moveSelection(target, this.shiftIndices(indices));
		this.decrement(oldListSize - ((XProcessingList<E>)this.list).size());
		return target;
	}

	@Override
	public final E removeAt(final int index) throws UnsupportedOperationException
	{
		this.checkIndex(index);
		final E element = ((XProcessingList<E>)this.list).removeAt(index);
		this.decrement();
		return element;
	}

	@Override
	public final E fetch()
	{
		return this.removeAt(0);
	}

	@Override
	public final E pop()
	{
		return this.removeAt(this.getEndIndex());
	}

	@Override
	public final E pinch()
	{
		return this.size == 0 ?null :this.removeAt(0);
	}

	@Override
	public final E pick()
	{
		return this.size == 0 ?null :this.removeAt(this.getEndIndex());
	}

	@Override
	public final SubListProcessor<E> removeRange(final int startIndex, final int length)
	{
		this.checkVector(startIndex, length);
		final int oldListSize = Jadoth.to_int(this.list.size());
		((XProcessingList<E>)this.list).removeRange(this.startIndex + startIndex*this.d, length*this.d);
		this.decrement(oldListSize - Jadoth.to_int(this.list.size()));
		return this;
	}

	@Override
	public final SubListProcessor<E> retainRange(final int startIndex, final int length)
	{
		this.checkVector(startIndex, length);
		final int oldListSize = Jadoth.to_int(this.list.size());
		((XProcessingList<E>)this.list).retainRange(this.startIndex + startIndex*this.d, length*this.d);
		this.decrement(oldListSize - Jadoth.to_int(this.list.size()));
		return this;
	}

	@Override
	public final int removeSelection(final int[] indices)
	{
		final int removeCount, oldListSize = Jadoth.to_int(this.list.size());
		((XProcessingList<E>)this.list).removeSelection(this.shiftIndices(indices));
		this.decrement(removeCount = oldListSize - Jadoth.to_int(this.list.size()));
		return Jadoth.to_int(removeCount);
	}

	@Override
	public final SubListProcessor<E> toReversed()
	{
		return new SubListProcessor<>((XProcessingList<E>)this.list, this.getEndIndex(), this.startIndex);
	}

	@Override
	public final SubListProcessor<E> copy()
	{
		return new SubListProcessor<>((XProcessingList<E>)this.list, this.startIndex, this.getEndIndex());
	}

	@Override
	public final int nullRemove()
	{
		final long removeCount, oldListSize = ((XProcessingList<E>)this.list).size();
//		((XRemovingList<E>)this.list).rngRemoveNull(this.startIndex, this.length);

		this.decrement(removeCount = oldListSize - ((XProcessingList<E>)this.list).size());
		return Jadoth.to_int(removeCount);
	}

	@Override
	public final SubListView<E> view(final int fromIndex, final int toIndex)
	{
		this.checkRange(fromIndex, toIndex);
		return new SubListView<>(this.list, this.startIndex + fromIndex*this.d, this.startIndex + toIndex*this.d);
	}


	@Override
	public final OldSubListProcessor<E> old()
	{
		return new OldSubListProcessor<>(this);
	}

	static class OldSubListProcessor<E> extends OldSubListView<E>
	{
		OldSubListProcessor(final SubListProcessor<E> list)
		{
			super(list);
		}

		@Override
		public final SubListProcessor<E> parent()
		{
			return (SubListProcessor<E>)super.parent();
		}

	}

	@Override
	public final boolean replaceOne(final E element, final E replacement)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

//	@Override
//	public final boolean replaceOne(final E sample, final Equalator<? super E> equalator, final E replacement)
//	{
//		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
//	}

	@Override
	public final int replace(final E element, final E replacement)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

//	@Override
//	public final int replace(final E sample, final Equalator<? super E> equalator, final E replacement)
//	{
//		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
//	}

	@Override
	public final int replaceAll(final XGettingCollection<? extends E> elements, final E replacement)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

//	@Override
//	public final int replaceAll(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator, final E replacement)
//	{
//		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
//	}

	@Override
	public final boolean replaceOne(final Predicate<? super E> predicate, final E substitute)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final int replace(final Predicate<? super E> predicate, final E substitute)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

//	@Override
//	public final int replace(final CtrlPredicate<? super E> predicate, final E substitute)
//	{
//		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
//	}

	@Override
	public final int modify(final Function<E, E> mapper)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final int modify(final Predicate<? super E> predicate, final Function<E, E> mapper)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

//	@Override
//	public final int modify(final CtrlPredicate<? super E> predicate, final Function<E, E> mapper)
//	{
//		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
//	}

	@Override
	public final boolean set(final int index, final E element) throws IndexOutOfBoundsException, ArrayIndexOutOfBoundsException
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final E setGet(final int index, final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final void setFirst(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final void setLast(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final XSortableSequence<E> shiftTo(final int sourceIndex, final int targetIndex)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final XSortableSequence<E> shiftTo(final int sourceIndex, final int targetIndex, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final XSortableSequence<E> shiftBy(final int sourceIndex, final int distance)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final XSortableSequence<E> shiftBy(final int sourceIndex, final int distance, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@SafeVarargs
	@Override
	public final XDecreasingList<E> setAll(final int index, final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final XDecreasingList<E> set(final int index, final E[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final XDecreasingList<E> set(final int index, final XGettingSequence<? extends E> elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final XDecreasingList<E> swap(final int indexA, final int indexB)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final XDecreasingList<E> swap(final int indexA, final int indexB, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final XDecreasingList<E> reverse()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final XDecreasingList<E> fill(final int offset, final int length, final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final XDecreasingList<E> sort(final Comparator<? super E> comparator)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

}