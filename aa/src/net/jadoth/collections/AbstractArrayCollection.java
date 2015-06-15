package net.jadoth.collections;

import net.jadoth.Jadoth;
import net.jadoth.collections.interfaces.Sized;
import net.jadoth.math.JadothMath;


/**
 * @author Thomas Muenz
 *
 */
public abstract class AbstractArrayCollection<E> extends AbstractExtendedCollection<E> implements Sized
{
	///////////////////////////////////////////////////////////////////////////
	// constants        //
	/////////////////////

	// internal marker object for marking to be removed slots for batch removal and null ambiguity resolution
	private static final transient Object MARKER = new Object();



	///////////////////////////////////////////////////////////////////////////
	// class methods    //
	/////////////////////

	@SuppressWarnings("unchecked")
	protected static final <E> E marker()
	{
		return (E)MARKER;
	}


	@SuppressWarnings("unchecked")
	protected static final <E> E[] newArray(final int length)
	{
		return (E[])new Object[length];
	}

	@SuppressWarnings("unchecked")
	protected static final <E> E[] bendArray(final Object[] array)
	{
		return (E[])array;
	}

	protected static final <E> E[] newArray(final int length, final E[] oldData, final int oldDataLength)
	{
		final E[] newArray = newArray(length);
		System.arraycopy(oldData, 0, newArray, 0, oldDataLength);
		return newArray;
	}

	public static final int pow2BoundMaxed(final long n)
	{
		return JadothMath.pow2BoundMaxed(Jadoth.checkArrayRange(n));
	}



	///////////////////////////////////////////////////////////////////////////
	// declared methods //
	/////////////////////

	/**
	 * This is an internal shortcut method to provide fast access to the various array-backed list implementations'
	 * storage arrays.<br>
	 * The purpose of this method is to allow access to the array only for read-only procedures, never for modifying
	 * accesses.
	 *
	 * @return the storage array used by the list, containing all elements in straight order.
	 */
	protected abstract E[] internalGetStorageArray();

//	public int scan(final Predicate<? super E> predicate)
//	{
//		return AbstractArrayStorage.scan(this.internalGetStorageArray(), this.size(), predicate);
//	}

}
