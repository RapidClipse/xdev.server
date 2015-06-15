package net.jadoth.functional;


/**
 * @author Thomas Muenz
 *
 */
public final class SumLong implements Aggregator<Long, Long>
{
	private long sum = 0;

	@Override
	public final void accept(final Long n)
	{
		if(n != null){
			this.sum += n;
		}
	}

	@Override
	public final Long yield()
	{
		return this.sum;
	}

}
