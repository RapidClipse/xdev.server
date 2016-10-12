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

package com.xdev.ui;


import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.ui.OptionGroup;
import com.xdev.ui.entitycomponent.BeanComponent;


/**
 * @author XDEV Software
 *		
 *
 * @deprecated replaced by
 *             {@link com.xdev.ui.entitycomponent.listselect.XdevOptionGroup}
 *             which is now a {@link BeanComponent}
 */
@Deprecated
public class XdevOptionGroup extends OptionGroup implements XdevComponent
{
	private final Extensions extensions = new Extensions();
	
	
	/**
	 *
	 */
	public XdevOptionGroup()
	{
		super();
	}
	
	
	/**
	 * @param caption
	 * @param options
	 */
	public XdevOptionGroup(final String caption, final Collection<?> options)
	{
		super(caption,options);
	}
	
	
	/**
	 * @param caption
	 * @param dataSource
	 */
	public XdevOptionGroup(final String caption, final Container dataSource)
	{
		super(caption,dataSource);
	}
	
	
	/**
	 * @param caption
	 */
	public XdevOptionGroup(final String caption)
	{
		super(caption);
	}
	
	
	// init defaults
	{
		setImmediate(true);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E addExtension(final Class<? super E> type, final E extension)
	{
		return this.extensions.add(type,extension);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E getExtension(final Class<E> type)
	{
		return this.extensions.get(type);
	}
}
