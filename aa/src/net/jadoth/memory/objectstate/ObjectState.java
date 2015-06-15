package net.jadoth.memory.objectstate;

import net.jadoth.Jadoth;
import net.jadoth.memory.Memory;

/**
 * Util class for low-level object state operations.
 *
 * @author Thomas Muenz
 */
public class ObjectState
{
	///////////////////////////////////////////////////////////////////////////
	// class methods  //
	///////////////////

	private static final ObjectValueCopier primValueCopier1 = new ObjectValueCopier(){
		@Override public void copy(final long memoryOffset, final Object instance1, final Object instance2) {
			Memory.set_int(instance2, memoryOffset, Memory.get_int(instance1, memoryOffset));
		}
	};

	private static final ObjectValueCopier primValueCopier2 = new ObjectValueCopier(){
		@Override public void copy(final long memoryOffset, final Object instance1, final Object instance2) {
			Memory.set_short(instance2, memoryOffset, Memory.get_short(instance1, memoryOffset));
		}
	};

	private static final ObjectValueCopier primValueCopier4 = new ObjectValueCopier(){
		@Override public void copy(final long memoryOffset, final Object instance1, final Object instance2) {
			Memory.set_int(instance2, memoryOffset, Memory.get_int(instance1, memoryOffset));
		}
	};

	private static final ObjectValueCopier primValueCopier8 = new ObjectValueCopier(){
		@Override public void copy(final long memoryOffset, final Object instance1, final Object instance2) {
			Memory.set_long(instance2, memoryOffset, Memory.get_long(instance1, memoryOffset));
		}
	};

	private static final ObjectValueCopier referenceCopier = new ObjectValueCopier(){
		@Override public void copy(final long memoryOffset, final Object instance1, final Object instance2) {
			Memory.setObject(instance2, memoryOffset, Memory.getObject(instance1, memoryOffset));
		}
	};

	public static final ObjectValueCopier getValueCopier_byte()
	{
		return primValueCopier1;
	}

	public static final ObjectValueCopier getValueCopier_boolean()
	{
		return primValueCopier1;
	}

	public static final ObjectValueCopier getValueCopier_short()
	{
		return primValueCopier2;
	}

	public static final ObjectValueCopier getValueCopier_char()
	{
		return primValueCopier2;
	}

	public static final ObjectValueCopier getValueCopier_int()
	{
		return primValueCopier4;
	}

	public static final ObjectValueCopier getValueCopier_float()
	{
		return primValueCopier4;
	}

	public static final ObjectValueCopier getValueCopier_long()
	{
		return primValueCopier8;
	}

	public static final ObjectValueCopier getValueCopier_double()
	{
		return primValueCopier8;
	}

	public static final ObjectValueCopier getValueCopierReference()
	{
		return referenceCopier;
	}

	public static ObjectValueCopier getObjectValueCopier(final Class<?> type)
	{
		if(!type.isPrimitive()){
			return referenceCopier;
		}
		switch(Memory.byteSizePrimitive(type)){
			case  1: return primValueCopier1;
			case  2: return primValueCopier2;
			case  4: return primValueCopier4;
			case  8: return primValueCopier8;
			default: throw new IllegalArgumentException();
		}
	}

	public static boolean isEqualObjectGraph(
		final Object object1,
		final Object object2,
		final ObjectStateHandlerLookup instanceStateHandlerLookup
	)
	{
		if(object1 == object2){
			return true;
		}
		if(object1 == null || object2 == null){
			return false;
		}
		return instanceStateHandlerLookup.lookupTypeHandler(Jadoth.getClass(object1)).isEqual(
			object1,
			object2,
			instanceStateHandlerLookup
		);
	}

	public static <T> boolean isEqual(final T t1, final T t2, final ObjectStateHandlerLookup stateHandlerLookup)
	{
		if(t1 == null){
			return t2 == null;
		}
		if(t2 == null || t1.getClass() != t2.getClass()){
			return false;
		}
		return stateHandlerLookup.lookupTypeHandler(Jadoth.getClass(t1)).isEqual(t1, t2, stateHandlerLookup);
	}

	public static <T> boolean isEqual(
		final T source,
		final T target,
		final long[] referenceOffsets,
		final ObjectStateHandlerLookup stateHandlerLookup
	)
	{
		for(int i = 0; i < referenceOffsets.length; i++){
			if(referenceOffsets[i] == 0 || isEqual(
				Memory.getObject(source, referenceOffsets[i]),
				Memory.getObject(target, referenceOffsets[i]),
				stateHandlerLookup
			)){
				continue;
			}
		}
		return true;
	}

	public static boolean isEqual(
		final Object[]                 array1            ,
		final Object[]                 array2            ,
		final int                      offset            ,
		final int                      length            ,
		final ObjectStateHandlerLookup stateHandlerLookup
	)
	{
		final int bound = offset + length;
		for(int i = offset; i < bound; i++){
			if(!isEqual(array1[i], array2[i], stateHandlerLookup)){
				return false;
			}
		}
		return true;
	}

}
