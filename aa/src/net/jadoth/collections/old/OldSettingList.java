package net.jadoth.collections.old;

import net.jadoth.collections.types.XSettingList;

public abstract class OldSettingList<E> extends OldGettingList<E>
{
	///////////////////////////////////////////////////////////////////////////
	// constructors     //
	/////////////////////

	protected OldSettingList(final XSettingList<E> list)
	{
		super(list);
	}



	///////////////////////////////////////////////////////////////////////////
	// override methods //
	/////////////////////

	@Override
	public XSettingList<E> parent()
	{
		return (XSettingList<E>)this.subject;
	}

	@Override
	public E set(final int index, final E element)
	{
		return ((XSettingList<E>)this.subject).setGet(index, element);
	}

	@Override
	public OldSettingList<E> subList(final int fromIndex, final int toIndex)
	{
		/* XSettingList implementations always create a SubList instance whose implementation creates an
		 * OldSettingList bridge instance, so this cast is safe (and inevitable). Savvy :)?
		 */
		return (OldSettingList<E>)(((XSettingList<E>)this.subject).range(fromIndex, toIndex).old());
	}

}
