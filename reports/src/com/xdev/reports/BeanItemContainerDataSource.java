/*
 * Copyright (C) 2013-2019 by XDEV Software, All Rights Reserved.
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

package com.xdev.reports;


import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.xdev.ui.entitycomponent.XdevBeanContainer;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


/**
 * @author XDEV Software
 *
 */
public class BeanItemContainerDataSource extends JRBeanCollectionDataSource
{
	public BeanItemContainerDataSource(final BeanItemContainer<?> container)
	{
		super(extractBeans(container));
	}
	
	
	public BeanItemContainerDataSource(final BeanItemContainer<?> container,
			final boolean isUseFieldDescription)
	{
		super(extractBeans(container),isUseFieldDescription);
	}


	public BeanItemContainerDataSource(final XdevBeanContainer<?> container)
	{
		super(extractBeans(container));
	}
	
	
	public BeanItemContainerDataSource(final XdevBeanContainer<?> container,
			final boolean isUseFieldDescription)
	{
		super(extractBeans(container),isUseFieldDescription);
	}
	
	
	private static <T> List<T> extractBeans(final BeanItemContainer<T> container)
	{
		return container.getItemIds().stream().map(container::getItem).map(BeanItem::getBean)
				.collect(Collectors.toList());
	}
	
	
	private static <T> List<T> extractBeans(final XdevBeanContainer<T> container)
	{
		return container.getItemIds().stream().map(container::getItem).map(BeanItem::getBean)
				.collect(Collectors.toList());
	}
}
