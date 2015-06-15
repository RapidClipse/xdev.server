package net.jadoth.collections.interfaces;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.IndexProcedure;
import net.jadoth.functional.Procedure;
import net.jadoth.reference.ReferenceType;
import net.jadoth.util.Composition;
import net.jadoth.util.Equalator;
import net.jadoth.util.chars.VarString;



// (21.09.2013)FIXME: why are the entries no interfaces on the interface level?
public interface ChainStorage<E, K, V, EN extends ChainStorage.Entry<E, K, V, EN>> extends Iterable<E>, Composition
{
	public interface Entry<E, K, V, EN extends Entry<E, K, V, EN>> extends Composition
	{
		// only marker interface so far
	}


	public void appendEntry(EN entry);

	public void prependEntry(EN entry);

	public void clear();



	///////////////////////////////////////////////////////////////////////////
	// chain navigation //
	/////////////////////

	public EN getChainEntry(int index) throws IndexOutOfBoundsException;

	public EN getRangeChainEntry(int offset, int length) throws IndexOutOfBoundsException;

	public EN getIntervalLowChainEntry(int lowIndex, int highIndex) throws IndexOutOfBoundsException;



	///////////////////////////////////////////////////////////////////////////
	//  content info    //
	/////////////////////

	public int size();

	/**
	 * Removes all empty entries from the passed chain and returns the number of removed entries.
	 *
	 * @param <E> the element type of the passed chain.
	 * @param chain the chain to be consolidated (cleared of empty entries).
	 * @return the number of removed entries.
	 */
	public int consolidate();

	public boolean hasVolatileElements();

	public ReferenceType getReferenceType();

	@Override
	public Iterator<E> iterator();

	public boolean equalsContent(XGettingCollection<? extends E> other, Equalator<? super E> equalator);



	///////////////////////////////////////////////////////////////////////////
	//   containing     //
	/////////////////////

	// containing - null //

	public boolean containsNull();

	public boolean rngContainsNull(final int offset, int length);

	// containing - identity //

	public boolean containsId(E element);

	public boolean rngContainsId(int offset, int length, E element);

	// containing - logical //

	public boolean contains(E element);

	public boolean contains(E sample, Equalator<? super E> equalator);

	public boolean rngContains(int offset, int length, E element);

//	public boolean rngContains(int offset, int length, E sample, Equalator<? super E> equalator);

	// containing - all array //

	public boolean containsAll(E[] elements, int elementsOffset, int elementsLength);

//	public boolean containsAll(E[] elements, int elementsOffset, int elementsLength, Equalator<? super E> equalator);

	public boolean rngContainsAll(
		int offset,
		int length,
		E[] elements,
		int elementsOffset,
		int elementsLength
	);

//	public boolean rngContainsAll(
//		int offset,
//		int length,
//		E[] elements,
//		int elementsOffset,
//		int elementsLength,
//		Equalator<? super E> equalator
//	);

	// containing - all collection //

	public boolean containsAll(XGettingCollection<? extends E> elements);

//	public boolean containsAll(XGettingCollection<? extends E> elements, Equalator<? super E> equalator);

	public boolean rngContainsAll(int offset, int length, XGettingCollection<? extends E> elements);

//	public boolean rngContainsAll(
//		int offset,
//		int length,
//		XGettingCollection<? extends E> samples,
//		Equalator<? super E> equalator
//	);



	///////////////////////////////////////////////////////////////////////////
	//    applying      //
	/////////////////////

	// applying - single //

	public boolean applies(Predicate<? super E> predicate);

	public boolean rngApplies(int offset, int length, Predicate<? super E> predicate);

	// applying - all //

	public boolean appliesAll(Predicate<? super E> predicate);

	public boolean rngAppliesAll(int offset, int length, Predicate<? super E> predicate);



	///////////////////////////////////////////////////////////////////////////
	//    counting      //
	/////////////////////

	// counting - element //

	public int count(E element);

