
package com.xdev.ui.entitycomponent;


import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Set;

import org.hibernate.mapping.Property;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.Converter;
import com.xdev.server.util.EntityIDResolver;
import com.xdev.server.util.HibernateEntityIDResolver;


//TODO check if object as ID type is always suitable
public class IDToEntityConverter<T> implements Converter<Object, T>
{
	private final EntityContainer<T>	container;
	private final EntityIDResolver		idResolver;
	
	
	/**
	 *
	 */
	public IDToEntityConverter(final EntityContainer<T> container)
	{
		this.container = container;
		this.idResolver = new HibernateEntityIDResolver();
	}
	
	
	@Override
	public T convertToModel(final Object itemID, final Class<? extends T> targetType,
			final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if(itemID == null)
		{
			return null;
		}
		
		// multi selection
		if(!(itemID instanceof Set))
		{
			final BeanItem<T> item = this.container.getEntityItem(itemID);
			if(item != null)
			{
				return item.getBean();
			}
		}
		
		return null;
	}
	
	
	@Override
	public Object convertToPresentation(final T value, final Class<? extends Object> targetType,
			final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if(value != null)
		{
			final Property idProperty = this.idResolver.getEntityIDProperty(value.getClass());
			try
			{
				/*
				 * TODO rather make entity manager accesible within
				 * entitycontainer and use it for this purpose
				 */
				final Field idField = value.getClass().getDeclaredField(idProperty.getName());
				idField.setAccessible(true);
				return idField.get(value);
			}
			catch(NoSuchFieldException | SecurityException | IllegalArgumentException
					| IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
		}
		return null;
	}
	
	
	@Override
	public Class<T> getModelType()
	{
		return this.container.getEntityType();
	}
	
	
	@Override
	public Class<Object> getPresentationType()
	{
		return Object.class;
	}
}
