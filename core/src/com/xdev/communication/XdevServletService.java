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


import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ApplicationConstants;
import com.xdev.Application;


/**
 * @author XDEV Software
 *
 */
public class XdevServletService extends VaadinServletService
{
	public XdevServletService(final XdevServlet servlet,
			final DeploymentConfiguration deploymentConfiguration) throws ServiceException
	{
		super(servlet,deploymentConfiguration);
	}
	
	
	@Override
	public void init() throws ServiceException
	{
		addSessionDestroyListener(event -> {
			final VaadinSession session = event.getSession();
			final Conversationables conversationables = session
					.getAttribute(Conversationables.class);
			if(conversationables != null)
			{
				conversationables.closeAll();
			}
		});
		
		super.init();
	}
	
	
	protected boolean isHeartbeatRequest(final VaadinRequest request)
	{
		final String pathInfo = request.getPathInfo();
		return pathInfo != null
				&& pathInfo.startsWith("/" + ApplicationConstants.HEARTBEAT_PATH + "/");
	}
	
	
	@Override
	public void requestStart(final VaadinRequest request, final VaadinResponse response)
	{
		if(request.getMethod().equals("POST") && !isHeartbeatRequest(request))
		{
			try
			{
				final VaadinSession session = findVaadinSession(request);
				if(session != null)
				{
					handleRequestStart(session);
				}
			}
			catch(final Exception e)
			{
				handleRequestServiceException(e);
			}
		}
		
		super.requestStart(request,response);
	}
	
	
	public void handleRequestStart(final VaadinSession session)
	{
		try
		{
			final Conversationables conversationables = session
					.getAttribute(Conversationables.class);
			if(conversationables != null)
			{
				for(final String persistenceUnit : Application.getPersistenceManager()
						.getPersistenceUnits())
				{
					Application.getSessionStrategyProvider()
							.getRequestStartSessionStrategy(conversationables,persistenceUnit)
							.requestStart(conversationables,persistenceUnit);
				}
			}
		}
		catch(final Exception e)
		{
			handleRequestServiceException(e);
		}
	}
	
	
	@Override
	public void requestEnd(final VaadinRequest request, final VaadinResponse response,
			final VaadinSession session)
	{
		if(!isHeartbeatRequest(request))
		{
			handleRequestEnd(session);
		}
		
		super.requestEnd(request,response,session);
	}
	
	
	public void handleRequestEnd(final VaadinSession session)
	{
		final SessionStrategyProvider sessionStrategyProvider = Application
				.getSessionStrategyProvider();
		if(session != null && sessionStrategyProvider != null)
		{
			try
			{
				final Conversationables conversationables = session
						.getAttribute(Conversationables.class);
				if(conversationables != null)
				{
					for(final String persistenceUnit : Application.getPersistenceManager()
							.getPersistenceUnits())
					{
						sessionStrategyProvider
								.getRequestEndSessionStrategy(conversationables,persistenceUnit)
								.requestEnd(conversationables,persistenceUnit);
					}
				}
			}
			catch(final Exception e)
			{
				handleRequestServiceException(e);
			}
		}
	}
	
	
	protected void handleRequestServiceException(final Exception exception)
	{
		Logger.getLogger(XdevServletService.class.getName()).log(Level.WARNING,
				exception.getMessage(),exception);
	}
	
	
	@Override
	protected VaadinSession createVaadinSession(final VaadinRequest request) throws ServiceException
	{
		final VaadinSession session = super.createVaadinSession(request);
		session.setAttribute(Conversationables.class,new Conversationables());
		session.setAttribute(ClientInfo.class,ClientInfo.get(request));
		return session;
	}
}
