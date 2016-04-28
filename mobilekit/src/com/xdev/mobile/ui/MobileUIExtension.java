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
import com.vaadin.server.VaadinSession;
import com.xdev.mobile.communication.MobileConfiguration;
import com.xdev.mobile.service.MobileService;
import com.xdev.ui.XdevUI;
import com.xdev.ui.XdevUIExtension;


/**
 * @author XDEV Software
 *
 */
public class MobileUIExtension implements XdevUIExtension
{
	private static Logger LOG = Logger.getLogger(MobileUIExtension.class.getName());


	@Override
	public void uiInitialized(final XdevUI ui)
	{
		final MobileConfiguration mobileConfiguration = VaadinSession.getCurrent()
				.getAttribute(MobileConfiguration.class);
		if(mobileConfiguration != null)
		{
			for(final Class<? extends MobileService> clazz : mobileConfiguration
					.getMobileServices())
			{
				try
				{
					final Constructor<? extends MobileService> constructor = clazz
							.getConstructor(AbstractClientConnector.class);
					constructor.newInstance(ui);
					LOG.log(Level.INFO,"Mobile service registered: " + clazz.getName());
				}
				catch(final Exception e)
				{
					LOG.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		}
	}
}
