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


import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;


/**
 * @author XDEV Software
 *
 */
public interface SessionStrategyProvider
{
	public static interface Factory
	{
		public SessionStrategyProvider createSessionStrategyProvider(final ServletContext context);
	}


	public SessionStrategy getRequestStartSessionStrategy(Conversationables conversationables,
			String persistenceUnit);
	
	
	public SessionStrategy getRequestEndSessionStrategy(Conversationables conversationables,
			String persistenceUnit);
	
	
	
	public class Implementation implements SessionStrategyProvider
	{
		protected final SessionStrategy					perRequest					= new SessionStrategy.PerRequest();
		protected final SessionStrategy					perConversation				= new SessionStrategy.PerConversation();
		protected final SessionStrategy					perConversationPessimistic	= new SessionStrategy.PerConversationPessimistic();
		protected final Map<String, SessionStrategy>	currentStrategies			= new HashMap<>();
		
		
		protected SessionStrategy storeSessionStrategy(final String persistenceUnit,
				final SessionStrategy strategy)
		{
			this.currentStrategies.put(persistenceUnit,strategy);
			return strategy;
		}
		
		
		@Override
		public SessionStrategy getRequestStartSessionStrategy(
				final Conversationables conversationables, final String persistenceUnit)
				throws RuntimeException
		{
			final Conversationable conversationable = conversationables.get(persistenceUnit);
			
			if(conversationable != null)
			{
				if(conversationable.getConversation() != null)
				{
					if(conversationable.getConversation().isActive())
					{
						if(conversationable.getConversation().isPessimisticUnit())
						{
							return storeSessionStrategy(persistenceUnit,
									this.perConversationPessimistic);
						}
						else
						{
							return storeSessionStrategy(persistenceUnit,this.perConversation);
						}
					}
				}
			}
			
			// return default strategy
			return storeSessionStrategy(persistenceUnit,this.perRequest);
		}
		
		
		@Override
		public SessionStrategy getRequestEndSessionStrategy(
				final Conversationables conversationables, final String persistenceUnit)
		{
			/*
			 * end request with existing strategy - don't exchange strategies
			 * between request / response
			 */
			final SessionStrategy strategy = this.currentStrategies.get(persistenceUnit);
			if(strategy != null)
			{
				return strategy;
			}
			
			final Conversationable conversationable = conversationables.get(persistenceUnit);
			
			if(conversationable != null)
			{
				if(conversationable.getConversation() != null)
				{
					if(conversationable.getConversation().isActive())
					{
						if(conversationable.getConversation().isPessimisticUnit())
						{
							return storeSessionStrategy(persistenceUnit,
									this.perConversationPessimistic);
						}
						else
						{
							return storeSessionStrategy(persistenceUnit,this.perConversation);
						}
					}
				}
			}
			
			// return default strategy
			return storeSessionStrategy(persistenceUnit,this.perRequest);
		}
	}
}
