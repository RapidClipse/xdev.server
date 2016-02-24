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

package com.xdev.ui.paging;


import org.hibernate.ScrollableResults;

import com.vaadin.ui.AbstractSelect;
import com.xdev.ui.entitycomponent.UIModelProvider;
import com.xdev.ui.entitycomponent.XdevBeanContainer;
import com.xdev.ui.util.KeyValueType;


public class PagingUIModelProvider<BEANTYPE> implements UIModelProvider<BEANTYPE>
{
	private final ScrollableResults results;
	
	
	public PagingUIModelProvider(final ScrollableResults results)
	{
		this.results = results;
	}
	
	
	@Override
	public XdevBeanContainer<BEANTYPE> getModel(final AbstractSelect component,
			final Class<BEANTYPE> entityClass, final KeyValueType<?, ?>... nestedProperties)
	{
		final PagedBeanItemContainer<BEANTYPE> beanItemContainer = new PagedBeanItemContainer<>(
				this.results,entityClass);
		for(final KeyValueType<?, ?> keyValuePair : nestedProperties)
		{
			beanItemContainer.addNestedContainerProperty(keyValuePair.getKey().toString());
		}
		
		return beanItemContainer;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRelatedModelConverter(final AbstractSelect component,
			final XdevBeanContainer<BEANTYPE> container)
	{
		// BeanItemContainer uses Beans as IDs iteself, no conversion
		// required
	}
}
