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


import java.util.Iterator;
import java.util.Set;

import javax.persistence.metamodel.EntityType;

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import com.xdev.communication.EntityManagerUtils;


public class HibernateEntityReferenceResolver implements EntityReferenceResolver
{
	private final Configuration	config;
	
	
	public HibernateEntityReferenceResolver()
	{
		this.config = new Configuration();
		Set<EntityType<?>> set = EntityManagerUtils.getEntityManager().getMetamodel()
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
	}
	
	
	@Override
	public String getReferenceEntityPropertyName(Class<?> referenceEntity, Class<?> entity)
	{
		PersistentClass clazz = this.config.getClassMapping(entity.getName());
		Property ref = null;
		
		for(@SuppressWarnings("unchecked")
		Iterator<Property> i = clazz.getReferenceablePropertyIterator(); i.hasNext();)
		{
			Property it = i.next();
			/*
			 * TODO not only referenceable properties are returned, hence a
			 * manual check is required
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
						return ref.getName();
					}
				}
			}
		}
		return null;
	}
}
