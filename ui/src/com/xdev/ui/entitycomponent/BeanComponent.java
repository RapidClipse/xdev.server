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
import java.util.List;

import com.vaadin.data.Container.Viewer;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.xdev.ui.util.KeyValueType;


public interface BeanComponent<BEANTYPE>
		extends Viewer, /* BeanViewer<BEANTYPE>, */ Component
{
	
	public void setContainerDataSource(Class<BEANTYPE> beanClass,
			KeyValueType<?, ?>... nestedProperties);
	
	
	public void setContainerDataSource(Class<BEANTYPE> beanClass, boolean autoQueryData,
			KeyValueType<?, ?>... nestedProperties);
	
	
	public void setContainerDataSource(Class<BEANTYPE> entityClass, Collection<BEANTYPE> data,
			KeyValueType<?, ?>... nestedProperties);
	
	
	/**
	 *
	 * @since 3.0
	 */
	public XdevBeanContainer<BEANTYPE> getBeanContainerDataSource();


	/**
	 * True means, a lazy query container is used as default table container
	 * which queries data at starup. False means, the user is responsible for
	 * using its own implementation or while invoking
	 * {@link #setContainerDataSource(Class, KeyValueType...)} a
	 * BeanItemContainer which must be explicitly filled with data is used.
	 *
	 * @param autoQuery
	 */
	public void setAutoQueryData(boolean autoQuery);


	public boolean isAutoQueryData();
	
	
	/**
	 *
	 * @since 3.0
	 */
	public BeanItem<BEANTYPE> getBeanItem(Object id);


	public BeanItem<BEANTYPE> getSelectedItem();


	/**
	 *
	 * @since 1.0.2
	 */
	public List<BeanItem<BEANTYPE>> getSelectedItems();
	
	
	public void select(Object itemId);


	public void addValueChangeListener(Property.ValueChangeListener listener);
}
