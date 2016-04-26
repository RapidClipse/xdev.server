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


import java.util.ArrayList;
import java.util.List;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Page;
import com.xdev.mobile.service.MobileService;
import com.xdev.mobile.service.MobileServiceDescriptor;

import elemental.json.JsonArray;


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

	private final List<BackButtonHandler> backButtonHandlers = new ArrayList<>();


	public AppService(final AbstractClientConnector target)
	{
		super(target);

		addFunction("app_onBackButton",this::app_onBackButton);
	}


	public void closeApp()
	{
		Page.getCurrent().getJavaScript().execute("app_closeApp()");
	}


	public void addBackButtonHandler(final BackButtonHandler handler)
	{
		if(this.backButtonHandlers.isEmpty())
		{
			Page.getCurrent().getJavaScript().execute("app_addBackButtonHandler()");
		}

		this.backButtonHandlers.add(handler);
	}


	public void removeBackButtonHandler(final BackButtonHandler handler)
	{
		this.backButtonHandlers.remove(handler);

		if(this.backButtonHandlers.isEmpty())
		{
			Page.getCurrent().getJavaScript().execute("app_removeBackButtonHandler()");
		}
	}


	public void addBackButtonListener()
	{
		final StringBuilder js = new StringBuilder();
		js.append("app_onBackKeyDown()");

		Page.getCurrent().getJavaScript().execute(js.toString());
	}


	private void app_onBackButton(final JsonArray arguments)
	{
		for(int i = this.backButtonHandlers.size(); --i >= 0;)
		{
			if(this.backButtonHandlers.get(i).handleBackButton())
			{
				break;
			}
		}
	}
}
