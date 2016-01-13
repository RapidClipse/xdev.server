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
 */

package com.xdev.ui.entitycomponent;


import com.vaadin.data.Container.Viewer;


/**
 *
 * @author XDEV Software (JW)
 *		
 * @param <BEANTYPE>
 */
public interface BeanViewer<BEANTYPE> extends Viewer
{
	
	/**
	 * Sets the Container that serves as the data source of the viewer.
	 *
	 * @param newDataSource
	 *            The new data source Item
	 */
	public void setDataContainer(XdevBeanContainer<BEANTYPE> newDataSource);
	
	
	/**
	 * Gets the Container serving as the data source of the viewer.
	 *
	 * @return data source Container
	 */
	public XdevBeanContainer<BEANTYPE> getDataContainer();
	
}
