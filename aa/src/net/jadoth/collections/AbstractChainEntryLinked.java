package net.jadoth.collections;

import static net.jadoth.Jadoth.ints;

import java.util.HashMap;
import java.util.Map;

import net.jadoth.Jadoth;
import net.jadoth.collections.types.HashCollection;
import net.jadoth.collections.types.HashCollection.Analysis;
import net.jadoth.util.KeyValue;


/**
 * @author Thomas Muenz
 *
 */
public abstract class AbstractChainEntryLinked<E, K, V, EN extends AbstractChainEntryLinked<E, K, V, EN>>
extends AbstractChainEntry<E, K, V, EN>
{
	public static <E, K, V, C extends HashCollection<K>, EN extends AbstractChainEntryLinked<E, K, V, EN>>
	Analysis<C> analyzeSlots(final C hashCollection, final EN[] slots)
	{
		final HashMap<Integer, int[]> distribution = new HashMap<>();

		int emptySlotCount = 0;
		for(EN entry : slots){
			if(entry == null){
				emptySlotCount++;
				continue;
			}

			int chainLength = 1;
			for(entry = entry.link; entry != null; entry = entry.link){
				chainLength++;
			}
			// intentionally dirty int reference hack (local implementation detail)
			final int[] count = distribution.get(chainLength);
			if(count == null){
				distribution.put(chainLength, ints(1));
			}
			else {
				count[0]++;
			}
		}
		distribution.put(0, ints(emptySlotCount));

		final int distRange = distribution.size();
		final LimitList<KeyValue<Integer, Integer>> result = new LimitList<>(distRange);

		int shortestEntryChainLength = Integer.MAX_VALUE;
		int longestEntryChainLength = 0;
		for(final Map.Entry<Integer, int[]> e : distribution.entrySet()){
			final int chainLength = e.getKey();
			if(chainLength > 0){
				if(chainLength < shortestEntryChainLength){
					shortestEntryChainLength = chainLength;
				}
				else if(chainLength > longestEntryChainLength){
					longestEntryChainLength = chainLength;
				}
			}
			result.add(Jadoth.keyValue(e.getKey(), e.getValue()[0]));
		}

		// sort by chain length
		JadothSort.valueSort(
			result.internalGetStorageArray(),
			(kv1, kv2) -> kv1.key().intValue() - kv2.key().intValue()
		);


		return new HashCollection.Analysis<>(
			hashCollection,
			Jadoth.to_int(hashCollection.size()),
			hashCollection.hashDensity(),
			slots.length,
			shortestEntryChainLength,
			longestEntryChainLength,
			distRange,
			result.immure()
		);
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	EN link; // the next (linked) entry in the hash chain (null for last in hash chain).



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	protected AbstractChainEntryLinked(final EN link)
	{
		super();
		this.link = link;
	}

}
