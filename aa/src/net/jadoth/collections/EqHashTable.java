package net.jadoth.collections;


import static net.jadoth.Jadoth.keyValue;
import static net.jadoth.Jadoth.notNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.interfaces.CapacityExtendable;
import net.jadoth.collections.old.BridgeXSet;
import net.jadoth.collections.old.OldSettingList;
import net.jadoth.collections.types.HashCollection;
import net.jadoth.collections.types.XEnum;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingEnum;
import net.jadoth.collections.types.XGettingMap;
import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.collections.types.XGettingTable;
import net.jadoth.collections.types.XImmutableList;
import net.jadoth.collections.types.XList;
import net.jadoth.collections.types.XProcessingCollection;
import net.jadoth.collections.types.XTable;
import net.jadoth.exceptions.ArrayCapacityException;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.IndexProcedure;
import net.jadoth.functional.JadothEqualators;
import net.jadoth.functional.Procedure;
import net.jadoth.hash.HashEqualator;
import net.jadoth.hash.JadothHash;
import net.jadoth.util.Composition;
import net.jadoth.util.Equalator;
import net.jadoth.util.KeyValue;
import net.jadoth.util.chars.VarString;


/* (12.07.2012 TM)FIXME: complete EqHashTable implementation
 * See all not implemented errors in method stubs
 */
