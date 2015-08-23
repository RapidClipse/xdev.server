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


import java.util.Map;

import javax.persistence.LockModeType;

import com.vaadin.server.VaadinSession;


public class ConversationUtils
{

	private final static String ENTITY_MANAGER_ATTRIBUTE = "EntityManager";


	public static Conversation getConversation()
	{
		/*
		 * TODO add provider to plug into entity manager administration for
		 * Vaadin Session independency
		 */
		final Conversationable conversationable = (Conversationable)VaadinSession.getCurrent()
				.getAttribute(ENTITY_MANAGER_ATTRIBUTE);
		return conversationable.getConversation();
	}


	private static Conversation newConversation()
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


	public static void startConversation()
	{
		newConversation().start();
	}


	public static void startPessimisticConversation(final LockModeType lockMode)
	{
		final Conversation conversation = newConversation();
		conversation.setPessimisticUnit(true,lockMode);
		conversation.start();
	}


	public static void lockConversation(final Object entity)
	{
		EntityManagerUtils.getEntityManager().lock(entity,getConversation().getLockModeType());
	}


	public static void lockConversation(final Object entity, final Map<String, Object> properties)
	{
		EntityManagerUtils.getEntityManager().lock(entity,getConversation().getLockModeType(),
				properties);
	}


	public static void startPessimisticConversation()
	{
		final Conversation conversation = newConversation();
		conversation.setPessimisticUnit(true,LockModeType.WRITE);
		conversation.start();
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
		return getConversation().isActive();
	}

}
