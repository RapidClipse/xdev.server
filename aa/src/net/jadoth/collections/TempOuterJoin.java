package net.jadoth.collections;

import java.util.Comparator;
import java.util.Iterator;

import net.jadoth.collections.sorting.Sortable;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.Procedure;

public class TempOuterJoin
{
	private static final <E> E advance(final Iterator<E> iterator)
	{
		return iterator.hasNext() ?iterator.next() :null;
	}

	public static final <
		E,
		C1 extends Iterable<? extends E> & Sortable<? extends E>,
		C2 extends Iterable<? extends E> & Sortable<? extends E>
	>
	void outerJoinWithSort(
		final C1                                    leftElements    ,
		final C2                                    rightElements   ,
		final Comparator<? super E>                 comparator      ,
		final Procedure<? super E>                  soleLeftHandler ,
		final Procedure<? super E>                  soleRightHandler,
		final BiProcedure<? super E, ? super E> matchHandler
	)
	{
		leftElements.sort(comparator);
		rightElements.sort(comparator);
		outerJoinPresorted(
			leftElements.iterator(),
			rightElements.iterator(),
			comparator,
			soleLeftHandler,
			soleRightHandler,
			matchHandler
		);
	}

	public static final <E, C1 extends Iterable<? extends E>, C2 extends Iterable<? extends E>> void outerJoinPresorted(
		final Iterator<? extends E>                 itrLeft         ,
		final Iterator<? extends E>                 itrRight        ,
		final Comparator<? super E>                 comparator      ,
		final Procedure<? super E>                  soleLeftHandler ,
		final Procedure<? super E>                  soleRightHandler,
		final BiProcedure<? super E, ? super E> matchHandler
	)
	{
		E left = advance(itrLeft), right = advance(itrRight);

		while(left != null || right != null)
		{
			// compare current elements
			final int order; // assignment inlined for performance

			// common case (#1): match
			if((order = right == null ?-1 :left == null ?1 :comparator.compare(left, right)) == 0){
				matchHandler.accept(left, right);
				left  = advance(itrLeft);
				right = advance(itrRight);
				continue;
			}

			// special cases: no match
			if(order < 0){ // right element is ahead
				// case #2: unmatched left element
				soleLeftHandler.accept(left);
				left = advance(itrLeft);
			}
			else { // left element is ahead
				// case #3: unmatched right element
				soleRightHandler.accept(right);
				right = advance(itrRight);
			}
		}
	}
}
