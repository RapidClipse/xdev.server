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


import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;


public interface XdevBeanContainer<T> extends Container.Filterable
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
	
	
	public Class<? super T> getBeanType();
}
