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

package com.xdev.ui.paging;


import com.vaadin.ui.AbstractSelect;
import com.xdev.ui.entitycomponent.IDToBeanCollectionConverter;
import com.xdev.ui.entitycomponent.IDToBeanConverter;
import com.xdev.ui.entitycomponent.UIModelProvider;
import com.xdev.ui.entitycomponent.XdevBeanContainer;
import com.xdev.ui.util.KeyValueType;
import com.xdev.util.JPAEntityIDResolver;


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
	}


	public LazyLoadingUIModelProvider(final int bachSize, final boolean readOnlyProperties,
			final boolean sortableProperties)
	{
		this.batchSize = bachSize;
		this.readOnlyProperties = readOnlyProperties;
		this.sortableProperties = sortableProperties;
	}


	@Override
	public XdevLazyEntityContainer<BEANTYPE> getModel(final AbstractSelect component,
			final Class<BEANTYPE> entityClass, final KeyValueType<?, ?>... nestedProperties)
	{
		final String idAttributeName = JPAEntityIDResolver.getInstance()
				.getEntityIDAttributeName(entityClass);
		
		final XdevLazyEntityContainer<BEANTYPE> container = new XdevLazyEntityContainer<>(
				entityClass,this.batchSize,idAttributeName);
		
		for(final KeyValueType<?, ?> keyValuePair : nestedProperties)
		{
			container.addContainerProperty(keyValuePair.getKey(),keyValuePair.getType(),
					keyValuePair.getValue(),this.readOnlyProperties,this.sortableProperties);
		}
		container.getQueryView().getQueryDefinition()
				.setMaxNestedPropertyDepth(this.getMaxNestedPropertyDepth(nestedProperties));
		
		return container;
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


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings({"rawtypes","unchecked"})
	@Override
	public void setRelatedModelConverter(final AbstractSelect component,
			final XdevBeanContainer<BEANTYPE> container)
	{
		// there is no vaadin multiselectable interface or something similar
		// hence cant use strategies here.
		if(component.isMultiSelect())
		{
			component.setConverter(new IDToBeanCollectionConverter(container));
		}
		else
		{
			component.setConverter(new IDToBeanConverter<BEANTYPE>(container));
		}
	}
}
