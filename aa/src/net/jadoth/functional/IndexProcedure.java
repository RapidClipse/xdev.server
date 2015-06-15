package net.jadoth.functional;

/**
 * @author Thomas Muenz
 *
 */
public interface IndexProcedure<T>
{
	public void accept(T e, int index);
}
