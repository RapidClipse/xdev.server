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

package com.xdev.mobile.service.app;


import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Page;
import com.xdev.mobile.service.MobileService;
import com.xdev.mobile.service.MobileServiceDescriptor;


/**
 *
 *
 * @author XDEV Software
 *
 */
@MobileServiceDescriptor("app-descriptor.xml")
@JavaScript("app.js")
public class AppService extends MobileService
{
	
	public static AppService getInstance()
	{
		return getServiceHelper(AppService.class);
	}
	
	
	public AppService(final AbstractClientConnector target)
	{
		super(target);
	}


	public void closeApp()
	{
		final StringBuilder js = new StringBuilder();
		js.append("app_closeApp()");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
	
	
	public void addBackButtonListener()
	{
		final StringBuilder js = new StringBuilder();
		js.append("app_onBackKeyDown()");
		
		Page.getCurrent().getJavaScript().execute(js.toString());
	}
}
