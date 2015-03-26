/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */
 
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
