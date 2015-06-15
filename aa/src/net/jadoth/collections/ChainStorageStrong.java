package net.jadoth.collections;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static net.jadoth.Jadoth.BREAK;
import static net.jadoth.collections.AbstractChainEntry.hopNext;
import static net.jadoth.collections.AbstractChainEntry.hopPrev;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.functions.CachedSampleEquality;
import net.jadoth.collections.functions.ElementIsContained;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.functional.Aggregator;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.IndexProcedure;
import net.jadoth.functional.Procedure;
import net.jadoth.math.FastRandom;
import net.jadoth.reference.ReferenceType;
import net.jadoth.util.Equalator;
import net.jadoth.util.branching.ThrowBreak;
import net.jadoth.util.chars.VarString;


public class ChainStorageStrong<E, K, V, EN extends AbstractChainEntry<E, K, V, EN>>
extends AbstractChainStorage<E, K, V, EN>
{
	static final String exceptionRange(final int size, final int offset, final int length)
	{
		return "Range ["+(length < 0 ?offset + length+1+";"+offset
			:offset+";"+(offset + length-1))+"] not in [0;"+(size-1)+"]";
	}

	static final String exceptionIndexOutOfBounds(final int size, final int index)
	{
		return "Index: "+index+", Size: "+size;
	}

	static final String exceptionIllegalSwapBounds(final int indexA, final int indexB,final int length)
	{
		return "Illegal swap bounds: ("+indexA+" ["+length+"] -> "+indexB+" ["+length+"])";
	}

	static final int validateArrayIteration(final Object[] array, final int offset, final int length)
	{
		// elements array range checking
		if(length >= 0){
			if(offset < 0 || offset + length > array.length){
				throw new IndexOutOfBoundsException(exceptionRange(array.length, offset, length));
			}
			if(length == 0){
				return 0;
			}
			return +1; // incrementing direction
		}
		else if(length < 0){
			if(offset + length < -1 || offset >= array.length){
				throw new IndexOutOfBoundsException(exceptionRange(array.length, offset, length));
			}
			return -1; // decrementing direction
		}
		else if(offset < 0 || offset >= array.length){
			throw new IndexOutOfBoundsException(exceptionIndexOutOfBounds(array.length, offset));
		}
		else {
			// handle length 0 special case not as escape condition but as last case to ensure index checking
			return 0;
		}
	}



	///////////////////////////////////////////////////////////////////////////
	// sorting internals //
	//////////////////////

	static <E, K, V, EN extends AbstractChainEntry<E, K, V, EN>>
	void mergesortHead(final EN head, final Comparator<? super E> comparator)
	{
		EN last, entry;
		try{
			entry = mergesort0(head.next, comparator); // sort
		}
		catch(final RuntimeException e){
			// rollback 8-)
			for(entry = head.prev; (entry = (last = entry).prev) != head;){
				entry.next = last;
			}
			throw e;
		}
		catch(final Error e){ // Java 7 where are you :.-(
			for(entry = head.prev; (entry = (last = entry).prev) != head;){
				entry.next = last;
			}
			throw e;
		}

		// reattach sorted chain to head and rebuild prev direction
		(head.next = entry).prev = head;              // entry is new start entry
		while((entry = (last = entry).next) != null){
			entry.prev = last;                        // rebuild prev references
		}
		head.prev = last;                             // last entry now is new end entry (obviously)
	}

	private static <E, K, V, EN extends AbstractChainEntry<E, K, V, EN>> void mergesortRange(
		final EN preFirst,
		final EN postLast,
		final EN head,
		final Comparator<? super E> comparator
	)
	{
		EN last, entry;
		try{
			if(postLast == null){
				entry = mergesort0(preFirst.next, comparator); // sort normally
			}
			else {
				entry = rngMergesort0(preFirst.next, postLast, comparator); // sort
			}
		}
		catch(final RuntimeException e){
			// rollback 8-)
			for(entry = (postLast==null ?head :postLast).prev; (entry = (last = entry).prev) != preFirst;){
				entry.next = last;
			}
			throw e;
		}
		catch(final Error e){ // Java 7 where are you :.-(
			for(entry = (postLast==null ?head :postLast).prev; (entry = (last = entry).prev) != preFirst;){
				entry.next = last;
			}
			throw e;
		}

		// reattach sorted chain to head and rebuild prev direction
		(preFirst.next = entry).prev = preFirst;      // entry is new start entry
		while((entry = (last = entry).next) != null){
			entry.prev = last;                        // rebuild prev references
		}
		if(postLast != null){
			postLast.prev = last;                     // entry now is new end entry
		}
		else {
			head.prev = last;
		}
	}

	private static <E, K, V, EN extends AbstractChainEntry<E, K, V, EN>> EN mergesort0(final EN chain, final Comparator<? super E> comparator)
	{
		// special case handling for empty or trivial chain
		if(chain == null || chain.next == null){
			return chain;
		}

		// inlined iterative splitting
		EN chain2, t1, t2 = chain2 = (t1 = chain).next;
		while(t2 != null && (t1 = t1.next = t2.next) != null){
			t2 = t2.next = t1.next;
		}

		// merging
		return merge1(mergesort0(chain, comparator), mergesort0(chain2, comparator), comparator);
	}

	private static <E, K, V, EN extends AbstractChainEntry<E, K, V, EN>> EN rngMergesort0(
		final EN chain,
		final EN end,
		final Comparator<? super E> cmp
	)
	{
		// special case handling for empty or trivial chain
		if(chain == null || chain.next == null){
			return chain;
		}

		// inlined iterative splitting
		EN chain2, t1, t2 = chain2 = (t1 = chain).next;
		while(t2 != end && (t1 = t1.next = t2.next) != end){
			t2 = t2.next = t1.next;
		}

		// merging
		return rngMerge1(rngMergesort0(chain, end, cmp), rngMergesort0(chain2, end, cmp), end, cmp);
	}

	// merge iterative
	private static <E, K, V, EN extends AbstractChainEntry<E, K, V, EN>> EN merge1(EN c1, EN c2, final Comparator<? super E> cmp)
	{
		if(c1 == null){
			return c2;
		}
		if(c2 == null){
			return c1;
		}

		final EN c;
		if(cmp.compare(c1.element(), c2.element()) < 0){
			c1 = (c = c1).next;
		}
		else {
			c2 = (c = c2).next;
		}

		for(EN t = c;;){
			if(c1 == null){
				t.next = c2;
				break;
			}
			else if(c2 == null){
				t.next = c1;
				break;
			}
			else if(cmp.compare(c1.element(), c2.element()) < 0)
				c1 = (t = t.next = c1).next;
			else
				c2 = (t = t.next = c2).next;
		}
		return c;
	}

	// merge iterative
	private static <E, K, V, EN extends AbstractChainEntry<E, K, V, EN>> EN rngMerge1(
		      EN c1,
		      EN c2,
		final EN end,
		final Comparator<? super E> cmp
	)
	{
		if(c1 == end){
			return c2;
		}
		if(c2 == end){
			return c1;
		}

		final EN c;
		if(cmp.compare(c1.element(), c2.element()) < 0){
			c1 = (c = c1).next;
		}
		else {
			c2 = (c = c2).next;
		}

		for(EN t = c;;){
			if(c1 == end){
				t.next = c2;
				break;
			}
			else if(c2 == end){
				t.next = c1;
				break;
			}
			else if(cmp.compare(c1.element(), c2.element()) < 0)
				c1 = (t = t.next = c1).next;
			else
				c2 = (t = t.next = c2).next;
		}
		return c;
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	final AbstractChainCollection<E, K, V, EN> parent;
	final EN head;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	public ChainStorageStrong(final AbstractChainCollection<E, K, V, EN> parent, final EN head)
	{
		super();
		this.parent = parent;
		(this.head = head).prev = head; // intentionally no setting of head.next! (supposed to remain null)
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////


	@Override
	protected final EN head()
	{
		return this.head;
	}

	@Override
	protected void disjoinEntry(final EN entry)
	{
		((entry.next == null ?this.head :entry.next).prev = entry.prev).next = entry.next;
	}

	@Override
	protected final boolean moveToStart(final EN entry)
	{
		if(entry.prev == this.head){
			return false; // already at the start, do nothing
		}
		if(entry.next == null){ // entry at end special case
			(this.head.prev = entry.prev).next = null;
		}
		else {
			// disjoin from current position
			entry.prev.next = entry.next;
			entry.next.prev = entry.prev;
		}
		// join in at first position
		entry.next = this.head.next;
		(entry.prev = this.head).next = entry;
		return true;
	}

	@Override
	protected final boolean moveToEnd(final EN entry)
	{
		if(entry.next == null){
			return false; // already at the end, do nothing
		}

		// disjoin from current position
		entry.prev.next = entry.next;
		entry.next.prev = entry.prev;

		// join in at head position and mark as end of chain
		(entry.prev = this.head.prev).next = this.head.prev = entry;
		entry.next = null;
		return true;
	}

	@Override
	public final void appendEntry(final EN entry)
	{
		(entry.prev = this.head.prev).next = this.head.prev = entry;
	}

	@Override
	public final void prependEntry(final EN entry)
	{
		entry.next = this.head.next;
		(entry.prev = this.head).next = entry;
	}

	@Override
	public void clear()
	{
		// not sure this is worth the effort
//		// break inter-entry references to ease GC
//		for(EN entry = this.head; (entry = entry.next) != null; ){
//			entry.prev.next = null; // subsequently clear previous entry's .next
//			entry.prev = null;
//		}

		// reset singleton fields
		(this.head.prev = this.head).next = null;
	}

	@Override
	public final void shiftBy(final int sourceIndex, final int distance)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

	@Override
	public final void shiftBy(final int sourceIndex, final int distance, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

	@Override
	public final void shiftTo(final int sourceIndex, final int targetIndex)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

	@Override
	public final void shiftTo(final int sourceIndex, final int targetIndex, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME Auto-generated method stub, not implemented yet
	}

	private void swapEntries(final EN entryA, final EN entryB)
	{
		//  / A \---1-->/ A \---3-->/ A \ ... / B \---5-->/ B \---7-->/ B \
		//  \prv/<--2---\   /<--4---\nxt/     \prv/<--6---\   /<--8---\nxt/

		EN temp;

		// step 1: swap next references
		((temp = entryA.next) != null ?temp :this.head).prev = entryB; // 4
		((entryA.next = entryB.next)                              // 3
			!= null ?entryB.next :this.head).prev = entryA;            // 8
		entryB.next = temp;                                       // 7

		// step 2: swap prev references (prev can never be null, only head)
		(temp = entryA.prev).next = entryB; // 1
		entryB.prev.next = entryA;          // 5
		entryA.prev = entryB.prev;          // 2
		entryB.prev = temp;                 // 6
	}




	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	// removing - indexed //

	@Override
	public final void removeRange(final int offset, int length)
	{
		EN e;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		if(length > 0){
			e = this.getRangeChainEntry(offset+length, -length);
		}
		else {
			e = this.getRangeChainEntry(offset, length);
			length = -length;
		}
		for(; length --> 0; e = e.prev){
			e.removeFrom(parent);
		}
	}

	@Override
	public final void retainRange(final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME ChainStrongStrongStorage#retainRange()
	}

	/**
	 * Removes all entries at the indices (offsets) given in the passed {@code int} array.
	 * <p>
	 * Note that the indices array gets presorted to increase algorithm performance. Pass a clone if the original
	 * {@code int} array shall be unchanged.
	 *
	 * @param <E> the element type of the this.
	 * @param chain the chain whose entries shall be removed.
	 * @param indices the indices (offsets) of the entries to be removed.
	 */
	@Override
	public final int removeSelection(final int... indices)
	{
		int s = Jadoth.to_int(this.parent.size()) - 1;
		JadothSort.sort(indices);

		// validate indices before copying the first element
		if(indices[0] < 0){ // check lowest index against 0 bound.
			throw new IndexOutOfBoundsException(exceptionIndexOutOfBounds(s+1, indices[0]));
		}
		if(indices[indices.length-1] > s){ // check highest index against size bound.
			throw new IndexOutOfBoundsException(exceptionIndexOutOfBounds(s+1, indices[indices.length-1]));
		}

		// remove all entries in a single reverse direction pass
		EN e = this.head.prev;
		int idx, removeCount = 0;
		final int lastIdx = -1;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		for(int i = indices.length-1; i > 0; i--){ // iterate over all (sorted) indices in reverse order
			if((idx = indices[i]) == lastIdx){
				continue;                          // skip duplicate indices
			}
			while(--s > idx){
				e = e.prev;                        // scroll to next entry to remove
			}
			e.removeFrom(parent);                 // remove entry, continue loop with next index
			removeCount++;
		}
		return removeCount;
	}

	@Override
	public final Iterator<E> iterator()
	{
		return new KeyItr();
	}

	@Override
	public final boolean equalsContent(final XGettingCollection<? extends E> other, final Equalator<? super E> equalator)
	{
		if(Jadoth.to_int(this.parent.size()) != Jadoth.to_int(other.size())){
			return false;
		}

		if(other instanceof AbstractSimpleArrayCollection<?>){
			final int otherSize = Jadoth.to_int(other.size());
			final E[] otherData = AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)other);
			EN entry = this.head.next;
			for(int i = 0; i < otherSize; i++, entry = entry.next){
				if(!equalator.equal(entry.element(), otherData[i])){
					return false;
				}
			}
			return true;
		}

		final Aggregator<E, Boolean> agg = new Aggregator<E, Boolean>(){
			private EN entry = ChainStorageStrong.this.head;

			@Override
			public final void accept(final E element)
			{
				if((this.entry = this.entry.next) == null){
					throw BREAK; // chain is too short
				}
				if(!equalator.equal(element, this.entry.element())){
					throw BREAK; // unequal element found
				}
			}

			@Override
			public final Boolean yield()
			{
				/* current entry may not be null (otherwise chain was too short)
				 * but next entry in chain must be null (otherwise chain is too long)
				 */
				return this.entry != null && (this.entry = this.entry.next) == null ?TRUE :FALSE;
			}
		};

		other.iterate(agg);
		return agg.yield();
	}



	///////////////////////////////////////////////////////////////////////////
	// chain navigation //
	/////////////////////

	@Override
	public EN getChainEntry(int index) throws IndexOutOfBoundsException
	{
		if(index < 0 || index >= Jadoth.to_int(this.parent.size()))  {
			throw new IndexOutOfBoundsException(exceptionIndexOutOfBounds(Jadoth.to_int(this.parent.size()), index));
		}
		EN e = this.head;
		if(Jadoth.to_int(this.parent.size())>>>1 < index){
			for(final int size = Jadoth.to_int(this.parent.size()); index < size; index++){
				e = e.prev;
			}
		}
		else {
			for(; index >= 0; index--){
				e = e.next;
			}
		}
		return e;
	}

	@Override
	public EN getRangeChainEntry(int offset, final int length) throws IndexOutOfBoundsException
	{
		final int size = Jadoth.to_int(this.parent.size());

		// validate length
		if(length >= 0){
			if(offset + length > size){
				throw new IndexOutOfBoundsException(exceptionRange(size, offset, length));
			}
			if(length == 0){
				return this.head;
			}
			// fall through to offset scrolling
		}
		else if(length < 0){
			if(offset + length < -1){
				throw new IndexOutOfBoundsException(exceptionRange(size, offset, length));
			}
			// fall through to offset scrolling
		}
		else if(offset < 0 || offset >= size) {
			throw new IndexOutOfBoundsException(exceptionIndexOutOfBounds(size, offset));
		}
		else {
			return null; // special case length 0: skip offset scrolling and return null (length 0 chain).
		}

		// scroll to offset
		EN entry = this.head;

//		System.out.println(size>>>1);
//		System.out.println(offset);

		if(offset <= size>>>1){ // <= to cover cases like offset 0, size 1
			if(offset < 0){
				throw new IndexOutOfBoundsException(exceptionIndexOutOfBounds(size, offset));
			}
			while(offset-- >= 0){
				entry = entry.next;
			}
		}
		else {
			if(offset >= size){
				throw new IndexOutOfBoundsException(exceptionIndexOutOfBounds(size, offset));
			}
			while(offset++ < size){
				entry = entry.prev;
			}
		}
		return entry; // entry at valuesid index offset with valuesid scrollable +/- length.
	}

	@Override
	public EN getIntervalLowChainEntry(int lowIndex, final int highIndex) throws IndexOutOfBoundsException
	{
		if(lowIndex < 0){
			throw new IndexOutOfBoundsException(exceptionIndexOutOfBounds(Jadoth.to_int(this.parent.size()), lowIndex));
		}
		if(highIndex >= Jadoth.to_int(this.parent.size())){
			throw new IndexOutOfBoundsException(exceptionIndexOutOfBounds(Jadoth.to_int(this.parent.size()), highIndex));
		}

		EN entry = this.head;
		while(lowIndex-- >= 0){
			entry = entry.next;
		}
		return entry;
	}



	///////////////////////////////////////////////////////////////////////////
	//  content info    //
	/////////////////////

	@Override
	public final int size()
	{
		return Jadoth.to_int(this.parent.size());
	}

	@Override
	public final int consolidate()
	{
		return 0;
	}

	@Override
	public final boolean hasVolatileElements()
	{
		return false;
	}

	@Override
	public final ReferenceType getReferenceType()
	{
		return ReferenceType.STRONG;
	}



	///////////////////////////////////////////////////////////////////////////
	//   containing     //
	/////////////////////

	// containing - null //

	@Override
	public final boolean containsNull()
	{
		for(EN e = this.head.next; e != null; e = e.next){
			if(e.hasNullElement()){
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean rngContainsNull(final int offset, int length)
	{
		EN e = this.getRangeChainEntry(offset, length); // validate range and scroll to offset
		if(length < 0){
			for(; length ++< 0; e = e.prev){
				if(e.hasNullElement()){
					return true;
				}
			}
		}
		else {
			for(; length --> 0; e = e.next){
				if(e.hasNullElement()){
					return true;
				}
			}
		}
		return false;
	}

	// containing - identity //

	@Override
	public final boolean containsId(final E element)
	{
		for(EN e = this.head.next; e != null; e = e.next){
			if(e.element() == element){
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean rngContainsId(final int offset, int length, final E element)
	{
		EN e = this.getRangeChainEntry(offset, length); // validate range and scroll to offset
		if(length < 0){
			for(; length ++< 0; e = e.prev){
				if(e.element() == element){
					return true;
				}
			}
		}
		else {
			for(; length --> 0; e = e.next){
				if(e.element() == element){
					return true;
				}
			}
		}
		return false;
	}

	// containing - logical //

	@Override
	public final boolean contains(final E element)
	{
		for(EN e = this.head.next; e != null; e = e.next){
			if(e.element() == element){
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean contains(final E sample, final Equalator<? super E> equalator)
	{
		for(EN e = this.head.next; e != null; e = e.next){
			if(equalator.equal(e.element(), sample)){
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean rngContains(final int offset, int length, final E element)
	{
		EN e = this.getRangeChainEntry(offset, length); // validate range and scroll to offset
		if(length < 0){
			for(; length ++< 0; e = e.prev){
				if(e.element() == element){
					return true;
				}
			}
		}
		else {
			for(; length --> 0; e = e.next){
				if(e.element() == element){
					return true;
				}
			}
		}
		return false;
	}

//	@Override
//	public final boolean rngContains(final int offset, int length, final E sample, final Equalator<? super E> equalator)
//	{
//		EN e = this.getRangeChainEntry(offset, length); // validate range and scroll to offset
//		if(length < 0){
//			for(; length ++< 0; e = e.prev){
//				if(equalator.equal(e.element(), sample)){
//					return true;
//				}
//			}
//		}
//		else {
//			for(; length --> 0; e = e.next){
//				if(equalator.equal(e.element(), sample)){
//					return true;
//				}
//			}
//		}
//		return false;
//	}

	// containing - all array //

	@Override
	public final boolean containsAll(final E[] elements, final int elementsOffset, final int elementsLength)
	{
		final EN first;
		if((first = this.head.next) == null){
			return false; // size 0
		}
		final int d;
		if((d = ChainStorageStrong.validateArrayIteration(elements, elementsOffset, elementsLength)) == 0){
			return true;
		}
		final int elementsBound = elementsOffset + elementsLength;

		main: for(int ei = elementsOffset; ei != elementsBound; ei+=d){
			final E element = elements[ei];
			for(EN e = first; e != null; e = e.next){
				if(e.element() == element){
					continue main;
				}
			}
			return false;  // one element was not found in this list, return false
		}
		return true; // all elements have been found, return true
	}

//	@Override
//	public final boolean containsAll(
//		final E[] elements,
//		final int elementsOffset,
//		final int elementsLength,
//		final Equalator<? super E> equalator
//	)
//	{
//		final EN first;
//		if((first = this.head.next) == null) return false; // size 0
//		final int d;
//		if((d = ChainStrongStrongStorage.validateArrayIteration(elements, elementsOffset, elementsLength)) == 0) return true;
//		final int elementsBound = elementsOffset + elementsLength;
//
//		// cross-iterate, either with or without ignoring nulls
//		main: for(int ei = elementsOffset; ei != elementsBound; ei+=d){
//			final E element = elements[ei];
//			for(EN e = first; e != null; e = e.next){
//				if(equalator.equal(e.element(), element)){
//					continue main;
//				}
//			}
//			return false;  // one element was not found in this list, return false
//		}
//		return true; // all elements have been found, return true
//	}

	@Override
	public final boolean rngContainsAll(
		final int offset,
		      int length,
		final E[] elements,
		final int elementsOffset,
		final int elementsLength
	)
	{
		final EN first;
		if((first = this.getRangeChainEntry(offset, length)) == null){
			return false; // size 0
		}
		final int d;
		if((d = ChainStorageStrong.validateArrayIteration(elements, elementsOffset, elementsLength)) == 0){
			return true;
		}
		final int elementsBound = elementsOffset + elementsLength;

		// hopping direction
		final AbstractChainEntry.Hopper hop;
		if(length < 0){
			hop = hopPrev;
			length = -length;
		}
		else hop = hopNext;

		main: for(int ei = elementsOffset; ei != elementsBound; ei+=d){
			final E element = elements[ei];
			for(EN e = first; e != null; e = hop.hop(e)){
				if(e.element() == element){
					continue main;
				}
			}
			return false;  // one element was not found in this list, return false
		}
		return true;  // all elements have been found, return true
	}

//	@Override
//	public final boolean rngContainsAll(
//		final int offset,
//		      int length,
//		final E[] elements,
//		final int elementsOffset,
//		final int elementsLength,
//		final Equalator<? super E> equalator
//	)
//	{
//		final EN first;
//		if((first = this.getRangeChainEntry(offset, length)) == null) return false; // size 0
//		final int d;
//		if((d = ChainStrongStrongStorage.validateArrayIteration(elements, elementsOffset, elementsLength)) == 0) return true;
//		final int elementsBound = elementsOffset + elementsLength;
//
//		// hopping direction
//		final ChainEntry.Hopper hop;
//		if(length < 0){
//			hop = hopPrev;
//			length = -length;
//		}
//		else hop = hopNext;
//
//		// cross-iterate, either with or without ignoring nulls
//		loop: for(int ei = elementsOffset; ei != elementsBound; ei+=d){
//			final E element = elements[ei];
//			for(EN e = first; e != null; e = hop.hop(e)){
//				if(equalator.equal(e.element(), element)){
//					continue loop;
//				}
//			}
//			return false;  // one element was not found in this list, return false
//		}
//		return true;  // all elements have been found, return true
//	}

	// containing - all collection //

	@Override
	public final boolean containsAll(final XGettingCollection<? extends E> elements)
	{
		if(elements instanceof AbstractSimpleArrayCollection<?>){
			// directly check array against array without predicate function or method calls
			return this.containsAll(
				AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements),
				0,
				Jadoth.to_int(elements.size())
			);
		}

		// iterate by predicate function
		return elements.applies(new Predicate<E>() { @Override public boolean test(final E e) {
			return ChainStorageStrong.this.contains(e);
		}});
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public final boolean containsAll(final XGettingCollection<? extends E> elements, final Equalator<? super E> equalator)
//	{
//		if(elements instanceof AbstractSimpleArrayCollection<?>){
//			// directly check array against array without predicate function or method calls
//			return this.containsAll(
//				AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements), 0, elements.size(),
//				equalator
//			);
//		}
//
//		// iterate by predicate function
//		final EN first = this.head.next;
//		return elements.applies(new Predicate<E>() { @Override public boolean test(final E e) {
//			for(EN entry = first; entry != null; entry = entry.next){
//				if(equalator.equal(entry.element(), e)){
//					return true;
//				}
//			}
//			return false;
//		}});
//	}

	@Override
	public final boolean rngContainsAll(final int offset, final int length, final XGettingCollection<? extends E> elements)
	{
		if(elements instanceof AbstractSimpleArrayCollection<?>){
			return this.rngContainsAll(
				offset,
				length,
				AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements),
				0,
				Jadoth.to_int(elements.size())
			);
		}

		// iterate by predicate function
		final EN first; // validate range and scroll to offset
		if((first = this.getRangeChainEntry(offset, length)) == null){
			return false;
		}

		// iterate by predicate function
		if(length < 0){
			return elements.applies(new Predicate<E>() {
				@Override public boolean test(final E e) {
					int len = length;
					for(EN entry = first; len ++< 0; entry = entry.prev){
						if(entry.element() == e){
							return true;
						}
					}
					return false;
				}
			});
		}
		return elements.applies(new Predicate<E>() { @Override public boolean test(final E e) {
			int len = length;
			for(EN entry = first; len --> 0; entry = entry.next){
				if(entry.element() == e){
					return true;
				}
			}
			return false;
		}});
	}

//	@Override
//	public final boolean rngContainsAll(
//		final int offset,
//		final int length,
//		final XGettingCollection<? extends E> samples,
//		final Equalator<? super E> equalator
//	)
//	{
//		if(samples instanceof AbstractSimpleArrayCollection<?>){
//			return this.rngContainsAll(
//				offset, length,
//				(E[])((AbstractSimpleArrayCollection<?>)samples).internalGetStorageArray(), 0, samples.size(),
//				equalator
//			);
//		}
//
//		final EN first; // validate range and scroll to offset
//		if((first = this.getRangeChainEntry(offset, length)) == null) return false;
//
//		// iterate by predicate function
//		if(length < 0){
//			return samples.applies(new Predicate<E>() { @Override public boolean test(final E e) {
//				int len = length;
//				for(EN entry = first; len ++< 0; entry = entry.prev){
//					if(equalator.equal(entry.element(), e)){
//						return true;
//					}
//				}
//				return false;
//			}});
//		}
//		return samples.applies(new Predicate<E>() { @Override public boolean test(final E e) {
//			int len = length;
//			for(EN entry = first; len --> 0; entry = entry.next){
//				if(equalator.equal(entry.element(), e)){
//					return true;
//				}
//			}
//			return false;
//		}});
//	}



	///////////////////////////////////////////////////////////////////////////
	//    applying      //
	/////////////////////

	// applying - single //

	@Override
	public final boolean applies(final Predicate<? super E> predicate)
	{
		try {
			for(EN e = this.head.next; e != null; e = e.next) {
				if(predicate.test(e.element())) {
					return true;
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return false;
	}

	@Override
	public final boolean rngApplies(final int offset, int length, final Predicate<? super E> predicate)
	{
		EN e = this.getRangeChainEntry(offset, length); // validate range and scroll to offset
		try {
			if(length < 0) {
				for(; length++ < 0; e = e.prev) {
					if(predicate.test(e.element())) {
						return true;
					}
				}
			}
			else {
				for(; length-- > 0; e = e.next) {
					if(predicate.test(e.element())) {
						return true;
					}
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return false;
	}

	// applying - all //

	@Override
	public final boolean appliesAll(final Predicate<? super E> predicate)
	{
		try {
			for(EN e = this.head.next; e != null; e = e.next) {
				if(!predicate.test(e.element())) {
					return false;
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return true;
	}

	@Override
	public final boolean rngAppliesAll(final int offset, int length, final Predicate<? super E> predicate)
	{
		EN e = this.getRangeChainEntry(offset, length); // validate range and scroll to offset
		try {
			if(length < 0) {
				for(; length++ < 0; e = e.prev) {
					if(!predicate.test(e.element())) {
						return false;
					}
				}
			}
			else {
				for(; length-- > 0; e = e.next) {
					if(!predicate.test(e.element())) {
						return false;
					}
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return true;
	}



	///////////////////////////////////////////////////////////////////////////
	//    counting      //
	/////////////////////

	// counting - element //

	@Override
	public final int count(final E element)
	{
		int count = 0;
		for(EN e = this.head.next; e != null; e = e.next){
			if(e.element() == element){
				count++;
			}
		}
		return count;
	}

	@Override
	public final int count(final E sample, final Equalator<? super E> equalator)
	{
		int count = 0;
		for(EN e = this.head.next; e != null; e = e.next){
			if(equalator.equal(e.element(), sample)) count++;
		}
		return count;
	}

	@Override
	public final int rngCount(final int offset, int length, final E element)
	{
		EN e = this.getRangeChainEntry(offset, length); // validate range and scroll to offset
		int count = 0;
		if(length < 0){
			for(; length ++< 0; e = e.prev){
				if(e.element() == element){
					count++;
				}
			}
		}
		else {
			for(; length --> 0; e = e.next){
				if(e.element() == element){
					count++;
				}
			}
		}
		return count;
	}

//	@Override
//	public final int rngCount(
//		final int offset,
//		      int length,
//		final E sample,
//		final Equalator<? super E> equalator
//	)
//	{
//		EN e = this.getRangeChainEntry(offset, length); // validate range and scroll to offset
//		int count = 0;
//		if(length < 0){
//			for(; length ++< 0; e = e.prev){
//				if(equalator.equal(e.element(), sample)){
//					count++;
//				}
//			}
//		}
//		else {
//			for(; length --> 0; e = e.next){
//				if(equalator.equal(e.element(), sample)){
//					count++;
//				}
//			}
//		}
//		return count;
//	}

	// counting - predicate //

	@Override
	public final int count(final Predicate<? super E> predicate)
	{
		int count = 0;
		try {
			for(EN e = this.head.next; e != null; e = e.next) {
				if(predicate.test(e.element())) {
					count++;
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return count;
	}

	@Override
	public final int rngCount(final int offset, int length, final Predicate<? super E> predicate)
	{
		EN e = this.getRangeChainEntry(offset, length); // validate range and scroll to offset
		int count = 0;
		try {
			if(length < 0) {
				for(; length++ < 0; e = e.prev) {
					if(predicate.test(e.element())) {
						count++;
					}
				}
			}
			else {
				for(; length-- > 0; e = e.next) {
					if(predicate.test(e.element())) {
						count++;
					}
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return count;
	}



	///////////////////////////////////////////////////////////////////////////
	// data arithmetic  //
	/////////////////////

	// data - data sets //

	@Override
	public final <C extends Procedure<? super E>> C intersect(
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		if(samples instanceof AbstractSimpleArrayCollection<?>){
			final E[] array = AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)samples);
			final int size = Jadoth.to_int(samples.size());
			ch: for(EN entry = this.head.next; entry != null; entry = entry.next){
				final E element = entry.element();
				for(int i = 0; i < size; i++){
					if(equalator.equal(element, array[i])){
						target.accept(element);
						continue ch;
					}
				}
			}
			return target;
		}

		/* has to be the long way around because:
		 * - can't directly pass an instance of type E to a collection of type ? extends E.
		 * - chain's equal element instances must be added, not samples'.
		 */
		final CachedSampleEquality<E> equalCurrentElement = new CachedSampleEquality<>(equalator);
		for(EN entry = this.head.next; entry != null; entry = entry.next){
			equalCurrentElement.sample = entry.element();
			if(samples.containsSearched(equalCurrentElement)){
				target.accept(equalCurrentElement.sample);
			}
		}
		return target;
	}

	@Override
	public final <C extends Procedure<? super E>> C except(
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		if(samples instanceof AbstractSimpleArrayCollection<?>){
			final E[] array = AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)samples);
			final int size = Jadoth.to_int(samples.size());
			ch: for(EN entry = this.head.next; entry != null; entry = entry.next){
				final E element = entry.element();
				for(int i = 0; i < size; i++){
					if(equalator.equal(element, array[i])){
						continue ch;
					}
				}
				target.accept(element);
			}
			return target;
		}

		/* has to be the long way around because:
		 * - can't directly pass an instance of type E to a collection of type ? extends E.
		 * - chain's equal element instances must be added, not samples'.
		 */
		final CachedSampleEquality<E> equalCurrentElement = new CachedSampleEquality<>(equalator);
		ch: for(EN entry = this.head.next; entry != null; entry = entry.next){
			equalCurrentElement.sample = entry.element();
			if(samples.containsSearched(equalCurrentElement)){
				continue ch;
			}
			target.accept(equalCurrentElement.sample);
		}
		return target;
	}

	@Override
	public final <C extends Procedure<? super E>> C union(
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		this.copyTo(target);
		if(samples instanceof AbstractSimpleArrayCollection<?>){
			final E[] array = AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)samples);
			final int size = Jadoth.to_int(samples.size());
			ch: for(int i = 0; i < size; i++){
				final E sample = array[i];
				for(EN entry = this.head.next; entry != null; entry = entry.next){
					if(equalator.equal(entry.element(), sample)){
						continue ch;
					}
				}
				target.accept(sample);
			}
			return target;
		}

		/* has to be the long way around because:
		 * - can't directly pass an instance of type E to a collection of type ? extends E.
		 * - chain's equal element instances must be added, not samples'.
		 */
		samples.iterate(new Procedure<E>(){
			@Override public void accept(final E e) {
				// local reference to AIC field
				final Equalator<? super E> equalator2 = equalator;

				for(EN entry = ChainStorageStrong.this.head.next; entry != null; entry = entry.next){
					if(equalator2.equal(e, entry.element())){
						return;
					}
				}
				target.accept(e);
			}}
		);
		return target;
	}

	@Override
	public final <C extends Procedure<? super E>> C rngIntersect(
		final int offset,
		      int length,
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		final EN first = this.getRangeChainEntry(offset, length);
		if(length < 0) length = -length;
		final AbstractChainEntry.Hopper ch = length < 0 ?AbstractChainEntry.hopPrev :AbstractChainEntry.hopNext;

		if(samples instanceof AbstractSimpleArrayCollection<?>){
			final E[] array = AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)samples);
			final int size = Jadoth.to_int(samples.size());
			ch: for(EN entry = first; length --> 0; entry = ch.hop(entry)){
				final E element = entry.element();
				for(int i = 0; i < size; i++){
					if(equalator.equal(element, array[i])){
						target.accept(element);
						continue ch;
					}
				}
			}
			return target;
		}

		/* has to be the long way around because:
		 * - can't directly pass an instance of type E to a collection of type ? extends E.
		 * - chain's equal element instances must be added, not samples'.
		 */
		final CachedSampleEquality<E> equalCurrentElement = new CachedSampleEquality<>(equalator);
		for(EN entry = first; length --> 0; entry = ch.hop(entry)){
			equalCurrentElement.sample = entry.element();
			if(samples.containsSearched(equalCurrentElement)){
				target.accept(equalCurrentElement.sample);
			}
		}
		return target;
	}

	@Override
	public final <C extends Procedure<? super E>> C rngExcept(
		final int offset,
		      int length,
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{

		final EN first = this.getRangeChainEntry(offset, length);
		if(length < 0) length = -length;
		final AbstractChainEntry.Hopper ch = length < 0 ?AbstractChainEntry.hopPrev :AbstractChainEntry.hopNext;

		if(samples instanceof AbstractSimpleArrayCollection<?>){
			final E[] array = AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)samples);
			final int size = Jadoth.to_int(samples.size());
			ch: for(EN entry = first; length --> 0; entry = ch.hop(entry)){
				final E element = entry.element();
				for(int i = 0; i < size; i++){
					if(equalator.equal(element, array[i])){
						continue ch;
					}
				}
				target.accept(element);
			}
			return target;
		}

		/* has to be the long way around because:
		 * - can't directly pass an instance of type E to a collection of type ? extends E.
		 * - chain's equal element instances must be added, not samples'.
		 */
		final CachedSampleEquality<E> equalCurrentElement = new CachedSampleEquality<>(equalator);
		ch: for(EN entry = first; length --> 0; entry = ch.hop(entry)){
			equalCurrentElement.sample = entry.element();
			if(samples.containsSearched(equalCurrentElement)){
				continue ch;
			}
			target.accept(equalCurrentElement.sample);
		}
		return target;
	}

	@Override
	public final <C extends Procedure<? super E>> C rngUnion(
		final int offset,
		final int length,
		final XGettingCollection<? extends E> samples,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		final EN first = this.getRangeChainEntry(offset, length);
		final AbstractChainEntry.Hopper ch = length < 0 ?AbstractChainEntry.hopPrev :AbstractChainEntry.hopNext;

		this.rngCopyTo(offset, length, target);
		if(samples instanceof AbstractSimpleArrayCollection<?>){
			final E[] array = AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)samples);
			final int size = Jadoth.to_int(samples.size());
			ar: for(int i = 0; i < size; i++){
				final E sample = array[i];
				int len = length;
				for(EN entry = first; len --> 0; entry = ch.hop(entry)){
					if(equalator.equal(entry.element(), sample)){
						continue ar;
					}
				}
				target.accept(sample);
			}
			return target;
		}

		final int normalizedLength = length >= 0 ?length :-length;
		samples.iterate(new Procedure<E>(){
			@Override public void accept(final E e) {
				// local reference to AIC field
				final Equalator<? super E> equalator2 = equalator;

				int len = normalizedLength;
				for(EN entry = first; len --> 0; entry = ch.hop(entry)){
					if(equalator2.equal(e, entry.element())){
						return;
					}
				}
				target.accept(e);
			}}
		);
		return target;
	}

	// data - copying //

	@Override
	public final <C extends Procedure<? super E>> C copyTo(final C target)
	{
		for(EN e = this.head.next; e != null; e = e.next){
			target.accept(e.element());
		}
		return target;
	}

	@Override
	public final <C extends Procedure<? super E>> C rngCopyTo(final int offset, int length, final C target)
	{
		final EN first;
		if((first = this.getRangeChainEntry(offset, length)) == null){
			return target;
		}

		if(length > 0){
			for(EN e = first; length --> 0; e = e.next){
				target.accept(e.element());
			}
		}
		else {
			for(EN e = first; length ++< 0; e = e.prev){
				target.accept(e.element());
			}
		}
		return target;
	}

	@Override
	public final <C extends Procedure<? super E>> C copySelection(final C target, final int... indices)
	{
		final int length = indices.length, size = Jadoth.to_int(this.parent.size());

		// validate all indices before copying the first element
		for(int i = 0, idx; i < length; i++){
			if((idx = indices[i]) < 0 || idx >= size){
				throw new IndexOutOfBoundsException(exceptionIndexOutOfBounds(size, idx));
			}
		}

		// actual copying. Note: can't sort indices as copying order might be relevant
		for(int i = 0; i < length; i++){
			target.accept(this.getChainEntry(indices[i]).element()); // scrolling is pretty inefficient here :(
		}
		return target;
	}

	@Override
	public final int copyToArray(final int offset, int length, final Object[] target, final int targetOffset)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null){
			return 0;
		}

		if(targetOffset < 0){
			throw new ArrayIndexOutOfBoundsException(targetOffset);
		}
		if((length < 0 ?-length :length)+targetOffset > target.length){
			throw new ArrayIndexOutOfBoundsException((length < 0 ?-length :length)+targetOffset);
		}

		int t = targetOffset;
		if(length > 0){
			for(; length --> 0; e = e.next){
				target[t++] = e.element();
			}
		}
		else {
			for(; length ++< 0; e = e.prev){
				target[t++] = e.element();
			}
		}
		return t - targetOffset;
	}

	// data - conditional copying //

	@Override
	public final <C extends Procedure<? super E>> C copyTo(final C target, final Predicate<? super E> predicate)
	{
		try {
			for(EN entry = this.head.next; entry != null; entry = entry.next) {
				if(predicate.test(entry.element())) {
					target.accept(entry.element());
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return target;
	}

	@Override
	public final <C extends Procedure<? super E>> C rngCopyTo(
		final int offset,
		      int length,
		final C target,
		final Predicate<? super E> predicate
	)
	{
		final EN first;
		if((first = this.getRangeChainEntry(offset, length)) == null){
			return target;
		}

		try {
			if(length > 0) {
				for(EN e = first; length-- > 0; e = e.next) {
					if(predicate.test(e.element())) {
						target.accept(e.element());
					}
				}
			}
			else {
				for(EN e = first; length++ < 0; e = e.prev) {
					if(predicate.test(e.element())) {
						target.accept(e.element());
					}
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return target;
	}

	// data - array transformation //

	@Override
	public final Object[] toArray()
	{
		final Object[] array;
		this.copyToArray(0, Jadoth.to_int(this.parent.size()), array = new Object[Jadoth.to_int(this.parent.size())], 0);
		return array;
	}

	@Override
	public final      E[] toArray(final Class<E> type)
	{
		final E[] array;
		this.copyToArray(0, Jadoth.to_int(this.parent.size()), array = JadothArrays.newArray(type, Jadoth.to_int(this.parent.size())), 0);
		return array;
	}

	@Override
	public final Object[] rngToArray(final int offset, final int length)
	{
		final Object[] array;
		this.copyToArray(offset, length, array = new Object[length < 0 ?-length :length], 0);
		return array;
	}

	@Override
	public final      E[] rngToArray(final int offset, final int length, final Class<E> type)
	{
		final E[] array;
		this.copyToArray(offset, length, array = JadothArrays.newArray(type, length < 0 ?-length :length), 0);
		return array;
	}



	///////////////////////////////////////////////////////////////////////////
	//    querying      //
	/////////////////////

	@Override
	public final E first()
	{
		return this.head.next == null ?null :this.head.next.element();
	}

	@Override
	public final E last()
	{
		return this.head.prev == this.head ?null :this.head.prev.element();
	}

	@Override
	public final E get(final int index)
	{
		return this.getChainEntry(index).element();
	}



	///////////////////////////////////////////////////////////////////////////
	//    searching     //
	/////////////////////

	// searching - sample //

	@Override
	public final E seek(final E sample, final Equalator<? super E> equalator)
	{
		for(EN e = this.head.next; e != null; e = e.next){
			if(equalator.equal(e.element(), sample)){
				return e.element();
			}
		}
		return null;
	}

//	@Override
//	public final E rngSeek(final int offset, int length, final E sample, final Equalator<? super E> equalator)
//	{
//		EN e;
//		if((e = this.getRangeChainEntry(offset, length)) == null) return null;
//
//		E element; // variable is necessary to defend against changing (e.g. weakly referencing) entries.
//		if(length > 0){
//			for(; length --> 0; e = e.next){
//				if(equalator.equal(element = e.element(), sample)){
//					return element;
//				}
//			}
//		}
//		else {
//			for(; length ++< 0; e = e.prev){
//				if(equalator.equal(element = e.element(), sample)){
//					return element;
//				}
//			}
//		}
//		return null;
//	}

	// searching - predicate //

	@Override
	public final E seek(final E sample)
	{
		for(EN e = this.head.next; e != null; e = e.next){
			if(sample == e.element()){
				return e.element();
			}
		}
		return null;
	}

	@Override
	public final E search(final Predicate<? super E> predicate)
	{
		try {
			for(EN e = this.head.next; e != null; e = e.next) {
				if(predicate.test(e.element())) {
					return e.element();
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return null;
	}

	@Override
	public final E rngSearch(final int offset, int length, final Predicate<? super E> predicate)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null){
			return null;
		}
		try {
			if(length > 0) {
				for(; length-- > 0; e = e.next) {
					if(predicate.test(e.element())) {
						return e.element();
					}
				}
			}
			else {
				for(; length++ < 0; e = e.prev) {
					if(predicate.test(e.element())) {
						return e.element();
					}
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return null;
	}

	// searching - min max //

	@Override
	public final E min(final Comparator<? super E> comparator)
	{
		EN e;
		if((e = this.head.next) == null){
			return null;
		}

		E element, loopMinElement = e.element();
		for(e = e.next; e != null; e = e.next){
			if(comparator.compare(loopMinElement, element = e.element()) > 0){
				loopMinElement = element;
			}
		}
		return loopMinElement;
	}

	@Override
	public final E max(final Comparator<? super E> comparator)
	{
		EN e;
		if((e = this.head.next) == null){
			return null;
		}

		E element, loopMaxElement = e.element();
		for(e = e.next; e != null; e = e.next){
			if(comparator.compare(loopMaxElement, element = e.element()) < 0){
				loopMaxElement = element;
			}
		}
		return loopMaxElement;
	}

	@Override
	public final E rngMin(final int offset, int length, final Comparator<? super E> comparator)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null){
			return null;
		}

		E element, loopMinElement = e.element();
		if(length > 0){
			for(e = e.next, length--; length --> 0; e = e.next){
				if(comparator.compare(loopMinElement, element = e.element()) > 0){
					loopMinElement = element;
				}
			}
		}
		else {
			for(e = e.prev, length++; length ++< 0; e = e.prev){
				if(comparator.compare(loopMinElement, element = e.element()) > 0){
					loopMinElement = element;
				}
			}
		}
		return loopMinElement;
	}

	@Override
	public final E rngMax(final int offset, int length, final Comparator<? super E> comparator)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null){
			return null;
		}

		E element, loopMaxElement = e.element();
		if(length > 0){
			for(e = e.next, length--; length --> 0; e = e.next){
				if(comparator.compare(loopMaxElement, element = e.element()) < 0){
					loopMaxElement = element;
				}
			}
		}
		else {
			for(e = e.prev, length++; length ++< 0; e = e.prev){
				if(comparator.compare(loopMaxElement, element = e.element()) < 0){
					loopMaxElement = element;
				}
			}
		}
		return loopMaxElement;
	}



	///////////////////////////////////////////////////////////////////////////
	//    executing     //
	/////////////////////

	// executing - procedure //

	@Override
	public final void iterate(final Procedure<? super E> procedure)
	{
		try {
			for(EN entry = this.head; (entry = entry.next) != null;) {
				procedure.accept(entry.element());
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */ }
	}

	@Override
	public final <A> void join(final BiProcedure<? super E, A> joiner, final A aggregate)
	{
		try {
			for(EN entry = this.head; (entry = entry.next) != null;) {
				joiner.accept(entry.element(), aggregate);
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */ }
	}

	@Override
	public final void rngIterate(final int offset, int length, final Procedure<? super E> procedure)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return;

		try {
			if(length > 0) {
				for(; length-- > 0; e = e.next) {
					procedure.accept(e.element());
				}
			}
			else {
				for(; length++ < 0; e = e.prev) {
					procedure.accept(e.element());
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
	}

	// executing - indexed procedure //

	@Override
	public final void iterate(final IndexProcedure<? super E> procedure)
	{
		try {
			int i = -1;
			for(EN entry = this.head; (entry = entry.next) != null;) {
				procedure.accept(entry.element(), ++i);
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */ }
	}

	private static <E, K, V, EN extends AbstractChainEntry<E, K, V, EN>> void rngIterateForward(
		      EN           entry ,
		      int                       offset,
		final int                       bound ,
		final IndexProcedure<? super E> procedure
	)
	{
		try {
			while(offset < bound) {
				procedure.accept((entry = entry.next).element(), offset++);
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */ }
	}

	private static <E, K, V, EN extends AbstractChainEntry<E, K, V, EN>> void rngIterateReverse(
		      EN           entry ,
		      int                       offset,
		final int                       bound ,
		final IndexProcedure<? super E> procedure
	)
	{
		try {
			while(offset > bound) {
				procedure.accept((entry = entry.prev).element(), offset--);
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */ }
	}

	@Override
	public final void rngIterate(final int offset, final int length, final IndexProcedure<? super E> procedure)
	{
		if(length >= 0){
			rngIterateForward(this.getRangeChainEntry(offset, length), offset, offset + length, procedure);
		}
		else {
			rngIterateReverse(this.getRangeChainEntry(offset, length), offset, offset + length, procedure);
		}
	}

	// executing - conditional //

	@Override
	public final void iterate(final Predicate<? super E> predicate, final Procedure<? super E> procedure)
	{
		try {
			for(EN e = this.head.next; e != null; e = e.next) {
				if(predicate.test(e.element())) {
					procedure.accept(e.element());
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
	}



	///////////////////////////////////////////////////////////////////////////
	//    indexing      //
	/////////////////////

	// indexing - single //

	@Override
	public final int indexOf(final E element)
	{
		int i = 0;
		for(EN e = this.head.next; e != null; e = e.next, i++){
			if(e.element() == element){
				return i;
			}
		}
		return -1;
	}

	@Override
	public final int indexOf(final E sample, final Equalator<? super E> equalator)
	{
		int i = 0;
		for(EN e = this.head.next; e != null; e = e.next, i++){
			if(equalator.equal(e.element(), sample)){
				return i;
			}
		}
		return -1;
	}

	@Override
	public final int rngIndexOf(int offset, final int length, final E element)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null){
			return -1;
		}

		final int bound = offset + length;
		if(length > 0){
			for(; offset != bound; e = e.next, offset++){
				if(e.element() == element){
					return offset;
				}
			}
		}
		else {
			for(; offset != bound; e = e.prev, offset--){
				if(e.element() == element){
					return offset;
				}
			}
		}
		return -1;
	}

	@Override
	public final int rngIndexOf(int offset, final int length, final E sample, final Equalator<? super E> equalator)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null){
			return -1;
		}

		final int bound = offset + length;
		if(length > 0){
			for(; offset != bound; e = e.next, offset++){
				if(equalator.equal(e.element(), sample)){
					return offset;
				}
			}
		}
		else {
			for(; offset != bound; e = e.prev, offset--){
				if(equalator.equal(e.element(), sample)){
					return offset;
				}
			}
		}
		return -1;
	}

	// indexing - predicate //

	@Override
	public final int indexOf(final Predicate<? super E> predicate)
	{
		int i = 0;
		try {
			for(EN e = this.head.next; e != null; e = e.next, i++) {
				if(predicate.test(e.element())) {
					return i;
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return -1;
	}

	@Override
	public final int rngIndexOf(int offset, final int length, final Predicate<? super E> predicate)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null){
			return -1;
		}

		final int bound = offset + length;
		try {
			if(length > 0) {
				for(; offset != bound; e = e.next, offset++) {
					if(predicate.test(e.element())) {
						return offset;
					}
				}
			}
			else {
				for(; offset != bound; e = e.prev, offset--) {
					if(predicate.test(e.element())) {
						return offset;
					}
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return -1;
	}

	// indexing - min max //

	@Override
	public final int minIndex(final Comparator<? super E> comparator)
	{
		EN e;
		if((e = this.head.next) == null){
			return -1;
		}

		E loopMinElement = e.element();
		int loopMinIndex = 0;
		int i = 1;
		for(e = e.next; e != null; e = e.next, i++){
			final E element;
			if(comparator.compare(loopMinElement, element = e.element()) > 0){
				loopMinElement = element;
				loopMinIndex = i;
			}
		}
		return loopMinIndex;
	}

	@Override
	public final int maxIndex(final Comparator<? super E> comparator)
	{
		EN e;
		if((e = this.head.next) == null){
			return -1;
		}

		E loopMaxElement = e.element();
		int loopMaxIndex = 0;
		int i = 1;
		for(e = e.next; e != null; e = e.next, i++){
			final E element;
			if(comparator.compare(loopMaxElement, element = e.element()) < 0){
				loopMaxElement = element;
				loopMaxIndex = i;
			}
		}
		return loopMaxIndex;
	}

	@Override
	public final int rngMinIndex(int offset, final int length, final Comparator<? super E> comparator)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null){
			return -1;
		}

		E loopMinElement = e.element();
		int loopMinIndex = 0;
		final int bound = offset + length;
		if(length > 0){
			for(E element; offset != bound; e = e.next, offset++){
				if(comparator.compare(loopMinElement, element = e.element()) > 0){
					loopMinElement = element;
					loopMinIndex = offset;
				}
			}
		}
		else {
			for(E element; offset != bound; e = e.prev, offset--){
				if(comparator.compare(loopMinElement, element = e.element()) > 0){
					loopMinElement = element;
					loopMinIndex = offset;
				}
			}
		}
		return loopMinIndex;
	}

	@Override
	public final int rngMaxIndex(int offset, final int length, final Comparator<? super E> comparator)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return -1;

		E loopMaxElement = e.element();
		int loopMaxIndex = 0;
		final int bound = offset + length;
		if(length > 0){
			for(E element; offset != bound; e = e.next, offset++){
				if(comparator.compare(loopMaxElement, element = e.element()) < 0){
					loopMaxElement = element;
					loopMaxIndex = offset;
				}
			}
		}
		else {
			for(E element; offset != bound; e = e.prev, offset--){
				if(comparator.compare(loopMaxElement, element = e.element()) < 0){
					loopMaxElement = element;
					loopMaxIndex = offset;
				}
			}
		}
		return loopMaxIndex;
	}

	// indexing - scan //

	@Override
	public final int scan(final Predicate<? super E> predicate)
	{
		int i = 0;
		int foundIndex = -1;
		for(EN e = this.head.next; e != null; e = e.next, i++){
			if(predicate.test(e.element())){
				foundIndex = i;
			}
		}
		return foundIndex;
	}

	@Override
	public final int rngScan(int offset, final int length, final Predicate<? super E> predicate)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return -1;

		final int bound = offset + length;
		int foundIndex = -1;
		if(length > 0){
			for(; offset != bound; e = e.next, offset++){
				if(predicate.test(e.element())){
					foundIndex = offset;
				}
			}
		}
		else {
			for(; offset != bound; e = e.prev, offset--){
				if(predicate.test(e.element())){
					foundIndex = offset;
				}
			}
		}
		return foundIndex;
	}



	///////////////////////////////////////////////////////////////////////////
	//   distinction    //
	/////////////////////

	@Override
	public final boolean hasDistinctValues()
	{
		for(EN e = this.head.next; e != null; e = e.next){
			final E element = e.element();
			for(EN lookAhead = e.next; lookAhead != null; lookAhead = lookAhead.next){
				if(element == lookAhead.element()){
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public final boolean hasDistinctValues(final Equalator<? super E> equalator)
	{
		for(EN e = this.head.next; e != null; e = e.next){
			final E element = e.element();
			for(EN lookAhead = e.next; lookAhead != null; lookAhead = lookAhead.next){
				if(equalator.equal(element, lookAhead.element())){
					return false;
				}
			}
		}
		return true;
	}

	// distinction copying //

	@Override
	public final <C extends Procedure<? super E>> C distinct(final C target)
	{
		return this.rngDistinct(Jadoth.to_int(this.parent.size()) - 1, Jadoth.to_int(this.parent.size()), target);
	}

	@Override
	public final <C extends Procedure<? super E>> C distinct(final C target, final Equalator<? super E> equalator)
	{
		return this.rngDistinct(Jadoth.to_int(this.parent.size()) - 1, Jadoth.to_int(this.parent.size()), target, equalator);
	}

	@Override
	public final <C extends Procedure<? super E>> C rngDistinct(final int offset, int length, final C target)
	{
		if(length == 0) return target; // required for correctness of direction reversion

		EN e;
		if((e = this.getRangeChainEntry(offset + length - (length>0?1:-1), -length)) == null) return target;

		// hopping direction (reversed for algorighm!)
		final AbstractChainEntry.Hopper ch = length < 0 ?hopNext :hopPrev;
		if(length < 0) length = -length;

		mainLoop: // find last distinct element in reverse order: means put first distinct element to target
		for(; length > 0; e = ch.hop(e), length--){
			final E element = e.element();
			for(EN lookAhead = ch.hop(e); lookAhead != null; lookAhead = ch.hop(lookAhead)){
				if(element == lookAhead.element()){
					continue mainLoop;
				}
			}
			target.accept(element);
		}

		return target;
	}

	@Override
	public final <C extends Procedure<? super E>> C rngDistinct(
		final int offset,
		      int length,
		final C target,
		final Equalator<? super E> equalator
	)
	{
		if(length == 0) return target; // required for correctness of direction reversion

		EN e;
		if((e = this.getRangeChainEntry(offset + length - (length>0?1:-1), -length)) == null) return target;

		// hopping direction (reversed for algorighm!)
		final AbstractChainEntry.Hopper ch = length < 0 ?hopNext :hopPrev;
		if(length < 0) length = -length;

		mainLoop: // find last distinct element in reverse order: means put first distinct element to target
		for(; length > 0; e = ch.hop(e), length--){
			final E element = e.element();
			for(EN lookAhead = ch.hop(e); lookAhead != null; lookAhead = ch.hop(lookAhead)){
				if(equalator.equal(element, lookAhead.element())){
					continue mainLoop;
				}
			}
			target.accept(element);
		}

		return target;
	}



	///////////////////////////////////////////////////////////////////////////
	// VarString appending //
	//////////////////////

	@Override
	public final VarString appendTo(final VarString vc)
	{
		for(EN entry = this.head.next; entry != null; entry = entry.next){
			vc.add(entry.element());
		}
		return vc;
	}

	@Override
	public final VarString appendTo(final VarString vc, final char seperator)
	{
		if(this.head.next == null){
			return vc;
		}
		for(EN entry = this.head; (entry = entry.next) != null;){
			entry.assembleElement(vc).add(seperator);
		}
		return vc.deleteLast();
	}

	@Override
	public final VarString appendTo(final VarString vc, final String seperator)
	{
		if(seperator == null || seperator.isEmpty()){
			return this.appendTo(vc);
		}

		if(this.head.next == null){
			return vc;
		}

		final char[] sepp = seperator.toCharArray();
		for(EN entry = this.head; (entry = entry.next) != null;){
			entry.assembleElement(vc).add(sepp);
		}
		return vc.deleteLast(sepp.length);
	}

	@Override
	public final VarString appendTo(final VarString vc, final BiProcedure<VarString, ? super E> appender)
	{
		for(EN entry = this.head.next; entry != null; entry = entry.next){
			appender.accept(vc, entry.element());
		}
		return vc;
	}

	@Override
	public final VarString appendTo(final VarString vc, final BiProcedure<VarString, ? super E> appender, final char seperator)
	{
		EN entry;
		if((entry = this.head.next) == null){
			return vc;
		}
		appender.accept(vc, entry.element());
		while((entry = entry.next) != null){
			appender.accept(vc.append(seperator), entry.element());
		}
		return vc;
	}

	@Override
	public final VarString appendTo(final VarString vc, final BiProcedure<VarString, ? super E> appender, final String seperator)
	{
		if(seperator == null || seperator.isEmpty()){
			return this.appendTo(vc, appender);
		}

		EN entry;
		if((entry = this.head.next) == null){
			return vc;
		}
		appender.accept(vc, entry.element());
		final char[] sepp = seperator.toCharArray();
		while((entry = entry.next) != null){
			appender.accept(vc.add(sepp), entry.element());
		}
		return vc;
	}

	@Override
	public final VarString rngAppendTo(int offset, final int length, final VarString vc)
	{
		EN entry;
		if((entry = this.getRangeChainEntry(offset, length)) == null){
			return vc;
		}
		final int bound = offset + length;
		vc.add(entry.element());
		if(length > 0){
			for(; ++offset != bound; entry = entry.next){
				vc.add(entry.element());
			}
		}
		else {
			for(; --offset != bound; entry = entry.prev){
				vc.add(entry.element());
			}
		}
		return vc;
	}

	@Override
	public final VarString rngAppendTo(int offset, final int length, final VarString vc, final char seperator)
	{
		EN entry;
		if((entry = this.getRangeChainEntry(offset, length)) == null){
			return vc;
		}
		final int bound = offset + length;
		vc.add(entry.element());
		if(length > 0){
			for(; ++offset != bound; entry = entry.next){
				vc.append(seperator).add(entry.element());
			}
		}
		else {
			for(; --offset != bound; entry = entry.prev){
				vc.append(seperator).add(entry.element());
			}
		}
		return vc;
	}

	@Override
	public final VarString rngAppendTo(int offset, final int length, final VarString vc, final String seperator)
	{
		if(seperator == null || seperator.isEmpty()){
			return this.rngAppendTo(offset, length, vc);
		}
		EN entry;
		if((entry = this.getRangeChainEntry(offset, length)) == null){
			return vc;
		}
		final char[] sepp = seperator.toCharArray();
		final int bound = offset + length;
		vc.add(entry.element());
		if(length > 0){
			for(; ++offset != bound; entry = entry.next){
				vc.add(sepp).add(entry.element());
			}
		}
		else {
			for(; --offset != bound; entry = entry.prev){
				vc.add(sepp).add(entry.element());
			}
		}
		return vc;
	}

	@Override
	public final VarString rngAppendTo(int offset, final int length, final VarString vc, final BiProcedure<VarString, ? super E> appender)
	{
		EN entry;
		if((entry = this.getRangeChainEntry(offset, length)) == null){
			return vc;
		}
		final int bound = offset + length;
		appender.accept(vc, entry.element());
		if(length > 0){
			for(; ++offset != bound; entry = entry.next){
				appender.accept(vc, entry.element());
			}
		}
		else {
			for(; --offset != bound; entry = entry.prev){
				appender.accept(vc, entry.element());
			}
		}
		return vc;
	}

	@Override
	public final VarString rngAppendTo(
		      int offset,
		final int length,
		final VarString vc,
		final BiProcedure<VarString, ? super E> appender,
		final char seperator
	)
	{
		EN entry;
		if((entry = this.getRangeChainEntry(offset, length)) == null){
			return vc;
		}
		final int bound = offset + length;
		appender.accept(vc, entry.element());
		if(length > 0){
			for(; ++offset != bound; entry = entry.next){
				appender.accept(vc.append(seperator), entry.element());
			}
		}
		else {
			for(; --offset != bound; entry = entry.prev){
				appender.accept(vc.append(seperator), entry.element());
			}
		}
		return vc;
	}

	@Override
	public final VarString rngAppendTo(
		      int offset,
		final int length,
		final VarString vc,
		final BiProcedure<VarString, ? super E> appender,
		final String seperator
	)
	{
		if(seperator == null || seperator.isEmpty()){
			return this.rngAppendTo(offset, length, vc, appender);
		}
		EN entry;
		if((entry = this.getRangeChainEntry(offset, length)) == null){
			return vc;
		}
		final char[] sepp = seperator.toCharArray();
		final int bound = offset + length;
		appender.accept(vc, entry.element());
		if(length > 0){
			for(; ++offset != bound; entry = entry.next){
				appender.accept(vc.add(sepp), entry.element());
			}
		}
		else {
			for(; --offset != bound; entry = entry.prev){
				appender.accept(vc.add(sepp), entry.element());
			}
		}
		return vc;
	}

	@Override
	public final String toString()
	{
		final VarString vc = VarString.New((int)(Jadoth.to_int(this.parent.size())*5.0f));
		for(EN e = this.head.next; e != null; e = e.next){
			vc.append('(').add(e.element()).add(')', '-');
		}
		if(vc.isEmpty()){
			vc.add('(', ')');
		}
		else {
			vc.deleteLast();
		}
		return vc.toString();
	}



	///////////////////////////////////////////////////////////////////////////
	//    removing      //
	/////////////////////

	// removing - indexed //

	@Override
	public final E remove(final int index)
	{
		final EN e;
		(e = this.getChainEntry(index)).removeFrom(this.parent);
		return e.element();
	}

	// removing - null //

	@Override
	public final int removeNull()
	{
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;

		int removeCount = 0;
		for(EN e = this.head.next; e != null; e = e.next){
			if(e.hasNullElement()){
				e.removeFrom(parent);
				removeCount++;
			}
		}
		return removeCount;
	}

	@Override
	public final int rngRemoveNull(int offset, final int length)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;
		int removeCount = 0;
		final int bound = offset + length;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		if(length > 0){
			for(; offset != bound; e = e.next, offset++){
				if(e.hasNullElement()){
					e.removeFrom(parent);
					removeCount++;
				}
			}
		}
		else {
			for(; offset != bound; e = e.prev, offset--){
				if(e.hasNullElement()){
					e.removeFrom(parent);
					removeCount++;
				}
			}
		}
		return removeCount;
	}

	// removing - one single //

	@Override
	public final E retrieve(final E element)
	{
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		for(EN e = this.head.next; e != null; e = e.next){
			if(e.element() == element){
				e.removeFrom(parent);
				return e.element();
			}
		}
		return null;
	}

	@Override
	public final E retrieve(final Predicate<? super E> predicate)
	{
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		for(EN e = this.head.next; e != null; e = e.next){
			if(predicate.test(e.element())){
				e.removeFrom(parent);
				return e.element();
			}
		}
		return null;
	}

	@Override
	public final E rngRetrieve(final int offset, int length, final E element)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return null;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		if(length > 0){
			for(; length --> 0; e = e.next){
				if(e.element() == element){
					e.removeFrom(parent);
					return e.element();
				}
			}
		}
		else {
			for(; length ++< 0; e = e.prev){
				if(e.element() == element){
					e.removeFrom(parent);
					return e.element();
				}
			}
		}
		return null;
	}

//	@Override
//	public final E rngRetrieve(final int offset, int length, final E sample, final Equalator<? super E> equalator)
//	{
//		EN e;
//		if((e = this.getRangeChainEntry(offset, length)) == null) return null;
//		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
//
//		if(length > 0){
//			for(; length --> 0; e = e.next){
//				if(equalator.equal(e.element(), sample)){
//					e.removeFrom(parent);
//					return e.element();
//				}
//			}
//		}
//		else {
//			for(; length ++< 0; e = e.prev){
//				if(equalator.equal(e.element(), sample)){
//					e.removeFrom(parent);
//					return e.element();
//				}
//			}
//		}
//		return null;
//	}

	@Override
	public final boolean removeOne(final E element)
	{
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		for(EN e = this.head.next; e != null; e = e.next){
			if(e.element() == element){
				e.removeFrom(parent);
				return true;
			}
		}
		return false;
	}

//	@Override
//	public final boolean removeOne(final E sample, final Equalator<? super E> equalator)
//	{
//		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
//		for(EN e = this.head.next; e != null; e = e.next){
//			if(equalator.equal(e.element(), sample)){
//				e.removeFrom(parent);
//				return true;
//			}
//		}
//		return false;
//	}

	// removing - multiple single //

	@Override
	public final int remove(final E element)
	{
		final int oldSize = Jadoth.to_int(this.parent.size());
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		for(EN e = this.head.next; e != null; e = e.next){
			if(e.element() == element){
				e.removeFrom(parent);
			}
		}
		return oldSize - Jadoth.to_int(this.parent.size());
	}

	@Override
	public final int remove(final E sample, final Equalator<? super E> equalator)
	{
		final int oldSize = Jadoth.to_int(this.parent.size());
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		for(EN e = this.head.next; e != null; e = e.next){
			if(equalator.equal(e.element(), sample)){
				e.removeFrom(parent);
			}
		}
		return oldSize - Jadoth.to_int(this.parent.size());
	}

	@Override
	public final int rngRemove(int offset, final int length, final E element)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;
		int removeCount = 0;
		final int bound = offset + length;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		if(length > 0){
			for(; offset != bound; e = e.next, offset++){
				if(e.element() == element){
					e.removeFrom(parent);
					removeCount++;
				}
			}
		}
		else {
			for(; offset != bound; e = e.prev, offset--){
				if(e.element() == element){
					e.removeFrom(parent);
					removeCount++;
				}
			}
		}
		return removeCount;
	}

//	@Override
//	public final int rngRemove(int offset, final int length, final E sample, final Equalator<? super E> equalator)
//	{
//		EN e;
//		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;
//		int removeCount = 0;
//		final int bound = offset + length;
//		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
//		if(length > 0){
//			for(; offset != bound; e = e.next, offset++){
//				if(equalator.equal(e.element(), sample)){
//					e.removeFrom(parent);
//					removeCount++;
//				}
//			}
//		}
//		else {
//			for(; offset != bound; e = e.prev, offset--){
//				if(equalator.equal(e.element(), sample)){
//					e.removeFrom(parent);
//					removeCount++;
//				}
//			}
//		}
//		return removeCount;
//	}

	// removing - multiple all array //

	@Override
	public final int removeAll(final E[] elements, final int elementsOffset, final int elementsLength)
	{
		final int d;
		if((d = ChainStorageStrong.validateArrayIteration(elements, elementsOffset, elementsLength)) == 0) return 0;
		final int elementsBound = elementsOffset + elementsLength;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;

		int removeCount = 0;
		for(EN e = this.head.next; e != null; e = e.next){
			final E element = e.element();
			for(int i = elementsOffset; i != elementsBound; i+=d){
				if(element == elements[i]){
					e.removeFrom(parent);
					removeCount++;
					break;
				}
			}
		}
		return removeCount;
	}

//	@Override
//	public final int removeAll(
//		final E[] samples,
//		final int samplesOffset,
//		final int samplesLength,
//		final Equalator<? super E> equalator
//	)
//	{
//		final int d;
//		if((d = ChainStrongStrongStorage.validateArrayIteration(samples, samplesOffset, samplesLength)) == 0) return 0;
//		final int elementsBound = samplesOffset + samplesLength;
//		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
//
//		int removeCount = 0;
//		for(EN e = this.head.next; e != null; e = e.next){
//			final E element = e.element();
//			for(int i = samplesOffset; i != elementsBound; i+=d){
//				if(equalator.equal(element, samples[i])){
//					e.removeFrom(parent);
//					removeCount++;
//					break;
//				}
//			}
//		}
//		return removeCount;
//	}

	@Override
	public final int rngRemoveAll(
		      int offset,
		final int length,
		final E[] elements,
		final int elementsOffset,
		final int elementsLength
	)
	{
		final int d;
		if((d = JadothArrays.validateArrayRange(elements, elementsOffset, elementsLength)) == 0) return 0;
		final EN first;
		if((first = this.getRangeChainEntry(offset, length)) == null) return 0;

		final int elementsBound = elementsOffset + elementsLength;
		final int bound = offset + length;
		int removeCount = 0;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		if(length > 0){
			for(EN e = first; offset != bound; e = e.next, offset++){
				for(int i = elementsOffset; i != elementsBound; i+=d){
					if(e.element() == elements[i]){
						e.removeFrom(parent);
						removeCount++;
						break;
					}
				}
			}
		}
		else {
			for(EN e = first; offset != bound; e = e.prev, offset++){
				for(int i = elementsOffset; i != elementsBound; i+=d){
					if(e.element() == elements[i]){
						e.removeFrom(parent);
						removeCount++;
						break;
					}
				}
			}
		}
		return removeCount;
	}

//	@Override
//	public final int rngRemoveAll(
//		      int offset,
//		final int length,
//		final E[] samples,
//		final int samplesOffset,
//		final int samplesLength,
//		final Equalator<? super E> equalator
//	)
//	{
//		final int d;
//		if((d = JaArrays.validateArrayRange(samples, samplesOffset, samplesLength)) == 0) return 0;
//		final EN first;
//		if((first = this.getRangeChainEntry(offset, length)) == null) return 0;
//
//		final int elementsBound = samplesOffset + samplesLength;
//		final int bound = offset + length;
//		int removeCount = 0;
//		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
//		if(length > 0){
//			for(EN e = first; offset != bound; e = e.next, offset++){
//				final E element = e.element();
//				for(int i = samplesOffset; i != elementsBound; i+=d){
//					if(equalator.equal(element, samples[i])){
//						e.removeFrom(parent);
//						removeCount++;
//						break;
//					}
//				}
//			}
//		}
//		else {
//			for(EN e = first; offset != bound; e = e.prev, offset++){
//				final E element = e.element();
//				for(int i = samplesOffset; i != elementsBound; i+=d){
//					if(equalator.equal(element, samples[i])){
//						e.removeFrom(parent);
//						removeCount++;
//						break;
//					}
//				}
//			}
//		}
//		return removeCount;
//	}

	// removing - multiple all collection //

	@Override
	public final int removeAll(final XGettingCollection<? extends E> elements)
	{
		if(elements instanceof AbstractSimpleArrayCollection<?>){
			return this.rngRemoveAll(
				0,
				Jadoth.to_int(this.parent.size()),
				AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements),
				0,
				Jadoth.to_int(elements.size())
			);
		}

		return elements.iterate(new Aggregator<E, Integer>(){
			private int removeCount = 0;
			@Override public void accept(final E e) {
				this.removeCount += ChainStorageStrong.this.remove(e);
			}
			@Override public Integer yield(){
				return this.removeCount;
			}
		}).yield();
	}

//	@Override
//	public final int removeAll(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
//	{
//		if(samples instanceof AbstractSimpleArrayCollection<?>){
//			return this.rngRemoveAll(0, this.parent.size(),
//				(E[])((AbstractSimpleArrayCollection<?>)samples).internalGetStorageArray(), 0, samples.size(),
//				equalator
//			);
//		}
//
//		return samples.iterate(new Aggregator<E, Integer>(){
//			private int removeCount = 0;
//			@Override public void accept(final E e) {
//				this.removeCount += ChainStrongStrongStorage.this.remove(e, equalator);
//			}
//			@Override public Integer yield(){
//				return this.removeCount;
//			}
//		});
//	}

	@Override
	public final int rngRemoveAll(final int offset, final int length, final XGettingCollection<? extends E> elements)
	{
		if(elements instanceof AbstractSimpleArrayCollection<?>){
			return this.rngRemoveAll(
				offset,
				length,
				AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements),
				0,
				Jadoth.to_int(elements.size())
			);
		}

		return elements.iterate(new Aggregator<E, Integer>(){
			private int removeCount = 0;
			@Override public void accept(final E e) {
				this.removeCount += ChainStorageStrong.this.rngRemove(offset, length, e);
			}
			@Override public Integer yield(){
				return this.removeCount;
			}
		}).yield();
	}

//	@Override
//	public final int rngRemoveAll(
//		final int offset,
//		final int length,
//		final XGettingCollection<? extends E> samples,
//		final Equalator<? super E> equalator
//	)
//	{
//		if(samples instanceof AbstractSimpleArrayCollection<?>){
//			return this.rngRemoveAll(offset, length,
//				(E[])((AbstractSimpleArrayCollection<?>)samples).internalGetStorageArray(), 0, samples.size(),
//				equalator
//			);
//		}
//
//		return samples.iterate(new Aggregator<E, Integer>(){
//			private int removeCount = 0;
//			@Override public void accept(final E e) {
//				this.removeCount += ChainStrongStrongStorage.this.rngRemove(offset, length, e, equalator);
//			}
//			@Override public Integer yield(){
//				return this.removeCount;
//			}
//		});
//	}

	// removing - duplicates //

	@Override
	public final int removeDuplicates()
	{
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;

		int removeCount = 0;
		for(EN e = this.head.next; e != null; e = e.next){
			final E element = e.element();
			for(EN lookAhead = e.next; lookAhead != null; lookAhead = lookAhead.next){
				if(element == lookAhead.element()){
					lookAhead.removeFrom(parent);
					removeCount++;
				}
			}
		}
		return removeCount;
	}

	@Override
	public final int removeDuplicates(final Equalator<? super E> equalator)
	{
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;

		int removeCount = 0;
		for(EN e = this.head.next; e != null; e = e.next){
			final E element = e.element();
			for(EN lookAhead = e.next; lookAhead != null; lookAhead = lookAhead.next){
				if(equalator.equal(element, lookAhead.element())){
					lookAhead.removeFrom(parent);
					removeCount++;
				}
			}
		}
		return removeCount;
	}

	@Override
	public final int rngRemoveDuplicates(final int offset, int length)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;

		// hopping direction
		final AbstractChainEntry.Hopper ch = length < 0 ?hopPrev :hopNext;
		if(length < 0) length = -length;

		int removeCount = 0;
		for(; length > 0; e = ch.hop(e), length--){
			final E element = e.element();
			for(EN lookAhead = ch.hop(e); lookAhead != null; lookAhead = ch.hop(lookAhead)){
				if(element == lookAhead.element()){
					lookAhead.removeFrom(parent);
					removeCount++;
				}
			}
		}
		return removeCount;
	}

	@Override
	public final int rngRemoveDuplicates(final int offset, int length, final Equalator<? super E> equalator)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;

		// hopping direction
		final AbstractChainEntry.Hopper ch = length < 0 ?hopPrev :hopNext;
		if(length < 0) length = -length;

		int removeCount = 0;
		for(; length > 0; e = ch.hop(e), length--){
			final E element = e.element();
			for(EN lookAhead = ch.hop(e); lookAhead != null; lookAhead = ch.hop(lookAhead)){
				if(equalator.equal(element, lookAhead.element())){
					lookAhead.removeFrom(parent);
					removeCount++;
				}
			}
		}
		return removeCount;
	}



	///////////////////////////////////////////////////////////////////////////
	//    reducing      //
	/////////////////////

	// reducing - predicate //

	@Override
	public final int reduce(final Predicate<? super E> predicate)
	{
		int removeCount = 0;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		for(EN e = this.head.next; e != null; e = e.next){
			if(predicate.test(e.element())){
				e.removeFrom(parent);
				removeCount++;
			}
		}
		return removeCount;
	}

	@Override
	public final int rngReduce(int offset, final int length, final Predicate<? super E> predicate)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;
		int removeCount = 0;
		final int bound = offset + length;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		try {
			if(length > 0) {
				for(; offset != bound; e = e.next, offset++) {
					if(predicate.test(e.element())) {
						e.removeFrom(parent);
						removeCount++;
					}
				}
			}
			else {
				for(; offset != bound; e = e.prev, offset--) {
					if(predicate.test(e.element())) {
						e.removeFrom(parent);
						removeCount++;
					}
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */ }
		return removeCount;
	}



	///////////////////////////////////////////////////////////////////////////
	//    retaining     //
	/////////////////////

	// retaining - array //

	@Override
	public final int retainAll(final E[] elements, final int elementsOffset, final int elementsLength)
	{
		final int d;
		if((d = ChainStorageStrong.validateArrayIteration(elements, elementsOffset, elementsLength)) == 0) return 0;
		final int elementsBound = elementsOffset + elementsLength;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;

		int removeCount = 0;
		main: for(EN e = this.head.next; e != null; e = e.next){
			final E element = e.element();
			for(int i = elementsOffset; i != elementsBound; i+=d){
				if(element == elements[i]){
					continue main;
				}
			}
			e.removeFrom(parent);
			removeCount++;
		}
		return removeCount;
	}

	public final int retainAll(
		final E[] samples,
		final int samplesOffset,
		final int samplesLength,
		final Equalator<? super E> equalator
	)
	{
		final int d;
		if((d = ChainStorageStrong.validateArrayIteration(samples, samplesOffset, samplesLength)) == 0) return 0;
		final int samplesBound = samplesOffset + samplesLength;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;

		int removeCount = 0;
		main: for(EN e = this.head.next; e != null; e = e.next){
			final E element = e.element();
			for(int i = samplesOffset; i != samplesBound; i+=d){
				if(equalator.equal(element, samples[i])){
					continue main;
				}
			}
			e.removeFrom(parent);
			removeCount++;
		}
		return removeCount;
	}

	@Override
	public final int rngRetainAll(
		      int offset,
		final int length,
		final E[] elements,
		final int elementsOffset,
		final int elementsLength
	)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;
		final int d;
		if((d = ChainStorageStrong.validateArrayIteration(elements, elementsOffset, elementsLength)) == 0) return 0;
		final int bound = offset + length;
		final int elementsBound = elementsOffset + elementsLength;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;

		int removeCount = 0;
		if(length > 0){
			main: for(; offset != bound; e = e.next, offset++){
				final E element = e.element();
				for(int i = elementsOffset; i != elementsBound; i+=d){
					if(element == elements[i]){
						continue main;
					}
				}
				e.removeFrom(parent);
				removeCount++;
			}
		}
		else {
			main: for(; offset != bound; e = e.prev, offset--){
				final E element = e.element();
				for(int i = elementsOffset; i != elementsBound; i+=d){
					if(element == elements[i]){
						continue main;
					}
				}
				e.removeFrom(parent);
				removeCount++;
			}
		}
		return removeCount;
	}

//	@Override
//	public final int rngRetainAll(
//		      int offset,
//		final int length,
//		final E[] samples,
//		final int samplesOffset,
//		final int samplesLength,
//		final Equalator<? super E> equalator
//	)
//	{
//		EN e;
//		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;
//		final int d;
//		if((d = ChainStrongStrongStorage.validateArrayIteration(samples, samplesOffset, samplesLength)) == 0) return 0;
//		final int bound = offset + length;
//		final int samplesBound = samplesOffset + samplesLength;
//		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
//
//		int removeCount = 0;
//		if(length > 0){
//			main: for(; offset != bound; e = e.next, offset++){
//				final E element = e.element();
//				for(int i = samplesOffset; i != samplesBound; i+=d){
//					if(equalator.equal(element, samples[i])){
//						continue main;
//					}
//				}
//				e.removeFrom(parent);
//				removeCount++;
//			}
//		}
//		else {
//			main: for(; offset != bound; e = e.prev, offset--){
//				final E element = e.element();
//				for(int i = samplesOffset; i != samplesBound; i+=d){
//					if(equalator.equal(element, samples[i])){
//						continue main;
//					}
//				}
//				e.removeFrom(parent);
//				removeCount++;
//			}
//		}
//		return removeCount;
//	}

	// retaining - collection //

	@Override
	public final int retainAll(final XGettingCollection<? extends E> elements)
	{
		if(elements instanceof AbstractSimpleArrayCollection<?>){
			// directly check array against array without predicate function or method calls
			return this.retainAll(
				AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements),
				0,
				Jadoth.to_int(elements.size())
			);
		}

		final ElementIsContained<E> currentElement = new ElementIsContained<>();
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		int removeCount = 0;
		for(EN e = this.head.next; e != null; e = e.next){
			currentElement.element = e.element();
			if(!elements.containsSearched(currentElement)){
				e.removeFrom(parent);
				removeCount++;
			}
		}
		return removeCount;
	}

	@Override
	public final int retainAll(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		if(samples instanceof AbstractSimpleArrayCollection<?>){
			// directly check array against array without predicate function or method calls
			return this.retainAll(
				AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)samples),
				0,
				Jadoth.to_int(samples.size()),
				equalator
			);
		}

		final CachedSampleEquality<E> equalCurrentElement = new CachedSampleEquality<>(equalator);
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		int removeCount = 0;
		for(EN e = this.head.next; e != null; e = e.next){
			equalCurrentElement.sample = e.element();
			if(!samples.containsSearched(equalCurrentElement)){
				e.removeFrom(parent);
				removeCount++;
			}
		}
		return removeCount;
	}

	@Override
	public final int rngRetainAll(int offset, final int length, final XGettingCollection<? extends E> elements)
	{
		if(elements instanceof AbstractSimpleArrayCollection<?>){
			// directly check array against array without predicate function or method calls
			return this.rngRetainAll(
				offset,
				length,
				AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements),
				0,
				Jadoth.to_int(elements.size())
			);
		}

		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;
		int removeCount = 0;
		final int bound = offset + length;
		final ElementIsContained<E> currentElement = new ElementIsContained<>();
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		if(length > 0){
			for(; offset != bound; e = e.next, offset++){
				currentElement.element = e.element();
				if(!elements.containsSearched(currentElement)){
					e.removeFrom(parent);
					removeCount++;
				}
			}
		}
		else {
			for(; offset != bound; e = e.prev, offset--){
				currentElement.element = e.element();
				if(!elements.containsSearched(currentElement)){
					e.removeFrom(parent);
					removeCount++;
				}
			}
		}
		return removeCount;
	}

//	@Override
//	public final int rngRetainAll(
//		      int offset,
//		final int length,
//		final XGettingCollection<? extends E> samples,
//		final Equalator<? super E> equalator
//	)
//	{
//		if(samples instanceof AbstractSimpleArrayCollection<?>){
//			// directly check array against array without predicate function or method calls
//			return this.rngRetainAll(offset, length,
//				(E[])((AbstractSimpleArrayCollection<?>)samples).internalGetStorageArray(), 0, samples.size(),
//				equalator
//			);
//		}
//
//		EN e;
//		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;
//		int removeCount = 0;
//		final int bound = offset + length;
//		final CachedSampleEquality<E> equalCurrentElement = new CachedSampleEquality<>(equalator);
//		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
//		if(length > 0){
//			for(; offset != bound; e = e.next, offset++){
//				equalCurrentElement.sample = e.element();
//				if(!samples.contains(equalCurrentElement)){
//					e.removeFrom(parent);
//					removeCount++;
//				}
//			}
//		}
//		else {
//			for(; offset != bound; e = e.prev, offset--){
//				equalCurrentElement.sample = e.element();
//				if(!samples.contains(equalCurrentElement)){
//					e.removeFrom(parent);
//					removeCount++;
//				}
//			}
//		}
//		return removeCount;
//	}



	///////////////////////////////////////////////////////////////////////////
	//    processing    //
	/////////////////////

	@Override
	public final int process(final Procedure<? super E> procedure)
	{
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		int removeCount = 0;
		try {
			for(EN e = this.head.next; e != null; e = e.next) {
				procedure.accept(e.element());
				e.removeFrom(parent);
				removeCount++;
			}
		}
		catch(final ThrowBreak b) {
			removeCount += parent.internalClear();
		}
		return removeCount;
	}

	@Override
	public final int rngProcess(int offset, final int length, final Procedure<? super E> procedure)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;
		int removeCount = 0;
		final int bound = offset + length;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		try {
			if(length > 0) {
				for(; offset != bound; e = e.next, offset++) {
					procedure.accept(e.element());
					e.removeFrom(parent);
					removeCount++;
				}
			}
			else {
				for(; offset != bound; e = e.prev, offset--) {
					procedure.accept(e.element());
					e.removeFrom(parent);
					removeCount++;
				}
			}
		}
		catch(final ThrowBreak b) {
			removeCount += parent.internalClear();
		}
		return removeCount;
	}




	///////////////////////////////////////////////////////////////////////////
	//     moving       //
	/////////////////////

	@Override
	public final int moveRange(int offset, final int length, final Procedure<? super E> target)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		final int bound = offset + length;

		if(length > 0){
			for(; offset != bound; e = e.next, offset++){
				target.accept(e.element());
				e.removeFrom(parent);
			}
		}
		else {
			for(; offset != bound; e = e.prev, offset--){
				target.accept(e.element());
				e.removeFrom(parent);
			}
		}
		return length < 0 ?-length :length;
	}

	@Override
	public final int moveSelection(final Procedure<? super E> target, final int... indices)
	{
		final int indicesLength = indices.length, size = Jadoth.to_int(this.parent.size());

		// validate all indices before copying the first element
		for(int i = 0; i < indicesLength; i++){
			if(indices[i] < 0 || indices[i] >= size){
				throw new IndexOutOfBoundsException(exceptionIndexOutOfBounds(size, indices[i]));
			}
		}

		final AbstractChainCollection<E, K, V, EN> parent = this.parent;

		// actual copying. Note: can't sort indices as copying order might be relevant
		EN e;
		for(int i = 0; i < indicesLength; i++){
			target.accept((e = this.getChainEntry(indices[i])).element()); // pretty inefficient scrolling here
			e.removeFrom(parent); // remove not until adding to target has been successful
		}

		return indicesLength; // removeCount is equal to index count if no exception occured
	}

	// moving - conditional //

	@Override
	public final int moveTo(final Procedure<? super E> target, final Predicate<? super E> predicate)
	{
		int removeCount = 0;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;
		for(EN e = this.head.next; e != null; e = e.next){
			final E element;
			if(predicate.test(element = e.element())){
				target.accept(element);
				e.removeFrom(parent);
				removeCount++;
			}
		}
		return removeCount;
	}

	@Override
	public final int rngMoveTo(
		      int offset,
		final int length,
		final Procedure<? super E> target,
		final Predicate<? super E> predicate
	)
	{
		final int bound = offset + length;
		int removeCount = 0;
		final EN first;
		if((first = this.getRangeChainEntry(offset, length)) == null) return 0;
		final AbstractChainCollection<E, K, V, EN> parent = this.parent;

		if(length > 0){
			for(EN e = first; offset != bound; e = e.next, offset++){
				if(predicate.test(e.element())){
					target.accept(e.element());
					e.removeFrom(parent);
					removeCount++;
				}
			}
		}
		else {
			for(EN e = first; offset != bound; e = e.prev, offset--){
				if(predicate.test(e.element())){
					target.accept(e.element());
					e.removeFrom(parent);
					removeCount++;
				}
			}
		}
		return removeCount;
	}



	///////////////////////////////////////////////////////////////////////////
	//     sorting       //
	//////////////////////

	@Override
	public final void sort(final Comparator<? super E> comparator)
	{
		// validate comparator before the chain gets splitted
		if(comparator == null){
			throw new NullPointerException();
		}
		if(Jadoth.to_int(this.parent.size()) <= 1){
			return; // empty or trivial chain is always sorted
		}
		mergesortHead(this.head, comparator);
	}

	@Override
	public final void rngSort(final int offset, int length, final Comparator<? super E> comparator)
	{
		// validate comparator before the chain gets splitted
		if(comparator == null) throw new NullPointerException();

		final EN preFirst;
		if(length <= 1 && length >= -1){
			return; // empty or trivial subchain is always sorted
		}
		else if(length > 0){
			preFirst = this.getRangeChainEntry(offset -      1,  length + 1);
		}
		else {
			preFirst = this.getRangeChainEntry(offset - length, -length + 1);
			length = -length;
		}

		EN postLast = preFirst.next;
		while(length --> 0){
			postLast = postLast.next;
		}

		mergesortRange(preFirst, postLast, this.head, comparator);
	}

	@Override
	public final boolean isSorted(final Comparator<? super E> comparator)
	{
		EN e;
		if((e = this.head.next) == null){
			return true; // empty chain is sorted
		}

		E loopLastElement = e.element();
		while((e = e.next) != null){
			final E element;
			if(comparator.compare(loopLastElement, element = e.element()) > 0){
				return false;
			}
			loopLastElement = element;
		}
		return true;
	}

	@Override
	public final boolean rngIsSorted(final int offset, int length, final Comparator<? super E> comparator)
	{
		EN e = this.getRangeChainEntry(offset, length);
		E loopLastElement = e.element();
		if(length > 0){
			for(; length --> 0; e = e.next){
				final E element;
				if(comparator.compare(loopLastElement, element = e.element()) > 0){
					return false;
				}
				loopLastElement = element;
			}
		}
		else {
			for(; length ++< 0; e = e.prev){
				final E element;
				if(comparator.compare(loopLastElement, element = e.element()) > 0){
					return false;
				}
				loopLastElement = element;
			}
		}
		return true;
	}



	///////////////////////////////////////////////////////////////////////////
	//     setting      //
	/////////////////////

	@SafeVarargs
	@Override
	public final void set(final int offset, final E... elements)
	{
		EN e = this.getRangeChainEntry(offset, elements.length);
		for(int i = 0; i < elements.length; i++){
			e.setElement0(elements[i]);
			e = e.next;
		}
	}

	@Override
	public final void set(final int offset, final E[] elements, final int elementsOffset, final int elementsLength)
	{
		EN e = this.getRangeChainEntry(offset, elementsLength);
		final int d = JadothArrays.validateArrayRange(elements, offset, elementsLength);
		for(int i = elementsOffset, bound = elementsOffset + elementsLength; i != bound; i+=d){
			e.setElement0(elements[i]);
			e = e.next;
		}
	}

	@Override
	public final void fill(int offset, final int length, final E element)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null){
			return;
		}
		final int bound = offset + length;
		if(length > 0){
			for(; offset != bound; e = e.next, offset++){
				e.setElement0(element);
			}
		}
		else {
			for(; offset != bound; e = e.prev, offset--){
				e.setElement0(element);
			}
		}
	}



	///////////////////////////////////////////////////////////////////////////
	//    replacing     //
	/////////////////////

	// replacing - one single //

	@Override
	public final int replaceOne(final E element, final E replacement)
	{
		int i = 0;
		for(EN e = this.head.next; e != null; e = e.next){
			if(e.element() == element){
				e.setElement0(replacement);
				return i;
			}
			i++;
		}
		return -1;
	}

//	@Override
//	public final int replaceOne(final E sample, final Equalator<? super E> equalator, final E replacement)
//	{
//		int i = 0;
//		for(EN e = this.head.next; e != null; e = e.next){
//			if(equalator.equal(e.element(), sample)){
//				e.setElement(replacement);
//				return i;
//			}
//			i++;
//		}
//		return -1;
//	}

	@Override
	public final int rngReplaceOne(int offset, final int length, final E element, final E replacement)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return -1;

		final int bound = offset + length;
		if(length > 0){
			for(; offset != bound; e = e.next, offset++){
				if(e.element() == element){
					e.setElement0(replacement);
					return offset;
				}
			}
		}
		else {
			for(; offset != bound; e = e.prev, offset--){
				if(e.element() == element){
					e.setElement0(replacement);
					return offset;
				}
			}
		}
		return -1;
	}

//	@Override
//	public final int rngReplaceOne(
//		      int offset,
//		final int length,
//		final E sample,
//		final Equalator<? super E> equalator,
//		final E replacement
//	)
//	{
//		EN e;
//		if((e = this.getRangeChainEntry(offset, length)) == null) return -1;
//
//		final int bound = offset + length;
//		if(length > 0){
//			for(; offset != bound; e = e.next, offset++){
//				if(equalator.equal(e.element(), sample)){
//					e.setElement(replacement);
//					return offset;
//				}
//			}
//		}
//		else {
//			for(; offset != bound; e = e.prev, offset--){
//				if(equalator.equal(e.element(), sample)){
//					e.setElement(replacement);
//					return offset;
//				}
//			}
//		}
//		return -1;
//	}

	// replacing - multiple single //

	@Override
	public final int replace(final E element, final E replacement)
	{
		int replaceCount = 0;
		for(EN e = this.head.next; e != null; e = e.next){
			if(e.element() == element){
				e.setElement0(replacement);
				replaceCount++;
			}
		}
		return replaceCount;
	}

//	@Override
//	public final int replace(final E sample, final Equalator<? super E> equalator, final E replacement)
//	{
//		int replaceCount = 0;
//		for(EN e = this.head.next; e != null; e = e.next){
//			if(equalator.equal(e.element(), sample)){
//				e.setElement(replacement);
//				replaceCount++;
//			}
//		}
//		return replaceCount;
//	}

	@Override
	public final int rngReplace(final int offset, int length, final E element, final E replacement)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;

		int replaceCount = 0;
		if(length > 0){
			for(; length --> 0; e = e.next){
				if(e.element() == element){
					e.setElement0(replacement);
					replaceCount++;
				}
			}
		}
		else {
			for(; length ++< 0; e = e.prev){
				if(e.element() == element){
					e.setElement0(replacement);
					replaceCount++;
				}
			}
		}
		return replaceCount;
	}

	@Override
	public final int rngReplace(
		final int offset,
		      int length,
		final E sample,
		final Equalator<? super E> equalator,
		final E replacement
	)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;

		int replaceCount = 0;
		if(length > 0){
			for(; length --> 0; e = e.next){
				if(equalator.equal(e.element(), sample)){
					e.setElement0(replacement);
					replaceCount++;
				}
			}
		}
		else {
			for(; length ++< 0; e = e.prev){
				if(equalator.equal(e.element(), sample)){
					e.setElement0(replacement);
					replaceCount++;
				}
			}
		}
		return replaceCount;
	}

	// replacing - multiple all array //

	@Override
	public final int replaceAll(final E[] elements, final int elementsOffset, final int elementsLength, final E replacement)
	{
		final int d;
		if((d = ChainStorageStrong.validateArrayIteration(elements, elementsOffset, elementsLength)) == 0) return 0;
		final int elementsBound = elementsOffset + elementsLength;

		int replaceCount = 0;
		for(EN e = this.head.next; e != null; e = e.next){
			final E element = e.element();
			for(int i = elementsOffset; i != elementsBound; i+=d){
				if(element == elements[i]){
					e.setElement0(replacement);
					replaceCount++;
					break;
				}
			}
		}
		return replaceCount;
	}

//	@Override
//	public final int replaceAll(
//		final E[] samples,
//		final int samplesOffset,
//		final int samplesLength,
//		final Equalator<? super E> equalator,
//		final E replacement
//	)
//	{
//		final int d;
//		if((d = ChainStrongStrongStorage.validateArrayIteration(samples, samplesOffset, samplesLength)) == 0) return 0;
//		final int elementsBound = samplesOffset + samplesLength;
//
//		int replaceCount = 0;
//		for(EN e = this.head.next; e != null; e = e.next){
//			final E element = e.element();
//			for(int i = samplesOffset; i != elementsBound; i+=d){
//				if(equalator.equal(element, samples[i])){
//					e.setElement(replacement);
//					replaceCount++;
//					break;
//				}
//			}
//		}
//		return replaceCount;
//	}

	@Override
	public final int rngReplaceAll(
		      int offset,
		final int length,
		final E[] elements,
		final int elementsOffset,
		final int elementsLength,
		final E replacement
	)
	{
		final int d;
		if((d = JadothArrays.validateArrayRange(elements, elementsOffset, elementsLength)) == 0) return 0;
		final EN first;
		if((first = this.getRangeChainEntry(offset, length)) == null) return 0;

		final int elementsBound = elementsOffset + elementsLength;
		final int bound = offset + length;
		int replaceCount = 0;
		if(length > 0){
			for(EN e = first; offset != bound; e = e.next, offset++){
				for(int i = elementsOffset; i != elementsBound; i+=d){
					if(e.element() == elements[i]){
						e.setElement0(replacement);
						replaceCount++;
						break;
					}
				}
			}
		}
		else {
			for(EN e = first; offset != bound; e = e.prev, offset++){
				for(int i = elementsOffset; i != elementsBound; i+=d){
					if(e.element() == elements[i]){
						e.setElement0(replacement);
						replaceCount++;
						break;
					}
				}
			}
		}
		return replaceCount;
	}

//	@Override
//	public final int rngReplaceAll(
//		      int offset,
//		final int length,
//		final E[] samples,
//		final int samplesOffset,
//		final int samplesLength,
//		final Equalator<? super E> equalator,
//		final E replacement
//	)
//	{
//		final int d;
//		if((d = JaArrays.validateArrayRange(samples, samplesOffset, samplesLength)) == 0) return 0;
//		final EN first;
//		if((first = this.getRangeChainEntry(offset, length)) == null) return 0;
//
//		final int elementsBound = samplesOffset + samplesLength;
//		final int bound = offset + length;
//		int replaceCount = 0;
//		if(length > 0){
//			for(EN e = first; offset != bound; e = e.next, offset++){
//				final E element = e.element();
//				for(int i = samplesOffset; i != elementsBound; i+=d){
//					if(equalator.equal(element, samples[i])){
//						e.setElement(replacement);
//						replaceCount++;
//						break;
//					}
//				}
//			}
//		}
//		else {
//			for(EN e = first; offset != bound; e = e.prev, offset++){
//				final E element = e.element();
//				for(int i = samplesOffset; i != elementsBound; i+=d){
//					if(equalator.equal(element, samples[i])){
//						e.setElement(replacement);
//						replaceCount++;
//						break;
//					}
//				}
//			}
//		}
//		return replaceCount;
//	}

	// replacing - multiple all collection //

	@Override
	public final int replaceAll(final XGettingCollection<? extends E> elements, final E replacement)
	{
		if(elements instanceof AbstractSimpleArrayCollection<?>){
			return this.rngReplaceAll(
				0,
				Jadoth.to_int(this.parent.size()),
				AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements),
				0,
				Jadoth.to_int(elements.size()),
				replacement
			);
		}

		return elements.iterate(new Aggregator<E, Integer>(){
			private int replaceCount = 0;
			@Override public void accept(final E e) {
				this.replaceCount += ChainStorageStrong.this.replace(e, replacement);
			}
			@Override public Integer yield(){
				return this.replaceCount;
			}
		}).yield();
	}

//	@Override
//	public final int replaceAll(
//		final XGettingCollection<? extends E> samples,
//		final Equalator<? super E> equalator,
//		final E replacement
//	)
//	{
//		if(samples instanceof AbstractSimpleArrayCollection<?>){
//			return this.rngReplaceAll(0, this.parent.size(),
//				(E[])((AbstractSimpleArrayCollection<?>)samples).internalGetStorageArray(), 0, samples.size(),
//				equalator, replacement
//			);
//		}
//
//		return samples.iterate(new Aggregator<E, Integer>(){
//			private int replaceCount = 0;
//			@Override public void accept(final E e) {
//				this.replaceCount += ChainStrongStrongStorage.this.replace(e, equalator, replacement);
//			}
//			@Override public Integer yield(){
//				return this.replaceCount;
//			}
//		});
//	}

	@Override
	public final int rngReplaceAll(
		final int                             offset     ,
		final int                             length     ,
		final XGettingCollection<? extends E> elements   ,
		final E                               replacement
	)
	{
		if(elements instanceof AbstractSimpleArrayCollection<?>){
			return this.rngReplaceAll(
				offset,
				length,
				AbstractSimpleArrayCollection.internalGetStorageArray((AbstractSimpleArrayCollection<?>)elements),
				0,
				Jadoth.to_int(elements.size()),
				replacement
			);
		}

		return elements.iterate(new Aggregator<E, Integer>(){
			private int replaceCount = 0;
			@Override public void accept(final E e) {
				this.replaceCount += ChainStorageStrong.this.rngReplace(offset, length, e, replacement);
			}
			@Override public Integer yield(){
				return this.replaceCount;
			}
		}).yield();
	}

//	@Override
//	public final int rngReplaceAll(
//		final int offset,
//		final int length,
//		final XGettingCollection<? extends E> samples,
//		final Equalator<? super E> equalator,
//		final E replacement
//	)
//	{
//		if(samples instanceof AbstractSimpleArrayCollection<?>){
//			return this.rngReplaceAll(offset, length,
//				(E[])((AbstractSimpleArrayCollection<?>)samples).internalGetStorageArray(), 0, samples.size(),
//				equalator, replacement
//			);
//		}
//
//		return samples.iterate(new Aggregator<E, Integer>(){
//			private int replaceCount = 0;
//			@Override public void accept(final E e) {
//				this.replaceCount += ChainStrongStrongStorage.this.rngReplace(offset, length, e, equalator, replacement);
//			}
//			@Override public Integer yield(){
//				return this.replaceCount;
//			}
//		});
//	}



	///////////////////////////////////////////////////////////////////////////
	//  substituting    //
	/////////////////////

	@Override
	public final int substituteOne(final Predicate<? super E> predicate, final E substitute)
	{
		try {
			int i = 0;
			for(EN e = this.head.next; e != null; e = e.next) {
				if(predicate.test(e.element())) {
					e.setElement0(substitute);
					return i;
				}
				i++;
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return -1;
	}


	@Override
	public final int rngSubstituteOne(
		      int offset,
		final int length,
		final Predicate<? super E> predicate,
		final E substitute
	)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null){
			return -1;
		}

		try {
			final int bound = offset + length;
			if(length > 0) {
				for(; offset != bound; e = e.next, offset++) {
					if(predicate.test(e.element())) {
						e.setElement0(substitute);
						return offset;
					}
				}
			}
			else {
				for(; offset != bound; e = e.prev, offset--) {
					if(predicate.test(e.element())) {
						e.setElement0(substitute);
						return offset;
					}
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return -1;
	}

	// substituting - multiple //

	@Override
	public final int substitute(final Predicate<? super E> predicate, final E substitute)
	{
		int replaceCount = 0;
		for(EN e = this.head.next; e != null; e = e.next){
			if(predicate.test(e.element())){
				e.setElement0(substitute);
				replaceCount++;
			}
		}
		return replaceCount;
	}

	@Override
	public final int rngSubstitute(final int offset, int length, final Predicate<? super E> predicate, final E substitute)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;

		int replaceCount = 0;
		if(length > 0){
			for(; length --> 0; e = e.next){
				if(predicate.test(e.element())){
					e.setElement0(substitute);
					replaceCount++;
				}
			}
		}
		else {
			for(; length ++< 0; e = e.prev){
				if(predicate.test(e.element())){
					e.setElement0(substitute);
					replaceCount++;
				}
			}
		}
		return replaceCount;
	}

	// replacing - mapped //

	@Override
	public final int modify(final Function<E, E> mapper)
	{
		int replaceCount = 0;
		for(EN e = this.head.next; e != null; e = e.next){
			final E replacement;
			if((replacement = mapper.apply(e.element())) != e.element()){
				e.setElement0(replacement);
				replaceCount++;
			}
		}
		return replaceCount;
	}

	@Override
	public final int modify(final Predicate<? super E> predicate, final Function<E, E> mapper)
	{
		int replaceCount = 0;
		try {
			for(EN e = this.head.next; e != null; e = e.next) {
				if(predicate.test(e.element())) {
					e.setElement0(mapper.apply(e.element()));
					replaceCount++;
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return replaceCount;
	}

	@Override
	public final int rngModify(final int offset, int length, final Function<E, E> mapper)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null){
			return 0;
		}

		int replaceCount = 0;
		try {
			E replacement;
			if(length > 0) {
				for(; length-- > 0; e = e.next) {
					if((replacement = mapper.apply(e.element())) != e.element()) {
						e.setElement0(replacement);
						replaceCount++;
					}
				}
			}
			else {
				for(; length++ < 0; e = e.prev) {
					if((replacement = mapper.apply(e.element())) != e.element()) {
						e.setElement0(replacement);
						replaceCount++;
					}
				}
			}
		}
		catch(final ThrowBreak b) { /* abort iteration */	}
		return replaceCount;
	}

	@Override
	public final int rngModify(final int offset, int length, final Predicate<? super E> predicate, final Function<E, E> mapper)
	{
		EN e;
		if((e = this.getRangeChainEntry(offset, length)) == null) return 0;

		int replaceCount = 0;
		if(length > 0){
			for(; length --> 0; e = e.next){
				if(predicate.test(e.element())){
					e.setElement0(mapper.apply(e.element()));
					replaceCount++;
				}
			}
		}
		else {
			for(; length ++< 0; e = e.prev){
				if(predicate.test(e.element())){
					e.setElement0(mapper.apply(e.element()));
					replaceCount++;
				}
			}
		}
		return replaceCount;
	}



	///////////////////////////////////////////////////////////////////////////
	//  inner classes   //
	/////////////////////

	final class KeyItr implements Iterator<E>
	{
		private EN current = ChainStorageStrong.this.head;

		@Override
		public final boolean hasNext()
		{
			return this.current.next != null;
		}

		@Override
		public final E next()
		{
			if(this.current.next == null){
				throw new NoSuchElementException();
			}
			return (this.current = this.current.next).element();
		}

		@Override
		public final void remove()
		{
			/* (02.12.2011 TM)NOTE:
			 * Dropped support for removal stuff because it would prevent using the iterator in read-only delegates.
			 * As it is an optional operation, no proper code can rely on it anyway and tbh: just use proper
			 * internal iteration means in the first place.
			 */
			throw new UnsupportedOperationException();
		}

	}



	///////////////////////////////////////////////////////////////////////////
	//     swapping     //
	/////////////////////

	@Override
	public final void swap(final int indexA, final int indexB)
	{
		final int size;
		if(indexA >= (size = Jadoth.to_int(this.parent.size())) || indexA < 0){
			throw new IndexOutOfBoundsException(exceptionIndexOutOfBounds(size, indexA));
		}
		if(indexB < 0 || indexB >= size){
			throw new IndexOutOfBoundsException(exceptionIndexOutOfBounds(size, indexB));
		}

		EN eA, eB;
		int i;
		if((i = indexA - indexB) >= 0){
			if(i == 0){
				return;
			}
			else if(i < size>>>1){
				eB = eA = this.getChainEntry(indexB);
				while(i --> 0){
					eA = eA.next;
				}
			}
			else {
				eA = this.getChainEntry(indexA);
				eB = this.getChainEntry(indexB);
			}
		}
		else {
			if(-i < size>>>1){
				eB = eA = this.getChainEntry(indexA);
				while(i ++< 0){
					eB = eB.next;
				}
			}
			else {
				eA = this.getChainEntry(indexA);
				eB = this.getChainEntry(indexB);
			}
		}
		this.swapEntries(eA, eB);
	}

	@Override
	public final void swap(int indexA, int indexB, final int length)
	{
		if(length == 0 || indexA == indexB){
			return;
		}
		else if(indexA > indexB){
			final int t = indexA;
			indexA = indexB;
			indexB = t;
		}

		final int bound;
		if(length < 0 || (bound = indexA + length) >= indexB){
			throw new IndexOutOfBoundsException(exceptionIllegalSwapBounds(indexA, indexB, length));
		}

		EN eA, eB, nextA, nextB;
		eB = this.getRangeChainEntry(indexB, length);
		eA = this.getChainEntry(indexA);

		for(; indexA < bound; indexA++){
			nextA = eA.next;
			nextB = eB.next;
			this.swapEntries(eA, eB);
			eA = nextA;
			eB = nextB;
		}
	}

	@Override
	public final void reverse()
	{
		EN eA, eB, nextA, nextB;
		if((eA = this.head.next) == null){
			return;
		}
		eB = this.head.prev;
		for(int i = Jadoth.to_int(this.parent.size())>>>1; i != 0; i--){
			nextA = eA.next;
			nextB = eB.prev;
			this.swapEntries(eA, eB);
			eA = nextA;
			eB = nextB;
		}
	}

	@Override
	public final void rngReverse(int offset, int length)
	{
		if(length == 0){
			return;
		}
		else if(length < 0){
			offset -=length=- length;
		}

		EN eA, eB, nextA, nextB;
		eA = this.getRangeChainEntry(offset, length);
		eB = this.getChainEntry(offset+length-1);
		for(length >>>= 1; length != 0; length--){
			nextA = eA.next;
			nextB = eB.prev;
			this.swapEntries(eA, eB);
			eA = nextA;
			eB = nextB;
		}
	}

	@Override
	public final void shuffle()
	{
		EN entry = this.head;
		int length = Jadoth.to_int(this.parent.size());

		final FastRandom random = new FastRandom();
		for(EN s1, s2; length > 0; entry = entry.next, length--){
			s1 = s2 = entry.next;
			for(final int i = random.nextInt(length); i > 0;){
				s2 = s2.next;
			}
			this.swapEntries(s1, s2);
		}
	}

	@Override
	public final void rngShuffle(final int offset, int length)
	{
		EN entry;
		if(length <= 0){
			if(length == 0) return; // length 0 special case: nothing to shuffle
			entry = this.getIntervalLowChainEntry(offset - (length = -length) + 1, offset).prev;
		}
		else {
			entry = this.getIntervalLowChainEntry(offset, offset + length - 1).prev;
		}

		final FastRandom random = new FastRandom();
		for(EN s1, s2; length > 0; entry = entry.next, length--){
			s1 = s2 = entry.next;
			for(final int i = random.nextInt(length); i > 0;){
				s2 = s2.next;
			}
			this.swapEntries(s1, s2);
		}
	}



	///////////////////////////////////////////////////////////////////////////
	//  inner classes   //
	/////////////////////

	final class Itr implements Iterator<E>
	{
		// first is head dummy entry
		private EN current = ChainStorageStrong.this.head;

		@Override
		public final boolean hasNext()
		{
			return this.current.next != null;
		}

		@Override
		public final E next()
		{
			return (this.current = this.current.next).element();
		}

		@Override
		public final void remove()
		{
			// fu overcomplicated operations in silly Iterator concept
			throw new UnsupportedOperationException();
		}

	}

}
