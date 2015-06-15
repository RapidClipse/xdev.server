package net.jadoth.collections.old;

import java.util.Collection;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XSet;
import net.jadoth.functional.JadothPredicates;

public abstract class BridgeXSet<E> extends OldGettingSet<E>
{
	protected BridgeXSet(final XSet<E> set)
	{
		super(set);
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public XSet<E> parent()
	{
		return (XSet<E>)super.parent();
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

//	@SuppressWarnings("unchecked")
//	@Override
//	public boolean remove(final Object o)
//	{
//		try{
//			return ((XSet<E>)this.subject).remove((E)o, Jadoth.equals) > 0;
//		}
//		catch(final RuntimeException e){
//			return false;
//		}
//	}

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

}
