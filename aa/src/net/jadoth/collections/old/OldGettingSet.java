package net.jadoth.collections.old;

import java.util.Collection;
import java.util.Iterator;

import net.jadoth.Jadoth;
import net.jadoth.collections.types.XGettingSet;
import net.jadoth.functional.JadothPredicates;

public abstract class OldGettingSet<E> implements OldSet<E>
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	protected final XGettingSet<E> subject;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	protected OldGettingSet(final XGettingSet<E> set)
	{
		super();
		this.subject = set;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public XGettingSet<E> parent()
	{
		return this.subject;
	}

	@Override
	public boolean add(final E e) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) throws UnsupportedOperationException
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
	public boolean isEmpty()
	{
		return this.subject.isEmpty();
	}

	@Override
	public Iterator<E> iterator()
	{
		return this.subject.iterator();
	}

	@Override
	public boolean remove(final Object o) throws UnsupportedOperationException
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
	public int size()
	{
		return Jadoth.to_int(this.subject.size());
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
