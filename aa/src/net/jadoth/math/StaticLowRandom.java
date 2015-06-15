package net.jadoth.math;

public final class StaticLowRandom
{
	/* experimental singlethreaded static variant of java.util.Random for ints.
	 * Tests show 3 times faster execution.
	 * For "low quality" random numbers (e.g. for use in spot testing array values), this
	 * has no disadvantage over the "proper" implementation.
	 */
	private static final long multiplier = 0x5DEECE66DL;
	private static final long addend = 0xBL;
	private static final long mask = (1L << 48) - 1;
	private static long seed = (System.nanoTime() ^ multiplier) & mask;

	private static int next(final int bits) {
		long oldseed, nextseed;
		do {
			oldseed = seed;
			nextseed = oldseed*multiplier+addend & mask;
		} while(!compareAndSet(oldseed, nextseed)); // faster than whithout private method
		return (int)(nextseed >>> 48 - bits);
	}
	private static boolean compareAndSet(final long oldseed, final long nextseed)
	{
		// (05.04.2011 TM)FIXME: concurrency could mess this up, maybe move everything to instance code.
		if(seed == oldseed){
			seed = nextseed;
			return true;
		}
		return false;
	}
	public static final int nextInt(final int n)
	{
		if (n <= 0) throw new IllegalArgumentException("n must be positive");
//		// this does not yield any performance gain in tests, so why do it. Stability problems with 31 below?
//		if ((n & -n) == n){  // i.e., n is a power of 2
//			return (int)(n * (long)next(31) >> 31);
//		}
		int bits, val;
		do val = (bits = next(31)) % n;
		while (bits - val + n-1 < 0);
		return val;
	}
}
