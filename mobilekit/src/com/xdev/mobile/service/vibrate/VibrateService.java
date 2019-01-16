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

package com.xdev.mobile.service.vibrate;


import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Page;
import com.xdev.mobile.config.MobileServiceConfiguration;
import com.xdev.mobile.service.AbstractMobileService;
import com.xdev.mobile.service.annotations.MobileService;
import com.xdev.mobile.service.annotations.Plugin;


/**
 * This service provides a way to vibrate the device.
 *
 * @author XDEV Software
 *
 */
@MobileService(plugins = @Plugin(name = "cordova-plugin-vibration", spec = "2.1.1"))
@JavaScript("vibrate.js")
public class VibrateService extends AbstractMobileService implements VibrateServiceAccess
{
	/**
	 * Returns the vibrate service.<br>
	 * To activate the service it has to be registered in the mobile.xml.
	 *
	 * <pre>
	 * {@code
	 * ...
	 * <service>com.xdev.mobile.service.vibrate.VibrateService</service>
	 * ...
	 * }
	 * </pre>
	 *
	 * @return the vibrate service if available
	 */
	public static VibrateServiceAccess getInstance()
	{
		return getMobileService(VibrateService.class);
	}


	public VibrateService(final AbstractClientConnector target,
			final MobileServiceConfiguration configuration)
	{
		super(target,configuration);
	}


	@Override
	public void vibrate(final int... pattern)
	{
		final StringBuilder js = new StringBuilder();
		js.append("vibrate_vibrate(");
		if(pattern.length == 1)
		{
			js.append(pattern[0]);
		}
		else
		{
			js.append('[');
			for(int i = 0; i < pattern.length; i++)
			{
				if(i > 0)
				{
					js.append(',');
				}
				js.append(pattern[i]);
			}
			js.append(']');
		}
		js.append(");");

		Page.getCurrent().getJavaScript().execute(js.toString());
	}
}
