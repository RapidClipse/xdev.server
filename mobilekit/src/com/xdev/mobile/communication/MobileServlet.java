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

package com.xdev.mobile.communication;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.ServletException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.SessionInitEvent;
import com.xdev.communication.ClientInfo;
import com.xdev.communication.XdevServlet;
import com.xdev.mobile.service.MobileService;


/**
 * Servlet which adds functionality for mobile clients. Also provides cordova
 * and bridge scripts for hybrid apps.
 *
 * @author XDEV Software
 *
 */
public class MobileServlet extends XdevServlet
{
	private static Logger		LOG	= Logger.getLogger(MobileServlet.class.getName());

	private MobileConfiguration	mobileConfiguration;


	@Override
	protected void servletInitialized() throws ServletException
	{
		super.servletInitialized();

		this.mobileConfiguration = readMobileConfiguration();
	}


	/**
	 * @return the mobileConfiguration
	 */
	public MobileConfiguration getMobileConfiguration()
	{
		return this.mobileConfiguration;
	}


	protected MobileConfiguration readMobileConfiguration()
	{
		final MobileConfiguration.Default config = new MobileConfiguration.Default();

		try
		{
			final URL url = findMobileXML();
			if(url == null)
			{
				LOG.log(Level.WARNING,"No mobile.xml found");
			}
			else
			{
				final Document document = new SAXReader().read(url);
				Optional.ofNullable(document.getRootElement()).map(e -> e.element("services"))
						.ifPresent(servicesElement -> {
							final ClassLoader classLoader = getClass().getClassLoader();
							config.setMobileServices(((List<?>)servicesElement.elements("service"))
									.stream().filter(Element.class::isInstance)
									.map(Element.class::cast)
									.map(serviceElement -> createService(classLoader,
											serviceElement))
									.filter(Objects::nonNull).collect(Collectors.toList()));
						});
			}
		}
		catch(final Exception e)
		{
			LOG.log(Level.SEVERE,e.getMessage(),e);
		}

		return config;
	}


	@SuppressWarnings("unchecked")
	private Class<? extends MobileService> createService(final ClassLoader classLoader,
			final Element serviceElement)
	{
		final String className = serviceElement.getTextTrim();
		
		try
		{
			final Class<?> serviceClass = classLoader.loadClass(className);
			if(MobileService.class.isAssignableFrom(serviceClass))
			{
				return (Class<? extends MobileService>)serviceClass;
			}
			else
			{
				throw new IllegalArgumentException(
						className + " is not a " + MobileService.class.getSimpleName());
			}
		}
		catch(final Exception e)
		{
			LOG.log(Level.SEVERE,e.getMessage(),e);
		}

		return null;
	}


	protected URL findMobileXML() throws MalformedURLException
	{
		URL resourceUrl = getServletContext().getResource("/mobile.xml");
		if(resourceUrl == null)
		{
			final ClassLoader classLoader = getService().getClassLoader();
			resourceUrl = classLoader.getResource("META-INF/mobile.xml");
			if(resourceUrl == null)
			{
				resourceUrl = classLoader.getResource("mobile.xml");
			}
		}
		return resourceUrl;
	}


	@Override
	protected void initSession(final SessionInitEvent event)
	{
		super.initSession(event);

		event.getSession().addBootstrapListener(new BootstrapListener()
		{
			@Override
			public void modifyBootstrapPage(final BootstrapPageResponse response)
			{
				if("true".equals(response.getRequest().getParameter("cordova")))
				{
					response.getDocument().head().prependElement("meta")
							.attr("http-equiv","Content-Security-Policy")
							.attr("content",getContentSecurityPolicy());
					final ClientInfo clientInfo = ClientInfo.get(response.getRequest());
					if(clientInfo.isAndroid())
					{
						response.getDocument().body().prependElement("script")
								.attr("type","text/javascript")
								.attr("src","VAADIN/cordova/android/cordova.js");
					}
					else if(clientInfo.isIOS())
					{
						response.getDocument().body().prependElement("script")
								.attr("type","text/javascript")
								.attr("src","VAADIN/cordova/ios/cordova.js");
					}
					else
					{
						response.getDocument().body().prependElement("script")
								.attr("type","text/javascript")
								.attr("src","VAADIN/cordova/windows/cordova.js");
					}
				}
			}


			@Override
			public void modifyBootstrapFragment(final BootstrapFragmentResponse response)
			{
			}
		});
	}


	/**
	 * Provides the content security policy which will be written into the
	 * default html page.
	 * <p>
	 * Default is
	 *
	 * <pre>
	 * default-src *; style-src 'self' 'unsafe-inline'; script-src 'self' 'unsafe-inline' 'unsafe-eval'
	 * </pre>
	 *
	 * @see <a href=
	 *      "https://github.com/apache/cordova-plugin-whitelist#content-security-policy">
	 *      Whitelist Plugin</a>
	 */
	protected String getContentSecurityPolicy()
	{
		return "default-src *; style-src 'self' 'unsafe-inline'; script-src 'self' 'unsafe-inline' 'unsafe-eval'";
	}
}
