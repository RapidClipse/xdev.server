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

package com.xdev.util;


import java.beans.PropertyDescriptor;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
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
		return resolveCaption(element,(Locale)null);
	}


	public static String resolveCaption(final Object element, final Locale locale)
	{
		return getCaptionResolver().resolveCaption(element,locale);
	}


	public static String resolveCaption(final Object element, final String captionValue)
	{
		return resolveCaption(element,captionValue,null);
	}


	public static String resolveCaption(final Object element, final String captionValue,
			final Locale locale)
	{
		return getCaptionResolver().resolveCaption(element,captionValue,locale);
	}
	
	
	/**
	 * @since 3.0
	 */
	public static String resolveCaption(final Class<?> clazz, final String propertyName)
	{
		if(JPAMetaDataUtils.isManaged(clazz))
		{
			final Attribute<?, ?> attribute = JPAMetaDataUtils.resolveAttribute(clazz,propertyName);
			if(attribute == null)
			{
				return propertyName;
			}

			final Member javaMember = attribute.getJavaMember();
			if(javaMember instanceof AnnotatedElement
					&& hasCaptionAnnotationValue((AnnotatedElement)javaMember))
			{
				return resolveCaption(javaMember);
			}

			return attribute.getName();
		}
		else
		{
			final PropertyDescriptor propertyDescriptor = BeanInfoUtils.getPropertyDescriptor(clazz,
					propertyName);
			if(propertyDescriptor == null)
			{
				return propertyName;
			}
			
			final Member javaMember = propertyDescriptor.getReadMethod();
			if(javaMember instanceof AnnotatedElement
					&& hasCaptionAnnotationValue((AnnotatedElement)javaMember))
			{
				return resolveCaption(javaMember);
			}

			return propertyDescriptor.getDisplayName();
		}
	}


	public static boolean hasCaptionAnnotationValue(final AnnotatedElement element)
	{
		final Caption annotation = element.getAnnotation(Caption.class);
		return annotation != null && annotation.value().length() > 0;
	}
}
