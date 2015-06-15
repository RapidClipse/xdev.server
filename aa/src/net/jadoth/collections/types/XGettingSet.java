package net.jadoth.collections.types;

import net.jadoth.functional.Procedure;


/**
 * @author Thomas Muenz
 *
 */
public interface XGettingSet<E> extends XGettingCollection<E>
{
	public interface Creator<E> extends XGettingCollection.Creator<E>
	{
		@Override
		public XGettingSet<E> newInstance();
	}



	@Override
	public XImmutableSet<E> immure();

	@Override
	public XGettingSet<E> copy();

	@Override
	public <P extends Procedure<? super E>> P iterate(P procedure);

}
