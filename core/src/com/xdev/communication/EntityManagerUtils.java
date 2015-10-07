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

package com.xdev.communication;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.vaadin.server.VaadinSession;
import com.xdev.db.connection.EntityManagerFactoryProvider;


public class EntityManagerUtils
{
	public final static String ENTITY_MANAGER_ATTRIBUTE = "EntityManager";

	private static EntityManagerFactory emf;


	public static EntityManagerFactory getEntityManagerFactory()
	{
		return emf;
	}

	private static EntityManagerFactoryProvider hibernateUtil;


	public static EntityManagerFactory initializeHibernateFactory(
			final EntityManagerFactoryProvider hibernateUtil)
	{
		EntityManagerUtils.hibernateUtil = hibernateUtil;
		return emf = hibernateUtil.getEntityManagerFactory();
	}


	public static EntityManagerFactoryProvider getEntityManagerFactoryProvider()
	{
		return hibernateUtil;
	}


	public static EntityManager getEntityManager()
	{
		/*
		 * TODO add provider to plug into entity manager administration for
		 * Vaadin Session independency
		 */
		final Conversationable conversationable = (Conversationable)VaadinSession.getCurrent()
				.getAttribute(ENTITY_MANAGER_ATTRIBUTE);
		if(conversationable != null)
		{
			return conversationable.getEntityManager();
		}
		return null;
	}


	public static Conversation getConversation()
	{
		/*
		 * TODO add provider to plug into entity manager administration for
		 * Vaadin Session independency
		 */
		final Conversationable conversationable = (Conversationable)VaadinSession.getCurrent()
				.getAttribute(ENTITY_MANAGER_ATTRIBUTE);
		if(conversationable != null)
		{
			return conversationable.getConversation();
		}
		return null;
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
		final CriteriaBuilder cb = EntityManagerUtils.getEntityManager().getCriteriaBuilder();
		final CriteriaQuery<T> cq = cb.createQuery(type);
		return cq;
	}
}
