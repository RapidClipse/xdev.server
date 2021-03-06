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

package com.xdev.charts.timeline;


import java.io.Serializable;

import com.xdev.charts.TimelineOptions;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class XdevTimelineChartConfig implements Serializable
{
	private TimelineOptions	timeline					= new TimelineOptions();
	private boolean			avoidOverlappingGridLines	= true;


	public TimelineOptions getTimeline()
	{
		return this.timeline;
	}
	
	
	/**
	 * An 'TimelineOptions' object that specifies the default Timeline-Chart style.
	 * <br>
	 *
	 * @param timeline
	 */
	public void setTimeline(final TimelineOptions timeline)
	{
		this.timeline = timeline;
	}
	
	
	/**
	 * @return the avoidOverlappingGridLines
	 */
	public boolean isAvoidOverlappingGridLines()
	{
		return this.avoidOverlappingGridLines;
	}
	
	
	/**
	 * Whether display elements (e.g., the bars in a timeline) should obscure grid
	 * lines. If false, grid lines may be covered completely by display elements. If
	 * true, display elements may be altered to keep grid lines visible. <br>
	 *
	 * @param avoidOverlappingGridLines
	 */
	public void setAvoidOverlappingGridLines(final boolean avoidOverlappingGridLines)
	{
		this.avoidOverlappingGridLines = avoidOverlappingGridLines;
	}
	
}
