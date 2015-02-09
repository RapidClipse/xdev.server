
package com.xdev.ui.paging;


public class XdevLazyEntityContainer<T> extends ExtendableEntityContainer<T>
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -687747633245500730L;
	
	
	public XdevLazyEntityContainer(Class<T> entityClass, int batchSize, Object idPropertyId)
	{
		super(entityClass,batchSize,idPropertyId,false,false);
	}
	
	
	public XdevLazyEntityContainer(Class<T> entityClass, int batchSize, Object idPropertyId,
			final Object[] sortPropertyIds, final boolean[] sortPropertyAscendingStates)
	{
		super(false,false,entityClass,batchSize,sortPropertyIds,sortPropertyAscendingStates,
				idPropertyId);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addContainerFilter(Filter filter)
	{
		super.addContainerFilter(filter);
		super.refresh();
	}
	
}
