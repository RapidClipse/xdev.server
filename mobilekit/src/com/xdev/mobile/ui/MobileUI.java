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

package com.xdev.mobile.ui;


import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Extension;
import com.vaadin.server.VaadinServlet;
import com.xdev.mobile.communication.MobileConfiguration;
import com.xdev.mobile.communication.MobileServlet;
import com.xdev.mobile.service.MobileService;
import com.xdev.ui.XdevUI;


/**
 * The topmost component in any component hierarchy ... see {@link XdevUI}.
 * <p>
 * Also contains all registered mobile services.
 *
 * @author XDEV Software
 *
 * @see #getMobileService(Class)
 */
public abstract class MobileUI extends XdevUI
{
	private static Logger LOG = Logger.getLogger(MobileServlet.class.getName());


	public MobileUI()
	{
		registerMobileServices();
	}


	private void registerMobileServices()
	{
		final VaadinServlet servlet = VaadinServlet.getCurrent();
		if(servlet instanceof MobileServlet)
		{
			for(final Class<? extends MobileService> clazz : ((MobileServlet)servlet)
					.getMobileConfiguration().getMobileServices())
			{
				try
				{
					final Constructor<? extends MobileService> constructor = clazz
							.getConstructor(AbstractClientConnector.class);
					constructor.newInstance(this);
					LOG.log(Level.INFO,"Mobile service registered: " + clazz.getName());
				}
				catch(final Exception e)
				{
					LOG.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		}
	}


	/**
	 * Returns a registered service.
	 *
	 * Services are registered in the mobile.xml configuration file. Example:
	 *
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <mobile-app>
	 * 	<services>
	 * 		<service>com.xdev.mobile.service.contacts.ContactsService</service>
	 * 	</services>
	 * </mobile-app>
	 * }
	 * </pre>
	 *
	 * @param type
	 * @return
	 * @see MobileServlet#getMobileConfiguration()
	 * @see MobileConfiguration#getMobileServices()
	 */
	public <T extends MobileService> T getMobileService(final Class<T> type)
	{
		for(final Extension extension : getExtensions())
		{
			if(extension.getClass() == type)
			{
				return type.cast(extension);
			}
		}
		return null;
	}
}
