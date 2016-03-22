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

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.vaadin.util.CurrentInstance;
import com.xdev.communication.XdevServlet;
import com.xdev.communication.XdevServletService;


/**
 * @author XDEV Software
 * @since 1.2
 */
public class PersistenceManager
{
	public final static PersistenceManager getCurrent()
	{
		PersistenceManager manager = null;
		final XdevServlet servlet = XdevServlet.getServlet();
		if(servlet != null)
		{
			final XdevServletService service = servlet.getService();
			if(service != null)
			{
				manager = service.getPersistenceManager();
			}
		}
		if(manager == null)
		{
			manager = CurrentInstance.get(PersistenceManager.class);
		}
		return manager;
	}

	private final EntityManagerFactoryProvider		entityManagerFactoryProvider;
	private final Map<String, Collection<Class<?>>>	unitToClasses;
	private final Map<Class<?>, String>				classToUnit;
	private final Map<String, EntityManagerFactory>	entityManagerFactories	= new HashMap<>();


	/**
	 *
	 * @noapi use {@link #getCurrent()} to get the {@link PersistenceManager},
	 *        initialization is done by {@link XdevServletService}.
	 * @throws PersistenceException
	 */
	public PersistenceManager(final XdevServletService service,
			final EntityManagerFactoryProvider entityManagerFactoryProvider)
					throws PersistenceException
	{
		this.entityManagerFactoryProvider = entityManagerFactoryProvider;
		this.unitToClasses = readPersistenceUnitTypes(service);
		this.classToUnit = createClassToUnitMap(this.unitToClasses);
	}


	protected Map<String, Collection<Class<?>>> readPersistenceUnitTypes(
			final XdevServletService service) throws PersistenceException
	{
		final Map<String, Collection<Class<?>>> persistenceUnitTypes = new LinkedHashMap<>();

		try
		{
			final URL url = findPersistenceXML(service);
			if(url != null)
			{
				final ClassLoader classLoader = service.getClassLoader();
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


	protected URL findPersistenceXML(final XdevServletService service) throws MalformedURLException
	{
		URL resourceUrl = service.getServlet().getServletContext()
				.getResource("/META-INF/persistence.xml");
		if(resourceUrl == null)
		{
			final ClassLoader classLoader = service.getClassLoader();
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


	public String getDefaultPersistenceUnit()
	{
		if(this.unitToClasses.isEmpty())
		{
			return null;
		}
		return getPersistenceUnits().iterator().next();
	}


	public Iterable<String> getPersistenceUnits()
	{
		return this.unitToClasses.keySet();
	}


	public Iterable<Class<?>> getPersistenceUnitClasses(final String persistenceUnit)
	{
		return this.unitToClasses.get(persistenceUnit);
	}


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


	public EntityManagerFactory getEntityManagerFactory(final String persistenceUnit)
	{
		EntityManagerFactory factory = this.entityManagerFactories.get(persistenceUnit);
		if(factory == null)
		{
			factory = this.entityManagerFactoryProvider.createEntityManagerFactory(persistenceUnit);
			this.entityManagerFactories.put(persistenceUnit,factory);
		}
		return factory;
	}


	public boolean isQueryCacheEnabled(final String persistenceUnit)
	{
		return isQueryCacheEnabled(getEntityManagerFactory(persistenceUnit));
	}
	
	
	public boolean isQueryCacheEnabled(final EntityManagerFactory factory)
	{
		final Map<String, Object> properties = factory.getProperties();
		final Object queryCacheProperty = properties.get("hibernate.cache.use_query_cache");
		return "true".equals(queryCacheProperty);
	}
	
	
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
