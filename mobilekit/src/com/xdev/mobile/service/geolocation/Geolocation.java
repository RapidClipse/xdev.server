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
public class Geolocation
{
	private Position	position;
	private double		watchID;


	public Geolocation()
	{
		
	}


	/**
	 * @param position
	 * @param watchID
	 */
	public Geolocation(final Position position, final double watchID)
	{
		super();
		this.position = position;
		this.watchID = watchID;
	}
	
	
	/**
	 * @return the position
	 */
	public Position getPosition()
	{
		return this.position;
	}


	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(final Position position)
	{
		this.position = position;
	}


	/**
	 * @return the watchID
	 */
	public double getWatchID()
	{
		return this.watchID;
	}


	/**
	 * @param watchID
	 *            the watchID to set
	 */
	public void setWatchID(final double watchID)
	{
		this.watchID = watchID;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Geolocation [position=" + this.position + ", watchID=" + this.watchID + "]";
	}
}
