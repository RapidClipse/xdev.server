package net.jadoth.cql;

import static net.jadoth.Jadoth.coalesce;
import static net.jadoth.Jadoth.notNull;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.jadoth.Jadoth;
import net.jadoth.collections.BulkList;
import net.jadoth.collections.EqHashEnum;
import net.jadoth.collections.EqHashTable;
import net.jadoth.collections.HashEnum;
import net.jadoth.collections.HashTable;
import net.jadoth.collections.LimitList;
import net.jadoth.collections.X;
import net.jadoth.collections.XIterable;
import net.jadoth.collections.functions.AggregateSum;
import net.jadoth.collections.interfaces.Sized;
import net.jadoth.collections.interfaces.To_double;
import net.jadoth.collections.sorting.Sortable;
import net.jadoth.collections.sorting.SortableProcedure;
import net.jadoth.collections.types.XSequence;
import net.jadoth.functional.Aggregator;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.JadothProcedures;
import net.jadoth.functional.Procedure;
import net.jadoth.hash.HashEqualator;
import net.jadoth.util.KeyValue;


/**
 * Collection Query Library
 * <p>
 * API for fluent query syntax and query instance definition.
 *
 * @author Thomas Muenz
 */
public final class CQL
{
	///////////////////////////////////////////////////////////////////////////
	// query stub constructors //
	////////////////////////////

	public static <I> CqlSelection<I> Selection()
	{
		return CqlSelection.New();
	}

	public static <I> CqlTransfer<I, XSequence<I>> Transfer()
	{
		return CqlTransfer.New();
	}

	public static <I, R> CqlAggregation<I, R> Aggregation()
	{
		return CqlAggregation.New();
	}

	public static <I, O> CqlProjection<I, O> Projection()
	{
		return CqlProjection.New();
	}

	public static <I, O, R extends XIterable<O>> CqlIteration<I, O, R> Iteration()
	{
		return CqlIteration.New();
	}

	public static <I, O, R> CqlQuery<I, O, R> Query()
	{
		return CqlQuery.New();
	}



	///////////////////////////////////////////////////////////////////////////
	// fluent API methods //
	///////////////////////

	public static <I> CqlSelection<I> select(final Predicate<? super I> predicate)
	{
		return CqlSelection.New(null, null, null, predicate, null);
	}

	public static <I, R> CqlAggregation<I, R> aggregate(final CqlResultor<I, R> aggregator)
	{
		return CqlAggregation.New(null, null, null, null, null, aggregator);
	}

	public static <I, R> CqlAggregation<I, R> aggregate(final Supplier<R> supplier, final BiProcedure<I, R> linker)
	{
		return aggregate(CqlResultor.New(supplier, linker));
	}

	public static <I, R extends Sortable<I>> CqlAggregation<I, R> aggregate(
		final Supplier<R>           supplier,
		final BiProcedure<I, R>     linker  ,
		final Comparator<? super I> order
	)
	{
		return aggregate(CqlResultor.New(supplier, linker, order));
	}

	public static <I, R> CqlAggregation<I, R> aggregate(
		final Supplier<R>          supplier ,
		final BiProcedure<I, R>    linker   ,
		final Procedure<? super R> finalizer
	)
	{
		return aggregate(CqlResultor.New(supplier, linker, finalizer));
	}

	public static <I> CqlSelection<I> from(final XIterable<? extends I> source)
	{
		return CqlSelection.New(source, null, null, null, null);
	}

	public static <I, O> CqlProjection<I, O> project(final Function<? super I, O> projector)
	{
		return CqlProjection.New(null, null, null, null, projector, null);
	}

	/**
	 * Fluent alias for {@code predicate.negate()}.
	 *
	 * @param predicate
	 * @return
	 */
	public static <T> Predicate<T> not(final Predicate<T> predicate)
	{
		return predicate.negate();
	}

	/**
	 * Helper method to make a lambda or method reference expression chainable.
	 * They somehow forgot that in the lambda language extension, so it has to be worked around, sadly.
	 *
	 * @param predicate
	 * @return
	 */
	public static <T> Predicate<T> where(final Predicate<T> predicate)
	{
		return predicate;
	}

	/**
	 * Fluent alias for {@code Comparator#reversed()}
	 *
	 * @param order
	 * @return
	 */
	public static <T> Comparator<T> reversed(final Comparator<T> order)
	{
		return order.reversed();
	}

	/**
	 * Helper method to make a lambda or method reference expression chainable.
	 * They somehow forgot that in the lambda language extension, so it has to be worked around, sadly.
	 *
	 * @param order
	 * @return
	 */
	public static <T> Comparator<T> comparing(final Comparator<T> order)
	{
		return order;
	}

