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
 
package com.xdev.ui.entitycomponent.hierarchical;


import java.lang.reflect.Field;
import java.util.Collection;


public interface Group
{
	public Class<?> getGroupClass();


	public Field getReference();


	// Field Value from Annotation or XML, or simply toString
	/*
	 * TODO if caption is not set at item creation time then use #toString
	 * result if possible.
	 */
	public Field getCaption();


	public Collection<?> getGroupData();


	public void setGroupData(Collection<?> groupData);



	// public void setData(Collection<?> data);

	public class Implementation implements Group
	{
		private final Class<?>	clazz;
		private final Field		identifier;
		private final Field		caption;
		private Collection<?>	groupData;


		public Implementation(final Class<?> clazz, final Field reference, final Field caption)
		{
			this.clazz = clazz;
			this.identifier = reference;
			this.caption = caption;
		}


		@Override
		public Field getReference()
		{
			return this.identifier;
		}


		@Override
		public Field getCaption()
		{
			return this.caption;

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
