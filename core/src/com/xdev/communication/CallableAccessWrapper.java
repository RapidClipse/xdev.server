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


import java.util.concurrent.Callable;

import javax.persistence.EntityManager;

import com.vaadin.util.CurrentInstance;
import com.xdev.Application;
import com.xdev.persistence.PersistenceManager;


/**
 * Callable wrapper for threads outside vaadin's session scope, e.g. web
 * services.
 * <p>
 * It ensures the proper handling of the application's {@link EntityManager} and
 * conversational state.
 *
 * @see EntityManager
 * @see Conversation
 * @see RunnableAccessWrapper
 *
 * @author XDEV Software
 *
 * @since 1.2
 */
public class CallableAccessWrapper<V> implements Callable<V>
{
	public static <T> T execute(final Callable<T> callable) throws Exception
	{
		return new CallableAccessWrapper<>(callable).call();
	}

	private final Callable<V> callable;


	public CallableAccessWrapper(final Callable<V> callable)
	{
		this.callable = callable;
	}


	@Override
	public V call() throws Exception
	{
		final PersistenceManager persistenceManager = Application.getPersistenceManager();
		
		final Conversationables conversationables = new Conversationables();
		CurrentInstance.set(Conversationables.class,conversationables);

		final SessionStrategyProvider sessionStrategyProvider = Application
				.getSessionStrategyProvider();
		for(final String persistenceUnit : persistenceManager.getPersistenceUnits())
		{
			sessionStrategyProvider
					.getRequestStartSessionStrategy(conversationables,persistenceUnit)
					.requestStart(conversationables,persistenceUnit);
		}

		try
		{
			return this.callable.call();
		}
		finally
		{
			for(final String persistenceUnit : persistenceManager.getPersistenceUnits())
			{
				sessionStrategyProvider
						.getRequestEndSessionStrategy(conversationables,persistenceUnit)
						.requestEnd(conversationables,persistenceUnit);
			}

			CurrentInstance.set(Conversationables.class,null);
		}
	}
}
