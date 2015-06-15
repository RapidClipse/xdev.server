package net.jadoth.collections;


import static net.jadoth.Jadoth.notNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Predicate;

import net.jadoth.Jadoth;
import net.jadoth.collections.interfaces.CapacityExtendable;
import net.jadoth.collections.interfaces.ChainStorage;
import net.jadoth.collections.old.BridgeXSet;
import net.jadoth.collections.types.HashCollection;
import net.jadoth.collections.types.XEnum;
import net.jadoth.collections.types.XGettingCollection;
import net.jadoth.collections.types.XGettingEnum;
import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.exceptions.ArrayCapacityException;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.IndexProcedure;
import net.jadoth.functional.Procedure;
import net.jadoth.hash.HashEqualator;
import net.jadoth.hash.JadothHash;
import net.jadoth.util.Composition;
import net.jadoth.util.Equalator;
import net.jadoth.util.chars.VarString;


public final class EqHashEnum<E> extends AbstractChainCollection<E, E, E, ChainEntryLinkedHashedStrong<E>>
implements XEnum<E>, HashCollection<E>, Composition
{
	///////////////////////////////////////////////////////////////////////////
	//  class methods   //
	/////////////////////

	public static final <E> EqHashEnum<E> New()
	{
		return new EqHashEnum<>(
			DEFAULT_HASH_LENGTH,
			DEFAULT_HASH_FACTOR,
			JadothHash.<E>hashEqualityValue()
		);
	}

	public static final <E> EqHashEnum<E> NewCustom(final int initialCapacity)
	{
		return new EqHashEnum<>(
			JadothHash.padHashLength(initialCapacity),
			DEFAULT_HASH_FACTOR,
			JadothHash.<E>hashEqualityValue()
		);
	}

	public static final <E> EqHashEnum<E> NewCustom(final int initialCapacity, final float hashDensity)
	{
		return new EqHashEnum<>(
			JadothHash.padHashLength(initialCapacity),
			JadothHash.hashDensity(hashDensity),
			JadothHash.<E>hashEqualityValue()
		);
	}

	public static final <E> EqHashEnum<E> New(final HashEqualator<? super E> hashEqualator)
	{
		return new EqHashEnum<>(DEFAULT_HASH_LENGTH, DEFAULT_HASH_FACTOR, hashEqualator);
	}

	public static final <E> EqHashEnum<E> NewCustom(
		final HashEqualator<? super E> hashEqualator  ,
		final int                      initialCapacity
	)
	{
		return new EqHashEnum<>(
			JadothHash.padHashLength(initialCapacity),
			DEFAULT_HASH_FACTOR,
			hashEqualator
		);
	}

	public static final <E> EqHashEnum<E> NewCustom(
		final HashEqualator<? super E> hashEqualator,
		final float                    hashDensity
	)
	{
		return new EqHashEnum<>(DEFAULT_HASH_LENGTH, hashDensity, hashEqualator);
	}

	public static final <E> EqHashEnum<E> NewCustom(
		final HashEqualator<? super E>hashEqualator  ,
		final int                     initialCapacity,
		final float                   hashDensity
	)
	{
		return new EqHashEnum<>(
			JadothHash.padHashLength(initialCapacity),
			JadothHash.hashDensity(hashDensity),
			hashEqualator
		);
	}

	@SafeVarargs
	public static final <E> EqHashEnum<E> New(final E... entries)
	{
		return NewCustom(JadothHash.<E>hashEqualityValue(), DEFAULT_HASH_FACTOR, entries);
	}
	
	public static final <E> EqHashEnum<E> New(final XGettingCollection<E> entries)
	{
		return EqHashEnum.<E>New().addAll(entries);
	}

	@SafeVarargs
	public static final <E> EqHashEnum<E> NewCustom(final float hashDensity, final E... entries)
	{
		return NewCustom(JadothHash.<E>hashEqualityValue(), hashDensity, entries);
	}

	@SafeVarargs
	public static final <E> EqHashEnum<E> New(final HashEqualator<? super E> hashEqualator, final E... entries)
	{
		return NewCustom(hashEqualator, DEFAULT_HASH_FACTOR, entries);
	}

	@SafeVarargs
	public static final <E> EqHashEnum<E> NewCustom(
		final HashEqualator<? super E> hashEqualator,
		final float                    hashDensity  ,
		final E...                     entries
	)
	{
		return new EqHashEnum<E>(
			JadothHash.padHashLength(entries.length), // might be too big if entries contains a lot of duplicates
			JadothHash.hashDensity(hashDensity),
			notNull(hashEqualator)
		).addAll(entries);
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	// data storage
	private final AbstractChainStorage<E, E, E, ChainEntryLinkedHashedStrong<E>> chain;
	              ChainEntryLinkedHashedStrong<E>[]                              slots;


	// hashing
	final HashEqualator<? super E> hashEqualator;
	float                          hashDensity  ;

	// cached values
	transient int capacity, range;

	int size = 0;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	private EqHashEnum(final EqHashEnum<E> original)
	{
		super();
		this.hashDensity   = original.hashDensity;
		this.hashEqualator = original.hashEqualator;
		this.range         = original.range;

		// constructor only copies configuration (concearn #1), not data (#2). See copy() for copying data.
		this.slots         = ChainEntryLinkedHashedStrong.array(original.slots.length);
		this.chain         = new ChainStorageStrong<>(this, new ChainEntryLinkedHashedStrong<E>(-1, null, null));
		this.capacity      = original.capacity;
	}

	private EqHashEnum(
		final int              pow2InitialHashLength,
		final float            positiveHashDensity  ,
		final HashEqualator<? super E> hashEqualator
	)
	{
		super();
		this.hashDensity   = positiveHashDensity;
		this.hashEqualator = hashEqualator;
		this.range         = pow2InitialHashLength - 1;

		this.slots         = ChainEntryLinkedHashedStrong.array(pow2InitialHashLength);
		this.chain         = new ChainStorageStrong<>(this, new ChainEntryLinkedHashedStrong<E>(-1, null, null));
		this.capacity      = (int)(pow2InitialHashLength * positiveHashDensity); // capped at MAX_VALUE
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	private ChainEntryLinkedHashedStrong<E> createNewEntry(final int hash, final E element)
	{
		if(this.size >= this.capacity){
			ensureFreeArrayCapacity(this.size); // size limit only needs to be checked if size reached capacity
			this.increaseStorage();
		}

		ChainEntryLinkedHashedStrong<E> e;
		this.slots[hash & this.range] = e = new ChainEntryLinkedHashedStrong<>(hash, element, this.slots[hash & this.range]);
		this.size++;
		return e;
	}

	private void increaseStorage()
	{
		this.rebuildStorage((int)(this.slots.length * 2.0f));
	}

	private void rebuildStorage(final int newSlotLength)
	{
		final ChainEntryLinkedHashedStrong<E>[] newSlots = ChainEntryLinkedHashedStrong.array(newSlotLength);
		final int modulo = newSlotLength >= Integer.MAX_VALUE ?Integer.MAX_VALUE :newSlotLength - 1;

		// iterate through all entries and assign them to the new storage
		for(ChainEntryLinkedHashedStrong<E> entry = this.chain.head(); (entry = entry.next) != null; ){
			entry.link = newSlots[entry.hash & modulo];
			newSlots[entry.hash & modulo] = entry;
		}

		this.capacity = newSlotLength >= Integer.MAX_VALUE ?Integer.MAX_VALUE :(int)(newSlotLength * this.hashDensity);
		this.slots = newSlots;
		this.range = modulo;
	}

	final void internalCollectUnhashed(final E element)
	{
		this.chain.appendEntry(new ChainEntryLinkedHashedStrong<>(0, element, null));
	}



	///////////////////////////////////////////////////////////////////////////
	// inheriteted ExtendedCollection methods  //
	////////////////////////////////////////////

	@Override
	public boolean nullAllowed()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME ExtendedCollection<E>#nullAllowed()
	}

	@Override
	protected ChainStorage<E, E, E, ChainEntryLinkedHashedStrong<E>> getInternalStorageChain()
	{
		return this.chain;
	}

	@Override
	protected int internalRemoveNullEntries()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME AbstractChainCollection<E>#internalRemoveNullEntries()
	}

	@Override
	protected int internalCountingAddAll(final E[] elements) throws UnsupportedOperationException
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME AbstractExtendedCollection<E>#internalCountingAddAll()
	}

	@Override
	protected int internalCountingAddAll(final E[] elements, final int offset, final int length) throws UnsupportedOperationException
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME AbstractExtendedCollection<E>#internalCountingAddAll()
	}

	@Override
	protected int internalCountingAddAll(final XGettingCollection<? extends E> elements) throws UnsupportedOperationException
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME AbstractExtendedCollection<E>#internalCountingAddAll()
	}

	@Override
	protected int internalCountingPutAll(final E[] elements) throws UnsupportedOperationException
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME AbstractExtendedCollection<E>#internalCountingPutAll()
	}

	@Override
	protected int internalCountingPutAll(final E[] elements, final int offset, final int length) throws UnsupportedOperationException
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME AbstractExtendedCollection<E>#internalCountingPutAll()
	}

	@Override
	protected int internalCountingPutAll(final XGettingCollection<? extends E> elements) throws UnsupportedOperationException
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME AbstractExtendedCollection<E>#internalCountingPutAll()
	}

	@Override
	protected void internalRemoveEntry(final ChainEntryLinkedHashedStrong<E> entry)
	{
		// set only creates SetEntries internally, so this cast is safe.
		final ChainEntryLinkedHashedStrong<E> setEntry = entry;
		ChainEntryLinkedHashedStrong<E> last, e = this.slots[setEntry.hash & this.range];

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
			if(e == null) throw new IllegalArgumentException("Set entry inconsistency detected");
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
	public final long size()
	{
		return this.size;
	}

	@Override
	public final boolean isEmpty()
	{
		return this.size == 0;
	}

	@Override
	public final void clear()
	{
		// break inter-entry references to ease GC
		this.chain.clear();

		// clear hash array
		final ChainEntryLinkedHashedStrong<E>[] slots = this.slots;
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
		this.slots = ChainEntryLinkedHashedStrong.array(DEFAULT_HASH_LENGTH);
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
		final int                               reqCapacity   = JadothHash.padHashLength((int)(this.size / this.hashDensity));
		final ChainEntryLinkedHashedStrong<E>[] slots         = ChainEntryLinkedHashedStrong.<E>array(reqCapacity);
		final int                               range         = reqCapacity >= Integer.MAX_VALUE ?Integer.MAX_VALUE :reqCapacity - 1;
		final HashEqualator<? super E>          hashEqualator = this.hashEqualator;
		final AbstractChainStorage<E, E, E, ChainEntryLinkedHashedStrong<E>> chain = this.chain;

		// keep the old chain head for old entries iteration and clear the chain for the new entries
		ChainEntryLinkedHashedStrong<E> entry = chain.head().next;
		chain.clear();

		int size = 0;
		oldEntries:
		for(/*entry must be outside, see comment*/; entry != null; entry = entry.next)
		{
			final int hash = hashEqualator.hash(entry.element);

			// check for rehash collisions
			for(ChainEntryLinkedHashedStrong<E> e = slots[hash & range]; e != null; e = e.link){
				if(e.hash == hash && hashEqualator.equal(e.element, entry.element)){
					continue oldEntries; // hash collision: value already contained, discard old element
				}
			}

			// register new entry for unique element
			chain.appendEntry(slots[hash & range] =
				new ChainEntryLinkedHashedStrong<>(hash, entry.element, slots[hash & range]))
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
	public final EqHashEnum<E> copy()
	{
		final EqHashEnum<E> newVarMap = new EqHashEnum<>(this);
		this.chain.iterate(new Procedure<E>(){
			@Override public void accept(final E entry){
				newVarMap.put(entry);
			}
		});
		return newVarMap;
	}

	@Override
	public final EqConstHashEnum<E> immure()
	{
		this.consolidate();
		return EqConstHashEnum.New(this.hashEqualator, this.hashDensity, this);
	}

	@Override
	public final XGettingEnum<E> view()
	{
		return new EnumView<>(this);
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
	public final OldVarSet<E> old()
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIXME EqHashTable#old()
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
	public final E putGet(final E element)
	{
		final int hash;
		for(ChainEntryLinkedHashedStrong<E> e = this.slots[(hash = this.hashEqualator.hash(element)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.element, element)){
				return e.setElement(element);
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, element));
		return null;
	}

	@Override
	public final E addGet(final E element)
	{
		final int hash;
		for(ChainEntryLinkedHashedStrong<E> e = this.slots[(hash = this.hashEqualator.hash(element)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.element, element)){
				return e.element;
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, element));
		return null;
	}

	@Override
	public final E substitute(final E element)
	{
		final int hash;
		for(ChainEntryLinkedHashedStrong<E> e = this.slots[(hash = this.hashEqualator.hash(element)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.element, element)){
				return e.element;
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, element));
		return element;
	}

	@Override
	public final boolean add(final E element)
	{
		final int hash;
		for(ChainEntryLinkedHashedStrong<E> e = this.slots[(hash = this.hashEqualator.hash(element)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.element, element)){
				return false; // already contained
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, element));
		return true;
	}

	@Override
	public final boolean put(final E element)
	{
		final int hash;
		for(ChainEntryLinkedHashedStrong<E> e = this.slots[(hash = this.hashEqualator.hash(element)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.element, element)){
				e.setElement0(element); // intentionally no moving to end here to cleanly separate concearns
				return false;
			}
		}
		this.chain.appendEntry(this.createNewEntry(hash, element));
		return true;
	}

	@Override
	public final HashCollection.Analysis<EqHashEnum<E>> analyze()
	{
		return AbstractChainEntryLinked.analyzeSlots(this, this.slots);
	}

	@Override
	public final int hashDistributionRange()
	{
		return this.slots.length;
	}

	@Override
	public final HashEqualator<? super E> hashEquality()
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
		return this.chain.appendTo(VarString.New(this.slots.length).append('['), ",").append(']').toString();
	}

	public final Procedure<E> procedureRemove()
	{
		return new Procedure<E>(){ @Override public void accept(final E element) {
			EqHashEnum.this.remove(element);
		}};
	}


	public final Predicate<E> predicateContains()
	{
		return new Predicate<E>(){ @Override public boolean test(final E element) {
			return EqHashEnum.this.contains(element);
		}};
	}

	@Override
	public final EqHashEnum<E> sort(final Comparator<? super E> comparator)
	{
		this.chain.sort(comparator);
		return this;
	}



	///////////////////////////////////////////////////////////////////////////
	// getting methods  //
	/////////////////////

	@Override
	public final XEnum<E> range(final int lowIndex, final int highIndex)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#range()
	}

	@Override
	public final XGettingEnum<E> view(final int lowIndex, final int highIndex)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#view()
	}

	@Override
	public final E[] toArray(final Class<E> type)
	{
		return this.chain.toArray(type);
	}

	// executing //

	@Override
	public final <P extends Procedure<? super E>> P iterate(final P procedure)
	{
		this.chain.iterate(procedure);
		return procedure;
	}

	@Override
	public final <A> A join(final BiProcedure<? super E, ? super A> joiner, final A aggregate)
	{
		this.chain.join(joiner, aggregate);
		return aggregate;
	}

	@Override
	public final int count(final E entry)
	{
		return this.chain.count(entry, this.hashEqualator);
	}

	@Override
	public final int countBy(final Predicate<? super E> predicate)
	{
		return this.chain.count(predicate);
	}

	// element querying //

	@Override
	public final E search(final Predicate<? super E> predicate)
	{
		return this.chain.search(predicate);
	}

	@Override
	public final E max(final Comparator<? super E> comparator)
	{
		return this.chain.max(comparator);
	}

	@Override
	public final E min(final Comparator<? super E> comparator)
	{
		return this.chain.min(comparator);
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
	public final boolean hasDistinctValues(final Equalator<? super E> equalator)
	{
		return this.chain.hasDistinctValues(equalator);
	}

	// boolean querying - applies //

	@Override
	public final boolean containsSearched(final Predicate<? super E> predicate)
	{
		return this.chain.applies(predicate);
	}

	@Override
	public final boolean applies(final Predicate<? super E> predicate)
	{
		return this.chain.appliesAll(predicate);
	}

	// boolean querying - contains //

	@Override
	public final boolean nullContained()
	{
		return false;
	}

	@Override
	public final boolean containsId(final E element)
	{
		final int hash; // search for element by hash
		for(ChainEntryLinkedHashedStrong<E> e = this.slots[(hash = this.hashEqualator.hash(element)) & this.range]; e != null; e = e.link){
			if(hash == e.hash && element == e.element){
				return true;
			}
		}
		return false;
	}

	@Override
	public final boolean contains(final E element)
	{
		final int hash; // search for element by hash
		for(ChainEntryLinkedHashedStrong<E> e = this.slots[(hash = this.hashEqualator.hash(element)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.element, element)){
				return true;
			}
		}
		return false;
	}

	@Override
	public final E seek(final E sample)
	{
		if(sample == null){ // null special case
			return null;
		}

		final int hash; // search for element by hash
		for(ChainEntryLinkedHashedStrong<E> e = this.slots[(hash = this.hashEqualator.hash(sample)) & this.range]; e != null; e = e.link){
			if(e.hash == hash && this.hashEqualator.equal(e.element, sample)){
				return e.element;
			}
		}
		return null;
	}

	@Override
	public final boolean containsAll(final XGettingCollection<? extends E> elements)
	{
		return elements.applies(this.predicateContains());
	}

	// boolean querying - equality //

	@Override
	public final boolean equals(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		if(samples == null || !(samples instanceof EqHashEnum<?>)){
			return false;
		}
		if(samples == this){
			return true;
		}
		return this.equalsContent(samples, equalator);
	}

	@Override
	public final boolean equalsContent(final XGettingCollection<? extends E> samples, final Equalator<? super E> equalator)
	{
		this.consolidate();
		if(this.size != Jadoth.to_int(samples.size())){
			return false;
		}

		// if sizes are equal and all elements of collection are contained in this set, they must have equal content
		return this.chain.equalsContent(samples, equalator);
	}

	// data set procedures //

	@Override
	public final <C extends Procedure<? super E>> C intersect(
		final XGettingCollection<? extends E> other,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		return this.chain.intersect(other, equalator, target);
	}

	@Override
	public final <C extends Procedure<? super E>> C except(
		final XGettingCollection<? extends E> other,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		return this.chain.except(other, equalator, target);
	}

	@Override
	public final <C extends Procedure<? super E>> C union(
		final XGettingCollection<? extends E> other,
		final Equalator<? super E> equalator,
		final C target
	)
	{
		return this.chain.union(other, equalator, target);
	}

	@Override
	public final <C extends Procedure<? super E>> C copyTo(final C target)
	{
		if(target == this){
			return target; // copying a set logic collection to itself would be a no-op, so spare the effort
		}
		return this.chain.copyTo(target);
	}

	@Override
	public final <C extends Procedure<? super E>> C filterTo(final C target, final Predicate<? super E> predicate)
	{
		return this.chain.copyTo(target, predicate);
	}

	@Override
	public final <T> T[] copyTo(final T[] target, final int offset)
	{
		this.chain.copyToArray(0, this.size, target, offset);
		return target;
	}

	@Override
	public final <C extends Procedure<? super E>> C distinct(final C target)
	{
		return this.distinct(target, this.hashEqualator);
	}

	@Override
	public final <C extends Procedure<? super E>> C distinct(final C target, final Equalator<? super E> equalator)
	{
		return this.chain.distinct(target, equalator);
	}

	@Override
	public final boolean nullAdd()
	{
		return this.add((E)null);
	}

	@SafeVarargs
	@Override
	public final EqHashEnum<E> addAll(final E... elements)
	{
		final EqHashEnum<E> parent = this;
		for(int i = 0, len = elements.length; i < len; i++) {
			parent.add(elements[i]);
		}
		return this;
	}

	@Override
	public final EqHashEnum<E> addAll(final E[] elements, final int srcIndex, final int srcLength)
	{
		final int d;
		if((d = JadothArrays.validateArrayRange(elements, srcIndex, srcLength)) == 0){
			return this;
		}
		final int bound = srcIndex + srcLength;
		final EqHashEnum<E> parent = this;
		for(int i = srcIndex; i != bound; i+=d) {
			parent.add(elements[i]);
		}
		return this;
	}

	@Override
	public final EqHashEnum<E> addAll(final XGettingCollection<? extends E> elements)
	{
		final EqHashEnum<E> parent = this;
		elements.iterate(new Procedure<E>(){ @Override public void accept(final E e){
			parent.add(e);
		}});
		return this;
	}

	@Override
	public final boolean nullPut()
	{
		return this.put((E)null);
	}

	@Override
	public final void accept(final E entry)
	{
		this.put(entry);
	}

	@SafeVarargs
	@Override
	public final EqHashEnum<E> putAll(final E... elements)
	{
		final EqHashEnum<E> parent = this;
		for(int i = 0, len = elements.length; i < len; i++) {
			parent.put(elements[i]);
		}
		return this;
	}

	@Override
	public final EqHashEnum<E> putAll(final E[] elements, final int srcIndex, final int srcLength)
	{
		final int d;
		if((d = JadothArrays.validateArrayRange(elements, srcIndex, srcLength)) == 0) return this;
		final int bound = srcIndex + srcLength;
		final EqHashEnum<E> parent = this;
		for(int i = srcIndex; i != bound; i+=d) {
			parent.put(elements[i]);
		}
		return this;
	}

	@Override
	public final EqHashEnum<E> putAll(final XGettingCollection<? extends E> elements)
	{
		return elements.copyTo(this);
	}

	// removing //

	@Override
	public final int remove(final E entry)
	{
		return this.chain.remove(entry, this.hashEqualator);
	}

	@Override
	public final int nullRemove()
	{
		return 0; // cannot remove a null entry because it can never be contained (only null key or null values)
	}

	// reducing //

	@Override
	public final int removeBy(final Predicate<? super E> predicate)
	{
		return this.chain.reduce(predicate);
	}

	// retaining //

	@Override
	public final int retainAll(final XGettingCollection<? extends E> elements)
	{
		return this.chain.retainAll(elements, this.hashEqualator);
	}

	@Override
	public final <P extends Procedure<? super E>> P process(final P procedure)
	{
		this.chain.process(procedure);
		return procedure;
	}

	@Override
	public final <C extends Procedure<? super E>> C moveTo(final C target, final Predicate<? super E> predicate)
	{
		this.chain.moveTo(target, predicate);
		return target;
	}

	// removing - all //

	@Override
	public final int removeAll(final XGettingCollection<? extends E> elements)
	{
		final int oldSize = this.size;
		elements.iterate(this.procedureRemove());
		return oldSize - this.size;
	}

	// removing - duplicates //

	@Override
	public final int removeDuplicates()
	{
		return 0;
	}

	@Override
	public final int removeDuplicates(final Equalator<? super E> equalator)
	{
		// singleton null can be ignored here
		return this.chain.removeDuplicates(equalator);
	}

	@Override
	public final EqHashEnum<E> toReversed()
	{
		final EqHashEnum<E> reversedVarSet = this.copy();
		reversedVarSet.chain.reverse();
		return reversedVarSet;
	}

	@Override
	public final <T extends Procedure<? super E>> T copySelection(final T target, final int... indices)
	{
		this.chain.copySelection(target, indices);
		return target;
	}

	@Override
	public final <T> T[] copyTo(final T[] target, final int targetOffset, final int offset, final int length)
	{
		this.chain.copyToArray(offset, length, target, targetOffset);
		return target;
	}

	@Override
	public final <P extends IndexProcedure<? super E>> P iterate(final P procedure)
	{
		this.chain.iterate(procedure);
		return procedure;
	}

	@Override
	public final E at(final int index)
	{
		return this.chain.get(index);
	}

	@Override
	public final E get()
	{
		return this.chain.first();
	}

	@Override
	public final E first()
	{
		return this.chain.first();
	}

	@Override
	public final E last()
	{
		return this.chain.last();
	}

	@Override
	public final E poll()
	{
		return this.size == 0 ?null :this.chain.first();
	}

	@Override
	public final E peek()
	{
		return this.size == 0 ?null :this.chain.last();
	}

	@Override
	public final int indexOf(final E entry)
	{
		return this.chain.indexOf(entry);
	}

	@Override
	public final int indexBy(final Predicate<? super E> predicate)
	{
		return this.chain.indexOf(predicate);
	}

	@Override
	public final boolean isSorted(final Comparator<? super E> comparator)
	{
		return this.chain.isSorted(comparator);
	}

	@Override
	public final int lastIndexOf(final E entry)
	{
		return this.chain.rngIndexOf(this.size - 1, -this.size, entry);
	}

	@Override
	public final int lastIndexBy(final Predicate<? super E> predicate)
	{
		return this.chain.rngIndexOf(this.size - 1, -this.size, predicate);
	}

	@Override
	public final int maxIndex(final Comparator<? super E> comparator)
	{
		return this.chain.maxIndex(comparator);
	}

	@Override
	public final int minIndex(final Comparator<? super E> comparator)
	{
		return this.chain.minIndex(comparator);
	}

	@Override
	public final int scan(final Predicate<? super E> predicate)
	{
		return this.chain.scan(predicate);
	}

	@Override
	public final <C extends Procedure<? super E>> C moveSelection(final C target, final int... indices)
	{
		this.chain.moveSelection(target, indices);
		return target;
	}

	@Override
	public final E removeAt(final int index)
	{
		return this.chain.remove(index);
	}

	@Override
	public final E fetch()
	{
		return this.chain.remove(0);
	}

	@Override
	public final E pop()
	{
		return this.chain.remove(this.size - 1);
	}

	@Override
	public final E pinch()
	{
		return this.size == 0 ?null :this.chain.remove(0);
	}

	@Override
	public final E pick()
	{
		return this.size == 0 ?null :this.chain.remove(this.size - 1);
	}

	@Override
	public final E retrieve(final E entry)
	{
		return this.chain.retrieve(entry);
	}

	@Override
	public final E retrieveBy(final Predicate<? super E> predicate)
	{
		return this.chain.retrieve(predicate);
	}

	@Override
	public final boolean removeOne(final E entry)
	{
		return this.chain.removeOne(entry);
	}

	@Override
	public final EqHashEnum<E> removeRange(final int startIndex, final int length)
	{
		this.chain.removeRange(startIndex, length);
		return this;
	}

	@Override
	public final EqHashEnum<E> retainRange(final int startIndex, final int length)
	{
		this.chain.retainRange(startIndex, length);
		return this;
	}

	@Override
	public final int removeSelection(final int[] indices)
	{
		return this.chain.removeSelection(indices);
	}

	@Override
	public final Iterator<E> iterator()
	{
		return this.chain.iterator();
	}

	@Override
	public final Object[] toArray()
	{
		return this.chain.toArray();
	}

	@Override
	public final EqHashEnum<E> reverse()
	{
		this.chain.reverse();
		return this;
	}

	@Override
	public final EqHashEnum<E> shiftTo(final int sourceIndex, final int targetIndex)
	{
		this.chain.shiftTo(sourceIndex, targetIndex);
		return this;
	}

	@Override
	public final EqHashEnum<E> shiftTo(final int sourceIndex, final int targetIndex, final int length)
	{
		this.chain.shiftTo(sourceIndex, targetIndex, length);
		return this;
	}

	@Override
	public final EqHashEnum<E> shiftBy(final int sourceIndex, final int distance)
	{
		this.chain.shiftTo(sourceIndex, distance);
		return this;
	}

	@Override
	public final EqHashEnum<E> shiftBy(final int sourceIndex, final int distance, final int length)
	{
		this.chain.shiftTo(sourceIndex, distance, length);
		return this;
	}

	@Override
	public final EqHashEnum<E> swap(final int indexA, final int indexB)
	{
		this.chain.swap(indexA, indexB);
		return this;
	}

	@Override
	public final EqHashEnum<E> swap(final int indexA, final int indexB, final int length)
	{
		this.chain.swap(indexA, indexB, length);
		return this;
	}

	@Override
	public final HashEqualator<? super E> equality()
	{
		return this.hashEqualator;
	}

	@Override
	public final boolean input(final int index, final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#input()
	}

	@SafeVarargs
	@Override
	public final int inputAll(final int index, final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#input()
	}

	@Override
	public final int inputAll(final int index, final E[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#inputAll()
	}

	@Override
	public final int inputAll(final int index, final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#inputAll()
	}

	@Override
	public final boolean insert(final int index, final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#insert()
	}

	@SafeVarargs
	@Override
	public final int insertAll(final int index, final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#insert()
	}

	@Override
	public final int insertAll(final int index, final E[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#insertAll()
	}

	@Override
	public final int insertAll(final int index, final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#insertAll()
	}

	@Override
	public final boolean prepend(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#prepend()
	}

	@Override
	public final boolean preput(final E element)
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
	public final EqHashEnum<E> prependAll(final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#prepend()
	}

	@Override
	public final EqHashEnum<E> prependAll(final E[] elements, final int srcStartIndex, final int srcLength)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#prependAll()
	}

	@Override
	public final EqHashEnum<E> prependAll(final XGettingCollection<? extends E> elements)
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
	public final EqHashEnum<E> preputAll(final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#preput()
	}

	@Override
	public final EqHashEnum<E> preputAll(final E[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#preputAll()
	}

	@Override
	public final EqHashEnum<E> preputAll(final XGettingCollection<? extends E> elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#preputAll()
	}

	@Override
	public final boolean set(final int index, final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#set()
	}

	@Override
	public final E setGet(final int index, final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#setGet()
	}

	@Override
	public final void setFirst(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#setFirst()
	}

	@Override
	public final void setLast(final E element)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#setLast()
	}

	@SafeVarargs
	@Override
	public final EqHashEnum<E> setAll(final int index, final E... elements)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#set()
	}

	@Override
	public final EqHashEnum<E> set(final int index, final E[] elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#set()
	}

	@Override
	public final EqHashEnum<E> set(final int index, final XGettingSequence<? extends E> elements, final int offset, final int length)
	{
		throw new net.jadoth.meta.NotImplementedYetError(); // FIX-ME EqHashTable.Entries#set()
	}



	public static final class OldVarSet<E> extends BridgeXSet<E>
	{
		OldVarSet(final EqHashEnum<E> set)
		{
			super(set);
		}

		@Override
		public EqHashEnum<E> parent()
		{
			return (EqHashEnum<E>)super.parent();
		}

	}

}
