
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
		final Future<Void> future = UI.getCurrent().access(new Runnable()
		{
			@Override
			public void run()
			{
				final EntityManagerFactory factory = EntityManagerHelper.getEntityManagerFactory();
				if(factory != null)
				{
					final EntityManager manager = factory.createEntityManager();
					// Add the EntityManager to the session
					VaadinSession.getCurrent().setAttribute("HibernateEntityManager",manager);
				}
				
				runnable.run();
				
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
			}
		});
		
		return future;
	}
}
