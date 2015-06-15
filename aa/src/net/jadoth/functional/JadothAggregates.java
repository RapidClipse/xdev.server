package net.jadoth.functional;

import net.jadoth.collections.functions.AggregateCountAll;


public final class JadothAggregates
{
	///////////////////////////////////////////////////////////////////////////
	// class methods    //
	/////////////////////

	public static final Aggregator<Integer, Integer> max(final int initialValue)
	{
		return new MaxInteger(initialValue);
	}

	public static final <E> AggregateCountAll<E> countAll()
	{
		return new AggregateCountAll<>();
	}




	public static final class MaxInteger implements Aggregator<Integer, Integer>
	{
		private int max;

		public MaxInteger(final int max)
		{
			super();
			this.max = max;
		}

		@Override
		public final void accept(final Integer value)
		{
			if(value != null && value > this.max){
				this.max = value;
			}
		}

		@Override
		public final Integer yield()
		{
			return this.max;
		}

	}



	private JadothAggregates() { throw new UnsupportedOperationException(); } // static only
}
