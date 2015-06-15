package net.jadoth.collections;

import net.jadoth.collections.interfaces.OptimizableCollection;
import net.jadoth.collections.interfaces.Sized;
import net.jadoth.collections.types.XList;
import net.jadoth.functional.Procedure;
import net.jadoth.functional._longProcedure;
import net.jadoth.math.JadothMath;
import net.jadoth.util.Composition;
import net.jadoth.util.KeyValue;

/**
 *
 * @author Thomas Muenz
 *
 */
public final class HashMapIdId implements Sized, OptimizableCollection, Composition
{
	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	private Entry[] hashSlots;
	private int     hashRange;
	private int     size = 0 ;



	///////////////////////////////////////////////////////////////////////////
	// constructors //
	/////////////////

	public HashMapIdId()
	{
		super();
		this.hashSlots = new Entry[1];
		this.hashRange = 0;
	}

	public HashMapIdId(final int slotSize)
	{
		super();
		this.hashSlots = new Entry[(this.hashRange = JadothMath.pow2BoundCapped(slotSize) - 1) + 1];
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	//////////////////////

	@Override
	public final long size()
	{
		return this.size;
	}

	@Override
	public final boolean isEmpty()
	{
		return this.size == 0;
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	private void rebuild(final int newLength)
	{
		if(this.hashSlots.length >= newLength || newLength <= 0){
			return;
		}

		final int newRange = newLength - 1;
		final Entry[] oldSlots = this.hashSlots;
		final Entry[] newSlots = new Entry[newLength];
		for(int i = 0; i < oldSlots.length; i++){
			if(oldSlots[i] == null){
				continue;
			}
			for(Entry next, entry = oldSlots[i]; entry != null; entry = next){
				next = entry.link;
				entry.link = newSlots[System.identityHashCode(entry.value) & newRange];
				newSlots[System.identityHashCode(entry.value) & newRange] = entry;
			}
		}
		this.hashSlots = newSlots;
		this.hashRange = newRange;
	}

	public boolean add(final long key, final long value)
	{
		final int index;
		Entry entry;
		if((entry = this.hashSlots[index = (int)(key & this.hashSlots.length - 1)]) == null){
			// simple case: hash slot is still empty, simply put a new entry
			this.putEntry(index, new Entry(key, value));
			return true;
		}

		// complex case: hash slot is not empty: scan through hash chain
		do {
			if(entry.key == key){ // collision: add logic discards new value
				return false;
			}
		}
		while((entry = entry.link) != null);

		// no collision found in hash chain: prepend a new entry
		this.putEntry(index, new Entry(key, value, this.hashSlots[index]));
		return true;
	}


	public boolean put(final long key, final long value)
	{
		final int index;
		Entry entry;
		if((entry = this.hashSlots[index = (int)(key & this.hashSlots.length - 1)]) == null){
			// simple case: hash slot is still empty, simply put a new entry
			this.putEntry(index, new Entry(key, value));
			return true;
		}

		// complex case: hash slot is not empty: scan through hash chain
		do {
			if(entry.key == key){ // collision: put logic replaces new value
				entry.value = value;
				return false;
			}
		}
		while((entry = entry.link) != null);

		// no collision found in hash chain: prepend a new entry
		this.putEntry(index, new Entry(key, value, this.hashSlots[index]));
		return true;
	}

	public long putGet(final long key, final long value)
	{
		final int index;
		Entry entry;
		if((entry = this.hashSlots[index = (int)(key & this.hashSlots.length - 1)]) == null){
			// simple case: hash slot is still empty, simply put a new entry
			this.putEntry(index, new Entry(key, value));
			return value;
		}

		// complex case: hash slot is not empty: scan through hash chain
		do {
			if(entry.key == key){ // collision: put logic replaces new value
				final long oldValue = entry.value;
				entry.value = value;
				return oldValue;
			}
		}
		while((entry = entry.link) != null);

		// no collision found in hash chain: prepend a new entry
		this.putEntry(index, new Entry(key, value, this.hashSlots[index]));
		return value;
	}

	public long addGet(final long key, final long value)
	{
		final int index;
		Entry entry;
		if((entry = this.hashSlots[index = (int)(key & this.hashSlots.length - 1)]) == null){
			// simple case: hash slot is still empty, simply put a new entry
			this.putEntry(index, new Entry(key, value));
			return value;
		}

		// complex case: hash slot is not empty: scan through hash chain
		do {
			if(entry.key == key){ // collision: add logic discards new value
				return entry.value;
			}
		}
		while((entry = entry.link) != null);

		// no collision found in hash chain: prepend a new entry
		this.putEntry(index, new Entry(key, value, this.hashSlots[index]));
		return value;
	}

	private void putEntry(final int index, final Entry entry)
	{
		this.hashSlots[index] = entry;
		if(++this.size >= this.hashRange){
			this.rebuild((int)(this.hashSlots.length * 2.0f));
		}
	}

	public long get(final long key)
	{
		// keys are assumed to be roughly sequential, hence (key ^ key>>>32) should not be necessary for distribution.
		for(Entry entry = this.hashSlots[(int)(key & this.hashRange)]; entry != null; entry = entry.link){
			if(entry.key == key){
				return entry.value;
			}
		}
		return 0L;
	}

	public XList<Long> getIds()
	{
		final BulkList<Long> list = new BulkList<>(this.size);
		for(Entry entry : this.hashSlots){
			for(; entry != null; entry = entry.link){
				list.add(entry.key);
			}
		}
		return list;
	}

	public int iterateObjects(final _longProcedure procedure)
	{
		for(Entry entry : this.hashSlots){
			for(; entry != null; entry = entry.link){
				procedure.accept(entry.value);
			}
		}
		return this.size;
	}

	/**
	 * Optimizes the internal storage and returns the remaining amount of entries.
	 * @return the amount of entries after the optimization is been completed.
	 */
	@Override
	public int optimize()
	{
		this.rebuild(JadothMath.pow2BoundCapped(this.size));
		return this.size;
	}

	public int iterateIds(final Procedure<? super Long> procedure)
	{
		for(Entry entry : this.hashSlots){
			for(; entry != null; entry = entry.link){
				procedure.accept(entry.key);
			}
		}
		return this.size;
	}

	public int iterateIds(final _longProcedure procedure)
	{
		for(Entry entry : this.hashSlots){
			for(; entry != null; entry = entry.link){
				procedure.accept(entry.key);
			}
		}
		return this.size;
	}

	public void clear()
	{
		final Entry[] slots = this.hashSlots;
		for(int i = 0, len = slots.length; i < len; i++){
			slots[i] = null;
		}
		this.size = 0;
	}


	static final class Entry implements KeyValue<Long, Long>, Composition
	{
		final long key;
		long value;
		Entry link;

		Entry(final long key, final long ref, final Entry next)
		{
			super();
			this.key = key;
			this.value = ref;
			this.link = next;
		}

		Entry(final long key, final long value)
		{
			super();
			this.key = key;
			this.value = value;
			this.link = null;
		}

		@Override
		public Long key()
		{
			return this.key;
		}

		@Override
		public Long value()
		{
			return this.value;
		}

	}

}
