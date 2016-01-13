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
 */

package com.xdev.communication;


import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import com.vaadin.server.VaadinSession;
import com.vaadin.server.VaadinSession.State;


/**
 * @author XDEV Software (JW)
 *		
 */
public class ConversationUtils
{
	
	private final static String ENTITY_MANAGER_ATTRIBUTE = "EntityManager";
	
	
	private static boolean isSessionAccessible()
	{
		if(VaadinSession.getCurrent() != null)
		{
			if(VaadinSession.getCurrent().getState() == State.OPEN)
			{
				return true;
			}
		}
		return false;
	}
	
	
	public static Conversation getConversation()
	{
		/*
		 * TODO add provider to plug into entity manager administration for
		 * Vaadin Session independency
		 */
		if(isSessionAccessible())
		{
			final Conversationable conversationable = (Conversationable)VaadinSession.getCurrent()
					.getAttribute(ENTITY_MANAGER_ATTRIBUTE);
			return conversationable.getConversation();
		}
		return null;
	}
	
	
	private static Conversation newConversation()
	{
		if(isSessionAccessible())
		{
			final Conversationable conversationable = (Conversationable)VaadinSession.getCurrent()
					.getAttribute(ENTITY_MANAGER_ATTRIBUTE);
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
	
	
	public static void startConversation()
	{
		final Conversation conversation = newConversation();
		if(conversation != null)
		{
			conversation.start();
		}
	}
	
	
	public static void startPessimisticConversation(final LockModeType lockMode)
	{
		final Conversation conversation = newConversation();
		if(conversation != null)
		{
			conversation.setPessimisticUnit(true,lockMode);
			conversation.start();
		}
	}
	
	
	public static void lockConversation(final Object entity)
	{
		final EntityManager em = EntityManagerUtils.getEntityManager();
		if(em != null)
		{
			final Conversation conversation = getConversation();
			if(conversation != null)
			{
				em.lock(entity,conversation.getLockModeType());
			}
		}
	}
	
	
	public static void lockConversation(final Object entity, final Map<String, Object> properties)
	{
		final EntityManager em = EntityManagerUtils.getEntityManager();
		if(em != null)
		{
			final Conversation conversation = getConversation();
			if(conversation != null)
			{
				em.lock(entity,conversation.getLockModeType(),properties);
			}
		}
	}
	
	
	public static void releaseConversationLock()
	{
		final EntityManager em = EntityManagerUtils.getEntityManager();
		if(em != null)
		{
			if(em.getTransaction().isActive())
			{
				em.getTransaction().commit();
			}
		}
	}
	
	
	public static void startPessimisticConversation()
	{
		final Conversation conversation = newConversation();
		if(conversation != null)
		{
			conversation.setPessimisticUnit(true,LockModeType.WRITE);
			conversation.start();
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
	
	
	public static boolean isConversationActive()
	{
		final Conversation conversation = getConversation();
		if(conversation != null)
		{
			return conversation.isActive();
		}
		return false;
	}
	
}
