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

package com.xdev.mobile.service.accelerometer;


import java.util.function.Consumer;

import com.xdev.mobile.service.MobileServiceError;


/**
 * @author XDEV Software
 *
 */
public interface AccelerometerServiceAccess
{
	/**
	 * Get the current acceleration along the x, y, and z axes.
	 *
	 * These acceleration values are returned to the success callback function.
	 *
	 * @param successCallback
	 *            The function to call when the position data is available
	 */
	default public void getCurrentAcceleration(final Consumer<Acceleration> successCallback)
	{
		getCurrentAcceleration(successCallback,null);
	}
	
	
	/**
	 * Get the current acceleration along the x, y, and z axes.
	 *
	 * These acceleration values are returned to the success callback function.
	 *
	 * @param successCallback
	 *            The function to call when the position data is available
	 * @param errorCallback
	 *            The function to call when there is an error getting the
	 *            heading position.
	 */
	public void getCurrentAcceleration(Consumer<Acceleration> successCallback,
			Consumer<MobileServiceError> errorCallback);
	
	
	/**
	 * Retrieves the device's current acceleration at a regular interval,
	 * executing the success callback each time. Specify the interval in
	 * milliseconds via the option's frequency parameter.
	 *
	 * @param options
	 * @param successCallback
	 *            The function to call each time the location data is available
	 * @param errorCallback
	 *            The function to call when there is an error getting the
	 *            location data.
	 */
	default public void watchAcceleration(final AccelerometerOptions options,
			final Consumer<AccelerationWatch> successCallback)
	{
		watchAcceleration(options,successCallback,null);
	}
	
	
	/**
	 * Retrieves the device's current acceleration at a regular interval,
	 * executing the success callback each time. Specify the interval in
	 * milliseconds via the option's frequency parameter.
	 *
	 * @param options
	 * @param successCallback
	 *            The function to call each time the acceleration data is
	 *            available
	 * @param errorCallback
	 *            The function to call when there is an error getting the
	 *            acceleration data.
	 */
	public void watchAcceleration(AccelerometerOptions options,
			Consumer<AccelerationWatch> successCallback,
			Consumer<MobileServiceError> errorCallback);
	
	
	/**
	 * Clears the specified heading watch.
	 *
	 * @param watchID
	 */
	public void clearWatchPosition(String watchID);
}