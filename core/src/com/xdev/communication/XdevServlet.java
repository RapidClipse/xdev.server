/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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


import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.nodes.Element;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.xdev.Application;
import com.xdev.util.ExtensionUtils;


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

	private final ContentSecurityPolicy contentSecurityPolicy = new ContentSecurityPolicy();


	/**
	 * @since 1.3
	 */
	public ContentSecurityPolicy getContentSecurityPolicy()
	{
		return this.contentSecurityPolicy;
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

		Application.init(getServletContext());

		try
		{
			final List<XdevServletExtension> extensions = ExtensionUtils.readExtensions("servlet",
					XdevServletExtension.class);

			for(final XdevServletExtension extension : extensions)
			{
				extension.servletInitialized(this);
			}
		}
		catch(final Exception e)
		{
			throw new ServletException(e);
		}

		getService().addSessionInitListener(event -> initSession(event));
	}


	protected void initSession(final SessionInitEvent event)
	{
		final VaadinSession session = event.getSession();
		session.setAttribute(URLParameterRegistry.class,new URLParameterRegistry());
		session.setAttribute(Cookies.class,new Cookies(session));
		
		initSession(event,ClientInfo.get(event.getRequest()));
	}


	protected void initSession(final SessionInitEvent event, final ClientInfo clientInfo)
	{
		event.getSession().addBootstrapListener(new BootstrapListener()
		{
			@Override
			public void modifyBootstrapPage(final BootstrapPageResponse response)
			{
				final ContentSecurityPolicy csp = XdevServlet.this.contentSecurityPolicy;
				if(!csp.isEmpty())
				{
					final Element head = response.getDocument().head();
					head.getElementsByAttributeValue("http-equiv","Content-Security-Policy")
							.forEach(Element::remove);
					head.prependElement("meta").attr("http-equiv","Content-Security-Policy")
							.attr("content",csp.toString());
				}
			}


			@Override
			public void modifyBootstrapFragment(final BootstrapFragmentResponse response)
			{
			}
		});

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


	@Override
	protected void service(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException
	{
		final String origin = request.getHeader("Origin");
		if(origin != null)
		{
			// Handle a preflight "option" requests
			final String requestMethod = request.getMethod();
			if("options".equalsIgnoreCase(requestMethod))
			{
				response.addHeader("Access-Control-Allow-Origin",origin);
				response.setHeader("Allow","GET, HEAD, POST, PUT, DELETE,TRACE,OPTIONS");

				// allow the requested method
				final String method = request.getHeader("Access-Control-Request-Method");
				response.addHeader("Access-Control-Allow-Methods",method);

				// allow the requested headers
				final String headers = request.getHeader("Access-Control-Request-Headers");
				response.addHeader("Access-Control-Allow-Headers",headers);

				response.addHeader("Access-Control-Allow-Credentials","true");
				response.setContentType("text/plain");
				response.setCharacterEncoding("utf-8");
				response.getWriter().flush();
				return;
			} // Handle UIDL post requests
			else if("post".equalsIgnoreCase(requestMethod))
			{
				response.addHeader("Access-Control-Allow-Origin",origin);
				response.addHeader("Access-Control-Allow-Credentials","true");
				super.service(request,response);
				return;
			}
		}

		super.service(request,response);
	}
}
