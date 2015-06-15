/**
 *
 */
package net.jadoth.functional;

import static net.jadoth.Jadoth.notNull;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A Procedure is the proper type to describe a piece of logic that shall be applied to a target. This name was also
 * favioured on the lambda-dev mailing by moreless everyone except the Oracle Java designer in charge, who chose the
 * absolutely wrong name Consumer instead. A consumer is an already in use, predefined concept for a logic
 * that consumes (devours, takes away) a target (e.g. producer-consumer pattern). This is NOT the case with a simple
 * procedure. A procedure does NOT consume the target. Hence it it very unwise to call it Consumer.
 * <p>
 * While all other proprietary functional types in this API get happily replaced by the finally available JDK
 * standard types (like {@link Function} ), an adept API designer cannot in good conscious replace a procedure type
 * with the perfectly sound name "Procedure" by a simply wrong "Consumer". Instead, this type extends Consumer, keeping
 * the proper name alive.
 *
 * @author Thomas Muenz
 *
 */
@FunctionalInterface
public interface Procedure<E> extends Consumer<E>
{
	@Override
	public void accept(E e);

	@Override
	public default Procedure<E> andThen(final Consumer<? super E> next)
	{
		// omg their naming and code style ... o_0. Hackers, hackers everywhere
        notNull(next);
        return (final E e) -> {
        	this.accept(e);
        	next.accept(e);
        };
	}

	public default Procedure<E> then(final Procedure<? super E> next)
	{
        notNull(next);
        return (final E e) -> {
        	this.accept(e);
        	next.accept(e);
        };
	}

	public static <E> void noOp(final E e)
	{
		// no-op
	}

}
