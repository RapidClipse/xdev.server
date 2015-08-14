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


import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionExpiredException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;


/**
 * @author XDEV Software
 *
 */
public interface VaadinSessionStrategyProvider
{
	public VaadinSessionStrategy getVaadinSessionStrategy(final VaadinRequest request,
			final VaadinService service);



	public class Implementation implements VaadinSessionStrategyProvider
	{

		/*
		 * (non-Javadoc)
		 *
		 * @see com.xdev.communication.VaadinSessionStrategyProvider#
		 * getVaadinSessionStrategy(com.vaadin.server.VaadinRequest,
		 * com.vaadin.server.VaadinService)
		 */
		@Override
		public VaadinSessionStrategy getVaadinSessionStrategy(final VaadinRequest request,
				final VaadinService service) throws RuntimeException
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
					.getAttribute(EntityManagerUtil.ENTITY_MANAGER_ATTRIBUTE);

			if(conversationable != null)
			{
				if(conversationable.getConversation() != null)
				{
					if(conversationable.getConversation().isActive())
					{
						if(conversationable.getConversation().isPessimisticUnit())
						{
							return new VaadinSessionStrategy.PerConversationPessimistic();
						}
						else
						{
							return new VaadinSessionStrategy.PerConversation();
						}
					}
				}
			}

			// return default strategy
			return new VaadinSessionStrategy.PerRequest();
		}
	}
}