public final class EqHashTable<K,V>
extends AbstractChainKeyValueCollection<K, V, ChainMapEntryLinkedHashedStrongStrong<K,V>>
implements XTable<K,V>, HashCollection<K>, Composition
{
	public interface Creator<K, V>
	{
		public EqHashTable<K,V> newInstance();
	}



	///////////////////////////////////////////////////////////////////////////
	//  class methods   //
	/////////////////////

	public static final <K, V> EqHashTable<K,V> New()
	{
		return new EqHashTable<>(
			DEFAULT_HASH_LENGTH,
			DEFAULT_HASH_FACTOR,
			JadothHash.hashEqualityValue()
		);
	}

	public static final <K, V> EqHashTable<K,V> NewCustom(
		final int              initialHashLength
	)
	{
		return new EqHashTable<>(
			JadothHash.padHashLength(initialHashLength),
			DEFAULT_HASH_FACTOR,
			JadothHash.hashEqualityValue()
		);
	}

	public static final <K, V> EqHashTable<K,V> NewCustom(
		final float            hashDensity
	)
	{
		return new EqHashTable<>(
			DEFAULT_HASH_LENGTH,
			JadothHash.hashDensity(hashDensity),
			JadothHash.hashEqualityValue()
		);
	}

	public static final <K, V> EqHashTable<K,V> NewCustom(
		final int              initialHashLength,
		final float            hashDensity
	)
	{
		return new EqHashTable<>(
			JadothHash.padHashLength(initialHashLength),
			JadothHash.hashDensity(hashDensity),
			JadothHash.hashEqualityValue()
		);
	}
	public static final <K, V> EqHashTable<K,V> New(
		final XGettingCollection<? extends KeyValue<? extends K, ? extends V>> entries
	)
	{
		return new EqHashTable<K,V>(
			DEFAULT_HASH_LENGTH,
			DEFAULT_HASH_FACTOR,
			JadothHash.hashEqualityValue()
		).internalAddEntries(entries);
	}

	public static final <K, V> EqHashTable<K,V> NewCustom(
		final int              initialHashLength,
		final float            hashDensity      ,
		final XGettingCollection<? extends KeyValue<? extends K, ? extends V>> entries
	)
	{
		return new EqHashTable<K,V>(
			JadothHash.padHashLength(initialHashLength),
			JadothHash.hashDensity(hashDensity),
			JadothHash.hashEqualityValue()
		).internalAddEntries(entries);
	}

	@SafeVarargs
	public static final <K, V> EqHashTable<K,V> New(
		final KeyValue<? extends K, ? extends V>... entries
	)
	{
		return new EqHashTable<K,V>(
			DEFAULT_HASH_LENGTH,
			DEFAULT_HASH_FACTOR,
			JadothHash.hashEqualityValue()
		).internalAddEntries(new ArrayView<>(entries));
	}

	@SafeVarargs
	public static final <K, V> EqHashTable<K,V> NewCustom(
		final int                                   initialHashLength,
		final float                                 hashDensity      ,
		final KeyValue<? extends K, ? extends V>... entries
	)
	{
		return new EqHashTable<K,V>(
			JadothHash.padHashLength(initialHashLength),
			JadothHash.hashDensity(hashDensity),
			JadothHash.hashEqualityValue()
		).internalAddEntries(new ArrayView<>(entries));
	}

	public static final <K, V> EqHashTable<K,V> New(
		final HashEqualator<? super K> hashEqualator
	)
	{
		return new EqHashTable<>(
			DEFAULT_HASH_LENGTH,
			DEFAULT_HASH_FACTOR,
			notNull(hashEqualator)
		);
	}

	public static final <K, V> EqHashTable<K,V> NewCustom(
		final HashEqualator<? super K> hashEqualator    ,
		final int              initialHashLength
	)
	{
		return new EqHashTable<>(
			JadothHash.padHashLength(initialHashLength),
			DEFAULT_HASH_FACTOR,
			notNull(hashEqualator)
		);
	}

	public static final <K, V> EqHashTable<K,V> NewCustom(
		final HashEqualator<? super K> hashEqualator,
		final float            hashDensity
	)
	{
		return new EqHashTable<>(
			DEFAULT_HASH_LENGTH,
			JadothHash.hashDensity(hashDensity),
			notNull(hashEqualator)
		);
	}

	public static final <K, V> EqHashTable<K,V> NewCustom(
		final HashEqualator<? super K> hashEqualator    ,
		final int              initialHashLength,
		final float            hashDensity
	)
	{
		return new EqHashTable<>(
			JadothHash.padHashLength(initialHashLength),
			JadothHash.hashDensity(hashDensity),
			notNull(hashEqualator)
		);
	}
	public static final <K, V> EqHashTable<K,V> New(
		final HashEqualator<? super K> hashEqualator,
		final XGettingCollection<? extends KeyValue<? extends K, ? extends V>> entries
	)
	{
		return new EqHashTable<K,V>(
			DEFAULT_HASH_LENGTH,
			DEFAULT_HASH_FACTOR,
			notNull(hashEqualator)
		).internalAddEntries(entries);
	}

	public static final <K, V> EqHashTable<K,V> NewCustom(
		final HashEqualator<? super K> hashEqualator    ,
		final int              initialHashLength,
		final float            hashDensity      ,
		final XGettingCollection<? extends KeyValue<? extends K, ? extends V>> entries
	)
	{
		return new EqHashTable<K,V>(
			JadothHash.padHashLength(initialHashLength),
			JadothHash.hashDensity(hashDensity),
			notNull(hashEqualator)
		).internalAddEntries(entries);
	}

	@SafeVarargs
	public static final <K, V> EqHashTable<K,V> New(
		final HashEqualator<? super K>              hashEqualator,
		final KeyValue<? extends K, ? extends V>... entries
	)
	{
		return new EqHashTable<K,V>(
			DEFAULT_HASH_LENGTH,
			DEFAULT_HASH_FACTOR,
			notNull(hashEqualator)
		).internalAddEntries(new ArrayView<>(entries));
	}

	@SafeVarargs
	public static final <K, V> EqHashTable<K,V> NewCustom(
		final HashEqualator<? super K>              hashEqualator    ,
		final int                                   initialHashLength,
		final float                                 hashDensity      ,
		final KeyValue<? extends K, ? extends V>... entries
	)
	{
		return new EqHashTable<K,V>(
			JadothHash.padHashLength(initialHashLength),
			JadothHash.hashDensity(hashDensity),
			notNull(hashEqualator)
		).internalAddEntries(new ArrayView<>(entries));
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	// data storage
	final AbstractChainKeyValueStorage<K, V, ChainMapEntryLinkedHashedStrongStrong<K,V>> chain;
	      ChainMapEntryLinkedHashedStrongStrong<K,V>[]                                   slots;

	// hashing
	final HashEqualator<? super K> hashEqualator;
	      float            hashDensity  ;

	// cached values
	int capacity, range, size;

	// satellite instances
	final Values values = new Values();
	final Keys   keys   = new Keys()  ;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	private EqHashTable(final EqHashTable<K,V> original)
	{
		super();
		this.hashDensity   = original.hashDensity;
		this.hashEqualator = original.hashEqualator;
		this.range         = original.range;

		// constructor only copies configuration (concearn #1), not data (#2). See copy() for copying data.
		this.slots         = ChainMapEntryLinkedHashedStrongStrong.array(original.slots.length);
		this.chain         = new ChainStrongStrongStorage<>(this, new ChainMapEntryLinkedHashedStrongStrong<K,V>(-1, null, null, null));
		this.capacity      = original.capacity;
	}

	private EqHashTable(
		final int              pow2InitialHashLength,
		final float            positiveHashDensity  ,
		final HashEqualator<? super K> hashEqualator
	)
	{
		super();
		this.hashDensity   = positiveHashDensity;
		this.hashEqualator = hashEqualator;
		this.range         = pow2InitialHashLength - 1;

		this.slots         = ChainMapEntryLinkedHashedStrongStrong.array(pow2InitialHashLength);
		this.chain         = new ChainStrongStrongStorage<>(this, new ChainMapEntryLinkedHashedStrongStrong<K,V>(-1, null, null, null));
		this.capacity      = (int)(pow2InitialHashLength * positiveHashDensity); // capped at MAX_VALUE
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	private ChainMapEntryLinkedHashedStrongStrong<K,V> createNewEntry(final int hash, final K key, final V value)
	{
		if(this.size >= this.capacity){
			ensureFreeArrayCapacity(this.size); // size limit only needs to be checked if size reached capacity
			this.increaseStorage();
		}

		ChainMapEntryLinkedHashedStrongStrong<K,V> e;
		this.slots[hash & this.range] = e = new ChainMapEntryLinkedHashedStrongStrong<>(hash, key, value, this.slots[hash & this.range]);
		this.size++;
		return e;
	}

	private void increaseStorage()
	{
		this.rebuildStorage((int)(this.slots.length * 2.0f));
	}

	private void rebuildStorage(final int newSlotLength)
	{
		final ChainMapEntryLinkedHashedStrongStrong<K,V>[] newSlots =  ChainMapEntryLinkedHashedStrongStrong.array(newSlotLength);
		final int modulo = newSlotLength >= Integer.MAX_VALUE ?Integer.MAX_VALUE :newSlotLength - 1;

		// iterate through all entries and assign them to the new storage
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> entry = this.chain.head(); (entry = entry.next) != null; ){
			entry.link = newSlots[entry.hash & modulo];
			newSlots[entry.hash & modulo] = entry;
		}

		this.capacity = newSlotLength >= Integer.MAX_VALUE ?Integer.MAX_VALUE :(int)(newSlotLength * this.hashDensity);
		this.slots = newSlots;
		this.range = modulo;
	}

	final boolean internalAddOnlyKey(final K key)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				return false; // already contained
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, key, null));
		return true;
	}

	final boolean internalPutOnlyKey(final K key)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				e.setKey(key); // intentionally no moving to end here to cleanly separate concearns
				return false;
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, key, null));
		return true;
	}

	final K internalPutGetKey(final K key)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				return e.setKey(key);
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, key, null));
		return null;
	}

	final K internalAddGetKey(final K key)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				return e.key();
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, key, null));
		return null;
	}

	final K internalSubsituteKey(final K key)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				return e.key();
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, key, null));
		return key;
	}

	final void internalAdd(final K key, final V value)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				return;
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, key, value));
	}

	final EqHashTable<K,V> internalAddEntries(final XGettingCollection<? extends KeyValue<? extends K, ? extends V>> entries)
	{
		entries.iterate(new Procedure<KeyValue<? extends K, ? extends V>>() {
			@Override public void accept(final KeyValue<? extends K, ? extends V> e) {
				EqHashTable.this.internalAdd(e.key(), e.value());
			}
		});
		return this;
	}


	// only used for backwards compatibility with old collections
	final V oldPutGet(final K key, final V value)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				// set only value, not key, according to inconsistent nonsense behaviour in old collections
				return e.setValue(value);
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, key, value));
		return null;
	}

	final KeyValue<K,V> getEntry(final K key)
	{
		final int hash; // search for key by hash
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				return e;
			}
		}
		return null;
	}

	final boolean containsKey(final K key)
	{
		final int hash; // search for element by hash
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				return true;
			}
		}
		return false;
	}

	final int removeKey(final K key)
	{
		final int hash = this.hashEqualator.hash(key);
		ChainMapEntryLinkedHashedStrongStrong<K,V> last, e;
		if((e = this.slots[hash & this.range]) == null){
			return 0;
		}

		// head entry special case
		if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
			this.slots[hash & this.range] = e.link;
			this.chain.disjoinEntry(e);
			this.size--;
			return 1; // return as key can only be contained once in a set
		}

		// search entry chain
		for(e = (last = e).link; e != null; e = (last = e).link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				last.link = e.link;
				this.chain.disjoinEntry(e);
				this.size--;
				// no further actions necessary (like removing the key etc) as entry will get garbage collected
				return 1; // return as key can only be contained once in a set
			}
		}

		return 0;
	}

	final void removeNullEntry()
	{
		this.remove((K)null);
	}

	boolean nullKeyPut()
	{
		return this.internalPutOnlyKey(null);
	}

	boolean nullKeyAdd()
	{
		return this.internalAddOnlyKey(null);
	}

	final void internalCollectUnhashed(final K key, final V value)
	{
		this.chain.appendEntry(new ChainMapEntryLinkedHashedStrongStrong<>(0, key, value, null));
	}



	///////////////////////////////////////////////////////////////////////////
	// inheriteted ExtendedCollection methods  //
	////////////////////////////////////////////

	@Override
	protected int internalCountingAddAll(final KeyValue<K,V>[] elements) throws UnsupportedOperationException
	{
		return this.internalCountingAddAll(elements, 0, elements.length);
	}

	@Override
	protected int internalCountingAddAll(final KeyValue<K,V>[] elements, final int offset, final int length) throws UnsupportedOperationException
	{
		final int bound = offset + length;
		int count = 0;
		for(int i = offset; i < bound; i++){
			if(this.add(elements[i])){
				count++;
			}
		}
		return count;
	}

	@Override
	protected int internalCountingAddAll(final XGettingCollection<? extends KeyValue<K,V>> elements) throws UnsupportedOperationException
	{
		return elements.iterate(new Procedure<KeyValue<K,V>>() {
			int count = 0;
			@Override public void accept(final KeyValue<K, V> e) {
				if(EqHashTable.this.add(e)){
					this.count++;
				}
			}
		}).count;
	}

	@Override
	protected int internalCountingPutAll(final KeyValue<K,V>[] elements) throws UnsupportedOperationException
	{
		return this.internalCountingAddAll(elements, 0, elements.length);
	}

	@Override
	protected int internalCountingPutAll(final KeyValue<K,V>[] elements, final int offset, final int length) throws UnsupportedOperationException
	{
		final int bound = offset + length;
		int count = 0;
		for(int i = offset; i < bound; i++){
			if(this.put(elements[i])){
				count++;
			}
		}
		return count;
	}

	@Override
	protected int internalCountingPutAll(final XGettingCollection<? extends KeyValue<K,V>> elements) throws UnsupportedOperationException
	{
		return elements.iterate(new Procedure<KeyValue<K,V>>() {
			int count = 0;
			@Override public void accept(final KeyValue<K, V> e) {
				if(EqHashTable.this.put(e)){
					this.count++;
				}
			}
		}).count;
	}

	@Override
	protected int internalRemoveNullEntries()
	{
		return this.removeKey(null);
	}

	@Override
	protected void internalRemoveEntry(final ChainMapEntryLinkedHashedStrongStrong<K,V> entry)
	{
		final ChainMapEntryLinkedHashedStrongStrong<K,V> setEntry = entry;
		ChainMapEntryLinkedHashedStrongStrong<K,V> last, e = this.slots[setEntry.hash & this.range];

		// remove entry from hashing chain
		if(e == setEntry){ // head entry special case
			this.slots[setEntry.hash & this.range] = setEntry.link;
		}
		else {
			while((e = (last = e).link) != null){
				if(e == setEntry){
					last.link = setEntry.link;
					break;
				}
			}
			// consistency check (passed entry e may not be contained in the hash chain at all)
			if(e == null){
				throw new IllegalArgumentException("Entry inconsistency detected");
			}
		}

		// remove entry e (unlink and disjoin)
		this.size--;
		this.chain.disjoinEntry(setEntry);
	}

	@Override
	protected int internalClear()
	{
		final int size = this.size;
		this.clear();
		return size;
	}

	@Override
	protected AbstractChainKeyValueStorage<K, V, ChainMapEntryLinkedHashedStrongStrong<K,V>> getInternalStorageChain()
	{
		return this.chain;
	}

	@Override
	public final long size()
	{
		return EqHashTable.this.size;
	}

	@Override
	public final boolean isEmpty()
	{
		return this.size == 0;
	}

	@Override
	public final void clear()
	{
		// clear chain
		this.chain.clear();

		// clear hash array
		final ChainMapEntryLinkedHashedStrongStrong<K,V>[] slots = this.slots;
		for(int i = 0, length = slots.length; i < length; i++){
			slots[i] = null;
		}

		// reset singleton fields
		this.size = 0;
	}

	@Override
	public final void truncate()
	{
		this.chain.clear();
		this.slots = ChainMapEntryLinkedHashedStrongStrong.array(DEFAULT_HASH_LENGTH);
		this.size = 0;
		this.capacity = (int)(1 * this.hashDensity);
	}

	@Override
	public final int consolidate()
	{
		return this.chain.consolidate();
	}

	@Override
	public final CapacityExtendable ensureCapacity(final long minimalCapacity)
	{
		if(this.capacity >= minimalCapacity){
			return this; // already enough free capacity
		}

		final int requiredSlotLength = (int)(minimalCapacity / this.hashDensity);
		if(requiredSlotLength > 1<<30){ // (technical) magic value
			this.rebuildStorage(Integer.MAX_VALUE); // special case: maximum slots length needed ("perfect" hashing)
			return this;
		}

		// normal case: calculate new slots legnth and rebuild storage
		int newSlotsLength = this.slots.length;
		while(newSlotsLength < requiredSlotLength){
			newSlotsLength <<= 1;
		}
		this.rebuildStorage(newSlotsLength);
		return this;
	}

	@Override
	public final CapacityExtendable ensureFreeCapacity(final long requiredFreeCapacity)
	{
		if(this.capacity - this.size >= requiredFreeCapacity){
			return this; // already enough free capacity
		}
		// overflow-safe check for unreachable capacity
		if(Integer.MAX_VALUE - this.size < requiredFreeCapacity){
			throw new ArrayCapacityException(requiredFreeCapacity + this.size);
		}

		final int requiredSlotLength = (int)((this.size + requiredFreeCapacity) / this.hashDensity);
		if(requiredSlotLength > 1<<30){ // (technical) magic value
			this.rebuildStorage(Integer.MAX_VALUE); // special case: maximum slots length needed ("perfect" hashing)
			return this;
		}
		int newSlotsLength = this.slots.length;
		while(newSlotsLength < requiredSlotLength){
			newSlotsLength <<= 1;
		}
		this.rebuildStorage(newSlotsLength);
		return this;
	}

	@Override
	public final int optimize()
	{
		final int requiredCapacity;
		if((requiredCapacity = (int)(this.size / this.hashDensity)) > 1<<30){ // (technical) magic value
			if(this.slots.length != Integer.MAX_VALUE){
				this.rebuildStorage(Integer.MAX_VALUE);
			}
			return this.capacity;
		}
		final int newCapacity = JadothHash.padHashLength(requiredCapacity);
		if(this.slots.length != newCapacity){
			this.rebuildStorage(newCapacity); // rebuild storage with new capacity
		}
		return this.capacity;
	}

	@Override
	public final int rehash()
	{
		// local helper variables, including capacity recalculation while at rebuilding anyway
		final int                                          reqCapacity   = JadothHash.padHashLength((int)(this.size / this.hashDensity));
		final ChainMapEntryLinkedHashedStrongStrong<K,V>[] slots         = ChainMapEntryLinkedHashedStrongStrong.<K,V>array(reqCapacity);
		final int                                          range         = reqCapacity >= Integer.MAX_VALUE ?Integer.MAX_VALUE :reqCapacity - 1;
		final HashEqualator<? super K>                     hashEqualator = this.hashEqualator;
		final AbstractChainKeyValueStorage<K, V, ChainMapEntryLinkedHashedStrongStrong<K,V>> chain = this.chain;

		// keep the old chain head for old entries iteration and clear the chain for the new entries
		ChainMapEntryLinkedHashedStrongStrong<K,V> entry = chain.head().next;
		chain.clear();

		int size = 0;
		oldEntries:
		for(/*entry must be outside, see comment*/; entry != null; entry = entry.next)
		{
			final int hash = hashEqualator.hash(entry.key);

			// check for rehash collisions
			for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = slots[hash & range]; e != null; e = e.link){
				if(e.hash == hash && hashEqualator.equal(e.key, entry.key)){
					continue oldEntries; // hash collision: key already contained, discard old entry
				}
			}

			// register new entry for unique element
			chain.appendEntry(slots[hash & range] =
				new ChainMapEntryLinkedHashedStrongStrong<>(hash, entry.key, entry.value, slots[hash & range]))
			;
			size++;
		}

		// update collection state with new members
		this.slots = slots;
		this.range = range;
		this.size  = size ;
		return size;
	}

	@Override
	public final EqHashTable<K,V> copy()
	{
		final EqHashTable<K,V> newVarMap = new EqHashTable<>(this);
		this.chain.iterate(new Procedure<KeyValue<K,V>>(){
			@Override public void accept(final KeyValue<K, V> entry){
				newVarMap.put(entry.key(), entry.value());
			}
		});
		return newVarMap;
	}

	@Override
	public final EqConstHashTable<K,V> immure()
	{
		this.consolidate();
		return EqConstHashTable.NewCustom(this.hashEqualator, this.size, this.hashDensity, this);
	}

	@Override
	public final XGettingTable<K, V> view()
	{
		return new TableView<>(this);
	}

	@Override
	public final void setHashDensity(final float hashDensity)
	{
		this.capacity = (int)(this.slots.length * (this.hashDensity = JadothHash.hashDensity(hashDensity))); // cast caps at max value
		this.optimize();
	}

	@Override
	public final boolean hasVolatileElements()
	{
		return false;
	}

	@Override
	public final boolean nullAllowed()
	{
		return true;
	}

	@Override
	public final boolean nullKeyAllowed()
	{
		return true;
	}

	@Override
	public final boolean nullValuesAllowed()
	{
		return true;
	}

	@Override
	public final V get(final K key)
	{
		final int hash; // search for key by hash
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				return e.value();
			}
		}
		return null;
	}

	@Override
	public final V ensure(final K key, final Function<? super K, V> valueProvider)
	{
		V value = this.get(key);
		if(value == null){
			this.add(key, value = valueProvider.apply(key));
		}
		return value;
	}

	@Override
	public final EqHashTable<K, V>.Keys keys()
	{
		return this.keys;
	}

	@Override
	public final XTable.EntriesBridge<K, V> old()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME EqHashTable#old()
	}

	@Override
	public net.jadoth.collections.types.XTable.Bridge<K, V> oldMap()
	{
		return new OldVarMap();
	}

	@Override
	public final V searchValue(final Predicate<? super K> keyPredicate)
	{
		final KeyValue<K,V> foundEntry = this.chain.search(new Predicate<KeyValue<K,V>>() {
			@Override public boolean test(final KeyValue<K, V> entry)
			{
				return keyPredicate.test(entry.key());
			}
		});
		return foundEntry != null ?foundEntry.value() :null;
	}

	@Override
	public final <C extends Procedure<? super V>> C query(final XIterable<? extends K> keys, final C collector)
	{
		keys.iterate(new Procedure<K>() {
			@Override
			public void accept(final K key)
			{
				collector.accept(EqHashTable.this.get(key));
			}
		});
		return collector;
	}

	@Override
	public final Values values()
	{
		return this.values;
	}

	@Override
	public final long currentCapacity()
	{
		return this.capacity;
	}

	@Override
	public final long maximumCapacity()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public final boolean isFull()
	{
		return this.size >= Integer.MAX_VALUE;
	}

	@Override
	public final long remainingCapacity()
	{
		return Integer.MAX_VALUE - this.size;
	}

	@Override
	public final boolean hasVolatileValues()
	{
		return this.chain.hasVolatileValues();
	}

	@Override
	public final KeyValue<K,V> putGet(final K key, final V value)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				return keyValue(e.setKey(key), e.setValue(value));
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, key, value));
		return null;
	}

	@Override
	public final KeyValue<K,V> addGet(final K key, final V value)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				return e;
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, key, value));
		return null;
	}

	@Override
	public final KeyValue<K,V> setGet(final K key, final V value)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				return keyValue(e.setKey(key), e.setValue(value));
			}
		}
		return null;
	}

	@Override
	public final boolean add(final K key, final V value)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				return false; // already contained
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, key, value));
		return true;
	}

	@Override
	public final boolean put(final K key, final V value)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				e.set0(key, value); // intentionally no moving to end here to cleanly separate concearns
				return false;
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, key, value));
		return true;
	}

	@Override
	public final boolean set(final K key, final V value)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				e.set0(key, value);
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean valuePut(final K key, final V value)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				e.setValue0(value);
				return false;
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, key, value));
		return true;
	}

	@Override
	public final boolean valueSet(final K key, final V value)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				e.setValue0(value);
				return true;
			}
		}
		return false;
	}

	@Override
	public final V valuePutGet(final K key, final V value)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				return e.setValue(value);
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, key, value));
		return null;
	}

	@Override
	public final V valueSetGet(final K key, final V value)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				return e.setValue(value);
			}
		}
		return null;
	}

	@Override
	public final V remove(final K key)
	{
		final int hash;
		ChainMapEntryLinkedHashedStrongStrong<K,V> last, e;
		if((e = this.slots[(hash = this.hashEqualator.hash(key)) & this.range]) == null){
			return null;
		}

		// head entry special case
		if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
			this.slots[hash & this.range] = e.link;
			this.chain.disjoinEntry(e);
			this.size--;
			return e.value(); // return as value can only be contained once in a set
		}

		// search entry chain
		for(e = (last = e).link; e != null; e = (last = e).link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), key)){
				last.link = e.link;
				this.chain.disjoinEntry(e);
				this.size--;
				// no further actions necessary (like removing the value etc) as entry will get garbage collected
				return e.value(); // return as value can only be contained once in a set
			}
		}

		return null;
	}

	@Override
	public final HashCollection.Analysis<EqHashTable<K,V>> analyze()
	{
		return AbstractChainEntryLinked.analyzeSlots(this, this.slots);
	}

	@Override
	public final int hashDistributionRange()
	{
		return this.slots.length;
	}

	@Override
	public final HashEqualator<? super K> hashEquality()
	{
		return this.hashEqualator;
	}

	@Override
	public final float hashDensity()
	{
		return this.hashDensity;
	}

	@Override
	public final boolean hasVolatileHashElements()
	{
		return this.chain.hasVolatileElements();
	}

	@Override
	public final String toString()
	{
		return this.chain.appendTo(VarString.New(this.slots.length).append('{'), ",").append('}').toString();
	}

	public final Procedure<K> procedureRemoveKey()
	{
		return new Procedure<K>(){
			@Override public void accept(final K key) {
				EqHashTable.this.removeKey(key);
			}}
		;
	}

	public final Procedure<KeyValue<K,V>> procedureRemoveEntry()
	{
		return new Procedure<KeyValue<K,V>>(){
			@Override public void accept(final KeyValue<K,V> entry) {
				EqHashTable.this.removeKey(entry.key());
			}}
		;
	}

	public final Predicate<K> predicateContainsKey()
	{
		return new Predicate<K>(){ @Override public boolean test(final K key) {
			return EqHashTable.this.containsKey(key);
		}};
	}

	public final Predicate<KeyValue<K,V>> predicateContainsEntry()
	{
		return new Predicate<KeyValue<K,V>>(){ @Override public boolean test(final KeyValue<K,V> entry) {
			final KeyValue<K,V> kv;
			if((kv = EqHashTable.this.getEntry(entry.key())) == null) return false;

			// equality of values is architectural restricted to simple referential equality
			return EqHashTable.this.hashEqualator.equal(kv.key(), entry.key()) && kv.value() == entry.value();
		}};
	}

	@Override
	public final EqHashTable<K, V> sort(final Comparator<? super KeyValue<K, V>> comparator)
	{
		this.chain.sort(comparator);
		return this;
	}



	///////////////////////////////////////////////////////////////////////////
	// getting methods  //
	/////////////////////

	@Override
	public final XEnum<KeyValue<K,V>> range(final int lowIndex, final int highIndex)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#range()
	}

	@Override
	public final XGettingEnum<KeyValue<K,V>> view(final int lowIndex, final int highIndex)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#view()
	}

	@Override
	public final KeyValue<K,V>[] toArray(final Class<KeyValue<K,V>> type)
	{
		return EqHashTable.this.chain.toArray(type);
	}

	// executing //

	@Override
	public final <P extends Procedure<? super KeyValue<K,V>>> P iterate(final P procedure)
	{
		EqHashTable.this.chain.iterate(procedure);
		return procedure;
	}

	@Override
	public final <A> A join(final BiProcedure<? super KeyValue<K,V>, ? super A> joiner, final A aggregate)
	{
		EqHashTable.this.chain.join(joiner, aggregate);
		return aggregate;
	}

	@Override
	public final int count(final KeyValue<K,V> entry)
	{
		return EqHashTable.this.chain.count(entry, this.equality());
	}

	@Override
	public final int countBy(final Predicate<? super KeyValue<K,V>> predicate)
	{
		return EqHashTable.this.chain.count(predicate);
	}

	// element querying //

	@Override
	public final KeyValue<K,V> search(final Predicate<? super KeyValue<K,V>> predicate)
	{
		return EqHashTable.this.chain.search(predicate);
	}

	@Override
	public final KeyValue<K,V> max(final Comparator<? super KeyValue<K,V>> comparator)
	{
		return EqHashTable.this.chain.max(comparator);
	}

	@Override
	public final KeyValue<K,V> min(final Comparator<? super KeyValue<K,V>> comparator)
	{
		return EqHashTable.this.chain.min(comparator);
	}

	/**
	 * As per definition of a set, this method always returns true.<br>
	 * Note that mutated elements whose hashcode has not been immuted by the employed hash logic
	 * can be contained multiple times, effectively breaking this method (because of breaking the hashing logic in the
	 * first place), so this information only has value if the elements' implementation is immutable or if the
	 * hash logic compensated their mutability (e.g. by using the identity hash code or by registering a once created
	 * hashcode, effectively "immuting" it).
	 *
	 * @return
	 * @see XGettingCollection#hasDistinctValues()
	 */
	@Override
	public final boolean hasDistinctValues()
	{
		return true;
	}

	@Override
	public final boolean hasDistinctValues(final Equalator<? super KeyValue<K,V>> equalator)
	{
		return EqHashTable.this.chain.hasDistinctValues(equalator);
	}

	// boolean querying - applies //

	@Override
	public final boolean containsSearched(final Predicate<? super KeyValue<K,V>> predicate)
	{
		return EqHashTable.this.chain.applies(predicate);
	}

	@Override
	public final boolean applies(final Predicate<? super KeyValue<K,V>> predicate)
	{
		return EqHashTable.this.chain.appliesAll(predicate);
	}

	// boolean querying - contains //

	@Override
	public final boolean nullContained()
	{
		return false;
	}

	@Override
	public final boolean containsId(final KeyValue<K,V> entry)
	{
		final int hash; // search for element by hash
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = EqHashTable.this.slots[(hash = EqHashTable.this.hashEqualator.hash(entry.key())) & EqHashTable.this.range]; e != null; e = e.link){
			if(hash == e.hash && entry == e.key()){
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean contains(final KeyValue<K,V> entry)
	{
		final int hash; // search for element by hash
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = EqHashTable.this.slots[(hash = EqHashTable.this.hashEqualator.hash(entry.key())) & EqHashTable.this.range]; e != null; e = e.link){
			if(e.hash == hash && EqHashTable.this.hashEqualator.equal(e.key(), entry.key())){
				return true;
			}
		}
		return false;
	}

	@Override
	public final KeyValue<K, V> seek(final KeyValue<K, V> sample)
	{
		if(sample == null){ // null special case
			return null;
		}

		final int hash; // search for element by hash
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = EqHashTable.this.slots[(hash = EqHashTable.this.hashEqualator.hash(sample.key())) & EqHashTable.this.range]; e != null; e = e.link){
			if(e.hash == hash && EqHashTable.this.hashEqualator.equal(e.key(), sample.key())){
				return e;
			}
		}
		return null;
	}

	@Override
	public final boolean containsAll(final XGettingCollection<? extends KeyValue<K,V>> elements)
	{
		return elements.applies(EqHashTable.this.predicateContainsEntry());
	}

	// boolean querying - equality //

	@Override
	public final boolean equals(final XGettingCollection<? extends KeyValue<K,V>> samples, final Equalator<? super KeyValue<K,V>> equalator)
	{
		if(samples == null || !(samples instanceof EqHashTable<?,?>.Keys)){
			return false;
		}
		if(samples == this){
			return true;
		}
		return this.equalsContent(samples, equalator);
	}

	@Override
	public final boolean equalsContent(final XGettingCollection<? extends KeyValue<K,V>> samples, final Equalator<? super KeyValue<K,V>> equalator)
	{
		this.consolidate();
		if(EqHashTable.this.size != Jadoth.to_int(samples.size())){
			return false;
		}

		// if sizes are equal and all elements of collection are contained in this set, they must have equal content
		return EqHashTable.this.chain.equalsContent(samples, equalator);
	}

	// data set procedures //

	@Override
	public final <C extends Procedure<? super KeyValue<K,V>>> C intersect(
		final XGettingCollection<? extends KeyValue<K,V>> other,
		final Equalator<? super KeyValue<K,V>> equalator,
		final C target
	)
	{
		return EqHashTable.this.chain.intersect(other, equalator, target);
	}

	@Override
	public final <C extends Procedure<? super KeyValue<K,V>>> C except(
		final XGettingCollection<? extends KeyValue<K,V>> other,
		final Equalator<? super KeyValue<K,V>> equalator,
		final C target
	)
	{
		return EqHashTable.this.chain.except(other, equalator, target);
	}

	@Override
	public final <C extends Procedure<? super KeyValue<K,V>>> C union(
		final XGettingCollection<? extends KeyValue<K,V>> other,
		final Equalator<? super KeyValue<K,V>> equalator,
		final C target
	)
	{
		return EqHashTable.this.chain.union(other, equalator, target);
	}

	@Override
	public final <C extends Procedure<? super KeyValue<K,V>>> C copyTo(final C target)
	{
		if(target == this){
			return target; // copying a set logic collection to itself would be a no-op, so spare the effort
		}
		return EqHashTable.this.chain.copyTo(target);
	}

	@Override
	public final <C extends Procedure<? super KeyValue<K,V>>> C filterTo(final C target, final Predicate<? super KeyValue<K,V>> predicate)
	{
		return EqHashTable.this.chain.copyTo(target, predicate);
	}

	@Override
	public final <T> T[] copyTo(final T[] target, final int offset)
	{
		EqHashTable.this.chain.copyToArray(0, EqHashTable.this.size, target, offset);
		return target;
	}

	@Override
	public final <C extends Procedure<? super KeyValue<K,V>>> C distinct(final C target)
	{
		return this.distinct(target, this.equality());
	}

	@Override
	public final <C extends Procedure<? super KeyValue<K,V>>> C distinct(final C target, final Equalator<? super KeyValue<K,V>> equalator)
	{
		return EqHashTable.this.chain.distinct(target, equalator);
	}

	@Override
	public final boolean nullAdd()
	{
		return EqHashTable.this.nullKeyAdd();
	}

	@Override
	public final boolean add(final KeyValue<K,V> entry)
	{
		return EqHashTable.this.add(entry.key(), entry.value());

	}

	@SafeVarargs
	@Override
	public final EqHashTable<K,V> addAll(final KeyValue<K,V>... elements)
	{
		final EqHashTable<K,V> parent = EqHashTable.this;
		for(int i = 0, len = elements.length; i < len; i++) {
			parent.add(elements[i].key(), elements[i].value());
		}
		return this;
	}

	@Override
	public final EqHashTable<K,V> addAll(final KeyValue<K,V>[] elements, final int srcIndex, final int srcLength)
	{
		final int d;
		if((d = JadothArrays.validateArrayRange(elements, srcIndex, srcLength)) == 0) return this;
		final int bound = srcIndex + srcLength;
		final EqHashTable<K,V> parent = EqHashTable.this;
		for(int i = srcIndex; i != bound; i+=d) {
			parent.add(elements[i].key(), elements[i].value());
		}
		return this;
	}

	@Override
	public final EqHashTable<K,V> addAll(final XGettingCollection<? extends KeyValue<K,V>> elements)
	{
		final EqHashTable<K,V> parent = EqHashTable.this;
		elements.iterate(new Procedure<KeyValue<K,V>>(){ @Override public void accept(final KeyValue<K,V> e){
			parent.add(e.key(), e.value());
		}});
		return this;
	}

	@Override
	public final boolean nullPut()
	{
		return EqHashTable.this.nullKeyPut();
	}

	@Override
	public final void accept(final KeyValue<K,V> entry)
	{
		EqHashTable.this.put(entry.key(), entry.value());
	}

	@Override
	public final boolean put(final KeyValue<K,V> entry)
	{
		return EqHashTable.this.put(entry.key(), entry.value());
	}

	@Override
	public final KeyValue<K,V> putGet(final KeyValue<K,V> entry)
	{
		return EqHashTable.this.putGet(entry.key(), entry.value());
	}

	@Override
	public final KeyValue<K,V> addGet(final KeyValue<K,V> entry)
	{
		return EqHashTable.this.addGet(entry.key(), entry.value());
	}

	@Override
	public final KeyValue<K, V> substitute(final KeyValue<K, V> entry)
	{
		final int hash;
		for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = this.slots[(hash = this.hashEqualator.hash(entry.key())) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.key(), entry.key())){
				return e;
			}
		}
		final ChainMapEntryLinkedHashedStrongStrong<K,V> newEntry;
		this.chain.appendEntry(newEntry = this.createNewEntry(hash, entry.key(), entry.value()));
		return newEntry;
	}

	@SafeVarargs
	@Override
	public final EqHashTable<K,V> putAll(final KeyValue<K,V>... elements)
	{
		final EqHashTable<K,V> parent = EqHashTable.this;
		for(int i = 0, len = elements.length; i < len; i++) {
			parent.put(elements[i].key(), elements[i].value());
		}
		return this;
	}

	@Override
	public final EqHashTable<K,V> putAll(final KeyValue<K,V>[] elements, final int srcIndex, final int srcLength)
	{
		final int d;
		if((d = JadothArrays.validateArrayRange(elements, srcIndex, srcLength)) == 0) return this;
		final int bound = srcIndex + srcLength;
		final EqHashTable<K,V> parent = EqHashTable.this;
		for(int i = srcIndex; i != bound; i+=d) {
			parent.put(elements[i].key(), elements[i].value());
		}
		return this;
	}

	@Override
	public final EqHashTable<K,V> putAll(final XGettingCollection<? extends KeyValue<K,V>> elements)
	{
		return elements.copyTo(this);
	}

	// removing //

	@Override
	public final int remove(final KeyValue<K,V> entry)
	{
		return EqHashTable.this.chain.remove(entry, this.equality());
	}

	@Override
	public final int nullRemove()
	{
		return 0; // cannot remove a null entry because it can never be contained (only null key or null values)
	}

	// reducing //

	@Override
	public final int removeBy(final Predicate<? super KeyValue<K,V>> predicate)
	{
		return EqHashTable.this.chain.reduce(predicate);
	}

	// retaining //

	@Override
	public final int retainAll(final XGettingCollection<? extends KeyValue<K,V>> elements)
	{
		return EqHashTable.this.chain.retainAll(elements, this.equality());
	}

	@Override
	public final <P extends Procedure<? super KeyValue<K, V>>> P process(final P procedure)
	{
		EqHashTable.this.chain.process(procedure);
		return procedure;
	}

	@Override
	public final <C extends Procedure<? super KeyValue<K,V>>> C moveTo(final C target, final Predicate<? super KeyValue<K,V>> predicate)
	{
		EqHashTable.this.chain.moveTo(target, predicate);
		return target;
	}

	// removing - all //

	@Override
	public final int removeAll(final XGettingCollection<? extends KeyValue<K,V>> elements)
	{
		final int oldSize = EqHashTable.this.size;
		elements.iterate(EqHashTable.this.procedureRemoveEntry());
		return oldSize - EqHashTable.this.size;
	}

	// removing - duplicates //

	@Override
	public final int removeDuplicates()
	{
		return 0;
	}

	@Override
	public final int removeDuplicates(final Equalator<? super KeyValue<K,V>> equalator)
	{
		// singleton null can be ignored here
		return EqHashTable.this.chain.removeDuplicates(equalator);
	}

	@Override
	public final EqHashTable<K,V> toReversed()
	{
		final EqHashTable<K,V> reversedVarSet = EqHashTable.this.copy();
		reversedVarSet.chain.reverse();
		return reversedVarSet;
	}

	@Override
	public final <T extends Procedure<? super KeyValue<K,V>>> T copySelection(final T target, final int... indices)
	{
		EqHashTable.this.chain.copySelection(target, indices);
		return target;
	}

	@Override
	public final <T> T[] copyTo(final T[] target, final int targetOffset, final int offset, final int length)
	{
		EqHashTable.this.chain.copyToArray(offset, length, target, targetOffset);
		return target;
	}

	@Override
	public final <P extends IndexProcedure<? super KeyValue<K, V>>> P iterate(final P procedure)
	{
		EqHashTable.this.chain.iterate(procedure);
		return procedure;
	}

	@Override
	public final KeyValue<K,V> at(final int index)
	{
		return EqHashTable.this.chain.get(index);
	}

	@Override
	public final KeyValue<K,V> get()
	{
		return EqHashTable.this.chain.first();
	}

	@Override
	public final KeyValue<K,V> first()
	{
		return EqHashTable.this.chain.first();
	}

	@Override
	public final KeyValue<K,V> last()
	{
		return EqHashTable.this.chain.last();
	}

	@Override
	public final KeyValue<K,V> poll()
	{
		return EqHashTable.this.size == 0 ?null :EqHashTable.this.chain.first();
	}

	@Override
	public final KeyValue<K,V> peek()
	{
		return EqHashTable.this.size == 0 ?null :EqHashTable.this.chain.last();
	}

	@Override
	public final int indexOf(final KeyValue<K,V> entry)
	{
		return EqHashTable.this.chain.indexOf(entry);
	}

	@Override
	public final int indexBy(final Predicate<? super KeyValue<K,V>> predicate)
	{
		return EqHashTable.this.chain.indexOf(predicate);
	}

	@Override
	public final boolean isSorted(final Comparator<? super KeyValue<K,V>> comparator)
	{
		return EqHashTable.this.chain.isSorted(comparator);
	}

	@Override
	public final int lastIndexOf(final KeyValue<K,V> entry)
	{
		return EqHashTable.this.chain.rngIndexOf(EqHashTable.this.size - 1, -EqHashTable.this.size, entry);
	}

	@Override
	public final int lastIndexBy(final Predicate<? super KeyValue<K,V>> predicate)
	{
		return EqHashTable.this.chain.rngIndexOf(EqHashTable.this.size - 1, -EqHashTable.this.size, predicate);
	}

	@Override
	public final int maxIndex(final Comparator<? super KeyValue<K,V>> comparator)
	{
		return EqHashTable.this.chain.maxIndex(comparator);
	}

	@Override
	public final int minIndex(final Comparator<? super KeyValue<K,V>> comparator)
	{
		return EqHashTable.this.chain.minIndex(comparator);
	}

	@Override
	public final int scan(final Predicate<? super KeyValue<K,V>> predicate)
	{
		return EqHashTable.this.chain.scan(predicate);
	}

	@Override
	public final <C extends Procedure<? super KeyValue<K,V>>> C moveSelection(final C target, final int... indices)
	{
		EqHashTable.this.chain.moveSelection(target, indices);
		return target;
	}

	@Override
	public final KeyValue<K,V> removeAt(final int index)
	{
		return EqHashTable.this.chain.remove(index);
	}

	@Override
	public final KeyValue<K,V> fetch()
	{
		return EqHashTable.this.chain.remove(0);
	}

	@Override
	public final KeyValue<K,V> pop()
	{
		return EqHashTable.this.chain.remove(EqHashTable.this.size - 1);
	}

	@Override
	public final KeyValue<K,V> pinch()
	{
		return EqHashTable.this.size == 0 ?null :EqHashTable.this.chain.remove(0);
	}

	@Override
	public final KeyValue<K,V> pick()
	{
		return EqHashTable.this.size == 0 ?null :EqHashTable.this.chain.remove(EqHashTable.this.size - 1);
	}

	@Override
	public final KeyValue<K,V> retrieve(final KeyValue<K,V> entry)
	{
		return EqHashTable.this.chain.retrieve(entry);
	}

	@Override
	public final KeyValue<K,V> retrieveBy(final Predicate<? super KeyValue<K,V>> predicate)
	{
		return EqHashTable.this.chain.retrieve(predicate);
	}

	@Override
	public final boolean removeOne(final KeyValue<K,V> entry)
	{
		return EqHashTable.this.chain.removeOne(entry);
	}

	@Override
	public final EqHashTable<K,V> removeRange(final int startIndex, final int length)
	{
		EqHashTable.this.chain.removeRange(startIndex, length);
		return this;
	}

	@Override
	public final EqHashTable<K,V> retainRange(final int startIndex, final int length)
	{
		EqHashTable.this.chain.retainRange(startIndex, length);
		return this;
	}

	@Override
	public final int removeSelection(final int[] indices)
	{
		return EqHashTable.this.chain.removeSelection(indices);
	}

	@Override
	public final Iterator<KeyValue<K,V>> iterator()
	{
		return EqHashTable.this.chain.iterator();
	}

	@Override
	public final Object[] toArray()
	{
		return EqHashTable.this.chain.toArray();
	}

	@Override
	public final EqHashTable<K,V> reverse()
	{
		EqHashTable.this.chain.reverse();
		return this;
	}

	@Override
	public final EqHashTable<K,V> shiftTo(final int sourceIndex, final int targetIndex)
	{
		EqHashTable.this.chain.shiftTo(sourceIndex, targetIndex);
		return this;
	}

	@Override
	public final EqHashTable<K,V> shiftTo(final int sourceIndex, final int targetIndex, final int length)
	{
		EqHashTable.this.chain.shiftTo(sourceIndex, targetIndex, length);
		return this;
	}

	@Override
	public final EqHashTable<K,V> shiftBy(final int sourceIndex, final int distance)
	{
		EqHashTable.this.chain.shiftTo(sourceIndex, distance);
		return this;
	}

	@Override
	public final EqHashTable<K,V> shiftBy(final int sourceIndex, final int distance, final int length)
	{
		EqHashTable.this.chain.shiftTo(sourceIndex, distance, length);
		return this;
	}

	@Override
	public final EqHashTable<K,V> swap(final int indexA, final int indexB)
	{
		EqHashTable.this.chain.swap(indexA, indexB);
		return this;
	}

	@Override
	public final EqHashTable<K,V> swap(final int indexA, final int indexB, final int length)
	{
		EqHashTable.this.chain.swap(indexA, indexB, length);
		return this;
	}

	@Override
	public final HashEqualator<KeyValue<K,V>> equality()
	{
		return JadothHash.<K,V>wrapAsKeyValue(EqHashTable.this.hashEqualator);
	}

	@Override
	public final boolean input(final int index, final KeyValue<K, V> element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#input()
	}

	@SafeVarargs
	@Override
	public final int inputAll(final int index, final KeyValue<K, V>... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#input()
	}

	@Override
	public final int inputAll(final int index, final KeyValue<K, V>[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#inputAll()
	}

	@Override
	public final int inputAll(final int index, final XGettingCollection<? extends KeyValue<K, V>> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#inputAll()
	}

	@Override
	public final boolean insert(final int index, final KeyValue<K, V> element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#insert()
	}

	@SafeVarargs
	@Override
	public final int insertAll(final int index, final KeyValue<K, V>... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#insert()
	}

	@Override
	public final int insertAll(final int index, final KeyValue<K, V>[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#insertAll()
	}

	@Override
	public final int insertAll(final int index, final XGettingCollection<? extends KeyValue<K, V>> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#insertAll()
	}

	@Override
	public final boolean prepend(final KeyValue<K, V> element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#prepend()
	}

	@Override
	public final boolean preput(final KeyValue<K, V> element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#preput()
	}

	@Override
	public final boolean nullInput(final int index)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#nullInput()
	}

	@Override
	public final boolean nullInsert(final int index)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#nullInsert()
	}

	@Override
	public final boolean nullPrepend()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#nullPrepend()
	}

	@SafeVarargs
	@Override
	public final EqHashTable<K,V> prependAll(final KeyValue<K, V>... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#prepend()
	}

	@Override
	public final EqHashTable<K,V> prependAll(final KeyValue<K, V>[] elements, final int srcStartIndex, final int srcLength)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#prependAll()
	}

	@Override
	public final EqHashTable<K,V> prependAll(final XGettingCollection<? extends KeyValue<K, V>> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#prependAll()
	}

	@Override
	public final boolean nullPreput()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#nullPreput()
	}

	@SafeVarargs
	@Override
	public final EqHashTable<K,V> preputAll(final KeyValue<K, V>... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#preput()
	}

	@Override
	public final EqHashTable<K,V> preputAll(final KeyValue<K, V>[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#preputAll()
	}

	@Override
	public final EqHashTable<K,V> preputAll(final XGettingCollection<? extends KeyValue<K, V>> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#preputAll()
	}

	@Override
	public final boolean set(final int index, final KeyValue<K, V> element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#set()
	}

	@Override
	public final KeyValue<K, V> setGet(final int index, final KeyValue<K, V> element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#setGet()
	}

	@Override
	public final void setFirst(final KeyValue<K, V> element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#setFirst()
	}

	@Override
	public final void setLast(final KeyValue<K, V> element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#setLast()
	}

	@SafeVarargs
	@Override
	public final EqHashTable<K,V> setAll(final int index, final KeyValue<K, V>... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#set()
	}

	@Override
	public final EqHashTable<K,V> set(final int index, final KeyValue<K, V>[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#set()
	}

	@Override
	public final EqHashTable<K,V> set(final int index, final XGettingSequence<? extends KeyValue<K, V>> elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#set()
	}



	public static final <K, VK, VV> Function<K, EqHashTable<VK, VV>> supplier(final HashEqualator<VK> hashEqualator)
	{
		return new Function<K, EqHashTable<VK, VV>>()
		{
			@Override
			public final EqHashTable<VK, VV> apply(final K key)
			{
				return EqHashTable.New(hashEqualator);
			}

		};
	}

	public static final <K, VK, VV> Function<K, EqHashTable<VK, VV>> supplier()
	{
		return new Function<K, EqHashTable<VK, VV>>()
		{
			@Override
			public final EqHashTable<VK, VV> apply(final K key)
			{
				return EqHashTable.New();
			}

		};
	}




	public final class Keys implements XTable.Keys<K, V>, HashCollection<K>
	{
		///////////////////////////////////////////////////////////////////////////
		// override methods //
		/////////////////////

		@Override
		public final int hashDistributionRange()
		{
			return EqHashTable.this.slots.length;
		}

		@Override
		public final boolean hasVolatileHashElements()
		{
			return EqHashTable.this.chain.hasVolatileElements();
		}

		@Override
		public final void setHashDensity(final float hashDensity)
		{
			EqHashTable.this.setHashDensity(hashDensity);
		}

		@Override
		public final HashCollection.Analysis<Keys> analyze()
		{
			return AbstractChainEntryLinked.analyzeSlots(this, EqHashTable.this.slots);
		}



		///////////////////////////////////////////////////////////////////////////
		// getting methods  //
		/////////////////////

		@Override
		public final Equalator<? super K> equality()
		{
			return EqHashTable.this.hashEquality();
		}

		@Override
		public final Keys copy()
		{
			return EqHashTable.this.copy().keys();
		}

		/**
		 * This method creates a {@link EqConstHashEnum} instance containing all (currently existing) elements
		 * of this {@link ZVarSetKeys}.<br>
		 * No matter which hashing logic this instance uses, the new {@link EqConstHashEnum} instance always uses
		 * a STRONG EQUALATOR logic, using this instance's logic's {@link HashEqualator}.<br>
		 * This is necessary to ensure that the {@link EqConstHashEnum} instance is really constant and does not
		 * (can not!) lose elements over time.<br>
		 * If a {@link EqConstHashEnum} with volatile elements is needed (e.g. as a "read-only weak set"),
		 * an appropriate custom behaviour {@link EqConstHashEnum} instance can be created via the various
		 * copy constructors.
		 *
		 * @return a new {@link EqConstHashEnum} instance strongly referencing this set's current elements.
		 */
		@Override
		public final EqConstHashEnum<K> immure()
		{
			this.consolidate();
			return EqConstHashEnum.New( //const set may not contain volatile hash logic.
				EqHashTable.this.hashEquality(),
				EqHashTable.this.hashDensity,
				this
			);
		}

		@Override
		public final XGettingEnum<K> view()
		{
			return new EnumView<>(this);
		}

		@Override
		public final XEnum<K> range(final int lowIndex, final int highIndex)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#range()
		}

		@Override
		public final XGettingEnum<K> view(final int lowIndex, final int highIndex)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#view()
		}

		@Override
		public final K[] toArray(final Class<K> type)
		{
			return EqHashTable.this.chain.keyToArray(type);
		}

		// executing //

		@Override
		public final <P extends Procedure<? super K>> P iterate(final P procedure)
		{
			EqHashTable.this.chain.keyIterate(procedure);
			return procedure;
		}

		@Override
		public final <A> A join(final BiProcedure<? super K, ? super A> joiner, final A aggregate)
		{
			EqHashTable.this.chain.keyJoin(joiner, aggregate);
			return aggregate;
		}

		@Override
		public final int count(final K element)
		{
			return this.contains(element) ?1 :0;
		}

		@Override
		public final int countBy(final Predicate<? super K> predicate)
		{
			return EqHashTable.this.chain.keyCount(predicate);
		}

		// element querying //

		@Override
		public final K seek(final K sample)
		{
			return EqHashTable.this.chain.keySeek(sample, EqHashTable.this.hashEqualator);
		}

		@Override
		public final K search(final Predicate<? super K> predicate)
		{
			return EqHashTable.this.chain.keySearch(predicate);
		}

		@Override
		public final K max(final Comparator<? super K> comparator)
		{
			return EqHashTable.this.chain.keyMax(comparator);
		}

		@Override
		public final K min(final Comparator<? super K> comparator)
		{
			return EqHashTable.this.chain.keyMin(comparator);
		}

		// boolean querying //

		@Override
		public final boolean hasVolatileElements()
		{
			return EqHashTable.this.chain.hasVolatileElements();
		}

		@Override
		public final boolean nullAllowed()
		{
			return true;
		}

		/**
		 * As per definition of a set, this method always returns true.<br>
		 * Note that mutated elements whose hashcode has not been immuted by the employed hash logic
		 * can be contained multiple times, effectively breaking this method (because of breaking the hashing logic in the
		 * first place), so this information only has value if the elements' implementation is immutable or if the
		 * hash logic compensated their mutability (e.g. by using the identity hash code or by registering a once created
		 * hashcode, effectively "immuting" it).
		 *
		 * @return
		 * @see XGettingCollection#hasDistinctValues()
		 */
		@Override
		public final boolean hasDistinctValues()
		{
			return true;
		}

		@Override
		public final boolean hasDistinctValues(final Equalator<? super K> equalator)
		{
			if(EqHashTable.this.hashEqualator == equalator){
				return true;
			}
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#hasDistinctValues()
		}

		// boolean querying - applies //

		@Override
		public final boolean containsSearched(final Predicate<? super K> predicate)
		{
			return EqHashTable.this.chain.keyApplies(predicate);
		}

		@Override
		public final boolean applies(final Predicate<? super K> predicate)
		{
			return EqHashTable.this.chain.keyAppliesAll(predicate);
		}

		// boolean querying - contains //

		@Override
		public final boolean nullContained()
		{
			return this.contains((K)null);
		}

		@Override
		public final boolean containsId(final K element)
		{
			final int hash; // search for element by hash
			for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = EqHashTable.this.slots[(hash = EqHashTable.this.hashEqualator.hash(element)) & EqHashTable.this.range]; e != null; e = e.link){
				if(hash == e.hash && element == e.key()){
					return true;
				}
			}
			return false;
		}

		@Override
		public final boolean contains(final K element)
		{
			// search for element by hash
			final int hash;
			for(ChainMapEntryLinkedHashedStrongStrong<K,V> e = EqHashTable.this.slots[(hash = EqHashTable.this.hashEqualator.hash(element)) & EqHashTable.this.range]; e != null; e = e.link){
				if(e.hash == hash && EqHashTable.this.hashEqualator.equal(e.key(), element)){
					return true;
				}
			}
			return false;
		}

		@Override
		public final boolean containsAll(final XGettingCollection<? extends K> elements)
		{
			return elements.applies(EqHashTable.this.predicateContainsKey());
		}

		// boolean querying - equality //

		@Override
		public final boolean equals(final XGettingCollection<? extends K> samples, final Equalator<? super K> equalator)
		{
			if(samples == null || !(samples instanceof EqHashTable<?,?>.Keys)){
				return false;
			}
			if(samples == this){
				return true;
			}
			return this.equalsContent(samples, equalator);
		}

		@Override
		public final boolean equalsContent(final XGettingCollection<? extends K> samples, final Equalator<? super K> equalator)
		{
			this.consolidate();
			if(EqHashTable.this.size != Jadoth.to_int(samples.size())){
				return false;
			}

			// if sizes are equal and all elements of collection are contained in this set, they must have equal content
			return EqHashTable.this.chain.keyEqualsContent(samples, equalator);
		}

		// data set procedures //

		@Override
		public final <C extends Procedure<? super K>> C intersect(
			final XGettingCollection<? extends K> other,
			final Equalator<? super K> equalator,
			final C target
		)
		{
			return EqHashTable.this.chain.keyIntersect(other, equalator, target);
		}

		@Override
		public final <C extends Procedure<? super K>> C except(
			final XGettingCollection<? extends K> other,
			final Equalator<? super K> equalator,
			final C target
		)
		{
			return EqHashTable.this.chain.keyExcept(other, equalator, target);
		}

		@Override
		public final <C extends Procedure<? super K>> C union(
			final XGettingCollection<? extends K> other,
			final Equalator<? super K> equalator,
			final C target
		)
		{
			return EqHashTable.this.chain.keyUnion(other, equalator, target);
		}

		@Override
		public final <C extends Procedure<? super K>> C copyTo(final C target)
		{
			if(target == this){
				return target; // copying a set logic collection to itself would be a no-op, so spare the effort
			}
			return EqHashTable.this.chain.keyCopyTo(target);
		}

		@Override
		public final <C extends Procedure<? super K>> C filterTo(final C target, final Predicate<? super K> predicate)
		{
			return EqHashTable.this.chain.keyCopyTo(target, predicate);
		}

		@Override
		public final <T> T[] copyTo(final T[] target, final int offset)
		{
			EqHashTable.this.chain.keyCopyToArray(0, EqHashTable.this.size, target, offset);
			return target;
		}

		@Override
		public final <C extends Procedure<? super K>> C distinct(final C target)
		{
			return this.distinct(target, EqHashTable.this.hashEqualator);
		}

		@Override
		public final <C extends Procedure<? super K>> C distinct(final C target, final Equalator<? super K> equalator)
		{
			if(EqHashTable.this.hashEqualator == equalator){
				return this.copyTo(target);
			}
			return EqHashTable.this.chain.keyDistinct(target, equalator);
		}



		///////////////////////////////////////////////////////////////////////////
		//  adding methods  //
		/////////////////////

		@Override
		public final long currentCapacity()
		{
			return EqHashTable.this.currentCapacity();
		}

		@Override
		public final long maximumCapacity()
		{
			return EqHashTable.this.maximumCapacity();
		}

		@Override
		public final boolean isFull()
		{
			return EqHashTable.this.isFull();
		}

		@Override
		public final long remainingCapacity()
		{
			return EqHashTable.this.remainingCapacity();
		}

		@Override
		public final int optimize()
		{
			return EqHashTable.this.optimize();
		}

		@Override
		public final int rehash()
		{
			return EqHashTable.this.rehash();
		}

		@Override
		public final Keys ensureFreeCapacity(final long requiredFreeCapacity)
		{
			EqHashTable.this.ensureFreeCapacity(requiredFreeCapacity);
			return this;
		}

		@Override
		public final Keys ensureCapacity(final long minimalCapacity)
		{
			EqHashTable.this.ensureCapacity(minimalCapacity);
			return this;
		}

		@Override
		public final boolean nullAdd()
		{
			return EqHashTable.this.nullKeyAdd();
		}

		@Override
		public final boolean add(final K element)
		{
			return EqHashTable.this.internalAddOnlyKey(element);
		}

		@SafeVarargs
		@Override
		public final Keys addAll(final K... elements)
		{
			final EqHashTable<K,V> parent = EqHashTable.this;
			for(int i = 0, len = elements.length; i < len; i++) {
				parent.internalAddOnlyKey(elements[i]);
			}
			return this;
		}

		@Override
		public final Keys addAll(final K[] elements, final int srcIndex, final int srcLength)
		{
			final int d;
			if((d = JadothArrays.validateArrayRange(elements, srcIndex, srcLength)) == 0) return this;
			final int bound = srcIndex + srcLength;
			final EqHashTable<K,V> parent = EqHashTable.this;
			for(int i = srcIndex; i != bound; i+=d) {
				parent.internalAddOnlyKey(elements[i]);
			}
			return this;
		}

		@Override
		public final Keys addAll(final XGettingCollection<? extends K> elements)
		{
			final EqHashTable<K,V> parent = EqHashTable.this;
			elements.iterate(new Procedure<K>(){ @Override public void accept(final K e){
				parent.internalAddOnlyKey(e);
			}});
			return this;
		}

		@Override
		public final boolean nullPut()
		{
			return EqHashTable.this.nullKeyPut();
		}

		@Override
		public final void accept(final K element)
		{
			EqHashTable.this.internalPutOnlyKey(element);
		}

		@Override
		public final boolean put(final K element)
		{
			return EqHashTable.this.internalPutOnlyKey(element);
		}

		@Override
		public final K putGet(final K element)
		{
			return EqHashTable.this.internalPutGetKey(element);
		}

		@Override
		public final K addGet(final K element)
		{
			return EqHashTable.this.internalAddGetKey(element);
		}

		@Override
		public final K substitute(final K element)
		{
			return EqHashTable.this.internalSubsituteKey(element);
		}

		@SafeVarargs
		@Override
		public final Keys putAll(final K... elements)
		{
			final EqHashTable<K,V> parent = EqHashTable.this;
			for(int i = 0, len = elements.length; i < len; i++) {
				parent.internalPutOnlyKey(elements[i]);
			}
			return this;
		}

		@Override
		public final Keys putAll(final K[] elements, final int srcIndex, final int srcLength)
		{
			final int d;
			if((d = JadothArrays.validateArrayRange(elements, srcIndex, srcLength)) == 0) return this;
			final int bound = srcIndex + srcLength;
			final EqHashTable<K,V> parent = EqHashTable.this;
			for(int i = srcIndex; i != bound; i+=d) {
				parent.internalPutOnlyKey(elements[i]);
			}
			return this;
		}

		@Override
		public final Keys putAll(final XGettingCollection<? extends K> elements)
		{
			return elements.copyTo(this);
		}



		///////////////////////////////////////////////////////////////////////////
		//  remove methods  //
		/////////////////////

		/**
		 * Allocates a new internal storage with default size. No cutting of entry references is performed.
		 * <p>
		 * This can be substantially faster than {@link #clear()} as long as enough heap size is available but will also
		 * fragment heap much faster and thus slow down garbage collection compared to {@link #clear()}.
		 * <p>
		 * To clear the set in a heap-clean way and reduce internal storage size to default, use both {@link #clear()}
		 * and {@link #truncate()}.
		 *
		 * @see XProcessingCollection#truncate()
		 */
		@Override
		public final void truncate()
		{
			EqHashTable.this.truncate();
		}

		@Override
		public final int consolidate()
		{
			return EqHashTable.this.consolidate();
		}

		// removing //

		@Override
		public final int remove(final K element)
		{
			return EqHashTable.this.removeKey(element);
		}

		@Override
		public final int nullRemove()
		{
			return EqHashTable.this.removeKey(null);
		}

		// reducing //

		@Override
		public final int removeBy(final Predicate<? super K> predicate)
		{
			return EqHashTable.this.chain.keyReduce(predicate);
		}

		// retaining //

		@Override
		public final int retainAll(final XGettingCollection<? extends K> elements)
		{
			return EqHashTable.this.chain.keyRetainAll(elements);
		}

		@Override
		public final <P extends Procedure<? super K>> P process(final P procedure)
		{
			EqHashTable.this.chain.keyProcess(procedure);
			return procedure;
		}

		@Override
		public final <C extends Procedure<? super K>> C moveTo(final C target, final Predicate<? super K> predicate)
		{
			EqHashTable.this.chain.keyMoveTo(target, predicate);
			return target;
		}

		// removing - all //

		@Override
		public final int removeAll(final XGettingCollection<? extends K> elements)
		{
			final int oldSize = EqHashTable.this.size;
			elements.iterate(EqHashTable.this.procedureRemoveKey());
			return oldSize - EqHashTable.this.size;
		}

		// removing - duplicates //

		@Override
		public final int removeDuplicates()
		{
			return 0;
		}

		@Override
		public final int removeDuplicates(final Equalator<? super K> equalator)
		{
			if(EqHashTable.this.hashEqualator == equalator){
				return 0; // set is guaranteed to contain unique values according to its inherent equalator
			}

			// singleton null can be ignored here
			return EqHashTable.this.chain.keyRemoveDuplicates(equalator);
		}

		@Override
		public final Keys toReversed()
		{
			final EqHashTable<K,V> reversedVarSet = EqHashTable.this.copy();
			reversedVarSet.chain.reverse();
			return reversedVarSet.keys;
		}

		@Override
		public final <T extends Procedure<? super K>> T copySelection(final T target, final int... indices)
		{
			EqHashTable.this.chain.keyCopySelection(target, indices);
			return target;
		}

		@Override
		public final <T> T[] copyTo(final T[] target, final int targetOffset, final int offset, final int length)
		{
			EqHashTable.this.chain.copyToArray(offset, length, target, targetOffset);
			return target;
		}

		@Override
		public final <P extends IndexProcedure<? super K>> P iterate(final P procedure)
		{
			EqHashTable.this.chain.keyIterate(procedure);
			return procedure;
		}

		@Override
		public final K at(final int index)
		{
			return EqHashTable.this.chain.keyGet(index);
		}

		@Override
		public final K get()
		{
			return EqHashTable.this.chain.keyFirst();
		}

		@Override
		public final K first()
		{
			return EqHashTable.this.chain.keyFirst();
		}

		@Override
		public final K last()
		{
			return EqHashTable.this.chain.keyLast();
		}

		@Override
		public final K poll()
		{
			return EqHashTable.this.size == 0 ?null :EqHashTable.this.chain.keyFirst();
		}

		@Override
		public final K peek()
		{
			return EqHashTable.this.size == 0 ?null :EqHashTable.this.chain.keyLast();
		}

		@Override
		public final int indexOf(final K element)
		{
			return EqHashTable.this.chain.keyIndexOf(element);
		}

		@Override
		public final int indexBy(final Predicate<? super K> predicate)
		{
			return EqHashTable.this.chain.keyIndexOf(predicate);
		}

		@Override
		public final boolean isSorted(final Comparator<? super K> comparator)
		{
			return EqHashTable.this.chain.keyIsSorted(comparator);
		}

		@Override
		public final int lastIndexOf(final K element)
		{
			return EqHashTable.this.chain.keyRngIndexOf(EqHashTable.this.size - 1, -EqHashTable.this.size, element);
		}

		@Override
		public final int lastIndexBy(final Predicate<? super K> predicate)
		{
			return EqHashTable.this.chain.keyRngIndexOf(EqHashTable.this.size - 1, -EqHashTable.this.size, predicate);
		}

		@Override
		public final int maxIndex(final Comparator<? super K> comparator)
		{
			return EqHashTable.this.chain.keyMaxIndex(comparator);
		}

		@Override
		public final int minIndex(final Comparator<? super K> comparator)
		{
			return EqHashTable.this.chain.keyMinIndex(comparator);
		}

		@Override
		public final int scan(final Predicate<? super K> predicate)
		{
			return EqHashTable.this.chain.keyScan(predicate);
		}

		@Override
		public final <C extends Procedure<? super K>> C moveSelection(final C target, final int... indices)
		{
			EqHashTable.this.chain.keyMoveSelection(target, indices);
			return target;
		}

		@Override
		public final K removeAt(final int index)
		{
			return EqHashTable.this.chain.keyRemove(index);
		}

		@Override
		public final K fetch()
		{
			return EqHashTable.this.chain.keyRemove(0);
		}

		@Override
		public final K pop()
		{
			return EqHashTable.this.chain.keyRemove(EqHashTable.this.size - 1);
		}

		@Override
		public final K pinch()
		{
			return EqHashTable.this.size == 0 ?null :EqHashTable.this.chain.keyRemove(0);
		}

		@Override
		public final K pick()
		{
			return EqHashTable.this.size == 0 ?null :EqHashTable.this.chain.keyRemove(EqHashTable.this.size - 1);
		}

		@Override
		public final K retrieve(final K element)
		{
			return EqHashTable.this.chain.keyRetrieve(element);
		}

		@Override
		public final K retrieveBy(final Predicate<? super K> predicate)
		{
			return EqHashTable.this.chain.keyRetrieve(predicate);
		}

		@Override
		public final boolean removeOne(final K element)
		{
			return EqHashTable.this.chain.keyRemoveOne(element);
		}

		@Override
		public final Keys removeRange(final int startIndex, final int length)
		{
			EqHashTable.this.chain.removeRange(startIndex, length);
			return this;
		}

		@Override
		public final Keys retainRange(final int startIndex, final int length)
		{
			EqHashTable.this.chain.retainRange(startIndex, length);
			return this;
		}

		@Override
		public final int removeSelection(final int[] indices)
		{
			return EqHashTable.this.chain.removeSelection(indices);
		}

		@Override
		public final boolean isEmpty()
		{
			return EqHashTable.this.isEmpty();
		}

		@Override
		public final Iterator<K> iterator()
		{
			return EqHashTable.this.chain.keyIterator();
		}

		@Override
		public final long size()
		{
			return EqHashTable.this.size;
		}

		@Override
		public final String toString()
		{
			if(EqHashTable.this.size == 0){
				return "[]"; // array causes problems with escape condition otherwise
			}

			final VarString vc = VarString.New(EqHashTable.this.slots.length).append('[');
			EqHashTable.this.chain.keyAppendTo(vc, ',').append(']');
			return vc.toString();
		}

		@Override
		public final Object[] toArray()
		{
			return EqHashTable.this.chain.keyToArray();
		}

		/**
		 * Cuts all references to existing entries, effectively clearing the set.
		 * <p>
		 * The internal storage remains at its current size. All inter-entry references are cut as well, easing garbage
		 * collection of discarded entry instances belonging to different generations.
		 * <p>
		 * To simply reallocate a new internal storage with default size, see {@link #truncate()}
		 *
		 * @see XProcessingCollection#clear()
		 */
		@Override
		public final void clear()
		{
			EqHashTable.this.clear();
		}

		@Override
		public final Keys reverse()
		{
			EqHashTable.this.chain.reverse();
			return this;
		}

		@Override
		public final Keys sort(final Comparator<? super K> comparator)
		{
			EqHashTable.this.chain.keySort(comparator);
			return this;
		}

		@Override
		public final Keys shiftTo(final int sourceIndex, final int targetIndex)
		{
			EqHashTable.this.chain.shiftTo(sourceIndex, targetIndex);
			return this;
		}

		@Override
		public final Keys shiftTo(final int sourceIndex, final int targetIndex, final int length)
		{
			EqHashTable.this.chain.shiftTo(sourceIndex, targetIndex, length);
			return this;
		}

		@Override
		public final Keys shiftBy(final int sourceIndex, final int distance)
		{
			EqHashTable.this.chain.shiftTo(sourceIndex, distance);
			return this;
		}

		@Override
		public final Keys shiftBy(final int sourceIndex, final int distance, final int length)
		{
			EqHashTable.this.chain.shiftTo(sourceIndex, distance, length);
			return this;
		}

		@Override
		public final Keys swap(final int indexA, final int indexB)
		{
			EqHashTable.this.chain.swap(indexA, indexB);
			return this;
		}

		@Override
		public final Keys swap(final int indexA, final int indexB, final int length)
		{
			EqHashTable.this.chain.swap(indexA, indexB, length);
			return this;
		}

		@Override
		public final OldKeys old()
		{
			return new OldKeys();
		}

		@Override
		public final EqHashTable<K,V> parent()
		{
			return EqHashTable.this;
		}

		@Override
		public final HashEqualator<? super K> hashEquality()
		{
			return EqHashTable.this.hashEquality();
		}

		@Override
		public final float hashDensity()
		{
			return EqHashTable.this.hashDensity();
		}

		public final class OldKeys extends BridgeXSet<K>
		{
			protected OldKeys()
			{
				super(Keys.this);
			}

			@Override
			public final Keys parent()
			{
				return (Keys)super.parent();
			}

		}

		@Override
		public final boolean input(final int index, final K element)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#input()
		}

		@SafeVarargs
		@Override
		public final int inputAll(final int index, final K... elements)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#input()
		}

		@Override
		public final int inputAll(final int index, final K[] elements, final int offset, final int length)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#inputAll()
		}

		@Override
		public final int inputAll(final int index, final XGettingCollection<? extends K> elements)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#inputAll()
		}

		@Override
		public final boolean insert(final int index, final K element)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#insert()
		}

		@SafeVarargs
		@Override
		public final int insertAll(final int index, final K... elements)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#insert()
		}

		@Override
		public final int insertAll(final int index, final K[] elements, final int offset, final int length)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#insertAll()
		}

		@Override
		public final int insertAll(final int index, final XGettingCollection<? extends K> elements)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#insertAll()
		}

		@Override
		public final boolean prepend(final K element)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#prepend()
		}

		@Override
		public final boolean preput(final K element)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#preput()
		}

		@Override
		public final boolean nullInput(final int index)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#nullInput()
		}

		@Override
		public final boolean nullInsert(final int index)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#nullInsert()
		}

		@Override
		public final boolean nullPrepend()
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#nullPrepend()
		}

		@Override
		public final Keys prependAll(@SuppressWarnings("unchecked") final K... elements)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#prepend()
		}

		@Override
		public final Keys prependAll(final K[] elements, final int srcStartIndex, final int srcLength)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#prependAll()
		}

		@Override
		public final Keys prependAll(final XGettingCollection<? extends K> elements)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#prependAll()
		}

		@Override
		public final boolean nullPreput()
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#nullPreput()
		}

		@SafeVarargs
		@Override
		public final Keys preputAll(final K... elements)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#preput()
		}

		@Override
		public final Keys preputAll(final K[] elements, final int offset, final int length)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#preputAll()
		}

		@Override
		public final Keys preputAll(final XGettingCollection<? extends K> elements)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#preputAll()
		}

		@Override
		public final boolean set(final int index, final K element)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#set()
		}

		@Override
		public final K setGet(final int index, final K element)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#setGet()
		}

		@Override
		public final void setFirst(final K element)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#setFirst()
		}

		@Override
		public final void setLast(final K element)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#setLast()
		}

		@SafeVarargs
		@Override
		public final Keys setAll(final int index, final K... elements)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#set()
		}

		@Override
		public final Keys set(final int index, final K[] elements, final int offset, final int length)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#set()
		}

		@Override
		public final Keys set(final int index, final XGettingSequence<? extends K> elements, final int offset, final int length)
		{
			throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Keys#set()
		}

	}



	public final class Values implements XTable.Values<K, V>
	{
		@Override
		public final Equalator<? super V> equality()
		{
			return JadothEqualators.identity();
		}

		@Override
		public final XList<V> copy()
		{
			return new BulkList<V>(Jadoth.to_int(EqHashTable.this.size())).addAll(this);
		}

		@Override
		public final <P extends Procedure<? super V>> P iterate(final P procedure)
		{
			EqHashTable.this.chain.valuesIterate(procedure);
			return procedure;
		}

		@Override
		public final <A> A join(final BiProcedure<? super V, ? super A> joiner, final A aggregate)
		{
			EqHashTable.this.chain.valuesJoin(joiner, aggregate);
			return aggregate;
		}

		@Override
		public final <P extends IndexProcedure<? super V>> P iterate(final P procedure)
		{
			EqHashTable.this.chain.valuesIterate(procedure);
			return procedure;
		}

		@Override
		public final Values toReversed()
		{
			final EqHashTable<K,V> reversedVarSet = EqHashTable.this.copy();
			reversedVarSet.chain.reverse();
			return reversedVarSet.values;
		}

		@Override
		public final boolean containsSearched(final Predicate<? super V> predicate)
		{
			return EqHashTable.this.chain.valuesApplies(predicate);
		}

		@Override
		public final boolean applies(final Predicate<? super V> predicate)
		{
			return EqHashTable.this.chain.valuesAppliesAll(predicate);
		}

		@Override
		public final boolean contains(final V value)
		{
			return EqHashTable.this.chain.valuesContains(value);
		}

		@Override
		public final boolean containsAll(final XGettingCollection<? extends V> values)
		{
			return EqHashTable.this.chain.valuesContainsAll(values);
		}

		@Override
		public final boolean containsId(final V value)
		{
			return EqHashTable.this.chain.valuesContainsId(value);
		}

		@Override
		public final <T extends Procedure<? super V>> T copyTo(final T target)
		{
			EqHashTable.this.chain.valuesCopyTo(target);
			return target;
		}

		@Override
		public final <T extends Procedure<? super V>> T filterTo(final T target, final Predicate<? super V> predicate)
		{
			EqHashTable.this.chain.valuesCopyTo(target, predicate);
			return target;
		}

		@Override
		public final <T> T[] copyTo(final T[] target, final int targetOffset)
		{
			EqHashTable.this.chain.valuesCopyToArray(0, Jadoth.to_int(this.size()), target, targetOffset);
			return target;
		}

		@Override
		public final int count(final V value)
		{
			return EqHashTable.this.chain.valuesCount(value);
		}

		@Override
		public final int countBy(final Predicate<? super V> predicate)
		{
			return EqHashTable.this.chain.valuesCount(predicate);
		}

		@Override
		public final <T extends Procedure<? super V>> T distinct(final T target)
		{
			EqHashTable.this.chain.valuesDistinct(target);
			return target;
		}

		@Override
		public final <T extends Procedure<? super V>> T distinct(final T target, final Equalator<? super V> equalator)
		{
			EqHashTable.this.chain.valuesDistinct(target, equalator);
			return target;
		}

		@Override
		public final boolean equals(final XGettingCollection<? extends V> samples, final Equalator<? super V> equalator)
		{
			if(samples == null || !(samples instanceof EqHashEnum<?>)) return false;
			if(samples == this) return true;
			return this.equalsContent(samples, equalator);
		}

		@Override
		public final boolean equalsContent(final XGettingCollection<? extends V> samples, final Equalator<? super V> equalator)
		{
			return EqHashTable.this.chain.valuesEqualsContent(samples, equalator);
		}

		@Override
		public final <T extends Procedure<? super V>> T except(final XGettingCollection<? extends V> other, final Equalator<? super V> equalator, final T target)
		{
			EqHashTable.this.chain.valuesExcept(other, equalator, target);
			return target;
		}

		@Override
		public final boolean hasDistinctValues()
		{
			return EqHashTable.this.chain.valuesHasDistinctValues();
		}

		@Override
		public final boolean hasDistinctValues(final Equalator<? super V> equalator)
		{
			return EqHashTable.this.chain.valuesHasDistinctValues(equalator);
		}

		@Override
		public final boolean hasVolatileElements()
		{
			return EqHashTable.this.hasVolatileValues();
		}

		@Override
		public final <T extends Procedure<? super V>> T intersect(final XGettingCollection<? extends V> other, final Equalator<? super V> equalator, final T target)
		{
			EqHashTable.this.chain.valuesIntersect(other, equalator, target);
			return target;
		}

		@Override
		public final boolean isEmpty()
		{
			return EqHashTable.this.isEmpty();
		}

		@Override
		public final Iterator<V> iterator()
		{
			return EqHashTable.this.chain.valuesIterator();
		}

		@Override
		public final V max(final Comparator<? super V> comparator)
		{
			return EqHashTable.this.chain.valuesMax(comparator);
		}

		@Override
		public final V min(final Comparator<? super V> comparator)
		{
			return EqHashTable.this.chain.valuesMin(comparator);
		}

		@Override
		public final boolean nullAllowed()
		{
			return EqHashTable.this.nullAllowed();
		}

		@Override
		public final boolean nullContained()
		{
			return EqHashTable.this.chain.valuesContains(null);
		}

		@Override
		public final OldValues old()
		{
			return new OldValues();
		}

		@Override
		public final V seek(final V sample)
		{
			return EqHashTable.this.chain.valuesGet(sample);
		}

		@Override
		public final V search(final Predicate<? super V> predicate)
		{
			return EqHashTable.this.chain.valuesSearch(predicate);
		}

		@Override
		public final long size()
		{
			return Jadoth.to_int(EqHashTable.this.size());
		}

		@Override
		public final long maximumCapacity()
		{
			return Jadoth.to_int(EqHashTable.this.size());
		}

		@Override
		public final boolean isFull()
		{
			return EqHashTable.this.isFull();
		}

		@Override
		public final long remainingCapacity()
		{
			return EqHashTable.this.remainingCapacity();
		}

		@Override
		public final String toString()
		{
			if(EqHashTable.this.size == 0){
				return "[]"; // array causes problems with escape condition otherwise
			}

			final VarString vc = VarString.New(EqHashTable.this.slots.length).append('[');
			EqHashTable.this.chain.valuesAppendTo(vc, ',').append(']');
			return vc.toString();
		}

		@Override
		public final Object[] toArray()
		{
			return EqHashTable.this.chain.valuesToArray();
		}

		@Override
		public final V[] toArray(final Class<V> type)
		{
			return EqHashTable.this.chain.valuesToArray(type);
		}

		@Override
		public final <T extends Procedure<? super V>> T union(
			final XGettingCollection<? extends V> other,
			final Equalator<? super V> equalator,
			final T target
		)
		{
			EqHashTable.this.chain.valuesUnion(other, equalator, target);
			return target;
		}

		@Override
		public final EqHashTable<K,V> parent()
		{
			return EqHashTable.this;
		}

		@Override
		public final SubListView<V> view(final int fromIndex, final int toIndex)
		{
			return new SubListView<>(this, fromIndex, toIndex);
		}

		@Override
		public final ListIterator<V> listIterator()
		{
			return EqHashTable.this.chain.valuesListIterator(0);
		}

		@Override
		public final ListIterator<V> listIterator(final int index)
		{
			return EqHashTable.this.chain.valuesListIterator(index);
		}

		@Override
		public final SubListProcessor<V> range(final int fromIndex, final int toIndex)
		{
			return new SubListProcessor<>(this, fromIndex, toIndex);
		}

		@Override
		public final XImmutableList<V> immure()
		{
			return new ConstList<>(this);
		}

		@Override
		public final ListView<V> view()
		{
			return new ListView<>(this);
		}

		@Override
		public final <T extends Procedure<? super V>> T copySelection(final T target, final int... indices)
		{
			EqHashTable.this.chain.valuesCopySelection(target, indices);
			return target;
		}

		@Override
		public final <T> T[] copyTo(final T[] target, final int targetOffset, final int offset, final int length)
		{
			EqHashTable.this.chain.valuesCopyToArray(offset, length, target, targetOffset);
			return target;
		}

		@Override
		public final V at(final int index)
		{
			return EqHashTable.this.chain.valuesGet(index);
		}

		@Override
		public final V get()
		{
			return EqHashTable.this.chain.valuesFirst();
		}

		@Override
		public final V first()
		{
			return EqHashTable.this.chain.valuesFirst();
		}

		@Override
		public final V last()
		{
			return EqHashTable.this.chain.valuesLast();
		}

		@Override
		public final V poll()
		{
			return EqHashTable.this.size == 0 ?null :EqHashTable.this.chain.valuesFirst();
		}

		@Override
		public final V peek()
		{
			return EqHashTable.this.size == 0 ?null :EqHashTable.this.chain.valuesLast();
		}

		@Override
		public final int indexOf(final V value)
		{
			return EqHashTable.this.chain.valuesIndexOf(value);
		}

		@Override
		public final int indexBy(final Predicate<? super V> predicate)
		{
			return EqHashTable.this.chain.valuesIndexOf(predicate);
		}

		@Override
		public final boolean isSorted(final Comparator<? super V> comparator)
		{
			return EqHashTable.this.chain.valuesIsSorted(comparator);
		}

		@Override
		public final int lastIndexOf(final V value)
		{
			return EqHashTable.this.chain.valuesRngIndexOf(EqHashTable.this.size - 1, EqHashTable.this.size, value);
		}

		@Override
		public final int lastIndexBy(final Predicate<? super V> predicate)
		{
			return EqHashTable.this.chain.valuesRngIndexOf(EqHashTable.this.size - 1, EqHashTable.this.size, predicate);
		}

		@Override
		public final int maxIndex(final Comparator<? super V> comparator)
		{
			return EqHashTable.this.chain.valuesMaxIndex(comparator);
		}

		@Override
		public final int minIndex(final Comparator<? super V> comparator)
		{
			return EqHashTable.this.chain.valuesMinIndex(comparator);
		}

		@Override
		public final int scan(final Predicate<? super V> predicate)
		{
			return EqHashTable.this.chain.valuesScan(predicate);
		}

		@Override
		public final <C extends Procedure<? super V>> C moveSelection(final C target, final int... indices)
		{
			EqHashTable.this.chain.valuesMoveSelection(target, indices);
			return target;
		}

		@Override
		public final V removeAt(final int index)
		{
			return EqHashTable.this.chain.valuesRemove(index);
		}

		@Override
		public final V fetch()
		{
			return EqHashTable.this.chain.valuesRemove(0);
		}

		@Override
		public final V pop()
		{
			return EqHashTable.this.chain.valuesRemove(EqHashTable.this.size - 1);
		}

		@Override
		public final V pinch()
		{
			return EqHashTable.this.size == 0 ?null :EqHashTable.this.chain.valuesRemove(0);
		}

		@Override
		public final V pick()
		{
			return EqHashTable.this.size == 0 ?null :EqHashTable.this.chain.valuesRemove(EqHashTable.this.size - 1);
		}

		@Override
		public final V retrieve(final V value)
		{
			return EqHashTable.this.chain.valuesRetrieve(value);
		}

		@Override
		public final V retrieveBy(final Predicate<? super V> predicate)
		{
			return EqHashTable.this.chain.valuesRetrieve(predicate);
		}

		@Override
		public final boolean removeOne(final V element)
		{
			return EqHashTable.this.chain.valuesRemoveOne(element);
		}

		@Override
		public final Values removeRange(final int startIndex, final int length)
		{
			EqHashTable.this.chain.removeRange(startIndex, length);
			return this;
		}

		@Override
		public final Values retainRange(final int startIndex, final int length)
		{
			EqHashTable.this.chain.retainRange(startIndex, length);
			return this;
		}

		@Override
		public final int removeSelection(final int[] indices)
		{
			return EqHashTable.this.chain.removeSelection(indices);
		}

		@Override
		public final void clear()
		{
			EqHashTable.this.clear();
		}

		@Override
		public final int consolidate()
		{
			return EqHashTable.this.consolidate();
		}

		@Override
		public final <C extends Procedure<? super V>> C moveTo(final C target, final Predicate<? super V> predicate)
		{
			EqHashTable.this.chain.valuesMoveTo(target, predicate);
			return target;
		}

		@Override
		public final int nullRemove()
		{
			return EqHashTable.this.chain.valuesRemove(null);
		}

		@Override
		public final int optimize()
		{
			return EqHashTable.this.optimize();
		}

		@Override
		public final <P extends Procedure<? super V>> P process(final P procedure)
		{
			EqHashTable.this.chain.valuesProcess(procedure);
			return procedure;
		}

		@Override
		public final int removeBy(final Predicate<? super V> predicate)
		{
			return EqHashTable.this.chain.valuesReduce(predicate);
		}

		@Override
		public final int remove(final V value)
		{
			return EqHashTable.this.chain.valuesRemove(value);
		}

		@Override
		public final int removeAll(final XGettingCollection<? extends V> values)
		{
			return EqHashTable.this.chain.valuesRemoveAll(values);
		}

		@Override
		public final int removeDuplicates()
		{
			return EqHashTable.this.chain.valuesRemoveDuplicates();
		}

		@Override
		public final int removeDuplicates(final Equalator<? super V> equalator)
		{
			return EqHashTable.this.chain.valuesRemoveDuplicates(equalator);
		}

		@Override
		public final int retainAll(final XGettingCollection<? extends V> values)
		{
			return EqHashTable.this.chain.valuesRetainAll(values);
		}

		@Override
		public final void truncate()
		{
			EqHashTable.this.truncate();
		}

		@Override
		public final Values fill(final int offset, final int length, final V value)
		{
			EqHashTable.this.chain.valuesFill(offset, length, value);
			return this;
		}

		@Override
		public final int replace(final V value, final V replacement)
		{
			return EqHashTable.this.chain.valuesReplace(value, replacement);
		}

		@Override
		public final int replaceAll(final XGettingCollection<? extends V> values, final V replacement)
		{
			return EqHashTable.this.chain.valuesReplaceAll(values, replacement);
		}

		@Override
		public final int modify(final Function<V, V> mapper)
		{
			return EqHashTable.this.chain.valuesModify(mapper);
		}

		@Override
		public final int modify(final Predicate<? super V> predicate, final Function<V, V> mapper)
		{
			return EqHashTable.this.chain.valuesModify(predicate, mapper);
		}

		@Override
		public final boolean replaceOne(final V value, final V replacement)
		{
			return EqHashTable.this.chain.valuesReplaceOne(value, replacement);
		}

		@Override
		public final Values reverse()
		{
			EqHashTable.this.chain.reverse();
			return this;
		}

		@Override
		public final boolean set(final int index, final V value)
		{
			return EqHashTable.this.chain.valuesSet(index, value) == value;
		}

		@Override
		public final V setGet(final int index, final V value)
		{
			return EqHashTable.this.chain.valuesSet(index, value);
		}

		@Override
		public final Values setAll(final int offset, @SuppressWarnings("unchecked") final V... values)
		{
			EqHashTable.this.chain.valuesSet(offset, values);
			return this;
		}

		@Override
		public final Values set(final int offset, final V[] src, final int srcIndex, final int srcLength)
		{
			EqHashTable.this.chain.valuesSet(offset, src, srcIndex, srcLength);
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public final Values set(final int offset, final XGettingSequence<? extends V> values, final int valuesOffset, final int valuesLength)
		{
			// (22.05.2011)NOTE: redundant copying due to implemenation laziness, for now
			EqHashTable.this.chain.valuesSet(offset, (V[])values.toArray(), valuesOffset, valuesLength);
			return this;
		}

		@Override
		public final void setFirst(final V value)
		{
			EqHashTable.this.chain.valuesSet(0, value);
		}

		@Override
		public final void setLast(final V value)
		{
			EqHashTable.this.chain.valuesSet(EqHashTable.this.size - 1, value);
		}

		@Override
		public final Values sort(final Comparator<? super V> comparator)
		{
			EqHashTable.this.chain.valuesSort(comparator);
			return this;
		}

		@Override
		public final int replace(final Predicate<? super V> predicate, final V substitute)
		{
			return EqHashTable.this.chain.valuesSubstitute(predicate, substitute);
		}

		@Override
		public final boolean replaceOne(final Predicate<? super V> predicate, final V substitute)
		{
			return EqHashTable.this.chain.valuesSubstituteOne(predicate, substitute);
		}

		@Override
		public final Values shiftTo(final int sourceIndex, final int targetIndex)
		{
			EqHashTable.this.chain.shiftTo(sourceIndex, targetIndex);
			return this;
		}

		@Override
		public final Values shiftTo(final int sourceIndex, final int targetIndex, final int length)
		{
			EqHashTable.this.chain.shiftTo(sourceIndex, targetIndex, length);
			return this;
		}

		@Override
		public final Values shiftBy(final int sourceIndex, final int distance)
		{
			EqHashTable.this.chain.shiftTo(sourceIndex, distance);
			return this;
		}

		@Override
		public final Values shiftBy(final int sourceIndex, final int distance, final int length)
		{
			EqHashTable.this.chain.shiftTo(sourceIndex, distance, length);
			return this;
		}

		@Override
		public final Values swap(final int indexA, final int indexB)
		{
			EqHashTable.this.chain.swap(indexA, indexB);
			return this;
		}

		@Override
		public final Values swap(final int indexA, final int indexB, final int length)
		{
			EqHashTable.this.chain.swap(indexA, indexB, length);
			return this;
		}

		public final class OldValues extends OldSettingList<V>
		{
			protected OldValues()
			{
				super(Values.this);
			}

			@Override
			public final Values parent()
			{
				return (Values)super.parent();
			}

		}

	}



	public final class OldVarMap implements XTable.Bridge<K, V>
	{

		@Override
		public final void clear()
		{
			EqHashTable.this.clear();
		}

		@SuppressWarnings("unchecked")
		@Override
		public final boolean containsKey(final Object key)
		{
			try{
				return EqHashTable.this.containsKey((K)key);
			}
			catch(final Exception e){
				/* how to safely detect an exception caused by an invalid type of passed object?
				 * Can't be sure to always be a ClassCastException...
				 * God damn stupid dilettantish Object type in old Map -.-
				 * As if they really found "reasonable code that affords Object" back then, nonsense.
				 */
				return false;
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public final boolean containsValue(final Object value)
		{
			try{
				return EqHashTable.this.chain.valuesContains((V)value);
			}
			catch(final Exception e){
				/* how to safely detect an exception caused by an invalid type of passed object?
				 * Can't be sure to always be a ClassCastException...
				 * God damn stupid dilettantish Object type in old Map -.-
				 * As if they really found "reasonable code that affords Object" back then, nonsense.
				 */
				return false;
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public final Set<java.util.Map.Entry<K, V>> entrySet()
		{
			/* (20.05.2011 TM)NOTE:
			 * Okay this is nasty:
			 * Entry implements KeyValue and java.util.Map.Entry
			 * XCollection-architecture wise, the "old" collections cleanly use KeyValue instead of Entry.
			 * But java.util.Set<KeyValue<K,V>> cannot be cast to Set<java.util.Map.Entry<K, V>>, generics-wise.
			 * Nevertheless, the "stuff behind" the typing IS compatible.
			 * So this typingly dirty but architectural clean workaround is used.
			 */
			return (Set<java.util.Map.Entry<K, V>>)(Set<?>)EqHashTable.this.old();
		}

		@SuppressWarnings("unchecked")
		@Override
		public final V get(final Object key)
		{
			try{
				return EqHashTable.this.get((K)key);
			}
			catch(final Exception e){
				/* how to safely detect an exception caused by an invalid type of passed object?
				 * Can't be sure to always be a ClassCastException...
				 * God damn stupid dilettantish Object type in old Map -.-
				 * As if they really found "reasonable code that affords Object" back then, nonsense.
				 */
				return null;
			}
		}

		@Override
		public final boolean isEmpty()
		{
			return EqHashTable.this.isEmpty();
		}

		@Override
		public final Set<K> keySet()
		{
			return EqHashTable.this.keys().old();
		}

		@Override
		public final V put(final K key, final V value)
		{
			return EqHashTable.this.oldPutGet(key, value);
		}

		@SuppressWarnings("unchecked")
		@Override
		public final void putAll(final Map<? extends K, ? extends V> m)
		{
			if(m instanceof XGettingMap.Bridge<?,?>){
				EqHashTable.this.addAll(((XGettingMap.Bridge<K,V>)m).parent());
				return;
			}

			final EqHashTable<K,V> parent = EqHashTable.this;
			for(final Map.Entry<? extends K, ? extends V> entry : m.entrySet()){
				parent.put(entry.getKey(), entry.getValue());
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public final V remove(final Object key)
		{
			try{
				return EqHashTable.this.remove((K)key);
			}
			catch(final Exception e){
				/* how to safely detect an exception caused by an invalid type of passed object?
				 * Can't be sure to always be a ClassCastException...
				 * God damn stupid dilettantish Object type in old Map -.-
				 * As if they really found "reasonable code that affords Object" back then, nonsense.
				 */
				return null;
			}
		}

		@Override
		public final int size()
		{
			return Jadoth.to_int(EqHashTable.this.size());
		}

		@Override
		public final Collection<V> values()
		{
			return EqHashTable.this.values.old(); // hehehe
		}

		@Override
		public final EqHashTable<K, V> parent()
		{
			return EqHashTable.this;
		}

	}

}
