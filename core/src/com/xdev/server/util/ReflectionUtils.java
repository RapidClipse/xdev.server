
package com.xdev.server.util;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;


public final class ReflectionUtils
{
	private ReflectionUtils()
	{
	}
	
	
	public static <T extends Annotation> Field getAnnotatedField(final Class<?> ownerClass,
			final Class<T> annotationClass)
	{
		for(int i = 0; i < ownerClass.getDeclaredFields().length; i++)
		{
			final Field f = ownerClass.getDeclaredFields()[i];
			final T a = f.getDeclaredAnnotation(annotationClass);
			if(a != null)
			{
				return f;
			}
		}
		
		return null;
	}
}
