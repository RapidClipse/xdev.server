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
 
package com.xdev.server.communication;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.vaadin.server.VaadinService;
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
		return (EntityManager)VaadinService.getCurrentRequest().getAttribute(
				"HibernateEntityManager");
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
