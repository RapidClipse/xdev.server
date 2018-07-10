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


import java.util.ArrayList;
import java.util.List;


/**
 * @author XDEV Software
 *
 */
public class XdevChartUtils
{
	public static void setHAxisScaling(final HAxis hAxis, final Double range, final Double min,
			final Double max)
	{
		final List<Ticks> list = new ArrayList<>();

		for(Double i = min; i <= max; i = i + range)
		{
			final Ticks ticks = new Ticks();
			ticks.setV(i);
			list.add(ticks);
		}
		
		hAxis.setTicks(list);
	}
	
	
	public static void setVAxisScaling(final VAxis vAxis, final Integer range, final Integer min,
			final Integer max)
	{
		final List<Integer> list = new ArrayList<>();
		
		for(Integer i = min; i <= max; i = i + range)
		{
			list.add(i);
		}

		vAxis.setTicks(list);
	}
}
