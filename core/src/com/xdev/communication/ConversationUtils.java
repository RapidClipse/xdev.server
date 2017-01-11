/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;

import com.xdev.Application;
import com.xdev.persistence.PersistenceUtils;


/**
 * @author XDEV Software
 *
 */
public class ConversationUtils
{
	public static Conversation getConversation()
	{
		return getConversation(Application.getPersistenceManager().getDefaultPersistenceUnit());
	}


	public static Conversation getConversation(final String persistenceUnit)
	{
		final Conversationable conversationable = Conversationables.getCurrent()
				.get(persistenceUnit);
		return conversationable != null ? conversationable.getConversation() : null;
	}


	private static Conversation newConversation(final String persistenceUnit)
	{
		final Conversationable conversationable = Conversationables.getCurrent()
				.get(persistenceUnit);
		if(conversationable != null)
		{
			final Conversation conversation = new Conversation.Implementation();

			try
			{
				conversationable.setConversation(conversation);
			}
			catch(final RuntimeException e)
			{
				return conversationable.getConversation();
			}

			return conversation;
		}

		return null;
	}


	public static Conversation startConversation()
	{
		return startConversation(Application.getPersistenceManager().getDefaultPersistenceUnit());
	}


	public static Conversation startConversation(final String persistenceUnit)
	{
		final Conversation conversation = newConversation(persistenceUnit);
		if(conversation != null)
		{
			conversation.start();
		}
		return conversation;
	}


	public static void startPessimisticConversation()
	{
		startPessimisticConversation(LockModeType.WRITE);
	}


	public static void startPessimisticConversation(final String persistenceUnit)
	{
		startPessimisticConversation(persistenceUnit,LockModeType.WRITE);
	}


	public static Conversation startPessimisticConversation(final LockModeType lockMode)
	{
		return startPessimisticConversation(
				Application.getPersistenceManager().getDefaultPersistenceUnit(),lockMode);
	}


	public static Conversation startPessimisticConversation(final String persistenceUnit,
			final LockModeType lockMode)
	{
		final Conversation conversation = newConversation(persistenceUnit);
		if(conversation != null)
		{
			conversation.setPessimisticUnit(true,lockMode);
			conversation.start();
		}
		return conversation;
	}


	public static void lockConversation(final Object entity)
	{
		final String persistenceUnit = Application.getPersistenceManager()
				.getPersistenceUnit(entity.getClass());
		final Conversationable conversationable = Conversationables.getCurrent()
				.get(persistenceUnit);
		if(conversationable != null)
		{
			final Conversation conversation = conversationable.getConversation();
			if(conversation != null)
			{
				conversationable.getEntityManager().lock(entity,conversation.getLockModeType());
			}
		}
	}


	public static void lockConversation(final Object entity, final Map<String, Object> properties)
	{
		final String persistenceUnit = Application.getPersistenceManager()
				.getPersistenceUnit(entity.getClass());
		final Conversationable conversationable = Conversationables.getCurrent()
				.get(persistenceUnit);
		if(conversationable != null)
		{
			final Conversation conversation = conversationable.getConversation();
			if(conversation != null)
			{
				conversationable.getEntityManager().lock(entity,conversation.getLockModeType(),
						properties);
			}
		}
	}


	public static void releaseConversationLock()
	{
		releaseConversationLock(Application.getPersistenceManager().getDefaultPersistenceUnit());
	}


	public static void releaseConversationLock(final String persistenceUnit)
	{
		final EntityManager em = PersistenceUtils.getEntityManager(persistenceUnit);
		if(em != null)
		{
			final EntityTransaction transaction = em.getTransaction();
			if(transaction.isActive())
			{
				transaction.commit();
			}
		}
	}


	public static void endConversation()
	{
		final Conversation conversation = getConversation();
		if(conversation != null)
		{
			conversation.end();
		}
	}


	public static void endConversation(final String persistenceUnit)
	{
		final Conversation conversation = getConversation(persistenceUnit);
		if(conversation != null)
		{
			conversation.end();
		}
	}


	public static boolean isConversationActive()
	{
		final Conversation conversation = getConversation();
		if(conversation != null)
		{
			return conversation.isActive();
		}
		return false;
	}


	public static boolean isConversationActive(final String persistenceUnit)
	{
		final Conversation conversation = getConversation(persistenceUnit);
		if(conversation != null)
		{
			return conversation.isActive();
		}
		return false;
	}
}
