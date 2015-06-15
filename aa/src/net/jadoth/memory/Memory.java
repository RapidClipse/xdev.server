package net.jadoth.memory;

import static net.jadoth.Jadoth.checkArrayRange;
import static net.jadoth.Jadoth.notNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import net.jadoth.util.VMUtils;
import sun.misc.Cleaner;
import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;


/**
 * Util class for low-level VM memory operations and information that makes the call site independent of
 * a certain JVM implementation (e.g. java.misc.Unsafe).
 *
 * @author Thomas Muenz
 */
public final class Memory
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	private static final Unsafe vm = (Unsafe)VMUtils.getSystemInstance();


	private static final long BABO = Unsafe.ARRAY_BYTE_BASE_OFFSET;
	private static final long ZABO = Unsafe.ARRAY_BOOLEAN_BASE_OFFSET;
	private static final long SABO = Unsafe.ARRAY_SHORT_BASE_OFFSET;
	private static final long CABO = Unsafe.ARRAY_CHAR_BASE_OFFSET;
	private static final long IABO = Unsafe.ARRAY_INT_BASE_OFFSET;
	private static final long FABO = Unsafe.ARRAY_FLOAT_BASE_OFFSET;
	private static final long LABO = Unsafe.ARRAY_LONG_BASE_OFFSET;
	private static final long DABO = Unsafe.ARRAY_DOUBLE_BASE_OFFSET;

	private static final long OABO = Unsafe.ARRAY_OBJECT_BASE_OFFSET;
	private static final int  OAIS = Unsafe.ARRAY_OBJECT_INDEX_SCALE;

	// better calculate it once instead of making wild assumptions that can change (e.g. 64 bit coops has only 12 byte)
	private static final int BYTE_SIZE_OBJECT_HEADER = calculateByteSizeObjectHeader();

	// According to tests and investigation, memory alignment is always 8 bytes, even for 32 bit JVMs.
	private static final int MEMORY_ALIGNMENT_FACTOR =                           8;
	private static final int MEMORY_ALIGNMENT_MODULO = MEMORY_ALIGNMENT_FACTOR - 1;
	private static final int MEMORY_ALIGNMENT_MASK   = ~MEMORY_ALIGNMENT_MODULO   ;

	private static final int  PAGE_SIZE = vm.pageSize();

	private static final long OFFSET_String_value          = internalDetermineFieldOffset(String.class       , "value"      );
	private static final long OFFSET_StringBuilder_value   = internalDetermineFieldOffset(StringBuilder.class, "value"      );
	private static final long OFFSET_StringBuilder_count   = internalDetermineFieldOffset(StringBuilder.class, "count"      );
	private static final long OFFSET_StringBuffer_value    = internalDetermineFieldOffset(StringBuffer.class , "value"      );
	private static final long OFFSET_StringBuffer_count    = internalDetermineFieldOffset(StringBuffer.class , "count"      );
	private static final long OFFSET_ArrayList_elementData = internalDetermineFieldOffset(ArrayList.class    , "elementData");
	private static final long OFFSET_ArrayList_size        = internalDetermineFieldOffset(ArrayList.class    , "size"       );

	private static final Constructor<String> STRING_CONSTRUCTOR_ARRAY_WRAPPER = getConstructorOrNull(String.class, char[].class, boolean.class);


	private static <T> Constructor<T> getConstructorOrNull(final Class<T> c, final Class<?>... parameterTypes)
	{
		try
		{
			final Constructor<T> ctor = c.getDeclaredConstructor(parameterTypes);
			ctor.setAccessible(true);
			return ctor;
		}
		catch(final Exception e)
		{
			return null;
		}
	}

	public static long objectFieldOffset(final Field field)
	{
		return vm.objectFieldOffset(field);
	}

	private static long internalDetermineFieldOffset(final Class<?> type, final String declaredFieldName)
	{
		// minimal algorithm, only for local use
		for(Class<?> c = type; c != null && c != Object.class; c = c.getSuperclass())
		{
			try
			{
				for(final Field field : c.getDeclaredFields())
				{
					if(field.getName().equals(declaredFieldName))
					{
						return vm.objectFieldOffset(field);
					}
				}
			}
			catch(final Exception e)
			{
				throw new Error(e); // explode and die :)
			}
		}
		throw new Error("Field not found: "+type.getName()+'#'+declaredFieldName);
	}

	public static final Object getStaticReference(final Field field)
	{
		if(!Modifier.isStatic(field.getModifiers()))
		{
			throw new IllegalArgumentException();
		}
		return vm.getObject(vm.staticFieldBase(field), vm.staticFieldOffset(field));
	}

	/**
	 * No idea if this method is really (still?) necesssary, but it sounds reasonable.
	 * See http://stackoverflow.com/questions/8462200/examples-of-forcing-freeing-of-native-memory-direct-bytebuffer-has-allocated-us
	 *
	 * @param directByteBuffer
	 */
	public static final void deallocateDirectByteBuffer(final ByteBuffer directByteBuffer)
	{
		// memory handling of this burrowedly tinkered buffer is a real P in the A.
	    if(!(directByteBuffer instanceof DirectBuffer))
	    {
	    	return;
	    }
	    final Cleaner cleaner = ((DirectBuffer)directByteBuffer).cleaner();
	    if(cleaner != null)
	    {
	    	cleaner.clean();
	    }
	}

	/**
	 * Just to encapsulate that clumsy cast.
	 *
	 * @param directByteBuffer
	 * @return
	 */
	public static final long directByteBufferAddress(final ByteBuffer directByteBuffer)
	{
		return ((DirectBuffer)directByteBuffer).address();
	}

	public static final ByteBuffer ensureDirectByteBufferCapacity(final ByteBuffer current, final long capacity)
	{
		if(current.capacity() >= capacity)
		{
			return current;
		}
		checkArrayRange(capacity);
		deallocateDirectByteBuffer(current);
		return ByteBuffer.allocateDirect((int)capacity);
	}


	/**
	 * This method is ONLY meant as a dirty shortcut to create String instances without the performance
	 * penalty of unnecessarily copying the char array.
	 * <p>
	 * Notes:<ol>
	 * <li>It can very well corrupt the string if the passed char array is altered after the string
	 * has been created.</li>
	 * <li>For short char sequences (around < 20 chars), the performed reflective operation
	 * may be slower than the normal copying constructor.</li>
	 * <li>Unrepresentative performance measuring to give a rough estimate:<br>
	 * - Standard copying constructor required statistically 5 to 7 ns for char array lengths from 0 to 20.<br>
	 * - Reflective wrapper instantiation required always statistically 7 ns, independent of array length.</li>
	 * <li>The name is intentionally verbose and clumsy to avoid the impression that this is some kind
	 * of convenient fluent syntax helper method.</li>
	 * <li>This method uses the standard char array copying constructor as a fallback on any {@link Throwable},
	 * so this method does never fail but only get slow.</li>
	 * </ol>
	 * <p>
	 * Conclusion: only use this method if you know what you're doing!
	 *
	 * @param chars the array to be used as the new {@link String} instance's internal storage value.
	 * @return a new {@link String} instance using the passed array as its internal storage value.
	 */
	public static final String wrapCharsAsString(final char... chars)
	{
		try
		{
			// caching the zero Integer showed no difference in performance. Maybe done by compiler anyway.
			return STRING_CONSTRUCTOR_ARRAY_WRAPPER.newInstance(chars, Boolean.TRUE);
		}
		catch(final Exception t)
		{
			// use standard copy-constructor as fallback because this method may not fail.
			return new String(chars);
		}
	}

	/**
	 * Access the normally unshared char array backing the passed {@link String} instance.
	 * While not the cleanest way to write code, this can be necessary at times for performance reasons
	 * in a well-controlled algorithm.
	 * <p>
	 * Warning:<br>
	 * This method is intended strictly for read-only purposes!<br>
	 * It also is strongly discouraged (if not "forbidden") to keep a reference to the accessed char array in
	 * a fashion that makes it accessible to long-lived and/or instances outside the control of the context
	 * calling this method.<br>
	 * In short: One must be sure to know what one is doing (including sufficient knowledge of the Java memory model)
	 * when using this method.<br>
	 * As a rule of thumb: accessing the char array in a local block (e.g. method), reading it and letting the
	 * reference run out of scope is a safe way to use this method.
	 *
	 * @param string the {@link String} instance whose backing char array.
	 * @return the internal char array of the passed {@link String} instance.
	 */
	public static char[] accessChars(final String string)
	{
		// must check not null here explictely to prevent VM crashes
		return (char[])vm.getObject(notNull(string), OFFSET_String_value);
	}

	public static char[] accessChars(final StringBuilder stringBuilder)
	{
		// must check not null here explictely to prevent VM crashes
		return (char[])vm.getObject(notNull(stringBuilder), OFFSET_StringBuilder_value);
	}

	public static void setData(final StringBuilder stringBuilder, final char[] array, final long offset, final long length)
	{
		notNull(stringBuilder); // must check not null here explictely to prevent VM crashes
		if((length & 1) == 1)
		{
			throw new RuntimeException("Invalid odd byte length for char array");
		}

		final char[] data = (char[])vm.getObject(stringBuilder, OFFSET_StringBuilder_value);
		if(data.length < (int)(length>>>1))
		{
			throw new RuntimeException("Not enough capacity");
		}

		vm.copyMemory(array, offset, data, Unsafe.ARRAY_CHAR_BASE_OFFSET, length);
		vm.putInt(stringBuilder, OFFSET_StringBuilder_count, (int)(length>>>1));
	}

	public static void setData(final StringBuffer stringBuffer, final char[] array, final long offset, final long length)
	{
		notNull(stringBuffer); // must check not null here explictely to prevent VM crashes
		if((length & 1) == 1)
		{
			throw new RuntimeException("Invalid odd byte length for char array");
		}

		final char[] data = (char[])vm.getObject(stringBuffer, OFFSET_StringBuffer_value);
		if(data.length < (int)(length>>>1))
		{
			throw new RuntimeException("Not enough capacity");
		}

		vm.copyMemory(array, offset, data, Unsafe.ARRAY_CHAR_BASE_OFFSET, length);
		vm.putInt(stringBuffer, OFFSET_StringBuffer_count, (int)(length>>>1));
	}

	public static char[] accessChars(final StringBuffer stringBuffer)
	{
		// must check not null here explictely to prevent VM crashes
		return (char[])vm.getObject(notNull(stringBuffer), OFFSET_StringBuffer_value);
	}

	public static Object[] accessStorage(final ArrayList<?> arrayList)
	{
		// must check not null here explictely to prevent VM crashes
		return (Object[])vm.getObject(notNull(arrayList), OFFSET_ArrayList_elementData);
	}

	public static void setSize(final ArrayList<?> arrayList, final int size)
	{
		// must check not null here explictely to prevent VM crashes
		vm.putInt(notNull(arrayList), OFFSET_ArrayList_size, size);
	}


	///////////////////////////////////////////////////////////////////////////
	// memory byte size methods //
	/////////////////////////////

	public static final int byteSizeFieldValue(final Class<?> type)
	{
		return type.isPrimitive() ?byteSizePrimitive(type) :byteSizeReference();
	}

	public static final int byteSizePrimitive(final Class<?> type)
	{
		// roughly ordered by probability
		if(type == int.class || type == float.class)
		{
			return 4;
		}
		if(type == long.class || type == double.class)
		{
			return 8;
		}
		if(type == char.class || type == short.class)
		{
			return 2;
		}
		if(type == boolean.class || type == byte.class)
		{
			return 1;
		}
		throw new IllegalArgumentException(); // intentionally covers to void.class
	}

	public static final int byteSize_byte()
	{
		return Unsafe.ARRAY_BYTE_INDEX_SCALE;
	}

	public static final int byteSize_boolean()
	{
		return Unsafe.ARRAY_BOOLEAN_INDEX_SCALE;
	}

	public static final int byteSize_short()
	{
		return Unsafe.ARRAY_SHORT_INDEX_SCALE;
	}

	public static final int byteSize_char()
	{
		return Unsafe.ARRAY_CHAR_INDEX_SCALE;
	}

	public static final int byteSize_int()
	{
		return Unsafe.ARRAY_INT_INDEX_SCALE;
	}

	public static final int byteSize_float()
	{
		return Unsafe.ARRAY_FLOAT_INDEX_SCALE;
	}

	public static final int byteSize_long()
	{
		return Unsafe.ARRAY_LONG_INDEX_SCALE;
	}

	public static final int byteSize_double()
	{
		return Unsafe.ARRAY_DOUBLE_INDEX_SCALE;
	}

	public static final int byteSizeObjectHeader()
	{
		return BYTE_SIZE_OBJECT_HEADER;
	}

	public static final int byteSizeReference()
	{
		return Unsafe.ARRAY_OBJECT_INDEX_SCALE;
	}

	public static final long byteSizeArray_byte(final long elementCount)
	{
		return Unsafe.ARRAY_BYTE_BASE_OFFSET + Unsafe.ARRAY_BYTE_INDEX_SCALE * elementCount;
	}

	public static final long byteSizeArray_boolean(final long elementCount)
	{
		return Unsafe.ARRAY_BOOLEAN_BASE_OFFSET + Unsafe.ARRAY_BOOLEAN_INDEX_SCALE * elementCount;
	}

	public static final long byteSizeArray_short(final long elementCount)
	{
		return Unsafe.ARRAY_SHORT_BASE_OFFSET + Unsafe.ARRAY_SHORT_INDEX_SCALE * elementCount;
	}

	public static final long byteSizeArray_char(final long elementCount)
	{
		return Unsafe.ARRAY_CHAR_BASE_OFFSET + Unsafe.ARRAY_CHAR_INDEX_SCALE * elementCount;
	}

	public static final long byteSizeArray_int(final long elementCount)
	{
		return Unsafe.ARRAY_INT_BASE_OFFSET + Unsafe.ARRAY_INT_INDEX_SCALE * elementCount;
	}

	public static final long byteSizeArray_float(final long elementCount)
	{
		return Unsafe.ARRAY_FLOAT_BASE_OFFSET + Unsafe.ARRAY_FLOAT_INDEX_SCALE * elementCount;
	}

	public static final long byteSizeArray_long(final long elementCount)
	{
		return Unsafe.ARRAY_LONG_BASE_OFFSET + Unsafe.ARRAY_LONG_INDEX_SCALE * elementCount;
	}

	public static final long byteSizeArray_double(final long elementCount)
	{
		return Unsafe.ARRAY_DOUBLE_BASE_OFFSET + Unsafe.ARRAY_DOUBLE_INDEX_SCALE * elementCount;
	}

	public static final long byteSizeArrayObject(final long elementCount)
	{
		return Unsafe.ARRAY_OBJECT_BASE_OFFSET + Unsafe.ARRAY_OBJECT_INDEX_SCALE * elementCount;
	}

	private static final int calculateByteSizeObjectHeader()
	{
		// min logic should be unnecessary but better exclude any source for potential errors
		long minOffset = Long.MAX_VALUE;
		final Field[] declaredFields = Memory.class.getDeclaredFields();
		for(final Field field : declaredFields)
		{
			if(Modifier.isStatic(field.getModifiers()))
			{
				continue;
			}
			if(vm.objectFieldOffset(field) < minOffset)
			{
				minOffset = vm.objectFieldOffset(field);
			}
		}
		if(minOffset == Long.MAX_VALUE)
		{
			throw new Error("Could not find object header dummy field in class "+Memory.class);
		}
		return (int)minOffset; // offset of first instance field is guaranteed to be in int range ^^.
	}

	public static final int byteSizeInstance(final Class<?> type)
	{
		if(type.isPrimitive())
		{
			throw new IllegalArgumentException();
		}
		if(type.isArray())
		{
			// instance byte size accounts only array header (object header plus length field plus overhead)
			return vm.arrayBaseOffset(type);
		}
		if(type == Object.class)
		{
			// required because Object's super class is null (see below)
			return byteSizeObjectHeader();
		}

		// declared fields suffice as all super class fields are positioned before them
		final Field[] declaredFields = type.getDeclaredFields();
		long maxInstanceFieldOffset = 0;
		Field maxInstanceField = null;
		for(int i = 0; i < declaredFields.length; i++)
		{
			if(Modifier.isStatic(declaredFields[i].getModifiers()))
			{
				continue;
			}
			final long fieldOffset = vm.objectFieldOffset(declaredFields[i]);
//			JadothConsole.debugln(fieldOffset+"\t"+declaredFields[i]);
			if(fieldOffset >= maxInstanceFieldOffset)
			{
				maxInstanceField = declaredFields[i];
				maxInstanceFieldOffset = fieldOffset;
			}
		}

		// no declared instance field at all, fall back to super class fields recursively
		if(maxInstanceField == null)
		{
			return byteSizeInstance(type.getSuperclass());
		}

		// memory alignment is a "wild" assumption at this point. One that will hopefully always be true. Otherwise it's a bug.
		return (int)alignAddress(maxInstanceFieldOffset + byteSizeFieldValue(maxInstanceField.getType()));
	}

	public static final long alignAddress(final long address)
	{
		if((address & MEMORY_ALIGNMENT_MODULO) == 0)
		{
			return address; // already aligned
		}
		// According to tests and investigation, memory alignment is always 8 bytes, even for 32 bit JVMs.
		return (address & MEMORY_ALIGNMENT_MASK) + MEMORY_ALIGNMENT_FACTOR;
	}

	public static final void ensureClassInitialized(final Class<?>... classes)
	{
		for(final Class<?> c : classes)
		{
			ensureClassInitialized(c);
		}
	}

	public static final void ensureClassInitialized(final Class<?> c)
	{
		vm.ensureClassInitialized(notNull(c));
	}

	@SuppressWarnings("unchecked")
	public static final <T> T instantiate(final Class<T> c) throws InstantiationException
	{
		return (T)vm.allocateInstance(notNull(c));
	}

	public static final <T, E extends T> void volatileArraySet(final T[] array, final int index, final E element)
		throws ArrayIndexOutOfBoundsException
	{
		if(index < 0 || index > array.length)
		{
			// implicit NPE
			throw new ArrayIndexOutOfBoundsException(index);
		}
		vm.putObjectVolatile(array, OABO + OAIS*index, element);
	}

	public static Field[] collectPrimitiveFieldsByByteSize(final Field[] fields, final int byteSize)
	{
		if(byteSize != 1 && byteSize != 2 && byteSize != 4 && byteSize != 8)
		{
			throw new IllegalArgumentException("Invalid Java primitive byte size: "+byteSize);
		}

		final Field[] primFields = new Field[fields.length];
		int primFieldsCount = 0;
		for(int i = 0; i < fields.length; i++)
		{
			if(fields[i].getType().isPrimitive() && Memory.byteSizePrimitive(fields[i].getType()) == byteSize)
			{
				primFields[primFieldsCount++] = fields[i];
			}
		}
		return Arrays.copyOf(primFields, primFieldsCount);
	}

	public static int calculatePrimitivesLength(final Field[] primFields)
	{
		int length = 0;
		for(int i = 0; i < primFields.length; i++)
		{
			if(!primFields[i].getType().isPrimitive())
			{
				throw new IllegalArgumentException("Not a primitive field: "+primFields[i]);
			}
			length += Memory.byteSizePrimitive(primFields[i].getType());
		}
		return length;
	}

	public static Object getStaticFieldBase(final Field field)
	{
		return vm.staticFieldBase(notNull(field)); // throws IllegalArgumentException, so no need to check here
	}

	public static long[] objectFieldOffsets(final Field[] fields)
	{
		final long[] offsets = new long[fields.length];
		for(int i = 0; i < fields.length; i++)
		{
			if(Modifier.isStatic(fields[i].getModifiers()))
			{
				throw new IllegalArgumentException("Not an instance field: "+fields[i]);
			}
			offsets[i] = (int)vm.objectFieldOffset(fields[i]);
		}
		return offsets;
	}

	public static long[] staticFieldOffsets(final Field[] fields)
	{
		final long[] offsets = new long[fields.length];
		for(int i = 0; i < fields.length; i++)
		{
			if(!Modifier.isStatic(fields[i].getModifiers()))
			{
				throw new IllegalArgumentException("Not a static field: "+fields[i]);
			}
			offsets[i] = (int)vm.staticFieldOffset(fields[i]);
		}
		return offsets;
	}

	public static byte[] toByteArray(final long[] longArray)
	{
		final byte[] bytes = new byte[checkArrayRange((long)longArray.length << 3)];
		vm.copyMemory(longArray, Unsafe.ARRAY_LONG_BASE_OFFSET, bytes, Unsafe.ARRAY_BYTE_BASE_OFFSET, bytes.length);
		return bytes;
	}

	public static int pageSize()
	{
		return PAGE_SIZE;
	}

	public static byte[] pageSizeBuffer()
	{
		return new byte[pageSize()];
	}

	public static void putShort(final byte[] bytes, final int index, final short value)
	{
		vm.putShort(bytes, (long)Unsafe.ARRAY_BYTE_BASE_OFFSET + index, value);
	}

	public static void putChar(final byte[] bytes, final int index, final char value)
	{
		vm.putChar(bytes, (long)Unsafe.ARRAY_BYTE_BASE_OFFSET + index, value);
	}

	public static void putInt(final byte[] bytes, final int index, final int value)
	{
		vm.putInt(bytes, (long)Unsafe.ARRAY_BYTE_BASE_OFFSET + index, value);
	}

	public static void putFloat(final byte[] bytes, final int index, final float value)
	{
		vm.putFloat(bytes, (long)Unsafe.ARRAY_BYTE_BASE_OFFSET + index, value);
	}

	public static void putLong(final byte[] bytes, final int index, final long value)
	{
		vm.putLong(bytes, (long)Unsafe.ARRAY_BYTE_BASE_OFFSET + index, value);
	}

	public static void putDouble(final byte[] bytes, final int index, final double value)
	{
		vm.putDouble(bytes, (long)Unsafe.ARRAY_BYTE_BASE_OFFSET + index, value);
	}

	public static void _longInByteArray(final byte[] bytes, final long value)
	{
		vm.putLong(bytes, (long)Unsafe.ARRAY_BYTE_BASE_OFFSET, value);
	}

	public static long _longFromByteArray(final byte[] bytes)
	{
		return vm.getLong(bytes, (long)Unsafe.ARRAY_BYTE_BASE_OFFSET);
	}



	public static final byte get_byte(final long address)
	{
		return vm.getByte(address);
	}

	public static final boolean get_boolean(final long address)
	{
		return vm.getBoolean(null, address);
	}

	public static final short get_short(final long address)
	{
		return vm.getShort(address);
	}

	public static final char get_char(final long address)
	{
		return vm.getChar(address);
	}

	public static final int get_int(final long address)
	{
		return vm.getInt(address);
	}

	public static final float get_float(final long address)
	{
		return vm.getFloat(address);
	}

	public static final long get_long(final long address)
	{
		return vm.getLong(address);
	}

	public static final double get_double(final long address)
	{
		return vm.getDouble(address);
	}

	public static final Object getObject(final long address)
	{
		return vm.getObject(null, address);
	}


	public static final byte get_byte(final Object instance, final long address)
	{
		return vm.getByte(instance, address);
	}

	public static final boolean get_boolean(final Object instance, final long address)
	{
		return vm.getBoolean(instance, address);
	}

	public static final short get_short(final Object instance, final long address)
	{
		return vm.getShort(instance, address);
	}

	public static final char get_char(final Object instance, final long address)
	{
		return vm.getChar(instance, address);
	}

	public static final int get_int(final Object instance, final long address)
	{
		return vm.getInt(instance, address);
	}

	public static final float get_float(final Object instance, final long address)
	{
		return vm.getInt(instance, address);
	}

	public static final long get_long(final Object instance, final long address)
	{
		return vm.getLong(instance, address);
	}

	public static final double get_double(final Object instance, final long address)
	{
		return vm.getInt(instance, address);
	}

	public static final Object getObject(final Object instance, final long address)
	{
		return vm.getObject(instance, address);
	}




	public static final void set_byte(final long address, final byte value)
	{
		vm.putByte(address, value);
	}

	public static final void set_boolean(final long address, final boolean value)
	{
		vm.putBoolean(null, address, value); // why is the 2-arg variant missing?
	}

	public static final void set_short(final long address, final short value)
	{
		vm.putShort(address, value);
	}

	public static final void set_char(final long address, final char value)
	{
		vm.putChar(address, value);
	}

	public static final void set_int(final long address, final int value)
	{
		vm.putInt(address, value);
	}

	public static final void set_float(final long address, final float value)
	{
		vm.putFloat(address, value);
	}

	public static final void set_long(final long address, final long value)
	{
		vm.putLong(address, value);
	}

	public static final void set_double(final long address, final double value)
	{
		vm.putDouble(address, value);
	}

	public static final void set_byte(final Object instance, final long offset, final byte value)
	{
		vm.putByte(instance, offset, value);
	}

	public static final void set_boolean(final Object instance, final long offset, final boolean value)
	{
		vm.putBoolean(instance, offset, value);
	}

	public static final void set_short(final Object instance, final long offset, final short value)
	{
		vm.putShort(instance, offset, value);
	}

	public static final void set_char(final Object instance, final long offset, final char value)
	{
		vm.putChar(instance, offset, value);
	}

	public static final void set_int(final Object instance, final long offset, final int value)
	{
		vm.putInt(instance, offset, value);
	}

	public static final void set_float(final Object instance, final long offset, final float value)
	{
		vm.putFloat(instance, offset, value);
	}

	public static final void set_long(final Object instance, final long offset, final long value)
	{
		vm.putLong(instance, offset, value);
	}

	public static final void set_double(final Object instance, final long offset, final double value)
	{
		vm.putDouble(instance, offset, value);
	}

	public static final void setObject(final Object instance, final long offset, final Object value)
	{
		vm.putObject(instance, offset, value);
	}

	public static final void copyRange(final long sourceAddress, final long targetAddress, final long length)
	{
		vm.copyMemory(sourceAddress, targetAddress, length);
	}

	public static final void copyRange(
		final Object source,
		final long   sourceOffset,
		final Object target,
		final long   targetOffset,
		final long   length
	)
	{
		vm.copyMemory(source, sourceOffset, target, targetOffset, length);
	}

	public static final void copyRangeToArray(final long address, final byte[] target)
	{
		vm.copyMemory(null, address, target, Unsafe.ARRAY_BYTE_BASE_OFFSET, target.length);
	}

	public static final void copyRangeToArray(final long address, final char[] target)
	{
		vm.copyMemory(null, address, target, Unsafe.ARRAY_CHAR_BASE_OFFSET, target.length << 1);
	}

	public static final void copyRangeToArray(final long address, final long[] target)
	{
		vm.copyMemory(null, address, target, Unsafe.ARRAY_LONG_BASE_OFFSET, target.length << 3);
	}

	public static final void copyRangeToArray(
		final long   address,
		final byte[] target,
		final int    targetIndex,
		final long   length
	)
	{
		vm.copyMemory(null, address, target, Unsafe.ARRAY_BYTE_BASE_OFFSET + targetIndex, length);
	}

	public static final void copyArrayToAddress(final byte[] array, final long address)
	{
		vm.copyMemory(array, Unsafe.ARRAY_BYTE_BASE_OFFSET, null, address, array.length);
	}

	public static final byte[] directByteBufferToArray(final ByteBuffer directByteBuffer)
	{
		final byte[] bytes;
		copyRangeToArray(
			directByteBufferAddress(directByteBuffer),
			bytes = new byte[directByteBuffer.limit()]
		);
		return bytes;
	}

	public static final void copyArray(
		final char[] source       ,
		final long   targetAddress,
		final int    sourceOffset ,
		final int    sourceCount
	)
	{
		vm.copyMemory(
			source,
			Unsafe.ARRAY_CHAR_BASE_OFFSET + (long)sourceOffset<<1,
			null,
			targetAddress,
			(long)sourceCount<<1
		);
	}

	public static final void copyArray(final byte[] array, final long targetAddress)
	{
		vm.copyMemory(array, Unsafe.ARRAY_BYTE_BASE_OFFSET, null, targetAddress, array.length);
	}

	public static final void copyArray(final byte[] array, final int offset, final int length, final long targetAddress)
	{
		vm.copyMemory(array, Unsafe.ARRAY_BYTE_BASE_OFFSET + offset, null, targetAddress, length);
	}

	public static final void copyArray(final boolean[] array, final int offset, final int length, final long targetAddress)
	{
		vm.copyMemory(array, Unsafe.ARRAY_BOOLEAN_BASE_OFFSET + offset, null, targetAddress, length);
	}

	public static final void copyArray(final short[] array, final int offset, final int length, final long targetAddress)
	{
		vm.copyMemory(array, Unsafe.ARRAY_SHORT_BASE_OFFSET + (offset<<1), null, targetAddress, length << 1);
	}

	public static final void copyArray(final char[] array, final long targetAddress)
	{
		vm.copyMemory(array, Unsafe.ARRAY_CHAR_BASE_OFFSET, null, targetAddress, array.length << 1);
	}

	public static final void copyArray(final char[] array, final int offset, final int length, final long targetAddress)
	{
		vm.copyMemory(array, Unsafe.ARRAY_CHAR_BASE_OFFSET + (offset<<1), null, targetAddress, length << 1);
	}

	public static final void copyArray(final int[] array, final int offset, final int length, final long targetAddress)
	{
		vm.copyMemory(array, Unsafe.ARRAY_INT_BASE_OFFSET + (offset<<2), null, targetAddress, length << 2);
	}

	public static final void copyArray(final float[] array, final int offset, final int length, final long targetAddress)
	{
		vm.copyMemory(array, Unsafe.ARRAY_FLOAT_BASE_OFFSET + (offset<<2), null, targetAddress, length << 2);
	}

	public static final void copyArray(final long[] array, final int offset, final int length, final long targetAddress)
	{
		vm.copyMemory(array, Unsafe.ARRAY_LONG_BASE_OFFSET + (offset<<3), null, targetAddress, length << 3);
	}

	public static final void copyArray(final double[] array, final int offset, final int length, final long targetAddress)
	{
		vm.copyMemory(array, Unsafe.ARRAY_DOUBLE_BASE_OFFSET + (offset<<3), null, targetAddress, length << 3);
	}

	public static final void copyString(final String string, final long targetAddress)
	{
		vm.copyMemory(
			accessChars(string),
			Unsafe.ARRAY_CHAR_BASE_OFFSET,
			null,
			targetAddress,
			string.length() * Unsafe.ARRAY_CHAR_INDEX_SCALE
		);
	}


	public static final byte get_byte(final byte[] data, final int offset)
	{
		return vm.getByte(data, BABO + offset);
	}

	public static final boolean get_boolean(final byte[] data, final int offset)
	{
		return vm.getBoolean(data, ZABO + offset);
	}

	public static final short get_short(final byte[] data, final int offset)
	{
		return vm.getShort(data, SABO + offset);
	}

	public static final char get_char(final byte[] data, final int offset)
	{
		return vm.getChar(data, CABO + offset);
	}

	public static final int get_int(final byte[] data, final int offset)
	{
		return vm.getInt(data, IABO + offset);
	}

	public static final float get_float(final byte[] data, final int offset)
	{
		return vm.getFloat(data, FABO + offset);
	}

	public static final long get_longFromBytes(final byte[] data, final int offset)
	{
		return vm.getLong(data, LABO + offset);
	}

	public static final double get_double(final byte[] data, final int offset)
	{
		return vm.getDouble(data, DABO + offset);
	}

	public static final long allocate(final long bytes)
	{
		return vm.allocateMemory(bytes);
	}

	public static final long reallocate(final long address, final long bytes)
	{
		return vm.reallocateMemory(address, bytes);
	}

	public static final void free(final long address)
	{
		vm.freeMemory(address);
	}

	public static final boolean compareAndSwap_int(
		final Object subject    ,
		final long   offset     ,
		final int    expected   ,
		final int    replacement
	)
	{
		return vm.compareAndSwapInt(subject, offset, expected, replacement);
	}

	public static final boolean compareAndSwap_long(
		final Object subject    ,
		final long   offset     ,
		final long   expected   ,
		final long   replacement
	)
	{
		return vm.compareAndSwapLong(subject, offset, expected, replacement);
	}

	public static final boolean compareAndSwapObject(
		final Object subject    ,
		final long   offset     ,
		final Object expected   ,
		final Object replacement
	)
	{
		return vm.compareAndSwapObject(subject, offset, expected, replacement);
	}

	public static final void volatileSetObject(
		final Object subject,
		final long   offset ,
		final Object value
	)
	{
		vm.putObjectVolatile(subject, offset, value);
	}



	Object fieldOffsetWorkaroundDummy;

	private Memory()
	{
		throw new Error();
	}

}
