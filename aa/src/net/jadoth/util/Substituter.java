package net.jadoth.util;

import net.jadoth.collections.EqHashEnum;
import net.jadoth.collections.XIterable;
import net.jadoth.functional.Procedure;
import net.jadoth.hash.HashEqualator;
import net.jadoth.util.chars.VarString;

public interface Substituter<T>
{
	public T substitute(T s);



	public interface Removing<T> extends Substituter<T>, net.jadoth.util.Clearable
	{
		@Override
		public void clear();

		public T remove(T s);
	}

	public interface Iterable<T> extends Substituter<T>, XIterable<T>
	{
		// empty so far
	}

	public interface Queryable<T> extends Substituter<T>
	{
		public boolean contains(T s);
	}

	public interface Managed<T> extends Removing<T>, Iterable<T>, Queryable<T>
	{
		// only type combining type
	}


	public static <T> Substituter.Implementation<T> New()
	{
		return new Implementation<>(EqHashEnum.<T>New());
	}

	public static <T> Substituter.Implementation<T> New(final HashEqualator<? super T> hashEqualator)
	{
		return new Implementation<>(EqHashEnum.New(hashEqualator));
	}



	public final class Implementation<T> implements Substituter.Managed<T>, Composition
	{
		///////////////////////////////////////////////////////////////////////////
		// instance fields //
		////////////////////

		final EqHashEnum<T> elements;



		///////////////////////////////////////////////////////////////////////////
		// constructors     //
		/////////////////////

		Implementation(final EqHashEnum<T> elements)
		{
			super();
			this.elements = elements;
		}



		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		@Override
		public final synchronized T substitute(final T item)
		{
			if(item == null){
				return null;
			}
			return this.elements.substitute(item);
		}

		@Override
		public final synchronized void clear()
		{
			this.elements.clear();
		}

		@Override
		public final synchronized <P extends Procedure<? super T>> P iterate(final P procedure)
		{
			this.elements.iterate(procedure);
			return procedure;
		}

		@Override
		public final synchronized boolean contains(final T item)
		{
			return this.elements.contains(item);
		}

		@Override
		public final synchronized T remove(final T item)
		{
			return this.elements.retrieve(item);
		}

		@Override
		public final String toString()
		{
			return this.iterate(
				new Procedure<T>()
				{
					final VarString vs = VarString.New(1000);
					int count = 0;

					@Override
					public void accept(final T e)
					{
						if(++this.count >= 11){
							return;
						}
						this.vs.add(e).lf();
					}

					final String yield()
					{
						if(this.count >= 11){
							this.vs.add("... ["+(this.count - 10)+" more]");
						}
						else if(!this.vs.isEmpty()){
							this.vs.deleteLast(); // delete trailing lf.
						}
						return this.vs.toString();
					}
				}
			).yield();
		}

	}

}
