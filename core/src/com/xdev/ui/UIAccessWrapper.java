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

package com.xdev.ui;


import javax.persistence.EntityManager;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.xdev.communication.Conversation;
import com.xdev.communication.XdevServlet;
import com.xdev.communication.XdevServletService;


/**
 * Runnable wrapper used in {@link UI#access(Runnable)} and
 * {@link UI#accessSynchronously(Runnable)}.
 * <p>
 * It ensures the proper handling of the application's {@link EntityManager} and
 * conversational state.
 *
 * @see EntityManager
 * @see Conversation
 * 		
 * @author XDEV Software
 * 		
 */
public class UIAccessWrapper implements Runnable
{
	private final Runnable runnable;


	public UIAccessWrapper(final Runnable runnable)
	{
		this.runnable = runnable;
	}


	@Override
	public void run()
	{
		final XdevServletService service = XdevServlet.getServlet().getService();
		final VaadinSession session = UI.getCurrent().getSession();
		
		try
		{
			service.handleRequestStart(session);

			this.runnable.run();
		}
		finally
		{
			service.handleRequestEnd(session);
		}
	}

	// protected void preRun()
	// {
	// final EntityManagerFactory factory = getEntityManagerFactory();
	// if(factory != null)
	// {
	// final EntityManager em = EntityManagerUtils.getEntityManager();
	// if(em == null)
	// {
	// startExclusiveWorkingUnit(factory);
	// }
	// else
	// {
	// if(!em.isOpen())
	// {
	// startExclusiveWorkingUnit(factory);
	// }
	// }
	// }
	// }
	//
	//
	// protected void postRun()
	// {
	// final EntityManager em = EntityManagerUtils.getEntityManager();
	// if(em != null)
	// {
	// if(EntityManagerUtils.getConversation() != null)
	// {
	// /*
	// * Keep the session and with it the persistence context alive
	// * during user think time while a conversation is active. The
	// * next request will automatically be handled by an appropriate
	// * conversation managing strategy.
	// */
	// if(EntityManagerUtils.getConversation().isActive())
	// {
	// try
	// {
	// // end unit of work
	// em.getTransaction().commit();
	// }
	// catch(final RollbackException e)
	// {
	// em.getTransaction().rollback();
	// }
	// }
	// }
	// else
	// {
	// try
	// {
	// // end unit of work
	// em.getTransaction().commit();
	// }
	// catch(final RollbackException e)
	// {
	// em.getTransaction().rollback();
	// }
	// try
	// {
	// em.close();
	// }
	// catch(final Exception e)
	// {
	// if(em != null)
	// {
	// final EntityTransaction tx = em.getTransaction();
	// if(tx != null && tx.isActive())
	// {
	// em.getTransaction().rollback();
	// }
	// }
	// }
	// }
	// }
	// }
	//
	//
	// protected EntityManagerFactory getEntityManagerFactory()
	// {
	// EntityManagerFactory factory =
	// EntityManagerUtils.getEntityManagerFactory();
	// if(factory == null)
	// {
	// try
	// {
	// final String hibernatePersistenceUnit =
	// UI.getCurrent().getSession().getService()
	// .getDeploymentConfiguration().getApplicationOrSystemProperty(
	// XdevServletService.HIBERNATEUTIL_FILTER_INIT_PARAM,null);
	// factory = EntityManagerUtils.initializeHibernateFactory(
	// new
	// EntityManagerFactoryProvider.Implementation(hibernatePersistenceUnit));
	// }
	// catch(final PersistenceException e)
	// {
	// Logger.getLogger(XdevServlet.class.getName()).log(Level.WARNING,e.getMessage(),e);
	// }
	// }
	//
	// return factory;
	// }
	//
	//
	// protected void startExclusiveWorkingUnit(final EntityManagerFactory
	// factory)
	// {
	// final EntityManager manager = factory.createEntityManager();
	//
	// // instantiate conversationable wrapper with entity
	// // manager.
	// final Conversationable.Implementation conversationable = new
	// Conversationable.Implementation();
	// conversationable.setEntityManager(manager);
	//
	// // Begin a database transaction, start the unit of
	// // work
	// manager.getTransaction().begin();
	// UI.getCurrent().getSession().setAttribute(EntityManagerUtils.ENTITY_MANAGER_ATTRIBUTE,
	// conversationable);
	// }
}
