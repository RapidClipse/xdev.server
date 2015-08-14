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


/**
 * @author XDEV Software
 *
 */
public interface Conversationable
{

	public void setEntityManager(EntityManager em);


	public EntityManager getEntityManager();


	public void setConversation(Conversation conversation);


	public Conversation getConversation();



	public class Implementation implements Conversationable
	{
		private EntityManager	em;
		private Conversation	conversation;


		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * com.xdev.communication.ConversationableEntityManager#setEntityManager
		 * (javax.persistence.EntityManager)
		 */
		@Override
		public void setEntityManager(final EntityManager em)
		{
			this.em = em;
		}


		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * com.xdev.communication.ConversationableEntityManager#getEntityManager
		 * ()
		 */
		@Override
		public EntityManager getEntityManager()
		{
			return this.em;
		}


		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * com.xdev.communication.ConversationableEntityManager#setConversation(
		 * com.xdev.communication.Conversation)
		 */
		@Override
		public void setConversation(final Conversation conversation)
		{
			if(this.conversation != null)
			{
				if(this.conversation.isActive())
				{
					throw new RuntimeException(
							"Another Conversation is already running, one cannot run multipleconversations");
				}
				else
				{
					this.conversation = conversation;
				}
			}
			else
			{
				this.conversation = conversation;
			}
		}


		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * com.xdev.communication.ConversationableEntityManager#getConversation(
		 * )
		 */
		@Override
		public Conversation getConversation()
		{
			return this.conversation;
		}

	}

}
