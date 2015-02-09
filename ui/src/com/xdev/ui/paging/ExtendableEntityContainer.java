
package com.xdev.ui.paging;


import org.vaadin.addons.lazyquerycontainer.CompositeItem;
import org.vaadin.addons.lazyquerycontainer.NestingBeanItem;

import com.vaadin.data.util.BeanItem;


/**
 * EntityContainer enables loading JPA entities in non lazy manner in single
 * read operation using same query backend as LCQ.
 *
 * @param <T>
 *            Entity class.
 * @author Tommi Laukkanen
 */
// copied from EntityContainer to become extendable
public class ExtendableEntityContainer<T> extends ExtendableLazyQueryContainer
{
	private static final long	serialVersionUID	= 1L;
	
	
	/**
	 * Constructor which configures query definition for accessing JPA entities.
	 *
	 * @param entityClass
	 *            The entity class.
	 * @param idPropertyId
	 *            The ID of the ID property or null if item index is used as ID.
	 * @param batchSize
	 *            The batch size.
	 * @param applicationManagedTransactions
	 *            True if application manages transactions instead of container.
	 * @param detachedEntities
	 *            True if entities are detached from PersistenceContext.
	 * @param compositeItems
	 *            True f items are wrapped to CompositeItems.
	 */
	public ExtendableEntityContainer(final Class<T> entityClass, final int batchSize,
			final Object idPropertyId, final boolean applicationManagedTransactions,
			final boolean detachedEntities)
	{
		super(new IntrospectionEntityQueryDefinition<T>(applicationManagedTransactions,
				detachedEntities,false,entityClass,batchSize,idPropertyId),
				new RequisitioningEntityQueryFactory<T>());
	}
	
	
	/**
	 * Constructor which configures query definition for accessing JPA entities.
	 * 
	 * @param applicationManagedTransactions
	 *            True if application manages transactions instead of container.
	 * @param detachedEntities
	 *            True if entities are detached from PersistenceContext. items
	 *            until commit.
	 * @param compositeItems
	 *            True if native items should be wrapped to CompositeItems.
	 * @param entityClass
	 *            The entity class.
	 * @param batchSize
	 *            The batch size.
	 * @param defaultSortPropertyIds
	 *            Properties participating in the native sort.
	 * @param defaultSortPropertyAscendingStates
	 *            List of property sort directions for the native sort.
	 * @param idPropertyId
	 *            Property containing the property ID.
	 */
	public ExtendableEntityContainer(final boolean applicationManagedTransactions,
			final boolean detachedEntities, final Class<T> entityClass, final int batchSize,
			final Object[] defaultSortPropertyIds,
			final boolean[] defaultSortPropertyAscendingStates, final Object idPropertyId)
	{
		super(new IntrospectionEntityQueryDefinition<T>(applicationManagedTransactions,
				detachedEntities,false,entityClass,batchSize,idPropertyId),
				new RequisitioningEntityQueryFactory<T>());
		
		getQueryView().getQueryDefinition().setDefaultSortState(defaultSortPropertyIds,
				defaultSortPropertyAscendingStates);
	}
	
	
	/**
	 * Adds entity to the container as first item i.e. at index 0.
	 *
	 * @return the new constructed entity.
	 */
	public T addEntity()
	{
		final Object itemId = addItem();
		return getEntity(indexOfId(itemId));
	}
	
	
	public BeanItem<T> addEntity(T entity)
	{
		return new NestingBeanItem<T>(entity,super.getQueryView().getQueryDefinition()
				.getMaxNestedPropertyDepth(),super.getQueryView().getQueryDefinition()
				.getPropertyIds());
	}
	
	
	/**
	 * Removes given entity at given index and returns it.
	 *
	 * @param index
	 *            Index of the entity to be removed.
	 * @return The removed entity.
	 */
	public T removeEntity(final int index)
	{
		final T entityToRemove = getEntity(index);
		removeItem(getIdByIndex(index));
		return entityToRemove;
	}
	
	
	/**
	 * Gets entity by ID.
	 *
	 * @param id
	 *            The ID of the entity.
	 * @return the entity.
	 */
	public T getEntity(final Object id)
	{
		return getEntity(indexOfId(id));
	}
	
	
	/**
	 * Gets entity at given index.
	 *
	 * @param index
	 *            The index of the entity.
	 * @return the entity.
	 */
	@SuppressWarnings("unchecked")
	public T getEntity(final int index)
	{
		if(getQueryView().getQueryDefinition().isCompositeItems())
		{
			final CompositeItem compositeItem = (CompositeItem)getItem(getIdByIndex(index));
			final BeanItem<T> beanItem = (BeanItem<T>)compositeItem.getItem("bean");
			return beanItem.getBean();
		}
		else
		{
			return ((BeanItem<T>)getItem(getIdByIndex(index))).getBean();
		}
	}
	
}
