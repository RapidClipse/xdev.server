package net.jadoth.util.chars;

import net.jadoth.util.Stateless;


public interface EscapeHandler
{
	public void handleEscapedChar(char escapedChar, VarString literalBuilder);

	public boolean needsEscaping(char chr);

	/**
	 * Contract: if no transformation is needed, the same character value is returned.
	 * TO DO: proper JavDoc
	 *
	 *
	 * @param chr
	 * @return
	 */
	public char transformEscapedChar(char chr);

	public char unescape(char chr);



	public final class Implementation implements EscapeHandler, Stateless
	{
		private static char internalUnescape(final char chr)
		{
			/* note:
			 * The left side are arbitrary literal escaping symbols,
			 * the right side are java syntax control character symbols.
			 * Both sides do not necessarily have to be the same characters in source code.
			 * They just happen to be (for conformity) in this generic default implementation.
			 */
			switch(chr) {
				case 't': return '\t';
				case 'b': return '\b';
				case 'n': return '\n';
				case 'r': return '\r';
				case 'f': return '\f';
				default : return chr ; // unmapped character, return directly
			}
		}

		@Override
		public final char unescape(final char chr)
		{
			return internalUnescape(chr);
		}

		@Override
		public final void handleEscapedChar(final char escapedChar, final VarString literalBuilder)
		{
			literalBuilder.add(internalUnescape(escapedChar));
		}

		@Override
		public final boolean needsEscaping(final char chr)
		{
			switch(chr) {
				case '\b':
				case '\f':
				case '\n':
				case '\r':
				case '\t': return true;
				default  : return false;
			}
		}

		@Override
		public final char transformEscapedChar(final char chr)
		{
			switch(chr) {
				case '\t': return 't';
				case '\b': return 'b';
				case '\n': return 'n';
				case '\r': return 'r';
				case '\f': return 'f';
				default  : return chr; // unmapped character, return directly
			}
		}

	}

}
