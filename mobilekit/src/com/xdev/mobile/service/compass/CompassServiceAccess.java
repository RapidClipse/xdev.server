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

package com.xdev.mobile.service.compass;


import java.util.function.Consumer;


/**
 * @author XDEV Software
 *
 */
public interface CompassServiceAccess
{
	/**
	 * Asynchronously acquires the current heading.
	 *
	 * @param successCallback
	 *            The function to call when the heading data is available
	 */
	default public void getCurrentHeading(final Consumer<Heading> successCallback)
	{
		getCurrentHeading(successCallback,null);
	}


	/**
	 * Asynchronously acquires the current heading.
	 *
	 * @param successCallback
	 *            The function to call when the heading data is available
	 * @param errorCallback
	 *            The function to call when there is an error getting the
	 *            heading position.
	 */
	public void getCurrentHeading(Consumer<Heading> successCallback,
			Consumer<CompassServiceError> errorCallback);
	
	
	/**
	 *
	 * Asynchronously watches the compass for changes to heading. When a change
	 * occurs, the successCallback is called with the new location.
	 *
	 * @param options
	 *            optional options
	 * @param successCallback
	 *            The function to call each time the heading data is available
	 * @param errorCallback
	 *            The function to call when there is an error getting the
	 *            heading data.
	 */
	default public void watchHeading(final CompassOptions options,
			final Consumer<HeadingWatch> successCallback)
	{
		watchHeading(options,successCallback,null);
	}


	/**
	 *
	 * Asynchronously watches the compass for changes to heading. When a change
	 * occurs, the successCallback is called with the new location.
	 *
	 * @param options
	 *            optional options
	 * @param successCallback
	 *            The function to call each time the heading data is available
	 * @param errorCallback
	 *            The function to call when there is an error getting the
	 *            heading data.
	 */
	public void watchHeading(CompassOptions options, Consumer<HeadingWatch> successCallback,
			Consumer<CompassServiceError> errorCallback);


	/**
	 * Clears the specified heading watch.
	 *
	 * @param watchID
	 */
	public void clearWatch(String watchID);
}
