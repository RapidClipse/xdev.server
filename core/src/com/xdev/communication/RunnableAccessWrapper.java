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

import com.vaadin.server.VaadinSession;
import com.vaadin.util.CurrentInstance;
import com.xdev.persistence.PersistenceManager;


/**
 * Runnable wrapper for threads outside vaadin's session scope, e.g. web
 * services.
 * <p>
 * It ensures the proper handling of the application's {@link EntityManager} and
 * conversational state.
 *
 * @see EntityManager
 * @see Conversation
 * @see CallableAccessWrapper
 * 
 * @author XDEV Software
 * 
 * @since 1.3
 */
public class RunnableAccessWrapper implements Runnable
{
	private final Runnable		runnable;
	private final VaadinSession	session;


	public RunnableAccessWrapper(final Runnable runnable)
	{
		this(runnable,VaadinSession.getCurrent());
	}


	public RunnableAccessWrapper(final Runnable runnable, final VaadinSession session)
	{
		this.runnable = runnable;
		this.session = session;
	}
	
	
	/**
	 * @return the session
	 * @since 3.0
	 */
	public VaadinSession getSession()
	{
		return this.session;
	}


	@Override
	public void run()
	{
		final XdevServletService service = XdevServletService.getCurrent();

		if(this.session != null)
		{
			try
			{
				service.handleRequestStart(this.session);

				this.runnable.run();
			}
			finally
			{
				service.handleRequestEnd(this.session);
			}
		}
		else
		{
			final PersistenceManager persistenceManager = PersistenceManager
					.get(service.getServlet().getServletContext());
			CurrentInstance.set(PersistenceManager.class,persistenceManager);

			final Conversationables conversationables = new Conversationables();
			CurrentInstance.set(Conversationables.class,conversationables);

			final VaadinSessionStrategyProvider sessionStrategyProvider = createVaadinSessionStrategyProvider();
			for(final String persistenceUnit : persistenceManager.getPersistenceUnits())
			{
				sessionStrategyProvider
						.getRequestStartVaadinSessionStrategy(conversationables,persistenceUnit)
						.requestStart(conversationables,persistenceUnit);
			}

			try
			{
				this.runnable.run();
			}
			finally
			{
				for(final String persistenceUnit : persistenceManager.getPersistenceUnits())
				{
					sessionStrategyProvider
							.getRequestEndVaadinSessionStrategy(conversationables,persistenceUnit)
							.requestEnd(conversationables,persistenceUnit);
				}
				
				CurrentInstance.set(PersistenceManager.class,null);
				CurrentInstance.set(Conversationables.class,null);
			}
		}
	}


	protected VaadinSessionStrategyProvider createVaadinSessionStrategyProvider()
	{
		return new VaadinSessionStrategyProvider.Implementation();
	}
}
