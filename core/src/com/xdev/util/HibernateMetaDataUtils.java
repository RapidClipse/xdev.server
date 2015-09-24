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

package com.xdev.util;


import org.hibernate.mapping.Collection;
import org.hibernate.mapping.ManyToOne;
import org.hibernate.mapping.OneToMany;
import org.hibernate.mapping.OneToOne;
import org.hibernate.mapping.Value;


public class HibernateMetaDataUtils
{
	public static String getReferencablePropertyName(final org.hibernate.mapping.Value value)
	{
		/*
		 * there is no super type for each relation type e.g. one-to-many
		 * many-to-many, one-to-one have are directly created are not inherited
		 * from a type like relation, they are independently inherited from
		 * value...
		 */

		final OneToMany oneToMany = getOneToMany(value);
		if(oneToMany != null)
		{
			return oneToMany.getReferencedEntityName();
		}
		else if(value instanceof OneToOne)
		{
			final OneToOne oneToOne = (OneToOne)value;
			return oneToOne.getReferencedEntityName();
		}
		
		final ManyToOne manyToOne = getManyToOne(value);
		if(manyToOne != null)
		{
			return manyToOne.getReferencedEntityName();
		}

		return null;
	}


	private static OneToMany getOneToMany(Value value)
	{
		// in case of wrapping because bidirectional
		if(value instanceof Collection)
		{
			value = ((Collection)value).getElement();
		}
		if(value instanceof OneToMany)
		{
			return (OneToMany)value;
		}
		return null;
	}
	
	
	private static ManyToOne getManyToOne(Value value)
	{
		// in case of wrapping because bidirectional
		if(value instanceof Collection)
		{
			value = ((Collection)value).getElement();
		}
		if(value instanceof ManyToOne)
		{
			return (ManyToOne)value;
		}
		return null;
	}
}
