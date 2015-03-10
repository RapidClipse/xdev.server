
package com.xdev.server.util.annotation;


import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.metamodel.EntityType;

import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;

import com.googlecode.genericdao.dao.jpa.GenericDAO;
import com.xdev.server.communication.EntityManagerHelper;


public class GenericDAOFactory
{
	private static final Configuration			config;
	private static final EntityFieldAnnotation	annotationReader;
	
	static
	{
		config = new Configuration();
		Set<EntityType<?>> set = EntityManagerHelper.getEntityManager().getMetamodel()
				.getEntities();
		
		for(Iterator<EntityType<?>> i = set.iterator(); i.hasNext();)
		{
			Class<?> eClazz = i.next().getJavaType();
			try
			{
				config.addClass(eClazz);
			}
			catch(MappingException e)
			{
				config.addAnnotatedClass(eClazz);
			}
		}
		config.buildMappings();
		annotationReader = new EntityFieldAnnotation();
	}
	
	
	// annotation types can not be generic
	@SuppressWarnings("unchecked")
	public static <T, I extends Serializable> GenericDAO<T, I> getDAO(Class<T> entityClass,
			Class<I> idClass) throws RuntimeException
	{
		Iterator<PersistentClass> classes = config.getClassMappings();
		while(classes.hasNext())
		{
			PersistentClass clazz = classes.next();
			
			if(clazz.getMappedClass().equals(entityClass))
			{
				DAO da = annotationReader.isAnnotatedType(clazz.getMappedClass(),DAO.class);
				try
				{
					return (GenericDAO<T, I>)da.daoClass().newInstance();
				}
				catch(InstantiationException | IllegalAccessException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		throw new RuntimeException("No DAO found for type " + entityClass.getName());
	}
}
