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

package com.xdev.mobile.service.geolocation;


import java.util.function.Consumer;


/**
 * @author XDEV Software
 *
 */
public interface GeolocationServiceAccess
{
	
	/**
	 * Asynchronously acquires the current position.
	 *
	 * @param options
	 *            optional options
	 * @param successCallback
	 *            The function to call when the position data is available
	 * @param errorCallback
	 *            The function to call when there is an error getting the
	 *            heading position.
	 */
	void getCurrentPosition(GeolocationOptions options, Consumer<Position> successCallback,
			Consumer<GeolocationServiceError> errorCallback);
	
	
	/**
	 *
	 * Asynchronously watches the geolocation for changes to geolocation. When a
	 * change occurs, the successCallback is called with the new location.
	 *
	 * @param options
	 *            optional options
	 * @param successCallback
	 *            The function to call each time the location data is available
	 * @param errorCallback
	 *            The function to call when there is an error getting the
	 *            location data.
	 */
	
	void watchPosition(GeolocationOptions options, Consumer<Geolocation> successCallback,
			Consumer<GeolocationServiceError> errorCallback);
	
	
	/**
	 * Clears the specified heading watch.
	 *
	 * @param watchID
	 */
	void clearWatchPosition(double watchID);
	
}