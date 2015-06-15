package net.jadoth.collections.functions;

import java.util.function.Predicate;

public final class ElementIsContained<E> implements Predicate<E>
{
	/* note:
	 * - the term element for a collection element is fixed and will never change
	 * - the reference can never be ruined from outside as this function is always instantiated in a local scope
	 * hence, the field is intentionally declared public to improve local performance and readability
	 */
	public E element = null;

	public ElementIsContained()
	{
		super();
	}
	@Override
	public boolean test(final E e)
	{
		return e == this.element;
	}
}