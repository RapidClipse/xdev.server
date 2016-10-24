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


import java.util.EventObject;


/**
 * An event fired by the {@link AppService}.
 *
 * @author XDEV Software
 *
 */
public class AppEvent extends EventObject
{
	private boolean consumed = false;
	
	
	public AppEvent(final AppService source)
	{
		super(source);
	}


	@Override
	public AppService getSource()
	{
		return (AppService)super.getSource();
	}


	/**
	 * Consumes this event so that it will not be processed in the default
	 * manner by the source which originated it.
	 */
	public void consume()
	{
		this.consumed = true;
	}


	/**
	 * Returns whether or not this event has been consumed.
	 *
	 * @see #consume
	 */
	public boolean isConsumed()
	{
		return this.consumed;
	}
}
