/*
 * Copyright (C) 2013-2017 by XDEV Software, All Rights Reserved.
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


/**
 * Contains heading data and timestamp created by the {@link CompassService}.
 *
 * @author XDEV Software
 *
 */
public class Heading
{
	private final double	magneticHeading;
	private final double	trueHeading;
	private final double	headingAccuracy;
	private final long		timestamp;


	Heading(final double magneticHeading, final double trueHeading, final double headingAccuracy,
			final long timestamp)
	{
		this.magneticHeading = magneticHeading;
		this.trueHeading = trueHeading;
		this.headingAccuracy = headingAccuracy;
		this.timestamp = timestamp;
	}


	/**
	 * The heading in degrees from 0-359.99 at a single moment in time.
	 */
	public double getMagneticHeading()
	{
		return this.magneticHeading;
	}


	/**
	 * The heading relative to the geographic North Pole in degrees 0-359.99 at
	 * a single moment in time. A negative value indicates that the true heading
	 * can't be determined.
	 */
	public double getTrueHeading()
	{
		return this.trueHeading;
	}


	/**
	 * The deviation in degrees between the reported heading and the true
	 * heading.
	 */
	public double getHeadingAccuracy()
	{
		return this.headingAccuracy;
	}


	/**
	 * The time at which this heading was determined.
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
		return "Heading [magneticHeading=" + this.magneticHeading + ", trueHeading="
				+ this.trueHeading + ", headingAccuracy=" + this.headingAccuracy + ", timestamp="
				+ this.timestamp + "]";
	}

}
