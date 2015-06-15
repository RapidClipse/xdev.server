package net.jadoth.hash;

import net.jadoth.util.JadothTypes;
import net.jadoth.util.KeyValue;


/**
 * Utility methods and centrally defined function instances related to hashing.
 *
 * @author Thomas Muenz
 */
public final class JadothHash
{
	/* it is important for the singleton to have its own proper named type
	 * to make the type indifferent to declaration order, e.g. for purposes of persisting metadata.
	 */
	static final class SingletonIdentityHashEqualator implements HashEqualator.IdentityHashEqualator<Object>
	{
		SingletonIdentityHashEqualator()
		{
			super();
		}

		@Override
		public final int hash(final Object object)
		{
			return System.identityHashCode(object);
		}

		@Override
		public final boolean equal(final Object object1, final Object object2)
		{
			return object1 == object2;
		}
	}

	static final class SingletonValueHashEqualator implements HashEqualator.IdentityHashEqualator<Object>
	{
		SingletonValueHashEqualator()
		{
			super();
		}

		@Override
		public final int hash(final Object object)
		{
			return object == null ?0 :object.hashCode();
		}

		@Override
		public final boolean equal(final Object object1, final Object object2)
		{
			return object1 == object2 || object1 != null && object1.equals(object2);
		}
	}

	static final class SingletonKeyValueIdentityHashEqualator implements HashEqualator.IdentityHashEqualator<KeyValue<Object, Object>>
	{
		SingletonKeyValueIdentityHashEqualator()
		{
			super();
		}

		@Override
		public final int hash(final KeyValue<Object, Object> kv)
		{
			return System.identityHashCode(kv.key());
		}

		@Override
		public final boolean equal(final KeyValue<Object, Object> kv1, final KeyValue<Object, Object> kv2)
		{
			return kv1.key() == kv2.key();
		}
	}


	/**
	 * Central stateless {@link HashEqualator} function instance with identity hash equality.
	 */
	static final SingletonIdentityHashEqualator HASH_EQUALITY_IDENTITY = new SingletonIdentityHashEqualator();

	/**
	 * Central stateless {@link HashEqualator} function instance with
	 * {@link Object#hashCode()} / {@link Object#equals(Object)} equlity (value type equality).
	 */
	static final SingletonValueHashEqualator HASH_EQUALITY_VALUE = new SingletonValueHashEqualator();

	static final SingletonKeyValueIdentityHashEqualator HASH_EQUALITY_IDENTITY_KV = new SingletonKeyValueIdentityHashEqualator();

	@SuppressWarnings("unchecked")
	public static final <E> HashEqualator<E> hashEqualityIdentity()
	{
		return (HashEqualator<E>)HASH_EQUALITY_IDENTITY;
	}

	@SuppressWarnings("unchecked")
	public static final <E> HashEqualator<E> hashEqualityValue()
	{
		return (HashEqualator<E>)HASH_EQUALITY_VALUE;
	}

	@SuppressWarnings("unchecked")
	public static final <KV extends KeyValue<?, ?>> HashEqualator<KV> keyValueHashEqualityKeyIdentity()
	{
		return (HashEqualator<KV>)HASH_EQUALITY_IDENTITY_KV;
	}

	private static String exceptionHashDensity(final float hashDensity)
	{
		return "Illegal hash density: " + hashDensity;
	}

	public static final int padHashLength(final int minimalHashLength)
	{
		if(minimalHashLength > 1<<30){ // (technical) magic value
			return Integer.MAX_VALUE;
		}
		int capacity = 1;
		while(capacity < minimalHashLength){
			capacity <<= 1;
		}
		return capacity;
	}

	public static final float hashDensity(final float hashDensity)
	{
		if(hashDensity <= 0 || Float.isNaN(hashDensity)){
			throw new IllegalArgumentException(exceptionHashDensity(hashDensity));
		}
		return hashDensity;
	}

	/**
	 * Primitive long value hash code as defined in {@link Long#hashCode()} because they forgot to provide it
	 * statically (as usual).
	 *
	 * @param value the value to be hashed.
	 * @return the int hash of the passed value according to {@link Long#hashCode()}.
	 */
	public static final int hashCode(final long value)
	{
		return (int)(value ^ value >>> 32);
	}

	/**
	 * Primitive double value hash code as defined in {@link Double#hashCode()} because they forgot to provide it
	 * statically (as usual).
	 *
	 * @param value the value to be hashed.
	 * @return the int hash of the passed value according to {@link Double#hashCode()}.
	 */
	public static final int hashCode(final double value)
	{
		// why does one have to write half the JDK by oneself if one wants to write proper software... *sigh*
		final long bits = Double.doubleToLongBits(value);
		return (int)(bits ^ bits >>> 32);
	}

	/**
	 * Wrappers the passed {@link HashEqualator} instance as a {@link KeyValue} {@link HashEqualator} instance, using
	 * the {@link KeyValue} instance's key as the hash element.
	 *
	 * @param hashEqualator single element {@link HashEqualator} instance to be wrapped.
	 * @return the passed instance as a {@link KeyValue} {@link HashEqualator} instance.
	 */
	public static final <K, V> HashEqualator<KeyValue<K, V>> wrapAsKeyValue(final HashEqualator<? super K> hashEqualator)
	{
		return new HashEqualator<KeyValue<K, V>>(){
			@Override
			public int hash(final KeyValue<K, V> kv)
			{
				return kv == null ?0 :hashEqualator.hash(kv.key());
			}

			@Override
			public boolean equal(final KeyValue<K, V> kv1, final KeyValue<K, V> kv2)
			{
				return kv1 != null && kv2 != null && hashEqualator.equal(kv1.key(), kv2.key());
			}
		};
	}

	public static final <E> HashEqualator<E> determineHashEquality(final Class<E> type)
	{
		return JadothTypes.isValueType(type)
			?JadothHash.<E>hashEqualityValue()
			:JadothHash.<E>hashEqualityIdentity()
		;
	}



	private JadothHash() { throw new UnsupportedOperationException(); } // static only
}
