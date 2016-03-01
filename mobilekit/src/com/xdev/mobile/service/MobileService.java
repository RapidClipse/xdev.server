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

package com.xdev.mobile.service;


import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.UI;
import com.xdev.mobile.ui.MobileUI;


/**
 * @author XDEV Software
 * 		
 */
public abstract class MobileService extends AbstractJavaScriptExtension
{
	protected static <T extends MobileService> T getServiceHelper(final Class<T> clazz)
	{
		final UI ui = UI.getCurrent();
		if(ui instanceof MobileUI)
		{
			return ((MobileUI)ui).getMobileService(clazz);
		}
		return null;
	}
	
	
	protected MobileService(final AbstractClientConnector target)
	{
		super(target);
	}


	protected String generateCallerID()
	{
		return Long.toString(System.nanoTime(),Character.MAX_RADIX);
	}
}
