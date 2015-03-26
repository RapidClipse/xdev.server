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
 
package com.xdev.server.util;


import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.xdev.server.communication.EntityManagerHelper;


public class AccessUtils
{
	public static Future<Void> access(final Runnable runnable)
	{
		final EntityManagerFactory factory = EntityManagerHelper.getEntityManagerFactory();
		if(factory != null)
		{
			final EntityManager manager = factory.createEntityManager();
			// Add the EntityManager to the session
			VaadinSession.getCurrent().setAttribute("HibernateEntityManager",manager);
		}

		final Future<Void> future = UI.getCurrent().access(runnable);

		try
		{
			EntityManagerHelper.closeEntityManager();
		}
		catch(final Exception e)
		{
			if(EntityManagerHelper.getEntityManager() != null)
			{
				final EntityTransaction tx = EntityManagerHelper.getTransaction();
				if(tx != null && tx.isActive())
				{
					EntityManagerHelper.rollback();
				}
			}
		}

		return future;
	}
}
