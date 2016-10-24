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
 * Contains position coordinates and timestamp created by the
 * {@link GeolocationService}.
 *
 * @author XDEV Software
 *
 */
public class Position
{
	private Coordinates	coords;
	private long		timestamp;


	Position()
	{
		
	}


	/**
	 * @param coordinates
	 * @param timestamp
	 */
	public Position(final Coordinates coordinates, final long timestamp)
	{
		super();
		this.coords = coordinates;
		this.timestamp = timestamp;
	}


	/**
	 * A set of geographic coordinates.
	 */
	public Coordinates getCoordinates()
	{
		return this.coords;
	}


	/**
	 * A set of geographic coordinates.
	 */
	public void setCoordinates(final Coordinates coordinates)
	{
		this.coords = coordinates;
	}


	/**
	 * Creation timestamp.
	 */
	public long getTimestamp()
	{
		return this.timestamp;
	}


	/**
	 * Creation timestamp.
	 */
	public void setTimestamp(final long timestamp)
	{
		this.timestamp = timestamp;
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Position [coordinates=" + this.coords + ", timestamp=" + this.timestamp + "]";
	}
	
}
