package net.jadoth.functional;

import java.util.function.Function;
import java.util.function.Predicate;

import net.jadoth.Jadoth;


public abstract class JadothProcedures
{
	///////////////////////////////////////////////////////////////////////////
	// class methods    //
	/////////////////////

	public static <E> Aggregator<E, Long> counter()
	{
		return new Aggregator<E, Long>(){
			long count = 0;

			@Override
			public void accept(final E element)
			{
				this.count++;
			}

			@Override
			public Long yield()
			{
				return this.count;
			}
		};
	}

	/**
	 * Constant for literally a no-op procedure.
	 */
	public static final <T> Procedure<T> noOp()
	{
		return e -> {/*noop*/};
	}

	// all direct //

	public static <E> Procedure<E> wrapWithSkip(final Procedure<? super E> procedure, final long skip)
	{
		return new WrapperProcedureSkip<E>(skip) {
			@Override
			public void accept(final E e)
			{
				if(--this.skip >= 0){
					return;
				}
				procedure.accept(e);
			}
		};
	}

	public static <E> Procedure<E> wrapWithLimit(final Procedure<? super E> procedure, final long limit)
	{
		return new WrapperProcedureLimit<E>(limit) {
			@Override
			public void accept(final E e)
			{
				procedure.accept(e);
				if(--this.limit == 0){
					throw Jadoth.BREAK;
				}
			}
		};
	}

	public static <E> Procedure<E> wrapWithSkipLimit(final Procedure<? super E> procedure, final long skip, final long limit)
	{
		return new WrapperProcedureSkipLimit<E>(skip, limit) {
			@Override
			public void accept(final E e)
			{
				if(--this.skip >= 0){
					return;
				}
				procedure.accept(e);
				if(--this.limit == 0){
					throw Jadoth.BREAK;
				}
			}
		};
	}

	// predicate //

	public static final <E> Procedure<E> wrapWithPredicate(
		final Procedure<? super E> procedure,
		final Predicate<? super E> predicate
	)
	{
		return e -> {
			if(!predicate.test(e)){
				return; // debug hook
			}
			procedure.accept(e);
		};
	}

	public static <E> Procedure<E> wrapWithPredicateSkip(
		final Procedure<? super E> procedure,
		final Predicate<? super E> predicate,
		final long                 skip
	)
	{
		return new WrapperProcedureSkip<E>(skip) {
			@Override
			public void accept(final E e)
			{
				if(!predicate.test(e)){
					return; // debug hook
				}
				if(--this.skip >= 0){
					return;
				}
				procedure.accept(e);
			}
		};
	}

	public static <E> Procedure<E> wrapWithPredicateLimit(
		final Procedure<? super E> procedure,
		final Predicate<? super E> predicate,
		final long                 limit
	)
	{
		return new WrapperProcedureLimit<E>(limit) {
			@Override
			public void accept(final E e)
			{
				if(!predicate.test(e)){
					return; // debug hook
				}
				procedure.accept(e);
				if(--this.limit == 0){
					throw Jadoth.BREAK;
				}
			}
		};
	}

	public static <E> Procedure<E> wrapWithPredicateSkipLimit(
		final Procedure<? super E> procedure,
		final Predicate<? super E> predicate,
		final long                 skip     ,
		final long                 limit
	)
	{
		return new WrapperProcedureSkipLimit<E>(skip, limit) {
			@Override
			public void accept(final E e)
			{
				if(!predicate.test(e)){
					return; // debug hook
				}
				if(--this.skip >= 0){
					return;
				}
				procedure.accept(e);
				if(--this.limit == 0){
					throw Jadoth.BREAK;
				}
			}
		};
	}

	// function //

	public static final <I, O> Procedure<I> wrapWithFunction(
		final Procedure<? super O>   procedure,
		final Function<? super I, O> function
	)
	{
		return e -> {
			procedure.accept(function.apply(e));
		};
	}

	public static <I, O> Procedure<I> wrapWithFunctionSkip(
		final Procedure<? super O>   procedure,
		final Function<? super I, O> function ,
		final long                   skip
	)
	{
		return new WrapperProcedureSkip<I>(skip) {
			@Override
			public void accept(final I e)
			{
				if(--this.skip >= 0){
					return;
				}
				procedure.accept(function.apply(e));
			}
		};
	}

	public static <I, O> Procedure<I> wrapWithFunctionLimit(
		final Procedure<? super O>   procedure,
		final Function<? super I, O> function ,
		final long                   limit
	)
	{
		return new WrapperProcedureLimit<I>(limit) {
			@Override
			public void accept(final I e)
			{
				procedure.accept(function.apply(e));
				if(--this.limit == 0){
					throw Jadoth.BREAK;
				}
			}
		};
	}

	public static <I, O> Procedure<I> wrapWithFunctionSkipLimit(
		final Procedure<? super O>   procedure,
		final Function<? super I, O> function ,
		final long                   skip     ,
		final long                   limit
	)
	{
		return new WrapperProcedureSkipLimit<I>(skip, limit) {
			@Override
			public void accept(final I e)
			{
				if(--this.skip >= 0){
					return;
				}
				procedure.accept(function.apply(e));
				if(--this.limit == 0){
					throw Jadoth.BREAK;
				}
			}
		};
	}

	// predicate function //

	public static final <I, O> Procedure<I> wrapWithPredicateFunction(
		final Procedure<? super O>   procedure,
		final Predicate<? super I>   predicate,
		final Function<? super I, O> function
	)
	{
		return e -> {
			if(!predicate.test(e)){
				return; // debug hook
			}
			procedure.accept(function.apply(e));
		};
	}

	public static <I, O> Procedure<I> wrapWithPredicateFunctionSkip(
		final Procedure<? super O>   procedure,
		final Predicate<? super I>   predicate,
		final Function<? super I, O> function ,
		final long                   skip
	)
	{
		return new WrapperProcedureSkip<I>(skip) {
			@Override
			public void accept(final I e)
			{
				if(!predicate.test(e)){
					return; // debug hook
				}
				if(--this.skip >= 0){
					return;
				}
				procedure.accept(function.apply(e));
			}
		};
	}

	public static <I, O> Procedure<I> wrapWithPredicateFunctionLimit(
		final Procedure<? super O>   procedure,
		final Predicate<? super I>   predicate,
		final Function<? super I, O> function ,
		final long                   limit
	)
	{
		return new WrapperProcedureLimit<I>(limit) {
			@Override
			public void accept(final I e)
			{
				if(!predicate.test(e)){
					return; // debug hook
				}
				procedure.accept(function.apply(e));
				if(--this.limit == 0){
					throw Jadoth.BREAK;
				}
			}
		};
	}

	public static <I, O> Procedure<I> wrapWithPredicateFunctionSkipLimit(
		final Procedure<? super O>   procedure,
		final Predicate<? super I>   predicate,
		final Function<? super I, O> function ,
		final long                   skip     ,
		final long                   limit
	)
	{
		return new WrapperProcedureSkipLimit<I>(skip, limit) {
			@Override
			public void accept(final I e)
			{
				if(!predicate.test(e)){
					return; // debug hook
				}
				if(--this.skip >= 0){
					return;
				}
				procedure.accept(function.apply(e));
				if(--this.limit == 0){
					throw Jadoth.BREAK;
				}
			}
		};
	}

}
