package net.jadoth.functional;


/**
 * @author Thomas Muenz
 *
 */
public interface IndexAggregator<E, R> extends IndexProcedure<E>
{
	@Override
	public void accept(E e, int index);

	public R yield();



	public interface Creator<E, R>
	{
		public IndexAggregator<E, R> createAggregator();
	}

}
