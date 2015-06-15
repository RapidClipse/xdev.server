package net.jadoth.collections.old;

import java.util.Collection;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.types.XList;
import net.jadoth.collections.types.XProcessingList;
import net.jadoth.collections.types.XSet;
import net.jadoth.functional.JadothPredicates;

public abstract class OldRemovingList<E> extends OldGettingList<E>
{
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	protected OldRemovingList(final XProcessingList<E> list)
	{
		super(list);
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public XProcessingList<E> parent()
	{
		return (XProcessingList<E>)this.subject;
	}

	@Override
	public void clear()
	{
		((XProcessingList<E>)this.subject).clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(final Object o)
	{
		try{
			return ((XProcessingList<E>)this.subject).removeBy(JadothPredicates.isEqualTo((E)o)) > 0;
		}
		catch(final RuntimeException e){
			return false;
		}
	}

	@Override
	public E remove(final int index)
	{
		return ((XProcessingList<E>)this.subject).removeAt(index);
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
		((XList<E>)this.subject).removeBy(new Predicate<E>(){ @Override public boolean test(final E e){
			return !c.contains(e);
		}});
		return oldSize - Jadoth.to_int(this.subject.size()) > 0;
	}

	@Override
	public OldRemovingList<E> subList(final int fromIndex, final int toIndex)
	{
		/* XList implementations always create a SubList instance whose implementation creates an
		 * OldXList bridge instance, so this cast is safe (and inevitable). Savvy :)?
		 */
		return (OldRemovingList<E>)((XProcessingList<E>)this.subject).range(fromIndex, toIndex).old();
	}
}
