package net.jadoth.functional;

public abstract class WrapperProcedureSkipLimit<I> implements Procedure<I>
{
	protected long skip, limit;

	WrapperProcedureSkipLimit(final long skip, final long limit)
	{
		super();
		this.skip  = skip ;
		this.limit = limit;
	}

}