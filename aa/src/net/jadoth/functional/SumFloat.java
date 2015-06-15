package net.jadoth.functional;


/**
 * @author Thomas Muenz
 *
 */
public final class SumFloat implements Aggregator<Float, Double>
{
	private double sum = 0;

	@Override
	public final void accept(final Float n)
	{
		if(n != null){
			this.sum += n;
		}
	}

	@Override
	public final Double yield()
	{
		return this.sum;
	}

}