	public int count(E sample, Equalator<? super E> equalator);

	public int rngCount(int offset, int length, E element);

//	public int rngCount(int offset, int length, E sample, Equalator<? super E> equalator);

	// counting - predicate //

	public int count(Predicate<? super E> predicate);

	public int rngCount(int offset, int length, Predicate<? super E> predicate);



	///////////////////////////////////////////////////////////////////////////
	// data arithmetic  //
	/////////////////////

	// data - data sets //

	public <C extends Procedure<? super E>> C intersect(
		XGettingCollection<? extends E> samples,
		Equalator<? super E> equalator,
		C target
	);

	public <C extends Procedure<? super E>> C except(
		XGettingCollection<? extends E> samples,
		Equalator<? super E> equalator,
		C target
	);

	public <C extends Procedure<? super E>> C union(
		XGettingCollection<? extends E> samples,
		Equalator<? super E> equalator,
		C target
	);

	public <C extends Procedure<? super E>> C rngIntersect(
		int offset,
		int length,
		XGettingCollection<? extends E> samples,
		Equalator<? super E> equalator,
		C target
	);

	public <C extends Procedure<? super E>> C rngExcept(
		int offset,
		int length,
		XGettingCollection<? extends E> samples,
		Equalator<? super E> equalator,
		C target
	);

	public <C extends Procedure<? super E>> C rngUnion(
		int offset,
		int length,
		XGettingCollection<? extends E> samples,
		Equalator<? super E> equalator,
		C target
	);

	// data - copying //

	public <C extends Procedure<? super E>> C copyTo(C target);

	public <C extends Procedure<? super E>> C rngCopyTo(int offset, int length, C target);

	public <C extends Procedure<? super E>> C copySelection(C target, int... indices);

	public int copyToArray(int offset, int length, Object[] target, int targetOffset);

	// data - conditional copying //

	public <C extends Procedure<? super E>> C copyTo(C target,Predicate<? super E> predicate);

	public <C extends Procedure<? super E>> C rngCopyTo(
		int offset,
		int length,
		C target,
		Predicate<? super E> predicate
	);

	// data - array transformation //

	public Object[] toArray();

	public      E[] toArray(Class<E> type);

	public Object[] rngToArray(int offset, int length);

	public      E[] rngToArray(int offset, int length, Class<E> type);



	///////////////////////////////////////////////////////////////////////////
	//    querying      //
	/////////////////////

	public E first();

	public E last();

	public E get(int index);



	///////////////////////////////////////////////////////////////////////////
	//    searching     //
	/////////////////////

	// searching - sample //

	public E seek(E sample);

	public E seek(E sample, Equalator<? super E> equalator);

//	public E rngSeek(int offset, int length, E sample, Equalator<? super E> equalator);

	// searching - predicate //

	public E search(Predicate<? super E> predicate);

	public E rngSearch(int offset, int length, Predicate<? super E> predicate);

	// searching - min max //

	public E min(Comparator<? super E> comparator);

	public E max(Comparator<? super E> comparator);

	public E rngMin(int offset, int length, Comparator<? super E> comparator);

	public E rngMax(int offset, int length, Comparator<? super E> comparator);



	///////////////////////////////////////////////////////////////////////////
	//    executing     //
	/////////////////////

	// executing - procedure //

	public void iterate(Procedure<? super E> procedure);

	public <A> void join(BiProcedure<? super E, A> joiner, A aggregate);

	public void rngIterate(int offset, int length, Procedure<? super E> procedure);

	// executing - indexed procedure //

	public void iterate(IndexProcedure<? super E> procedure);

	public void rngIterate(int offset, int length, IndexProcedure<? super E> procedure);

	// executing - conditional //

	// (14.01.2013 TM)FIXME: replace (predicate, procedure) variants by simple (procedure) with wrapping procedure

	public void iterate(Predicate<? super E> predicate, Procedure<? super E> procedure);

	// executing - conditional, limited //




