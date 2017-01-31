/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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

package com.xdev.reports.tableexport;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Table;

import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


/**
 * @author XDEV Software
 *
 */
public final class DataSourceFactory
{
	private DataSourceFactory()
	{
	}
	
	
	public static JRDataSource createDataSource(final BeanItemContainer<?> container)
	{
		final List<?> beanList = container.getItemIds().stream().map(container::getItem)
				.map(BeanItem::getBean).collect(Collectors.toList());
		return new JRBeanCollectionDataSource(beanList);
	}


	public static JRDataSource createDataSource(final Table table, final List<Column> columns)
	{
		final String[] columnNames = columns.stream().map(Column::getColumnHeader)
				.toArray(String[]::new);

		final DRDataSource dataSource = new DRDataSource(columnNames);
		
		final Container container = table.getContainerDataSource();
		final Locale locale = table.getLocale();
		
		for(final Object id : container.getItemIds())
		{
			dataSource.add(getFormattedValues(container.getItem(id),columns,locale));
		}
		
		return dataSource;
	}
	
	
	private static Object[] getFormattedValues(final Item item, final List<Column> columns,
			final Locale locale)
	{
		final List<Object> values = new ArrayList<>();

		for(final Column column : columns)
		{
			final Object propertyId = column.getPropertyID();
			final Object value = item.getItemProperty(propertyId).getValue();
			
			final Converter<String, Object> converter = column.getConverter();
			if(converter != null)
			{
				values.add(converter.convertToPresentation(value,String.class,locale));
			}
			else
			{
				values.add(String.valueOf(value));
			}
		}

		return values.toArray();
	}
}
