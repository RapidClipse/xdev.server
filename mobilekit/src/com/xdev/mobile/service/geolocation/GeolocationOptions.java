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

package com.xdev.mobile.service.geolocation;


/**
 * Optional parameters to customize the retrieval of the geolocation.
 *
 * @author XDEV Software
 *
 */
public class GeolocationOptions
{
	public static GeolocationOptions withHighAccuracy()
	{
		return new GeolocationOptions().enableHighAccuracy(true);
	}


	public static GeolocationOptions withLowAccuracy()
	{
		return new GeolocationOptions().enableHighAccuracy(false);
	}
	
	private Boolean	enableHighAccuracy;
	private Long	timeout;
	private Long	maximumAge;


	public GeolocationOptions()
	{
	}


	/**
	 * @return the enable high accuracy setting
	 * @see #enableHighAccuracy(boolean)
	 */
	public Boolean getEnableHighAccuracy()
	{
		return this.enableHighAccuracy;
	}


	/**
	 * Provides a hint that the application needs the best possible results. By
	 * default, the device attempts to retrieve a {@link Position} using
	 * network-based methods. Setting this property to <code>true</code> tells
	 * the framework to use more accurate methods, such as satellite
	 * positioning.
	 */
	public GeolocationOptions enableHighAccuracy(final boolean enableHighAccuracy)
	{
		this.enableHighAccuracy = enableHighAccuracy;
		return this;
	}


	/**
	 * @return the timeout setting
	 * @see #timeout(long)
	 */
	public Long getTimeout()
	{
		return this.timeout;
	}


	/**
	 * The maximum length of time (milliseconds) that is allowed to pass from
	 * the call to
	 * {@link GeolocationService#getCurrentPosition(java.util.function.Consumer, java.util.function.Consumer)}
	 * or
	 * {@link GeolocationService#watchPosition(java.util.function.Consumer, java.util.function.Consumer, int)}
	 * until the corresponding success callback executes. If the success
	 * callback is not invoked within this time, the geolocationError callback
	 * is passed a {@link GeolocationServiceError.Reason#TIMEOUT} error code.
	 * (Note that when used in conjunction with
	 * {@link GeolocationService#watchPosition(java.util.function.Consumer, java.util.function.Consumer, int)},
	 * the error callback could be called on an interval every timeout
	 * milliseconds!)
	 */
	public GeolocationOptions timeout(final long timeout)
	{
		this.timeout = timeout;
		return this;
	}


	/**
	 * @return the maximum age setting
	 * @see #maximumAge(long)
	 */
	public Long getMaximumAge()
	{
		return this.maximumAge;
	}


	/**
	 * Accept a cached position whose age is no greater than the specified time
	 * in milliseconds.
	 */
	public GeolocationOptions maximumAge(final long maximumAge)
	{
		this.maximumAge = maximumAge;
		return this;
	}
}
