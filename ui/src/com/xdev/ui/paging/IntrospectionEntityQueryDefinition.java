/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
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

package com.xdev.ui.paging;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.vaadin.addons.lazyquerycontainer.EntityQueryDefinition;

import com.vaadin.data.Container.Sortable;
import com.vaadin.data.util.MethodPropertyDescriptor;
import com.vaadin.data.util.VaadinPropertyDescriptor;


public class IntrospectionEntityQueryDefinition<T> extends EntityQueryDefinition
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8366293949237228773L;
	
	private boolean				readOnlyProperties	= true;
	
	
	public boolean isReadOnlyProperties()
	{
		return readOnlyProperties;
	}
	
	
	public void setReadOnlyProperties(boolean readOnlyProperties)
	{
		this.readOnlyProperties = readOnlyProperties;
	}
	
	
	public IntrospectionEntityQueryDefinition(boolean applicationManagedTransactions,
			boolean detachedEntities, boolean compositeItems, Class<T> entityClass, int batchSize,
			Object idPropertyId)
	{
		super(applicationManagedTransactions,detachedEntities,compositeItems,entityClass,batchSize,
				idPropertyId);
		
		this.addContainerProperties(getPropertyDescriptors(entityClass),entityClass);
	}
	
	
	protected void addContainerProperties(
			LinkedHashMap<String, VaadinPropertyDescriptor<T>> beanProperties, Class<T> entityClass)
	{
		if(entityClass == null)
		{
			throw new IllegalArgumentException(
					"The bean type passed to QueryDefinition must not be null");
		}
		for(Map.Entry<String, VaadinPropertyDescriptor<T>> entry : beanProperties.entrySet())
		{
			List<?> sortables = this.sortableProperties(beanProperties);
			boolean sortableProperty = false;
			for(int i = 0; i < sortables.size(); i++)
			{
				if(entry.getKey().equals(sortables.get(i)))
				{
					sortableProperty = true;
				}
			}
			
			//oddly there is no reasonable way to control read only state for particular columns.
			if(this.isReadOnlyProperties())
			{
				super.addProperty(entry.getKey(),entry.getValue().getPropertyType(),null,
						isReadOnlyProperties(),sortableProperty);
			}
			else
			{
				super.addProperty(entry.getKey(),entry.getValue().getPropertyType(),null,false,
						sortableProperty);
			}
		}
	}
	
	
	/**
	 * Returns the sortable property identifiers for the container. Can be used
	 * to implement {@link Sortable#getSortableContainerPropertyIds()}.
	 */
	protected List<?> sortableProperties(
			LinkedHashMap<String, VaadinPropertyDescriptor<T>> beanProperties)
	{
		LinkedList<Object> sortables = new LinkedList<Object>();
		for(Map.Entry<String, VaadinPropertyDescriptor<T>> entry : beanProperties.entrySet())
		{
			Class<?> propertyType = entry.getValue().getPropertyType();
			if(Comparable.class.isAssignableFrom(propertyType) || propertyType.isPrimitive())
			{
				sortables.add(entry.getKey());
			}
		}
		return sortables;
	}
	
	
	// getPropertyDescriptors code copied form Vaadin BeanItem
	/**
	 * <p>
	 * Perform introspection on a Java Bean class to find its properties.
	 * </p>
	 *
	 * <p>
	 * Note : This version only supports introspectable bean properties and
	 * their getter and setter methods. Stand-alone <code>is</code> and
	 * <code>are</code> methods are not supported.
	 * </p>
	 *
	 * @param beanClass
	 *            the Java Bean class to get properties for.
	 * @param <BT>
	 *            the bean type
	 * @return an ordered map from property names to property descriptors
	 */
	private static final <BT> LinkedHashMap<String, VaadinPropertyDescriptor<BT>> getPropertyDescriptors(
			final Class<BT> beanClass)
	{
		final LinkedHashMap<String, VaadinPropertyDescriptor<BT>> pdMap = new LinkedHashMap<String, VaadinPropertyDescriptor<BT>>();
		
		// Try to introspect, if it fails, we just have an empty Item
		try
		{
			List<PropertyDescriptor> propertyDescriptors = getBeanPropertyDescriptor(beanClass);
			
			// Add all the bean properties as MethodProperties to this Item
			// later entries on the list overwrite earlier ones
			for(PropertyDescriptor pd : propertyDescriptors)
			{
				final Method getMethod = pd.getReadMethod();
				if((getMethod != null) && getMethod.getDeclaringClass() != Object.class)
				{
					VaadinPropertyDescriptor<BT> vaadinPropertyDescriptor = new MethodPropertyDescriptor<BT>(
							pd.getName(),pd.getPropertyType(),pd.getReadMethod(),
							pd.getWriteMethod());
					pdMap.put(pd.getName(),vaadinPropertyDescriptor);
				}
			}
		}
		catch(final java.beans.IntrospectionException ignored)
		{
		}
		
		return pdMap;
	}
	
	
	// code copied form vaadin BeanItem
	/**
	 * Returns the property descriptors of a class or an interface.
	 *
	 * For an interface, superinterfaces are also iterated as Introspector does
	 * not take them into account (Oracle Java bug 4275879), but in that case,
	 * both the setter and the getter for a property must be in the same
	 * interface and should not be overridden in subinterfaces for the discovery
	 * to work correctly.
	 *
	 * For interfaces, the iteration is depth first and the properties of
	 * superinterfaces are returned before those of their subinterfaces.
	 *
	 * @param beanClass
	 *            the bean class
	 * @return list of property descriptors
	 * @throws IntrospectionException
	 */
	private static final List<PropertyDescriptor> getBeanPropertyDescriptor(final Class<?> beanClass)
			throws IntrospectionException
	{
		// Oracle bug 4275879: Introspector does not consider superinterfaces of
		// an interface
		if(beanClass.isInterface())
		{
			List<PropertyDescriptor> propertyDescriptors = new ArrayList<PropertyDescriptor>();
			
			for(Class<?> cls : beanClass.getInterfaces())
			{
				propertyDescriptors.addAll(getBeanPropertyDescriptor(cls));
			}
			
			BeanInfo info = Introspector.getBeanInfo(beanClass);
			propertyDescriptors.addAll(Arrays.asList(info.getPropertyDescriptors()));
			
			return propertyDescriptors;
		}
		else
		{
			BeanInfo info = Introspector.getBeanInfo(beanClass);
			return Arrays.asList(info.getPropertyDescriptors());
		}
	}
}
