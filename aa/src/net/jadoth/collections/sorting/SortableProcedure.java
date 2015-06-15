package net.jadoth.collections.sorting;

import java.util.Comparator;

import net.jadoth.functional.Procedure;

/**
 * Composite type to guarantee that the implementation of {@link Sortable} and {@link Procedure} refers to the same
 * parametrized type.
 *
 * @author Thomas Muenz
 * @param <E>
 */
public interface SortableProcedure<E> extends Sortable<E>, Procedure<E>
{
	public static <E> void sortIfApplicable(final Procedure<E> procedure, final Comparator<? super E> comparator)
	{
		if(comparator == null || !(procedure instanceof SortableProcedure<?>)){
			return;
		}
		((SortableProcedure<E>)procedure).sort(comparator);
	}
}
