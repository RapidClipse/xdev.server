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
 * Parameters to customize the retrieval of the acceleration.
 *
 * @author XDEV Software
 *
 */
public class AccelerometerOptions
{
	public static AccelerometerOptions withFrequency(final int milliseconds)
	{
		return new AccelerometerOptions().frequency(milliseconds);
	}

	private int frequency = 1000;
	
	
	public AccelerometerOptions()
	{
	}
	
	
	/**
	 * The frequency of updates in milliseconds.
	 */
	public int getFrequency()
	{
		return this.frequency;
	}


	/**
	 * The frequency of updates in milliseconds.
	 */
	public AccelerometerOptions frequency(final int frequency)
	{
		this.frequency = frequency;
		return this;
	}
}
