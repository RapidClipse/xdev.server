/*
 * Copyright (C) 2013-2015 by XDEV Software, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xdev.mobile.service.geolocation;


/**
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
	 * @return the coordinates
	 */
	public Coordinates getCoordinates()
	{
		return this.coords;
	}
	
	
	/**
	 * @param coordinates
	 *            the coordinates to set
	 */
	public void setCoordinates(final Coordinates coordinates)
	{
		this.coords = coordinates;
	}
	
	
	/**
	 * @return the timestamp
	 */
	public long getTimestamp()
	{
		return this.timestamp;
	}
	
	
	/**
	 * @param timestamp
	 *            the timestamp to set
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
