package net.jadoth.cql;

import static net.jadoth.Jadoth.notNull;

import java.util.Comparator;
import java.util.function.Supplier;

import net.jadoth.collections.XIterable;
import net.jadoth.collections.sorting.Sortable;
import net.jadoth.collections.sorting.SortableProcedure;
import net.jadoth.collections.types.XSequence;
import net.jadoth.functional.Aggregator;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.Procedure;

public interface CqlResultor<O,R>
{
	public Aggregator<O, R> prepareCollector(XIterable<?> source);



	public static <O> CqlResultor<O, XSequence<O>> New()
	{
		return e -> new CqlWrapperCollectorProcedure<>(CQL.prepareTargetCollection(e));
	}

	public static <O, R> CqlResultor<O, R> New(final Aggregator<O, R> collector)
	{
		notNull(collector);
		return e -> collector;
	}

	public static <O, T extends Procedure<O> & XIterable<O>> CqlResultor<O, T> New(final T target)
	{
		notNull(target);
		return e -> new CqlWrapperCollectorProcedure<>(target);
	}

	public static <O, T extends Procedure<O>> CqlResultor<O, T> New(final Supplier<T> supplier)
	{
		return e -> new CqlWrapperCollectorProcedure<>(supplier.get());
	}

	public static <O, T extends SortableProcedure<O> & XIterable<O>> CqlResultor<O, T> New(
		final Supplier<T>           supplier,
		final Comparator<? super O> order
	)
	{
		notNull(supplier);
		return order == null
			?CqlResultor.New(supplier)
			:e -> new CqlWrapperCollectorSequenceSorting<>(supplier.get(), order)
		;
	}

	public static <O, T> CqlResultor<O, T> New(final Supplier<T> supplier, final BiProcedure<O, T> linker)
	{
		notNull(supplier);
		return e -> new Aggregator.AbstractImplementation<O, T>(supplier.get()) {
			@Override
			public void accept(final O element)
			{
				linker.accept(element, this.result);
			}
		};
	}

	public static <O, T extends Sortable<O>> CqlResultor<O, T> New(
		final Supplier<T>           supplier,
		final BiProcedure<O, T>     linker  ,
		final Comparator<? super O> order
	)
	{
		notNull(supplier);
		return order == null
			?CqlResultor.New(supplier, linker)
			:e -> new CqlWrapperCollectorLinkingSorting<>(supplier.get(), linker, order)
		;
	}

	public static <O, T> CqlResultor<O, T> New(
		final Supplier<T>          supplier ,
		final BiProcedure<O, T>    linker   ,
		final Procedure<? super T> finalizer
	)
	{
		notNull(supplier);
		return finalizer == null
			?CqlResultor.New(supplier, linker)
			:e -> new CqlWrapperCollectorLinkingFinalizing<>(supplier.get(), linker, finalizer)
		;
	}

}
