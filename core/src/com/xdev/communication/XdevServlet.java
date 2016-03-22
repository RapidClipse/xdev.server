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


import javax.servlet.ServletException;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;


/**
 * Extended {@link VaadinServlet} with full control over each type of
 * communication, including websockets.
 *
 * @author XDEV Software
 */
public class XdevServlet extends VaadinServlet
{
	public static XdevServlet getServlet()
	{
		return (XdevServlet)VaadinServlet.getCurrent();
	}
	
	
	@Override
	protected VaadinServletService createServletService(
			final DeploymentConfiguration deploymentConfiguration) throws ServiceException
	{
		final XdevServletService servletService = new XdevServletService(this,
				deploymentConfiguration);
		servletService.init();
		return servletService;
	}
	
	
	@Override
	public XdevServletService getService()
	{
		return (XdevServletService)super.getService();
	}
	
	
	@Override
	protected void servletInitialized() throws ServletException
	{
		super.servletInitialized();
		
		getService().addSessionInitListener(event -> initSession(event));
	}
	
	
	protected void initSession(final SessionInitEvent event)
	{
		event.getSession().setAttribute(URLParameterRegistry.class,new URLParameterRegistry());
		
		initSession(event,ClientInfo.get(event.getRequest()));
	}
	
	
	protected void initSession(final SessionInitEvent event, final ClientInfo clientInfo)
	{
		if(clientInfo.isMobile() || clientInfo.isTablet())
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
