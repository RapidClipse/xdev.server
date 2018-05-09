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

package com.xdev.charts.pie;


import java.io.Serializable;

import com.xdev.charts.AbstractXdevChartConfig;
import com.xdev.charts.TextStyle;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 */
public class XdevPieChartConfig extends AbstractXdevChartConfig implements Serializable
{

	public final static String	PIESLICETEXT_PERCENTAGE	= "percentage";
	public final static String	PIESLICETEXT_VALUE		= "value";
	public final static String	PIESLICETEXT_LABEL		= "label";
	public final static String	PIESLICETEXT_NONE		= "none";

	private Boolean				is3D					= false;
	private Double				pieHole					= 0.0;
	private String				pieSliceText			= PIESLICETEXT_PERCENTAGE;
	private String				pieSliceBorderColor		= "white";
	private Double				sliceVisibilityThreshold;
	private String				pieResidueSliceColor	= "#ccc";
	private String				pieResidueSliceLabel	= "Other";

	private TextStyle			pieSliceTextStyle		= new TextStyle();
	
	
	public Boolean getIs3D()
	{
		return this.is3D;
	}
	
	
	public void setIs3D(final Boolean is3d)
	{
		this.is3D = is3d;
	}
	
	
	public Double getPieHole()
	{
		return this.pieHole;
	}
	
	
	public void setPieHole(final Double pieHole)
	{
		this.pieHole = pieHole;
	}
	
	
	public String getPieSliceText()
	{
		return this.pieSliceText;
	}
	
	
	/**
	 * The content of the text displayed on the slice. Can be one of the following:
	 * <br>
	 * <ul>
	 * <li>'percentage' - The percentage of the slice size out of the total.</li>
	 * <li>'value' - The quantitative value of the slice.</li>
	 * <li>'label' - The name of the slice.</li>
	 * <li>'none' - No text is displayed.</li>
	 * </ul>
	 * 
	 * @param pieSliceText
	 */
	public void setPieSliceText(final String pieSliceText)
	{
		this.pieSliceText = pieSliceText;
	}
	
	
	public String getPieSliceBorderColor()
	{
		return this.pieSliceBorderColor;
	}
	
	
	/**
	 * The color of the slice borders. Only applicable when the chart is
	 * two-dimensional. <br>
	 * Can be a simple HTML color string, for example: 'red' or '#00cc00'.
	 * 
	 * @param pieSliceBorderColor
	 */
	public void setPieSliceBorderColor(final String pieSliceBorderColor)
	{
		this.pieSliceBorderColor = pieSliceBorderColor;
	}
	
	
	public Double getSliceVisibilityThreshold()
	{
		return this.sliceVisibilityThreshold;
	}
	
	
	/**
	 * The fractional value of the pie, below which a slice will not show
	 * individually. All slices that have not passed this threshold will be combined
	 * to a single "Other" slice, whose size is the sum of all their sizes. Default
	 * is not to show individually any slice which is smaller than half a degree.
	 * 
	 * @param sliceVisibilityThreshold
	 */
	public void setSliceVisibilityThreshold(final Double sliceVisibilityThreshold)
	{
		this.sliceVisibilityThreshold = sliceVisibilityThreshold;
	}
	
	
	public String getPieResidueSliceColor()
	{
		return this.pieResidueSliceColor;
	}
	
	
	/**
	 * Color for the combination slice that holds all slices below
	 * sliceVisibilityThreshold.
	 * 
	 * @param pieResidueSliceColor
	 */
	public void setPieResidueSliceColor(final String pieResidueSliceColor)
	{
		this.pieResidueSliceColor = pieResidueSliceColor;
	}
	
	
	public String getPieResidueSliceLabel()
	{
		return this.pieResidueSliceLabel;
	}
	
	
	/**
	 * A label for the combination slice that holds all slices below
	 * sliceVisibilityThreshold.
	 * 
	 * @param pieResidueSliceLabel
	 */
	public void setPieResidueSliceLabel(final String pieResidueSliceLabel)
	{
		this.pieResidueSliceLabel = pieResidueSliceLabel;
	}
	
	
	public TextStyle getPieSliceTextStyle()
	{
		return this.pieSliceTextStyle;
	}
	
	
	/**
	 * An object that specifies the slice text style.
	 * 
	 * @param pieSliceTextStyle
	 */
	public void setPieSliceTextStyle(final TextStyle pieSliceTextStyle)
	{
		this.pieSliceTextStyle = pieSliceTextStyle;
	}
}
