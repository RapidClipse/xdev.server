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


import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.xdev.ui.util.KeyValueType;


/* TODO implementation for custom beanidresolver with <IDTYPE> generic for enhanced use cases
 * + write javadoc to communicate that per default the bean itself is used as identifier see BeanItemContainer#BeanItemResolver */
public interface UIModelProvider<BEANTYPE>
{

	public <T extends Component> Container getModel(T component, Class<BEANTYPE> entityClass,
			KeyValueType<?, ?>... nestedProperties);



	public class Implementation<BEANTYPE> implements UIModelProvider<BEANTYPE>
	{

		@Override
		public <T extends Component> BeanItemContainer<BEANTYPE> getModel(final T table,
				final Class<BEANTYPE> entityClass, final KeyValueType<?, ?>... nestedProperties)
		{
			final BeanItemContainer<BEANTYPE> beanItemContainer = new BeanItemContainer<>(
					entityClass);

			for(final KeyValueType<?, ?> keyValuePair : nestedProperties)
			{
				beanItemContainer.addNestedContainerProperty(keyValuePair.getKey().toString());
			}

			return beanItemContainer;
		}

	}
}
