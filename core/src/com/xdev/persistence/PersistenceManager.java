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

package com.xdev.persistence;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * @author XDEV Software
 * @since 1.2
 */
public interface PersistenceManager
{
	public static interface Factory
	{
		public PersistenceManager createPersistenceManager(final ServletContext context);
	}
	
	
	public String getDefaultPersistenceUnit();
	
	
	public Iterable<String> getPersistenceUnits();
	
	
	public Iterable<Class<?>> getPersistenceUnitClasses(final String persistenceUnit);
	
	
	public String getPersistenceUnit(Class<?> clazz);
	
	
	public EntityManagerFactory getEntityManagerFactory(final String persistenceUnit);
	
	
	public default boolean isQueryCacheEnabled(final String persistenceUnit)
	{
		return isQueryCacheEnabled(getEntityManagerFactory(persistenceUnit));
	}
	
	
	public boolean isQueryCacheEnabled(final EntityManagerFactory factory);
	
	
	public void close();
	
	
	
	public static class Implementation implements PersistenceManager
	{
		private final Map<String, Collection<Class<?>>>	unitToClasses;
		private final Map<Class<?>, String>				classToUnit;
		private final Map<String, EntityManagerFactory>	entityManagerFactories	= new HashMap<>();
		
		
		public Implementation(final ServletContext servletContext) throws PersistenceException
		{
			this.unitToClasses = readPersistenceUnitTypes(servletContext);
			this.classToUnit = createClassToUnitMap(this.unitToClasses);
		}
		
		
		protected Map<String, Collection<Class<?>>> readPersistenceUnitTypes(
				final ServletContext servletContext) throws PersistenceException
		{
			final Map<String, Collection<Class<?>>> persistenceUnitTypes = new LinkedHashMap<>();
			
			try
			{
				final URL url = findPersistenceXML(servletContext);
				if(url != null)
				{
					final ClassLoader classLoader = servletContext.getClassLoader();
					final Document document = new SAXReader().read(url);
					final Element rootElement = document.getRootElement();
					if(rootElement != null)
					{
						for(final Object o : rootElement.elements("persistence-unit"))
						{
							final Element unitElement = (Element)o;
							final String name = unitElement.attributeValue("name");
							final List<Class<?>> classes = new ArrayList<>();
							for(final Object clazzElem : unitElement.elements("class"))
							{
								final String className = ((Element)clazzElem).getTextTrim();
								if(className.length() > 0)
								{
									classes.add(classLoader.loadClass(className));
								}
							}
							persistenceUnitTypes.put(name,classes);
						}
					}
				}
			}
			catch(final Exception e)
			{
				throw new PersistenceException(e);
			}
			
			return persistenceUnitTypes;
		}
		
		
		protected URL findPersistenceXML(final ServletContext servletContext)
				throws MalformedURLException
		{
			URL resourceUrl = servletContext.getResource("/META-INF/persistence.xml");
			if(resourceUrl == null)
			{
				final ClassLoader classLoader = servletContext.getClassLoader();
				resourceUrl = classLoader.getResource("META-INF/persistence.xml");
			}
			return resourceUrl;
		}
		
		
		protected Map<Class<?>, String> createClassToUnitMap(
				final Map<String, Collection<Class<?>>> unitToClasses)
		{
			final Map<Class<?>, String> classToUnit = new HashMap<>();
			unitToClasses.entrySet().forEach(entry -> {
				final String unit = entry.getKey();
				entry.getValue().forEach(clazz -> classToUnit.put(clazz,unit));
			});
			return classToUnit;
		}
		
		
		@Override
		public String getDefaultPersistenceUnit()
		{
			if(this.unitToClasses.isEmpty())
			{
				return null;
			}
			return getPersistenceUnits().iterator().next();
		}
		
		
		@Override
		public Iterable<String> getPersistenceUnits()
		{
			return this.unitToClasses.keySet();
		}
		
		
		@Override
		public Iterable<Class<?>> getPersistenceUnitClasses(final String persistenceUnit)
		{
			return this.unitToClasses.get(persistenceUnit);
		}
		
		
		@Override
		public String getPersistenceUnit(Class<?> clazz)
		{
			while(clazz != null && clazz != Object.class)
			{
				final String unit = this.classToUnit.get(clazz);
				if(unit != null)
				{
					return unit;
				}
				clazz = clazz.getSuperclass();
			}
			
			return null;
		}
		
		
		@Override
		public EntityManagerFactory getEntityManagerFactory(final String persistenceUnit)
		{
			EntityManagerFactory factory = this.entityManagerFactories.get(persistenceUnit);
			if(factory == null)
			{
				factory = getEntityManagerFactoryProvider()
						.createEntityManagerFactory(persistenceUnit);
				this.entityManagerFactories.put(persistenceUnit,factory);
			}
			return factory;
		}
		
		
		protected EntityManagerFactoryProvider getEntityManagerFactoryProvider()
		{
			return EntityManagerFactoryProvider.DEFAULT;
		}
		
		
		@Override
		public boolean isQueryCacheEnabled(final EntityManagerFactory factory)
		{
			final Map<String, Object> properties = factory.getProperties();
			final Object queryCacheProperty = properties.get("hibernate.cache.use_query_cache");
			return "true".equals(queryCacheProperty);
		}
		
		
		@Override
		public void close()
		{
			this.entityManagerFactories.values().forEach(factory -> {
				if(factory.isOpen())
				{
					factory.close();
				}
			});
			this.entityManagerFactories.clear();
		}
	}
}