	///////////////////////////////////////////////////////////////////////////
	//    indexing      //
	/////////////////////

	// indexing - single //

	public int indexOf(E element);

	public int indexOf(E sample, Equalator<? super E> equalator);

	public int rngIndexOf(int offset, int length, E element);

	public int rngIndexOf(int offset, int length, E sample, Equalator<? super E> equalator);

	// indexing - predicate //

	public int indexOf(Predicate<? super E> predicate);

	public int rngIndexOf(int offset, int length, Predicate<? super E> predicate);

	// indexing - min max //

	public int minIndex(Comparator<? super E> comparator);

	public int maxIndex(Comparator<? super E> comparator);

	public int rngMinIndex(int offset, int length, Comparator<? super E> comparator);

	public int rngMaxIndex(int offset, int length, Comparator<? super E> comparator);

	// indexing - scan //

	public int scan(Predicate<? super E> predicate);

	public int rngScan(int offset, int length, Predicate<? super E> predicate);

	// indexing - special //

//	public int lastIndexOf(E sample, Equalator<? super E> equalator);



	///////////////////////////////////////////////////////////////////////////
	//   distinction    //
	/////////////////////

	// distinction querying //

	public boolean hasDistinctValues();

	public boolean hasDistinctValues(Equalator<? super E> equalator);

	// distinction copying //

	public <C extends Procedure<? super E>> C distinct(C target);

	public <C extends Procedure<? super E>> C distinct(C target, Equalator<? super E> equalator);

	public <C extends Procedure<? super E>> C rngDistinct(int offset, int length, C target);

	public <C extends Procedure<? super E>> C rngDistinct(
		int offset,
		int length,
		C target,
		Equalator<? super E> equalator
	);



	///////////////////////////////////////////////////////////////////////////
	// VarString appending //
	//////////////////////

	public VarString appendTo(VarString vc);

	public VarString appendTo(VarString vc, char seperator);

	public VarString appendTo(VarString vc, String seperator);

	public VarString appendTo(VarString vc, BiProcedure<VarString, ? super E> appender);

	public VarString appendTo(VarString vc, BiProcedure<VarString, ? super E> appender, char seperator);

	public VarString appendTo(VarString vc, BiProcedure<VarString, ? super E> appender, String seperator);

	public VarString rngAppendTo(int offset, int length, VarString vc);

	public VarString rngAppendTo(int offset, int length, VarString vc, char seperator);

	public VarString rngAppendTo(int offset, int length, VarString vc, String seperator);

	public VarString rngAppendTo(int offset, int length, VarString vc, BiProcedure<VarString, ? super E> appender);

	public VarString rngAppendTo(int offset, int length, VarString vc, BiProcedure<VarString, ? super E> appender, char seperator);

	public VarString rngAppendTo(int offset, int length, VarString vc, BiProcedure<VarString, ? super E> appender, String seperator);

	@Override
	public String toString();



	///////////////////////////////////////////////////////////////////////////
	//    removing      //
	/////////////////////

	// removing - indexed //

	public E remove(int index);

	public void removeRange(int offset, int length);

	public void retainRange(int offset, int length);

	/**
	 * Removes all entries at the indices (offsets) given in the passed {@code int} array.
	 * <p>
	 * Note that the indices array gets presorted to increase algorithm performance. If the original {@code int} array
	 * shall be unchanged, a clone must be passed.
	 *
	 * @param <E> the element type of the chain.
	 * @param chain the chain whose entries shall be removed.
	 * @param indices the indices (offsets) of the entries to be removed.
	 * @return the amount of actually removed entries.
	 */
	public int removeSelection(int... indices);

	// removing - null //

	public int removeNull();

	public int rngRemoveNull(int offset, int length);

	// removing - one single //

	public E retrieve(E element);

	public E retrieve(Predicate<? super E> predicate);

	public E rngRetrieve(int offset, int length, E element);

//	public E rngRetrieve(int offset, int length, E sample, Equalator<? super E> equalator);

