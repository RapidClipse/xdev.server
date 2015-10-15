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


import java.util.Enumeration;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.vaadin.server.VaadinSession;


/**
 * @author XDEV Software
 * 		
 */
public class XdevHttpSessionListener implements HttpSessionListener
{

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.
	 * HttpSessionEvent)
	 */
	@Override
	public void sessionCreated(final HttpSessionEvent se)
	{
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.
	 * http.HttpSessionEvent)
	 */
	@Override
	public void sessionDestroyed(final HttpSessionEvent se)
	{
		VaadinSession vSession = null;
		final Enumeration<String> attributeNames = se.getSession().getAttributeNames();

		while(attributeNames.hasMoreElements())
		{
			final Object attribute = se.getSession().getAttribute(attributeNames.nextElement());
			if(attribute instanceof VaadinSession)
			{
				vSession = (VaadinSession)attribute;
			}

		}

		if(vSession != null)
		{
			final Conversationable conversationable = (Conversationable)vSession
					.getAttribute(EntityManagerUtils.ENTITY_MANAGER_ATTRIBUTE);

			if(conversationable != null)
			{
				final EntityManager em = conversationable.getEntityManager();

				if(em != null)
				{
					if(em.getTransaction().isActive())
					{
						try
						{
							// end unit of work
							em.getTransaction().commit();
						}
						catch(final RollbackException e)
						{
							em.getTransaction().rollback();
						}
					}

					try
					{
						em.close();
					}
					catch(final Exception e)
					{
						if(em != null)
						{
							final EntityTransaction tx = em.getTransaction();
							if(tx != null && tx.isActive())
							{
								em.getTransaction().rollback();
							}
						}
					}
				}
			}
		}
	}

}