	public static <E, T> Comparator<E> comparing(final Function<E, T> getter, final Comparator<T> order)
	{
		return (e1, e2) -> order.compare(getter.apply(e1), getter.apply(e2));
	}

	public static <I> Function<I, I> identity()
	{
		return Function.identity();
	}



	///////////////////////////////////////////////////////////////////////////
	// preparing //
	//////////////

	public static <I> XIterable<? extends I> prepareSource(final XIterable<? extends I> source)
	{
		return coalesce(source, X.empty());
	}

	public static <E> XSequence<E> prepareTargetCollection(final XIterable<?> source)
	{
		// best effort to choose a suitable generic buffer type
		return source instanceof Sized
			?new LimitList<>(Jadoth.to_int(((Sized)source).size()))
			:new BulkList<>(32)
		;
	}

	public static <I> Procedure<I> prepareSourceIterator(
		final Long                 skip     ,
		final Long                 limit    ,
		final Predicate<? super I> selector ,
		final Procedure<? super I> target
	)
	{
		if(selector == null){
			return prepareSourceIterator(skip, limit, target);
		}

		if(isSkip(skip)){
			return isLimit(limit)
				?JadothProcedures.wrapWithPredicateSkipLimit(target, selector, skip, limit)
				:JadothProcedures.wrapWithPredicateSkip     (target, selector, skip       )
			;
		}
		return isLimit(limit)
			?JadothProcedures.wrapWithPredicateLimit(target, selector, limit)
			:JadothProcedures.wrapWithPredicate     (target, selector       )
		;
	}

	public static <I> Procedure<I> prepareSourceIterator(
		final Long                 skip  ,
		final Long                 limit ,
		final Procedure<? super I> target
	)
	{
		if(isSkip(skip)){
			return isLimit(limit)
				?JadothProcedures.wrapWithSkipLimit(target, skip, limit)
				:JadothProcedures.wrapWithSkip     (target, skip)
			;
		}

		return isLimit(limit)
			?JadothProcedures.wrapWithLimit(target, limit)
			:i -> target.accept(i)
		;
	}

	public static <I, O> Procedure<I> prepareSourceIterator(
		final Long                   skip     ,
		final Long                   limit    ,
		final Predicate<? super I>   selector ,
		final Function<? super I, O> projector,
		final Procedure<? super O>   target
	)
	{
		if(selector == null){
			return prepareSourceIterator(skip, limit, projector, target);
		}
		notNull(projector);

		if(isSkip(skip)){
			return isLimit(limit)
				?JadothProcedures.wrapWithPredicateFunctionSkipLimit(target, selector, projector, skip, limit)
				:JadothProcedures.wrapWithPredicateFunctionSkip     (target, selector, projector, skip       )
			;
		}
		return isLimit(limit)
			?JadothProcedures.wrapWithPredicateFunctionLimit(target, selector, projector, limit)
			:JadothProcedures.wrapWithPredicateFunction     (target, selector, projector       )
		;
	}

	public static <I, O> Procedure<I> prepareSourceIterator(
		final Long                   skip     ,
		final Long                   limit    ,
		final Function<? super I, O> projector,
		final Procedure<? super O>   target
	)
	{
		notNull(projector);

		if(isSkip(skip)){
			return isLimit(limit)
				?JadothProcedures.wrapWithFunctionSkipLimit(target, projector, skip, limit)
				:JadothProcedures.wrapWithFunctionSkip     (target, projector, skip       )
			;
		}

		return isLimit(limit)
			?JadothProcedures.wrapWithFunctionLimit(target, projector, limit)
			:JadothProcedures.wrapWithFunction     (target, projector       )
		;
	}



	///////////////////////////////////////////////////////////////////////////
	// executing //
	//////////////

	public static <E> XSequence<E> executeQuery(final XIterable<? extends E> source)
	{
		return executeQuery(source, prepareTargetCollection(source));
	}

	public static <E, T extends Procedure<? super E>> T executeQuery(final XIterable<? extends E> source, final T target)
	{
		return source.iterate(target);
	}

	public static <I, P extends Procedure<? super I>> P executeQuery(
		final XIterable<? extends I> source  ,
		final Long                   skip    ,
		final Long                   limit   ,
		final Predicate<? super I>   selector,
		final P                      target
	)
	{
		executeQuery(
			source,
			prepareSourceIterator(skip, limit, selector, target)
		);
		return target;
	}

	public static <I, P extends Procedure<I>> P executeQuery(
		final XIterable<? extends I> source  ,
		final Long                   skip    ,
		final Long                   limit   ,
		final Predicate<? super I>   selector,
		final P                      target  ,
		final Comparator<? super I>  order
	)
	{
		executeQuery(source, skip, limit, selector, target);
		SortableProcedure.sortIfApplicable(target, order);
		return target;
	}

