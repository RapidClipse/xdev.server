package net.jadoth.cql;

import net.jadoth.functional.Aggregator;
import net.jadoth.functional.BiProcedure;
import net.jadoth.functional.Procedure;

public final class CqlWrapperCollectorLinkingFinalizing<O, R> implements Aggregator<O, R>
{
	final R                    target   ;
	final BiProcedure<O, R>    linker   ;
	final Procedure<? super R> finalizer;

	CqlWrapperCollectorLinkingFinalizing(final R target, final BiProcedure<O, R> linker, final Procedure<? super R> finalizer)
	{
		super();
		this.target    = target   ;
		this.linker    = linker   ;
		this.finalizer = finalizer;
	}

	@Override
	public final void accept(final O element)
	{
		this.linker.accept(element, this.target);
	}

	@Override
	public final R yield()
	{
		this.finalizer.accept(this.target);
		return this.target;
	}

}
