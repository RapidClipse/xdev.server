 package net.jadoth.collections;

import net.jadoth.collections.interfaces.ChainStorage;
import net.jadoth.util.KeyValue;
import net.jadoth.util.chars.VarString;



/**
 * @author Thomas Muenz
 *
 */
public abstract class AbstractChainEntry<E, K, V, EN extends AbstractChainEntry<E, K, V, EN>>
implements ChainStorage.Entry<E, K, V, EN>, KeyValue<K, V>, java.util.Map.Entry<K, V>
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	public static final Hopper hopNext = new Hopper() { @Override public <E, K, V, EN extends AbstractChainEntry<E, K, V, EN>> EN hop(final EN e){
		return e.next;
	}};
	public static final Hopper hopPrev = new Hopper() { @Override public <E, K, V, EN extends AbstractChainEntry<E, K, V, EN>> EN hop(final EN e){
		return e.prev;
	}};



	///////////////////////////////////////////////////////////////////////////
	// static methods //
	///////////////////

	public static String chainString(AbstractChainEntry<?, ?, ?, ?> entry)
	{
		if(entry == null){
			return null;
		}

		final VarString vc = VarString.New().append('(');
		for(; entry != null; entry = entry.next){
			vc.add(entry).add(')', '-', '(');
		}
		return vc.deleteLast(2).toString();
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	EN prev = null;  // the previous entry in the order chain (head for first entry).
	EN next = null;  // the next entry in the order chain (null for last entry).



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	@SuppressWarnings("unchecked") // sadly no self type in Java
	protected void removeFrom(final AbstractChainCollection<E, K, V, EN> handler)
	{
		handler.internalRemoveEntry((EN)this);
	}

	protected boolean emptyKey()
	{
		return false;
	}

	protected boolean emptyValue()
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		// only for debug
		return this.assembleElement(VarString.New()).toString();
	}
	
	public VarString assembleElement(final VarString vs)
	{
		return vs.add(this.element());
	}
	
	public VarString assembleKey(final VarString vs)
	{
		return vs.add(this.key());
	}
	
	public VarString assembleValue(final VarString vs)
	{
		return vs.add(this.value());
	}



	///////////////////////////////////////////////////////////////////////////
	// abstract methods //
	/////////////////////
	
	protected abstract void setElement0(E element);
	protected abstract E setElement(E element);
	protected abstract boolean hasNullElement();
	protected abstract E element();
	
	protected abstract void set0(K key, V value);
	
	@Override
	public abstract K key();
	protected abstract K setKey(K key); // for consistency with clumsy #setValue (should actually be named setGetKey)
	protected abstract void setKey0(K key); // for consistency with clumsy #setValue (should actually be named setKey)
	protected abstract boolean hasNullKey();

	@Override
	public abstract V value();
	@Override
	public abstract V setValue(V value); // polymorphs with java.util.Map$Entry (should actually be named setGetValue)
	protected abstract void setValue0(V value); // for consistency with clumsy #setValue (should actually be named setValue)
	protected abstract boolean hasNullValue();

	protected abstract boolean sameKV(KeyValue<K,V> other);



	public static class Head<E, K, V, EN extends AbstractChainEntry<E, K, V, EN>> extends AbstractChainEntry<E, K, V, EN>
	{
		@Override
		protected final void setElement0(final E element)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		protected final E setElement(final E element)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		protected final boolean hasNullElement()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		protected final E element()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public K getKey()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public V getValue()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		protected void set0(final K key, final V value)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public K key()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		protected K setKey(final K key)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		protected void setKey0(final K key)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		protected boolean hasNullKey()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public V value()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		public V setValue(final V value)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		protected void setValue0(final V value)
		{
			throw new UnsupportedOperationException();
		}

		@Override
		protected boolean hasNullValue()
		{
			throw new UnsupportedOperationException();
		}

		@Override
		protected boolean sameKV(final KeyValue<K, V> other)
		{
			throw new UnsupportedOperationException();
		}
		
	}

	public interface Hopper
	{
		public <E, K, V, EN extends AbstractChainEntry<E, K, V, EN>> EN hop(EN entry);
	}
}
