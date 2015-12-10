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

package com.xdev.util;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.xdev.res.StringResourceUtils;


/**
 * @author XDEV Software
 * 		
 */
public interface CaptionResolver
{
	public String resolveCaption(Object element, Locale locale);
	
	
	
	public static class Implementation implements CaptionResolver
	{
		@Override
		public String resolveCaption(final Object element, final Locale locale)
		{
			final Caption caption = getCaptionAnnotation(element);
			String value = caption != null ? caption.value() : null;
			if(value == null || value.length() == 0)
			{
				if(element instanceof Class)
				{
					return ((Class<?>)element).getSimpleName();
				}
				if(element instanceof Member)
				{
					return ((Member)element).getName();
				}

				// avoid stack overflow
				if(!isCallFromToString())
				{
					return element.toString();
				}
				
				return element.getClass().getName() + "@" + Integer.toHexString(element.hashCode());
			}
			
			value = StringResourceUtils.localizeString(value,locale,element);
			
			value = format(value,element);
			
			return value;
		}
		
		
		protected Caption getCaptionAnnotation(final Object element)
		{
			AnnotatedElement annotatedElement;
			if(element instanceof AnnotatedElement)
			{
				annotatedElement = (AnnotatedElement)element;
			}
			else
			{
				annotatedElement = element.getClass();
			}
			return annotatedElement.getAnnotation(Caption.class);
		}
		
		
		protected boolean isCallFromToString()
		{
			final Throwable throwable = new Throwable();
			throwable.fillInStackTrace();
			return Arrays.stream(throwable.getStackTrace())
					.filter(element -> "toString".equals(element.getMethodName())).findFirst()
					.isPresent();
		}
		
		
		protected String format(String string, final Object element)
		{
			Function<String, String> parameterProvider = null;
			
			int start;
			int searchStart = 0;
			while((start = string.indexOf("{%",searchStart)) >= 0)
			{
				final int end = string.indexOf("}",start + 2);
				if(end > start)
				{
					if(parameterProvider == null)
					{
						parameterProvider = getParameterProvider(element);
					}
					
					final String parameterName = string.substring(start + 2,end);
					final String value = parameterProvider.apply(parameterName);
					
					final StringBuilder sb = new StringBuilder();
					sb.append(string.substring(0,start));
					sb.append(value);
					sb.append(string.substring(end + 1));
					string = sb.toString();
					
					searchStart = start + value.length();
				}
				else
				{
					break;
				}
			}
			return string;
		}
		
		
		protected Function<String, String> getParameterProvider(final Object element)
		{
			if(element instanceof Member)
			{
				return parameter -> parameter;
			}
			
			return new BeanInfoParameterProvider(element);
		}
	}
	
	
	
	public static class BeanInfoParameterProvider implements Function<String, String>
	{
		protected static Logger	LOG				= Logger
				.getLogger(BeanInfoParameterProvider.class.getName());
				
		private final Object	element;
								
		private boolean			acquireBeanInfo	= true;
		private BeanInfo		beanInfo;
								
								
		public BeanInfoParameterProvider(final Object element)
		{
			this.element = element;
		}
		
		
		protected Object getElement()
		{
			return this.element;
		}
		
		
		@Override
		public String apply(final String parameter)
		{
			if(this.acquireBeanInfo)
			{
				this.acquireBeanInfo = false;
				
				try
				{
					this.beanInfo = Introspector.getBeanInfo(this.element.getClass());
				}
				catch(final IntrospectionException e)
				{
					LOG.log(Level.SEVERE,e.getMessage(),e);
				}
			}
			
			if(this.beanInfo == null)
			{
				return parameter;
			}
			
			return getParameter(parameter,this.beanInfo);
		}
		
		
		protected String getParameter(final String parameter, final BeanInfo beanInfo)
		{
			final PropertyDescriptor propertyDescriptor = Arrays
					.stream(beanInfo.getPropertyDescriptors())
					.filter(pd -> pd.getName().equals(parameter)).findFirst().orElse(null);
			if(propertyDescriptor == null)
			{
				return parameter;
			}
			
			final Method method = propertyDescriptor.getReadMethod();
			if(method.getParameterCount() > 0)
			{
				return parameter;
			}
			
			try
			{
				final Object value = method.invoke(this.element);
				return String.valueOf(value);
			}
			catch(final Throwable t)
			{
				LOG.log(Level.SEVERE,t.getMessage(),t);
				return parameter;
			}
		}
	}
}
