/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */
 
package com.xdev.util;


import java.util.Iterator;
import java.util.Set;

import javax.persistence.metamodel.EntityType;

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import com.xdev.communication.EntityManagerUtil;


/**
 * Returns a Vaadin Item property chain.
 * 
 */
// TODO create VaadinItemPathConcatenator to avoid manual "." appending
public class XdevEntityReferenceResolver implements EntityReferenceResolver
{
	private final Configuration		config;
	private final EntityIDResolver	idResolver;
	
	
	public XdevEntityReferenceResolver()
	{
		this.config = new Configuration();
		Set<EntityType<?>> set = EntityManagerUtil.getEntityManager().getMetamodel()
				.getEntities();
		
		for(Iterator<EntityType<?>> i = set.iterator(); i.hasNext();)
		{
			Class<?> eClazz = i.next().getJavaType();
			try
			{
				this.config.addClass(eClazz);
			}
			catch(MappingException e)
			{
				this.config.addAnnotatedClass(eClazz);
			}
		}
		this.config.buildMappings();
		this.idResolver = new HibernateEntityIDResolver();
	}
	
	
	@Override
	public String getReferenceEntityPropertyName(Class<?> referenceEntity, Class<?> entity)
			throws RuntimeException
	{
		PersistentClass clazz = this.config.getClassMapping(entity.getName());
		Property ref = null;
		
		for(@SuppressWarnings("unchecked")
		Iterator<Property> i = clazz.getReferenceablePropertyIterator(); i.hasNext();)
		{
			Property it = i.next();
			/*
			 * not only referenceable properties are returned, hence a manual
			 * check is required
			 */
			if(HibernateMetaDataUtils.getReferencablePropertyName(it.getValue()) != null)
			{
				ref = it;
				String propertyName = HibernateMetaDataUtils.getReferencablePropertyName(ref
						.getValue());
				
				if(propertyName != null)
				{
					if(propertyName.equals(referenceEntity.getName()))
					{
						return this.attachId(ref);
					}
				}
			}
		}
		return getReferenceEntityPropertyname(referenceEntity,entity,ref);
	}
	
	
	// look deeper into entity
	protected String getReferenceEntityPropertyname(Class<?> referenceEntity,
			Class<?> previousClass, Property previousProperty) throws RuntimeException
	{
		if(previousProperty != null)
		{
			String name = previousProperty.getName() + ".";
			previousClass = previousProperty.getType().getReturnedClass();
			PersistentClass clazz = this.config.getClassMapping(previousClass.getName());
			
			for(@SuppressWarnings("unchecked")
			Iterator<Property> i = clazz.getReferenceablePropertyIterator(); i.hasNext();)
			{
				Property it = i.next();
				/*
				 * not only referenceable properties are returned, hence a
				 * manual check is required
				 */
				if(HibernateMetaDataUtils.getReferencablePropertyName(it.getValue()) != null)
				{
					previousProperty = it;
					String propertyName = HibernateMetaDataUtils
							.getReferencablePropertyName(previousProperty.getValue());
					
					if(propertyName != null)
					{
						if(propertyName.equals(referenceEntity.getName()))
						{
							return this.attachId(name,previousProperty);
						}
					}
				}
			}
		}
		return getReferenceEntityPropertyname(referenceEntity,previousClass,previousProperty);
	}
	
	
	private String attachId(String itemPropertyPath, Property property)
	{
		Class<?> javaClass = property.getType().getReturnedClass();
		return itemPropertyPath + property.getName() + "."
				+ this.idResolver.getEntityIDProperty(javaClass).getName();
	}
	
	
	private String attachId(Property property)
	{
		Class<?> javaClass = property.getType().getReturnedClass();
		return property.getName() + "." + this.idResolver.getEntityIDProperty(javaClass).getName();
	}
	
}
