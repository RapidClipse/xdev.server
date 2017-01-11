/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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

package com.xdev.util;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;

import org.apache.log4j.Logger;


/**
 * @author XDEV Software
 * @since 3.0
 */
public final class BeanInfoUtils
{
	private BeanInfoUtils()
	{
	}
	
	
	public static PropertyDescriptor getPropertyDescriptor(Class<?> beanClass,
			final String propertyPath)
	{
		try
		{
			BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);

			final String[] parts = propertyPath.split("\\.");
			for(int i = 0; i < parts.length - 1; i++)
			{
				final String name = parts[i];
				final PropertyDescriptor propertyDescriptor = getPropertyDescriptor(beanInfo,name);
				if(propertyDescriptor == null)
				{
					return null;
				}
				beanClass = propertyDescriptor.getPropertyType();
				if(beanClass == null)
				{
					return null;
				}
				beanInfo = Introspector.getBeanInfo(beanClass);
			}

			return getPropertyDescriptor(beanInfo,parts[parts.length - 1]);
		}
		catch(final Exception e)
		{
			Logger.getLogger(BeanInfoUtils.class).error(e);
		}
		
		return null;
	}


	public static PropertyDescriptor getPropertyDescriptor(final BeanInfo beanInfo,
			final String name)
	{
		return Arrays.stream(beanInfo.getPropertyDescriptors())
				.filter(d -> d.getName().equals(name)).findFirst().orElse(null);
	}
}
