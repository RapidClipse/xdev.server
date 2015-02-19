package com.xdev.server.db.connection;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public interface HibernateUtil
{
	public EntityManagerFactory getEntityManagerFactory();
	
	public class Implementation implements HibernateUtil
	{
		private final EntityManagerFactory	entityManagerFactory;
		
		public Implementation(String persistenceUnit)
		{
			entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		}
		
		
		public EntityManagerFactory getEntityManagerFactory()
		{
			return entityManagerFactory;
		}
		
	}
}