	public boolean removeOne(E element);

//	public boolean removeOne(E sample, Equalator<? super E> equalator);

	// removing - multiple single //

	public int remove(E element);

	public int remove(E sample, Equalator<? super E> equalator);

	public int rngRemove(int offset, int length, E element);

//	public int rngRemove(int offset, int length, E sample, Equalator<? super E> equalator);

	// removing - multiple single, limited //

//	public int remove(E element, int skip, Integer limit);

//	public int remove(E sample, Equalator<? super E> equalator, int skip, Integer limit);

//	public int rngRemove(int offset, int length, E element, int skip, Integer limit);

//	public int rngRemove(int offset, int length, E sample, Equalator<? super E> equalator, int skip, Integer limit);

	// removing - multiple all array //

	public int removeAll(E[] elements, int elementsOffset, int elementsLength);

//	public int removeAll(E[] samples, int samplesOffset, int samplesLength, Equalator<? super E> equalator);

	public int rngRemoveAll(int offset, int length, E[] elements, int elementsOffset, int elementsLength);

//	public int rngRemoveAll(int offset, int length,	E[] samples, int samplesOffset, int samplesLength, Equalator<? super E> equalator);

	// removing - multiple all collection //

	public int removeAll(XGettingCollection<? extends E> elements);

//	public int removeAll(XGettingCollection<? extends E> samples, Equalator<? super E> equalator);

	public int rngRemoveAll(int offset, int length, XGettingCollection<? extends E> elements);

//	public int rngRemoveAll(int offset, int length, XGettingCollection<? extends E> samples, Equalator<? super E> equalator);

	// removing - duplicates //

	public int removeDuplicates();

	public int removeDuplicates(Equalator<? super E> equalator);

	public int rngRemoveDuplicates(int offset, int length);

	public int rngRemoveDuplicates(int offset, int length, Equalator<? super E> equalator);



	///////////////////////////////////////////////////////////////////////////
	//     reducing     //
	/////////////////////

	// reducing - predicate //

	public int reduce(Predicate<? super E> predicate);

	public int rngReduce(int offset, int length, Predicate<? super E> predicate);

	// reducing - limited //

//	public int reduce(Predicate<? super E> predicate, int skip, Integer limit);

//	public int rngReduce(int offset, int length, Predicate<? super E> predicate, int skip, Integer limit);



	///////////////////////////////////////////////////////////////////////////
	//    retaining     //
	/////////////////////

	// retaining - array //

	public int retainAll(E[] elements, int elementsOffset, int elementsLength);

//	public int retainAll(E[] samples, int elementsOffset, int elementsLength, Equalator<? super E> equalator);

	public int rngRetainAll(int offset, int length, E[] elements, int elementsOffset, int elementsLength);

//	public int rngRetainAll(
//		int offset,
//		int length,
//		E[] samples,
//		int elementsOffset,
//		int elementsLength,
//		Equalator<? super E> equalator
//	);

	// retaining - collection //

	public int retainAll(XGettingCollection<? extends E> elements);

	public int retainAll(XGettingCollection<? extends E> samples, Equalator<? super E> equalator);

	public int rngRetainAll(int offset, int length, XGettingCollection<? extends E> elements);

//	public int rngRetainAll(int offset,	int length,	XGettingCollection<? extends E> samples, Equalator<? super E> equalator);



	///////////////////////////////////////////////////////////////////////////
	//   processing     //
	/////////////////////

	public int process(Procedure<? super E> procedure);

	public int rngProcess(int offset, int length, Procedure<? super E> procedure);



	///////////////////////////////////////////////////////////////////////////
	//     Moving       //
	/////////////////////

	public int moveRange(int offset, int length, Procedure<? super E> target);

	public int moveSelection(Procedure<? super E> target, int... indices);

	// moving - conditional //

	public int moveTo(Procedure<? super E> target, Predicate<? super E> predicate);

	public int rngMoveTo(int offset, int length, Procedure<? super E> target, Predicate<? super E> predicate);



