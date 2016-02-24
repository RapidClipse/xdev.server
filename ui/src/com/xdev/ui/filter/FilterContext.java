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

package com.xdev.ui.filter;


import com.vaadin.data.Container;
import com.vaadin.data.Container.Filterable;


/**
 * @author XDEV Software
 * 		
 */
public interface FilterContext
{
	public FilterSettings getSettings();
	
	
	public Container.Filterable getContainer();
	
	
	public Object getPropertyId();



	public static class Implementation implements FilterContext
	{
		private final FilterSettings		settings;
		private final Container.Filterable	container;
		private final Object				propertyId;
											
											
		public Implementation(final FilterSettings settings, final Filterable container,
				final Object propertyId)
		{
			this.settings = settings;
			this.container = container;
			this.propertyId = propertyId;
		}
		
		
		@Override
		public FilterSettings getSettings()
		{
			return this.settings;
		}
		
		
		@Override
		public Container.Filterable getContainer()
		{
			return this.container;
		}
		
		
		@Override
		public Object getPropertyId()
		{
			return this.propertyId;
		}
	}
}
