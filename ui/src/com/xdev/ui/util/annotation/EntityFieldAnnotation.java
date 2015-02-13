
package com.xdev.ui.util.annotation;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;


public class EntityFieldAnnotation
{
	public <T extends Annotation> T getAnnotation(final Class<?> ownerClass,
			final Class<T> annotationClass)
	{
		for(int i = 0; i < ownerClass.getDeclaredFields().length; i++)
		{
			final Field f = ownerClass.getDeclaredFields()[i];
			final T a = f.getDeclaredAnnotation(annotationClass);
			if(a != null)
			{
				return a;
			}
		}

		return null;
	}


	public <T extends Annotation> Field getAnnotationField(final Class<?> ownerClass,
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
