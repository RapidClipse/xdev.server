
package net.jadoth.collections;

import java.util.Comparator;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.collections.types.XList;
import net.jadoth.collections.types.XProcessingList;
import net.jadoth.collections.types.XSettingList;
import net.jadoth.functional.Procedure;
import net.jadoth.util.Equalator;


public final class SubList<E> extends SubListAccessor<E> implements XList<E>
{
	/* (12.07.2012 TM)FIXME: complete SubList implementation
	 * See all "FIX-ME"s
	 * remove redundant method implementations
	 */

	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	public SubList(final XList<E> list, final int fromIndex, final int toIndex)
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

	private void increment()
	{
		this.size += 1;
		this.length += this.d;
	}

	private void increment(final long amount)
	{
		this.size += amount;
		this.length += amount*this.d;
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
	public final long currentCapacity()
	{
		return ((XList<E>)this.list).currentCapacity();
	}

	@Override
	public final void clear()
	{
		((XList<E>)this.list).removeRange(this.startIndex, this.length);
		this.internalClear();
	}




	///////////////////////////////////////////////////////////////////////////
	//   add methods    //
	/////////////////////

	@Override
	public final void accept(final E element)
	{
		this.add(element);
	}

	@Override
	public final boolean add(final E e)
	{
		((XList<E>)this.list).input(this.startIndex + this.length, e);
		this.increment();
		return true;
	}

	@SafeVarargs
	@Override
	public final SubList<E> addAll(final E... elements)
	{
		((XList<E>)this.list).inputAll(this.startIndex + this.length, elements);
		this.increment(elements.length);
		return this;
	}

	@Override
	public final SubList<E> addAll(final XGettingCollection<? extends E> elements)
	{
		final int oldListSize = Jadoth.to_int(this.list.size());
		((XList<E>)this.list).inputAll(this.startIndex + this.length, elements);
		this.increment(((XList<E>)this.list).size() - oldListSize);
		return this;
	}

	@Override
	public final SubList<E> addAll(final E[] elements, final int offset, final int length)
	{
		final int oldListSize = Jadoth.to_int(this.list.size());
		((XList<E>)this.list).inputAll(this.getEndIndex(), elements, offset, length);
		this.increment(((XList<E>)this.list).size() - oldListSize);
		return this;
	}

	@Override
	public final boolean nullAdd()
	{
		((XList<E>)this.list).input(this.startIndex + this.length, (E)null);
		this.increment();
		return true;
	}



	///////////////////////////////////////////////////////////////////////////
	//   put methods    //
	/////////////////////

	@Override
	public final boolean put(final E element)
	{
		return this.add(element);
	}

	@SafeVarargs
	@Override
	public final SubList<E> putAll(final E... elements)
	{
		return this.addAll(elements);
	}

	@Override
	public final SubList<E> putAll(final E[] elements, final int offset, final int length)
	{
		return this.addAll(elements, offset, length);
	}

	@Override
	public final SubList<E> putAll(final XGettingCollection<? extends E> elements)
	{
		return elements.copyTo(this);
	}

	@Override
	public final boolean nullPut()
	{
		return this.nullAdd();
	}



	///////////////////////////////////////////////////////////////////////////
	// prepend methods //
	////////////////////

	@Override
	public final boolean prepend(final E element)
	{
		if(this.d > 0){
			if(((XList<E>)this.list).insert(this.startIndex, element)){
				this.startIndex--;
				this.increment();
				return true;
			}

		}
		else {
			if(((XList<E>)this.list).insert(this.startIndex+1, element)){
				this.startIndex++;
				this.increment();
				return true;
			}
		}
		return false;
	}

	@SafeVarargs
	@Override
	public final SubList<E> prependAll(final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final SubList<E> prependAll(final E[] elements, final int srcStartIndex, final int srcLength)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final SubList<E> prependAll(final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final boolean nullPrepend()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}



	///////////////////////////////////////////////////////////////////////////
	// preput methods  //
	////////////////////

	@Override
	public final boolean preput(final E element)
	{
		if(this.d > 0){
			((XList<E>)this.list).input(this.startIndex, element);
			this.startIndex--;

		}
		else {
			((XList<E>)this.list).input(this.startIndex+1, element);
			this.startIndex++;
		}
		this.increment();
		return true;
	}

	@SafeVarargs
	@Override
	public final SubList<E> preputAll(final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final SubList<E> preputAll(final E[] elements, final int srcStartIndex, final int srcLength)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final SubList<E> preputAll(final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final boolean nullPreput()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}



	///////////////////////////////////////////////////////////////////////////
	//  insert methods  //
	/////////////////////

	@Override
	public final boolean insert(final int index, final E element)
	{
		this.checkIndex(index);
		return ((XList<E>)this.list).insert(this.startIndex + index*this.d, element);
	}

	@SafeVarargs
	@Override
	public final int insertAll(final int index, final E... elements)
	{
		this.checkIndex(index);
		final int oldListSize = Jadoth.to_int(this.list.size());

		if(this.d > 0){
			((XList<E>)this.list).insertAll(this.startIndex + index*+1, elements);
		}
		else {
			((XList<E>)this.list).insertAll(this.startIndex + index*-1, JadothArrays.toReversed(elements));
		}
		final long increase;
		this.increment(increase = ((XList<E>)this.list).size() - oldListSize);
		return Jadoth.to_int(increase);
	}

	@Override
	public final int insertAll(final int index, final E[] elements, final int offset, final int length)
	{
		this.checkIndex(index);
		final int oldListSize = Jadoth.to_int(this.list.size());

		if(this.d > 0){
			((XList<E>)this.list).insertAll(this.startIndex + index*+1, elements, offset, length);
		}
		else {
			((XList<E>)this.list).insertAll(this.startIndex + index*-1, JadothArrays.toReversed(elements, offset, length));
		}
		final long increase;
		this.increment(increase = ((XList<E>)this.list).size() - oldListSize);
		return Jadoth.to_int(increase);
	}

	@Override
	public final int insertAll(final int index, final XGettingCollection<? extends E> elements)
	{
		final int oldListSize = Jadoth.to_int(this.list.size());
		((XList<E>)this.list).insertAll(this.startIndex + index*this.d, elements);
		final long increase;
		this.increment(increase = ((XList<E>)this.list).size() - oldListSize);
		return Jadoth.to_int(increase);
	}

	@Override
	public final boolean nullInsert(final int index)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}



	///////////////////////////////////////////////////////////////////////////
	//  input methods   //
	/////////////////////

	@Override
	public final boolean input(final int index, final E element)
	{
		this.checkIndex(index);
		return ((XList<E>)this.list).input(this.startIndex + index*this.d, element);
	}

	@SafeVarargs
	@Override
	public final int inputAll(final int index, final E... elements)
	{
		this.checkIndex(index);
		final int oldListSize = Jadoth.to_int(this.list.size());

		if(this.d > 0){
			((XList<E>)this.list).inputAll(this.startIndex + index*+1, elements);
		}
		else {
			((XList<E>)this.list).inputAll(this.startIndex + index*-1, JadothArrays.toReversed(elements));
		}
		final long increase;
		this.increment(increase = ((XList<E>)this.list).size() - oldListSize);
		return Jadoth.to_int(increase);
	}

	@Override
	public final int inputAll(final int index, final E[] elements, final int offset, final int length)
	{
		this.checkIndex(index);
		final int oldListSize = Jadoth.to_int(this.list.size());

		if(this.d > 0){
			((XList<E>)this.list).inputAll(this.startIndex + index*+1, elements, offset, length);
		}
		else {
			((XList<E>)this.list).inputAll(this.startIndex + index*-1, JadothArrays.toReversed(elements, offset, length));
		}
		final long increase;
		this.increment(increase = ((XList<E>)this.list).size() - oldListSize);
		return Jadoth.to_int(increase);
	}

	@Override
	public final int inputAll(final int index, final XGettingCollection<? extends E> elements)
	{
		final int oldListSize = Jadoth.to_int(this.list.size());
		((XList<E>)this.list).inputAll(this.startIndex + index*this.d, elements);
		final long increase;
		this.increment(increase = ((XList<E>)this.list).size() - oldListSize);
		return Jadoth.to_int(increase);
	}

	@Override
	public final boolean nullInput(final int index)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final <P extends Procedure<? super E>> P process(final P procedure)
	{
		final int oldListSize = Jadoth.to_int(this.list.size());
		XUtilsCollection.rngProcess((XProcessingList<E>)this.list, this.startIndex, this.length, procedure);
		this.decrement(oldListSize - Jadoth.to_int(this.list.size()));
		return procedure;
	}

	@Override
	public final int removeDuplicates(final Equalator<? super E> equalator)
	{
		final long removeCount, oldListSize = ((XList<E>)this.list).size();
		XUtilsCollection.rngRemoveDuplicates((XProcessingList<E>)this.list, this.startIndex, this.length, equalator);
		this.decrement(removeCount = oldListSize - Jadoth.to_int(this.list.size()));
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

//	@Override
//	public final int retainAll(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
//	{
//		final long removeCount, oldListSize = ((XList<E>)this.list).size();
//		XUtilsCollection.rngRetainAll((XProcessingList<E>)this.list, this.startIndex, this.length, samples, equalator);
//		this.decrement(removeCount = oldListSize - this.list.size());
//		return Jadoth.to_int(removeCount);
//	}

	@Override
	public final int remove(final E element)
	{
		final long removeCount, oldListSize = ((XList<E>)this.list).size();
		XUtilsCollection.rngRemove((XProcessingList<E>)this.list, this.startIndex, this.length, element);
		this.decrement(removeCount = oldListSize - Jadoth.to_int(this.list.size()));
		return Jadoth.to_int(removeCount);
	}

	@Override
	public final int removeAll(final XGettingCollection<? extends E> elements)
	{
		final long removeCount, oldListSize = ((XList<E>)this.list).size();
		XUtilsCollection.rngRemoveAll((XProcessingList<E>)this.list, this.startIndex, this.length, elements);
		this.decrement(removeCount = oldListSize - Jadoth.to_int(this.list.size()));
		return Jadoth.to_int(removeCount);
	}

	@Override
	public final int removeDuplicates()
	{
		final long removeCount, oldListSize = ((XList<E>)this.list).size();
		XUtilsCollection.rngRemoveDuplicates((XProcessingList<E>)this.list, this.startIndex, this.length);
		this.decrement(removeCount = oldListSize - Jadoth.to_int(this.list.size()));
		return Jadoth.to_int(removeCount);
	}

	@Override
	public final int retainAll(final XGettingCollection<? extends E> elements)
	{
		final long removeCount, oldListSize = ((XList<E>)this.list).size();
		XUtilsCollection.rngRetainAll((XProcessingList<E>)this.list, this.startIndex, this.length, elements);
		this.decrement(removeCount = oldListSize - Jadoth.to_int(this.list.size()));
		return Jadoth.to_int(removeCount);
	}

	@Override
	public final int removeBy(final Predicate<? super E> predicate)
	{
		final long removeCount, oldListSize = ((XList<E>)this.list).size();
		XUtilsCollection.rngReduce((XProcessingList<E>)this.list, this.startIndex, this.length, predicate);
		this.decrement(removeCount = oldListSize - Jadoth.to_int(this.list.size()));
		return Jadoth.to_int(removeCount);
	}

	@Override
	public final void truncate()
	{
		((XList<E>)this.list).removeRange(this.startIndex, this.length);
		this.internalClear();
	}

	@Override
	public final SubList<E> range(final int fromIndex, final int toIndex)
	{
		this.checkRange(fromIndex, toIndex);
		return new SubList<>(
			(XList<E>)this.list,
			this.startIndex + fromIndex*this.d,
			this.startIndex + toIndex*this.d
		);
	}





	@Override
	public final SubList<E> ensureFreeCapacity(final long minimalFreeCapacity)
	{
		((XList<E>)this.list).ensureFreeCapacity(minimalFreeCapacity);
		return this;
	}

	@Override
	public final SubList<E> ensureCapacity(final long minimalCapacity)
	{
		((XList<E>)this.list).ensureCapacity(minimalCapacity + this.size);
		return this;
	}

	@Override
	public final int consolidate()
	{
		return ((XList<E>)this.list).consolidate() > 0 ?1 :0;
	}

	@Override
	public final <C extends Procedure<? super E>> C moveTo(final C target, final Predicate<? super E> predicate)
	{
		final int oldListSize = Jadoth.to_int(this.list.size());
		XUtilsCollection.rngMoveTo((XProcessingList<E>)this.list, this.startIndex, this.length, target, predicate);
		this.decrement(oldListSize - Jadoth.to_int(this.list.size()));
		return target;
	}

	@Override
	public final int optimize()
	{
		return ((XList<E>)this.list).optimize();
	}





	@Override
	public final <C extends Procedure<? super E>> C moveSelection(final C target, final int... indices)
	{
		final int oldListSize = Jadoth.to_int(this.list.size());
		((XList<E>)this.list).moveSelection(target, this.shiftIndices(indices));
		this.decrement(oldListSize - Jadoth.to_int(this.list.size()));
		return target;
	}

	@Override
	public final E removeAt(final int index) throws UnsupportedOperationException
	{
		this.checkIndex(index);
		final E element = ((XList<E>)this.list).removeAt(index);
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
	public final SubList<E> removeRange(final int startIndex, final int length)
	{
		this.checkVector(startIndex, length);
		final int oldListSize = Jadoth.to_int(this.list.size());
		((XList<E>)this.list).removeRange(this.startIndex + startIndex*this.d, length*this.d);
		this.decrement(oldListSize - Jadoth.to_int(this.list.size()));
		return this;
	}

	@Override
	public final SubList<E> retainRange(final int startIndex, final int length)
	{
		this.checkVector(startIndex, length);
		final int oldListSize = Jadoth.to_int(this.list.size());
		((XList<E>)this.list).retainRange(this.startIndex + startIndex*this.d, length*this.d);
		this.decrement(oldListSize - Jadoth.to_int(this.list.size()));
		return this;
	}

	@Override
	public final int removeSelection(final int[] indices)
	{
		final long removeCount, oldListSize = ((XList<E>)this.list).size();
		((XList<E>)this.list).removeSelection(this.shiftIndices(indices));
		this.decrement(removeCount = oldListSize - Jadoth.to_int(this.list.size()));
		return Jadoth.to_int(removeCount);
	}

	@Override
	public final SubList<E> toReversed()
	{
		return new SubList<>((XList<E>)this.list, this.getEndIndex(), this.startIndex);
	}

	@Override
	public final SubList<E> copy()
	{
		return new SubList<>((XList<E>)this.list, this.startIndex, this.getEndIndex());
	}

	@Override
	public final int nullRemove()
	{
		final long removeCount, oldListSize = ((XList<E>)this.list).size();
		XUtilsCollection.rngRemoveNull((XProcessingList<E>)this.list, this.startIndex, this.length);
		this.decrement(removeCount = oldListSize - Jadoth.to_int(this.list.size()));
		return Jadoth.to_int(removeCount);
	}

	@Override
	public final SubList<E> sort(final Comparator<? super E> comparator)
	{
		XUtilsCollection.rngSort((XSettingList<E>)this.list, this.startIndex, this.length, comparator);
		return this;
	}

	@Override
	public final SubList<E> swap(final int indexA, final int indexB)
	{
		this.checkIndex(indexA);
		this.checkIndex(indexB);
		((XList<E>)this.list).swap(this.startIndex + indexA*this.d, this.startIndex + indexB*this.d);
		return this;
	}

	@Override
	public final SubList<E> swap(final int indexA, final int indexB, final int length)
	{
		this.checkVector(indexA, length);
		this.checkVector(indexB, length);
		((XList<E>)this.list).swap(this.startIndex + indexA*this.d, this.startIndex + indexB*this.d, length*this.d);
		return this;
	}

	@SafeVarargs
	@Override
	public final SubList<E> setAll(final int offset, final E... elements)
	{
		super.setAll(offset, elements);
		return this;
	}

	@Override
	public final SubList<E> set(final int offset, final E[] src, final int srcIndex, final int srcLength)
	{
		super.set(offset, src, srcIndex, srcLength);
		return this;
	}

	@Override
	public final SubList<E> set(final int offset, final XGettingSequence<? extends E> elements, final int elementsOffset, final int elementsLength)
	{
		this.checkVector(offset, elementsLength);
		if(this.d > 0){
			((XList<E>)this.list).set(this.startIndex + offset*+1, elements, elementsOffset, elementsLength);
		}
		else {
			final int revElementsStartIndex;
			if(elementsLength == 0){
				revElementsStartIndex = elementsOffset;
			}
			else if(elementsLength > 0){
				revElementsStartIndex = elementsOffset + elementsLength - 1;
			}
			else {
				revElementsStartIndex = elementsOffset + elementsLength + 1;
			}
			((XList<E>)this.list).set(this.startIndex + offset*-1, elements, revElementsStartIndex, -elementsLength);
		}
		return this;
	}

	@Override
	public final SubList<E> fill(final int offset, final int length, final E element)
	{
		this.checkVector(offset, length);
		((XList<E>)this.list).fill(this.startIndex + offset*this.d, length*this.d, element);
		return this;
	}


	@Override
	public final OldSubList<E> old()
	{
		return new OldSubList<>(this);
	}

	static class OldSubList<E> extends OldSubListAccessor<E>
	{
		OldSubList(final SubList<E> list)
		{
			super(list);
		}

		@Override
		public final SubList<E> parent()
		{
			return (SubList<E>)super.parent();
		}

	}

//	@Override
//	public final SubList<E> iterate(final CtrlProcedure<? super E> procedure)
//	{
//		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
//	}

//	@Override
//	public final SubList<E> iterate(final CtrlIndexProcedure<? super E> procedure)
//	{
//		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
//	}

	@Override
	public final <T> T[] copyTo(final T[] target, final int targetOffset, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final SubList<E> reverse()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final SubList<E> shiftTo(final int sourceIndex, final int targetIndex)
	{
		super.shiftTo(sourceIndex, targetIndex);
		return this;
	}

	@Override
	public final SubList<E> shiftTo(final int sourceIndex, final int targetIndex, final int length)
	{
		super.shiftTo(sourceIndex, targetIndex, length);
		return this;
	}

	@Override
	public final SubList<E> shiftBy(final int sourceIndex, final int distance)
	{
		super.shiftBy(sourceIndex, distance);
		return this;
	}

	@Override
	public final SubList<E> shiftBy(final int sourceIndex, final int distance, final int length)
	{
		super.shiftBy(sourceIndex, distance, length);
		return this;
	}

}
