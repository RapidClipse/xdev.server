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
 * A Coordinates object is attached to a {@link Position} object that is
 * available to callback functions in requests for the current position. It
 * contains a set of properties that describe the geographic coordinates of a
 * position.
 *
 * @author XDEV Software
 *
 */
public class Coordinates
{
	private final double	latitude;
	private final double	longitude;
	private final double	altitude;
	private final double	accuracy;
	private final double	altitudeAccuracy;
	private final double	heading;
	private final double	speed;
	
	
	Coordinates(final double latitude, final double longitude, final double altitude,
			final double accuracy, final double altitudeAccuracy, final double heading,
			final double speed)
	{
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.accuracy = accuracy;
		this.altitudeAccuracy = altitudeAccuracy;
		this.heading = heading;
		this.speed = speed;
	}
	
	
	/**
	 * Latitude in decimal degrees.
	 */
	public double getLatitude()
	{
		return this.latitude;
	}
	
	
	/**
	 * Longitude in decimal degrees.
	 */
	public double getLongitude()
	{
		return this.longitude;
	}
	
	
	/**
	 * Height of the position in meters above the ellipsoid.
	 */
	public double getAltitude()
	{
		return this.altitude;
	}
	
	
	/**
	 * Accuracy level of the latitude and longitude coordinates in meters.
	 */
	public double getAccuracy()
	{
		return this.accuracy;
	}
	
	
	/**
	 * Accuracy level of the altitude coordinate in meters.
	 */
	public double getAltitudeAccuracy()
	{
		return this.altitudeAccuracy;
	}
	
	
	/**
	 * Direction of travel, specified in degrees counting clockwise relative to
	 * the true north.
	 */
	public double getHeading()
	{
		return this.heading;
	}
	
	
	/**
	 * Current ground speed of the device, specified in meters per second.
	 */
	public double getSpeed()
	{
		return this.speed;
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Coordinates [latitude=" + this.latitude + ", longitude=" + this.longitude
				+ ", altitude=" + this.altitude + ", accuracy=" + this.accuracy
				+ ", altitudeAccuracy=" + this.altitudeAccuracy + ", heading=" + this.heading
				+ ", speed=" + this.speed + "]";
	}
}
