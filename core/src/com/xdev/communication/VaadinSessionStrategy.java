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


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;

import org.hibernate.FlushMode;
import org.hibernate.Session;

import com.xdev.persistence.PersistenceManager;


/**
 * Manages Session propagation.
 *
 * @author XDEV Software
 * 
 */
public interface VaadinSessionStrategy
{
	public void requestStart(Conversationables conversationables, String persistenceUnit);


	public void requestEnd(Conversationables conversationables, String persistenceUnit);



	/**
	 * Request / Response propagation to avoid session per operation anti
	 * pattern.
	 *
	 * @author XDEV Software
	 * 
	 */
	public class PerRequest implements VaadinSessionStrategy
	{
		@Override
		public void requestStart(final Conversationables conversationables,
				final String persistenceUnit)
		{
			final EntityManagerFactory factory = PersistenceManager.getCurrent()
					.getEntityManagerFactory(persistenceUnit);
			final EntityManager manager = factory.createEntityManager();
			
			// instantiate conversationable wrapper with entity manager.
			final Conversationable.Implementation conversationable = new Conversationable.Implementation();
			conversationable.setEntityManager(manager);
			
			// Begin a database transaction, start the unit of work
			manager.getTransaction().begin();
			
			conversationables.put(persistenceUnit,conversationable);
		}
		
		
		@Override
		public void requestEnd(final Conversationables conversationables,
				final String persistenceUnit)
		{
			final Conversationable conversationable = conversationables.get(persistenceUnit);
			if(conversationable != null)
			{
				final EntityManager em = conversationable.getEntityManager();
				if(em != null)
				{
					final Conversation conversation = conversationable.getConversation();
					if(conversation != null)
					{
						/*
						 * Keep the session and with it the persistence context
						 * alive during user think time while a conversation is
						 * active. The next request will automatically be
						 * handled by an appropriate conversation managing
						 * strategy.
						 */
						if(conversation.isActive())
						{
							final EntityTransaction transaction = em.getTransaction();
							if(transaction.isActive())
							{
								try
								{
									// end unit of work
									transaction.commit();
								}
								catch(final RollbackException e)
								{
									transaction.rollback();
								}
							}
						}
					}
					else
					{
						conversationables.close(conversationable);
					}
				}
			}
		}
	}
	
	
	
	/**
	 * Extended persistence context pattern.
	 *
	 * @author XDEV Software
	 * 
	 */
	public class PerConversation implements VaadinSessionStrategy
	{
		@Override
		public void requestStart(final Conversationables conversationables,
				final String persistenceUnit)
		{
			final Conversationable conversationable = conversationables.get(persistenceUnit);
			if(conversationable != null)
			{
				final EntityManager em = conversationable.getEntityManager();
				if(em != null)
				{
					final Session session = em.unwrap(Session.class);
					if(session.getHibernateFlushMode() != FlushMode.MANUAL)
					{
						/*
						 * Prepare conversation - disable AUTO flush mode for
						 * each transaction commit, to achieve the conversation
						 * unit of work context.
						 */
						session.setHibernateFlushMode(FlushMode.MANUAL);
					}
					
					/*
					 * Begin a database transaction, reconnects Session -
					 * continues the unit of work
					 */
					final EntityTransaction transaction = em.getTransaction();
					if(!transaction.isActive())
					{
						transaction.begin();
					}
				}
			}
		}
		
		
		@Override
		public void requestEnd(final Conversationables conversationables,
				final String persistenceUnit)
		{
			final Conversationable conversationable = conversationables.get(persistenceUnit);
			if(conversationable != null)
			{
				final Conversation conversation = conversationable.getConversation();
				if(conversation != null)
				{
					final EntityManager em = conversationable.getEntityManager();
					if(em != null)
					{
						final EntityTransaction transaction = em.getTransaction();
						if(conversation.isActive())
						{
							// Event was not the last request, continue
							// conversation
							if(transaction.isActive())
							{
								try
								{
									transaction.commit();
								}
								catch(final RollbackException e)
								{
									transaction.rollback();
								}
							}
							
						}
						else
						{
							/*
							 * The event was the last request: flush, commit,
							 * close
							 */
							em.flush();
							if(transaction.isActive())
							{
								try
								{
									transaction.commit();
								}
								catch(final RollbackException e)
								{
									transaction.rollback();
								}
							}
							em.close();
						}
					}
				}
			}
		}
	}
	
	
	
	/**
	 * Extended persistence context pattern.
	 *
	 * @author XDEV Software
	 * 
	 */
	public class PerConversationPessimistic extends PerConversation
	{
		@Override
		public void requestEnd(final Conversationables conversationables,
				final String persistenceUnit)
		{
			final Conversationable conversationable = conversationables.get(persistenceUnit);
			if(conversationable != null)
			{
				final Conversation conversation = conversationable.getConversation();
				if(conversation != null)
				{
					final EntityManager em = conversationable.getEntityManager();
					if(em != null)
					{
						final EntityTransaction transaction = em.getTransaction();
						if(!conversation.isActive())
						{
							/*
							 * The event was the last request: flush, commit,
							 * close
							 */
							em.flush();
							if(transaction.isActive())
							{
								try
								{
									transaction.commit();
								}
								catch(final RollbackException e)
								{
									transaction.rollback();
								}
							}
							em.close();
						}
					}
				}
			}
		}
	}
}
