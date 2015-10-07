/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
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


import java.util.Collection;

import com.vaadin.data.util.BeanItemContainer;


/**
 * @author XDEV Software
 * 		
 */
public class XdevBeanItemContainer<BEANTYPE> extends BeanItemContainer<BEANTYPE>
		implements XdevBeanContainer<BEANTYPE>
{

	/**
	 * @param type
	 * @throws IllegalArgumentException
	 */
	public XdevBeanItemContainer(final Class<? super BEANTYPE> type) throws IllegalArgumentException
	{
		super(type);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.xdev.ui.entitycomponent.XdevBeanContainer#removeAll()
	 */
	@Override
	public void removeAll()
	{
		this.removeAllItems();
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.xdev.ui.entitycomponent.XdevBeanContainer#refresh()
	 */
	@Override
	public void refresh()
	{
		// no need to synchronize
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.xdev.ui.entitycomponent.XdevBeanContainer#removeAll(java.util.
	 * Collection)
	 */
	@Override
	public void removeAll(final Collection<? extends BEANTYPE> beans)
	{
		for(final BEANTYPE bean : beans)
		{
			this.removeItem(bean);
		}
	}
}
