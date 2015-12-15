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

package com.xdev.ui.entitycomponent.hierarchical;


import java.lang.reflect.Field;
import java.util.Collection;

import com.xdev.util.Caption;


public interface Group
{
	public Class<?> getGroupClass();
	
	
	public Field getReference();
	
	
	/**
	 * @deprecated not used anymore, caption is now handled by {@link Caption}
	 */
	@Deprecated
	public Field getCaption();
	
	
	public Collection<?> getGroupData();
	
	
	public void setGroupData(Collection<?> groupData);
	
	
	
	public class Implementation implements Group
	{
		private final Class<?>	clazz;
		private final Field		identifier;
		private Collection<?>	groupData;
								
								
		public Implementation(final Class<?> clazz, final Field reference)
		{
			this.clazz = clazz;
			this.identifier = reference;
		}
		
		
		@Deprecated
		public Implementation(final Class<?> clazz, final Field reference, final Field caption)
		{
			this(clazz,reference);
		}
		
		
		@Override
		public Field getReference()
		{
			return this.identifier;
		}
		
		
		@Override
		public Field getCaption()
		{
			return null;
		}
		
		
		@Override
		public Class<?> getGroupClass()
		{
			return this.clazz;
		}
		
		
		@Override
		public Collection<?> getGroupData()
		{
			return this.groupData;
		}
		
		
		@Override
		public void setGroupData(final Collection<?> groupData)
		{
			this.groupData = groupData;
		}
		
		
		@Override
		public String toString()
		{
			if(this.clazz != null)
			{
				return this.clazz.getName();
			}
			return super.toString();
		}
	}
}
