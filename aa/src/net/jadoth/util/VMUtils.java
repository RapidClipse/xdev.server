package net.jadoth.util;

import java.lang.reflect.Field;

import net.jadoth.memory.Memory;
import sun.misc.Unsafe;


/**
 * Non-memory-related vm-level operations like unchecked {@link Throwable} throwing or lock handling.
 *
 * @see Memory
 *
 * @author Thomas Muenz
 */
public final class VMUtils
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	private static final Unsafe vm = (Unsafe)getSystemInstance();



	///////////////////////////////////////////////////////////////////////////
	// class methods    //
	/////////////////////

	// return type not specified to avoid public API dependencies to sun implementation details
	public static final Object getSystemInstance()
	{
		// all that clumsy detour ... x_x
		if(VMUtils.class.getClassLoader() == null){
			return Unsafe.getUnsafe(); // Not on bootclasspath
		}
		try {
			final Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			return theUnsafe.get(VMUtils.class);
		}
		catch(final Exception e) {
			throw new Error("Could not obtain access to sun.misc.Unsafe", e);
		}
	}

	public static final void throwUnchecked(final Throwable t)
	{
		vm.throwException(t);
	}



	private VMUtils(){ throw new UnsupportedOperationException(); }
}
