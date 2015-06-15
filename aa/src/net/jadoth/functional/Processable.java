package net.jadoth.functional;


/**
 * @author Thomas Muenz
 *
 */
public interface Processable<E>
{
	public <P extends Procedure<? super E>> P process(P processor);
}
