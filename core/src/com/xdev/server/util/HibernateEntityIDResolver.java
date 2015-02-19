
package com.xdev.server.util;


import java.util.Iterator;
import java.util.Set;

import javax.persistence.metamodel.EntityType;

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import com.xdev.server.communication.EntityManagerHelper;


public class HibernateEntityIDResolver implements EntityIDResolver
{
	private final Configuration	config;
	
	
	public HibernateEntityIDResolver()
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
	public <T> Property getEntityIDProperty(Class<T> entityClass)
	{
		PersistentClass clazz = this.config.getClassMapping(entityClass.getName());
		Property idProperty = clazz.getIdentifierProperty();
		
		return idProperty;
	}
	
	
	public Property getEntityReferenceProperty(Class<?> entityClassA, Class<?> entityClassB)
	{
		
		return null;
	}
}
