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


/**
 * @author XDEV Software
 *		
 */
public class Coordinates
{
	private double	latitude;
	private double	longitude;
	private double	altitude;
	private double	accuracy;
	private double	altitudeAccuracy;
	private double	heading;
	private double	speed;


	public Coordinates()
	{
	
	}


	/**
	 * @param latitude
	 * @param longitude
	 * @param altitude
	 * @param accuracy
	 * @param altitudeAccuracy
	 * @param heading
	 * @param speed
	 */
	public Coordinates(final double latitude, final double longitude, final double altitude,
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
	 * @return the latitude
	 */
	public double getLatitude()
	{
		return this.latitude;
	}
	
	
	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(final double latitude)
	{
		this.latitude = latitude;
	}
	
	
	/**
	 * @return the longitude
	 */
	public double getLongitude()
	{
		return this.longitude;
	}
	
	
	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(final double longitude)
	{
		this.longitude = longitude;
	}
	
	
	/**
	 * @return the altitude
	 */
	public double getAltitude()
	{
		return this.altitude;
	}
	
	
	/**
	 * @param altitude
	 *            the altitude to set
	 */
	public void setAltitude(final double altitude)
	{
		this.altitude = altitude;
	}
	
	
	/**
	 * @return the accuracy
	 */
	public double getAccuracy()
	{
		return this.accuracy;
	}
	
	
	/**
	 * @param accuracy
	 *            the accuracy to set
	 */
	public void setAccuracy(final double accuracy)
	{
		this.accuracy = accuracy;
	}
	
	
	/**
	 * @return the altitudeAccuracy
	 */
	public double getAltitudeAccuracy()
	{
		return this.altitudeAccuracy;
	}
	
	
	/**
	 * @param altitudeAccuracy
	 *            the altitudeAccuracy to set
	 */
	public void setAltitudeAccuracy(final double altitudeAccuracy)
	{
		this.altitudeAccuracy = altitudeAccuracy;
	}
	
	
	/**
	 * @return the heading
	 */
	public double getHeading()
	{
		return this.heading;
	}
	
	
	/**
	 * @param heading
	 *            the heading to set
	 */
	public void setHeading(final double heading)
	{
		this.heading = heading;
	}
	
	
	/**
	 * @return the speed
	 */
	public double getSpeed()
	{
		return this.speed;
	}
	
	
	/**
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(final double speed)
	{
		this.speed = speed;
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
