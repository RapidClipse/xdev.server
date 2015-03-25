
package com.xdev.server.communication;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.vaadin.server.VaadinSession;
import com.xdev.server.db.connection.HibernateUtil;


// Vaadin Session / technology - specific global helper
public class EntityManagerHelper
{
	
	private static EntityManagerFactory	emf;
	
	
	public static EntityManagerFactory getEntityManagerFactory()
	{
		return emf;
	}
	
	private static HibernateUtil	hibernateUtil;
	
	
	public static EntityManagerFactory initializeHibernateFactory(HibernateUtil hibernateUtil)
	{
		EntityManagerHelper.hibernateUtil = hibernateUtil;
		return emf = hibernateUtil.getEntityManagerFactory();
	}
	
	
	public static HibernateUtil getHibernateutil()
	{
		return hibernateUtil;
	}
	
	
	public static EntityManager getEntityManager()
	{
		return (EntityManager)VaadinSession.getCurrent().getAttribute("HibernateEntityManager");
	}
	
	
	public static void closeEntityManager()
	{
		EntityManager entityManager = getEntityManager();
		if(entityManager != null)
		{
			entityManager.close();
		}
	}
	
	
	public static void closeEntityManagerFactory()
	{
		emf.close();
	}
	
	
	public static void beginTransaction()
	{
		getEntityManager().getTransaction().begin();
	}
	
	
	public static void rollback()
	{
		getEntityManager().getTransaction().rollback();
	}
	
	
	public static void commit()
	{
		getEntityManager().getTransaction().commit();
	}
	
	
	public static EntityTransaction getTransaction()
	{
		return getEntityManager().getTransaction();
	}
	
	
	public static <T> CriteriaQuery<T> getCriteriaQuery(Class<T> type)
	{
		CriteriaBuilder cb = EntityManagerHelper.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(type);
		return cq;
	}
}