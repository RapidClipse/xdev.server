
package com.xdev.server.dal;


import java.io.Serializable;

import com.googlecode.genericdao.dao.jpa.GenericDAO;
import com.xdev.server.util.SoftCache;


public class DAOs
{
	// private static final Configuration config;
	// private static final EntityFieldAnnotation annotationReader;
	private static final SoftCache<Class<?>, GenericDAO<?, ?>>	cache;
	
	static
	{
		// config = new Configuration();
		// Set<EntityType<?>> set =
		// EntityManagerHelper.getEntityManager().getMetamodel()
		// .getEntities();
		//
		// for(Iterator<EntityType<?>> i = set.iterator(); i.hasNext();)
		// {
		// Class<?> eClazz = i.next().getJavaType();
		// try
		// {
		// config.addClass(eClazz);
		// }
		// catch(MappingException e)
		// {
		// config.addAnnotatedClass(eClazz);
		// }
		// }
		// config.buildMappings();
		// annotationReader = new EntityFieldAnnotation();
		
		cache = new SoftCache<>();
	}
	
	
	public static <T, I extends Serializable> GenericDAO<T, I> get(
			Class<? extends GenericDAO<T, I>> daoType) throws RuntimeException
	{
		synchronized(cache)
		{
			@SuppressWarnings("unchecked")
			GenericDAO<T, I> dao = (GenericDAO<T, I>)cache.get(daoType);
			
			if(dao == null)
			{
				try
				{
					dao = daoType.newInstance();
					cache.put(daoType,dao);
				}
				catch(InstantiationException | IllegalAccessException e)
				{
					throw new RuntimeException(e);
				}
			}
			
			return dao;
		}
	}
	
	// - just use get with dao type for now
	// annotation types can not be generic
	// @SuppressWarnings("unchecked")
	// public static <T, I extends Serializable> GenericDAO<T, I> get(Class<T>
	// entityClass,
	// Class<I> idClass) throws RuntimeException
	// {
	// Iterator<PersistentClass> classes = config.getClassMappings();
	// while(classes.hasNext())
	// {
	// PersistentClass clazz = classes.next();
	//
	// if(clazz.getMappedClass().equals(entityClass))
	// {
	// DAO da =
	// annotationReader.isAnnotatedType(clazz.getMappedClass(),DAO.class);
	// try
	// {
	// return (GenericDAO<T, I>)da.daoClass().newInstance();
	// }
	// catch(InstantiationException | IllegalAccessException e)
	// {
	// throw new RuntimeException(e);
	// }
	// }
	// }
	// throw new RuntimeException("No DAO found for type " +
	// entityClass.getName());
	// }
}
