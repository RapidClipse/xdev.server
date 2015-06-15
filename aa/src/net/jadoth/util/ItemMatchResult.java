package net.jadoth.util;

import net.jadoth.Jadoth;
import net.jadoth.collections.BulkList;
import net.jadoth.collections.ConstList;
import net.jadoth.collections.types.XGettingSequence;
import net.jadoth.functional.IndexProcedure;
import net.jadoth.functional.JadothPredicates;


public class ItemMatchResult<T>
{
	///////////////////////////////////////////////////////////////////////////
	//  class methods   //
	/////////////////////

	static <T> ConstList<T> collectRemaining(final ConstList<T> input, final ConstList<KeyValue<T,T>> matches)
	{
		final BulkList<T> remaining = new BulkList<>(Jadoth.to_int(input.size()));
		input.iterate(new IndexProcedure<T>() {
			@Override public void accept(final T e, final int index){
				remaining.add(matches.at(index) == null ?e :null);
			}
		});
		return remaining.immure();
	}

	static <T> ConstList<T> collectUnmatched(final ConstList<T> input, final ConstList<KeyValue<T,T>> matches)
	{
		final BulkList<T> unmatched = new BulkList<>(Jadoth.to_int(input.size()));
		input.iterate(new IndexProcedure<T>() {
			@Override public void accept(final T e, final int index){
				if(matches.at(index) == null){
					unmatched.add(e);
				}
			}
		});
		return unmatched.immure();
	}

	static <T> ConstList<T> collectMatched(final ConstList<T> input, final ConstList<KeyValue<T,T>> matches)
	{
		final BulkList<T> matched = new BulkList<>(Jadoth.to_int(input.size()));
		input.iterate(new IndexProcedure<T>() {
			@Override public void accept(final T e, final int index){
				if(matches.at(index) != null){
					matched.add(e);
				}
			}
		});
		return matched.immure();
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields  //
	/////////////////////

	final int matchCount;

	final ConstList<T> sources;
	final ConstList<T> targets;

	final ConstList<KeyValue<T,T>> matchesInSourceOrder;
	final ConstList<KeyValue<T,T>> matchesInTargetOrder;

	ConstList<T> remainingSources = null;
	ConstList<T> remainingTargets = null;
	ConstList<T> unmatchedSources = null;
	ConstList<T> unmatchedTargets = null;
	ConstList<T>   matchedSources = null;
	ConstList<T>   matchedTargets = null;

	ConstList<KeyValue<T,T>> sourceMatches = null;
	ConstList<KeyValue<T,T>> targetMatches = null;



	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	protected ItemMatchResult(
		final int matchCount,
		final ConstList<T> sourceInput,
		final ConstList<T> targetInput,
		final ConstList<KeyValue<T,T>> matchesInSourceOrder,
		final ConstList<KeyValue<T,T>> matchesInTargetOrder
	)
	{
		super();
		this.matchCount = matchCount;
		this.sources = sourceInput;
		this.targets = targetInput;
		this.matchesInSourceOrder = matchesInSourceOrder;
		this.matchesInTargetOrder = matchesInTargetOrder;
	}



	///////////////////////////////////////////////////////////////////////////
	// getters          //
	/////////////////////

	public int getMatchCount()
	{
		return this.matchCount;
	}

	public XGettingSequence<T> getInputSources()
	{
		return this.sources;
	}

	public XGettingSequence<T> getInputTargets()
	{
		return this.targets;
	}

	public XGettingSequence<KeyValue<T, T>> getMatchesInSourceOrder()
	{
		return this.matchesInSourceOrder;
	}

	public XGettingSequence<KeyValue<T, T>> getMatchesInTargetOrder()
	{
		return this.matchesInTargetOrder;
	}

	public XGettingSequence<T> getRemainingSources()
	{
		if(this.remainingSources == null){
			this.remainingSources = collectRemaining(this.sources, this.matchesInSourceOrder);
		}
		return this.remainingSources;
	}

	public XGettingSequence<T> getRemainingTargets()
	{
		if(this.remainingTargets == null){
			this.remainingTargets = collectRemaining(this.targets, this.matchesInTargetOrder);
		}
		return this.remainingTargets;
	}

	public XGettingSequence<T> getUnmatchedSources()
	{
		if(this.unmatchedSources == null){
			this.unmatchedSources = collectUnmatched(this.sources, this.matchesInSourceOrder);
		}
		return this.unmatchedSources;
	}

	public XGettingSequence<T> getUnmatchedTargets()
	{
		if(this.unmatchedTargets == null){
			this.unmatchedTargets = collectUnmatched(this.targets, this.matchesInTargetOrder);
		}
		return this.unmatchedTargets;
	}

	public XGettingSequence<KeyValue<T, T>> getSourceMatches()
	{
		if(this.sourceMatches == null){
			this.sourceMatches = this.matchesInSourceOrder.filterTo(new BulkList<KeyValue<T, T>>(this.matchCount), JadothPredicates.notNull()).immure();
		}
		return this.sourceMatches;
	}

	public XGettingSequence<KeyValue<T, T>> getTargetMatches()
	{
		if(this.targetMatches == null){
			this.targetMatches = this.matchesInTargetOrder.filterTo(new BulkList<KeyValue<T, T>>(this.matchCount), JadothPredicates.notNull()).immure();
		}
		return this.targetMatches;
	}

	public ConstList<T> getMatchedSources()
	{
		if(this.matchedSources == null){
			this.matchedSources = collectMatched(this.sources, this.matchesInSourceOrder);
		}
		return this.matchedSources;
	}

	public ConstList<T> getMatchedTargets()
	{
		if(this.matchedTargets == null){
			this.matchedTargets = collectMatched(this.targets, this.matchesInTargetOrder);
		}
		return this.matchedTargets;
	}

}
