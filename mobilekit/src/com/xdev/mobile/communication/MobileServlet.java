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

package com.xdev.mobile.communication;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
 * Servlet optimized for mobile clients. Also provides cordova and bridge
 * scripts for hybrid apps.
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
	
	
	@SuppressWarnings("unchecked")
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
				final SAXReader reader = new SAXReader();
				final Document document = reader.read(url);
				final Element rootElement = document.getRootElement();
				if(rootElement != null)
				{
					final Element servicesElement = rootElement.element("services");
					if(servicesElement != null)
					{
						final ClassLoader classLoader = getClass().getClassLoader();
						
						final List<Class<? extends MobileService>> services = new ArrayList<>();
						for(final Object serviceObject : servicesElement.elements("service"))
						{
							if(serviceObject instanceof Element)
							{
								final Element serviceElement = (Element)serviceObject;
								final String className = serviceElement.getTextTrim();
								try
								{
									final Class<?> serviceClass = classLoader.loadClass(className);
									if(MobileService.class.isAssignableFrom(serviceClass))
									{
										services.add((Class<? extends MobileService>)serviceClass);
									}
									else
									{
										throw new IllegalArgumentException(
												className + " is not a MobileService");
									}
								}
								catch(final Exception e)
								{
									LOG.log(Level.SEVERE,e.getMessage(),e);
								}
							}
						}
						config.setMobileServices(services.toArray(new Class[services.size()]));
					}
				}
			}
		}
		catch(final Exception e)
		{
			LOG.log(Level.SEVERE,e.getMessage(),e);
		}
		
		return config;
	}
	
	
	private URL findMobileXML() throws MalformedURLException
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
