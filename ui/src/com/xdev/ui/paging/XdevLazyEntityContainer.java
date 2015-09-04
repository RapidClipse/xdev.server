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


public class XdevLazyEntityContainer<T> extends ExtendableLazyEntityContainer<T>
{
	/**
	 *
	 */
	private static final long serialVersionUID = -687747633245500730L;
	
	
	public XdevLazyEntityContainer(final Class<T> entityClass, final int batchSize,
			final Object idPropertyId)
	{
		super(entityClass,batchSize,idPropertyId,false,false);
	}
	
	
	public XdevLazyEntityContainer(final Class<T> entityClass, final int batchSize,
			final Object idPropertyId, final Object[] sortPropertyIds,
			final boolean[] sortPropertyAscendingStates)
	{
		super(false,false,entityClass,batchSize,sortPropertyIds,sortPropertyAscendingStates,
				idPropertyId);
	}
}
