/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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

package com.xdev.ui.fieldgroup;


import org.hibernate.StaleObjectStateException;

import com.vaadin.data.util.BeanItem;
import com.xdev.dal.DAOs;
import com.xdev.ui.XdevFieldGroup;


/**
 * @author XDEV Software
 * @since 3.2
 */
public class JPASaveHandler<T> implements SaveHandler<T>
{
	@Override
	public T save(final XdevFieldGroup<T> fieldGroup) throws ObjectLockedException
	{
		final BeanItem<T> beanItem = fieldGroup.getItemDataSource();
		final T bean = beanItem.getBean();
		
		try
		{
			final T persistentBean = DAOs.get(bean).save(bean);
			
			if(persistentBean != bean)
			{
				BeanItem<T> newItem = null;
				
				final BeanItemCreator<T> beanItemCreator = fieldGroup.getBeanItemCreator();
				if(beanItemCreator != null)
				{
					newItem = beanItemCreator.createBeanItem(beanItem,persistentBean);
				}
				if(newItem == null)
				{
					newItem = new BeanItem<T>(persistentBean,fieldGroup.getBeanType());
				}
				fieldGroup.setItemDataSource(newItem);
			}
			
			return persistentBean;
		}
		catch(final StaleObjectStateException e)
		{
			throw new ObjectLockedException(e,fieldGroup,bean);
		}
	}
}
