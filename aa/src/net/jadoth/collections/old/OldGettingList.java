package net.jadoth.collections.old;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import net.jadoth.Jadoth;
import net.jadoth.collections.types.XGettingList;
import net.jadoth.functional.JadothPredicates;
import net.jadoth.util.iterables.ReadOnlyListIterator;

public abstract class OldGettingList<E> implements OldList<E>
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	protected final XGettingList<E> subject;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	protected OldGettingList(final XGettingList<E> list)
	{
		super();
		this.subject = list;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public XGettingList<E> parent()
	{
		return this.subject;
	}

	@Override
	public boolean add(final E e) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(final int index, final E element) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends E> c) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(final Object o)
	{
		try{
			return this.subject.containsSearched(JadothPredicates.isEqualTo((E)o));
		}
		catch(final RuntimeException e){
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsAll(final Collection<?> c)
	{
		try{
			for(final Object o : c) {
				if(!this.subject.containsSearched(JadothPredicates.isEqualTo((E)o))){
					return false;
				}
			}
			return true;
		}
		catch(final RuntimeException e){
			return false;
		}
	}

	@Override
	public E get(final int index)
	{
		return this.subject.at(index);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int indexOf(final Object o)
	{
		try{
			return this.subject.indexBy(JadothPredicates.isEqualTo((E)o));
		}
		catch(final RuntimeException e){
			return -1;
		}
	}

	@Override
	public boolean isEmpty()
	{
		return this.subject.isEmpty();
	}

	@Override
	public Iterator<E> iterator()
	{
		return this.subject.iterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int lastIndexOf(final Object o)
	{
		try{
			return this.subject.lastIndexBy(JadothPredicates.isEqualTo((E)o));
		}
		catch(final RuntimeException e){
			return -1;
		}
	}

	@Override
	public ListIterator<E> listIterator()
	{
		return new ReadOnlyListIterator<>(this.subject);
	}

	@Override
	public ListIterator<E> listIterator(final int index)
	{
		return new ReadOnlyListIterator<>(this.subject, index);
	}

	@Override
	public boolean remove(final Object o) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public E remove(final int index) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(final Collection<?> c) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(final Collection<?> c) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public E set(final int index, final E element)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int size()
	{
		return Jadoth.to_int(this.subject.size());
	}

	@Override
	public OldGettingList<E> subList(final int fromIndex, final int toIndex)
	{
		/* XGettingList implementations always create a SubList instance whose implementation creates an
		 * OldGettingList bridge instance, so this cast is safe (and inevitable). Savvy :)?
		 */
		return (OldGettingList<E>)this.subject.range(fromIndex, toIndex).old();
	}

	@Override
	public Object[] toArray()
	{
		return this.subject.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a)
	{
		return this.subject.copyTo(a, 0);
	}

	@Override
	public String toString()
	{
		return this.subject.toString();
	}
}
