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

package com.xdev.charts;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class TimelineOptions
{
	private boolean		colorByRowLabel	= false;
	private boolean		groupByRowLabel	= true;
	private TextStyle	rowLabelStyle;
	private boolean		showBarLabels	= true;
	private boolean		showRowLabels	= true;
	private String		singleColor;
	private TextStyle	barLabelStyle;
	
	
	public TextStyle getBarLabelStyle()
	{
		return this.barLabelStyle;
	}
	
	
	public void setBarLabelStyle(final TextStyle barLabelStyle)
	{
		this.barLabelStyle = barLabelStyle;
	}
	
	
	public boolean isColorByRowLabel()
	{
		return this.colorByRowLabel;
	}
	
	
	public void setColorByRowLabel(final boolean colorByRowLabel)
	{
		this.colorByRowLabel = colorByRowLabel;
	}
	
	
	public boolean isGroupByRowLabel()
	{
		return this.groupByRowLabel;
	}
	
	
	public void setGroupByRowLabel(final boolean groupByRowLabel)
	{
		this.groupByRowLabel = groupByRowLabel;
	}
	
	
	public TextStyle getRowLabelStyle()
	{
		return this.rowLabelStyle;
	}
	
	
	public void setRowLabelStyle(final TextStyle rowLabelStyle)
	{
		this.rowLabelStyle = rowLabelStyle;
	}
	
	
	public boolean isShowBarLabels()
	{
		return this.showBarLabels;
	}
	
	
	public void setShowBarLabels(final boolean showBarLabels)
	{
		this.showBarLabels = showBarLabels;
	}
	
	
	public boolean isShowRowLabels()
	{
		return this.showRowLabels;
	}
	
	
	public void setShowRowLabels(final boolean showRowLabels)
	{
		this.showRowLabels = showRowLabels;
	}
	
	
	public String getSingleColor()
	{
		return this.singleColor;
	}
	
	
	public void setSingleColor(final String singleColor)
	{
		this.singleColor = singleColor;
	}
}
