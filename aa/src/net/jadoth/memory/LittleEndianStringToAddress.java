package net.jadoth.memory;





public class LittleEndianStringToAddress
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	private static final char[] STRING_BOOLEAN_FALSE = {'f', 'a', 'l', 's', 'e'};
	private static final long   STRING_BYTE_LENGTH_BOOLEAN_FALSE = STRING_BOOLEAN_FALSE.length<<1;

	private static final char[] STRING_BOOLEAN_TRUE = {'t', 'r', 'u', 'e'};
	private static final long   STRING_BYTE_LENGTH_BOOLEAN_TRUE = STRING_BOOLEAN_TRUE.length<<1;

	private static final char[] STRING_INT_MIN_VALUE = {'-', '2', '1', '4', '7', '4', '8', '3', '6', '4', '8'};
	private static final long   STRING_BYTE_LENGTH_INT_MIN_VALUE = STRING_INT_MIN_VALUE.length<<1;

	private static final char[] STRING_LONG_MIN_VALUE = {'-','9','2','2','3','3','7','2','0','3','6','8','5','4','7','7','5','8','0','8'};
	private static final long   STRING_BYTE_LENGTH_LONG_MIN_VALUE = STRING_LONG_MIN_VALUE.length<<1;

	private static final int[] LITTLE_ENDIAN_CHARS_00TO99 = {
		0x30_0030, 0x31_0030, 0x32_0030, 0x33_0030, 0x34_0030, 0x35_0030, 0x36_0030, 0x37_0030, 0x38_0030, 0x39_0030,
		0x30_0031, 0x31_0031, 0x32_0031, 0x33_0031, 0x34_0031, 0x35_0031, 0x36_0031, 0x37_0031, 0x38_0031, 0x39_0031,
		0x30_0032, 0x31_0032, 0x32_0032, 0x33_0032, 0x34_0032, 0x35_0032, 0x36_0032, 0x37_0032, 0x38_0032, 0x39_0032,
		0x30_0033, 0x31_0033, 0x32_0033, 0x33_0033, 0x34_0033, 0x35_0033, 0x36_0033, 0x37_0033, 0x38_0033, 0x39_0033,
		0x30_0034, 0x31_0034, 0x32_0034, 0x33_0034, 0x34_0034, 0x35_0034, 0x36_0034, 0x37_0034, 0x38_0034, 0x39_0034,
		0x30_0035, 0x31_0035, 0x32_0035, 0x33_0035, 0x34_0035, 0x35_0035, 0x36_0035, 0x37_0035, 0x38_0035, 0x39_0035,
		0x30_0036, 0x31_0036, 0x32_0036, 0x33_0036, 0x34_0036, 0x35_0036, 0x36_0036, 0x37_0036, 0x38_0036, 0x39_0036,
		0x30_0037, 0x31_0037, 0x32_0037, 0x33_0037, 0x34_0037, 0x35_0037, 0x36_0037, 0x37_0037, 0x38_0037, 0x39_0037,
		0x30_0038, 0x31_0038, 0x32_0038, 0x33_0038, 0x34_0038, 0x35_0038, 0x36_0038, 0x37_0038, 0x38_0038, 0x39_0038,
		0x30_0039, 0x31_0039, 0x32_0039, 0x33_0039, 0x34_0039, 0x35_0039, 0x36_0039, 0x37_0039, 0x38_0039, 0x39_0039
	};

	private static final int[] LITTLE_ENDIAN_HEX_DEV = {
		0x30_0030, 0x31_0030, 0x32_0030, 0x33_0030, 0x34_0030, 0x35_0030, 0x36_0030, 0x37_0030, 0x38_0030, 0x39_0030, 0x41_0030, 0x42_0030, 0x43_0030, 0x44_0030, 0x45_0030, 0x46_0030,
		0x30_0031, 0x31_0031, 0x32_0031, 0x33_0031, 0x34_0031, 0x35_0031, 0x36_0031, 0x37_0031, 0x38_0031, 0x39_0031, 0x41_0031, 0x42_0031, 0x43_0031, 0x44_0031, 0x45_0031, 0x46_0031,
		0x30_0032, 0x31_0032, 0x32_0032, 0x33_0032, 0x34_0032, 0x35_0032, 0x36_0032, 0x37_0032, 0x38_0032, 0x39_0032, 0x41_0032, 0x42_0032, 0x43_0032, 0x44_0032, 0x45_0032, 0x46_0032,
		0x30_0033, 0x31_0033, 0x32_0033, 0x33_0033, 0x34_0033, 0x35_0033, 0x36_0033, 0x37_0033, 0x38_0033, 0x39_0033, 0x41_0033, 0x42_0033, 0x43_0033, 0x44_0033, 0x45_0033, 0x46_0033,
		0x30_0034, 0x31_0034, 0x32_0034, 0x33_0034, 0x34_0034, 0x35_0034, 0x36_0034, 0x37_0034, 0x38_0034, 0x39_0034, 0x41_0034, 0x42_0034, 0x43_0034, 0x44_0034, 0x45_0034, 0x46_0034,
		0x30_0035, 0x31_0035, 0x32_0035, 0x33_0035, 0x34_0035, 0x35_0035, 0x36_0035, 0x37_0035, 0x38_0035, 0x39_0035, 0x41_0035, 0x42_0035, 0x43_0035, 0x44_0035, 0x45_0035, 0x46_0035,
		0x30_0036, 0x31_0036, 0x32_0036, 0x33_0036, 0x34_0036, 0x35_0036, 0x36_0036, 0x37_0036, 0x38_0036, 0x39_0036, 0x41_0036, 0x42_0036, 0x43_0036, 0x44_0036, 0x45_0036, 0x46_0036,
		0x30_0037, 0x31_0037, 0x32_0037, 0x33_0037, 0x34_0037, 0x35_0037, 0x36_0037, 0x37_0037, 0x38_0037, 0x39_0037, 0x41_0037, 0x42_0037, 0x43_0037, 0x44_0037, 0x45_0037, 0x46_0037,
		0x30_0038, 0x31_0038, 0x32_0038, 0x33_0038, 0x34_0038, 0x35_0038, 0x36_0038, 0x37_0038, 0x38_0038, 0x39_0038, 0x41_0038, 0x42_0038, 0x43_0038, 0x44_0038, 0x45_0038, 0x46_0038,
		0x30_0039, 0x31_0039, 0x32_0039, 0x33_0039, 0x34_0039, 0x35_0039, 0x36_0039, 0x37_0039, 0x38_0039, 0x39_0039, 0x41_0039, 0x42_0039, 0x43_0039, 0x44_0039, 0x45_0039, 0x46_0039,
		0x30_0041, 0x31_0041, 0x32_0041, 0x33_0041, 0x34_0041, 0x35_0041, 0x36_0041, 0x37_0041, 0x38_0041, 0x39_0041, 0x41_0041, 0x42_0041, 0x43_0041, 0x44_0041, 0x45_0041, 0x46_0041,
		0x30_0042, 0x31_0042, 0x32_0042, 0x33_0042, 0x34_0042, 0x35_0042, 0x36_0042, 0x37_0042, 0x38_0042, 0x39_0042, 0x41_0042, 0x42_0042, 0x43_0042, 0x44_0042, 0x45_0042, 0x46_0042,
		0x30_0043, 0x31_0043, 0x32_0043, 0x33_0043, 0x34_0043, 0x35_0043, 0x36_0043, 0x37_0043, 0x38_0043, 0x39_0043, 0x41_0043, 0x42_0043, 0x43_0043, 0x44_0043, 0x45_0043, 0x46_0043,
		0x30_0044, 0x31_0044, 0x32_0044, 0x33_0044, 0x34_0044, 0x35_0044, 0x36_0044, 0x37_0044, 0x38_0044, 0x39_0044, 0x41_0044, 0x42_0044, 0x43_0044, 0x44_0044, 0x45_0044, 0x46_0044,
		0x30_0045, 0x31_0045, 0x32_0045, 0x33_0045, 0x34_0045, 0x35_0045, 0x36_0045, 0x37_0045, 0x38_0045, 0x39_0045, 0x41_0045, 0x42_0045, 0x43_0045, 0x44_0045, 0x45_0045, 0x46_0045,
		0x30_0046, 0x31_0046, 0x32_0046, 0x33_0046, 0x34_0046, 0x35_0046, 0x36_0046, 0x37_0046, 0x38_0046, 0x39_0046, 0x41_0046, 0x42_0046, 0x43_0046, 0x44_0046, 0x45_0046, 0x46_0046
	};

	private static final int
		V001G = 1_000_000_000,
		V100M =   100_000_000,
		V010M =    10_000_000,
		V001M =     1_000_000,
		V100K =       100_000,
		V010K =        10_000,
		V001K =         1_000,
		V100  =           100,
		V010  =            10
	;

	private static final long
		V001Z = 1_000_000_000_000_000_000L,
		V100P =   100_000_000_000_000_000L,
		V010P =    10_000_000_000_000_000L,
		V001P =     1_000_000_000_000_000L,
		V100T =       100_000_000_000_000L,
		V010T =        10_000_000_000_000L,
		V001T =         1_000_000_000_000L,
		V100G =           100_000_000_000L,
		V010G =            10_000_000_000L
	;

	private static final int n = '\\'+('\n'<<16);
	private static final int r = '\\'+('\r'<<16);
	private static final int q = '\\'+( '"'<<16);
	private static final int b = '\\'+('\\'<<16);

	public static final long toString(final char value, final long address)
	{
		if(value == '\n'){
			Memory.set_int(address, n);
			return address + 4;
		}
		if(value == '\r'){
			Memory.set_int(address, r);
			return address + 4;
		}
		if(value == '\\'){
			Memory.set_int(address, b);
			return address + 4;
		}
		if(value == '"'){
			Memory.set_int(address, q);
			return address + 4;
		}
		Memory.set_char(address, value);
		return address + 2;
	}

	public static final long toString(final boolean value, final long address)
	{
		if(value){
			Memory.copyArray(STRING_BOOLEAN_TRUE, address);
			return address + STRING_BYTE_LENGTH_BOOLEAN_TRUE;
		}
		Memory.copyArray(STRING_BOOLEAN_FALSE, address);
		return address + STRING_BYTE_LENGTH_BOOLEAN_FALSE;
	}

	public static final long toHexDecString(final byte value, final long address)
	{
		// (25.02.2013)FIXME: fix negative offset
		Memory.set_int(address, LITTLE_ENDIAN_HEX_DEV[value >= 0 ?value :128+value]);
		return address + 4;
	}

	public static final long toString(final long value, final long address)
	{
		if(value < 0){
			return toStringNegative(value, address);
		}
		return toStringPositiveLong(value, address); // normal case
	}

	public static final long toString(final int value, final long address)
	{
		if(value < 0){
			return toStringNegative(value, address);
		}
		return toStringPositiveInt(value, address); // normal case
	}

	public static final long toString(final short value, final long address)
	{
		if(value < 0){
			Memory.set_char(address, '-');
			return toStringPositiveInit5(-value, address + 2);
		}
		return toStringPositiveInit5(value, address); // normal case
	}

	public static final long toString(final byte value, final long address)
	{
		if(value < 0){
			Memory.set_char(address, '-');
			return toStringPositiveInit5(-value, address + 2);
		}
		return toStringPositiveInit5(value, address); // normal case
	}

	public static final long toString(final float value, final long address)
	{
		// (25.02.2013 TM)TODO: Float.toString(): optimize horrible moron-code from JDK
		final String valueString = Float.toString(value);
		Memory.copyString(valueString, address);
		return valueString.length() << 1; // chars are 2 byte long. Not sure if this will ever change
	}

	public static final long toString(final double value, final long address)
	{
		// (25.02.2013 TM)TODO: Double.toString(): optimize horrible moron-code from JDK
		final String valueString = Double.toString(value);
		Memory.copyString(valueString, address);
		return valueString.length() << 1; // chars are 2 byte long. Not sure if this will ever change
	}



	static long toStringNegative(final long value, final long address)
	{
		if(value == Long.MIN_VALUE){ // unnegatable special negative case
			Memory.copyArray(STRING_LONG_MIN_VALUE, address);
			return address + STRING_BYTE_LENGTH_LONG_MIN_VALUE;
		}
		Memory.set_char(address, '-');
		return toStringPositiveLong(-value, address + 2); // standard negative case normalization
	}

	static long toStringNegative(final int value, final long address)
	{
		if(value == Integer.MIN_VALUE){ // unnegatable special negative case
			Memory.copyArray(STRING_INT_MIN_VALUE, address);
			return address + STRING_BYTE_LENGTH_INT_MIN_VALUE;
		}
		Memory.set_char(address, '-');
		return toStringPositiveInt(-value, address + 2); // standard negative case normalization
	}

	private static long toStringPositiveLong(final long value, final long address)
	{
		if(value >= V001G){
			if(value >= V001Z){
				return toStringPositiveHI(value % V001Z, put1Char((int)(value/V001Z), address));
			}
			if(value >= V100P){
				return toStringPositiveHI(value, address);
			}
			if(value >= V010P){
				return toStringPositiveFG(value % V010P, put1Char((int)(value/V010P), address));
			}
			if(value >= V001P){
				return toStringPositiveFG(value, address);
			}
			if(value >= V100T){
				return toStringPositiveDE(value % V100T, put1Char((int)(value/V100T), address));
			}
			if(value >= V010T){
				return toStringPositiveDE(value, address);
			}
			if(value >= V001T){
				return toStringPositiveBC(value % V001T, put1Char((int)(value/V001T), address));
			}
			if(value >= V100G){
				return toStringPositiveBC(value, address);
			}
			if(value >= V010G){
				return toStringPositive9A(value % V010G, put1Char((int)(value/V010G), address));
			}
			return toStringPositive9A(value, address);
		}
		return toStringPositiveInt((int)value, address);
	}

	private static long toStringPositiveInt(final int value, final long address)
	{
		/* notes:
		 * - this algorithm is about twice as fast as the one in JDK in all situations
		 *   (even in worst case range [10000; 100000[)
		 * - using redundant % instead of calculating quotient and rest is suprisingly marginally faster
		 * - any more tinkering in either direction of more inlining or more abstraction
		 *   always yielded worse performance
		 * - a three-digit cache (3x 1000 chars) algorithm might improve assembly performance,
		 *   but complicates the intermediate cases and costs a lot of memory (~6kb)
		 * - a full binary decision tree instead of simple bisection made overall performance worse
		 * - separation in sub-methods surprisingly makes performance a lot worse
		 */
		if(value >= V100K){
			if(value >= V001G){
				return toStringPositive9m(value, address);
			}
			if(value >= V100M){
				return toStringPositive78(value % V100M, put1Char(value/V100M, address));
			}
			if(value >= V010M){
				return toStringPositive78(value, address);
			}
			if(value >= V001M){
				return toStringPositive56(value % V001M, put1Char(value/V001M, address));
			}
			return toStringPositive56(value, address);
		}
		return toStringPositiveInit5(value, address);
	}

	public static long toStringPositiveInit5(final int value, final long address)
	{
		if(value >= V100){
			if(value >= V010K){
				return toStringPositive43(value % V010K, put1Char(value/V010K, address));
			}
			if(value >= V001K){
				return toStringPositive43(value, address);
			}
			return put2Chars(value % V100, put1Char(value/V100, address));
		}
		return toStringPositive21(value, address);
	}

	public static long toStringPositive21(final int value, final long address)
	{
		return value >= V010
			?put2Chars(value, address)
			:put1Char (value, address)
		;
	}

	private static long toStringPositiveHI(final long value, final long address)
	{
		return toStringPositiveFG(value % V010P, put2Chars((int)(value / V010P), address));
	}

	private static long toStringPositiveFG(final long value, final long address)
	{
		return toStringPositiveDE(value % V100T, put2Chars((int)(value / V100T), address));
	}

	private static long toStringPositiveDE(final long value, final long address)
	{
		return toStringPositiveBC(value % V001T, put2Chars((int)(value / V001T), address));
	}

	private static long toStringPositiveBC(final long value, final long address)
	{
		return toStringPositive9A(value % V010G, put2Chars((int)(value / V010G), address));
	}

	private static long toStringPositive9A(final long value, final long address)
	{
		return toStringPositive78((int)(value % V100M), put2Chars((int)(value / V100M), address));
	}

	private static long toStringPositive9m(final int value, final long address)
	{
		return toStringPositive78(value % V100M, put2Chars(value / V100M, address));
	}

	private static long toStringPositive78(final int value, final long address)
	{
		return toStringPositive56(value % V001M, put2Chars(value / V001M, address));
	}

	private static long toStringPositive56(final int value, final long address)
	{
		return toStringPositive43(value % V010K, put2Chars(value / V010K, address));
	}

	private static long toStringPositive43(final int value, final long address)
	{
		return put2Chars(value % V100, put2Chars(value / V100, address));
	}

	private static long put1Char(final int singleDigitValue, final long address)
	{
		Memory.set_char(address, (char)(0x30 + singleDigitValue));
		return address + 2;
	}

	private static long put2Chars(final int doubleDigitValue, final long address)
	{
		Memory.set_int(address, LITTLE_ENDIAN_CHARS_00TO99[doubleDigitValue]);
		return address + 4;
	}

}
