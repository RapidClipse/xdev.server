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

package com.xdev.charts.config;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class Tooltip
{
	private boolean	isHtml	= false;
	private String	trigger	= "focus";
	
	
	public boolean getisHtml()
	{
		return this.isHtml;
	}
	
	
	public void setisHtml(final boolean isHtml)
	{
		this.isHtml = isHtml;
	}


	/**
	 * @return the trigger
	 */
	public String getTrigger()
	{
		return this.trigger;
	}


	/**
	 * The user interaction that causes the tooltip to be displayed: <br>
	 * <ul>
	 * <li>'focus' - The tooltip will be displayed when the user hovers over the
	 * element.</li>
	 * <li>'none' - The tooltip will not be displayed.</li>
	 * <li>'selection' - The tooltip will be displayed when the user selects the
	 * element.</li>
	 * </ul>
	 * <br>
	 * 
	 * @param trigger
	 *            the trigger to set
	 */
	public void setTrigger(final String trigger)
	{
		this.trigger = trigger;
	}

}
