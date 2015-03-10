
package com.xdev.server.util;


import java.util.Iterator;
import java.util.Set;

import javax.persistence.metamodel.EntityType;

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import com.xdev.server.communication.EntityManagerHelper;


public class HibernateEntityReferenceResolver implements EntityReferenceResolver
{
	private final Configuration	config;
	
	
	public HibernateEntityReferenceResolver()
	{
		this.config = new Configuration();
		Set<EntityType<?>> set = EntityManagerHelper.getEntityManager().getMetamodel()
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
