
package com.xdev.ui.entitycomponent;


import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import org.hibernate.mapping.Property;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.Converter;
import com.xdev.server.util.EntityIDResolver;
import com.xdev.server.util.HibernateEntityIDResolver;


//TODO check if object as ID type is always suitable
public class IDToEntitySetConverter<T> implements Converter<Set<Object>, Set<T>>
{
	private final EntityContainer<T>	container;
	private final EntityIDResolver		idResolver;
	private Set<T>						beanSet	= new LinkedHashSet<>();
	private Set<Object>					idSet	= new LinkedHashSet<>();
	
	
	/**
	 *
	 */
	public IDToEntitySetConverter(final EntityContainer<T> container)
	{
		this.container = container;
		this.idResolver = new HibernateEntityIDResolver();
	}
	
	
	@Override
	public Set<T> convertToModel(final Set<Object> itemIds,
			final Class<? extends Set<T>> targetType, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		this.beanSet = new LinkedHashSet<>();
		if(itemIds != null)
		{
			for(final Object itemId : itemIds)
			{
				final BeanItem<T> item = this.container.getEntityItem(itemId);
				if(item != null)
				{
					this.beanSet.add(item.getBean());
				}
			}
			return this.beanSet;
		}
		
		return this.beanSet;
	}
	
	
	@Override
	public Set<Object> convertToPresentation(final Set<T> values,
			final Class<? extends Set<Object>> targetType, final Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		this.idSet = new LinkedHashSet<>();
		if(values != null)
		{
			for(final T bean : values)
			{
				try
				{
					/*
					 * TODO rather make entity manager accesible within
					 * entitycontainer and use it for this purpose
					 */
					final Property idProperty = this.idResolver
							.getEntityIDProperty(bean.getClass());
					final Field idField = bean.getClass().getDeclaredField(idProperty.getName());
					idField.setAccessible(true);
					
					this.idSet.add(idField.get(bean));
				}
				catch(NoSuchFieldException | SecurityException | IllegalArgumentException
						| IllegalAccessException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		return this.idSet;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<T>> getModelType()
	{
		return (Class<Set<T>>)this.beanSet.getClass();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<Set<Object>> getPresentationType()
	{
		return (Class<Set<Object>>)this.idSet.getClass();
	}
	
}
