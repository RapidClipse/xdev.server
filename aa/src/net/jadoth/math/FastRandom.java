package net.jadoth.math;

public final class FastRandom
{
	/* experimental singlethreaded static variant of java.util.Random for ints.
	 * Tests show 3 times faster execution.
	 * For "low quality" random numbers (e.g. for use in spot testing array values), this
	 * has no disadvantage over the "proper" implementation.
	 */
	private static final long multiplier = 0x5DEECE66DL;
	private static final long addend = 0xBL;
	private static final long mask = (1L << 48) - 1;
	private long seed = (System.nanoTime() ^ multiplier) & mask;

	private int next(final int bits) {
		long oldseed, nextseed;
		do {
			oldseed = this.seed;
			nextseed = oldseed*multiplier+addend & mask;
		} while(!this.compareAndSet(oldseed, nextseed)); // faster than whithout private method
		return (int)(nextseed >>> 48 - bits);
	}
	private boolean compareAndSet(final long oldseed, final long nextseed)
	{
		if(this.seed == oldseed){
			this.seed = nextseed;
			return true;
		}
		return false;
	}
	public final int nextInt(final int n)
	{
		if (n <= 0) throw new IllegalArgumentException("n must be positive");
		int bits, val;
		do val = (bits = this.next(31)) % n;
		while (bits - val + n-1 < 0);
		return val;
	}
}
