package net.jadoth.collections.types;

import net.jadoth.functional.Procedure;

public interface XGettingEnum<E> extends XGettingSet<E>, XGettingSequence<E>
{
	public interface Creator<E> extends XGettingSet.Creator<E>, XGettingSequence.Factory<E>
	{
		@Override
		public XGettingEnum<E> newInstance();
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public XImmutableEnum<E> immure();

	@Override
	public XGettingEnum<E> copy();

	@Override
	public XGettingEnum<E> toReversed();

	@Override
	public XGettingEnum<E> view();

	@Override
	public XGettingEnum<E> view(int lowIndex, int highIndex);

	@Override
	public XGettingEnum<E> range(int lowIndex, int highIndex);

	@Override
	public <P extends Procedure<? super E>> P iterate(P procedure);

}
