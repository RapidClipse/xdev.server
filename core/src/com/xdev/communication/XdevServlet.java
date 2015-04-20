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


import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionExpiredException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WebBrowser;
import com.xdev.db.connection.HibernateUtils;


/**
 * Extended {@link Servlet} with full control over each type of communication,
 * including websockets.
 *
 * @author XDEV Software
 */
public class XdevServlet extends VaadinServlet
{
	static final String	HIBERNATEUTIL_FILTER_INIT_PARAM	= "persistenceUnit";
	
	
	@Override
	protected VaadinServletService createServletService(
			final DeploymentConfiguration deploymentConfiguration) throws ServiceException
	{
		final VaadinServletService servletService = new VaadinServletService(this,
				deploymentConfiguration)
		{
			private boolean	hibernateFactoryInitialized	= false;


			@Override
			public void requestStart(final VaadinRequest request, final VaadinResponse response)
			{
				if(!this.hibernateFactoryInitialized)
				{
					this.hibernateFactoryInitialized = true;

					try
					{
						final String hibernatePersistenceUnit = deploymentConfiguration
								.getApplicationOrSystemProperty(HIBERNATEUTIL_FILTER_INIT_PARAM,
										null);
						EntityManagerHelper
								.initializeHibernateFactory(new HibernateUtils.Implementation(
										hibernatePersistenceUnit));
					}
					catch(final PersistenceException e)
					{
						Logger.getLogger(XdevServlet.class.getName()).log(Level.WARNING,
								e.getMessage(),e);
					}
				}

				if(request.getMethod().equals("POST"))
				{
					try
					{
						final EntityManagerFactory factory = EntityManagerHelper
								.getEntityManagerFactory();
						if(factory != null)
						{
							final EntityManager manager = factory.createEntityManager();
							final VaadinSession session = findVaadinSession(request);
							// Add the EntityManager to the session
							session.setAttribute(EntityManagerHelper.ENTITY_MANAGER_ATTRIBUTE,
									manager);
						}
					}
					catch(PersistenceException | ServiceException | SessionExpiredException e)
					{
						Logger.getLogger(XdevServlet.class.getName()).log(Level.WARNING,
								e.getMessage(),e);
					}
				}

				super.requestStart(request,response);
			}


			@Override
			public void requestEnd(final VaadinRequest request, final VaadinResponse response,
					final VaadinSession session)
			{
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

				super.requestEnd(request,response,session);
			}
		};
		servletService.init();
		return servletService;
	}
	
	
	@Override
	protected void servletInitialized() throws ServletException
	{
		super.servletInitialized();
		
		getService().addSessionInitListener(event -> initSession(event));
	}
	
	
	protected void initSession(final SessionInitEvent event)
	{
		final WebBrowser browser = new WebBrowser();
		browser.updateRequestDetails(event.getRequest());
		if(browser.isAndroid() || browser.isIOS() || browser.isWindowsPhone())
		{
			event.getSession().addBootstrapListener(new BootstrapListener()
			{
				@Override
				public void modifyBootstrapPage(final BootstrapPageResponse response)
				{
					response.getDocument().head().prependElement("meta").attr("name","viewport")
							.attr("content","width=device-width, initial-scale=1.0"
							// + ", maximum-scale=1.0, user-scalable=0"
							);
				}
				
				
				@Override
				public void modifyBootstrapFragment(final BootstrapFragmentResponse response)
				{
				}
			});
		}
	}
}
