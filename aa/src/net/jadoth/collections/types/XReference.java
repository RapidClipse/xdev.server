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
import net.jadoth.reference.Reference;
import net.jadoth.util.Equalator;

/**
 * @author Thomas Muenz
 *
 */
/**
 * Simple Reference class to handle mutable references. Handle with care!
 * <p>
 * Note: In most cases, a mutable reference object like this should not be neccessary if the program is well
 * structured (that's why no such class exists in the Java API).
 * Extensive use of this class where it would be better to restructure the program may end in even more structural
 * problems.<br>
 * Yet in some cases, a mutable reference really is needed or at least helps in creating cleaner structures.<br>
 * So again, use wisely.
 */
public interface XReference<E> extends XReferencing<E>, XSettingList<E>, XSortableEnum<E>, Reference<E>
{
	@Override
	public void set(E element);



	@Override
	public boolean replaceOne(E element, E replacement);

	@Override
	public int replace(E element, E replacement);

	@Override
	public int replaceAll(XGettingCollection<? extends E> elements, E replacement);

	@Override
	public boolean replaceOne(Predicate<? super E> predicate, E substitute);

	@Override
	public int replace(Predicate<? super E> predicate, E substitute);

//	@Override
//	public int modify(Function<E, E> mapper);
//
//	@Override
//	public int modify(Predicate<? super E> predicate, Function<E, E> mapper);

	@Override
	public boolean set(int index, E element);

	@Override
	public E setGet(int index, E element);

	@Override
	public void setFirst(E element);

	@Override
	public void setLast(E element);

	@Override
	public XReference<E> shiftTo(int sourceIndex, int targetIndex);

	@Override
	public XReference<E> shiftTo(int sourceIndex, int targetIndex, int length);

	@Override
	public XReference<E> shiftBy(int sourceIndex, int distance);

	@Override
	public XReference<E> shiftBy(int sourceIndex, int distance, int length);

	@SuppressWarnings("unchecked")
	@Override
	public XReference<E> setAll(int index, E... elements);

	@Override
	public XReference<E> set(int index, E[] elements, int offset, int length);

	@Override
	public XReference<E> set(int index, XGettingSequence<? extends E> elements, int offset, int length);

	@Override
	public XReference<E> swap(int indexA, int indexB);

	@Override
	public XReference<E> swap(int indexA, int indexB, int length);

	@Override
	public XReference<E> reverse();

	@Override
	public XReference<E> fill(int offset, int length, E element);

	@Override
	public XReference<E> sort(Comparator<? super E> comparator);

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
	public XReference<E> copy();

	@Override
	public XReference<E> toReversed();

	@Override
	public XReferencing<E> view();

	@Override
	public XReferencing<E> view(int lowIndex, int highIndex);

	@Override
	public XReference<E> range(int fromIndex, int toIndex);

}
