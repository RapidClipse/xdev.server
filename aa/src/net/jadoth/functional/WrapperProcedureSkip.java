package net.jadoth.functional;

public abstract class WrapperProcedureSkip<I> implements Procedure<I>
{
	protected long skip;

	WrapperProcedureSkip(final long skip)
	{
		super();
		this.skip = skip;
	}

}