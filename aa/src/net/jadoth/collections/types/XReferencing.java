/**
 *
 */
package net.jadoth.collections.types;

import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.Predicate;

import net.jadoth.collections.Constant;
import net.jadoth.collections.old.OldList;
import net.jadoth.functional.Procedure;
import net.jadoth.reference.Referencing;
import net.jadoth.util.Equalator;

/**
 * @author Thomas Muenz
 *
 */
// (15.01.2013 TM)FIXME: Rename to XSingle or so
public interface XReferencing<E> extends XGettingList<E>, XGettingEnum<E>, Referencing<E>
{
	@Override
	public E get();



	@Override
	public E at(int index);

	@Override
	public E first();

	@Override
	public E last();

	@Override
	public E poll();

	@Override
	public E peek();

	@Override
	public int maxIndex(Comparator<? super E> comparator);

	@Override
	public int minIndex(Comparator<? super E> comparator);

	@Override
	public int indexOf(E element);

	@Override
	public int indexBy(Predicate<? super E> predicate);

	@Override
	public int lastIndexOf(E element);

	@Override
	public int lastIndexBy(Predicate<? super E> predicate);

	@Override
	public int scan(Predicate<? super E> predicate);

	@Override
	public boolean isSorted(Comparator<? super E> comparator);

	@Override
	public <T extends Procedure<? super E>> T copySelection(T target, int... indices);

	@Override
	public <T> T[] copyTo(T[] target, int targetOffset, int offset, int length);

	@Override
	public Iterator<E> iterator();

	@Override
	public Object[] toArray();

	@Override
	public boolean hasVolatileElements();

	@Override
	public long size();

	@Override
	public boolean isEmpty();

	@Override
	public Equalator<? super E> equality();

	@Override
	public E[] toArray(Class<E> type);

	@Override
	public boolean equals(XGettingCollection<? extends E> samples, Equalator<? super E> equalator);

	@Override
	public boolean equalsContent(XGettingCollection<? extends E> samples, Equalator<? super E> equalator);

	@Override
	public boolean nullContained();

	@Override
	public boolean containsId(E element);

	@Override
	public boolean contains(E element);

	@Override
	public boolean containsSearched(Predicate<? super E> predicate);

	@Override
	public boolean containsAll(XGettingCollection<? extends E> elements);

	@Override
	public boolean applies(Predicate<? super E> predicate);

	@Override
	public int count(E element);

	@Override
	public int countBy(Predicate<? super E> predicate);

	@Override
	public boolean hasDistinctValues();

	@Override
	public boolean hasDistinctValues(Equalator<? super E> equalator);

	@Override
	public E search(Predicate<? super E> predicate);

	@Override
	public E seek(E sample);

	@Override
	public E max(Comparator<? super E> comparator);

	@Override
	public E min(Comparator<? super E> comparator);

	@Override
	public <T extends Procedure<? super E>> T distinct(T target);

	@Override
	public <T extends Procedure<? super E>> T distinct(T target, Equalator<? super E> equalator);

	@Override
	public <T extends Procedure<? super E>> T copyTo(T target);

	@Override
	public <T extends Procedure<? super E>> T filterTo(T target, Predicate<? super E> predicate);

	@Override
	public <T> T[] copyTo(T[] target, int targetOffset);

	@Override
	public <T extends Procedure<? super E>> T union(XGettingCollection<? extends E> other, Equalator<? super E> equalator, T target);

	@Override
	public <T extends Procedure<? super E>> T intersect(XGettingCollection<? extends E> other, Equalator<? super E> equalator, T target);

	@Override
	public <T extends Procedure<? super E>> T except(XGettingCollection<? extends E> other, Equalator<? super E> equalator, T target);

	@Override
	public boolean nullAllowed();

	@Override
	public long maximumCapacity();

	@Override
	public long remainingCapacity();

	@Override
	public boolean isFull();

	@Override
	public Constant<E> immure();

	@Override
	public ListIterator<E> listIterator();

	@Override
	public ListIterator<E> listIterator(int index);

	@Override
	public OldList<E> old();

	@Override
	public XReferencing<E> copy();

	@Override
	public XReferencing<E> toReversed();

	@Override
	public XReferencing<E> view();

	@Override
	public XReferencing<E> view(int lowIndex, int highIndex);

	@Override
	public XReferencing<E> range(int fromIndex, int toIndex);

}
