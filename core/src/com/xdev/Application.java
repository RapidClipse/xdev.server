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

package com.xdev;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import com.xdev.communication.SessionStrategyProvider;
import com.xdev.persistence.PersistenceManager;
import com.xdev.util.concurrent.XdevExecutorService;


/**
 * Central registry for application-global services.
 *
 * @author XDEV Software
 * @since 3.0
 *
 */
public final class Application
{
	private static XdevExecutorService		executorService;
	private static PersistenceManager		persistenceManager;
	private static SessionStrategyProvider	sessionStrategyProvider;


	/**
	 * @return the executorService
	 */
	public static XdevExecutorService getExecutorService()
	{
		return executorService;
	}


	/**
	 * @return the persistenceManager
	 */
	public static PersistenceManager getPersistenceManager()
	{
		return persistenceManager;
	}


	/**
	 * @return the sessionStrategyProvider
	 */
	public static SessionStrategyProvider getSessionStrategyProvider()
	{
		return sessionStrategyProvider;
	}


	/**
	 * @param servletContext
	 * @noapi
	 */
	public static void init(final ServletContext servletContext)
	{
		if(executorService == null)
		{
			executorService = createXdevExecutorService(servletContext);
			persistenceManager = createPersistenceManager(servletContext);
			sessionStrategyProvider = createSessionStrategyProvider(servletContext);
		}
	}


	private static XdevExecutorService createXdevExecutorService(final ServletContext context)
	{
		final String className = context.getInitParameter("xdev.executorService.factory");
		if(className != null && className.length() > 0)
		{
			try
			{
				final XdevExecutorService.Factory factory = (XdevExecutorService.Factory)Class
						.forName(className).newInstance();
				return factory.createXdevExecutorService(context);
			}
			catch(final Throwable t)
			{
				Logger.getLogger(Application.class).error(t.getMessage(),t);
			}
		}

		return new XdevExecutorService.Implementation(context);
	}


	private static PersistenceManager createPersistenceManager(final ServletContext context)
	{
		final String className = context.getInitParameter("xdev.persistenceManager.factory");
		if(className != null && className.length() > 0)
		{
			try
			{
				final PersistenceManager.Factory factory = (PersistenceManager.Factory)Class
						.forName(className).newInstance();
				return factory.createPersistenceManager(context);
			}
			catch(final Throwable t)
			{
				Logger.getLogger(Application.class).error(t.getMessage(),t);
			}
		}

		return new PersistenceManager.Implementation(context);
	}


	private static SessionStrategyProvider createSessionStrategyProvider(
			final ServletContext context)
	{
		final String className = context.getInitParameter("xdev.sessionStrategyProvider.factory");
		if(className != null && className.length() > 0)
		{
			try
			{
				final SessionStrategyProvider.Factory factory = (SessionStrategyProvider.Factory)Class
						.forName(className).newInstance();
				return factory.createSessionStrategyProvider(context);
			}
			catch(final Throwable t)
			{
				Logger.getLogger(Application.class).error(t.getMessage(),t);
			}
		}

		return new SessionStrategyProvider.Implementation();
	}



	@WebListener
	public static class ContextListener implements ServletContextListener
	{
		@Override
		public void contextInitialized(final ServletContextEvent event)
		{
			Application.init(event.getServletContext());
		}


		@Override
		public void contextDestroyed(final ServletContextEvent event)
		{
			Application.executorService.shutdown();
			Application.persistenceManager.close();
		}
	}


	/*
	 * no instantiation
	 */
	private Application()
	{
	}
}
