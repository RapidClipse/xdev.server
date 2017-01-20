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
 *
 * @author XDEV Software
 *
 */
public class HeadingWatch
{
	private final Heading	heading;
	private final String	watchID;
	
	
	HeadingWatch(final Heading heading, final String watchID)
	{
		this.heading = heading;
		this.watchID = watchID;
	}
	
	
	/**
	 * @return the heading
	 */
	public Heading getHeading()
	{
		return this.heading;
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
	 * CompassService.getInstance().clearWatch(headingWatch.getWatchID());
	 * }
	 */
	public void clear()
	{
		CompassService.getInstance().clearWatch(this.watchID);
	}
	
	
	@Override
	public String toString()
	{
		return "HeadingWatch [heading=" + this.heading + ", watchID=" + this.watchID + "]";
	}
}
