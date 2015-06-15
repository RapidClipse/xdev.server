package net.jadoth.util.iterables;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import net.jadoth.Jadoth;
import net.jadoth.collections.types.XGettingList;
import net.jadoth.collections.types.XList;
import net.jadoth.exceptions.IndexBoundsException;

/**
 * Generic (and potentially imperformant!) implementation of a {@link ListIterator}.<br>
 * Routes all modifying procedures ({@link #add(Object)}, {@link #remove()}, {@link #set(Object)}) to the wrapped
 * {@link List} which may throw an {@link UnsupportedOperationException} if it does not support the procedure.
 * <p>
 * If the use of an Iterator is not mandatory (e.g. through an API), it is strongly recommended to instead use
 * Higher Order Programming concepts from "Collection 2.0" types like {@link XGettingList}, {@link XList}, etc.
 * and their functional procedures like {@link XGettingList#accept(net.jadoth.lang.functional.Operation)}, etc.
 *
 * @author Thomas Muenz
 *
 */
public class GenericListIterator<E> implements ListIterator<E>
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	private final XList<E> list;
	private int index;
	private int lastRet;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	public GenericListIterator(final XList<E> list) throws IndexBoundsException
	{
		super();
		this.list    = list;
		this.index   =    0;
		this.lastRet =   -1;
	}

	public GenericListIterator(final XList<E> list, final int index) throws IndexBoundsException
	{
		super();
		this.list = list;
		/* (20.11.2011)NOTE:
		 * the definition of java.util.list#listIterator(int) is bugged for all collection than can be empty
		 * (i.e. for ALL collections).
		 * The exception definition says:
		 * throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
		 * Then what should happen if the list is empty and the method is called with index 0 for the first element?
		 * index is not < 0 and index is also not > size (which is 0). So no exception is to be thrown but
		 * a valid iterator has to be returned, validly returning one element (that at index 0).
		 * But which element should that be if the list is empty?
		 * => bug
		 *
		 * The extended collection's backward-compatibility #listIterator(int) throws the correct exception in this
		 * case.
		 */
		if(index < 0 || index >= Jadoth.to_int(list.size())){
			throw new IndexBoundsException(Jadoth.to_int(list.size()), index);
		}
		this.index = index;
		this.lastRet = -1;
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public void add(final E e) throws UnsupportedOperationException
	{
		this.list.add(e);
	}

	@Override
	public boolean hasNext()
	{
		return this.index < Jadoth.to_int(this.list.size()); // list size could have changed meanwhile
	}

	@Override
	public boolean hasPrevious()
	{
		return this.index > 0 && this.index <= Jadoth.to_int(this.list.size()); // list size could have changed meanwhile
	}

	@Override
	public E next() throws NoSuchElementException
	{
		try{
			final int i;
			final E e = this.list.at(i = this.index);
			this.lastRet = i;
			this.index = i+1;
			return e;
		}
		catch(final IndexOutOfBoundsException e){
			throw new NoSuchElementException();
		}
	}

	@Override
	public int nextIndex()
	{
		return this.index;
	}

	@Override
	public E previous() throws NoSuchElementException
	{
		try{
			final int i;
			final E e = this.list.at(i = this.index - 1);
			this.lastRet = this.index = i;
			return e;
		}
		catch(final IndexOutOfBoundsException e){
			throw new NoSuchElementException();
		}
	}

	@Override
	public int previousIndex()
	{
		return this.index - 1;
	}

	@Override
	public void remove() throws NoSuchElementException, UnsupportedOperationException
	{
		if(this.lastRet == -1){
			throw new IllegalStateException();
		}

		try{
			this.list.removeAt(this.lastRet);
			if(this.lastRet < this.index){
				this.index--;
			}
			this.lastRet = -1;
		}
		catch(final IndexOutOfBoundsException e){
			throw new NoSuchElementException();
		}
	}

	@Override
	public void set(final E e) throws NoSuchElementException, UnsupportedOperationException
	{
		if(this.lastRet == -1){
			throw new IllegalStateException();
		}

		try{
			this.list.setGet(this.lastRet, e);
		}
		catch(final IndexOutOfBoundsException ex){
			throw new NoSuchElementException();
		}
	}

}