	public static <I, R> R executeQuery(
		final XIterable<? extends I> source  ,
		final Long                   skip    ,
		final Long                   limit   ,
		final Predicate<? super I>   selector,
		final CqlResultor<I, R>      resultor,
		final Comparator<? super I>  order
	)
	{
		final Aggregator<I, R> collector = resultor.prepareCollector(source);
		executeQuery(source, skip, limit, selector, collector, order);
		return collector.yield();
	}

	public static <I, O, R> R executeQuery(
		final XIterable<? extends I> source   ,
		final Long                   skip     ,
		final Long                   limit    ,
		final Predicate<? super I>   selector ,
		final Function<? super I, O> projector,
		final CqlResultor<O, R>      resultor ,
		final Comparator<? super O>  order
	)
	{
		final Aggregator<O, R> collector = resultor.prepareCollector(source);
		executeQuery(source, skip, limit, selector, projector, collector, order);
		return collector.yield();
	}

	public static <I, O, P extends Procedure<O>> P executeQuery(
		final XIterable<? extends I> source   ,
		final Long                   skip     ,
		final Long                   limit    ,
		final Predicate<? super I>   selector ,
		final Function<? super I, O> projector,
		final P                      target   ,
		final Comparator<? super O>  order
	)
	{
		executeQuery(
			source,
			prepareSourceIterator(skip, limit, selector , projector, target)
		);
		SortableProcedure.sortIfApplicable(target, order);
		return target;
	}



	///////////////////////////////////////////////////////////////////////////
	// Resultor constructors //
	//////////////////////////

	public static <O, T extends Procedure<O>> CqlResultor<O, T> resulting(final Supplier<T> supplier)
	{
		return CqlResultor.New(supplier);
	}

	public static <O, T extends Procedure<O> & XIterable<O>> CqlResultor<O, T> resultingIterable(
		final Supplier<T> supplier
	)
	{
		return CqlResultor.New(supplier);
	}

	public static <O> CqlResultor<O, BulkList<O>> resultingBulkList()
	{
		return CqlResultor.New((Supplier<BulkList<O>>)BulkList::New);
	}

	public static <O> CqlResultor<O, BulkList<O>> resultingBulkList(final int initialCapacity)
	{
		return CqlResultor.New(() -> BulkList.<O>New(initialCapacity));
	}

	public static <O> CqlResultor<O, LimitList<O>> resultingLimitList(final int initialCapacity)
	{
		return CqlResultor.New(() -> LimitList.<O>New(initialCapacity));
	}

	public static <O> CqlResultor<O, HashEnum<O>> resultingHashEnum()
	{
		return CqlResultor.New(() -> HashEnum.<O>New());
	}

	public static <O> CqlResultor<O, EqHashEnum<O>> resultingEqHashEnum()
	{
		return CqlResultor.New(() -> EqHashEnum.<O>New());
	}

	public static <O> CqlResultor<O, EqHashEnum<O>> resultingEqHashEnum(final HashEqualator<? super O> hashEqualator)
	{
		return CqlResultor.New(() -> EqHashEnum.<O>New(hashEqualator));
	}

	public static <K, V> CqlResultor<KeyValue<K, V>, HashTable<K, V>> resultingHashTable()
	{
		return CqlResultor.New(() -> HashTable.<K, V>New());
	}

	public static <K, V> CqlResultor<KeyValue<K, V>, EqHashTable<K, V>> resultingEqHashTable()
	{
		return CqlResultor.New(() -> EqHashTable.<K, V>New());
	}

	public static <K, V> CqlResultor<KeyValue<K, V>, EqHashTable<K, V>> resultingEqHashTable(
		final HashEqualator<? super K> hashEqualator
	)
	{
		return CqlResultor.New(() -> EqHashTable.<K, V>New(hashEqualator));
	}

	// (25.03.2014 Tm)TODO: CQL: more resulting~


	public static <O> CqlResultor<O, Double> sum(final To_double<? super O> getter)
	{
		return e -> new AggregateSum<>(getter);
	}


	///////////////////////////////////////////////////////////////////////////
	// internal helper methods //
	////////////////////////////

	static Long asLong(final Number number)
	{
		return number instanceof Long ?(Long)number :number.longValue();
	}

	static boolean isSkip(final Long skip)
	{
		return skip != null && skip != 0;
	}

	static boolean isLimit(final Long limit)
	{
		return limit != null && limit != 0;
	}

	private CQL()
	{
		throw new java.lang.UnsupportedOperationException(); // static class only
	}

}
