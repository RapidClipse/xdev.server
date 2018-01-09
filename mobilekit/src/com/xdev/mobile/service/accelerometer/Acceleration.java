/*
 * Copyright (C) 2013-2018 by XDEV Software, All Rights Reserved.
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


/**
 * Contains Accelerometer data captured at a specific point in time.
 * Acceleration values include the effect of gravity (9.81 m/s^2), so that when
 * a device lies flat and facing up, x, y, and z values returned should be 0, 0,
 * and 9.81.
 *
 * @author XDEV Software
 *
 */
public class Acceleration
{
	private final double	x;
	private final double	y;
	private final double	z;
	private final long		timestamp;


	Acceleration(final double x, final double y, final double z, final long timestamp)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.timestamp = timestamp;
	}


	/**
	 * Amount of acceleration on the x-axis. (in m/s^2)
	 */
	public double getX()
	{
		return this.x;
	}


	/**
	 * Amount of acceleration on the y-axis. (in m/s^2)
	 */
	public double getY()
	{
		return this.y;
	}


	/**
	 * Amount of acceleration on the z-axis. (in m/s^2)
	 */
	public double getZ()
	{
		return this.z;
	}


	/**
	 * Creation timestamp in milliseconds.
	 */
	public long getTimestamp()
	{
		return this.timestamp;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Acceleration [x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", timestamp="
				+ this.timestamp + "]";
	}

}