	///////////////////////////////////////////////////////////////////////////
	//     ordering     //
	/////////////////////

	public void shiftTo(final int sourceIndex, final int targetIndex);

	public void shiftTo(final int sourceIndex, final int targetIndex, final int length);

	public void shiftBy(final int sourceIndex, final int distance);

	public void shiftBy(final int sourceIndex, final int distance, final int length);

	public void swap(int indexA, int indexB);

	public void swap(int indexA, int indexB, int length);

	public void reverse();

	public void rngReverse(int offset, int length);



	///////////////////////////////////////////////////////////////////////////
	//     sorting       //
	//////////////////////

	public void sort(Comparator<? super E> comparator);

	public void rngSort(int offset, int length, Comparator<? super E> comparator);

	public boolean isSorted(Comparator<? super E> comparator);

	public boolean rngIsSorted(int offset, int length, Comparator<? super E> comparator);

	public void shuffle();

	public void rngShuffle(int offset, int length);



	///////////////////////////////////////////////////////////////////////////
	//     setting      //
	/////////////////////

	@SuppressWarnings("unchecked")
	public void set(int offset, E... elements);

	public void set(int offset, E[] elements, int elementsOffset, int elementsLength);

	public void fill(int offset, int length, E element);



	///////////////////////////////////////////////////////////////////////////
	//    replacing     //
	/////////////////////

	// replacing - one single //

	public int replaceOne(E element, E replacement);

//	public int replaceOne(E sample, Equalator<? super E> equalator, E replacement);

	public int rngReplaceOne(int offset, int length, E element, E replacement);

//	public int rngReplaceOne(int offset, int length, E sample, Equalator<? super E> equalator, E replacement);

	// replacing - multiple single //

	public int replace(E element, E replacement);

//	public int replace(E sample, Equalator<? super E> equalator, E replacement);

	public int rngReplace(int offset, int length, E element, E replacement);

	public int rngReplace(int offset, int length, E sample, Equalator<? super E> equalator, E replacement);

	// replacing - multiple all array //

	public int replaceAll(E[] elements, int elementsOffset, int elementsLength, E replacement);

//	public int replaceAll(
//		E[] samples,
//		int samplesOffset,
//		int samplesLength,
//		Equalator<? super E> equalator,
//		E replacement
//	);

	public int rngReplaceAll(int offset, int length, E[] elements, int elementsOffset, int elementsLength, E replacement);

//	public int rngReplaceAll(
//		int offset,
//		int length,
//		E[] samples,
//		int samplesOffset,
//		int samplesLength,
//		Equalator<? super E> equalator,
//		E replacement
//	);

	// replacing - multiple all collection //

	public int replaceAll(XGettingCollection<? extends E> elements, E replacement);

//	public int replaceAll(XGettingCollection<? extends E> samples, Equalator<? super E> equalator, E replacement);

	public int rngReplaceAll(int offset, int length, XGettingCollection<? extends E> elements, E replacement);

//	public int rngReplaceAll(
//		int offset,
//		int length,
//		XGettingCollection<? extends E> samples,
//		Equalator<? super E> equalator,
//		E replacement
//	);

	// replacing - mapped //

	public int modify(Function<E, E> mapper);

	public int modify(Predicate<? super E> predicate, Function<E, E> mapper);

	public int rngModify(int offset, int length, Function<E, E> mapper);

	public int rngModify(int offset, int length, Predicate<? super E> predicate, Function<E, E> mapper);



	///////////////////////////////////////////////////////////////////////////
	//  substituting    //
	/////////////////////

	// substituting - one //

	public int substituteOne(Predicate<? super E> predicate, E substitute);

	public int rngSubstituteOne(int offset, int length, Predicate<? super E> predicate, E substitute);

	// substituting - multiple //

	public int substitute(Predicate<? super E> predicate, E substitute);

	public int rngSubstitute(int offset, int length, Predicate<? super E> predicate, E substitute);

}
