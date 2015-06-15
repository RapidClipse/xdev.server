package net.jadoth.collections;

import java.util.function.Predicate;

import net.jadoth.collections.old.OldCollection;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XProcessingSequence;
import net.jadoth.functional.Procedure;
import net.jadoth.util.Equalator;

public class SubProcessor<E> extends SubView<E> implements XProcessingSequence<E>
{
	/* (12.07.2012 TM)FIXME: complete SubProcessor implementation
	 * See all "FIX-ME"s
	 */

	@Override
	public E fetch()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public <C extends Procedure<? super E>> C moveSelection(final C target, final int... indices)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public E pick()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public E pinch()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public E pop()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public E removeAt(final int index)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public XProcessingSequence<E> removeRange(final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public XProcessingSequence<E> retainRange(final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public int removeSelection(final int[] indices)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public E retrieve(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public E retrieveBy(final Predicate<? super E> predicate)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public boolean removeOne(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public <C extends Procedure<? super E>> C moveTo(final C target, final Predicate<? super E> predicate)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public final <P extends Procedure<? super E>> P process(final P procedure)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public int removeBy(final Predicate<? super E> predicate)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public int removeDuplicates(final Equalator<? super E> equalator)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public void clear()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public int consolidate()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public int nullRemove()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public int optimize()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public int remove(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public int removeAll(final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public int removeDuplicates()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public int retainAll(final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public void truncate()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}


	@Override
	public SubProcessor<E> copy()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public SubProcessor<E> view()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public OldCollection<E> old()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

	@Override
	public SubProcessor<E> toReversed()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME Auto-generated method stub, not implemented yet
	}

}
