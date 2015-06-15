package net.jadoth.functional;

public abstract class WrapperProcedureLimit<I> implements Procedure<I>
{
	protected long limit;

	WrapperProcedureLimit(final long limit)
	{
		super();
		this.limit = limit;
	}

}