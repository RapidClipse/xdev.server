
package com.xdev.ui.paging;


public class XdevLazyEntityContainer<T> extends ExtendableEntityContainer<T>
{
	/**
	 *
	 */
	private static final long	serialVersionUID	= -687747633245500730L;


	public XdevLazyEntityContainer(final Class<T> entityClass, final int batchSize,
			final Object idPropertyId)
	{
		super(entityClass,batchSize,idPropertyId,false,false);
	}


	public XdevLazyEntityContainer(final Class<T> entityClass, final int batchSize,
			final Object idPropertyId, final Object[] sortPropertyIds,
			final boolean[] sortPropertyAscendingStates)
	{
		super(false,false,entityClass,batchSize,sortPropertyIds,sortPropertyAscendingStates,
				idPropertyId);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addContainerFilter(final Filter filter)
	{
		super.addContainerFilter(filter);
		// TODO refresh required?
		// super.refresh();
		notifyItemSetChanged();
	}

}
