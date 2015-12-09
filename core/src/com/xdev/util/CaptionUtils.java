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


import java.util.Locale;

import javax.persistence.metamodel.Attribute;


/**
 * @author XDEV Software
 *		
 */
public final class CaptionUtils
{
	private CaptionUtils()
	{
	}
	
	private static CaptionResolver captionResolver;
	
	
	public static CaptionResolver getCaptionResolver()
	{
		if(captionResolver == null)
		{
			captionResolver = new CaptionResolver.Implementation();
		}
		
		return captionResolver;
	}
	
	
	public static void setCaptionResolver(final CaptionResolver captionResolver)
	{
		CaptionUtils.captionResolver = captionResolver;
	}
	
	
	public static String resolveCaption(final Object element)
	{
		return resolveCaption(element,Locale.getDefault());
	}


	public static String resolveCaption(final Object element, final Locale locale)
	{
		return getCaptionResolver().resolveCaption(element,locale);
	}
	
	
	public static String resolveEntityMemberCaption(final Class<?> entityClass,
			final String propertyName)
	{
		final Attribute<?, ?> attribute = HibernateMetaDataUtils.resolveAttribute(entityClass,
				propertyName);
		if(attribute == null)
		{
			return propertyName;
		}

		return resolveCaption(attribute.getJavaMember());
	}
}
