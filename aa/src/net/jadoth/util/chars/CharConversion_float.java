package net.jadoth.util.chars;


public final class CharConversion_float
{
	/* (24.07.2013 TM)FIXME: CharConversion_float: copy from CharConversion_double
	 * (19.01.2014)NOTE: just copying and adjusting won't do because
	 *  long is too big (too many digits) and int is too small.
	 *
	 *  Additional logic is required that either assembles a full long and then cuts digits
	 *  (would be kind of stupid, also rounding issues) or that accounts for float length "properly".
	 *  Given that floats are hardly used because of their tiny resolution, this has no priority for now.
	 *
	 *  Also adjusting the logic to use all floats instead of double is not quite possible as
	 *  exponent() alone still requires to convert to double once anyway.
	 *  Probably the CPU converts them to double internally anyway and a 32bit value moved around
	 *  does not yield any advantage in a 64 bit architecture as well.
	 *  It's probably more efficient to still use double assembly but with reduced digits and add an according
	 *  comment (derived from this one here) to the implementation.
	 */

	public static final int put(final float value, final char[] target, final int offset)
	{
		// pure algorithm method intentionally without array bounds check. Use VarString etc. for that.

		return JadothChars.put(Float.toString(value), target, offset);
	}



	private CharConversion_float() { throw new UnsupportedOperationException(); } // static only
}
