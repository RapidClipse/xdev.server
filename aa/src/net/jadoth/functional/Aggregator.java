package net.jadoth.functional;


/**
 * @author Thomas Muenz
 *
 */
@FunctionalInterface
public interface Aggregator<E, R> extends Procedure<E>
{
	public default Aggregator<E, R> reset()
	{
		// no-op in default implementation (no state to reset)
		return this;
	}

	@Override
	public void accept(E element);

	public default R yield()
	{
		return null;
	}



	public interface Creator<E, R>
	{
		public Aggregator<E, R> createAggregator();
	}



	public abstract class AbstractImplementation<E, R> implements Aggregator<E, R>
	{
		protected final R result;

		public AbstractImplementation(final R result)
		{
			super();
			this.result = result;
		}

		@Override
		public final R yield()
		{
			return this.result;
		}


	}

}
