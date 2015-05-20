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

package com.xdev.mobilekit.communication;


import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.VaadinServletService;
import com.xdev.communication.XdevServletService;


/**
 * Extended {@link TouchKitServlet} with full control over each type of
 * communication, including websockets.
 *
 * @author XDEV Software
 *
 */
public class MobileKitServlet extends TouchKitServlet
{
	@Override
	protected VaadinServletService createServletService(
			final DeploymentConfiguration deploymentConfiguration) throws ServiceException
	{
		final XdevServletService servletService = new XdevServletService(this,
				deploymentConfiguration);
		servletService.init();
		return servletService;
	}
}
