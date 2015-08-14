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

package com.xdev.communication;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.xdev.db.connection.HibernateUtils;


public class EntityManagerHelperThreadLocal
{
	private static EntityManagerFactory emf;
	
	private static final ThreadLocal<ConversationableEntityManager> ENTITY_MANAGERS = new ThreadLocal<ConversationableEntityManager>();
	
	
	/** Returns a fresh EntityManager */
	public static ConversationableEntityManager getEntityManager()
	{
		return ENTITY_MANAGERS.get();
	}
	
	
	public static void setEntityManager(final ConversationableEntityManager value)
	{
		ENTITY_MANAGERS.set(value);
	}
	
	
	public static EntityManagerFactory getEntityManagerFactory()
	{
		return emf;
	}
	
	private static HibernateUtils hibernateUtil;
	
	
	public static EntityManagerFactory initializeHibernateFactory(
			final HibernateUtils hibernateUtil)
	{
		EntityManagerHelperThreadLocal.hibernateUtil = hibernateUtil;
		return emf = hibernateUtil.getEntityManagerFactory();
	}
	
	
	public static HibernateUtils getHibernateutil()
	{
		return hibernateUtil;
	}
	
	
	public static void closeEntityManager()
	{
		final EntityManager entityManager = getEntityManager();
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
	
	
	public static <T> CriteriaQuery<T> getCriteriaQuery(final Class<T> type)
	{
		final CriteriaBuilder cb = EntityManagerHelperThreadLocal.getEntityManager()
				.getCriteriaBuilder();
		final CriteriaQuery<T> cq = cb.createQuery(type);
		return cq;
	}
}
