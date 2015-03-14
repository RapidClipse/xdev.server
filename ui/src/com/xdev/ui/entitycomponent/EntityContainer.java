
package com.xdev.ui.entitycomponent;


import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;


//entity filtering for example for master detail purpose
public interface EntityContainer<T> extends Container.Filterable
{
	/**
	 * Adds entity to the container as first item i.e. at index 0.
	 *
	 * @return the new constructed entity.
	 */
	public T addEntity();


	public int addEntity(final T entity);


	/**
	 * Removes given entity at given index and returns it.
	 *
	 * @param index
	 *            Index of the entity to be removed.
	 * @return The removed entity.
	 */
	public T removeEntity(final int index);


	public void removeEntity(T entity);


	/**
	 * Gets entity by ID.
	 *
	 * @param id
	 *            The ID of the entity.
	 * @return the entity.
	 */
	public BeanItem<T> getEntityItem(final Object id);


	/**
	 * Gets entity at given index.
	 *
	 * @param index
	 *            The index of the entity.
	 * @return the entity.
	 */
	public BeanItem<T> getEntityItem(final int index);


	public void refresh();
	
	
	public Class<T> getEntityType();
}
