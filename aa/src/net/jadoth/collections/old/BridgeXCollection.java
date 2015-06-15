package net.jadoth.collections.old;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.types.XCollection;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XSet;
import net.jadoth.functional.JadothPredicates;

public class BridgeXCollection<E> implements OldCollection<E>
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	protected final XCollection<E> subject;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	protected BridgeXCollection(final XCollection<E> collection)
	{
		super();
		this.subject = collection;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public XCollection<E> parent()
	{
		return this.subject;
	}

	@Override
	public boolean add(final E e)
	{
		return ((XSet<E>)this.subject).add(e);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addAll(final Collection<? extends E> c)
	{
		if(c instanceof XGettingCollection<?>){
			((XSet<E>)this.subject).addAll((XGettingCollection<? extends E>)c);
			return true;
		}

		final XSet<E> list = (XSet<E>)this.subject;
		for(final E e : c) {
			list.add(e);
		}
		return true;
	}

	@Override
	public void clear()
	{
		((XSet<E>)this.subject).clear();
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(final Object o)
	{
		try{
			return ((XSet<E>)this.subject).removeBy(JadothPredicates.isEqualTo((E)o)) > 0;
		}
		catch(final RuntimeException e){
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean removeAll(final Collection<?> c)
	{
		int removeCount = 0;
		final XSet<E> list = (XSet<E>)this.subject;

		// even xcollections have to be handled that way because of the missing type info (argh)
		for(final Object o : c) {
			try{
				removeCount += list.removeBy(JadothPredicates.isEqualTo((E)o));
			}
			catch(final RuntimeException e){
				// incompatible element, just skip and proceed with next
			}
		}
		return removeCount > 0;
	}

	@Override
	public boolean retainAll(final Collection<?> c)
	{
		final int oldSize = Jadoth.to_int(this.subject.size());
		((XSet<E>)this.subject).removeBy(new Predicate<E>(){ @Override public boolean test(final E e){
			return !c.contains(e);
		}});
		return oldSize - Jadoth.to_int(this.subject.size()) > 0;
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

}
