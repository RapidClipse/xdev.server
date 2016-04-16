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

package com.xdev.mobile.service.event;


import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.Page;
import com.xdev.mobile.service.MobileService;


/**
 * This service provides a way to vibrate the device.
 *
 * @author XDEV Software
 *
 */
@JavaScript("event.js")
public class EventService extends MobileService
{
	
	public static EventService getInstance()
	{
		return getServiceHelper(EventService.class);
	}


	public EventService(final AbstractClientConnector target)
	{
		super(target);
	}
	
	
	public void closeApp()
	{
		final StringBuilder js = new StringBuilder();
		js.append("event_closeApp()");

		Page.getCurrent().getJavaScript().execute(js.toString());
	}


	public void addBackButtonListener()
	{
		final StringBuilder js = new StringBuilder();
		js.append("event_onBackKeyDown()");

		Page.getCurrent().getJavaScript().execute(js.toString());
	}
}
