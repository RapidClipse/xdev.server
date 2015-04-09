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

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.xdev.ui.util.KeyValueType;


public interface BeanComponent<BEANTYPE> extends BeanViewer<BEANTYPE>, Component
{
	public void setDataContainer(Class<BEANTYPE> beanClass, KeyValueType<?, ?>... nestedProperties);


	public void setDataContainer(Class<BEANTYPE> entityClass, Collection<BEANTYPE> data,
			KeyValueType<?, ?>... nestedProperties);


	public BeanItem<BEANTYPE> getItem(Object id);


	public BeanItem<BEANTYPE> getSelectedItem();


	public void addValueChangeListener(Property.ValueChangeListener listener);
}
