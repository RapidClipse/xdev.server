/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * For further information see
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.ui.entitycomponent;


import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;


public interface XdevBeanContainer<T>
		extends Container.Sortable, Container.Filterable, Container.Indexed
{
	public BeanItem<T> addBean(final T bean) throws UnsupportedOperationException;
	
	
	@Override
	public BeanItem<T> getItem(Object itemId) throws UnsupportedOperationException;
	
	
	public void addAll(Collection<? extends T> beans) throws UnsupportedOperationException;
	
	
	public void removeAll() throws UnsupportedOperationException;
	
	
	public void removeAll(Collection<? extends T> beans) throws UnsupportedOperationException;


	/**
	 * refresh data for query implementations
	 */
	public void refresh();
	
	
	public BeanItem<T> replaceItem(BeanItem<T> oldItem, T newBean);
	
	
	public Class<? super T> getBeanType();
	
	
	public void setRequiredProperties(Object... propertyIDs);


	public Object[] getRequiredProperties();
}
