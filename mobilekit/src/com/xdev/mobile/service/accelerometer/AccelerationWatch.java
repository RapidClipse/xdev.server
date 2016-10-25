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


/**
 *
 * @author XDEV Software
 *
 */
public class AccelerationWatch
{
	private final Acceleration	acceleration;
	private final String		watchID;
	
	
	AccelerationWatch(final Acceleration acceleration, final String watchID)
	{
		this.acceleration = acceleration;
		this.watchID = watchID;
	}
	
	
	/**
	 * @return the acceleration
	 */
	public Acceleration getAcceleration()
	{
		return this.acceleration;
	}
	
	
	/**
	 * @return the watchID
	 */
	public String getWatchID()
	{
		return this.watchID;
	}
	
	
	/**
	 * Convenience method to clear this watch position.
	 * <p>
	 * Synonym for:
	 * <p>
	 * {@code
	 * AccelerometerService.getInstance().clearWatchPosition(accelerationWatch.getWatchID());
	 * }
	 */
	public void clear()
	{
		AccelerometerService.getInstance().clearWatchPosition(this.watchID);
	}
	
	
	@Override
	public String toString()
	{
		return "AccelerationWatch [acceleration=" + this.acceleration + ", watchID=" + this.watchID
				+ "]";
	}
}
