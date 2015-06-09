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

package com.xdev.ui.paging;


import org.hibernate.mapping.Property;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.ConverterUtil;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.UI;
import com.xdev.ui.entitycomponent.BeanContainer;
import com.xdev.ui.entitycomponent.BeanDelegatorConverter;
import com.xdev.ui.entitycomponent.IDToBeanConverter;
import com.xdev.ui.entitycomponent.UIModelProvider;
import com.xdev.ui.entitycomponent.grid.XdevGrid;
import com.xdev.ui.util.KeyValueType;
import com.xdev.util.HibernateEntityIDResolver;


//hibernate/JPA specific implementation
public class LazyLoadingUIModelProvider<BEANTYPE> implements UIModelProvider<BEANTYPE>
{
	private final int			batchSize;
	private boolean				readOnlyProperties				= true, sortableProperties = true;
	
	/**
	 * for example car.manufacturer.name
	 */
	private static final String	VAADIN_PROPERTY_NESTING_PATTERN	= "\\.";
	
	
	public LazyLoadingUIModelProvider(final int batchSize, final Object idProperty)
	{
		this.batchSize = batchSize;
		this.init();
	}
	
	
	public LazyLoadingUIModelProvider(final int batchSize, final boolean readOnlyProperties,
			final boolean sortableProperties)
	{
		this.batchSize = batchSize;
		this.readOnlyProperties = readOnlyProperties;
		this.sortableProperties = sortableProperties;
		this.init();
	}
	
	
	private void init()
	{
	}
	
	
	@Override
	public <T extends Component> XdevLazyEntityContainer<BEANTYPE> getModel(final T component,
			final Class<BEANTYPE> entityClass, final KeyValueType<?, ?>... nestedProperties)
	{
		final Property idProperty = new HibernateEntityIDResolver()
				.getEntityIDProperty(entityClass);
		
		final XdevLazyEntityContainer<BEANTYPE> let = new XdevLazyEntityContainer<>(entityClass,
				this.batchSize,idProperty.getName());
		
		for(final KeyValueType<?, ?> keyValuePair : nestedProperties)
		{
			let.addContainerProperty(keyValuePair.getKey(),keyValuePair.getType(),
					keyValuePair.getValue(),this.readOnlyProperties,this.sortableProperties);
		}
		let.getQueryView().getQueryDefinition()
				.setMaxNestedPropertyDepth(this.getMaxNestedPropertyDepth(nestedProperties));
		
		return let;
	}
	
	
	/*
	 * 7.4.0 Vaadin grid is no longer an AbstractSelect, hence there must be
	 * this hard case check.
	 */
	public <T extends Component> void setBeanConverter(final BeanContainer<BEANTYPE> container,
			final T component)
	{
		if(component instanceof AbstractSelect)
		{
			final Converter<Object, BEANTYPE> converter = new IDToBeanConverter<BEANTYPE>(container);
			// register non beanitemcontainer id converter
			((AbstractSelect)component).setConverter(converter);
		}
		else if(component instanceof XdevGrid<?>)
		{
			final XdevGrid<?> grid = (XdevGrid<?>)component;
			for(final Column column : grid.getColumns())
			{
				final Class<?> columnModelType = grid.getColumnModelType(column);
				final Class<?> presentationType = column.getRenderer().getPresentationType();
				final Converter<?, ?> defaultConverter = ConverterUtil.getConverter(column
						.getRenderer().getPresentationType(),columnModelType,UI.getCurrent()
						.getSession());
				
				if(defaultConverter != null)
				{
					column.setConverter(defaultConverter);
				}
				else
				{
					column.setEditorField(new GridFieldEditorProvider.Implementation(column
							.getPropertyId(),container).getFieldEditor(columnModelType,
							presentationType));
					column.setConverter(new BeanDelegatorConverter(columnModelType,presentationType));
				}
			}
		}
	}
	
	
	private int getMaxNestedPropertyDepth(final KeyValueType<?, ?>[] nestedProperties)
	{
		int maxNestedPropertyDepth = 0;
		
		for(int i = 0; i < nestedProperties.length; i++)
		{
			final int currentDepth = nestedProperties[i].getKey().toString()
					.split(VAADIN_PROPERTY_NESTING_PATTERN).length;
			if(currentDepth > maxNestedPropertyDepth)
			{
				maxNestedPropertyDepth = currentDepth;
			}
		}
		return maxNestedPropertyDepth;
	}
}
