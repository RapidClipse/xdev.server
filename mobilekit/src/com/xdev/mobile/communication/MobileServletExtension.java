/*
 * Copyright (C) 2013-2019 by XDEV Software, All Rights Reserved.
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.ServletException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.vaadin.server.CustomizedSystemMessages;
import com.xdev.communication.XdevServlet;
import com.xdev.communication.XdevServletExtension;
import com.xdev.mobile.config.MobileConfiguration;
import com.xdev.mobile.config.MobileServiceConfiguration;
import com.xdev.mobile.service.AbstractMobileService;


/**
 * @author XDEV Software
 *
 */
public class MobileServletExtension implements XdevServletExtension
{
	private static Logger LOG = Logger.getLogger(MobileServletExtension.class.getName());


	@Override
	public void servletInitialized(final XdevServlet servlet) throws ServletException
	{
		servlet.getService().setSystemMessagesProvider(systemMessagesInfo -> {
			final CustomizedSystemMessages messages = new CustomizedSystemMessages();
			messages.setSessionExpiredNotificationEnabled(false);
			messages.setCommunicationErrorNotificationEnabled(false);
			return messages;
		});

		final MobileConfiguration mobileConfiguration = readMobileConfiguration(servlet);
		servlet.getService().addSessionInitListener(event -> event.getSession()
				.setAttribute(MobileConfiguration.class,mobileConfiguration));
	}


	protected MobileConfiguration readMobileConfiguration(final XdevServlet servlet)
	{
		final MobileConfiguration.Default config = new MobileConfiguration.Default();

		try
		{
			final URL url = findMobileXML(servlet);
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
									.map(serviceElement -> createServiceConfiguration(classLoader,
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
	protected MobileServiceConfiguration createServiceConfiguration(final ClassLoader classLoader,
			final Element serviceElement)
	{
		final String className = serviceElement.attributeValue("class");

		try
		{
			final Class<?> serviceClass = classLoader.loadClass(className);
			if(!AbstractMobileService.class.isAssignableFrom(serviceClass))
			{
				throw new IllegalArgumentException(
						className + " is not a " + AbstractMobileService.class.getSimpleName());
			}

			final Map<String, String> params = ((List<?>)serviceElement.elements("param")).stream()
					.filter(Element.class::isInstance).map(Element.class::cast).collect(Collectors
							.toMap(e -> e.attributeValue("name"),e -> e.attributeValue("value")));

			final MobileServiceConfiguration.Default configuration = new MobileServiceConfiguration.Default();
			configuration.setServiceClass((Class<? extends AbstractMobileService>)serviceClass);
			configuration.setParameters(params);
			return configuration;
		}
		catch(final Exception e)
		{
			LOG.log(Level.SEVERE,e.getMessage(),e);
		}

		return null;
	}


	protected URL findMobileXML(final XdevServlet servlet) throws MalformedURLException
	{
		final ClassLoader classLoader = servlet.getService().getClassLoader();
		URL resourceUrl = classLoader.getResource("META-INF/mobile.xml");
		if(resourceUrl == null)
		{
			resourceUrl = servlet.getServletContext().getResource("/mobile.xml");
			if(resourceUrl == null)
			{
				resourceUrl = classLoader.getResource("mobile.xml");
			}
		}
		return resourceUrl;
	}
}
