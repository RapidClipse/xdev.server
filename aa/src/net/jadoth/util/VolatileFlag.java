package net.jadoth.util;

import net.jadoth.memory.Memory;
import net.jadoth.reflect.JadothReflect;

public final class VolatileFlag
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	private static final long FIELD_OFFSET_state = Memory.objectFieldOffset(JadothReflect.getInstanceFieldOfType(VolatileFlag.class, int.class));



	///////////////////////////////////////////////////////////////////////////
	// class methods  //
	///////////////////

	public static final VolatileFlag New()
	{
		return new VolatileFlag();
	}

	public static final VolatileFlag New(final boolean state)
	{
		return new VolatileFlag().set(state);
	}



	///////////////////////////////////////////////////////////////////////////
	// instance fields //
	////////////////////

	// note that this causes no memory overhead compared to a boolean as all instances get memory-aligned anyway
	private volatile int state;



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	public final boolean on()
	{
		return !Memory.compareAndSwap_int(this, FIELD_OFFSET_state, 0, 1);
	}

	public final boolean off()
	{
		return Memory.compareAndSwap_int(this, FIELD_OFFSET_state, 1, 0);
	}

	public final VolatileFlag set(final boolean state)
	{
		if(state){
			this.on();
		}
		else {
			this.off();
		}
		return this;
	}

	public final boolean state()
	{
		return this.state != 0;
	}

}
