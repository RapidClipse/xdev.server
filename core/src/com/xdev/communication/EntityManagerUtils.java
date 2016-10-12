/*
 * Copyright (C) 2013-2016 by XDEV Software, All Rights Reserved.
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
 *
 * For further information see
 * <http://www.rapidclipse.com/en/legal/license/license.html>.
 */

package com.xdev.communication;


import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaQuery;

import com.xdev.dal.JPADAO;
import com.xdev.persistence.PersistenceManager;
import com.xdev.persistence.PersistenceUtils;


/**
 *
 * @author XDEV Software (JW)
 * 		
 * @deprecated replaced by {@link PersistenceUtils}, {@link PersistenceManager},
 *             {@link ConversationUtils} and {@link Conversationables}, will be
 *             removed in a future release
 */
@Deprecated
public class EntityManagerUtils
{
	/**
	 *
	 * @return the default persistence unit's EntityManager
	 * @deprecated replaced by {@link PersistenceUtils#getEntityManager(String)}
	 */
	@Deprecated
	public static EntityManager getEntityManager()
	{
		return PersistenceUtils
				.getEntityManager(PersistenceManager.getCurrent().getDefaultPersistenceUnit());
	}


	/**
	 *
	 * @deprecated replaced by {@link ConversationUtils#getConversation()}
	 */
	@Deprecated
	public static Conversation getConversation()
	{
		return ConversationUtils.getConversation();
	}


	/**
	 * @deprecated no-op
	 */
	@Deprecated
	public static void closeEntityManager()
	{
	}


	/**
	 * @deprecated no-op
	 */
	@Deprecated
	public static void closeEntityManagerFactory()
	{
	}


	/**
	 * @deprecated use {@link JPADAO#beginTransaction()}
	 */
	@Deprecated
	public static void beginTransaction()
	{
		final EntityManager em = getEntityManager();
		if(em != null)
		{
			em.getTransaction().begin();
		}
	}


	/**
	 * @deprecated use {@link JPADAO#rollback()}
	 */
	@Deprecated
	public static void rollback()
	{
		final EntityManager em = getEntityManager();
		if(em != null)
		{
			em.getTransaction().rollback();
		}
	}


	/**
	 * @deprecated use {@link JPADAO#commit()}
	 */
	@Deprecated
	public static void commit()
	{
		final EntityManager em = getEntityManager();
		if(em != null)
		{
			em.getTransaction().commit();
		}
	}


	/**
	 * @return <code>null</code>
	 * @deprecated no-op
	 */
	@Deprecated
	public static EntityTransaction getTransaction()
	{
		return null;
	}


	/**
	 * @deprecated replaced by {@link PersistenceUtils#getCriteriaQuery(Class)}
	 */
	@Deprecated
	public static <T> CriteriaQuery<T> getCriteriaQuery(final Class<T> type)
	{
		return PersistenceUtils.getCriteriaQuery(type);
	}


	/**
	 * @deprecated replaced by
	 *             {@link PersistenceManager#isQueryCacheEnabled(String)},
	 *             {@link PersistenceManager#isQueryCacheEnabled(EntityManagerFactory)}
	 */
	@Deprecated
	public static boolean isQueryCacheEnabled(final EntityManager entityManager)
	{
		final Map<String, Object> properties = entityManager.getEntityManagerFactory()
				.getProperties();
		final Object queryCacheProperty = properties.get("hibernate.cache.use_query_cache");
		return "true".equals(queryCacheProperty);
	}
}
