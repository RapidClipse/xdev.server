package net.jadoth.util;

import net.jadoth.hash.HashImmutable;


/**
 * A type whose instances represents (effectively) immutable values that should only be primarily handled as values
 * instead of objects (e.g. for determining equality and comparison). String, primitive wrappers, etc. should have been
 * marked with an interface like that. Sadly, they aren't. Nevertheless, here is a proper marker interface
 * to mark self defined types as being value types.
 * <p>
 * Value types are the only types where inherently implemented equals() and hashCode() are properly applicable.
 * As Java is sadly missing a SELF typing, the untyped equals(Object obj) can't be defined more specific
 * (like for example public boolean equals(SELF obj) or such).
 *
 * <p>
 * Also see:
 * @see HashImmutable
 * @see Immutable
 * @see Stateless
 *
 * @author Thomas Muenz
 *
 */
public interface ValueType extends HashImmutable
{
	// marker interface
}
