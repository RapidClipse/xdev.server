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

package com.xdev.charts.combo;


import java.io.Serializable;
import java.util.List;

import com.xdev.charts.AbstractXdevChartConfig;
import com.xdev.charts.config.Series;


/**
 *
 * @author XDEV Software (SS)
 * @since 4.0
 *
 */
public class XdevComboChartConfig extends AbstractXdevChartConfig implements Serializable
{
	private List<Series> series;
	
	
	/**
	 * @return the series
	 */
	public List<Series> getSeries()
	{
		return this.series;
	}


	/**
	 * @param series
	 *            the series to set
	 */
	public void setSeries(final List<Series> series)
	{
		this.series = series;
	}
}
