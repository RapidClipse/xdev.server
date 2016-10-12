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


import java.util.HashMap;
import java.util.Map;


/**
 * @author XDEV Software
 *
 */
public interface VaadinSessionStrategyProvider
{
	public VaadinSessionStrategy getRequestStartVaadinSessionStrategy(
			Conversationables conversationables, String persistenceUnit);


	public VaadinSessionStrategy getRequestEndVaadinSessionStrategy(
			Conversationables conversationables, String persistenceUnit);



	public class Implementation implements VaadinSessionStrategyProvider
	{
		protected final VaadinSessionStrategy				perRequest					= new VaadinSessionStrategy.PerRequest();
		protected final VaadinSessionStrategy				perConversation				= new VaadinSessionStrategy.PerConversation();
		protected final VaadinSessionStrategy				perConversationPessimistic	= new VaadinSessionStrategy.PerConversationPessimistic();
		protected final Map<String, VaadinSessionStrategy>	currentStrategies			= new HashMap<>();


		protected VaadinSessionStrategy storeSessionStrategy(final String persistenceUnit,
				final VaadinSessionStrategy strategy)
		{
			this.currentStrategies.put(persistenceUnit,strategy);
			return strategy;
		}


		@Override
		public VaadinSessionStrategy getRequestStartVaadinSessionStrategy(
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
		public VaadinSessionStrategy getRequestEndVaadinSessionStrategy(
				final Conversationables conversationables, final String persistenceUnit)
		{
			/*
			 * end request with existing strategy - don't exchange strategies
			 * between request / response
			 */
			final VaadinSessionStrategy strategy = this.currentStrategies.get(persistenceUnit);
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
