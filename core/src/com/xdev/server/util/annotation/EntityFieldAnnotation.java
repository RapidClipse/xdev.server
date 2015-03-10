
package com.xdev.server.util.annotation;


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
	
	
	@SuppressWarnings("unchecked")
	public <T extends Annotation> T isAnnotatedType(final Class<?> ownerClass,
			final Class<T> annotationClass) throws RuntimeException
	{
		for(int i = 0; i < ownerClass.getDeclaredAnnotations().length; i++)
		{
			final Annotation annotation = ownerClass.getDeclaredAnnotations()[i];
			if(annotation.annotationType().equals(annotationClass))
			{
				return (T)annotation;
			}
		}
		
		throw new RuntimeException("No annotation with name " + annotationClass.getName()
				+ " declared in class " + ownerClass.getName());
	}
}
