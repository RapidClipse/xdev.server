/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
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


import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionExpiredException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;


/**
 * @author XDEV Software Julian Will
 * 		
 */
public interface VaadinSessionStrategyProvider
{
	public VaadinSessionStrategy getRequestStartVaadinSessionStrategy(final VaadinRequest request,
			final VaadinService service);
			
			
	public VaadinSessionStrategy getRequestEndVaadinSessionStrategy(final VaadinRequest request,
			final VaadinService service);
			
			
			
	public class Implementation implements VaadinSessionStrategyProvider
	{
		private VaadinSessionStrategy currentStrategy;
		
		
		private VaadinSessionStrategy getSessionStrategy(
				final Class<? extends VaadinSessionStrategy> strategy)
		{
			if(this.currentStrategy != null)
			{
				// do not instantiate new type if new type matches with old
				if(this.currentStrategy.getClass().isAssignableFrom(strategy))
				{
					return currentStrategy;
				}
				else
				{
					// types to not match, create new strategy lazily
					try
					{
						this.currentStrategy = strategy.newInstance();
						return this.currentStrategy;
					}
					catch(InstantiationException | IllegalAccessException e)
					{
						throw new RuntimeException(e);
					}
				}
			}
			try
			{
				this.currentStrategy = strategy.newInstance();
			}
			catch(InstantiationException | IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
			
			return this.currentStrategy;
		}
		
		
		/*
		 * (non-Javadoc)
		 *
		 * @see com.xdev.communication.VaadinSessionStrategyProvider#
		 * getVaadinSessionStrategy(com.vaadin.server.VaadinRequest,
		 * com.vaadin.server.VaadinService)
		 */
		@Override
		public VaadinSessionStrategy getRequestStartVaadinSessionStrategy(
				final VaadinRequest request, final VaadinService service) throws RuntimeException
		{
			VaadinSession session;
			try
			{
				session = service.findVaadinSession(request);
			}
			catch(ServiceException | SessionExpiredException e)
			{
				throw new RuntimeException(e);
			}
			
			final Conversationable conversationable = (Conversationable)session
					.getAttribute(EntityManagerUtils.ENTITY_MANAGER_ATTRIBUTE);
					
			if(conversationable != null)
			{
				if(conversationable.getConversation() != null)
				{
					if(conversationable.getConversation().isActive())
					{
						if(conversationable.getConversation().isPessimisticUnit())
						{
							return this.getSessionStrategy(
									VaadinSessionStrategy.PerConversationPessimistic.class);
						}
						else
						{
							return this.getSessionStrategy(
									VaadinSessionStrategy.PerConversation.class);
						}
					}
				}
			}
			
			// return default strategy
			return this.getSessionStrategy(VaadinSessionStrategy.PerRequest.class);
		}
		
		
		/*
		 * (non-Javadoc)
		 *
		 * @see com.xdev.communication.VaadinSessionStrategyProvider#
		 * getRequestEndVaadinSessionStrategy(com.vaadin.server.VaadinRequest,
		 * com.vaadin.server.VaadinService)
		 */
		@Override
		public VaadinSessionStrategy getRequestEndVaadinSessionStrategy(final VaadinRequest request,
				final VaadinService service)
		{
			/*
			 * end request with existing strategy - don't exchange strategies
			 * between request / response
			 */
			if(this.currentStrategy != null)
			{
				return this.currentStrategy;
			}
			else
			{
				VaadinSession session;
				try
				{
					session = service.findVaadinSession(request);
				}
				catch(ServiceException | SessionExpiredException e)
				{
					throw new RuntimeException(e);
				}
				
				final Conversationable conversationable = (Conversationable)session
						.getAttribute(EntityManagerUtils.ENTITY_MANAGER_ATTRIBUTE);
						
				if(conversationable != null)
				{
					if(conversationable.getConversation() != null)
					{
						if(conversationable.getConversation().isActive())
						{
							if(conversationable.getConversation().isPessimisticUnit())
							{
								return this.getSessionStrategy(
										VaadinSessionStrategy.PerConversationPessimistic.class);
							}
							else
							{
								return this.getSessionStrategy(
										VaadinSessionStrategy.PerConversation.class);
							}
						}
					}
				}
			}
			// return default strategy
			return this.getSessionStrategy(VaadinSessionStrategy.PerRequest.class);
		}
	}
}
