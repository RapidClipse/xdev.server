/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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

package com.xdev.persistence;


import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.xdev.Application;
import com.xdev.communication.Conversationable;
import com.xdev.communication.Conversationables;


/**
 * @author XDEV Software
 * @since 1.2
 */
public final class PersistenceUtils
{
	private PersistenceUtils()
	{
	}


	public static EntityManager getEntityManager(final Class<?> managedType)
	{
		final PersistenceManager persistenceManager = Application.getPersistenceManager();
		if(persistenceManager != null)
		{
			final String persistenceUnit = persistenceManager.getPersistenceUnit(managedType);
			if(persistenceUnit != null)
			{
				return getEntityManager(persistenceUnit);
			}
		}

		return null;
	}


	public static EntityManager getEntityManager(final String persistenceUnit)
	{
		final Conversationables conversationables = Conversationables.getCurrent();
		if(conversationables != null)
		{
			final Conversationable conversationable = conversationables.get(persistenceUnit);
			if(conversationable != null)
			{
				return conversationable.getEntityManager();
			}
		}

		return null;
	}


	public static <T> CriteriaQuery<T> getCriteriaQuery(final Class<T> managedType)
	{
		final EntityManager em = getEntityManager(managedType);
		if(em != null)
		{
			final CriteriaBuilder cb = em.getCriteriaBuilder();
			final CriteriaQuery<T> cq = cb.createQuery(managedType);
			return cq;
		}
		return null;
	}
}
